package org.lxp.weixin.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * <a href="https://blog.csdn.net/m0_56172703/article/details/133642286">spring webClient配置及使用</a>
 * <a href="https://blog.csdn.net/weixin_44266223/article/details/122967933">WebClient 连接池配置</a>
 */
@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ConnectionProvider provider = ConnectionProvider
                .builder("wx-client")
                // 等待超时时间
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                // 最大连接数
                .maxConnections(200)
                // 最大空闲时间
                .maxIdleTime(Duration.ofSeconds(5))
                // 最大等待连接数量
                .pendingAcquireMaxCount(-1)
                .build();
        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 6000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .responseTimeout(Duration.ofSeconds(6))
                .keepAlive(true)
                // connect successfully
                .doOnConnected(connection -> connection.addHandlerFirst(new ReadTimeoutHandler(6))
                        .addHandlerFirst(new ReadTimeoutHandler(6)))
                .doAfterRequest(((httpClientRequest, connection) -> {
                    connection.channel().alloc().buffer().release();
                    connection.channel().flush();
                    connection.channel().pipeline().flush();
                }));
        return WebClient.builder()
                .baseUrl("https://api.weixin.qq.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader(HttpHeaders.CONNECTION, "keep-alive")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
