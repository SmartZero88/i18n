package com.zero.i18n.api;

import java.util.*;

public interface ISMLBuilder<I> {

    /** Abirtary metadata */
    public <T> ISMLBuilder<I> withMetadata(T metadata);

    public ISMLBuilder<I> withMessage(Locale english, String message);

    /** Build a SML with zero arguments */
    public I build0();

    public <A> ITextPattern1<I, A> build1(Class<A> class1);

    public <A, B> ITextPattern2<I, A, B> build2(Class<A> class1, Class<B> class2);

    public <A, B, C> ITextPattern3<I, A, B, C> build3(
        Class<A> class1, Class<B> class2, Class<C> class3);

    public <A, B, C> ITextPatternN<I, A, B, C> buildN(
        Class<A> class1, Class<B> class2, Class<C> class3);

}
