package com.kuangchi.sdd.baseConsole.log.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.log.model.Log;


public interface LogService {
	
	void addLog(Map<String, String> parameter);
	
	//按条件查询日志
	public String getLogList(Log log, int index, int pageSize);
	
	
	public void backupLog(String absolutePath);
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
 	
 	public List<Log> ExportLog(Log log);//导出日志
 	
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
 	public boolean delLogInterval(Map map, String login_user);
}
