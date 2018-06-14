package com.xkd.mapper;

import com.xkd.model.Label;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/11.
 */
public interface AttachmentMapper  {
    Integer insert(Map<String,Object> paramMap);

    Map<String,Object> selectById(@Param("id")String id);

    Integer updateById(Map<String,Object> paramMap);

    Integer insertList(@Param("objectId")String objectId, @Param("type")int type,
                       @Param("createdBy")String createdBy, @Param("createDate")Date createDate, @Param("pictureList")List<String> pictureList);

    List<Map<String,Object>> selectAttachmentByObjectId(@Param("objectId")String  objectId);

    int deleteById(@Param("id") String id);

    int deleteByObjectId(@Param("objectId")String objectId);

    int insertAttachmentList(@Param("list")List<Map<String,Object>>  list);

}
