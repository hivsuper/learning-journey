package org.lxp.java11;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerIT {

    @Autowired
    private TestRestTemplate template;

    @Test
    void get() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/?param=afdafd", String.class);
        assertThat(response.getBody()).isEqualTo("get afdafd");
    }

    @Test
    void testGetWithHttpClientSync() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(template.getRootUri() + "?param=afdafd"))
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.body()).isEqualTo("get afdafd");
    }

    @Test
    void testGetWithHttpClientAsync() throws Exception {
        List<URI> targets = Arrays.asList(
                new URI(template.getRootUri() + "?param=afdafd"),
                new URI(template.getRootUri() + "?param=123456"));
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<String>> futures = targets.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target).GET().build(),
                                HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body))
                .collect(Collectors.toList());
        List<String> rtn = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        assertThat(rtn).containsExactlyInAnyOrder("get afdafd", "get 123456");
    }

    @Test
    void testPostWithHttpClientSync() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(template.getRootUri()))
                .POST(HttpRequest.BodyPublishers.ofString("afdafd"))
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.body()).isEqualTo("post afdafd");
    }

    @Test
    void testPostWithHttpClientAsync() throws Exception {
        List<URI> targets = Arrays.asList(
                new URI(template.getRootUri()),
                new URI(template.getRootUri()));
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<String>> futures = targets.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target).POST(HttpRequest.BodyPublishers.ofString("123456")).build(),
                                HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body))
                .collect(Collectors.toList());
        List<String> rtn = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        assertThat(rtn).containsExactly("post 123456", "post 123456");
    }
}