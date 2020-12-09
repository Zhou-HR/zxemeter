package com.gdiot;

import java.time.Duration;

import org.mybatis.spring.annotation.MapperScan;
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

import com.gdiot.lora.LoraEMServerThread;
import com.gdiot.lora.LoraSmokeAlertServerThread;
import com.gdiot.lora.LoraSmokeServerThread;
import com.gdiot.lora.LoraWMServerThread;
import com.gdiot.mqtt.MqttServerThread;
import com.gdiot.mqtt.SmokeMqttServerThread;
import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.tcp.TcpDecoderHandler;
import com.gdiot.tcp.TcpEncoderHandler;
import com.gdiot.tcp.TcpHandler;
import com.gdiot.udp.UdpDecoderHandler;
import com.gdiot.udp.UdpEncoderHandler;
import com.gdiot.udp.UdpHandler;
import com.gdiot.udp.UdpServerThread;
import com.gdiot.util.SpringContextUtils;
import com.gdiot.util.TcpConfig;
import com.gdiot.util.UdpConfig;

import reactor.core.publisher.Flux;
import reactor.netty.tcp.TcpServer;
import reactor.netty.udp.UdpServer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.gdiot.mapper")
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

        //启动电表接收  中性平台启动  "00000000000000e3";//芯北演示电表，中性平台
        LoraEMServerThread mLoraEMServerThread = new LoraEMServerThread();
        mLoraEMServerThread.start();

        //启动烟感接收   中性平台启动  "00000000000000e5";//国动烟感演示
        LoraSmokeServerThread mLoraSmokeServerThread = new LoraSmokeServerThread();
        mLoraSmokeServerThread.start();

        //启动华南理工烟感集中报警接收  "00000000000000eb";//国动烟感集中报警演示
//		LoraSmokeAlertServerThread mLoraSmokeAlertServerThread = new LoraSmokeAlertServerThread();
//		mLoraSmokeAlertServerThread.start();

//		//UDP 水表  原始 ZP超声波阀控水表NB-iot传协议--上行和下行   中性平台启动
        UdpServerThread mUdpServer = new UdpServerThread();
        mUdpServer.start();

        //启动2G电表接收   中性平台启动  暂不启动
//		MqttServerThread mMqttServerThread = new MqttServerThread();
//		mMqttServerThread.start();

        //启动水表接收   中性平台启动  "00000000000000e2";//水表应用  不启动
//		LoraWMServerThread mLoraWMServerThread = new LoraWMServerThread();
//		mLoraWMServerThread.start();

        //启动lora 测试应用0000000000000016 ;//水表应用  不启动
//		LoraWMServerThread mLoraWMServerThread = new LoraWMServerThread();
//		mLoraWMServerThread.start();

        //启动烟感mqtt接收   华南理工
        SmokeMqttServerThread mSmokeMqttServerThread = new SmokeMqttServerThread();
        mSmokeMqttServerThread.start();

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

    public void MqttHanderSmokeMessage(String message) {
        if (asyncService == null) {
            asyncService = SpringContextUtils.getBean(AsyncService.class);
            log.info("opened asyncService create");
        }
        DataSenderTask task = new DataSenderTask(message.toString(), "mqtt_smoke_alert");
        asyncService.executeAsync(task);
        log.info("task: receive lora data done");
    }

    @Bean
        //此处注释掉后不启动TCP，UDP  中性平台启动
    CommandLineRunner serverRunner() {
        return strings -> {
            log.info("application: serverRunner");
            log.info("application: 启动TCP服务");
            createTcpServer();////电表 安科瑞 TCP

//			log.info("application: 启动UDP服务");
//			createUdpServer();
        };
    }

    /**
     * 创建UDP Server
     *
     * @param udpDecoderHanlder： 解析UDP Client上报数据handler
     */
    private void createUdpServer() {
        UdpServer.create()
                .handle((in, out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
                .doOnBound(conn -> conn
                        .addHandler("decoder", new UdpDecoderHandler())
                        .addHandler("encoder", new UdpEncoderHandler())
                        .addHandler("handler", new UdpHandler())
                ) //可以添加多个handler
                .port(UdpConfig.PORT) //UDP Server端口 8089
                .bindNow(Duration.ofSeconds(30));
    }

    /**
     * 创建TCP Server
     *
     * @param tcpDecoderHanlder： 解析TCP Client上报数据的handler
     */
    private void createTcpServer() {
        log.info("application: createTcpServer");
        TcpServer.create()
                .handle((in, out) -> {
                    in.receive()
                            .asByteArray()
                            .subscribe();
                    return Flux.never();
                })
                .doOnConnection(conn -> conn
                        .addHandler("decoder", new TcpDecoderHandler())
                        .addHandler("encoder", new TcpEncoderHandler())
                        .addHandler("handler", new TcpHandler())
                )
//				.host(TcpConfig.IP)//实例只写了如何添加handler,可添加delimiter，tcp生命周期，decoder，encoder等handler
                .port(TcpConfig.PORT)
                .bindNow();
    }

}
