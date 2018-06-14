package com.kuangchi.sdd.commConsole.holiday.service;

import java.util.List;

import com.kuangchi.sdd.commConsole.holiday.model.holidayData;
public interface IHolidayService {
    
    public String getHoliday(String mac,String deviceType);
    public String setHoliday(String Mac,String device_type,List<holidayData> holiday);	
    
}
