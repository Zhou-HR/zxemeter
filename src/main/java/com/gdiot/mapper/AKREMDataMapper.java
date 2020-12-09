package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.AKREMDataPo;
import com.gdiot.model.AKREMEventPo;
import com.gdiot.model.AKREMReadPo;

public interface AKREMDataMapper {
	int insertOne(AKREMDataPo xAKREMDataPo);
	
	List<AKREMDataPo> selectList(@Param("dev_id")String dev_id,@Param("e_num")String e_num, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source,@Param("limit")int limit,@Param("offset")int offset);
	int countByCondition(@Param("dev_id")String dev_id,@Param("e_num")String e_num, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime,@Param("source")String source);
	
	List<AKREMDataPo> selectOne(@Param("dev_id")String dev_id,@Param("e_seq")long e_seq);
	
	int insertReadData(AKREMReadPo mAKREMReadPo);
	List<AKREMReadPo> selectRead(@Param("imei")String imei,@Param("e_num")String e_num,@Param("type")String type);
	
	int insertEventData(AKREMEventPo mAKREMEventPo);
	List<AKREMEventPo> selectEvent(@Param("imei")String imei,@Param("e_num")String e_num,@Param("type")String type);
}
