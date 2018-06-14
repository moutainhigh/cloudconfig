package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/17.
 */
public interface FieldShowMapper {


    List<Map<String,Object>> selectFieldShow(@Param("module")String module);

    List<Map<String,Object>> selectUserFieldShow(@Param("userId") String userId,@Param("module")String module);


    int deleteByUserId(@Param("userId")String userId,@Param("module")String module);

    int insertFieldUserList(@Param("list")List<Map<String,Object>> list);
}
