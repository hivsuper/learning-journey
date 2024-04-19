package org.lxp.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class OKHttpHelperTest {

    @Rule
    public WireMockRule forecastIoService = new WireMockRule(8080);

    @Test
    public void testBatchGet() {
        OKHttpHelper helper = new OKHttpHelper();
        final String crawlerUrl = "http://127.0.0.1:8080/crawler-test";
        forecastIoService.stubFor(get(urlEqualTo("/crawler-test")).willReturn(aResponse().withFixedDelay(10).withStatus(HttpStatus.SC_OK)));
        assertThat(helper.get(crawlerUrl)).isEqualTo(200);
    }
}