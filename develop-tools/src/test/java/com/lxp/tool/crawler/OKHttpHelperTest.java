package com.lxp.tool.crawler;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class OKHttpHelperTest {

    private final WireMockServer wireMockServer = new WireMockServer();

    @BeforeEach
    public void setUp() {
        wireMockServer.start();
        configureFor("127.0.0.1", 8080);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testBatchGet() {
        OKHttpHelper helper = new OKHttpHelper();
        final String crawlerUrl = "http://127.0.0.1:8080/crawler-test";
        stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        assertThat(helper.get(crawlerUrl)).isEqualTo(200);
    }
}