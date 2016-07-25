package com.zero.i18n.api;

public interface IText {
    /**
     * Get the (translated if possible) human readable text representation
     * 
     * @return
     */
    String get();

    /** abitrary metadata (e.g. type of message, language of message) */
    <T> T getMetadata(Class<T> metadataType);
}
