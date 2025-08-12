package org.lxp.gradle.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.gradle.service.TestTableService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Slf4j
@Component
@RequiredArgsConstructor
public class TestScheduleTask {
    private final TestTableService testTableService;

    @Scheduled(cron = "1/5 * * * * *")
    public void scheduleTask() {
        log.info("start to call findAll");
        testTableService.findAll();
        log.info("start to call findAllSync");
        testTableService.findAllSync();
    }
}
