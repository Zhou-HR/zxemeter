package com.gdiot.util;

import com.huawei.iotplatform.client.NorthApiClient;
import com.huawei.iotplatform.client.NorthApiException;
import com.huawei.iotplatform.client.dto.AuthOutDTO;
import com.huawei.iotplatform.client.invokeapi.Authentication;

/**
 * @author: shenzhengkai
 * @className: AuthenticationUtil
 * @description: 获取accesstoken
 * @date: 日期: 2020/10/27 时间: 11:34
 **/
public class AuthenticationUtil {
    public static String getAccessToken() {
        NorthApiClient northApiClient = AuthUtil.initApiClient();
        Authentication authentication = new Authentication(northApiClient);
        AuthOutDTO authOutDTO = null;
        String accessToken = null;
        try {
            authOutDTO = authentication.getAuthToken();
            accessToken = authOutDTO.getAccessToken();
        } catch (NorthApiException e) {
            e.printStackTrace();
        }
        return accessToken;
    }
}
