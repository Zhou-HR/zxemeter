package com.gdiot.controller;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.YDConfig;
import com.gdiot.util.YDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 数据接收程序接口类 千丁电表
 *
 * @author ZhouHR
 * @date 2020/12/11
 */
@RestController
@RequestMapping("/qd")
public class YDQDDateReceiver {

    @Autowired
    private AsyncService asyncService;

    private static Logger logger = LoggerFactory.getLogger(YDZXXBDateReceiver.class);

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
    public String receive(@RequestBody String body) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        logger.info("data receive:  body String --- " + body);
        /************************************************
         *  解析数据推送请求，非加密模式。
         *  如果是明文模式使用以下代码
         **************************************************/
        /*************明文模式  start****************/
        YDUtil.BodyObj obj = YDUtil.resolveBody(body, false);
//        logger.info("data receive:  body Object --- " +obj);
        //body Object --- { "msg":{"dev_id":518766092,"imei":"866971030431923","value":"123456789","at":1552029366942,"ds_id":"3200_0_5750","type":1}，"nonce":h9(2&z&z，"signature":sq56j/8xiIRMqJwYv7gt1w==}
        if (obj != null) {
            boolean dataRight = true;
            //boolean dataRight = YDUtil.checkSignature(obj, YDConfig.QD_TOKEN);
            if (dataRight) {
                // body String --- {"msg":{"at":1552274056993,"imei":"866971031491171","type":1,"ds_id":"3200_0_5750","value":"0000350001350100000101FF724039221000000087220000081700000150000073810300747400009788030081090000000212180118F4","dev_id":518816929},"msg_signature":"36TD6jzIr1ntIzbn7dcRcw==","nonce":"I$TLyP&f"}
//            	logger.info("data receive: content" + obj.toString());
//            	logger.info("data receive: getMsg length:"+obj.getMsg().toString().length());
//            	logger.info("data receive: getMsg:"+obj.getMsg().toString());

                DataSenderTask task = new DataSenderTask(body, "qd");
                asyncService.executeAsync(task);
//            	task.run();
                logger.info("task: recvUpdateDeviceDatasNotify done");
            } else {
                logger.info("data receive: signature error");
            }
        } else {
            logger.info("data receive: body empty error");
        }
        /*************明文模式  end****************/


        /********************************************************
         *  解析数据推送请求，加密模式
         *
         *  如果是加密模式使用以下代码
         ********************************************************/
        /*************加密模式  start****************/
//        Util.BodyObj obj1 = Util.resolveBody(body, true);
//        logger.info("data receive:  body Object--- " +obj1);
//        if (obj1 != null){
//            boolean dataRight1 = Util.checkSignature(obj1, token);
//            if (dataRight1){
//                String msg = Util.decryptMsg(obj1, aeskey);
//                logger.info("data receive: content" + msg);
//            }else {
//                logger.info("data receive:  signature error " );
//            }
//        }else {
//            logger.info("data receive: body empty error" );
//        }
        /*************加密模式  end****************/
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
        if (YDUtil.checkToken(msg, nonce, signature, YDConfig.QD_TOKEN)) {
            return msg;
        } else {
            return "error";
        }
    }
}
