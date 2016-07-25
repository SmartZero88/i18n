package com.zero.i18n.compiler.model;

import java.util.*;

import org.apache.commons.lang3.builder.*;

import com.google.common.collect.*;

/** Implementation agnostic Translation Entry */
public class SMLEntry<ID> {

    private ID messageId;
    private String category;
    private Map<Locale, String> translations = Maps.newHashMap();
    private String messageType = "-";
    private String naturalKey;

    public SMLEntry(ID messageId) {
        this.messageId = messageId;
    }

    public void setNaturalKey(String key) {
        this.naturalKey = key;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public ID getMessageId() {
        return messageId;
    }

    public void addLanguage(Locale language, String text) {
        translations.put(language, text);
    }

    public Map<Locale, String> getTranslations() {
        return translations;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
    }

}
