package com.gdiot.service.impl;

import com.gdiot.mapper.QDEMDataMapper;
import com.gdiot.model.QDEMDataPo;
import com.gdiot.service.IQDEMDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhouHR
 * @date 2020/12/14
 */
@Slf4j
@Service("QDEMDataService")
public class QDEMDataServiceImpl implements IQDEMDataService {

    @Autowired
    private QDEMDataMapper qdemDataMapper;

    @Override
    public int addOne(QDEMDataPo qdemDataPo) {
        log.info("begin qd em data addOne");
        return qdemDataMapper.insert(qdemDataPo);
    }
}
