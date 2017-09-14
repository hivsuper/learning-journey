package org.lxp.springboot.vo;

import io.swagger.annotations.ApiModelProperty;

public class BaseVO {
    @ApiModelProperty(value = "status code")
    protected int resCode;
    @ApiModelProperty(value = "description")
    protected String resDes;

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResDes() {
        return resDes;
    }

    public void setResDes(String resDes) {
        this.resDes = resDes;
    }

    @Override
    public String toString() {
        return "BaseVO [resCode=" + resCode + ", resDes=" + resDes + "]";
    }
}