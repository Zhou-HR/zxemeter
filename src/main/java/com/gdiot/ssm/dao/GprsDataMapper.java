package com.gdiot.ssm.dao;

import com.gdiot.ssm.entity.GprsDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface GprsDataMapper {
    int insertOne(GprsDataPo LoraData);
}
