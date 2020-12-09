package com.gdiot.tcp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.DateUtil;
import com.gdiot.util.SpringContextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Sharable
@Slf4j
@Service
public class TcpDecoderHandler extends MessageToMessageDecoder {

    @Autowired
    private AsyncService asyncService;

    @Override
    protected void decode(ChannelHandlerContext arg0, Object o, List list) throws Exception {
        // TODO Auto-generated method stub
        log.info("解析client上报数据");
        log.info("解析client上报数据arg0.name():" + arg0.name());

        ByteBuf byteBuf = (ByteBuf) o;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
//        log.info("{解析TCP client上报数据}Hex2: " + DateUtil.toHexString(data));
        String msg = DateUtil.toHexString(data);//new String(data,"utf-8");
        log.info("{解析TCP client上报数据}Hex: " + msg);
        list.add(msg);

        if (asyncService == null) {
            asyncService = SpringContextUtils.getBean(AsyncService.class);
            log.info("opened asyncService create");
        }
        DataSenderTask task = new DataSenderTask(msg, "akr_2g_em");
        asyncService.executeAsync(task);
        log.info("receive tcp data done");
    }
}
