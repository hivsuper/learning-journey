package org.lxp.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpClientHelperTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private AbstractHttpClientHelper helper;
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
    public void testBatchGet() throws IOException {
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
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
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(61000).withStatus(HttpStatus.SC_OK)));
        expectedException.expect(ExecutionException.class);
        helper = new CloseableHttpAsyncClientHelper();
        helper.get(crawlerUrl);
    }

    @Test
    public void shouldThrowExceptionWhenSocketTimeout() throws Exception {
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(61000).withStatus(HttpStatus.SC_OK)));
        expectedException.expect(SocketTimeoutException.class);
        helper = new CloseableHttpClientHelper();
        helper.get(crawlerUrl);
    }

    @Test
    @Ignore
    public void shouldThrowExceptionWhenConnectTimeout() {
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(41000).withStatus(HttpStatus.SC_OK)));
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("ConnectionPoolTimeoutException");
        helper = new CloseableHttpClientHelper();
        final int size = helper.maxTotalConnection;
        ExecutorService executorService = Executors.newFixedThreadPool(size * 10);
        IntStream.range(0, size * 10).forEach(index -> CompletableFuture.runAsync(this::execute, executorService));
        execute();
    }

    private void execute() {
        try {
            helper.get(crawlerUrl);
        } catch (Exception e) {
            if (e instanceof ConnectionPoolTimeoutException) {
                throw new RuntimeException("ConnectionPoolTimeoutException");
            } else {
                e.printStackTrace();
            }
        }
    }
}
