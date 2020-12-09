package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMCmdSendLogPo;
import com.gdiot.model.WMDataPo;
import com.gdiot.model.WMReadDataPo;

public interface WMDataMapper {
	
	int insertOne(WMDataPo wMDataPo);

	int countbyDevId(@Param("dev_id")String dev_id);
	List<WMDataPo> selectbyDevId(@Param("dev_id")String dev_id,@Param("limit")int limit,@Param("offset")int offset);
	
	List<WMDataPo> selectbyNum(@Param("wm_num")String wm_num);
	
	int insertCmdSendData(WMCmdDataPo mWMCmdDataPo);
	int updateCmdSendStatus(WMCmdDataPo mWMCmdDataPo);
	List<WMCmdDataPo> selectCmdSendData();
	
	int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo);
	
	int insertReadData(WMReadDataPo mWMReadDataPo);
	List<WMReadDataPo> selectReadData(@Param("wm_num")String wm_num,@Param("type")String type);
}
