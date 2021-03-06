package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.NBValue;

/**
 * @author ZhouHR
 */
public interface LoraMapper {

    int countLoraByCondition(Map<String, Object> map);

    List<NBValue> selectLoraList(Map<String, Object> map);
}
