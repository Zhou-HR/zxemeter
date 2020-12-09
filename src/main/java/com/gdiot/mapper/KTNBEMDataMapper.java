package com.gdiot.mapper;


import com.gdiot.model.KTREMReadPo;
import com.gdiot.model.ktEmDataPo;

public interface KTNBEMDataMapper {
	int insertKTData(ktEmDataPo electricBanlancePo);

    int insertCmdReadData(KTREMReadPo ktremReadPo);
}
