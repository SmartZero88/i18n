package com.zero.i18n.compiler.source;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import com.zero.i18n.compiler.compiler.*;
import com.zero.i18n.compiler.model.*;

public final class PropsRepository implements ITranslationRepository<String> {

    private static final int MAX_CONSTANT_LENGTH = 6;
    private final List<File> propertyFiles;

    public PropsRepository(List<File> propertyFiles) {
        this.propertyFiles = propertyFiles;
    }

    @Override
    public Collection<SMLEntry<String>> loadRecords() {
        Map<String, SMLEntry<String>> recordMap = new HashMap<>();
        System.out.println("Generating started");

        for (File propertyFile : this.propertyFiles) {
            try {
                Properties prop = new Properties();
                prop.load(new FileInputStream(propertyFile));
                String baseName = FilenameUtils.getBaseName(propertyFile.getName());
                String langCode = StringUtils.substringAfterLast(baseName, "_");
                String categoryName = StringUtils.substringBeforeLast(baseName, "_");

                Locale locale = this.getFromCode(langCode);
                System.out.println(
                    "Generating from Property File " + propertyFile + " - " + locale);
                for (String key : prop.stringPropertyNames()) {
                    String value = prop.getProperty(key);
                    String globalKey = this.globalKey(key, categoryName);
                    SMLEntry<String> existingRecord = recordMap.get(globalKey);
                    if (existingRecord == null) {
                        System.out.println("Generating new Record " + globalKey);
                        existingRecord = new SMLEntry<>(key);
                        existingRecord.setCategory(categoryName);
                        existingRecord.setNaturalKey(key);
                        recordMap.put(globalKey, existingRecord);
                    } else {
                        throw new IllegalStateException(
                            "Incomplete Translation: Duplicate '" + globalKey + "'");
                    }

                    existingRecord.addLanguage(locale, value);
                    System.out.println(
                        "Generating from Property File "
                            + globalKey + " => " + propertyFile + " - " + locale + " " + key + " "
                            + value + " " + existingRecord.getTranslations().size());

                    if (locale == Locale.ENGLISH) {
                        existingRecord.setNaturalKey(generateNaturalKey(existingRecord));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return recordMap.values();
    }

    private Locale getFromCode(String langCode) {
        if (StringUtils.isBlank(langCode)) {
            return Locale.ENGLISH;
        } else {
            return Locale.forLanguageTag(langCode);
        }
    }

    private String globalKey(String key, String category) {
        return category + "/" + key;
    }

    private static String generateNaturalKey(SMLEntry<String> entry) {

        String withoutSpecials = entry
            .getMessageId().replaceAll("[^a-zA-Z]", "_").replaceAll("__", "_").replaceAll("^_", "")
            .replaceAll("_$", "").toUpperCase();
        List<String> words = Arrays.asList(withoutSpecials.split("_", MAX_CONSTANT_LENGTH + 1));

        if (words.size() > MAX_CONSTANT_LENGTH) {
            words = words.subList(0, MAX_CONSTANT_LENGTH);
        }
        withoutSpecials = StringUtils.join(words, "_");

        System.out.println("NATURAL KEY " + entry.getMessageId() + " -> " + withoutSpecials);

        return withoutSpecials;

    }

}
