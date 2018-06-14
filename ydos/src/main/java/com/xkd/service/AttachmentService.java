package com.xkd.service;

import com.xkd.mapper.AttachmentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/11.
 */
@Service
public class AttachmentService  {
    @Autowired
    AttachmentMapper attachmentMapper;
    public   List<Map<String,Object>> selectAttachmentByObjectId( String  objectId){
        return attachmentMapper.selectAttachmentByObjectId(objectId);

    }
    public  int deleteById(  String id){
        return attachmentMapper.deleteById(id);
    }
    public int deleteByObjectId( String objectId){
        return attachmentMapper.deleteByObjectId(objectId);
    }
    public int insertAttachmentList( List<Map<String,Object>>  list){
        return attachmentMapper.insertAttachmentList(list);
    }

}
