package org.lxp.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class MDCTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                Optional.ofNullable(contextMap).ifPresent(MDC::setContextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
