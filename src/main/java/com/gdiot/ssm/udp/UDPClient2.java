package com.gdiot.ssm.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import com.gdiot.ssm.util.UdpConfig;

/**
 * @author ZhouHR
 */
public class UDPClient2 {
    private DatagramSocket client;

    public static void main(String[] args) {
        UDPClient2 client = new UDPClient2();
        Scanner scanner = new Scanner(System.in);
        //建立死循环，不断发送数据
        while (true) {
            String msg = scanner.nextLine();
            if ("##".equals(msg))
                break;
            //打印响应的数据
            System.out.println(client.sendAndReceive(UdpConfig.SERVICE_IP, UdpConfig.PORT, msg));
        }
    }

    public String sendAndReceive(String ip, int port, String msg) {
        String responseMsg = "";

        try {
            //创建客户端的DatagramSocket对象，不必传入地址和对象
            client = new DatagramSocket();
            byte[] sendBytes = msg.getBytes();
            //封装要发送目标的地址
            InetAddress address = InetAddress.getByName(ip);
            //封装要发送的DatagramPacket的对象，由于要发送到目的主机，所以要加上地址和端口号
            DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, address, port);

            try {
                //发送数据
                client.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] responseBytes = new byte[UdpConfig.MAX_LENGTH];
            //创建响应信息的DatagramPacket对象
            DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length);
            try {
                //等待响应信息，同服务端一样，客户端也会在这一步阻塞，直到收到一个数据包
                client.receive(responsePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //解析数据包内容
            responseMsg = new String(responsePacket.getData(), 0, responsePacket.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭客户端
            if (client != null) {
                client.close();
                client = null;
            }
        }

        return responseMsg;
    }
}
