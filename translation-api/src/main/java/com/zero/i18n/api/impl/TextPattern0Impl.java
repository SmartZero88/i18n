package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class TextPattern0Impl<I> extends BaseTextPatternImplementation<I> {

    public TextPattern0Impl(Class<? extends I> implementation, LocaleProvider localeProvider,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        super(implementation, localeProvider, translations, metadata);
    }

    public I get() {
        return internalGet();
    }
}
