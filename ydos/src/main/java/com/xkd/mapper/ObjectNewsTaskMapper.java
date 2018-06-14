package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/7.
 */
public interface ObjectNewsTaskMapper {
    public int insert(Map map);
    public int update(Map map);
    public Map select(@Param("id")String id);

    public List<Map<String,Object>> selectList(@Param("pcCompanyId")String pcCompanyId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public Integer selectListCount(@Param("pcCompanyId")String pcCompanyId  );

    public List<String> selectUndoneTaskId( );
}
