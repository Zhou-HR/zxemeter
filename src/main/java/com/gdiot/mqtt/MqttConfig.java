package com.gdiot.mqtt;

/**
 * @author ZhouHR
 */
public class MqttConfig {
    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://114.215.192.141:1883";
    //定义一个主题
    public static final String TOPIC_TEST = "mtopic";
    public static final String TOPIC_CLIENT = "mqtt_2g/client";//服务端订阅 ，设备上报的消息，设备发布
    public static final String TOPIC_SERVER = "mqtt_2g/server";//服务端发布，下行的消息，设备端订阅
    //定义MQTT的ID，可以在MQTT服务配置中指定
    public static final String CLIENTID = "mqtt_2g/xb";

    public static final int CONNECTION_TIMEOUT = 10;
    public static final int KEEP_ALIVE_TIME = 20;

    public static final String USERNAME = "stonegeek";
    public static final String PASSWORD = "123456";

    /*
     * 0 almost once
     * 1 atleast once
     * 2 exactly once
     */
    public static final int qos_almost_once = 0;
    public static final int qos_atleast_once = 1;
    public static final int qos_exactly_once = 2;


    public static final String SMOKE_HOST = "tcp://183.6.183.252:17883";
    public static final String SMOKE_userName = "GDIoT";
    public static final String SMOKE_passWord = "GDIoT_021";
    //定义一个主题
    public static final String SMOKE_TOPIC_SERVER = "GDService/rawdata";//服务端发布，下行的消息，设备端订阅
    //定义MQTT的ID，可以在MQTT服务配置中指定
    public static final String SMOKE_clientid = "GDIoT";
    public static final String SMOKE_LIST0 = "000000000000a0a6,000000000000a0a7,000000000000a0a8,000000000000a0a9,000000000000a0aa";
    public static final String SMOKE_LIST = "000000000000a0b0,000000000000a0b1,000000000000a0b2,000000000000a0b3,000000000000a0b4";
    public static final String SMOKE_LIST_TYPE = "smoke_sensor_mqtt";
}
