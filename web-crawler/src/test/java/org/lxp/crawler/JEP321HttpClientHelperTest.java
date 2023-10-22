package org.lxp.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class JEP321HttpClientHelperTest {
    private final String crawlerUrl = "http://127.0.0.1:8080/crawler-test";
    @Rule
    public WireMockRule forecastIoService = new WireMockRule();

    @Before
    public void setUp() {
        forecastIoService.start();
    }

    @After
    public void tearDown() {
        forecastIoService.stop();
    }

    @Test
    public void testPost() throws URISyntaxException, IOException, InterruptedException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper();
        final String result = "hahaha";
        final String requestBody = "lalala";
        forecastIoService.stubFor(post(urlEqualTo("/crawler-test"))
                .withHeader("Content-Type", containing("text/plain;charset=UTF-8"))
                .withRequestBody(containing(requestBody))
                .willReturn(ok(result).withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        assertThat(helper.post(crawlerUrl, requestBody, "Content-Type", "text/plain;charset=UTF-8")).isEqualTo(result);
    }

    @Test
    public void testGet() throws URISyntaxException, IOException, InterruptedException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper();
        final String result = "hahaha";
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test".concat("?p1=123")))
                .withHeader("Content-Type", containing("text/plain;charset=UTF-8"))
                .willReturn(ok(result).withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        assertThat(helper.get(crawlerUrl.concat("?p1=123"), "Content-Type", "text/plain;charset=UTF-8")).isEqualTo(result);
    }
}