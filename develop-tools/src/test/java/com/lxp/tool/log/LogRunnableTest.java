package com.lxp.tool.log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class LogRunnableTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogRunnableTest.class);
    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    public void testRun() {
        Runnable runnable = () -> {
            LOGGER.info("This is runnable.");
        };

        LogRunnable logRunnable = Mockito.spy(new LogRunnable(runnable));
        Set<String> set = new HashSet<>();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logRunnable).setLogContext();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logRunnable).clearLogContext();

        List<CompletableFuture<Void>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> {
            return CompletableFuture.runAsync(logRunnable, executorService);
        }).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        Assert.assertEquals("[setLogContext, clearLogContext]", set.toString());
    }
}
