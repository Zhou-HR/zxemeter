package com.gdiot.ssm.device;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.DeviceResponse;
import com.gdiot.ssm.http.yd.HttpGetMethod;
import com.gdiot.ssm.http.yd.OnenetApiException;
import com.gdiot.ssm.http.yd.RequestInfo.Method;
import com.gdiot.ssm.util.YDConfig;


/**
 * @author ZhouHR
 */
public class GetDeviceApi extends AbstractAPI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private HttpGetMethod HttpMethod;
    private String devId;

    /**
     * 精确查询单个设备
     * 参数顺序与构造函数顺序一致
     *
     * @param devId:设备名，String
     * @param key:masterkey    或者 设备apikey,String
     */
    public GetDeviceApi(String devId, String key) {
        this.devId = devId;
        this.key = key;
        this.method = Method.GET;
        this.HttpMethod = new HttpGetMethod(method);
        this.url = YDConfig.YD_BASE_URL + "/devices" + "/" + devId;
        Map<String, Object> headmap = new HashMap<String, Object>();
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);

        HttpMethod.setcompleteUrl(url, null);
    }


    public BasicResponse<DeviceResponse> executeApi() {
        BasicResponse response = null;
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            HttpResponse httpResponse = HttpMethod.execute();
            response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
            response.setJson(mapper.writeValueAsString(response));
            Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), DeviceResponse.class);
            response.setData(newData);
            return response;
        } catch (Exception e) {
            logger.error("json error {}", e.getMessage());
            throw new OnenetApiException(e.getMessage());
        } finally {
            try {
                HttpMethod.httpClient.close();
            } catch (Exception e) {
                logger.error("http close error: {}", e.getMessage());
                throw new OnenetApiException(e.getMessage());
            }
        }

    }
}
