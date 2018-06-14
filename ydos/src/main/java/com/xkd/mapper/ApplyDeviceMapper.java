package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApplyDeviceMapper {

    public  Integer insert(@Param("applyDeviceList")List<Map<String,Object>> applyDeviceList);

    public  List<Map<String,Object>> selectByApplyId(@Param("applyId")String applyId);

}
