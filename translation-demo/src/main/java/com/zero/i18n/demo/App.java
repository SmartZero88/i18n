package com.zero.i18n.demo;

import java.util.*;

import com.zero.i18n.api.*;
import com.zero.i18n.api.impl.*;
import com.zero.i18n.demo.i18n.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        LocaleProviders.register(new LocaleProvider() {

            @Override
            public Locale getLocale() {
                return Locale.ENGLISH;
            }
        });
        LocaleProviders.register(new LocaleProvider() {

            @Override
            public Locale getLocale() {
                return Locale.FRENCH;
            }
        });
        System.out.println(TranslationInfomessage.TEST.get());
        System.out.println(TranslationInfomessage.TEST_PARAMS.format("Ha Noi", "boom boom").get());
    }
}
