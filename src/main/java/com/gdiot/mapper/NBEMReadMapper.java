package com.gdiot.mapper;

import com.gdiot.model.YDEMNBReadPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface NBEMReadMapper {

    int countByCondition(@Param("dev_id") String dev_id, @Param("e_num") String eNum, @Param("imei") String imei, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source);

    List<YDEMNBReadPo> selectList(@Param("dev_id") String deviceNo, @Param("e_num") String eNum, @Param("imei") String imei, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

    int insertOne(YDEMNBReadPo mYDEMNBReadPo);

}