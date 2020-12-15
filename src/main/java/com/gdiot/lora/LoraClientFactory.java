package com.gdiot.lora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhouHR
 */
public class LoraClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoraClientFactory.class);
    private final LoraTask client;

    private static volatile LoraClientFactory mLoraClientFactory = null;

    public static LoraClientFactory getInstance(LoraTask t) {
        LOGGER.info("LoraClientFactory getInstance run");
        if (null == mLoraClientFactory) {
            synchronized (LoraClientFactory.class) {
                if (null == mLoraClientFactory) {
                    //LOGGER.info("MQTTClientFactory new");
                    mLoraClientFactory = new LoraClientFactory(t);
                }
            }
        }
        return mLoraClientFactory;
    }

    private LoraClientFactory(LoraTask t) {
        client = t;
        //LOGGER.info("LoraClientFactory new---client="+client);
    }

    public LoraTask getClient() {
        return client;
    }

}
