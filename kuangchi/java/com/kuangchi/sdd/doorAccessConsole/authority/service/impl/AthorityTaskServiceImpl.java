package com.kuangchi.sdd.doorAccessConsole.authority.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.AthorityTaskDao;
import com.kuangchi.sdd.doorAccessConsole.authority.service.AthorityTaskService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;

/**
 * @创建人：     huixian.pan
 * @创建时间： 2016-10-10
 * @创建内容： 授权任务表service实现层
 */

@Service("athorityTaskServiceImpl")
public class AthorityTaskServiceImpl implements AthorityTaskService{
		@Resource(name="athorityTaskDao")	
		AthorityTaskDao athorityTaskDao;
		
		@Resource(name="LogDaoImpl")
		private LogDao logDao;
		
		
		
		
		/* 组织权限查看 by huixian.pan*/
		public Grid searchDoorSysDeptAuth(Map map){
			Grid grid=new Grid();
			grid.setRows(athorityTaskDao.searchDoorSysDeptAuth(map));
			grid.setTotal(athorityTaskDao.countSearchDoorSysDeptAuth(map));
			return grid;
		}
		
		/* 组织权限查看（下载用） by huixian.pan */
		public List<Map>  downloadDoorSysDeptAuth(Map map){
			return  athorityTaskDao.downloadDoorSysDeptAuth(map);
		}
		
		@Override
		public Grid getAuthorityTask(Map map) {
			Grid grid=new Grid();
			grid.setRows(athorityTaskDao.getAuthorityTask(map));
			grid.setTotal(athorityTaskDao.getAuthorityTaskCount(map));
			return grid;
		}
		

		@Override
		public List<Map> getFailureAuthorityTask() {
			return athorityTaskDao.getFailureAuthorityTask();
		}
		
		/*通过权限历史表id查询权限历史*/
		@Override
		public List<Map>  getDeletedAuthById(String id){
			return athorityTaskDao.getDeletedAuthById(id);
		}

		@Override
		public List<Map> getTimeGroup() {
			return athorityTaskDao.getTimeGroup();
		}

	

		@Override
		public boolean addFailureAuthorityTask(List<Map> mapList,String today,String create_user) {
			Map<String,String> log = new HashMap<String,String>();
			for(Map  map:mapList){
				boolean result1=athorityTaskDao.delAuthTaskHis(map);
				boolean result=athorityTaskDao.addFailureAuthorityTask(map);
				if(result && result1){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "下发权限任务");
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "新增下发权限任务成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "下发权限任务");
					log.put("V_OP_FUNCTION", "新增");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "新增下发权限任务失败");
					logDao.addLog(log);
					return false;
				}
				
				
			}
			return true;
		}

		@Override
		public boolean deleteRepeatAuthorityTask(List<Map> mapList,String today,String create_user) {
			Map<String,String> log = new HashMap<String,String>();
			for(Map  map:mapList){
				boolean result=athorityTaskDao.deleteRepeatAuthorityTask(map);
				if(result){
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "下发权限任务");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除下发权限任务成功");
					logDao.addLog(log);
				}else{
					log.put("V_OP_TYPE", "业务");
					log.put("V_OP_NAME", "下发权限任务");
					log.put("V_OP_FUNCTION", "删除");
					log.put("V_OP_ID", create_user);
					log.put("V_OP_MSG", "删除下发权限任务失败");
					logDao.addLog(log);
					return false;
				}
				
				
			}
			return true;
		}


		@Override
		public Grid getAuthorityTaskList(Map map,String page,String rows) {
			int Page = Integer.valueOf(page);
			int Rows = Integer.valueOf(rows);
			map.put("page", (Page-1)*Rows);
			map.put("rows", Rows);
			
			Grid grid=new Grid();
			grid.setRows(athorityTaskDao.getAuthorityTaskList(map));
			grid.setTotal(athorityTaskDao.getAuthorityTaskListCount(map));
			return grid;
		}

		
}
