package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class TextPattern3Impl<I, A, B, C> extends BaseTextPatternImplementation<I>
    implements ITextPattern3<I, A, B, C> {

    public TextPattern3Impl(Class<? extends I> implementation, LocaleProvider localeProvider2,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        super(implementation, localeProvider2, translations, metadata);
    }

    @Override
    public I format(A arg1, B arg2, C arg3) {
        return internalGet(arg1, arg2, arg3);
    }
}
