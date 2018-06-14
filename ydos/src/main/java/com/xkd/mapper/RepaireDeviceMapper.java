package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RepaireDeviceMapper {

    public Integer insert(@Param("repaireDeviceList") List<Map<String,Object>> repaireDeviceList);

    public  List<Map<String,Object>> selectByRepaireId(@Param("repaireId")String repaireId);


    List<Map<String,Object>> selectUserContentByRepaireId(@Param("repaireId")String repaireId);
}
