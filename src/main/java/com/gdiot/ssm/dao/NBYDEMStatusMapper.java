package com.gdiot.ssm.dao;

import com.gdiot.ssm.entity.YDEMeterStatusPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface NBYDEMStatusMapper {

    int insertStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    int updateEMStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    List<YDEMeterStatusPo> selectDevid(@Param("dev_id") String dev_id, @Param("imei") String imei);

    int countByCondition(@Param("dev_id") String dev_id, @Param("imei") String imei, @Param("status") String status);

    List<YDEMeterStatusPo> selectStatus(@Param("dev_id") String dev_id, @Param("imei") String imei, @Param("status") String status, @Param("limit") int limit, @Param("offset") int offset);
}
