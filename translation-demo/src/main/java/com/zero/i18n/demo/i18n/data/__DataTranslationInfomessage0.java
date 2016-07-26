
package com.zero.i18n.demo.i18n.data;

import java.util.Locale;
import com.zero.i18n.api.IText;
import com.zero.i18n.api.impl.SMLBuilder;

public interface __DataTranslationInfomessage0 {

    /**
     * 
     * @param en
     *     Hello world
     * @param fr
     *     Ch&agrave;o
     */
    public static IText TEST = SMLBuilder.create().
			withMessage(Locale.FRENCH, "Ch\u00e0o").
			withMessage(Locale.ENGLISH, "Hello world").
			build0();

}
