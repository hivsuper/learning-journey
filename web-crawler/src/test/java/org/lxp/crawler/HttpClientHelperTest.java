package org.lxp.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class HttpClientHelperTest {
    private AbstractHttpClientHelper helper;
    private final String crawlerUrl = "http://127.0.0.1:8080/crawler-test";

    @Rule
    public WireMockRule forecastIoService = new WireMockRule(8080);

    @Test
    public void testBatchGet() throws IOException {
        stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        // given
        List<String> urls = IntStream.range(0, 100)
                .mapToObj(index -> crawlerUrl)
                .collect(Collectors.toList());
        // execute
        StopWatch stopWatch = new StopWatch();
        helper = new CloseableHttpAsyncClientHelper();
        stopWatch.start();
        List<Integer> statusCodesAsyncBatch = helper.batchGet(urls);
        stopWatch.stop();
        long costTimeAsyncBatch = stopWatch.getLastTaskTimeMillis();

        helper = new CloseableHttpClientHelper();
        stopWatch.start();
        List<Integer> statusCodesBatch = helper.batchGet(urls);
        stopWatch.stop();
        long costTimeBatch = stopWatch.getLastTaskTimeMillis();

        // verify
        assertThat(statusCodesBatch).containsExactlyElementsOf(statusCodesAsyncBatch);
        assertThat(costTimeBatch).isGreaterThan(costTimeAsyncBatch);
    }

    @Test
    public void shouldThrowExceptionWhenAsyncSocketTimeout() throws Exception {
        stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(6100).withStatus(HttpStatus.SC_OK)));
        helper = new CloseableHttpAsyncClientHelper();
        assertThrows(ExecutionException.class, () -> helper.get(crawlerUrl));
    }

    @Test
    public void shouldThrowExceptionWhenSocketTimeout() {
        stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(6100).withStatus(HttpStatus.SC_OK)));
        helper = new CloseableHttpClientHelper();
        assertThrows(SocketTimeoutException.class, () -> helper.get(crawlerUrl));
    }

    @Test
    public void shouldThrowExceptionWhenConnectTimeout() {
        stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(6100).withStatus(HttpStatus.SC_OK)));
        stubFor(get(urlEqualTo("/crawler-test2")).willReturn(aResponse().withFixedDelay(1).withStatus(HttpStatus.SC_OK)));
        helper = new CloseableHttpClientHelper();
        final int size = helper.maxTotalConnection;
        ExecutorService executorService = Executors.newFixedThreadPool(size * 10);
        IntStream.range(0, size * 10).forEach(index -> CompletableFuture.runAsync(() -> {
            try {
                helper.get(crawlerUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService));
        assertThrows(ConnectionPoolTimeoutException.class, () -> helper.get("http://127.0.0.1:8080/crawler-test2"));
    }
}
