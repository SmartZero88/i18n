package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class TextPattern2Impl<I, A, B> extends BaseTextPatternImplementation<I>
    implements ITextPattern2<I, A, B> {

    public TextPattern2Impl(Class<? extends I> implementation, LocaleProvider localeProvider2,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        super(implementation, localeProvider2, translations, metadata);
    }

    @Override
    public I format(A arg1, B arg2) {
        return internalGet(arg1, arg2);
    }
}
