package com.gd.mapper;

import java.util.List;
import java.util.Map;

import com.gd.model.po.NBSendPackage;

public interface NBSendPackageMapper {

	int countByCondition(Map<String, Object> map);
	
	List<NBSendPackage> selectList(Map<String, Object> map);


	
}
