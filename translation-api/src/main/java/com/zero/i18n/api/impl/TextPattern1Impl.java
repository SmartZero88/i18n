package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class TextPattern1Impl<I, A> extends BaseTextPatternImplementation<I>
    implements ITextPattern1<I, A> {

    public TextPattern1Impl(
        Class<? extends I> implementation, LocaleProvider defaultLocale,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        super(implementation, defaultLocale, translations, metadata);
    }

    @Override
    public I format(A arg1) {
        return internalGet(arg1);
    }

}
