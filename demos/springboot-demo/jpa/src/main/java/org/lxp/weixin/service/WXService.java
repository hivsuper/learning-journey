package org.lxp.weixin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.exception.WXException;
import org.lxp.weixin.response.Jscode2sessionResponse;
import org.lxp.weixin.util.JsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WXService {
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;
    private final WebClient webClient;

    /**
     * Success: sessionId={"errcode":40029,"errmsg":"invalid code, rid: **"}
     * Fail: sessionId={"session_key":"**","openid":"***"}
     */
    public Jscode2sessionResponse get(String jsCode) throws JsonProcessingException {
        Mono<String> jscode2sessionMono = webClient.get().uri("/sns/jscode2session?appid=" + appId +
                        "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code")
                .retrieve()
                .bodyToMono(String.class);
        String sessionId = jscode2sessionMono.block();
        log.debug("sessionId={}", sessionId);
        Jscode2sessionResponse response = JsonHelper.toObject(Jscode2sessionResponse.class, sessionId);
        if (Objects.nonNull(response.getErrCode())) {
            throw new WXException(response.getErrCode(), response.getErrMsg());
        }
        return response;
    }
}
