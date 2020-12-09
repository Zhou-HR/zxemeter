package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.Meter;
import com.gd.model.po.MeterMaintain;
import com.gd.model.po.SimpleDict;

/**
 * @author ZhouHR
 */
public interface MeterPersonMapper {

    int countByCondition(Map<String, Object> map);

    List<Meter> selectList(Map<String, Object> map);

    Meter findById(@Param("id") int id);

    Meter findByMeterNo(@Param("meterNo") String meterNo);

    List<SimpleDict> findPersonById(@Param("id") int id);

    void updateMeterPerson(@Param("id") int id, @Param("userCode") String userCode);

    MeterMaintain findMeterMaintainByMeterId(@Param("id") int id, @Param("userCode") String userCode, @Param("sendDate") String sendDate);

    void insert(MeterMaintain meterMaintain);

    void updateReasonById(MeterMaintain meterMaintain);

    void updateResultById(MeterMaintain meterMaintain);

}
