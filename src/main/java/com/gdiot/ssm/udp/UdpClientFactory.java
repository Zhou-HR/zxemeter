package com.gdiot.ssm.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientFactory {
	private static Logger LOGGER = LoggerFactory.getLogger(UdpClientFactory.class);
	private UdpSendTask client;

    private static volatile UdpClientFactory mUdpClientFactory = null;

    public static UdpClientFactory getInstance(UdpSendTask t){
    	LOGGER.info("udpClientFactory getInstance run");
        if(null == mUdpClientFactory){
            synchronized (UdpClientFactory.class){
                if(null == mUdpClientFactory){
                	//LOGGER.info("MQTTClientFactory new");
                	mUdpClientFactory = new UdpClientFactory(t);
                }
            }
        }
        return mUdpClientFactory;
    }
    private UdpClientFactory(UdpSendTask t) {
    	client = t;
    	//LOGGER.info("LoraClientFactory new---client="+client);
    }
    public UdpSendTask getClient() {
    	return client;
    }
	
}
