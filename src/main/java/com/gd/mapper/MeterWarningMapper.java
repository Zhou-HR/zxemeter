package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.MeterProperty;
import com.gd.model.po.MeterWarning;
import com.gd.model.po.NBValue;
import com.gd.model.po.OfflineReport;

public interface MeterWarningMapper {

	int countByCondition(Map<String, Object> map);
	
	List<MeterWarning> selectList(Map<String, Object> map);
	
	List<MeterWarning> selectExportList(Map<String, Object> map);
	
	List<MeterWarning> selectMeterWarning(@Param("userId") int userId,@Param("hour") int hour);
	
	List<MeterProperty> getMeterProperty();
	
	List<MeterWarning> select10Warning();
	
	int get24hrWarningNum();
	
	void setMeterProperty(@Param("id") int id,@Param("value") String value);
	
	List<OfflineReport> offlineReport();
	
	OfflineReport powerOff(@Param("meterNo") String meterNo,@Param("lastSendTime") String lastSendTime);
	
	List<NBValue> offLineDeal(@Param("meterNo") String meterNo,@Param("lastSendTime") String lastSendTime,@Param("companyId") String companyId);
	
	void insert(MeterWarning meterWarning);
	
	List<MeterWarning> selectMeterEvent();
	
	void updateDealMeterEvent(@Param("id")int id);
	
	List<String> selectMeterOnline();
	
	void updateMeterStatus(@Param("meterNo")String meterNo,@Param("status")int status);
	
	void updateMeterOffline();
	
	List<MeterWarning> selectListLahezha();
	
	void updateMeterLahezha(@Param("lahezhaStatus")Integer lahezhaStatus,@Param("meterNo")String meterNo);
}
