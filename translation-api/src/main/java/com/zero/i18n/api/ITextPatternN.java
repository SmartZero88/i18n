package com.zero.i18n.api;

/** three arguments or more */
public interface ITextPatternN<I, A, B, C> {

    public I format(A arg1, B arg2, C arg3, Object... moreArgs);
}
