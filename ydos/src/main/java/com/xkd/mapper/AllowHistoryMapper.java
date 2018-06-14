package com.xkd.mapper;

import com.xkd.service.ObjectNewsService;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by dell on 2018/3/3.
 */
public interface AllowHistoryMapper {

    public int insert(Map<String,Object> map );
    public int update(Map<String,Object> map);

    public Map<String,Object> selectById(@Param("id")String id);

}
