package org.lxp.weixin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.entity.User;
import org.lxp.weixin.exception.ErrorCodeEnum;
import org.lxp.weixin.exception.WXException;
import org.lxp.weixin.repository.UserRepository;
import org.lxp.weixin.response.Jscode2sessionResponse;
import org.lxp.weixin.util.JsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final UserRepository userRepository;

    public void loginOrRegister(String jsCode) {
        final var response = getWXResponse(jsCode);
        final var openId = response.getOpenId();
        final var rtn = userRepository.findOne(Example.of(User.builder().openId(openId).build()));
        if (rtn.isEmpty()) {
            userRepository.save(User.builder().openId(openId).build());
        }
    }

    /**
     * Fail: sessionId={"errcode":40029,"errmsg":"invalid code, rid: **"}
     * Success: sessionId={"session_key":"**","openid":"***"}
     */
    private Jscode2sessionResponse getWXResponse(String jsCode) {
        final var jscode2sessionMono = webClient.get().uri("/sns/jscode2session?appid=" + appId +
                        "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code")
                .retrieve()
                .bodyToMono(String.class);
        final var sessionId = jscode2sessionMono.block();
        log.debug("sessionId={}", sessionId);
        final var response = JsonHelper.toObject(Jscode2sessionResponse.class, sessionId);
        if (Objects.nonNull(response.getErrCode())) {
            log.error("{}, {}", response.getErrCode(), response.getErrMsg());
            throw new WXException(ErrorCodeEnum.WX_FAILURE);
        }
        return response;
    }
}
