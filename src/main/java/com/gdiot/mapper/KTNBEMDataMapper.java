package com.gdiot.mapper;


import com.gdiot.model.KTREMReadPo;
import com.gdiot.model.ktEmDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface KTNBEMDataMapper {
    int insertKTData(ktEmDataPo electricBanlancePo);

    int insertCmdReadData(KTREMReadPo ktremReadPo);
}
