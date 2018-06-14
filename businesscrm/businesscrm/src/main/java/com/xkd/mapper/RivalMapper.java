package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/28.
 */
public interface RivalMapper {

    public int insertRival(Map<String,Object> map);

    public int updateRival(Map<String,Object> map);

    public Map<String,Object> selectRivalByName(@Param("rivalName")String rivalName,@Param("pcCompanyId")String pcCompanyId);

    public int deleteByIds(@Param("idList")List<String> idList);

    public List<Map<String,Object>> searchRival(@Param("departmentIdList")List<String> departmentIdList,@Param("searchValue")String searchValue,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public int searchRivalCount(@Param("departmentIdList")List<String> departmentIdList,@Param("searchValue")String searchValue);

    public Map<String,Object> selectRivalById(@Param("id")String id);

}
