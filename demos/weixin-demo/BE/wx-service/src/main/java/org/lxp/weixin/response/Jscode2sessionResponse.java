package org.lxp.weixin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jscode2sessionResponse {
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("unionid")
    private String unionId;
    @JsonProperty("errmsg")
    private String errMsg;
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("errcode")
    private Integer errCode;
}
