package com.zero.i18n.compiler.model;

import java.util.*;

public class MLSLocales {

    public static Locale getLocale(String language) {
        if ("D".equals(language)) {
            return Locale.GERMAN;
        }
        if ("F".equals(language)) {
            return Locale.FRENCH;
        }
        if ("E".equals(language)) {
            return Locale.ENGLISH;
        }
        throw new IllegalArgumentException("Unsupported language " + language);
    }

    public static String getConstantName(Locale language) {
        if (language == Locale.GERMAN) {
            return "GERMAN";
        }
        if (language == Locale.FRENCH) {
            return "FRENCH";
        }
        if (language == Locale.ENGLISH) {
            return "ENGLISH";
        }
        throw new IllegalArgumentException("Unsupported language " + language);
    }

    public static String getSingleChar(Locale locale) {
        if (locale == Locale.GERMAN) {
            return "D";
        }
        if (locale == Locale.FRENCH) {
            return "F";
        }
        if (locale == Locale.ENGLISH) {
            return "E";
        }
        throw new IllegalArgumentException("Unsupported language " + locale);
    }

}
