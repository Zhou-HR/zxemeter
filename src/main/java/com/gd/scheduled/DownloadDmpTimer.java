package com.gd.scheduled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.redis.JedisUtil;
import com.gd.util.DateUtil;
import com.gd.util.DateUtil2;


/**
 * @author ZhouHR
 */ //@Component
@Slf4j
public class DownloadDmpTimer {

    final String redisKey = "emeter-DownloadDmpTimer-tryTimes-";


    //	@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 40 7,12,15,19,23 * * ?")
    public void downLoadDmp() {

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String fileName = DateUtil.getDmpHour(new Date()) + "3001.tar.gz";
        log.info("======================start download file : " + fileName);
        try {
            URL url = new URL("http://47.103.102.210:9081/gdiot/meter/exportDmp?fileName=" + fileName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(30 * 60 * 1000);
//			conn.setReadTimeout(30000);

            inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            fileOutputStream = new FileOutputStream("d://yxl/" + fileName);
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            log.info("======================end download file : " + fileName);
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (Exception e) {

            }
        }
    }

    final static long SLEEP_TIME = 15000L;//5分钟


    @Scheduled(cron = "0 0 3 * * ?")
//	@Scheduled(cron = "0 40 7,12,15,19,23 * * ?")
    public void downLoadDmpHis() {

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String fileName = "GDMETER_HIS_" + DateUtil2.getToday2() + "-0225.tar.gz";
        String key = redisKey + fileName;
        Integer tryTimes = (Integer) JedisUtil.get(key);
        if (tryTimes == null) tryTimes = 0;//初始化
        if (tryTimes > 3) return;

        log.info("======================start download file : " + fileName);
        try {
            URL url = new URL("http://47.103.102.210:9081/gdiot/meter/exportDmp?type=his&fileName=" + fileName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30 * 60 * 1000);//20m

            inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            fileOutputStream = new FileOutputStream("d://yxl/" + fileName);
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            log.info("======================end download file : " + fileName);
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (Exception e) {

            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (Exception e) {
            }
            File file = new File("d://yxl/" + fileName);
            boolean result = true;
            if (file.exists()) {
                //判断文件大小
                Long fileLength = file.length() / 1024L / 1024L;
                if (fileLength < 450L) {//如果小于400M
                    result = false;
                    tryTimes++;
                }
            } else {
                result = false;
                tryTimes++;
            }
            JedisUtil.set(key, tryTimes, 3600 * 3);//2小时有效期
            if (!result)
                downLoadDmpHis();
        }
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getDmpHour(new Date()) + "3001.tar.gz");
        DownloadDmpTimer xx = new DownloadDmpTimer();
        xx.downLoadDmpHis();
    }
}
