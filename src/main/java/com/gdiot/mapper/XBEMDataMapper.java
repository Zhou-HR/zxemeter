package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.XBEMDataPo;

public interface XBEMDataMapper {
	int insertOne(XBEMDataPo xBEMDataPo);
	
	List<XBEMDataPo> selectList(@Param("dev_id")String dev_id,@Param("e_num")String e_num, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source,@Param("limit")int limit,@Param("offset")int offset);
	int countByCondition(@Param("dev_id")String dev_id,@Param("e_num")String e_num, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source);
	
	List<XBEMDataPo> selectOne(@Param("dev_id")String dev_id,@Param("e_seq")long e_seq);
}