package com.gdiot.controller;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.AuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 数据接收程序接口类
 *
 * @author zjq
 */
@RestController
@RequestMapping("/jbq")
public class KTDateReceiver {
    @Autowired
    private AsyncService asyncService;

    private static Logger logger = LoggerFactory.getLogger(YDAKRDateReceiver.class);

    @PostMapping(value = "/receiveData")
    public void receive(@RequestBody String body) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        logger.info("kt data receive:  body String --- " + body);
        AuthenticationUtil.getAccessToken();
        DataSenderTask task = new DataSenderTask(body, "kt_nb_em");
        asyncService.executeAsync(task);
        logger.info("task: recvUpdateDeviceDatasNotify done");


    }
}
