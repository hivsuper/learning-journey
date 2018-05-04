package com.lxp.tool.log;

import java.util.Map;

import org.slf4j.MDC;

public class AbstractLogWrapper<T> {
    private final T job;
    private final Map<?, ?> context;

    public AbstractLogWrapper(T t) {
        this.job = t;
        this.context = MDC.getCopyOfContextMap();
    }

    public void setLogContext() {
        if (this.context != null) {
            MDC.setContextMap(this.context);
        }
    }

    public void clearLogContext() {
        MDC.clear();
    }

    public T getJob() {
        return this.job;
    }
}
