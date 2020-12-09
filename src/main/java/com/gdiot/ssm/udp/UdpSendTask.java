package com.gdiot.ssm.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import com.gdiot.ssm.util.Utilty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpSendTask implements Runnable {
	private ArrayList<DatagramPacket> downDataList = new ArrayList<DatagramPacket>();
    private DatagramSocket datagramSocket;
    
    public void setDatagramSocket(DatagramSocket datagramSocket){
    	this.datagramSocket = datagramSocket;
    }
    
    /**
     * 监听发送数据
     */
    public void run() {
        try {
            while(true){
                try {
                    try {
        				sendMsgDO();
        				Thread.sleep(100);
        			} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("udp线程出现异常：" + e.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void response(DatagramPacket packet) throws Exception {
        datagramSocket.send(packet);
    }
    
    //发送数据
    public void send(String IP,int port,String msg) throws SocketException, UnknownHostException,IOException {
        byte[] msg_b= Utilty.hexStringToBytes(msg);
        DatagramPacket dp=new DatagramPacket(msg_b, msg_b.length,InetAddress.getByName(IP), port);
        //通过udp发送
        datagramSocket.send(dp);
    }
    
    public boolean sendMsg(String IP,int port,String msg) throws SocketException, UnknownHostException,IOException {
		byte[] msg_b= Utilty.hexStringToBytes(msg);
        DatagramPacket dp=new DatagramPacket(msg_b, msg_b.length,InetAddress.getByName(IP), port);
		synchronized(downDataList){
			downDataList.add(dp);
		}
		return false;
	}
    //批量发送数据
	private void sendMsgDO() throws SocketException, UnknownHostException,IOException {
		synchronized(downDataList){
			int msgLen = downDataList.size();
			while(msgLen > 0){
				datagramSocket.send(downDataList.remove(--msgLen));
			}
		}
	}
}