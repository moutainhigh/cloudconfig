package com.kuangchi.sdd.attendanceConsole.attendRecord.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendRecord.dao.IAttendRecordDao;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.AttendRecordModel;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.Outwork;
import com.kuangchi.sdd.attendanceConsole.attendRecord.service.IAttendRecordService;

@Service("attendRecordService")
public class AttendRecordServiceImpl implements IAttendRecordService {
	
	@Resource(name="attendRecordDao")	
	private IAttendRecordDao attendRecordDao;

	@Override
	public List<AttendRecordModel> getAttendRecord(Map map) {
		
		List<AttendRecordModel> records =  attendRecordDao.getAttendRecord(map);
		
		//查询每条数据考勤异常情况
		for (AttendRecordModel record : records) {
			Map<String, Object> info = new HashMap<String, Object>();
			info.put("staff_num", record.getStaff_num());
			info.put("everyday_time", record.getEvery_date());
			List<String> exceptionType = attendRecordDao.getExceptionType(info);
			
			if(exceptionType != null && exceptionType.size() !=0)
				record.setIsException(Integer.parseInt(exceptionType.get(0)));
		}
		return records;
	}

	@Override
	public Boolean addOutworkInfo(Outwork work) {
		return attendRecordDao.addOutworkInfo(work);
	}

	@Override
	public Boolean deleteOutworkInfo(String id) {
		return attendRecordDao.deleteOutworkInfo(id);
	}

	@Override
	public Boolean editOutworkInfo(Outwork work) {
		return attendRecordDao.editOutworkInfo(work);
	}

	@Override
	public List<Outwork> selectOutworkInfoById(String id) {
		return attendRecordDao.selectOutworkInfoById(id);
	}

	
}
