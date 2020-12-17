package com.gdiot.mapper;

import com.gdiot.model.AKREMReadPo;
import com.gdiot.model.QDEMDataPo;
import com.gdiot.model.QDEMReadPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface QDEMDataMapper {
    /**
     * @param mAKREMReadPo
     * @return
     */
    int insertReadData(QDEMReadPo mQDEMReadPo);
}