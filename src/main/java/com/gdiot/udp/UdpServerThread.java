package com.gdiot.udp;

import java.net.DatagramSocket;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdiot.service.AsyncService;
import com.gdiot.util.UdpConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpServerThread extends Thread {
    private DatagramSocket datagramSocket;
    
    public void run() {
    	 try {
            datagramSocket = new DatagramSocket(UdpConfig.PORT);
            System.out.println("udp服务端已经启动！");
             
            UdpReceiveTask mUdpReceiveTask = new UdpReceiveTask();
//     		UdpClientFactory.getInstance(mUdpTask);
     		mUdpReceiveTask.setDatagramSocket(datagramSocket);
//            mUdpTask.run();
            new Thread(mUdpReceiveTask).start();
            log.info("UdpReceiveTask启动接收程序");
            
//            UdpSendTask mUdpSendTask = new UdpSendTask();
//     		UdpClientFactory.getInstance(mUdpSendTask);
//            mUdpSendTask.setDatagramSocket(datagramSocket);
////            mUdpSendTask.run();
//            new Thread(mUdpSendTask).start();
//            log.info("UdpReceiveTask启动发送程序");
             
         } catch (Exception e) {
             datagramSocket = null;
             System.out.println("udp服务端启动失败！");
             e.printStackTrace();
         }
    }
}