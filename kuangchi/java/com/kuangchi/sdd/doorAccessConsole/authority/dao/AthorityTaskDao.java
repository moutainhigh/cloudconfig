package com.kuangchi.sdd.doorAccessConsole.authority.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @创建人：     huixian.pan
 * @创建时间： 2016-10-10
 * @创建内容： 授权任务表Dao层
 */
public interface AthorityTaskDao {
	
	/* 组织权限查看（分页） by huixian.pan*/
	public List<Map>  searchDoorSysDeptAuth(Map map);
	
	/* 组织权限查看总行数  by huixian.pan*/
	public Integer  countSearchDoorSysDeptAuth(Map map);
	
	/* 组织权限查看（下载用） by huixian.pan */
	public List<Map>  downloadDoorSysDeptAuth(Map map);
	
	/*查询所有下发权限任务*/
	public List<Map>  getAuthorityTask(Map map);
	
	/*查询所有下发权限任务行数*/
	public Integer  getAuthorityTaskCount(Map map);
	
	/*查询所有失败下发权限任务*/
	public List<Map>  getFailureAuthorityTask();
	
	/*通过权限历史表id查询权限历史*/
	public List<Map>  getDeletedAuthById(String id);
	
	/*查询所有时段组信息*/
	public List<Map>  getTimeGroup();
	
	/*把失败的下发权限任务插到历史表中 */
	public boolean addFailureAuthorityTask(Map map);
	
	/*删除失败下发权限任务历史 */
	public boolean delAuthTaskHis(Map map);
	
	/*删除重复的失败下发权限任务 */
	public boolean deleteRepeatAuthorityTask(Map map);

	public List<Map> getAuthorityTaskList(Map map);//查询权限任务表

	public int getAuthorityTaskListCount(Map map);//查询权限任务表总数
}
