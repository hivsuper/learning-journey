package org.lxp.jpa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.zalando.logbook.HttpRequest;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class LogBookConfig {
    /**
     * Override org.zalando.logbook.autoconfigure.LogbookAutoConfiguration#requestCondition
     *
     * @return
     */
    @Bean
    public Predicate<HttpRequest> requestCondition() {
        return ($) -> Objects.isNull($.getContentType()) ||
                Stream.of(MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .noneMatch(type -> {
                            String contentType = $.getContentType();
                            log.debug("contentType=>{}", contentType);
                            return !StringUtils.commaDelimitedListToSet(contentType).contains(type);
                        });
    }
}
