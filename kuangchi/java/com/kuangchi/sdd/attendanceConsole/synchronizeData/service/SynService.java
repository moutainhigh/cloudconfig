package com.kuangchi.sdd.attendanceConsole.synchronizeData.service;

public interface SynService {
	   //同步部门
       public void synSqlServerOrgDepartmentData();       
       //同步员工账号
       public void synSqlServerOrgUserAccountData();
       //同步请假数据
       public void synSqlServerCheckData(String startDate,String endDate);
       //同步外出数据
       public void synSqlServerOutData(String startDate,String endDate);
       //同步请假数据
       public void synSqlServerBrushCardLog(String startDate,String endDate);
       //整理请假数据
       public void shuffleSqlServerCheckData(String startDate,String endDate,String employeeId);
       public void shuffleSqlServerOutData(String startDate,String endDate,String employeeId);
}
