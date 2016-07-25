package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

public class SMLBuilder<I> implements ISMLBuilder<I> {

    private final Map<Locale, String> messages = new HashMap<>();
    private LocaleProvider localeProvider = LocaleProviders.PROXY;
    private Map<Class<?>, Object> metadata = new HashMap<>();
    private Class<? extends I> implementation;

    public SMLBuilder(Class<? extends I> class1) {
        this.implementation = class1;
    }

    public ISMLBuilder<I> translation(Locale locale, String string) {
        return this;
    }

    public static SMLBuilder<IText> create() {
        return new SMLBuilder<IText>(ITextImpl.class);
    }

    public static <I> SMLBuilder<I> create(Class<I> interf, Class<? extends I> implementation) {
        return new SMLBuilder<I>(implementation);
    }

    @Override
    public ISMLBuilder<I> withMessage(Locale locale, String message) {
        this.messages.put(locale, message);
        return this;
    }

    @Override
    public <T> SMLBuilder<I> withMetadata(T metadataValue) {
        this.metadata.put(metadataValue.getClass(), metadataValue);
        return this;
    }

    /** return IText directly */
    @Override
    public I build0() {
        return new TextPattern0Impl<I>(
            this.implementation, this.localeProvider, this.messages, this.metadata).get();
    }

    @Override
    public <A> ITextPattern1<I, A> build1(Class<A> class1) {
        return new TextPattern1Impl<I, A>(
            this.implementation, this.localeProvider, this.messages, this.metadata);
    }

    @Override
    public <A, B> ITextPattern2<I, A, B> build2(Class<A> class1, Class<B> class2) {
        return new TextPattern2Impl<I, A, B>(
            this.implementation, this.localeProvider, this.messages, this.metadata);
    }

    @Override
    public <A, B, C> ITextPattern3<I, A, B, C> build3(
        Class<A> class1, Class<B> class2, Class<C> class3) {
        return new TextPattern3Impl<I, A, B, C>(
            this.implementation, this.localeProvider, this.messages, this.metadata);
    }

    @Override
    public <A, B, C> ITextPatternN<I, A, B, C> buildN(
        Class<A> class1, Class<B> class2, Class<C> class3) {
        return new TextPatternNImpl<I, A, B, C>(
            this.implementation, this.localeProvider, this.messages, this.metadata);
    }
}
