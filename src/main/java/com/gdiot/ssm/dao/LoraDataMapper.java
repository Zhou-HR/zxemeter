package com.gdiot.ssm.dao;

import com.gdiot.ssm.entity.LoraDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface LoraDataMapper {

    int countByCondition(@Param("dev_id") String deviceNo, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source);

    List<LoraDataPo> selectList(@Param("dev_id") String deviceNo, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

    int insertOne(LoraDataPo LoraData);


}
