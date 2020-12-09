package com.gdiot.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author ZhouHR
 */
public class LoraConfig {

    //外网平台www.guodongiot.com:
    //OpenAPI:https://www.guodongiot.com:442/api/OpenAPI_XXXXX 或 http://www.guodongiot.com:90/api/OpenAPI_XXXXX
    //Websocket：ws://www.guodongiot.com:92/webs?
    /**
     * 1、往指定的节点发送数据
     * url：连接地址+OpenAPI_SendDataForNode（如：https://www.workingbao.com:442/api/OpenAPI_SendDataForNode或http://www.workingbao.com:88/api/OpenAPI_SendDataForNode）
     * content to send:{"params":{"devEUI":"000000000005c5da","data":"\\x123456"}}
     */

//	public static String baseUrl = "ws://www.guodongiot.com:92/webs?";

//	public static String baseUrl_http_node = "http://www.guodongiot.com:90/api/OpenAPI_SendDataForNode";
//	public static String baseUrl_https_node = "https://www.guodongiot.com:442/api/OpenAPI_SendDataForNode";
//	
//	public static String baseUrl_http_cast = "http://www.guodongiot.com:90/api/OpenAPI_SendDataForMulticast";
//	public static String baseUrl_https_cast = "https://www.guodongiot.com:442/api/OpenAPI_SendDataForMulticast";


    public static String baseUrl = "ws://115.29.186.49:92/webs?";
    public static String baseUrl_http_node = "http://115.29.186.49:90/api/OpenAPI_SendDataForNode";
    public static String baseUrl_https_node = "https://115.29.186.49:442/api/OpenAPI_SendDataForNode";
    public static String baseUrl_http_cast = "http://115.29.186.49:90/api/OpenAPI_SendDataForMulticast";
    public static String baseUrl_https_cast = "https://115.29.186.49:442/api/OpenAPI_SendDataForMulticast";

    //zhang.jieqiong@gd-iot.com
//	public static String appEUI = "00000000000000d6";//智能电表系统
    public static String em_appEUI = "00000000000000e3";//芯北演示电表，中性平台
    public static String userSec = "41cca87dcb93f2257040108fe6b9a1c4";
    public static String userID = "3457061";
    public static String groupid = "00000040";

    //标识数据类型，0为：unconfirmed，1为：confirmed confirmed需要设备回复ACK
    public static int lora_send_type = 0;//可选参数0或者1；
    public static String requestid = "12344aassa";

    public static String wm_appEUI = "00000000000000e2";//水表应用

    public static String smoke_appEUI = "00000000000000e5";//国动烟感演示

    //sales@gd-iot.com 未使用
//	public static String demo_userSec = "4561d1d7f907b176c49ab96f5acf1c1c";
//	public static String demo_userID = "3457020";
//	public static String smoke_alarm_appEUI = "00000000000000eb";

    //luo.yi@gd-iot.com OTAA
    public static String smoke_alarm_userSec = "cc786baaab895bccc56b76bc59753cd4";
    public static String smoke_alarm_userID = "3457085";
    public static String smoke_alarm_appEUI = "00000000000000f3";//华南理工集中报警演示

    public static String lora_em = "lora_em";
    public static String lora_wm = "lora_wm";
    public static String lora_smoke = "lora_smoke";
    public static String lora_smoke_alert = "lora_smoke_alert";
    public static String mqtt_smoke_alert = "mqtt_smoke_alert";

    //5785959@qq.com ClassC ABP //华南理工集中报警演示
    public static String smoke_alarm_userSec_zs = "be089d6b702148dd65320b4fd3f0f046";
    public static String smoke_alarm_userID_zs = "3457023";
    public static String smoke_alarm_appEUI_zs = "0000000000000085";//0000000000000088 85测试，88正式

    //wu.ming
//	public static String appEUI = "00000000000000d9";
//	public static String userSec = "81f5543c54149788915db5a1f42fe0ab";
//	public static String userID = "3457072";

    //chen.pan
//	public static String appEUI = "00000000000000d3";
//	public static String userSec = "4561d1d7f907b176c49ab96f5acf1c1c";
//	public static String userID = "3457020";

    /*
    public static String currentTimeStr = String.valueOf(System.currentTimeMillis());
    public static String tokenStr = getMD5(currentTimeStr + appEUI + userSec);
    public static String url = baseUrl+"?appEUI=" + appEUI + "&token=" + tokenStr + "&time_s=" + currentTimeStr;

    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
