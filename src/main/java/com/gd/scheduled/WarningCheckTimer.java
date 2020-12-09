package com.gd.scheduled;

import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.mapper.MeterWarningMapper;
import com.gd.mapper.UserMapper;
import com.gd.model.po.MeterWarning;
import com.gd.model.po.User;
import com.gd.util.DateUtil;
import com.gd.util.DateUtil2;
import com.gd.util.DingDingUtil;
import com.gd.util.EmailUtil;
import com.gd.util.JdbcSchedularLog;
import com.gd.util.PerformanceUtil;
import com.gd.util.PropertiesUtil;

/**
 * @author ZhouHR
 */
@Component
@Slf4j
public class WarningCheckTimer {

    static final Long TimeLevel2 = 60l * 80;//80分钟

    private static String warning_emeter_url = (String) PropertiesUtil.getInstance().getProperty("interface.properties", "warning_emeter_url");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MeterWarningMapper meterWarningMapper;


    @Scheduled(cron = "0 5 * * * ?")//每小时的5分钟
//	@Scheduled(cron = "0/5 * * * * ?")
    public void sendUserEmailSchedule() {
        log.info("start send user email!!!");

        List<User> list = userMapper.getUserEmailNotNull();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        for (User user : list) {
            String everyHour = user.getEveryHour();
            String strHour = user.getHour();
            //测试发送
//			sendEmail(user,10);
            if (strHour != null) {
                int userHour = Integer.valueOf(strHour);
                System.out.println(userHour == hour);
                if (userHour == hour) {
                    System.out.println(userHour == hour);
                    //sendEmail
                    sendEmail(user, 24);
                }
            } else {
                int neveryHour = Integer.valueOf(everyHour);
                int i = 1;
                int time = neveryHour;
                while (time < 24) {
                    if (time == hour) {
                        //sendEmail
                        sendEmail(user, neveryHour);
                    }
                    i++;
                    time = i * neveryHour;
                }
            }

        }
        log.info("end send user email!!!");
    }

    private void sendEmail(User user, int hour) {
        try {
            List<MeterWarning> list = meterWarningMapper.selectMeterWarning(user.getId(), hour);
            String content = "请登录智能电表系统查看详细信息。";// 内容
            StringBuilder sb = new StringBuilder();
            if (list.size() > 0) {
                for (MeterWarning meterWarning : list) {
                    sb.append("电表：").append(meterWarning.getMeterNo()).append("，在").append(DateUtil.getTime(meterWarning.getCreateTime()))
                            .append("，发出" + meterWarning.getDescription() + "警报！").append("<br>");
                }
                sb.append(content);

                //发送钉钉消息
//				DingDingUtil.send(user.getName(),user.getDdUserid(), sb.toString());

                EmailUtil.send(null, sb.toString(), user.getEmail());
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int g_num = 0;

    @Scheduled(cron = "0 0/1 * * * ?")//0 0?
//	@Scheduled(cron = "0/5 * * * * ?")//0 0?
    public void syncMeterEvent() {
        log.info("start syncMeterEvent time!!!");
        g_num++;
        long duration = 0;
        String name = "syncMeterEvent";
        String msg = "success";
        int count = 0;
        long start = System.currentTimeMillis();
        try {
            List<MeterWarning> list = meterWarningMapper.selectMeterEvent();

            long now = System.currentTimeMillis() / 1000;

            int id = 0;
            int i = 0;
            for (MeterWarning meterWarning : list) {

                if (i == 0) id = meterWarning.getId();//获取最大id 最后批量更新用
                i++;
                String description = meterWarning.getDescription();
                switch (description) {
                    case "power_cut":
                        meterWarning.setDescription("断电");
                        break;
                    case "B1":
                        meterWarning.setDescription("断电");
                        break;
                    case "B3":
                        meterWarning.setDescription("拉合闸");
                        break;
                    case "B5":
                        meterWarning.setDescription("过流");
                        break;
                    case "B7":
                        meterWarning.setDescription("过压");
                        break;
                    case "B9":
                        meterWarning.setDescription("欠压");
                        break;
                }

                {
                    //时间格式转化一下下
                    if (StringUtils.isEmpty(meterWarning.getEstarttime()))
                        meterWarning.setEstarttime("");
                    else {
                        String startTime = meterWarning.getEstarttime();
                        startTime = DateUtil2.dealWarningTime(startTime);
                        meterWarning.setEstarttime(startTime);
                    }
                    if (StringUtils.isEmpty(meterWarning.getEendtime()))
                        meterWarning.setEendtime("");
                    else {
                        String endTime = meterWarning.getEendtime();
                        endTime = DateUtil2.dealWarningTime(endTime);
                        meterWarning.setEendtime(endTime);
                    }

                    meterWarningMapper.insert(meterWarning);
                }
            }
            meterWarningMapper.updateDealMeterEvent(id);

        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
            if (g_num % 60 == 1) {
                duration = PerformanceUtil.spendTime(start);
                JdbcSchedularLog.insert(name, msg, duration, count);
            }
        }

        log.info("end syncMeterEvent successfully!!!");
    }

    @Scheduled(cron = "0 * * * * ?")//0 0?
//	@Scheduled(cron = "0/5 * * * * ?")
    public void meterOnline() {
        log.info("start check meter Online!!!");

        long duration = 0;
        String name = "check meter Online";
        String msg = "success";
        int count = 0;
        long start = System.currentTimeMillis();
        try {
            List<String> list = meterWarningMapper.selectMeterOnline();
            for (String meterNo : list) {
                count++;
                meterWarningMapper.updateMeterStatus(meterNo, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
            duration = PerformanceUtil.spendTime(start);
            JdbcSchedularLog.insert(name, msg, duration, count);
        }

        log.info("end check meter Online successfully!!!");

    }

    @Scheduled(cron = "0 * * * * ?")//0 0?
//	@Scheduled(cron = "0/5 * * * * ?")
    public void meterOffline() {
        log.info("start check meter Offline!!!");

        long duration = 0;
        String name = "check meter Offline";
        String msg = "success";
        int count = 0;
        long start = System.currentTimeMillis();
        try {
            meterWarningMapper.updateMeterOffline();
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
            duration = PerformanceUtil.spendTime(start);
            JdbcSchedularLog.insert(name, msg, duration, count);
        }

        log.info("end check meter Offline successfully!!!");

    }

}
