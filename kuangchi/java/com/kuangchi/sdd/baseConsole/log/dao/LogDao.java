package com.kuangchi.sdd.baseConsole.log.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.log.model.Log;

public interface LogDao {
	
 	//按条件查询日志
 	public List<Log> getLogListByParams(Map map);
 	
 	public List<Log> exportLogList(Log log);//导出日志
 	
 	//获取日志信息的数量
 	public int getListRecords(Map map);
 	
 	public List<Map<String, Object>> getAllLogList();
 	
 	public List<String> getLogTableColumns();
 	
 	public void deleteLogById(Map<String, Object> map);
 	/**
 	 * 获取日志业务类型
 	 * chudan.guo
 	 */
 	public List<Log> getOp_type();
 	/**
 	 * 获取日志全部功能
 	 * chudan.guo
 	 */
 	public List<Log> getOp_function();
 	
 	/**
 	 * 按时间区间备份日志
 	 * @author minting.he
 	 * @param map
 	 * @return
 	 */
 	public List<Log> getLogInterval(Map map);
 	
 	/**
 	 * 删除时间区间备份的日志
 	 * @author minting.he
 	 * @param map
 	 * @return
 	 */
 	public boolean delLogInterval(Map map);

	public void addLog(Map<String, String> log);
}
