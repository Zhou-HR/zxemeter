package com.gdiot.ssm.device;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdiot.ssm.http.yd.RequestInfo.Method;

/**
 * @author ZhouHR
 */
public abstract class AbstractAPI<T> {
    public String key;
    public String url;
    public Method method;
    public ObjectMapper mapper = new ObjectMapper();
}
