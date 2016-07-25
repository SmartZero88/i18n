package com.zero.i18n.compiler.source;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import com.google.common.collect.*;
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
        Map<String, SMLEntry<String>> recordMap = Maps.newHashMap();
        System.out.println("Generating started");

        for (File propertyFile : propertyFiles) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(propertyFile));
                String langCode = StringUtils.substringAfterLast(
                    FilenameUtils.getBaseName(propertyFile.getName()),
                    "_");
                String categoryName = StringUtils.substringBeforeLast(
                    FilenameUtils.getBaseName(propertyFile.getName()),
                    "_");

                Locale locale = getFromCode(langCode);
                System.out.println(
                    "Generating from Property File " + propertyFile + " - " + locale);
                for (String key : prop.stringPropertyNames()) {

                    String value = prop.getProperty(key);

                    String globalKey = globalKey(key, categoryName);
                    SMLEntry<String> existingRecord = recordMap.get(globalKey);
                    if (existingRecord == null) {
                        System.out.println("Generating new Record " + globalKey);
                        existingRecord = new SMLEntry<String>(key);
                        existingRecord.setCategory(categoryName);
                        existingRecord.setNaturalKey(key);
                        recordMap.put(globalKey, existingRecord);
                    }
                    else {
                        System.out.println("Generating found existing");

                    }

                    existingRecord.addLanguage(locale, convertText(value));
                    System.out.println(
                        "Generating from Property File " + globalKey + " => " + propertyFile + " - "
                            + locale + " " + key + " " + value + " " + existingRecord
                                .getTranslations().size());

                    if (locale == Locale.ENGLISH) {
                        existingRecord.setNaturalKey(generateNaturalKey(existingRecord));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        boolean invalid = false;
        for (SMLEntry<String> entry : recordMap.values()) {
            // wrong number of entries
            if (entry.getTranslations().size() != propertyFiles.size()) {
                System.out.println("Incomplete Translation for key " + entry.getNaturalKey());
                invalid = true;
            }
        }

        if (invalid) {
            throw new IllegalStateException("Incomplete Translation found.");
        }

        return recordMap.values();

    }

    private Locale getFromCode(String langCode) {
        if (StringUtils.isBlank(langCode)) {
            return Locale.ENGLISH;
        }
        else {
            return Locale.forLanguageTag(langCode);
        }
    }

    private String globalKey(String key, String category) {
        return category + "/" + key;
    }

    /** convert the text from mls format to a format usable by java */
    private String convertText(String string) {
        return string;
    }

    private static String generateNaturalKey(SMLEntry<String> entry) {

        String withoutSpecials = entry
            .getMessageId().replaceAll("[^a-zA-Z]", "_").replaceAll("__", "_")
            .replaceAll("^_", "").replaceAll("_$", "").toUpperCase();
        List<String> words = Arrays.asList(withoutSpecials.split("_", MAX_CONSTANT_LENGTH + 1));

        if (words.size() > MAX_CONSTANT_LENGTH) {
            words = words.subList(0, MAX_CONSTANT_LENGTH);
        }
        withoutSpecials = StringUtils.join(words, "_");

        System.out.println("NATURAL KEY " + entry.getMessageId() + " -> " + withoutSpecials);

        return withoutSpecials;

    }

}
