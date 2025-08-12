package org.lxp.gradle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.gradle.repository.TestTableRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestTableService {
    private final TestTableRepository testTableRepository;

    @Async
    public void findAll() {
        log.info("Find all in TestTable {}", testTableRepository);
    }

    public void findAllSync() {
        log.info("Find all in TestTable {}", testTableRepository);
    }
}
