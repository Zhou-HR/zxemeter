package com.gdiot.service;

import java.util.List;
import java.util.Map;

import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMCmdSendLogPo;
import com.gdiot.model.WMDataPo;
import com.gdiot.model.WMReadDataPo;

/**
 * @author ZhouHR
 */
public interface IWMDataService {

    int addOne(WMDataPo WMDataPo);

    List<WMDataPo> selectbyDevId(String dev_id, int pageNo, int pageSize);

    List<WMDataPo> selectbyNum(String wm_num);

    int insertCmdData(WMCmdDataPo mWMCmdDataPo);

    int updateCmdStatus(WMCmdDataPo mWMCmdDataPo);

    List<WMCmdDataPo> selectSendCmd();

    int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo);

    int insertReadData(WMReadDataPo mWMReadDataPo);

    List<WMReadDataPo> selectReadData(String wm_num, String type);
}
