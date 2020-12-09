package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.MeterMaintain;
import com.gd.model.po.MeterMaintainPic;

public interface MeterMaintainMapper {

	int countByCondition(Map<String, Object> map);
	
	List<MeterMaintain> selectList(Map<String, Object> map);
	
	MeterMaintain findById(@Param("id") int id);
	
	void insertMaintainPic(MeterMaintainPic meterMaintainPic);
	
	void updateMaintainPicReset(@Param("id") int id);
	
	void updateMaintainPic(@Param("imgs") String imgs,@Param("id") int id);
	
	List<MeterMaintainPic> selectMaintainPic(@Param("id") int id);

}
