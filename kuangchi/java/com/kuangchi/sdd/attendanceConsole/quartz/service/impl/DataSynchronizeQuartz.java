package com.kuangchi.sdd.attendanceConsole.quartz.service.impl;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.quartz.dao.AttendDataRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.dao.CheckRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.model.AttendDataRecord;
import com.kuangchi.sdd.attendanceConsole.quartz.model.CardRecord;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;

@Service("dataSynchronizeQuartzJob")
public class DataSynchronizeQuartz {
    @Resource(name="checkRecordDaoImpl")
	CheckRecordDao checkRecordDao;
    
    @Resource(name="attendDataRecordDao")
    AttendDataRecordDao attendDataRecordDao;

    @Resource(name="cronServiceImpl")
    ICronService cronService;
    Semaphore semaphore=new Semaphore(1);
    
    @Resource(name="employeeDao")
    EmployeeDao employeeDao;
    
    
    
	public void synchronizeCheckRecord(){
		//集群访问时，只有与数据库中相同的IP地址可以执行页面定时器的业务操作
		boolean getResource=false;
		try {
			getResource=	semaphore.tryAcquire(1,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if (getResource) {
			try {
			 	boolean r = cronService.compareIP();	
			 	if(r){
				   List<CardRecord> cardRecordList=checkRecordDao.getSynchronizeCardRecordList();
				   for (int i = 0; i < cardRecordList.size(); i++) {
					   CardRecord cardRecord=cardRecordList.get(i);
					   String staff_no=cardRecord.getStaffNo();
					   String staff_num=employeeDao.getStaffNumbyStaffNo(staff_no);
					   String staff_name=cardRecord.getStaffName();
					   String checktime=cardRecord.getCheckTime();
					   String device_mac=cardRecord.getMacAddress();
					   String door_num=String.valueOf(cardRecord.getDoorNum());
					   String door_name=cardRecord.getDoorName();
					   String device_name=cardRecord.getDeviceName();
					   
					   AttendDataRecord attendDataRecord=new AttendDataRecord();
					   attendDataRecord.setStaff_num(staff_num);
					   attendDataRecord.setStaff_no(staff_no);
					   attendDataRecord.setStaff_name(staff_name);
					   attendDataRecord.setChecktime(checktime);
					   attendDataRecord.setDevice_mac(device_mac);
					   attendDataRecord.setDoor_num(door_num);
					   attendDataRecord.setDoor_name(door_name);
					   attendDataRecord.setDevice_name(device_name);
					   attendDataRecordDao.insertAttendDataRecord(attendDataRecord);
				   }
				   if(cardRecordList.size()>0){
					   String firstID= String.valueOf(cardRecordList.get(0).getId());
					   //String lastID= String.valueOf(cardRecordList.get(8).getId());
					   String lastID=String.valueOf(cardRecordList.get(cardRecordList.size()-1).getId());
					   checkRecordDao.deleteRemoteCardRecord(firstID,lastID);
				   }
			   }
			 	
			} catch (Exception e) {
				 e.printStackTrace();
			}finally{
				semaphore.release();
			}
			 	
		}
   }


}


   

