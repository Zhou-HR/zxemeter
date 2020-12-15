package com.gdiot.mapper;

import com.gdiot.model.YDEMeterEventPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface NBEMEventMapper {
    /**
     * @param dev_id
     * @param eNum
     * @param imei
     * @param beginTime
     * @param endTime
     * @param source
     * @return
     */
    int countByCondition(@Param("dev_id") String dev_id, @Param("e_num") String eNum, @Param("imei") String imei, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source);

    /**
     * @param deviceNo
     * @param eNum
     * @param imei
     * @param beginTime
     * @param endTime
     * @param source
     * @param limit
     * @param offset
     * @return
     */
    List<YDEMeterEventPo> selectList(@Param("dev_id") String deviceNo, @Param("e_num") String eNum, @Param("imei") String imei, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param mYDEMEventPo
     * @return
     */
    int insertOne(YDEMeterEventPo mYDEMEventPo);

}
