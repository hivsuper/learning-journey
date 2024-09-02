package org.lxp.springboot.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AsyncConfigTest {
    @Test
    void getAsyncExecutor() {
        final var key = "key";
        final var value = "value";
        final var asyncConfig = new AsyncConfig();
        final var executor = asyncConfig.getAsyncExecutor();
        MDC.put(key, value);
        final var countDownLatch = new CountDownLatch(1);
        final var logger = Mockito.mock(Logger.class);

        assertThat(executor).isNotNull();
        executor.execute(() -> {
            logger.info("{},{}", Thread.currentThread().getName(), MDC.get(key));
            countDownLatch.countDown();
        });
        await().until(() -> countDownLatch.getCount() == 0);

        final var argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger, times(1)).info(eq("{},{}"), argumentCaptor.capture(), eq(value));
        assertThat(argumentCaptor.getValue()).startsWith("asyncExecutor-");
    }

    @Test
    void getAsyncUncaughtExceptionHandler() {
        assertThat(new AsyncConfig().getAsyncUncaughtExceptionHandler()).isInstanceOf(CustomAsyncExceptionHandler.class);
    }
}