package com.lxp.tool.log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class LogAndCatchExceptionRunnableTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAndCatchExceptionRunnableTest.class);
    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    public void testRun() {
        try {
            MDC.put("method", "testRun");
            LOGGER.info("test start");
            LogAndCatchExceptionRunnable logRunnable = spy(new LogAndCatchExceptionRunnable(RunnabeTestHelper.getRunnable()));
            Set<String> set = new HashSet<>();
            doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logRunnable).setLogContext();
            doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logRunnable).clearLogContext();

            List<CompletableFuture<Void>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> CompletableFuture.runAsync(logRunnable, executorService)).collect(Collectors.toList());
            futures.forEach(CompletableFuture::join);
            assertEquals("[setLogContext, clearLogContext]", set.toString());
            LOGGER.info("test finish");
        } finally {
            MDC.clear();
        }
    }

    @Test
    public void testRunnableWithoutCatchException() {
        AtomicInteger counter = new AtomicInteger(0);
        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> CompletableFuture.runAsync(new LogAndCatchExceptionRunnable(RunnabeTestHelper.getRunnable(counter)), executorService)).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        assertEquals(5, counter.get());
    }

    @Test
    public void testRunnableWithCatchException() {
        AtomicInteger counter = new AtomicInteger(0);
        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> CompletableFuture.runAsync(new LogAndCatchExceptionRunnable(RunnabeTestHelper.getRunnableWithCatchException(counter)), executorService)).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        assertEquals(5, counter.get());
    }
}
