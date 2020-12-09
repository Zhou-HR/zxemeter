package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.NBValue;

public interface NBMapper {

	int countByCondition(Map<String, Object> map);
	
	List<NBValue> selectList(Map<String, Object> map);

	int selectExist(@Param("time") long time);
	
	void insertLog(@Param("msg") String msg,@Param("duration") long duration);
	
	int countNBByCondition(Map<String, Object> map);
	
	List<NBValue> selectNBList(Map<String, Object> map);
	
	int countZZNBByCondition(Map<String, Object> map);
	
	List<NBValue> selectZZNBList(Map<String, Object> map);
	
	String getCompanyIdsByUserId(@Param("userId") String userId);
}
