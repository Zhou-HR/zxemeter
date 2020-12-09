package com.gd.scheduled;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.mapper.MeterWarningMapper;
import com.gd.model.po.MeterWarning;


/**
 * @author ZhouHR
 */
@Component
@Slf4j
public class LahezhaCheckTimer {

    @Autowired
    private MeterWarningMapper meterWarningMapper;

    @Scheduled(cron = "0 0/1 * * * ?")//1分钟判断一次?
    public void syncErpUserSchedule() {
        log.info("start check meter Lahezha!!!");

        long duration = 0;
        String name = "check meter Lahezha";
        String msg = "success";
        int count = 0;
        long start = System.currentTimeMillis();
        try {
            List<MeterWarning> list = meterWarningMapper.selectListLahezha();
            for (MeterWarning meterWarning : list) {
                if ("55".equals(meterWarning.getEswitch())) {
                    meterWarningMapper.updateMeterLahezha(0, meterWarning.getMeterNo());
                }
                if ("AA".equals(meterWarning.getEswitch())) {
                    meterWarningMapper.updateMeterLahezha(1, meterWarning.getMeterNo());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
        }

        log.info("end check meter Lahezha successfully!!!");
    }


}
