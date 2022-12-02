package org.lxp.java8;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StudyCompletableFutureTest {
    private final int ROUND = 1000;

    @Test
    public void testExecutorService() throws Exception {
        Pair<Long, List<String>> pair = StudyCompletableFuture.executorService();
        System.out.println("executorService:" + pair.getLeft());
        Assert.assertEquals(StudyCompletableFuture.MAX_SIZE, pair.getRight().size());
    }

    @Test
    public void testCompletableFutureSupplyAsync() throws Exception {
        Pair<Long, List<String>> pair = StudyCompletableFuture.completableFutureSupplyAsync();
        System.out.println("completableFutureSupplyAsync:" + pair.getLeft());
        Assert.assertEquals(StudyCompletableFuture.MAX_SIZE, pair.getRight().size());
    }

    @Test
    public void testCompletableFutureSupplyAsyncWithExecutorService() throws Exception {
        Pair<Long, List<String>> pair = StudyCompletableFuture.completableFutureSupplyAsyncWithExecutorService();
        System.out.println("completableFutureSupplyAsyncWithExecutorService:" + pair.getLeft());
        Assert.assertEquals(StudyCompletableFuture.MAX_SIZE, pair.getRight().size());
    }

    @Test
    public void testCompletableFutureRunAsync() throws Exception {
        Pair<Long, List<String>> pair = StudyCompletableFuture.completableFutureRunAsync();
        System.out.println("completableFutureRunAsync:" + pair.getLeft());
        Assert.assertEquals(StudyCompletableFuture.MAX_SIZE, pair.getRight().size());
    }

    @Test
    public void testCompletableFutureRunAsyncWithExecutorService() throws Exception {
        Pair<Long, List<String>> pair = StudyCompletableFuture.completableFutureRunAsyncWithExecutorService();
        System.out.println("completableFutureRunAsyncWithExecutorService:" + pair.getLeft());
        Assert.assertEquals(StudyCompletableFuture.MAX_SIZE, pair.getRight().size());
    }

    @Test
    public void testSupplyAsyncAndThenApply() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenApply(i -> i + 1);
        Assert.assertEquals(2, future.get().intValue());
    }

    @Test
    public void testSupplyAsyncAndThenCompose() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenCompose(i -> CompletableFuture.supplyAsync(() -> i + 2));
        Assert.assertEquals(3, future.get().intValue());
    }

    @Test
    public void testSupplyAsyncAndAccept() throws InterruptedException, ExecutionException {
        StringBuilder sb = new StringBuilder();
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenAccept(x -> {
            sb.append(x + 3);
        }).join();
        Assert.assertEquals("4", sb.toString());
    }

    @Test
    public void notAllTasksAreDoneUnlessShutDownThreadPool() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(1, ROUND).mapToObj(index -> CompletableFuture.runAsync(() -> {
            throw new IllegalStateException(String.valueOf(counter.incrementAndGet()));
        }, executorService)).collect(Collectors.toList());
        try {
            futures.forEach(CompletableFuture::join);
        } catch (Exception e) {
            System.out.println(counter.get());
        }
        Assertions.assertThat(counter.get()).isLessThan(ROUND);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
        Assertions.assertThat(counter.get()).isEqualTo(ROUND);
    }

    @Test
    public void allTasksGetDone() {
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(1, ROUND).mapToObj(index -> CompletableFuture.runAsync(() -> {
            throw new IllegalStateException(String.valueOf(counter.incrementAndGet()));
        }, executorService)).collect(Collectors.toList());
        CompletableFuture<Void> headerFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
        try {
            headerFuture.join();
        } catch (Exception e) {
            System.out.println(counter.get());
        }
        Assertions.assertThat(counter.get()).isEqualTo(ROUND);
    }

    @Test
    public void notAllTasksAreDoneWithThreadPool() {
        AtomicInteger counter = new AtomicInteger(0);
        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(1, ROUND).mapToObj(index -> CompletableFuture.runAsync(() ->
                new Thread(() -> {
                    throw new IllegalStateException(String.valueOf(counter.incrementAndGet()));
                }, "Thread-" + index).start()
        )).collect(Collectors.toList());
        CompletableFuture<Void> headerFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
        try {
            headerFuture.join();
        } catch (Exception e) {
            System.out.println(counter.get());
        }
        Assertions.assertThat(counter.get()).isLessThan(ROUND);
    }
}
