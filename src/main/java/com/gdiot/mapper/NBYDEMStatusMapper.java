package com.gdiot.mapper;

import com.gdiot.model.YDEMeterStatusPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface NBYDEMStatusMapper {
    /**
     * @param mYDEMeterStatusPo
     * @return
     */
    int insertStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    /**
     * @param mYDEMeterStatusPo
     * @return
     */
    int updateEMStatus(YDEMeterStatusPo mYDEMeterStatusPo);

    /**
     * @param dev_id
     * @param imei
     * @return
     */
    List<YDEMeterStatusPo> selectDevid(@Param("dev_id") String dev_id, @Param("imei") String imei);

    /**
     * @param dev_id
     * @param imei
     * @param status
     * @return
     */
    int countByCondition(@Param("dev_id") String dev_id, @Param("imei") String imei, @Param("status") String status);

    /**
     * @param dev_id
     * @param imei
     * @param status
     * @param limit
     * @param offset
     * @return
     */
    List<YDEMeterStatusPo> selectStatus(@Param("dev_id") String dev_id, @Param("imei") String imei, @Param("status") String status, @Param("limit") int limit, @Param("offset") int offset);
}
