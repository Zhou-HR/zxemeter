package com.gdiot.mapper;

import com.gdiot.model.XBEMDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface XBEMDataMapper {
    /**
     * @param xBEMDataPo
     * @return
     */
    int insertOne(XBEMDataPo xBEMDataPo);

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
    List<XBEMDataPo> selectList(@Param("dev_id") String dev_id, @Param("e_num") String e_num, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("source") String source, @Param("limit") int limit, @Param("offset") int offset);

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
    List<XBEMDataPo> selectOne(@Param("dev_id") String dev_id, @Param("e_seq") long e_seq);
}
