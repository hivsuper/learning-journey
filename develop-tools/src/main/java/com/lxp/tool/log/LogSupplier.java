package com.lxp.tool.log;

import java.util.function.Supplier;

public class LogSupplier<T> extends AbstractLogWrapper<Supplier<T>> implements Supplier<T> {
    public LogSupplier(Supplier<T> supplier) {
        super(supplier);
    }

    @Override
    public T get() {
        this.setLogContext();
        try {
            return getJob().get();
        } finally {
            this.clearLogContext();
        }
    }
}