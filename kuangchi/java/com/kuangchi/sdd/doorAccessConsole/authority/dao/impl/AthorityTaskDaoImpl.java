package com.kuangchi.sdd.doorAccessConsole.authority.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.AthorityTaskDao;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;

/**
 * @创建人：     huixian.pan
 * @创建时间： 2016-10-10
 * @创建内容： 授权任务表Dao实现层
 */
@Repository("athorityTaskDao")
public class AthorityTaskDaoImpl extends BaseDaoImpl<Object>  implements AthorityTaskDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	
	/* 组织权限查看（分页） by huixian.pan*/
	public List<Map>  searchDoorSysDeptAuth(Map map){
		  return this.getSqlMapClientTemplate().queryForList("searchDoorSysDeptAuth", map);
	}
	
	/* 组织权限查看总行数  by huixian.pan*/
	public Integer  countSearchDoorSysDeptAuth(Map map){
		  return (Integer) this.getSqlMapClientTemplate().queryForObject("countSearchDoorSysDeptAuth", map);
	}
	
	/* 组织权限查看（下载用） by huixian.pan */
	public List<Map>  downloadDoorSysDeptAuth(Map map){
		 return this.getSqlMapClientTemplate().queryForList("downloadDoorSysDeptAuth", map);
	}
	
	
	
	/*查询所有下发权限任务*/
	@Override
	public List<Map> getAuthorityTask(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityTask", map);
	}

	/*查询所有下发权限任务行数*/
	@Override
	public Integer getAuthorityTaskCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getAuthorityTaskCount", map);
	}
	
	/*查询所有失败下发权限任务*/
	@Override
	public List<Map> getFailureAuthorityTask() {
		return this.getSqlMapClientTemplate().queryForList("getFailureAuthorityTask");
	}
	
	/*通过权限历史表id查询权限历史*/
	public List<Map>  getDeletedAuthById(String id){
		return this.getSqlMapClientTemplate().queryForList("getDeletedAuthById",id);
	}
			
	
	/*查询所有时段组信息*/
	@Override
	public List<Map> getTimeGroup() {
		return this.getSqlMapClientTemplate().queryForList("getTimeGroup2");
	}

	/*把失败的下发权限任务插到历史表中 */
	@Override
	public boolean addFailureAuthorityTask(Map map) {
		return this.insert("addFailureAuthorityTask", map);
	}
	
	

	/*删除失败下发权限任务历史 */
	public boolean delAuthTaskHis(Map map){
		return this.delete("delAuthTaskHis", map);
	}
	
	/*删除重复的失败下发权限任务 */
	@Override
	public boolean deleteRepeatAuthorityTask(Map map) {
		return this.delete("deleteRepeatAuthorityTask", map);
	}

	@Override
	public List<Map> getAuthorityTaskList(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityTaskList", map);
	}

	@Override
	public int getAuthorityTaskListCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getAuthorityTaskListCount", map);
	}

	
	
	
	

}
