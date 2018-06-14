package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/10.
 */
public interface StatisticMapper {

    public int selectCustomerCount(@Param("userId") String userId,@Param("departmentIdList")List<String> departmentIdList);
    public int selectThisMonthCustomerCount(@Param("userId") String userId,@Param("departmentIdList")List<String> departmentIdList);

    public int selectTotalScheduleTimes(@Param("userId") String userId,@Param("pcCompanyId")String pcCompanyId);
    public int selectThisMonthScheduleTimes(@Param("userId") String userId,@Param("pcCompanyId")String pcCompanyId);

     public List<Map<String,Object>> selectUserLevelStatistics(@Param("userId") String userId,@Param("departmentIdList")List<String> departmentIdList);


 }
