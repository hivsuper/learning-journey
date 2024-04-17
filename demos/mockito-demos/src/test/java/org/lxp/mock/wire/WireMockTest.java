package org.lxp.mock.wire;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://www.baeldung.com/introduction-to-wiremock
 */
@RunWith(MockitoJUnitRunner.class)
public class WireMockTest {
    private static final int PORT = 8080;
    private WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        final String body = "Welcome to Baeldung!";
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/baeldung")).willReturn(aResponse().withBody(body)));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/baeldung"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.body()).isEqualTo(body);
    }
}
