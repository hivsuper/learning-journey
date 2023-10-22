package org.lxp.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class JEP321HttpClientHelper {
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
                .GET().build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
