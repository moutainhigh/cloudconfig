package com.kuangchi.sdd.baseConsole.times.times.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.times.times.dao.TimesDao;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午4:57:16
 * @功能描述: 时段模块-Dao实现类
 */
@Repository("timesDaoImpl")
public class TimesDaoImpl extends BaseDaoImpl<Times> implements TimesDao {

	@Override
	public List<Times> getTimesByParamPage(Times times, int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("begin_time", times.getBegin_time());
		param.put("end_time", times.getEnd_time());
		param.put("eare_num", times.getEare_num());
		param.put("times_num", times.getTimes_num());
		if(page!=0 && size!=0){
			param.put("page", (page - 1) * size);
			param.put("size", size);
		}
		return this.getSqlMapClientTemplate().queryForList("getTimesByParamPage", param);
	}

	@Override
	public int getTimesByParamCount(Times times) {
		return (Integer) find("getTimesByParamCount", times);
	}
	
	@Override
	public List<String> getTimesGroupByTimesNum(String times_num) {
		return this.getSqlMapClientTemplate().queryForList("getTimesGroupByTimesNum",times_num);
	}
	
	@Override
	public boolean addTimes(Times times) {
		int maxNum = 0;
		if(find("getMaxNum", times)!=null){
			maxNum = (Integer) find("getMaxNum", times);
		} 
		if(maxNum == 255){
			return false;
		}else if(maxNum > 127 && maxNum < 255){
			times.setEare_num(1);
		}else {
			times.setEare_num(0);
		}
		times.setTimes_num(String.valueOf(maxNum+1));
		return insert("addTimes", times);
	}

	@Override
	public boolean modifyTimes(Times times) {
		return update("modifyTimes", times);
	}

	@Override
	public boolean deleteTimesById(String times_ids) {
		return delete("deleteTimesById", times_ids);
	}
	
	@Override
	public List<DeviceInfo> getAllDevice() {
		return this.getSqlMapClientTemplate().queryForList("getAllDevice");
	}
	
	@Override
	public String getNameSpace() {
		return "common.Times";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Times> getTimesByParamInterface(Times times) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("eare_num", times.getEare_num());
		
		return this.getSqlMapClientTemplate().queryForList("getTimesByParamInterface", param);
	}

	@Override
	public List<DeviceInfo> getDeviceByNums(String deviceNums) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceByNums", deviceNums);
	}

}
