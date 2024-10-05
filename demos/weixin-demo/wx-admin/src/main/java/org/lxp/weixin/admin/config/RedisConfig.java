package org.lxp.weixin.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;

@Configuration
public class RedisConfig {

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        final var mapper = new ObjectMapper();
        mapper.registerModule(new CoreJackson2Module()); // Fix "no Creators, like default construct" exception
        mapper.registerModule(new JavaTimeModule());
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}