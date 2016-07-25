package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class TextPatternNImpl<I, A, B, C> extends BaseTextPatternImplementation<I>
    implements ITextPatternN<I, A, B, C> {

    public TextPatternNImpl(Class<? extends I> implementation, LocaleProvider localeProvider2,
        Map<Locale, String> translations, Map<Class<?>, ?> metadata) {
        super(implementation, localeProvider2, translations, metadata);
    }

    @Override
    public I format(A arg1, B arg2, C arg3, Object... moreArgs) {
        return internalGet(toArray(arg1, arg2, arg3, moreArgs));
    }

    private Object[] toArray(A arg1, B arg2, C arg3, Object[] moreArgs) {
        Object[] result = new Object[moreArgs.length + 3];
        Object[] first = {arg1, arg2, arg3};
        System.arraycopy(first, 0, result, 0, 3);
        System.arraycopy(moreArgs, 0, result, 3, moreArgs.length);
        return result;
    }

}
