package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/5/4.
 */
public interface BusinessTripMapper {
    public int insertBusinessTrip(Map<String, Object> map);

    public int updateBusinessTrip(Map<String, Object> map);

    public int deleteBusinessTrip(@Param("idList") List<String> idList);

    public List<Map<String,Object>> searchBusinessTrip(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("responsibleUserId")String responsibleUserId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public int searchBusinessTripCount(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("responsibleUserId")String responsibleUserId);
}
