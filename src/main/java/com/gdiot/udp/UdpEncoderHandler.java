package com.gdiot.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;

@Service
public class UdpEncoderHandler extends MessageToMessageEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpEncoderHandler.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List list) throws Exception {

        LOGGER.info("{}发送消息{}:" );
    	UdpPacketMsg temp = (UdpPacketMsg)o;
    	
        //byte[] data = o.toString().getBytes();
    	byte[] data = temp.getData();
        ByteBuf buf = ctx.alloc().buffer(data.length);
        buf.writeBytes(data);
        //InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 10000);//指定客户端的IP及端口
        InetSocketAddress inetSocketAddress = temp.getClientAddress();
        list.add(new DatagramPacket(buf, inetSocketAddress));
        LOGGER.info("{}发送消息{}:" + new String(data));
    }
}