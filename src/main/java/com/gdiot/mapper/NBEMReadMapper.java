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
    List<YDEMNBReadPo> selectList(@Param("dev_id") String deviceNo, @Param("e_num") String eNum, @Param("imei") String imei, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param mYDEMNBReadPo
     * @return
     */
    int insertOne(YDEMNBReadPo mYDEMNBReadPo);

    /**
     * @param imei
     * @param type
     * @param data_seq
     * @return
     */
    List<YDEMNBReadPo> selectListByseq(@Param("imei") String imei, @Param("type") String type, @Param("data_seq") String data_seq);

    /**
     * @param imei
     * @return
     */
    List<YDEMNBReadPo> selectAllList(@Param("imei") String imei);

}
