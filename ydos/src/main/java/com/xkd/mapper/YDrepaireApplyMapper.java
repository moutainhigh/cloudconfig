package com.xkd.mapper;

import com.xkd.model.YDrepaireApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface YDrepaireApplyMapper {

    Integer insert(Map<String,Object> map);

    Map<String,Object> selectById(@Param("id") String id);

    Integer updateById(Map<String,Object> map);
    List<String> selectDeviceIdByApplyId(@Param("applyId")String applyId);

}