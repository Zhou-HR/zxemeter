package com.gdiot.ssm.cmds;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdiot.ssm.device.AbstractAPI;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.CmdsResponse;
import com.gdiot.ssm.http.yd.HttpPostMethod;
import com.gdiot.ssm.http.yd.OnenetApiException;
import com.gdiot.ssm.http.yd.RequestInfo.Method;
import com.gdiot.ssm.util.DateUtil;

/**
 * @author ZhouHR
 */
public class NBSendCmds extends AbstractAPI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpPostMethod httpMethod;
    private final long time;
    public ObjectMapper mapper = new ObjectMapper();
    private final Method method;

    public NBSendCmds(String url, String contents, String time, String api_key) {
        logger.info("--------------NBSendCmds---url=" + url);
        this.time = Long.parseLong(time);
        //head参数
        this.method = Method.POST;
        Map<String, Object> headmap = new HashMap<>();
        httpMethod = new HttpPostMethod(method);
        headmap.put("api-key", api_key);
        httpMethod.setHeader(headmap);

        // body参数
        Map<String, Object> bodymap = new HashMap<>();
        if (!"".equals(contents)) {
            bodymap.put("args", contents);
            String json;
            ObjectMapper remapper = new ObjectMapper();
            try {
                json = remapper.writeValueAsString(bodymap);
            } catch (Exception e1) {
                logger.error("json error", e1.getMessage());
                throw new OnenetApiException(e1.getMessage());
            }
            httpMethod.setEntity(json);
            httpMethod.setcompleteUrl(url, null);
        } else {
            httpMethod = null;
        }

    }

    public BasicResponse<CmdsResponse> executeApi() {
        BasicResponse response = null;
        try {
            if (httpMethod != null) {
                //此处判断时间是否在30分钟上报时间内，超出30分钟的发下行成功率不高，此处目前无效（表号传入后不查数据库了，时间用来当前时间）
                if (DateUtil.ifCurrentTimeFree(time)) {
                    HttpResponse httpResponse = httpMethod.execute();
                    response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
                    response.setJson(mapper.writeValueAsString(response));
                    Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), CmdsResponse.class);
                    response.setData(newData);
                } else {
                    httpMethod = new HttpPostMethod(method);
                    response.setErrno(100);
                    response.setError("The device is busy ,please try again later! ");
                    response.setJson("{\"error\":\"The device is busy ,please try again later!\"}");
                }
                return response;
            } else {
                httpMethod = new HttpPostMethod(method);
                response.setErrno(100);
                response.setError("parameter error");
                response.setJson("{\"error\":\"parameter error\"}");
                return response;
            }
        } catch (Exception e) {
            logger.error("json error {}", e.getMessage());
            throw new OnenetApiException(e.getMessage());
        } finally {
            try {
                httpMethod.httpClient.close();
            } catch (Exception e) {
                logger.error("http close error: {}", e.getMessage());
                throw new OnenetApiException(e.getMessage());
            }
        }

    }
}
