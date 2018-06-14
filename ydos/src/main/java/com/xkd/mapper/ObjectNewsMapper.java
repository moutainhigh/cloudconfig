package com.xkd.mapper;

import com.xkd.service.ObjectNewsService;
import com.xkd.service.ObjectNewsTaskService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ObjectNewsMapper {

    Integer saveObjectNews(@Param("ObjectNewsList") List<Map<String,Object>> ObjectNewsList);

    List<Map<String,Object>> selectObjectNewsByUserId(@Param("userId")String userId,@Param("appFlag") int appFlag,@Param("newsTypeList") List<Integer> newsTypeList,
    @Param("pageSize") int pageSizeInt,@Param("currentPage") int currentPageInt);

    Integer getNoReadNewsCount(@Param("userId")String userId);

    Integer updateAllNewsRead(@Param("userId")String userId);

    Integer updateReadById(@Param("newsId")String newsId);

    Map<String,Object> selectById(@Param("id")String id);


    List<Map<String,Object>> selectCustomerPushNews(@Param("userId")String userId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    Integer selectCustomerPushNewsCount(@Param("userId")String userId);

    List<Map<String,Object>> selectTechnicanPushNews(@Param("userId")String userId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    Integer selectTechnicanPushNewsCount(@Param("userId")String userId);

    List<Map<String,Object>> selectPushNewsByTaskId(@Param("taskId")String taskId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    Integer selectPushNewsByTaskIdCount(@Param("taskId")String taskId);

    Integer selectTotalObjectNewsByUserId(@Param("userId")String userId,@Param("appFlag") int appFlag,@Param("newsTypeList") List<Integer> newsTypeList);

    Integer deleteById(@Param("id")String id);
}
