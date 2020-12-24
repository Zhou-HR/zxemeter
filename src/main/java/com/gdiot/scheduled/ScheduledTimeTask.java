package com.gdiot.scheduled;

import com.gdiot.mapper.XBEMMapper;
import com.gdiot.model.EMMeterPo;
import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */ //@Component
@Slf4j
public class ScheduledTimeTask implements ApplicationRunner {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private XBEMMapper mXBEMMapper;

    @Override
    public void run(ApplicationArguments args) {
        // 1，查出所有电表表号
        // 2，循环，进入线程池。
        // 3,根据表号，当前日期，查前24小时内最早的一条记录和最晚的一条记录，两者相减，得出一天的用电量,保存
        // 注：此处使用注解@Component，开机会执行一次，去掉注解将不执行
        long currentTime = System.currentTimeMillis();

        List<EMMeterPo> meterList = mXBEMMapper.selectAllEmeter();
        for (EMMeterPo emMeterPo : meterList) {
            String e_num = emMeterPo.getMeterNo();
            log.info("查询表号 e_num=" + e_num);

            Map<String, Object> map = new HashMap<>();
            map.put("time", currentTime);
            map.put("e_num", e_num);
            map.put("EMMeterPo", emMeterPo);
            DataSenderTask task = new DataSenderTask(map, "selectEMDayValue");
            asyncService.executeAsync(task);
        }
    }


}
