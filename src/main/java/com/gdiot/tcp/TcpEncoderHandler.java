package com.gdiot.tcp;

import java.util.List;

import org.springframework.stereotype.Service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TcpEncoderHandler extends MessageToMessageEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object o, List<Object> list) throws Exception {
		byte[] data = o.toString().getBytes("utf-8");
    	ByteBuf buf = ctx.alloc().buffer(data.length);
    	buf.writeBytes(data);
    	list.add(buf);
        log.info("{TCP 下行 send data }: "+o.toString());
	}

}
