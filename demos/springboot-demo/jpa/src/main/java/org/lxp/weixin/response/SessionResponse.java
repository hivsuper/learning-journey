package org.lxp.weixin.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public record SessionResponse(
        String sessionKey,
        String unionId,
        String openId) {
}
