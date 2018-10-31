package com.lxp.tool.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.class)
public class LogSupplierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogSupplierTest.class);
    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    public void testGet() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<String> supplier = () -> {
            String rtn = String.valueOf(counter.incrementAndGet());
            MDC.put(RunnabeTestHelper.RUNNABLE, rtn);
            LOGGER.info("This is {} supplier.", rtn);
            return rtn;
        };

        LogSupplier<String> logSupplier = Mockito.spy(new LogSupplier<>(supplier));
        Set<String> set = new HashSet<>();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logSupplier).setLogContext();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logSupplier).clearLogContext();

        List<CompletableFuture<String>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> CompletableFuture.supplyAsync(logSupplier, executorService)).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        Assert.assertEquals("[setLogContext, clearLogContext]", set.toString());
    }

}
