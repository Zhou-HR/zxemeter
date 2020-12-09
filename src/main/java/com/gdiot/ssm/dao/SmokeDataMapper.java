package com.gdiot.ssm.dao;

import com.gdiot.ssm.entity.SmokeDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface SmokeDataMapper {

    int insertOne(SmokeDataPo mSmokeDataPo);

    int countbyDevId(@Param("dev_id") String dev_id);

    List<SmokeDataPo> selectbyDevId(@Param("dev_id") String dev_id, @Param("limit") int limit, @Param("offset") int offset);
}
