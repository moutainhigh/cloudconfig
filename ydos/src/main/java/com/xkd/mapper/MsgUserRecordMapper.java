package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-3-6;
 */
public interface MsgUserRecordMapper {

    List<Map<String, Object>> selectRecordByUserId(@Param("userId") String userId,
                                                   @Param("start") Integer start,
                                                   @Param("pageSize") Integer pageSize);

    Integer countRecordByUserId(@Param("userId") String userId);

}
