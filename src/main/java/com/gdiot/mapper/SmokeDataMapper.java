package com.gdiot.mapper;

import com.gdiot.model.SmokeDataPo;
import com.gdiot.model.SmokeDevicePo;
import com.gdiot.model.SmokeDownPo;
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
    /**
     * @param mSmokeDataPo
     * @return
     */
    int insertOne(SmokeDataPo mSmokeDataPo);

    /**
     * @param dev_id
     * @return
     */
    int countbyDevId(@Param("dev_id") String dev_id);

    /**
     * @param dev_id
     * @param limit
     * @param offset
     * @return
     */
    List<SmokeDataPo> selectbyDevId(@Param("dev_id") String dev_id, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param deviceId
     * @param deviceType
     * @return
     */
    List<SmokeDevicePo> selectAlarmList(@Param("deviceId") String deviceId, @Param("deviceType") String deviceType);

    /**
     * @param mSmokeDownPo
     * @return
     */
    int insertAlarmDown(SmokeDownPo mSmokeDownPo);
}
