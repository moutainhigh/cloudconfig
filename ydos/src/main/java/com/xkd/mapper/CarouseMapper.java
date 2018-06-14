package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/9.
 */
public interface CarouseMapper {

    int insert(Map<String,Object> map);
    int deleteByTType(@Param("ttype")Integer ttype,@Param("pcCompanyId")String pcCompanyId);
    int deleteById(@Param("id")String  id);
    List<Map<String,Object>> selectByTType(@Param("ttype")Integer ttype,@Param("pcCompanyIdList")List<String> pcCompanyIdList);
}
