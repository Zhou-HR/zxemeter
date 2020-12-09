package com.gdiot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdiot.mapper.XBEMMapper;
import com.gdiot.model.EMDayReportPo;
import com.gdiot.model.EMMeterPo;
import com.gdiot.model.XBEMDataPo;
import com.gdiot.service.IXBEMService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
@Service("XBEMService")
public class XBEMServiceImpl implements IXBEMService {

    @Autowired
    private XBEMMapper mXBEMMapper;

    @Override
    public List<EMMeterPo> selectAllEmeter() {
        log.info("begin xb selectAllEmeter ");
        return mXBEMMapper.selectAllEmeter();
    }

    @Override
    public XBEMDataPo selcetStartValue(String e_num, Long beginTime, Long endTime) {
//		log.info("<<<<begin xb selcetStartValue ");
        log.info("<<<<begin xb selcetStartValue e_num=" + e_num + ",beginTime=" + beginTime + ",endTime=" + endTime);

        List<XBEMDataPo> list = mXBEMMapper.selcetStartValue(e_num, beginTime, endTime);
//		log.info("<<<<begin xb selcetStartValue listsize="+list.size());
        if (list != null && list.size() > 0) {
//			log.info("---------------<<<<begin xb selcetStartValue list.get(0)="+list.get(0));
            XBEMDataPo mXBEMDataPo = list.get(0);
            return mXBEMDataPo;
        }
        return null;
    }

    @Override
    public XBEMDataPo selcetEndValue(String e_num, Long beginTime, Long endTime) {
//		log.info(">>>>>begin xb selcetEndValue ");
        log.info(">>>>>begin xb selcetEndValue e_num=" + e_num + ",beginTime=" + beginTime + ",endTime=" + endTime);

        List<XBEMDataPo> list = mXBEMMapper.selcetEndValue(e_num, beginTime, endTime);
//		log.info(">>>>>begin xb selcetEndValue listsize="+list.size());
        if (list != null && list.size() > 0) {
//			log.info("--------------->>>>>begin xb selcetEndValue list.get(0)="+list.get(0));
            XBEMDataPo mXBEMDataPo = list.get(0);
            return mXBEMDataPo;
        }
        return null;
    }

    @Override
    public int SaveOne(EMDayReportPo xEMDayReportPo) {
        log.info("begin xb EMDayReportPo addOne");
        return mXBEMMapper.SaveOne(xEMDayReportPo);
    }
}
