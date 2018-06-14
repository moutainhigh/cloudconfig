package com.xkd.mapper;

import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-28;
 */
public interface DcYdReportCommentMapper {

    Integer insert (Map<String, Object> comment);

    Integer delete (String commentId);

    Integer update (Map<String, Object> comment);

    List<Map<String, Object>> list(String reportId);

}
