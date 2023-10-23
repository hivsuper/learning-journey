package com.lxp.tool.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogAndCatchExceptionRunnable extends AbstractLogWrapper<Runnable> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAndCatchExceptionRunnable.class);

    public LogAndCatchExceptionRunnable(Runnable runnable) {
        super(runnable);
    }

    @Override
    public void run() {
        this.setLogContext();
        try {
            getJob().run();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            this.clearLogContext();
        }
    }
}