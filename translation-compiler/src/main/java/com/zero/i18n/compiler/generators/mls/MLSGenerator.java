package com.zero.i18n.compiler.generators.mls;

import java.util.*;
import java.util.Map.*;

import org.apache.commons.lang3.*;

import com.zero.i18n.compiler.model.*;

public class MLSGenerator {

    // 00000030DCInformation
    public <T> String format(SMLEntry<T> record) {
        String f = "";
        for (Entry<Locale, String> text : record.getTranslations().entrySet()) {
            Locale locale = text.getKey();
            f += String.format("%08d", record.getMessageId()) + MLSLocales.getSingleChar(locale)
                + record.getMessageType() + formatText(text.getValue()) + "\r\n";
        }
        return f;
    }

    private String formatText(String string) {
        // apply any hacks
        string = StringUtils.replace(string, "\r\n", "\\");
        string = StringUtils.replace(string, "\n", "\\");
        string = StringUtils.replace(string, "\u2026", "...");

        if (string == null || string.length() == 0)
            string = " ";
        return string;
    }

}
