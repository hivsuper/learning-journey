package org.lxp.gradle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.gradle.repository.TestTableRepository;
import org.lxp.gradle.schedule.MDCTaskDecorator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestTableService {
    private final TestTableRepository testTableRepository;

    @Async
    public void findAll() {
        log.info("@Async find all in TestTable {}", testTableRepository.findAll());
    }

    public void findAllSync() {
        final var decorator = new MDCTaskDecorator();
        final var runnable = decorator.decorate(() -> log.info("Find all in TestTable {}", testTableRepository.findAll()));
        CompletableFuture.runAsync(runnable);
    }
}
