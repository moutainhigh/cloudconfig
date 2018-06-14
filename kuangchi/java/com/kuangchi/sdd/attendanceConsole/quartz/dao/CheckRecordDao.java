package com.kuangchi.sdd.attendanceConsole.quartz.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.quartz.model.CardRecord;

public interface CheckRecordDao {
	//获取CS系统的打卡记录
    public List<CardRecord> getSynchronizeCardRecordList();
    public void deleteRemoteCardRecord(String firstID,String lastID);
}
