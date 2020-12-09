package com.gdiot.util;

import com.huawei.iotplatform.client.NorthApiClient;
import com.huawei.iotplatform.client.NorthApiException;
import com.huawei.iotplatform.client.dto.ClientInfo;

public class AuthUtil {

    private static NorthApiClient northApiClient = null;

    public static NorthApiClient initApiClient() {
        if (northApiClient != null) {
            return northApiClient;
        }
        northApiClient = new NorthApiClient();
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setPlatformIp(Constant.BASE_URL);
        clientInfo.setPlatformPort(Constant.PORT);
        clientInfo.setAppId(Constant.APPID);
        clientInfo.setSecret(Constant.SECRET);
        try {
            northApiClient.setClientInfo(clientInfo);
            northApiClient.initSSLConfig();
        } catch (NorthApiException e) {
            System.out.println(e.toString());
        }
        return northApiClient;
    }
}
