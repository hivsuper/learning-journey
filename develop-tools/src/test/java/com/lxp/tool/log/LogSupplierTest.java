package com.lxp.tool.log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
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
public class LogSupplierTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogSupplierTest.class);
    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    public void testGet() throws Exception {
        Supplier<String> supplier = () -> {
            LOGGER.info("This is supplier.");
            return "1";
        };

        LogSupplier<String> logSupplier = Mockito.spy(new LogSupplier<>(supplier));
        Set<String> set = new HashSet<>();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logSupplier).setLogContext();
        Mockito.doAnswer(invocation -> set.add(invocation.getMethod().getName())).when(logSupplier).clearLogContext();

        List<CompletableFuture<String>> futures = IntStream.rangeClosed(0, 4).mapToObj(index -> {
            return CompletableFuture.supplyAsync(logSupplier, executorService);
        }).collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        Assert.assertEquals("[setLogContext, clearLogContext]", set.toString());
    }

}
