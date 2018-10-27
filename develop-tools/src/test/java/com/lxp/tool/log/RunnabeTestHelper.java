package com.lxp.tool.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.atomic.AtomicInteger;

class RunnabeTestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunnabeTestHelper.class);
    private static final String RUNNABLE = "runnable";

    static Runnable getRunnable() {
        return () -> {
            MDC.put(RUNNABLE, "getRunnable=".concat(String.valueOf(System.currentTimeMillis())));
            LOGGER.info("This is runnable.");
        };
    }

    static Runnable getRunnable(AtomicInteger counter) {
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            if (counter.incrementAndGet() == 2) {
                throw new NullPointerException();
            }
            LOGGER.info("This is {} runnable.", counter.get());
        };
    }

    static Runnable getRunnableWithCatchException(AtomicInteger counter) {
        return () -> {
            try {
                Thread.sleep(1000);
                if (counter.incrementAndGet() == 2) {
                    throw new NullPointerException();
                }
                LOGGER.info("This is {} runnable.", counter.get());
            } catch (Exception e) {
                LOGGER.error("error", e);
            }
        };
    }
}
