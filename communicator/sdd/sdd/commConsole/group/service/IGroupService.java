package com.kuangchi.sdd.commConsole.group.service;

import java.util.List;


import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.commConsole.group.model.TimeGroupBlock;
import com.kuangchi.sdd.commConsole.holiday.model.HolidayBean;
public interface IGroupService {
    
   
    public String setGroup(String Mac,String device_type,List<TimeBlock> group);	
    public String getGroup(String mac,String block,String device_type);
}
