package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.EMDayReportPo;
import com.gdiot.model.EMMeterPo;
import com.gdiot.model.XBEMDataPo;

public interface XBEMMapper {
	
	List<EMMeterPo> selectAllEmeter();
	
	List<XBEMDataPo> selcetStartValue(@Param("e_num")String dev_id, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime);
	
	List<XBEMDataPo> selcetEndValue(@Param("e_num")String dev_id, @Param("beginTime")Long beginTime, @Param("endTime")Long endTime);
	
	int SaveOne(EMDayReportPo xEMDayReportPo);
}
