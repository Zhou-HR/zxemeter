package com.gdiot.ssm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.ssm.entity.YDEMeterDataPo;

public interface NBYDEMeterDataMapper {

	int countByCondition(@Param("dev_id")String dev_id,@Param("e_num")String eNum,@Param("imei")String imei, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source);
	List<YDEMeterDataPo> selectList(@Param("dev_id")String deviceNo,@Param("e_num")String eNum,@Param("imei")String imei, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source,@Param("limit")int limit,@Param("offset")int offset);
	
	int insertOne(YDEMeterDataPo mYDEMeterDataPo);

	List<YDEMeterDataPo> selectbyimei(@Param("imei")String imei);
	
	int updateData(YDEMeterDataPo mYDEMeterDataPo);
	List<YDEMeterDataPo> selectbyseq(@Param("imei")String imei,@Param("e_seq")long e_seq);
}