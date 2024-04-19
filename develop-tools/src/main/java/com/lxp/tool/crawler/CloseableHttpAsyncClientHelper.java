package com.lxp.tool.crawler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class CloseableHttpAsyncClientHelper extends AbstractHttpClientHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseableHttpAsyncClientHelper.class);
    private final HttpAsyncClientBuilder httpAsyncClientBuilder;

    public CloseableHttpAsyncClientHelper() throws IOReactorException {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(maxTotalConnection);
        connManager.setDefaultMaxPerRoute(maxTotalConnection);
        httpAsyncClientBuilder = HttpAsyncClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig);
    }

    @Override
    public List<Integer> batchGet(List<String> urls) {
        CloseableHttpAsyncClient httpclient = httpAsyncClientBuilder.build();
        httpclient.start();

        List<Future<HttpResponse>> respList = urls.stream().map(url -> {
            final HttpGet request = buildGet(url);
            return httpclient.execute(request, futureCallback);
        }).collect(Collectors.toCollection(LinkedList::new));

        return respList.stream().map(response -> {
            try {
                return response.get().getStatusLine().getStatusCode();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Integer get(String url) throws Exception {
        CloseableHttpAsyncClient httpclient = httpAsyncClientBuilder.build();
        httpclient.start();

        final HttpGet request = buildGet(url);
        Future<HttpResponse> response = httpclient.execute(request, new FutureCallback<HttpResponse>() {
            public void completed(HttpResponse httpResponse) {
                LOGGER.info("request complete");
            }

            public void failed(Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

            public void cancelled() {
            }
        });
        return response.get().getStatusLine().getStatusCode();
    }

    private FutureCallback<HttpResponse> futureCallback = new FutureCallback<HttpResponse>() {
        public void completed(HttpResponse httpResponse) {
            LOGGER.info("request complete");
        }

        public void failed(Exception e) {
            LOGGER.error("request failed", e);
        }

        public void cancelled() {
            LOGGER.info("request cancelled");
        }
    };
}
