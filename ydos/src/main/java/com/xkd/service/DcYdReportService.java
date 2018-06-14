package com.xkd.service;

import com.xkd.mapper.DcYdReportMapper;
import com.xkd.model.DcYdReport;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-27;
 */

@Service
public class DcYdReportService {

    @Autowired
    UserService userService;

    @Autowired
    DcYdReportMapper dcYdReportMapper;

    @Autowired
    DcYdReportSheetService dcYdReportSheetService;

    public int insertDcYdReport(Map<String, Object> dcYdReport) {
        return dcYdReportMapper.insert(dcYdReport);
    }

    public int deleteById(String id) {
        return dcYdReportMapper.deleteById(id);
    }

    public int updateDcYdReport(Map<String, Object> dcYdReport) {
        return dcYdReportMapper.update(dcYdReport);
    }

    public Map<String, Object> selectById(String id) {
        Map<String, Object> dcYdReport = dcYdReportMapper.selectById(id);
        formatDate(dcYdReport);
        return dcYdReport;
    }

    public List<Map<String, Object>> filterReport(List<String> userId, Integer status, Integer type, String startDate, String endDate, Integer dateFilter,String pcCompanyId,Integer currentPage, Integer pageSize) {
        Integer start = (currentPage - 1) * pageSize;
        List<Map<String, Object>> reportList = dcYdReportMapper.filterReport(userId, status, type, startDate, endDate, dateFilter, pcCompanyId, start, pageSize);
        for (Map<String, Object> report : reportList) {
            formatDate(report);
            List<Map<String, Object>> reportNoteList;
            reportNoteList = dcYdReportSheetService.listreportNote((String) report.get("id"));
            report.put("reportNoteList", reportNoteList);
        }
        return reportList;
    }

    public Integer filterReportCount(List<String> userIdList, Integer status, Integer type, String pcCompanyId,String startDate, String endDate, Integer dateFilter){
        return dcYdReportMapper.filterReportCount(userIdList, status, type, pcCompanyId,startDate, endDate, dateFilter);
    }

    private void formatDate(Map<String, Object> report) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Timestamp createDate = (Timestamp) report.get("createDate");
        Timestamp updateDate = (Timestamp) report.get("updateDate");
        report.put("createDate", format.format(createDate));
        if (updateDate != null){
            report.put("updateDate", format.format(updateDate));
        }
    }


    public Integer countReportByPcCompanyIdAndDate(String pcCompanyId){
        return dcYdReportMapper.countReportByPcCompanyIdAndDate(pcCompanyId);
    }

    public Integer insertForSchedule(String id, String userId, String pcCompanyId,Integer type){
        return dcYdReportMapper.insertForSchedule(id, userId, pcCompanyId,type);
    }

    public List<Map<String, String>> selectAllLastDayReport(Integer type){
        return dcYdReportMapper.selectAllLastDayReport(type);
    }

    public Integer updateReportStatus(List<String> reportIdList){
        return dcYdReportMapper.updateReportStatus(reportIdList);
    }

}
