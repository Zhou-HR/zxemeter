package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.Company;
import com.gd.model.po.Station;
import com.gd.model.po.TextValue;

public interface StationMapper {

	int countByCondition(Map<String, Object> map);
	
	List<Station> selectList(Map<String, Object> map);
	
	List<Company> getCompanyNum();
	
//	void updateCarrier(@Param("ydPart") String ydPart,@Param("dxPart") String dxPart,@Param("ltPart") String ltPart,
//	@Param("ydPrice") String ydPrice,@Param("dxPrice") String dxPrice,@Param("ltPrice") String ltPrice,
//	@Param("companyId") String companyId,@Param("projectNo") String projectNo);
	
	void updateCarrier1(@Param("carrierName") String carrierName,@Param("carrierPart") String carrierPart,
			@Param("carrierPrice") String carrierPrice,
			@Param("companyId") String companyId,@Param("projectNo") String projectNo,@Param("userId") String userId);
	
	void restoreDefault(
			@Param("companyId") String companyId,@Param("projectNo") String projectNo,@Param("userId") String userId);

	List<TextValue> getCarrier();
}
