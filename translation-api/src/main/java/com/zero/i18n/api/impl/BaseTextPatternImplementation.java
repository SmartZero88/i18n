package com.zero.i18n.api.impl;

import java.lang.reflect.*;
import java.util.*;

import com.zero.i18n.api.*;

public class BaseTextPatternImplementation<I> {

    public Map<Class<?>, ?> getMetadata() {
        return this.metadata;
    }

    Map<Locale, String> translations;
    private final LocaleProvider localeProvider;
    private final Map<Class<?>, ?> metadata;
    private final Class<? extends I> implementation;

    public BaseTextPatternImplementation(
        Class<? extends I> implementation, LocaleProvider localeProvider2,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        this.implementation = implementation;
        this.localeProvider = localeProvider2;
        this.translations = translations;
        this.metadata = metadata;
    }

    public Locale getDefaultLocale() {
        return this.localeProvider.getLocale();
    }

    protected I internalGet(Object... args) {
        try {
            return this.implementation
                .getConstructor(BaseTextPatternImplementation.class, Object[].class).newInstance(
                    this, args);
        } catch (
            InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new LinkageError("Translation not correctly generated", e);
        }
    }

    public Map<Locale, String> getTranslations() {
        return this.translations;
    }

    public <T> T getMetadata(Class<T> metadataType) {
        return (T) this.metadata.get(metadataType);
    }

}
