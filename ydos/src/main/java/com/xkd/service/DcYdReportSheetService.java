package com.xkd.service;

import com.xkd.mapper.DcYdReportSheetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-28;
 */
@Service
public class DcYdReportSheetService {

    @Autowired
    private DcYdReportSheetMapper dcYdReportSheetMapper;

    public Integer addReportNote(Map<String, Object> reportNote) {
        return dcYdReportSheetMapper.insert(reportNote);
    }

    public Integer updateReportNote(Integer completeFlag, String id) {
        return dcYdReportSheetMapper.update(completeFlag, id);
    }

    public List<Map<String, Object>> listreportNote(String reportId) {
        return dcYdReportSheetMapper.list(reportId);
    }

    public Integer deleteReportNoteById(String id){
        return dcYdReportSheetMapper.deleteByReportId(id);
    }

}
