package com.xkd.service;

import com.xkd.mapper.DcYdReportCommentMapper;
import org.mapstruct.MappingInheritanceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-28;
 */

@Service
public class DcYdReportCommentService {

    @Autowired
    private DcYdReportCommentMapper dcYdReportCommentMapper;

    @Autowired
    private UserService userService;

    public Integer addComment(Map<String, Object> comment){
        return dcYdReportCommentMapper.insert(comment);
    }

    public Integer deleteComment(String commentId){
        return dcYdReportCommentMapper.delete(commentId);
    }

    public Integer updateComment(Map<String, Object> comment){
        return dcYdReportCommentMapper.update(comment);
    }

    public List<Map<String, Object>> listComment(String reportId){
        List<Map<String, Object>> commentList = dcYdReportCommentMapper.list(reportId);
        for (Map<String, Object> comment:commentList){
            addUserNameToComment(comment);
        }
        return commentList;
    }

    private void addUserNameToComment(Map<String, Object> comment) {
        String userId = (String) comment.get("createdBy");
        comment.put("userName", userService.selectUserById(userId).get("uname"));
        Timestamp createDate  = (Timestamp) comment.get("createDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        comment.put("createDate", format.format(createDate));
    }

}
