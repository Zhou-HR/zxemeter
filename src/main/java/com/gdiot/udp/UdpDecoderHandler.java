package com.gdiot.udp;

import com.gdiot.service.AsyncService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author ZhouHR
 */
@Slf4j
@Service
public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket> {

    @Autowired
    private AsyncService asyncService;

    @Override
    protected void decode(ChannelHandlerContext arg0, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        // TODO Auto-generated method stub
        log.info("解析client上报数据");
        InetSocketAddress recipen = datagramPacket.recipient();
        InetSocketAddress sender = datagramPacket.sender();
        ByteBuf byteBuf = datagramPacket.content();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);

        UdpPacketMsg udpPacketMsg = new UdpPacketMsg();
        udpPacketMsg.setServerAddress(recipen);
        udpPacketMsg.setClientAddress(sender);
        udpPacketMsg.setData(data);
        log.info("接收数据包byteBuf：" + byteBuf);
        log.info("接收数据包data：" + data);

        //String msg = new String(data);
        //LOGGER.info("{}收到消息{}:" + msg);
        out.add(udpPacketMsg); //将数据传入下一个handler

//        byte[] buffer = new byte[UdpConfig.MAX_LENGTH];
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//        receive(packet);
//        response(packet);
//        log.error("接收数据包packet：" + packet);
    }
	
	/*public void receive(DatagramPacket packet) throws Exception {
        datagramSocket.receive(packet);

        //4.从数据报包中获取数据
          byte[] data=  packet.getData();//获取数据报包中的数据
          int length = packet.getLength();//
          InetAddress ip = packet.getAddress();
          int port = packet.getPort();
          System.out.println("内容length:"+data.length);
          System.out.println("数据长度:"+length);
          System.out.println("发送方的IP地址:"+ip.toString());
          System.out.println("发送方的端口号:"+port);
          String dataStr = Utilty.parseByte2HexStr(data);
          System.out.println("接收数据包:::" + dataStr +"\n");
          
         // 发送方的IP地址:/223.104.255.74
         // 发送方的端口号:40596
          Map<String,Object> map = new HashMap<String,Object>();
          map.put("IP", ip.toString());
          map.put("port", port);
          map.put("data", dataStr);
          if(asyncService == null) {
  			asyncService =  SpringContextUtils.getBean(AsyncService.class);
  			log.info("opened asyncService create");
  		}
  		DataSenderTask task = new DataSenderTask(map,"udp_wm");
  		asyncService.executeAsync(task);
  		log.info("task: receive udp data done");
    }

    private IWMDataService mIWMDataService;
    public void response(DatagramPacket packet) throws Exception {
    	InetAddress ip = packet.getAddress();
        int port = packet.getPort();
        System.out.println("发送方的IP地址:"+ip.toString());
        System.out.println("发送方的端口号:"+port);
        
        if(mIWMDataService == null) {
			mIWMDataService =  SpringContextUtils.getBean(IWMDataService.class);
		}
        List<WMCmdDataPo> list = mIWMDataService.selectSendCmd();
        if(list != null && list.size() > 0) {
        	for(WMCmdDataPo mWMCmdDataPo :list) {
        		String content = mWMCmdDataPo.getCmdData();
        		String type = mWMCmdDataPo.getType();
        		byte[] msg_b= Utilty.hexStringToBytes(content);
        		DatagramPacket dp=new DatagramPacket(msg_b, msg_b.length,ip, port);
        		//通过udp发送
        		datagramSocket.send(dp);
        		log.info("send cmd succ type=" + type);
        		String newStatus = "0";
        		mWMCmdDataPo.setDownStatus(newStatus);
        		mIWMDataService.updateCmdStatus(mWMCmdDataPo);
        		
        		//save to db log
				WMCmdSendLogPo mWMCmdSendLogPo = new WMCmdSendLogPo();
				mWMCmdSendLogPo.setWmNum(mWMCmdDataPo.getWmNum());
				mWMCmdSendLogPo.setCmdData(content);
				mWMCmdSendLogPo.setDownIp(ip.toString());
				mWMCmdSendLogPo.setDownPort(String.valueOf(port));
				mWMCmdSendLogPo.setType(type);
				mIWMDataService.insertCmdSendLog(mWMCmdSendLogPo);
        	}
        }
    }*/

}
