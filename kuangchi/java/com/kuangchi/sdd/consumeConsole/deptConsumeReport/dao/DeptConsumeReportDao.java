package com.kuangchi.sdd.consumeConsole.deptConsumeReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.deptConsumeReport.model.DeptConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-19 
 * @功能描述: 部门消费报表-dao
 */
public interface DeptConsumeReportDao {
    
/*获取所有部门消费报表信息（分页）*/
public List<DeptConsumeReportModel> getDeptConsumeReportByParam(Map<String, Object> map);
	
/*获取所有部门消费报表信息的总数量*/
public Integer getDeptConsumeReportCount(Map<String, Object> map);

/*按筛选条件获取部门消费报表信息*/
public List<DeptConsumeReportModel> initDeptConsumeReport(Map<String, Object> map);

/*按筛选条件获取部门消费报表信息总数量*/
public Integer initDeptConsumeReportCount(Map<String, Object> map);

/*获取所有部门消费报表信息(导出用)*/
public List<DeptConsumeReportModel> getDeptConsumeReportNoLimit(Map<String, Object> map);

/*按筛选条件获取部门消费报表信息(导出用)*/
public List<DeptConsumeReportModel> initDeptConsumeReportNoLimit(Map<String, Object> map);

/*获取所有商户类型*/
public List<Vendor>  getVendor();
	
}
