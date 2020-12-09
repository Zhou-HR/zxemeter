package com.gdiot.ssm.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.gdiot.ssm.util.UdpConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
public class UdpClient {

    //    private String sendStr = "hello";
    private String netAddress = "255.255.255.255";

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public UdpClient(String msg) {
        try {
            datagramSocket = new DatagramSocket();
            byte[] buf = msg.getBytes();
            InetAddress address = InetAddress.getByName(netAddress);
            datagramPacket = new DatagramPacket(buf, buf.length, address, UdpConfig.PORT);
            datagramSocket.send(datagramPacket);

            byte[] receBuf = new byte[1024];
            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
            datagramSocket.receive(recePacket);

            String receStr = new String(recePacket.getData(), 0, recePacket.getLength());
            log.info("receStr=" + receStr);

            //获取服务端ip
//            String serverIp = recePacket.getAdress();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UdpClient udpClient = new UdpClient("hello");
                }
            }).start();
        }
    }
}