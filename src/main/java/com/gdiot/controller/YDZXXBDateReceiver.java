package com.gdiot.controller;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.YDConfig;
import com.gdiot.util.YDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * 数据接收程序接口类 中性平台，芯北电表
 *
 * @author zjq
 */
@RestController
@RequestMapping("/yd")
public class YDZXXBDateReceiver {

    @Autowired
    private AsyncService asyncService;

    private static final Logger logger = LoggerFactory.getLogger(YDZXXBDateReceiver.class);

    /**
     * 功能描述：第三方平台数据接收。<p>
     * <ul>注:
     *     <li>1.OneNet平台为了保证数据不丢失，有重发机制，如果重复数据对业务有影响，数据接收端需要对重复数据进行排除重复处理。</li>
     *     <li>2.OneNet每一次post数据请求后，等待客户端的响应都设有时限，在规定时限内没有收到响应会认为发送失败。
     *          接收程序接收到数据时，尽量先缓存起来，再做业务逻辑处理。</li>
     * </ul>
     *
     * @param body 数据消息
     * @return 任意字符串。OneNet平台接收到http 200的响应，才会认为数据推送成功，否则会重发。
     */
    @PostMapping(value = "/receive")
    public String receive(@RequestBody String body) {

        logger.info("data receive:  body String --- " + body);
        /*
         *  解析数据推送请求，非加密模式。
         *  如果是明文模式使用以下代码
         */
        /*明文模式  start*/
        YDUtil.BodyObj obj = YDUtil.resolveBody(body, false);
        if (obj != null) {
            boolean dataRight = YDUtil.checkSignature(obj, YDConfig.ZX_XB_EM_TOKEN);
            if (dataRight) {

                DataSenderTask task = new DataSenderTask(body, "nb");
                asyncService.executeAsync(task);
                logger.info("task: recvUpdateDeviceDatasNotify done");
            } else {
                logger.info("data receive: signature error");
            }
        } else {
            logger.info("data receive: body empty error");
        }
        /*明文模式  end*/


        /*
         *  解析数据推送请求，加密模式
         *
         *  如果是加密模式使用以下代码
         */
        /*加密模式  start*/
        /*加密模式  end*/
        return "ok";
    }

    /**
     * 功能说明： URL&Token验证接口。如果验证成功返回msg的值，否则返回其他值。
     *
     * @param msg       验证消息
     * @param nonce     随机串
     * @param signature 签名
     * @return msg值
     */

    @GetMapping(value = "/receive")
    public String check(@RequestParam(value = "msg") String msg,
                        @RequestParam(value = "nonce") String nonce,
                        @RequestParam(value = "signature") String signature) throws UnsupportedEncodingException {

        logger.info("url&token check: msg:{} nonce{} ", msg, nonce);
        logger.info("url&token check: signature:{}", signature);
        if (YDUtil.checkToken(msg, nonce, signature, YDConfig.ZX_XB_EM_TOKEN)) {
            return msg;
        } else {
            return "error";
        }
    }
}

