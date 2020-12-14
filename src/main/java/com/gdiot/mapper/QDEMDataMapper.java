package com.gdiot.mapper;

import com.gdiot.model.QDEMDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface QDEMDataMapper {

    int insert(QDEMDataPo record);

}