package com.gdiot.mapper;

import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMCmdSendLogPo;
import com.gdiot.model.WMDataPo;
import com.gdiot.model.WMReadDataPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhouHR
 */
@Mapper
@Component
public interface WMDataMapper {
    /**
     * @param wMDataPo
     * @return
     */
    int insertOne(WMDataPo wMDataPo);

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
    List<WMDataPo> selectbyDevId(@Param("dev_id") String dev_id, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param wm_num
     * @return
     */
    List<WMDataPo> selectbyNum(@Param("wm_num") String wm_num);

    /**
     * @param mWMCmdDataPo
     * @return
     */
    int insertCmdSendData(WMCmdDataPo mWMCmdDataPo);

    /**
     * @param mWMCmdDataPo
     * @return
     */
    int updateCmdSendStatus(WMCmdDataPo mWMCmdDataPo);

    /**
     * @return
     */
    List<WMCmdDataPo> selectCmdSendData();

    /**
     * @param mWMCmdSendLogPo
     * @return
     */
    int insertCmdSendLog(WMCmdSendLogPo mWMCmdSendLogPo);

    /**
     * @param mWMReadDataPo
     * @return
     */
    int insertReadData(WMReadDataPo mWMReadDataPo);

    /**
     * @param wm_num
     * @param type
     * @return
     */
    List<WMReadDataPo> selectReadData(@Param("wm_num") String wm_num, @Param("type") String type);
}
