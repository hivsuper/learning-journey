package com.lxp.tool.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RegexPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class JEP321HttpClientHelperTest {
    public static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=UTF-8";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String RESULT = "hahaha";
    private final String crawlerUrl = "http://127.0.0.1:8080/crawler-test";
    private final int maxTotalConnection = 10;
    @Rule
    public WireMockRule forecastIoService = new WireMockRule(8080);

    @Before
    public void setUp() {
        stubFor(get(new UrlPattern(new RegexPattern("/crawler-test(.*)"), true))
                .withHeader(CONTENT_TYPE, containing(TEXT_PLAIN_CHARSET_UTF_8))
                .willReturn(ok(RESULT).withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
    }

    @Test
    public void testPost() throws URISyntaxException, IOException, InterruptedException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(String.valueOf(maxTotalConnection));
        final String requestBody = "lalala";
        stubFor(post(urlEqualTo("/crawler-test"))
                .withHeader(CONTENT_TYPE, containing(TEXT_PLAIN_CHARSET_UTF_8))
                .withRequestBody(containing(requestBody))
                .willReturn(ok(RESULT).withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        assertThat(helper.post(crawlerUrl, requestBody, CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)).isEqualTo(RESULT);
    }

    @Test
    public void testGet() throws URISyntaxException, IOException, InterruptedException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(String.valueOf(maxTotalConnection));
        assertThat(helper.get(crawlerUrl.concat("?p1=123"), CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)).isEqualTo(RESULT);
    }

    @Test
    public void getWithThreadPool() throws URISyntaxException, IOException, InterruptedException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(maxTotalConnection);
        assertThat(helper.getWithThreadPool(crawlerUrl, CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)).isEqualTo(RESULT);
    }

    @Test
    public void getAsync() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(maxTotalConnection);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(crawlerUrl))
                .headers(CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)
                .GET().build();
        assertThat(helper.getAsync(request, helper.getHttpClientWithTimeout(1), 5)).isEqualTo(RESULT);
    }

    @Test
    public void getAsyncThrowExceptionWhenTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(maxTotalConnection);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://10.255.255.1")).GET().build();
        String response = helper.getAsync(httpRequest, helper.getHttpClientWithTimeout(1), 5);
        assertThat(response).contains("timed out");
    }

    @Test
    public void getHttpClientWithTimeout() {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(maxTotalConnection);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://10.255.255.1")).GET().build();
        HttpClient httpClient = helper.getHttpClientWithTimeout(1);
        HttpConnectTimeoutException thrown = assertThrows(
                "Expected send() to throw HttpConnectTimeoutException, but it didn't",
                HttpConnectTimeoutException.class,
                () -> httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()));
        assertThat(thrown.getMessage()).contains("timed out");
    }

    @Test
    public void sendAsyncWithStringSubscriber() throws URISyntaxException {
        JEP321HttpClientHelper helper = new JEP321HttpClientHelper(maxTotalConnection);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(crawlerUrl))
                .headers(CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8)
                .GET().build();
        assertThat(helper.sendAsyncWithStringSubscriber(request, helper.getHttpClientWithTimeout(1))).isEqualTo(RESULT);
    }
}