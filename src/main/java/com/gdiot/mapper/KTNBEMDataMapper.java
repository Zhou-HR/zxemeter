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
    /**
     * @param electricBanlancePo
     * @return
     */
    int insertKTData(ktEmDataPo electricBanlancePo);

    /**
     * @param ktremReadPo
     * @return
     */
    int insertCmdReadData(KTREMReadPo ktremReadPo);
}
