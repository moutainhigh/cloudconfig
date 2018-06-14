package com.kuangchi.sdd.attendanceConsole.statistic.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.statistic.dao.TestStatisticDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
/**
 * 测试每日考勤
 * @author gengji.yang
 *
 */
@Repository("testStatisticDao")
public class TestStatisticDaoImpl extends BaseDaoImpl<Object> implements TestStatisticDao{

	@Override
	public void insertAttendRecord(String staffNum, String staffName,
			String checkTime) {
		Map map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("staffName", staffName);
		map.put("checkTime", checkTime);
		getSqlMapClientTemplate().insert("insertAttendRecord", map);
	}

	@Override
	public String getNameSpace() {
		return "common.TestStatistic";
	}

	@Override
	public String getTableName() {
		return null;
	}

}
