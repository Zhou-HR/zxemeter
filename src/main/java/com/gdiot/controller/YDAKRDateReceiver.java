package com.gdiot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.ResultObject;
import com.gdiot.util.YDConfig;
import com.gdiot.util.YDUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 数据接收程序接口类
 *
 * @author zjq
 */
@Controller
@RequestMapping("/akr")
public class YDAKRDateReceiver {

    @Autowired()
    private AsyncService asyncService;

    private static Logger logger = LoggerFactory.getLogger(YDAKRDateReceiver.class);

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
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @ResponseBody
    public String receive(@RequestBody String body) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        logger.info("akr data receive:  body String --- " + body);
        /************************************************
         *  解析数据推送请求，非加密模式。
         *  如果是明文模式使用以下代码
         **************************************************/
        /*************明文模式  start****************/
        YDUtil.BodyObj obj = YDUtil.resolveBody(body, false);
//        logger.info("data receive:  body Object --- " +obj);
        //body Object --- { "msg":{"dev_id":518766092,"imei":"866971030431923","value":"123456789","at":1552029366942,"ds_id":"3200_0_5750","type":1}，"nonce":h9(2&z&z，"signature":sq56j/8xiIRMqJwYv7gt1w==}
        if (obj != null) {
            boolean dataRight = YDUtil.checkSignature(obj, YDConfig.AKR_TOKEN);
            if (dataRight) {
                // body String --- {"msg":{"at":1552274056993,"imei":"866971031491171","type":1,"ds_id":"3200_0_5750","value":"0000350001350100000101FF724039221000000087220000081700000150000073810300747400009788030081090000000212180118F4","dev_id":518816929},"msg_signature":"36TD6jzIr1ntIzbn7dcRcw==","nonce":"I$TLyP&f"}
//            	logger.info("data receive: content" + obj.toString());
//            	logger.info("data receive: getMsg length:"+obj.getMsg().toString().length());
//            	logger.info("data receive: getMsg:"+obj.getMsg().toString());

                //DataSenderTask run-data :{"msg":{"at":1594199318758,"imei":"864162044318999","type":1,"ds_id":"3300_0_5706","value":"7b7b9131323030363133303934303030320000000000005b5b312d31282801034c000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003e803e803e803e8a3c029295d5d5b5b312d32282801030c000000000000000000000000937029295d5de6c47d7d","dev_id":606702219},"msg_signature":"Pc8Qzrsx3Z7OSW5qXbeu2w==","nonce":"XC_y!ljh"}
//            	DataSenderTask task = new DataSenderTask(body,"akr_nb_em");
                DataSenderTask task = new DataSenderTask(body, "akr_nb_em_single");//akr_nb_em_single
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

    @RequestMapping(value = "/receive", method = RequestMethod.GET)
    @ResponseBody
    public String check(@RequestParam(value = "msg") String msg,
                        @RequestParam(value = "nonce") String nonce,
                        @RequestParam(value = "signature") String signature) throws UnsupportedEncodingException {

        logger.info("akr url&token check: msg:{} nonce{} ", msg, nonce);
        logger.info("akr url&token check: signature:{}", signature);
        if (YDUtil.checkToken(msg, nonce, signature, YDConfig.AKR_TOKEN)) {
            return msg;
        } else {
            return "error";
        }
    }
}

