package org.lxp.gradle.schedule;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.eq;

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

        Assertions.assertThat(executor).isNotNull();
        executor.execute(() -> {
            logger.info("{}, {}", Thread.currentThread().getName(), MDC.get(key));
            countDownLatch.countDown();
        });
        Awaitility.await().until(() -> countDownLatch.getCount() == 0);

        final var argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(logger, Mockito.times(1))
                .info(eq("{}, {}"), argumentCaptor.capture(), eq(value));
        Assertions.assertThat(argumentCaptor.getValue()).startsWith("async-exec-");
    }

    @Test
    void getAsyncUncaughtExceptionHandler() {
        Assertions.assertThat(new AsyncConfig().getAsyncUncaughtExceptionHandler())
                .isInstanceOf(CustomAsyncExceptionHandler.class);
    }
}