package com.kuangchi.sdd.ZigBeeConsole.task.dao;





import java.util.List;
import java.util.Map;


/**
 * 光子锁任务管理 - dao
 * @author yuman.gao
 */
public interface ITaskDao {
	
	/**
	 * 根据参数查询任务表
	 * @author yuman.gao
	 */
	public List<Map> getZBTaskByParamPage(Map<String, Object> param);
	
	/**
	 * 根据参数查询任务表总数
	 * @author yuman.gao
	 */
	public Integer getZBTaskByParamCount(Map<String, Object> param);
	
	/**
	 * 根据参数查询任务历史表
	 * @author yuman.gao
	 */
	public List<Map> getZBTaskHisByParamPage(Map<String, Object> param);
	
	/**
	 * 根据参数查询任务历史表总数
	 * @author yuman.gao
	 */
	public Integer getZBTaskHisByParamCount(Map<String, Object> param);
	/**
	 * 根据参数查询权限表
	 * @author chudan.guo
	 */
	public List<Map> getAuthorityPage(Map<String, Object> param);
	
	/**
	 * 根据参数查询权限表总条数
	 * @author chudan.guo
	 */
	public Integer getAuthorityPageCount(Map<String, Object> param);
}
