package com.kuangchi.sdd.doorAccessConsole.authority.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

/**
 * @创建人：     huixian.pan
 * @创建时间： 2016-10-10
 * @创建内容： 授权任务表service层
 */
public interface AthorityTaskService {

	/* 组织权限查看 by huixian.pan*/
	public Grid searchDoorSysDeptAuth(Map map);
	
	/* 组织权限查看（下载用） by huixian.pan */
	public List<Map>  downloadDoorSysDeptAuth(Map map);
	
	/*查询所有下发权限任务*/
	public Grid getAuthorityTask(Map map);
	
	/*查询所有失败下发权限任务*/
	public List<Map>  getFailureAuthorityTask();
	
	/*通过权限历史表id查询权限历史*/
	public List<Map>  getDeletedAuthById(String id);
	
	/*查询所有时段组信息*/
	public List<Map>  getTimeGroup();
	
	/*把失败的下发权限任务插到历史表中 */
	public boolean addFailureAuthorityTask(List<Map> mapList,String today,String create_user);
	
	/*删除重复的失败下发权限任务 */
	public boolean deleteRepeatAuthorityTask(List<Map> mapList,String today,String create_user);

	public Grid getAuthorityTaskList(Map map,String page,String rows);//查询权限任务表
	
	
	
	
}
