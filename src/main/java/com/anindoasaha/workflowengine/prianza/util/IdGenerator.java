package com.anindoasaha.workflowengine.prianza.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class IdGenerator<T, R> {

    public static IdGenerator<String, String> IdentityGenerator =
                                                        new IdGenerator<>(n -> n + "_"
                                                                + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace(':', '_'));


    private Function<T, R> function = null;

    public IdGenerator(Function<T, R> function) {
        this.function = function;
    }

    public R generate(T t) {
        return function.apply(t);
    }
}
