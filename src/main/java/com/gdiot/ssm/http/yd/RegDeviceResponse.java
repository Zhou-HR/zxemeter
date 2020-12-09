package com.gdiot.ssm.http.yd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ZhouHR
 */
public class RegDeviceResponse {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    @JsonProperty(value = "key")
    public String key;
    @JsonProperty(value = "device_id")
    public String devid;
}
