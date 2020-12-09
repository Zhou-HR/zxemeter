package com.gdiot.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UdpHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(UdpHandler.class);
	
	private HashMap<String, UdpPacketMsg> clientMap = new HashMap<String, UdpPacketMsg>();
	private ChannelHandlerContext conetxt;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //String preHandlerAfferentMsg = (String)msg; //得到消息后，可根据消息类型分发给不同的service去处理数据
    	
    	UdpPacketMsg udpPacketMsg = (UdpPacketMsg)msg;
    	
        String preHandlerAfferentMsg = new String(udpPacketMsg.getData());
        
        log.info("{}preHandler传入的数据{}"+preHandlerAfferentMsg);
        //ctx.writeAndFlush("hello word"); //返回数据给UDP Client
        
        if(/*clientMap.get("000000000000000001") != null*/clientMap.get(preHandlerAfferentMsg) != null) {
        	//UdpPacketMsg ttemp  = clientMap.get("000000000000000001");
        	UdpPacketMsg ttemp  = clientMap.get(preHandlerAfferentMsg);
        	ttemp.setClientAddress(udpPacketMsg.getClientAddress());
        	ttemp.setServerAddress(udpPacketMsg.getServerAddress());
        	setConetxt(ctx);
        }else {
        	udpPacketMsg.setData(null);
            setConetxt(ctx);
        	//clientMap.put("000000000000000001", udpPacketMsg);
            clientMap.put(preHandlerAfferentMsg, udpPacketMsg);
        }
        
        log.info("channelRead");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        ctx.fireChannelInactive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete");
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered");
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged");
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.info("exceptionCaught");
        ctx.fireExceptionCaught(cause);
    }

	public HashMap<String, UdpPacketMsg> getClientMap() {
		return clientMap;
	}

	public ChannelHandlerContext getConetxt() {
		return conetxt;
	}

	public void setConetxt(ChannelHandlerContext conetxt) {
		this.conetxt = conetxt;
	}
    
}