package com.gdiot.ssm.service;

import java.util.List;
import java.util.Map;

import com.gdiot.ssm.entity.WMCmdDataPo;
import com.gdiot.ssm.entity.WMCmdSendLogPo;
import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.entity.WMReadDataPo;

public interface IWMDataService {

	int addOne(WMDataPo WMDataPo);
	List<WMDataPo> selectbyDevId(String dev_id,int pageNo,int pageSize);
	List<WMDataPo> selectbyNum(String wm_num);
	
	int insertCmdData(WMCmdDataPo mWMCmdDataPo);
	int updateCmdStatus(WMCmdDataPo mWMCmdDataPo);
	List<WMCmdDataPo> selectSendCmd();
	
	int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo);
	
	int insertReadData(WMReadDataPo mWMReadDataPo);
	List<WMReadDataPo> selectReadData(String wm_num,String type);
}
