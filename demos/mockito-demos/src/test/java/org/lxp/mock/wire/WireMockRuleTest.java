package org.lxp.mock.wire;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

/**
 * https://www.baeldung.com/introduction-to-wiremock
 */
@RunWith(MockitoJUnitRunner.class)
public class WireMockRuleTest {
    private static final int PORT = 8080;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    @Test
    public void testURLMatching() throws IOException, InterruptedException {
        stubFor(get(urlPathMatching("/baeldung/.*"))
                .willReturn(aResponse()
                        .withStatus(200)));
        stubFor(get(urlPathEqualTo("/baeldung/wiremock"))
                .willReturn(aResponse()
                        .withStatus(503)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/baeldung/wiremock"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
        assertEquals(503, response.statusCode());
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

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/baeldung/wiremock"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
        assertEquals(200, response.statusCode());
    }
}
