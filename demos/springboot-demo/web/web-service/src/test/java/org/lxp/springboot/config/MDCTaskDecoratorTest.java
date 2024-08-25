package org.lxp.springboot.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
public class MDCTaskDecoratorTest {
    private static final String MAIN = "main";
    private static final String SUB = "sub";
    private static final String VALUE = "value";
    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setUp() {
        countDownLatch = new CountDownLatch(1);
        MDC.clear();
    }

    @AfterEach
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void decorateWhenMainThreadHasContext() {
        MDC.put(MAIN, VALUE);

        threadPoolTaskExecutor().execute(getRunnable());
        await().until(() -> countDownLatch.getCount() == 0);

        assertThat(MDC.get(MAIN)).isEqualTo(VALUE);
        assertThat(MDC.get(SUB)).isNull();
    }

    @Test
    public void decorateWhenMainThreadHasNoContext() {
        threadPoolTaskExecutor().execute(getRunnable());
        await().until(() -> countDownLatch.getCount() == 0);

        assertThat(MDC.get(MAIN)).isNull();
        assertThat(MDC.get(SUB)).isNull();
        assertThat(MDC.getCopyOfContextMap()).isNull();
    }

    private Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    private Runnable getRunnable() {
        return () -> {
            MDC.put(MAIN, "runnable");
            countDownLatch.countDown();
        };
    }
}