package com.kuangchi.sdd.businessConsole.process.service;

import java.util.List;
import java.util.Map;

public interface ActivitiProcessService {
    public String insertLeaveRecord(String processInstanceId);
    public String outApplication(String processInstanceId);
    public List<Map<String, String>> getLeaveCategoryList();
    public String insertForgetCheck(String processInstanceId);
    public void recordOverTimeApplication(String processInstanceId);
}
