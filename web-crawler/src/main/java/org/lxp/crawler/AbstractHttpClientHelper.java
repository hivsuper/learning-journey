package org.lxp.crawler;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

public abstract class AbstractHttpClientHelper {
    protected int maxTotalConnection = 10;
    protected RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000)
            .setConnectionRequestTimeout(10000).build();

    public abstract List<Integer> batchGet(List<String> urls) throws IOException;

    public abstract Integer get(String url) throws Exception;

    protected HttpGet buildGet(String url) {
        HttpGet get = new HttpGet(url);
        get.setHeader("Connection", "close");
        return get;
    }
}
