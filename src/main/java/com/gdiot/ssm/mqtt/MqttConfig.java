package com.gdiot.ssm.mqtt;

/*
 * created by zhangjieqiong at 2019/04/16
 */
public class MqttConfig {
	//tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://114.215.192.141:1883";
    //定义一个主题
    public static final String TOPIC_TEST = "mtopic";
    public static final String TOPIC_CLIENT = "mqtt_2g/client";//服务端订阅 ，设备上报的消息，设备发布
    public static final String TOPIC_SERVER = "mqtt_2g/server";//服务端发布，下行的消息，设备端订阅
    //定义MQTT的ID，可以在MQTT服务配置中指定
    public static final String clientid = "mqtt_2g/xb";
    
    public static final int ConnectionTimeout = 10;
    public static final int KeepAliveTime = 20;

    public static final String userName = "stonegeek";
    public static final String passWord = "123456";
    
    /*
     * 0 almost once
     * 1 atleast once
     * 2 exactly once
     */
    public static final int qos_almost_once = 0;
    public static final int qos_atleast_once = 1;
    public static final int qos_exactly_once = 2;
}
