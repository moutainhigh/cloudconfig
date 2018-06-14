package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;


public class TkAuthQuartz {
	@Resource(name="cronServiceImpl")
	private ICronService cronService;
	
	static Semaphore semaphoreA=new Semaphore(1);
	
	PeopleAuthorityManagerService peopleAuthorityManagerService;
	
	public PeopleAuthorityManagerService getPeopleAuthorityManagerService() {
		return peopleAuthorityManagerService;
	}
	public void setPeopleAuthorityManagerService(
			PeopleAuthorityManagerService peopleAuthorityManagerService) {
		this.peopleAuthorityManagerService = peopleAuthorityManagerService;
	}
    
	TkCommService tkCommService;

	public TkCommService getTkCommService() {
		return tkCommService;
	}
	public void setTkCommService(TkCommService tkCommService) {
		this.tkCommService = tkCommService;
	}
	/**
	 * 执行授权任务表
	 * 每次50条任务
	 * huixian.pan
	 */
	public synchronized void excuteAuthTasks1(){
		boolean getResource=false;
		try {
  		    getResource=semaphoreA.tryAcquire(1,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		try {
		if(getResource){
			boolean r = cronService.compareIP();	
			if(r){
				List<Map> list=peopleAuthorityManagerService.getTkAuthTasks();
				for(Map map:list){
					if((Integer)map.get("action_flag")==0){//添加权限
						addAuth(map);
					}else if((Integer) map.get("action_flag") == 2){//黑名单，设备删除权限，权限表假删除
						updAuth(map);
					}else {// 删除权限
						delAuth(map);
					}
				}
			}
		}
		} catch (Exception e) {
			 e.printStackTrace();
		}finally{
			semaphoreA.release();	 
		}
		
	}
	
    /*添加权限*/
    public void addAuth(Map m){
    	if (getTryTimes(m) < 2) {
    	addTryTimes(m);
    	boolean isSuccess=true;
		Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");
		Map setAuthMap=null;
		if(!"".equals(tkCommService.getTkCommUrl(m.get("device_num").toString()))){
			String seAuthtUrl = tkCommService.getTkCommUrl(m.get("device_num").toString())+ "setEleAuth.do?";
			//参数
			String dataMap = GsonUtil.toJson(m);
			
			//发送请求
			String setAuthStr = HttpRequest.sendPost(seAuthtUrl, "data="+dataMap);
			//处理响应
			setAuthMap = GsonUtil.toBean(setAuthStr, HashMap.class);
		}
		if(setAuthMap!=null){
			Integer result_code=(int) Math.pow((Double) setAuthMap.get("resultCode"), 1);
			if (!(result_code == 0)) {
				isSuccess = false;
			}
		}else{
			isSuccess = false;
		}
		if(isSuccess){
			//记录任务成功历史
			makeTaskHis(m,0,0,getTryTimes(m));
			//删除任务
			delTask(m);
			//插入权限表
			//addTkAuthRecord(m);
			//更新权限表状态
			updateTskState2(m,"01");
		}else{
			//判断次数是否大于3
			if(getTryTimes(m)>=2){//大于2
				//停止下发权限，认定下发权限失败，记录任务失败历史
				makeTaskHis(m,0,1,getTryTimes(m));
				//删除任务
				delTask(m);
				//更新权限表状态
				updateTskState2(m,"02");
			}
		 }
    	}/*else{
    		//停止下发权限，认定下发权限失败，记录任务失败历史
			makeTaskHis(m,0,1,getTryTimes(m));
			//删除任务
			delTask(m);
			//更新权限表状态
			updateTskState2(m,"02");
    	}*/
    }	
    
    
    /* 删除权限*/
	public void delAuth(Map m){
		if (getTryTimes(m) < 2) {
			addTryTimes(m);
			boolean isSuccess=true;
			Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");
			Map delAuthMap=null;
			if(!"".equals(tkCommService.getTkCommUrl(m.get("device_num").toString()))){
				String delAuthUrl = tkCommService.getTkCommUrl(m.get("device_num").toString())+ "delEleAuth.do?";
				//准备参数
				String dataMap = GsonUtil.toJson(m);
				
				//发送请求
				String delAuthStr = HttpRequest.sendPost(delAuthUrl, "data="+dataMap);
				//处理响应
				delAuthMap = GsonUtil.toBean(delAuthStr, HashMap.class);
			}
			if(delAuthMap!=null){
				Integer result_code=(int) Math.pow((Double) delAuthMap.get("resultCode"), 1);
				if (!(result_code == 0)) {
					isSuccess = false;
				}
			}else{
				isSuccess = false;
			}
			if(isSuccess){
				//记录任务成功历史
				makeTaskHis(m,1,0,getTryTimes(m));
				//删除任务
				delTask(m);
				//删除权限表
				delTkAuthRecord(m);
			}else{
				//判断次数是否大于2
				if(getTryTimes(m)>=2){//大于2
					//停止删除权限，认定删除权限失败，记录删除任务失败历史
					makeTaskHis(m,1,1,getTryTimes(m));
					//删除任务
					delTask(m);
					//更新状态
					updateTskState(m,"12");
				}
			}
		}/*else{
			//停止删除权限，认定删除权限失败，记录删除任务失败历史
			makeTaskHis(m,1,1,getTryTimes(m));
			//删除任务
			delTask(m);
			//更新状态
			updateTskState(m,"12");
		}*/
	}
	
    
    
    /* 删除权限（伪删除）*/
	public void updAuth(Map m){
		if (getTryTimes(m) < 2) {
		addTryTimes(m);
		boolean isSuccess=true;
		Map delAuthMap=null;
		if(!"".equals(tkCommService.getTkCommUrl(m.get("device_num").toString()))){
			String delAuthUrl = tkCommService.getTkCommUrl(m.get("device_num").toString())+ "delEleAuth.do?";
			//准备参数
			String dataMap = GsonUtil.toJson(m);
		
			//发送请求
			String delAuthStr = HttpRequest.sendPost(delAuthUrl, "data="+dataMap);
			//处理响应
			delAuthMap = GsonUtil.toBean(delAuthStr, HashMap.class);
		}
		if(delAuthMap!=null){
			Integer result_code=(int) Math.pow((Double) delAuthMap.get("resultCode"), 1);
			if (!(result_code == 0)) {
				isSuccess = false;
			}
		}else{
			isSuccess = false;
		}
		if(isSuccess){
			//记录任务成功历史
			makeTaskHis(m,1,0,getTryTimes(m));
			//删除任务
			delTask(m);
			//删除权限表(伪删除)
			updateTkAuthRecord(m);
		}else{
			//判断次数是否大于2
			if(getTryTimes(m)>=2){//大于2
				//停止删除权限，认定删除权限失败，记录删除任务失败历史
				makeTaskHis(m,1,1,getTryTimes(m));
				//删除任务
				delTask(m);
				//更新状态
				updateTskState(m,"12");
			}
		}
	  }/*else{
			//停止删除权限，认定删除权限失败，记录删除任务失败历史
			makeTaskHis(m,1,1,getTryTimes(m));
			//删除任务
			delTask(m);
			//更新状态
			updateTskState(m,"12");
	  }*/
	}
	
	
    
    //权限任务表尝试次数+1
    public void  addTryTimes(Map map){
    	 peopleAuthorityManagerService.tryTimesPlus(map);
    }
    
    //获取尝试次数
    public  Integer   getTryTimes(Map map){
    	return peopleAuthorityManagerService.getTryTime(map);
    }
    
    /*获取单个授权任务 */
	public Map  getTask(Map map){
		return peopleAuthorityManagerService.getSingleTask(map);
	}
    
    //删除任务
    public void delTask(Map map){
    	 peopleAuthorityManagerService.delTkAuthTask(map);
    }
    
    //新增授权任务历史记录
    public void makeTaskHis(Map map,Integer actionFlag,Integer resultFlag,Integer try_times){
         map.put("action_flag", actionFlag);
 		 map.put("result_flag", resultFlag); 
 		 map.put("try_times", try_times); 
    	 peopleAuthorityManagerService.addTkAuthTaskHis(map);
    }
    
    //添加权限任务
    public  void  addTkAuthRecord(Map map){
   	    peopleAuthorityManagerService.addTkAuthRecord(map);
    }
    
    //删除权限任务
    public  void  delTkAuthRecord(Map map){
    	peopleAuthorityManagerService.delTkAuthRecord(map);
    }
    //删除权限任务（伪删除）
    public  void  updateTkAuthRecord(Map map){
    	peopleAuthorityManagerService.updateTkAuthRecord(map);
    }
    
    //供删除权限用的 更新权限记录 task_state
    public void updateTskState(Map map,String state){
    	map.put("state", state);
    	peopleAuthorityManagerService.updTkAuthRecordA(map);
    }
    
    //供新增权限用的 更新权限记录 task_state
    public void updateTskState2(Map map,String state){
    	map.put("state", state);
    	peopleAuthorityManagerService.updTkAuthRecord2(map);
    }
	
}
