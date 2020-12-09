package com.gd.mapper;

import java.util.List;
import java.util.Map;

import com.gd.model.po.MeterSim;

/**
 * @author ZhouHR
 */
public interface MeterSimMapper {

    int countByCondition(Map<String, Object> map);

    List<MeterSim> selectList(Map<String, Object> map);

}
