package com.gdiot.ssm.http.yd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ZhouHR
 */
public class CmdsResponse {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("desc")
    private String desc;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
