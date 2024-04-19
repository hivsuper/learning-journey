package com.lxp.tool.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CloseableHttpClientHelper extends AbstractHttpClientHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseableHttpClientHelper.class);
    private final HttpClientBuilder httpClientBuilder;

    public CloseableHttpClientHelper() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxTotalConnection);
        connManager.setDefaultMaxPerRoute(maxTotalConnection);
        httpClientBuilder = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig);
    }

    @Override
    public List<Integer> batchGet(List<String> urls) {
        CloseableHttpClient httpclient = httpClientBuilder.build();
        return urls.stream().map(url -> {
            final HttpGet request = buildGet(url);
            try (CloseableHttpResponse response = httpclient.execute(request)) {
                return response.getStatusLine().getStatusCode();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Integer get(String url) throws IOException {
        CloseableHttpClient httpclient = httpClientBuilder.build();
        final HttpGet request = buildGet(url);
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            return response.getStatusLine().getStatusCode();
        }
    }
}
