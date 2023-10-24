package org.lxp.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.time.temporal.ChronoUnit.SECONDS;

public class JEP321HttpClientHelper {

    private ExecutorService executor;

    public JEP321HttpClientHelper(String maxTotalConnection) {
        System.setProperty("jdk.httpclient.connectionPoolSize", maxTotalConnection);
    }

    public JEP321HttpClientHelper(int maxTotalConnection) {
        this.executor = Executors.newFixedThreadPool(maxTotalConnection);
    }

    public String post(String url, String requestBody, String... headers) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(headers)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.of(10, SECONDS))
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String get(String url, String... headers) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(headers)
                .version(HttpClient.Version.HTTP_2)
                .GET().build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getWithThreadPool(String url, String... headers) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers(headers)
                .GET().build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .executor(executor)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getAsync(HttpRequest request, HttpClient httpClient, int waitSeconds) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<String> response = httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(Throwable::getMessage);
        return response.get(waitSeconds, TimeUnit.SECONDS);
    }

    public HttpClient getHttpClientWithTimeout(int seconds) {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(seconds))
                .build();
    }
}
