package org.lxp.multiple.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Super.Li
 * @Description: 测试Thread的方法
 * @date Jul 4, 2017
 */
public class ThreadMethodsTest {
    static String SLEEP = "sleep";
    static String YIELD = "yield";
    static String JOIN = "join";
    private Map<String, ThreadMethodEnum> map = new ConcurrentHashMap<>();

    class TheadSleep implements Runnable {
        private int millis;

        public TheadSleep(int millis) {
            this.millis = millis;
        }

        @Override
        public void run() {
            long threadId = Thread.currentThread().getId();
            ThreadMethodEnum sleep = ThreadMethodEnum.SLEEP;
            sleep.init(threadId);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sleep.stop();
            map.put(sleep.getMethod(), sleep);
        }
    }

    class TheadYield implements Runnable {
        @Override
        public void run() {
            long threadId = Thread.currentThread().getId();
            ThreadMethodEnum yield = ThreadMethodEnum.YIELD;
            yield.init(threadId);
            Thread.yield();
            yield.stop();
            map.put(yield.getMethod(), yield);
        }
    }

    class TheadJoin implements Runnable {
        private Thread thread;
        private int millis = 0;

        public TheadJoin(Thread thread) {
            this.thread = thread;
        }

        public TheadJoin(Thread thread, int millis) {
            this.thread = thread;
            this.millis = millis;
        }

        @Override
        public void run() {
            try {
                long threadId = Thread.currentThread().getId();
                ThreadMethodEnum join = ThreadMethodEnum.JOIN;
                join.init(threadId);
                if (millis > 0) {
                    this.thread.join(millis);
                } else {
                    this.thread.join();
                }
                join.stop();
                map.put(join.getMethod(), join);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    final AtomicInteger interruptCount = new AtomicInteger(0);
    final int interruptIndex = 100;

    class ThreadInterrupt extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                interruptCount.incrementAndGet();
                if (interruptCount.intValue() == interruptIndex) {
                    interrupt();
                }
            }
        }
    }

    @Test
    public void testSleep() throws InterruptedException {
        int millis = 1000;
        Thread sleep = new Thread(new TheadSleep(millis));
        sleep.start();
        sleep.join();
        ThreadMethodEnum tme = map.get(SLEEP);
        // 预期sleep时间与millis相近
        Assertions.assertEquals(1, Math.round((tme.getStopTimeMillis() - tme.getStartTimeMillis()) * 1.0 / millis));
    }

    @Test
    public void testYield() throws InterruptedException {
        Thread yield = new Thread(new TheadYield());
        yield.start();
        yield.join();
        ThreadMethodEnum tme = map.get(YIELD);
        // 预期yield消耗时间几乎为0
        Assertions.assertEquals(1, Math.round(tme.getStartTimeMillis() * 1.0 / tme.getStopTimeMillis()));
    }

    @Test
    public void testJoin() throws InterruptedException {
        int millis = 1000;
        Thread sleep = new Thread(new TheadSleep(millis));
        sleep.start();
        Thread join = new Thread(new TheadJoin(sleep));
        join.start();
        join.join();
        ThreadMethodEnum tme = map.get(SLEEP);
        // 预期sleep时间与millis相近
        Assertions.assertEquals(1, Math.round((tme.getStopTimeMillis() - tme.getStartTimeMillis()) * 1.0 / millis));
        tme = map.get(JOIN);
        // 预期join阻塞时间与millis相近
        Assertions.assertEquals(1, Math.round((tme.getStopTimeMillis() - tme.getStartTimeMillis()) * 1.0 / millis));
    }

    @Test
    public void testJoinMillis() throws InterruptedException {
        int millis = 500;
        Thread sleep = new Thread(new TheadSleep(millis * 2));
        sleep.start();
        Thread join = new Thread(new TheadJoin(sleep, millis));
        join.start();
        join.join();
        ThreadMethodEnum tme = map.get(SLEEP);
        // 预期sleep不能在join阻塞结束前设置，故为null
        Assertions.assertTrue(tme == null);
        tme = map.get(JOIN);
        // 预期join阻塞时间与millis相近
        Assertions.assertEquals(1, Math.round((tme.getStopTimeMillis() - tme.getStartTimeMillis()) / millis));
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        ThreadInterrupt interrupt = new ThreadInterrupt();
        interrupt.start();
        interrupt.join();
        Assertions.assertEquals(interruptIndex, interruptCount.intValue());
    }
}

enum ThreadMethodEnum {
    SLEEP(ThreadMethodsTest.SLEEP) {
        @Override
        public void init(long threadId) {
            super.start(threadId);
        }
    },
    YIELD(ThreadMethodsTest.YIELD) {
        @Override
        public void init(long threadId) {
            super.start(threadId);
        }
    },
    JOIN(ThreadMethodsTest.JOIN) {
        @Override
        public void init(long threadId) {
            super.start(threadId);
        }
    };
    private String method;
    private long threadId;
    private long startTimeMillis;
    private long stopTimeMillis = 0;

    private ThreadMethodEnum(String method) {
        this.method = method;
    }

    public void stop() {
        this.stopTimeMillis = System.currentTimeMillis();
    }

    public String getMethod() {
        return method;
    }

    public long getThreadId() {
        return threadId;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getStopTimeMillis() {
        return stopTimeMillis;
    }

    private void start(long threadId) {
        this.threadId = threadId;
        this.startTimeMillis = System.currentTimeMillis();
    }

    public abstract void init(long threadId);
}