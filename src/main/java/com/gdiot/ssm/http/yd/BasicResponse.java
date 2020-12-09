package com.gdiot.ssm.http.yd;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author ZhouHR
 */
public class BasicResponse<T> {
    public int errno;
    public String error;
    @JsonProperty("data")
    public Object dataInternal;
    public T data;
    @JsonIgnore
    public String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonGetter("data")
    public Object getDataInternal() {
        return dataInternal;
    }

    @JsonSetter("data")
    public void setDataInternal(Object dataInternal) {
        this.dataInternal = dataInternal;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
