package com.zero.i18n.api.impl;

import java.util.*;

import com.zero.i18n.api.*;

/**
 * Global level point to register LocaleProviders. on startup of your
 * application, call LocaleProviders.register(myProvider) to register this.
 * Access is static and your localeprovider needs to use an internal way
 * (threadlocal etc.) to find the correct locale.
 */
public class LocaleProviders {

    /**
     * Create a proxy, so that every call to LocaleProvider:get goes through
     * this class
     */
    private static final class LocaleProviderProxy implements LocaleProvider {
        @Override
        public Locale getLocale() {
            return get().getLocale();
        }
    }

    public static final LocaleProvider PROXY = new LocaleProviderProxy();

    private static LocaleProvider localeProvider;

    public static void register(LocaleProvider localeProvider) {
        LocaleProviders.localeProvider = localeProvider;
    }

    public static LocaleProvider get() {
        if (localeProvider == null) {
            throw new IllegalStateException("No LocaleProvider set");
        }
        return localeProvider;
    }
}
