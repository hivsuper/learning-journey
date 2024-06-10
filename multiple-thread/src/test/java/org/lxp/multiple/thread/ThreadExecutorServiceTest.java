package org.lxp.multiple.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lxp.multiple.thread.task.SumTask;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Super.Li
 * @Description: 测试ExecutorService
 * @date Jul 6, 2017
 */
public class ThreadExecutorServiceTest {
    private static final String THIS_IS_SHUTDOWN_WITH_AWAIT_TERMINATION = "This is shutdownWithAwaitTermination";
    private static final int RESULT = 111;

    private static boolean submitRunnable() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> System.out.println("This is submitRunnable"));
        return future.get() == null;
    }

    private static Integer submitRunnableWithResult() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(() -> System.out.println("This is submitRunnableWithResult"), RESULT);
        return future.get();
    }

    private static Integer submitBlockCallable() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(() -> {
            System.out.println("This is submitBlockCallable");
            return RESULT;
        });
        return future.get();// 阻塞
    }

    private static boolean submitNonBlockCallable() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(() -> {
            System.out.println("This is submitNonBlockCallable");
            return RESULT;
        });
        while (!future.isDone()) {// 非阻塞
            System.out.println(new Date());
        }
        return future.isDone();
    }

    private static String shutdown() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final StringBuilder sb = new StringBuilder();
        executorService.submit(() -> {
            Thread.sleep(10000);
            sb.append("This is shutdown");
            return RESULT;
        });
        executorService.shutdown();
        return sb.toString();
    }

    private static String shutdownWithAwaitTermination() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final StringBuilder sb = new StringBuilder();
        executorService.submit(() -> {
            Thread.sleep(10000);
            sb.append(THIS_IS_SHUTDOWN_WITH_AWAIT_TERMINATION);
            return RESULT;
        });
        executorService.shutdown();
        if (executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS)) {
            return sb.toString();
        } else {
            throw new RuntimeException();
        }
    }

    private static int testForkJoinPool(List<Integer> list) throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(8);
        Future<Integer> future = forkJoinPool.submit(new SumTask(list));
        return future.get();
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        Assertions.assertTrue(submitRunnable());
        Assertions.assertEquals(RESULT, submitRunnableWithResult().intValue());
        Assertions.assertEquals(RESULT, submitBlockCallable().intValue());
        Assertions.assertTrue(submitNonBlockCallable());
        Assertions.assertTrue(shutdown().isEmpty());
        Assertions.assertEquals(THIS_IS_SHUTDOWN_WITH_AWAIT_TERMINATION, shutdownWithAwaitTermination());
        Assertions.assertEquals(10, testForkJoinPool(Arrays.asList(1, 2, 3, 4)));
        Assertions.assertEquals(49, testForkJoinPool(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        Assertions.assertEquals(60, testForkJoinPool(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)));
    }

}
