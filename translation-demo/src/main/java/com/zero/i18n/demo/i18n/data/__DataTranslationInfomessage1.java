
package com.zero.i18n.demo.i18n.data;

import java.util.Locale;
import com.zero.i18n.api.IText;
import com.zero.i18n.api.ITextPattern2;
import com.zero.i18n.api.impl.SMLBuilder;

public interface __DataTranslationInfomessage1 {

    /**
     * 
     * @param en
     *     Hello world {0}, {1}!!!
     * @param fr
     *     Ch&agrave;o {0}, {1}!!!
     */
    public static ITextPattern2 <IText, Object, Object> TEST_PARAMS = SMLBuilder.create().
			withMessage(Locale.FRENCH, "Ch\u00e0o {0}, {1}!!!").
			withMessage(Locale.ENGLISH, "Hello world {0}, {1}!!!").
			build2(Object.class, Object.class);

}
