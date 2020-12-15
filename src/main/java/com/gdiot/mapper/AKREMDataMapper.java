package com.gdiot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gdiot.model.AKREMDataPo;
import com.gdiot.model.AKREMEventPo;
import com.gdiot.model.AKREMReadPo;
import org.springframework.stereotype.Component;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface AKREMDataMapper {
    /**
     * @param xAKREMDataPo
     * @return
     */
    int insertOne(AKREMDataPo xAKREMDataPo);

    /**
     * @param dev_id
     * @param e_num
     * @param beginTime
     * @param endTime
     * @param source
     * @param limit
     * @param offset
     * @return
     */
    List<AKREMDataPo> selectList(@Param("dev_id") String dev_id, @Param("e_num") String e_num, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param dev_id
     * @param e_num
     * @param beginTime
     * @param endTime
     * @param source
     * @return
     */
    int countByCondition(@Param("dev_id") String dev_id, @Param("e_num") String e_num, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source);

    /**
     * @param dev_id
     * @param e_seq
     * @return
     */
    List<AKREMDataPo> selectOne(@Param("dev_id") String dev_id, @Param("e_seq") long e_seq);

    /**
     * @param mAKREMReadPo
     * @return
     */
    int insertReadData(AKREMReadPo mAKREMReadPo);

    /**
     * @param imei
     * @param e_num
     * @param type
     * @return
     */
    List<AKREMReadPo> selectRead(@Param("imei") String imei, @Param("e_num") String e_num, @Param("type") String type);

    /**
     * @param mAKREMEventPo
     * @return
     */
    int insertEventData(AKREMEventPo mAKREMEventPo);

    /**
     * @param imei
     * @param e_num
     * @param type
     * @return
     */
    List<AKREMEventPo> selectEvent(@Param("imei") String imei, @Param("e_num") String e_num, @Param("type") String type);
}
