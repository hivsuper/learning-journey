package org.lxp.weixin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.core.Conditions;

import java.util.function.Predicate;

@Slf4j
@Configuration
public class LogBookConfig {
    /**
     * Since org.zalando.logbook.autoconfigure.LogbookAutoConfiguration merges all predicates through
     * mergeWithExcludes(mergeWithIncludes(condition)), here override #requestCondition
     * to add exclusion predicate of content type
     *
     * @return
     */
    @Bean
    public Predicate<HttpRequest> requestCondition() {
        return Conditions.exclude(Conditions.contentType(MediaType.MULTIPART_FORM_DATA_VALUE),
                Conditions.contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
        );
    }
}
