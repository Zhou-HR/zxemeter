package com.gd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gd.mapper.MeterMapper;
import com.gd.model.po.Meter;

/**
 * @author ZhouHR
 */
@Service
public class MeterService {

    @Autowired
    private MeterMapper meterMapper;

    @Transactional
    public int insertMeter(List<Meter> list) {
        int count = 0;
        for (Meter meter : list) {
            if ("lora".equals(meter.getMeterType())) meter.setPlatform("物联网");
            if ("nb".equals(meter.getMeterType())) meter.setPlatform("移动");
            if ("2g".equals(meter.getMeterType())) meter.setPlatform("MQTT");

            meterMapper.insertMeter(meter);
            count++;
        }
        return count;
    }

}
