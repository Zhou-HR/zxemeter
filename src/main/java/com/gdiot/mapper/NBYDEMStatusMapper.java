package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.YDEMeterStatusPo;

public interface NBYDEMStatusMapper {

	int insertStatus(YDEMeterStatusPo mYDEMeterStatusPo);
	int updateEMStatus(YDEMeterStatusPo mYDEMeterStatusPo);
	List<YDEMeterStatusPo> selectDevid(@Param("dev_id")String dev_id,@Param("imei")String imei);
	
	int countByCondition(@Param("dev_id")String dev_id,@Param("imei")String imei,@Param("status")String status);
	List<YDEMeterStatusPo> selectStatus(@Param("dev_id")String dev_id,@Param("imei")String imei,@Param("status")String status,@Param("limit")int limit,@Param("offset")int offset);
}
