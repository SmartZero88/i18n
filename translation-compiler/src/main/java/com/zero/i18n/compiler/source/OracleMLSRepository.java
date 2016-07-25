package com.zero.i18n.compiler.source;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.*;

import com.google.common.collect.*;
import com.zero.i18n.compiler.compiler.*;
import com.zero.i18n.compiler.model.*;

public class OracleMLSRepository implements ITranslationRepository<Long> {

    private static final int MAX_CONSTANT_LENGTH = 6;
    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private Properties keyMapping;
    private List<String> categories;

    public OracleMLSRepository(
        String dbUrl, String dbUser, String dbPass, List<String> categories,
        Properties keyMapping) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.categories = categories;
        this.keyMapping = keyMapping;
    }

    @Override
    public Collection<SMLEntry<Long>> loadRecords() {

        // Static Driver loading
        // new OracleDriver();

        Map<Long, SMLEntry<Long>> recordMap = Maps.newHashMap();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {

            PreparedStatement statement = connection
                .prepareStatement(
                    "select MSGA_TEXT, MSGTX_MSG_IDNR, MSGTX_MSGL_SHORT, MSGTX_TEXT, MSG_MSGT_TYPE "
                        + "from MSGTX, MSG, MSGA where MSG_IDNR = MSGTX_MSG_IDNR and MSGA_OH_IDNR = MSG_OH_IDNR order by MSGTX_MSG_IDNR");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String category = result.getString(1);
                long messageId = result.getLong(2);
                String language = result.getString(3);
                String text = result.getString(4);
                String messageType = result.getString(5);

                if (categories != null && categories.size() > 0) {
                    if (!categories.contains(category)) {
                        continue;
                    }
                }

                SMLEntry<Long> existingRecord = recordMap.get(messageId);
                if (existingRecord == null) {
                    existingRecord = new SMLEntry<Long>(messageId);
                    existingRecord.setCategory(category);
                    existingRecord.setMessageType(messageType);
                    existingRecord.setNaturalKey("MLS" + messageId); // This is the fallback, will
                                                                     // be overridden by mapping
                                                                     // once english langugage is
                                                                     // present
                    recordMap.put(messageId, existingRecord);
                }

                existingRecord.addLanguage(MLSLocales.getLocale(language), convertText(text));

                if (MLSLocales.getLocale(language) == Locale.ENGLISH) {
                    existingRecord.setNaturalKey(findNaturalKey(existingRecord));
                }

            }

            return recordMap.values();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** convert the text from mls format to a format usable by java */
    private String convertText(String string) {

        string = StringUtils.trimToEmpty(string);
        for (int i = 0; i < 99; i++) {
            String search = String.format("&%02d", i + 1);
            String replace = "{" + i + "}";

            string = StringUtils.replace(string, search, replace);
        }

        return string;
    }

    private String findNaturalKey(SMLEntry<Long> existingRecord) {
        String key = existingRecord.getMessageId() + "";
        String value = keyMapping.getProperty(key);
        if (value == null) {
            value = generateNaturalKey(existingRecord);
            keyMapping.setProperty(key, value);
        }

        // _ at start only when value begins with a number
        value = StringUtils.removeStart(value, "_");
        if (StringUtils.isNumeric(value.substring(0, 1))) {
            value = "_" + value;
            keyMapping.setProperty(key, value);
        }

        System.out.println("NATURAL KEY " + key + " -> " + value);

        return value;
    }

    private static String generateNaturalKey(SMLEntry<Long> entry) {
        String englishTranslation = entry.getTranslations().get(Locale.ENGLISH);
        if (englishTranslation == null) {
            System.err.println("Translation not found for key " + entry.getMessageId());
            return "UNKNOWN_" + entry.getMessageId();
        }
        else {
            String withoutSpecials = englishTranslation
                .replaceAll("[^a-zA-Z]", "_").replaceAll("__", "_")
                .replaceAll("^_", "").replaceAll("_$", "").toUpperCase();
            List<String> words = Arrays.asList(withoutSpecials.split("_", MAX_CONSTANT_LENGTH + 1));

            if (words.size() > MAX_CONSTANT_LENGTH) {
                words = words.subList(0, MAX_CONSTANT_LENGTH);
            }
            withoutSpecials = StringUtils.join(words, "_");

            System.out.println("NATURAL KEY " + englishTranslation + " -> " + withoutSpecials);

            withoutSpecials += "_" + entry.getMessageId();
            return withoutSpecials;

        }
    }
}
