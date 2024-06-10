package org.lxp.multiple.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author Super.Li
 * @Description: 规定线程次序的方法
 * @date Jul 5, 2017
 */
public class ThreadOrderTest {
    private long millisUnit = 1000;
    private int count = 2;

    class ThreadOrder {
        /**
         * join方法使多个线程依次执行
         *
         * @return
         * @throws InterruptedException
         */
        public long preserveOrderViaJoin() throws InterruptedException {
            long startMillis = System.currentTimeMillis();
            Thread tmp;
            for (int i = 0; i < count; i++) {
                tmp = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(millisUnit);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "join-" + i);
                tmp.start();
                tmp.join();// 不停地检测线程是否执行完成，执行完成才继续往下
            }
            return System.currentTimeMillis() - startMillis;
        }

        /**
         * CountdownLatch可同时阻塞多个线程，但它们可并发执行
         *
         * @return
         * @throws InterruptedException
         */
        public long preserveOrderViaCountdownLatch() throws InterruptedException {
            long startMillis = System.currentTimeMillis();
            final CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(millisUnit);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        countDownLatch.countDown();// 只要计数器清零，等待的线程就可以开始执行，于是可以达到并发的效果
                    }
                }, "countDownLatch-" + i).start();
            }
            countDownLatch.await();
            return System.currentTimeMillis() - startMillis;
        }
    }

    @Test
    public void testPreserveOrderViaJoin() throws InterruptedException {
        ThreadOrder threadOrder = new ThreadOrder();
        Assertions.assertEquals(count, threadOrder.preserveOrderViaJoin() / millisUnit);
    }

    @Test
    public void testPreserveOrderViaCountdownLatch() throws InterruptedException {
        ThreadOrder threadOrder = new ThreadOrder();
        Assertions.assertEquals(1, threadOrder.preserveOrderViaCountdownLatch() / millisUnit);
    }
}