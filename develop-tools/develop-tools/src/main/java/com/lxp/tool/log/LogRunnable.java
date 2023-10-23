package com.lxp.tool.log;

public class LogRunnable extends AbstractLogWrapper<Runnable> implements Runnable {
    public LogRunnable(Runnable runnable) {
        super(runnable);
    }

    @Override
    public void run() {
        this.setLogContext();
        try {
            getJob().run();
        } finally {
            this.clearLogContext();
        }
    }
}