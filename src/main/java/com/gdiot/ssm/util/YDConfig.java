package com.gdiot.ssm.util;

/**
 * @author ZhouHR
 */
public class YDConfig {

    //中性平台
    private static String token = "gdiot8087";//用户自定义token和OneNet第三方平台配置里的token一致
    private static String aeskey = "+9TKjzg75YwMNsV23XFsnl07lDojSlqqfI4eX2S6gqM=";//aeskey和OneNet第三方平台配置里的token一致


    public static final String YD_BASE_URL = "http://api.heclouds.com";
    public static final String USER_ID = "85079";

    public static final String YD_EXECUTE_URL = "http://api.heclouds.com" + "/nbiot/execute?";

    public static final int OBJ_ID = 3200;
    public static final int OBJ_INST_ID = 0;
    public static final int RES_ID = 5505;

    //电表 警备区

    public static final String JBQ_API_KEY = "irs1j5quJQFGzUB=pLU4=nTBNGg=";
    public static final String JBQ_PRODUCT_ID = "216901";
    public static final String JBQ_PROTOCOL = "LWM2M";
    public static final String JBQ_TOKEN = "gdiot8087jbq";

    //中性平台 电表 芯北

    public static final String ZX_XB_EM_API_KEY = "ixyfnlCNoowX=jvGnPGnInlVDdU=";
    public static final String ZX_XB_EM_PRODUCT_ID = "359876";
    public static final String ZX_XB_EM_PROTOCOL = "LWM2M";
    public static final String ZX_XB_EM_TOKEN = "gdiot8086zxxb";

    //中性电表 安科瑞电表

    public static final String AKR_API_KEY = "HTE23EfqURZkaiHd6tWGT24BaXc=";
    public static final String AKR_PRODUCT_ID = "356978";
    public static final String AKR_PROTOCOL = "LWM2M";
    public static final String AKR_TOKEN = "gdiot8087akr";

    public static final int AKR_OBJ_ID = 3300;
    public static final int AKR_OBJ_INST_ID = 0;
    public static final int AKR_RES_ID = 5750;

    //千丁电表

    public static final String QD_API_KEY = "w83aMYZyr==K46EarAEbS=LmHW0=";
    public static final String QD_PRODUCT_ID = "391509";
    public static final String QD_PROTOCOL = "LWM2M";
    public static final String QD_TOKEN = "gdiot8087QD";

    public static final int QD_OBJ_ID = 3300;
    public static final int QD_OBJ_INST_ID = 0;
    public static final int QD_RES_ID = 5750;

    //智能电表  集团正式

    public static final String GD_XB_EM_API_KEY = "1HKtAUuVmulJJCva6J7gTT=h=QI=";
    public static final String GD_XB_EM_PRODUCT_ID = "216901";
    public static final String GD_XB_EM_PROTOCOL = "LWM2M";
    public static final String GD_XB_EM_TOKEN = "gdiot888";

    //智能开关   集团正式

    public static final String SWITCH_PRODUCT_ID = "318926";
    public static final String SWITCH_TOKEN = "gdiot999";
    public static final String SWITCH_API_KEY = "Eig72WmlhxqrF1fF7AalHfAdO0s=";
    private static String SWITCH_ACCESS_KEY = "ookwlkfmnLcRfFSti5USZmvzKVN3JBZx4Sib3mdPRyg=";
    public static final String SWITCH_PROTOCOL = "LWM2M";

}
