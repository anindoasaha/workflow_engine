package com.anindoasaha.workflowengine.prianza.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Configuration {

    private static Injector injector = Guice.createInjector(new ServiceModule(), new RepositoryModule());

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }
}
