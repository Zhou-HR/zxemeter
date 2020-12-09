package com.gdiot.service;

import java.util.List;

import com.gdiot.model.EMDayReportPo;
import com.gdiot.model.EMMeterPo;
import com.gdiot.model.XBEMDataPo;

/**
 * @author ZhouHR
 */
public interface IXBEMService {

    List<EMMeterPo> selectAllEmeter();

    XBEMDataPo selcetStartValue(String e_num, Long beginTime, Long endTime);

    XBEMDataPo selcetEndValue(String e_num, Long beginTime, Long endTime);

    int SaveOne(EMDayReportPo xEMDayReportPo);
}
