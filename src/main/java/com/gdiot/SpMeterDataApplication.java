package com.gdiot;

import java.time.Duration;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gdiot.ssm.lora.LoraEMServerThread;
import com.gdiot.ssm.lora.LoraSmokeServerThread;
import com.gdiot.ssm.lora.LoraWMServerThread;
import com.gdiot.ssm.mqtt.MqttServerThread;
import com.gdiot.ssm.service.AsyncService;
import com.gdiot.ssm.task.DataSenderTask;
import com.gdiot.ssm.udp.UdpServerThread;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.UdpConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.gdiot.ssm.dao")
@EnableScheduling
public class SpMeterDataApplication {

    @Autowired
    private static AsyncService asyncService;
    private static ApplicationContext context;

    public static void main(String[] args) {
//		SpringApplication.run(SpMeterDataApplication.class, args);
        log.info("-------------SpMeterDataApplication run----------------");

        SpringApplication application = new SpringApplication(SpMeterDataApplication.class);
        context = application.run(args);

        SpringContextUtils mSpringContextUtils = new SpringContextUtils();
        mSpringContextUtils.setApplicationContext(context);

        //启动电表接收
        LoraEMServerThread mLoraEMServerThread = new LoraEMServerThread();
        mLoraEMServerThread.start();

        //启动2G电表接收
//		MqttServerThread mMqttServerThread = new MqttServerThread();
//		mMqttServerThread.start();

        //启动水表接收
//		LoraWMServerThread mLoraWMServerThread = new LoraWMServerThread();
//		mLoraWMServerThread.start();

        //启动烟感接收
        LoraSmokeServerThread mLoraSmokeServerThread = new LoraSmokeServerThread();
        mLoraSmokeServerThread.start();

//		//UDP 水表 
        UdpServerThread mUdpServer = new UdpServerThread();
        mUdpServer.start();
    }

    public void LoraHanderMessage(String message, String msgtype) {
        if (asyncService == null) {
            asyncService = SpringContextUtils.getBean(AsyncService.class);
            log.info("opened asyncService create");
        }
        DataSenderTask task = new DataSenderTask(message, msgtype);
        asyncService.executeAsync(task);
        log.info("task: receive lora data done");
    }

    public void MqttHanderMessage(String message) {
        if (asyncService == null) {
            asyncService = SpringContextUtils.getBean(AsyncService.class);
            log.info("opened asyncService create");
        }
        DataSenderTask task = new DataSenderTask(message.toString(), "mqtt_2g");
        asyncService.executeAsync(task);
        log.info("task: receive lora data done");
    }
	
	/*
	@Bean
	CommandLineRunner serverRunner(UdpDecoderHandler udpDecoderHanlder, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler) {
		return strings -> {
			createUdpServer(udpDecoderHanlder, udpEncoderHandler, udpHandler);
		};
	}*/

    /**
     *
     * 创建UDP Server
     * @param udpDecoderHandler： 用于解析UDP Client上报数据的handler
     * @param udpEncoderHandler： 用于向UDP Client发送数据进行编码的handler
     * @param udpHandler: 用户维护UDP链接的handler
     */
	/*private void createUdpServer(UdpDecoderHandler udpDecoderHandler, UdpEncoderHandler udpEncoderHandler, UdpHandler udpHandler) {
		log.info("--------------udp server 启动---start----------------");
		UdpServer.create()
				.handle((in,out) -> {
					in.receive()
					.asByteArray()
					.subscribe();
					return Flux.never();
				})
				.port(UdpConfig.PORT) //UDP Server端口
				.doOnBound(conn -> conn
						.addHandler("decoder",udpDecoderHandler)
						.addHandler("encoder", udpEncoderHandler)
						.addHandler("handler", udpHandler)
						) //可以添加多个handler
				.bindNow(Duration.ofSeconds(30));
		log.info("-------------udp server 启动---end----------------");
	}*/

}
