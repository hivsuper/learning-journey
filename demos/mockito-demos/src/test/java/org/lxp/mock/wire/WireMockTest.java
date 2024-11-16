package org.lxp.mock.wire;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <a href="https://www.baeldung.com/introduction-to-wiremock">introduction-to-wiremock</a>
 */
@ExtendWith(MockitoExtension.class)
public class WireMockTest {
    private static final int PORT = 8080;
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        final String body = "Welcome to Baeldung!";
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/baeldung")).willReturn(aResponse().withBody(body)));

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/baeldung"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertThat(response.body()).isEqualTo(body);
        }
    }

    @Test
    public void testURLMatching() throws IOException, InterruptedException {
        stubFor(get(urlPathMatching("/baeldung/.*"))
                .willReturn(aResponse()
                        .withStatus(200)));
        stubFor(get(urlPathEqualTo("/baeldung/wiremock"))
                .willReturn(aResponse()
                        .withStatus(503)));

        try (HttpClient httpClient = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/baeldung/wiremock"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
            assertEquals(503, response.statusCode());
        }
    }

    @Test
    public void testStubPriority() throws IOException, InterruptedException {
        stubFor(get(urlPathMatching("/baeldung/.*"))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(200)));
        stubFor(get(urlPathEqualTo("/baeldung/wiremock"))
                .atPriority(2)
                .withHeader("Accept", matching("text/.*"))
                .willReturn(aResponse()
                        .withStatus(503)));

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/baeldung/wiremock"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
            assertEquals(200, response.statusCode());
        }
    }
}
