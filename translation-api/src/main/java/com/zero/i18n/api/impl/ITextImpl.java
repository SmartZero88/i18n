package com.zero.i18n.api.impl;

import java.text.*;
import java.util.*;

import com.zero.i18n.api.*;

public final class ITextImpl implements IText {

    private final BaseTextPatternImplementation<ITextImpl> baseTextPatternImplementation;
    private final Object[] args;

    public ITextImpl(
        BaseTextPatternImplementation<ITextImpl> baseTextPatternImplementation, Object[] args) {
        this.baseTextPatternImplementation = baseTextPatternImplementation;
        this.args = args;
    }

    @Override
    public String get() {
        Locale locale = ensureSupported(baseTextPatternImplementation.getDefaultLocale());
        String translatedText = this.baseTextPatternImplementation.translations.get(locale);

        // Only format if there are arguments
        if (args.length > 0) {
            return MessageFormat.format(translatedText, args);
        }
        else {
            return translatedText;
        }
    }

    private Locale ensureSupported(Locale locale) {
        if (this.baseTextPatternImplementation.translations.containsKey(locale)) {
            return locale;
        }
        else {
            // TODO log error or handle this case properly
            return Locale.ENGLISH;
        }
    }

    @Override
    public <T> T getMetadata(Class<T> metadataType) {
        return baseTextPatternImplementation.getMetadata(metadataType);
    }
}
