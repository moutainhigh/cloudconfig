package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.FloorGroupModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.TkDeviceModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-9-26 
 * @功能描述: 人员授权-service实现层
 */

@Transactional
@Service("peopleAuthorityManagerServiceImpl")
public class PeopleAuthorityManagerServiceImpl implements  PeopleAuthorityManagerService{

	@Resource(name = "peopleAuthorityManagerDaoImpl")
	private PeopleAuthorityManagerDao peopleAuthorityManagerDao;
	
   @Resource(name="LogDaoImpl")
	private LogDao logDao;
	

	@Override
	public  String getCardType(String card_num) {
		return peopleAuthorityManagerDao.getCardType(card_num);
	}
   
	@Override
	public List<TkDeviceModel> getTkDeviceInfo(Map<String, Object>  map) {
		return peopleAuthorityManagerDao.getTkDeviceInfo(map);
	}

	@Override
	public Integer getTkDeviceInfoCount(Map<String, Object> map) {
		return peopleAuthorityManagerDao.getTkDeviceInfoCount(map);
	}

	@Override
	public Map getTkDeviceIPByDeviceNum(String device_num) {
		return peopleAuthorityManagerDao.getTkDeviceIPByDeviceNum(device_num);
	}
	
	@Override
	public List<TkDeviceModel> getTkDeviceInfoByDeviceNum(
			Map<String, Object> map) {
		return peopleAuthorityManagerDao.getTkDeviceInfoByDeviceNum(map);
	}

	@Override
	public Integer getTkDeviceInfoCountByDeviceNum(Map<String, Object> map) {
		return peopleAuthorityManagerDao.getTkDeviceInfoCountByDeviceNum(map);
	}

	@Override
	public List<FloorGroupModel> getfloorGroupSelections() {
		return peopleAuthorityManagerDao.getfloorGroupSelections();
	}
	
	@Override
	public List<String> getFloorNum(String floor_group_num) {
		return peopleAuthorityManagerDao.getFloorNum(floor_group_num);
	}
	
	@Override
	public Grid getBoundCardInfo(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getCardBoundInfo(map));
		grid.setTotal(peopleAuthorityManagerDao.countCardBoundInfo(map));
		return grid;
	}
	
	@Override
	public Grid getAuthsByStaffNum(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getAuthsByStaffNum(map));
		grid.setTotal(peopleAuthorityManagerDao.countAuthsByStaffNum(map));
		return grid;
	}
	
	@Override
	public Grid getAuthsByStaffNum2(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getAuthsByStaffNum2(map));
		grid.setTotal(peopleAuthorityManagerDao.countAuthsByStaffNum2(map));
		return grid;
	}
	
	@Override
	public List<Map> getAuthsByStaffNumNoLimit(Map map){
		return peopleAuthorityManagerDao.getAuthsByStaffNumNoLimit(map);
	}
	
	
	@Override
	public boolean addPeopleAuthority(List<PeopleAuthorityManager> pamList,
			String today, String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		for(PeopleAuthorityManager  pam:pamList){
			/*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
			
			Map map=new HashMap();
			map.put("card_num", pam.getCard_num());
			map.put("device_num",pam.getDevice_num());
			boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
			
			/*----------------------------------------------------*/
			
			boolean result=peopleAuthorityManagerDao.addPeopleAuthority(pam);
			if(result && result1){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增人员权限成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "新增");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "新增人员权限失败");
				logDao.addLog(log);
				return false;
			}
			
			
		}
		return true;
		
	}

	@Override
	public boolean updatePeopleAuthority(List<PeopleAuthorityManager> pamList,
			String today, String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		for(PeopleAuthorityManager  pam:pamList){
			boolean result=peopleAuthorityManagerDao.updatePeopleAuthority(pam);
			if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除人员权限成功");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除人员权限失败");
				logDao.addLog(log);
				return false;
			}
		}
		return true;
	}

	
	@Override
	public boolean deletePeopleAuthority(List<PeopleAuthorityManager> pamList,
			String today, String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		for(PeopleAuthorityManager  pam:pamList){
			boolean result=peopleAuthorityManagerDao.deletePeopleAuthority(pam);
			if(result){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除人员权限");
				logDao.addLog(log);
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_NAME", "人员授权");
				log.put("V_OP_FUNCTION", "删除");
				log.put("V_OP_ID", create_user);
				log.put("V_OP_MSG", "删除人员权限");
				logDao.addLog(log);
				return false;
			}
			
			
		}
		return true;
		
	}

	/*查询所有下发权限任务*/
	@Override
	public Grid getTkAuthorityTask(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getTkAuthorityTask(map));
		grid.setTotal(peopleAuthorityManagerDao.getTkAuthorityTaskCount(map));
		return grid;
	}

	/*查询所有失败的下发权限任务*/
	@Override
	public List<PeopleAuthorityManager> getTkFailureAuthorityTask() {
		return peopleAuthorityManagerDao.getTkFailureAuthorityTask();
	}
	
	/*查询所有人员下发权限任务*/
	@Override
	public Grid getStaffAuthorityTask(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getStaffAuthorityTask(map));
		grid.setTotal(peopleAuthorityManagerDao.getStaffAuthorityTaskCount(map));
		return grid;
	}
	
	/*查询所有下发权限任务历史*/
	@Override
	public Grid getAllAuthTaskHis(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getAllAuthTaskHis(map));
		grid.setTotal(peopleAuthorityManagerDao.getAllAuthTaskHisCount(map));
		return grid;
	}
	
	/*查询所有人员失败的下发权限任务*/
	@Override
	public List<PeopleAuthorityManager> getStaffFailureAuthorityTask() {
		return peopleAuthorityManagerDao.getStaffFailureAuthorityTask();
	}
	
	 /*下发失败的权限任务（包括未删除和未下载任务）*/
	 public boolean downloadStaffFailureTask(List<PeopleAuthorityManager> pamList,String today,String create_user){
		 Map<String,String> log = new HashMap<String,String>();
			for(PeopleAuthorityManager  pam:pamList){
				Map map=new  HashMap();
				map.put("id", pam.getId());
				boolean result2=peopleAuthorityManagerDao.delFailureAuthorityTaskHis(map);
				if("0".equals(pam.getAction_flag())){  //下发未下载任务
					boolean result=peopleAuthorityManagerDao.addPeopleAuthority(pam);
					if(result && result2){
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_NAME", "人员授权");
						log.put("V_OP_FUNCTION", "新增");
						log.put("V_OP_ID", create_user);
						log.put("V_OP_MSG", "新增人员权限成功");
						logDao.addLog(log);
					}else{
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_NAME", "人员授权");
						log.put("V_OP_FUNCTION", "新增");
						log.put("V_OP_ID", create_user);
						log.put("V_OP_MSG", "新增人员权限失败");
						logDao.addLog(log);
						return false;
					}
				}else if("1".equals(pam.getAction_flag())){
					boolean result=peopleAuthorityManagerDao.updatePeopleAuthority(pam);
					if(result && result2){
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_NAME", "人员授权");
						log.put("V_OP_FUNCTION", "删除");
						log.put("V_OP_ID", create_user);
						log.put("V_OP_MSG", "删除人员权限成功");
						logDao.addLog(log);
					}else{
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_NAME", "人员授权");
						log.put("V_OP_FUNCTION", "删除");
						log.put("V_OP_ID", create_user);
						log.put("V_OP_MSG", "删除人员权限失败");
						logDao.addLog(log);
						return false;
					}
				}
				
			}
			return true;
	 }
	 
	
	
	/*查询所有部门下发权限任务*/
	@Override
	public Grid getDeptAuthorityTask(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getDeptAuthorityTask(map));
		grid.setTotal(peopleAuthorityManagerDao.getDeptAuthorityTaskCount(map));
		return grid;
	}
	
	/*查询所有失败的部门下发权限任务*/
	@Override
	public List<PeopleAuthorityManager> getDeptFailureAuthorityTask() {
		return peopleAuthorityManagerDao.getDeptFailureAuthorityTask();
	}
	
	/*通过任务历史id查询任务历史详情 */
	@Override
	public List<PeopleAuthorityManager> getAuthorityTaskById(String id){
		return peopleAuthorityManagerDao.getAuthorityTaskById(id);
	}
    
	/*查询所有人员权限*/
	public List<Map> getStaffAuthority(){
		return peopleAuthorityManagerDao.getStaffAuthority();
	};
	
	/*查询所有部门权限*/
	public List<Map> getDeptAuthority(){
		return peopleAuthorityManagerDao.getDeptAuthority();
	};
	
	/*通过卡号获取楼层信息*/
	public List<Map> getFloorListByCardNum(String card_num,String device_num){
		Map map=new HashMap();
		map.put("card_num", card_num);
		map.put("device_num", device_num);
		return  peopleAuthorityManagerDao.getFloorListByCardNum(map);
	}
	
	/*通过部门编号获取楼层信息 */
	public List<Map> getFloorListByObjectNum(String object_num,String device_num){
		Map map=new HashMap();
		map.put("object_num", object_num);
		map.put("device_num", device_num);
		return  peopleAuthorityManagerDao.getFloorListByObjectNum(map);
	}
	

	
/*------------------------------定时下发权限调用方法起-------------------------------------------*/
	/*获取所有梯控授权任务*/
	@Override
	public List<Map> getTkAuthTasks() {
		return peopleAuthorityManagerDao.getTkAuthTasks();
	}

	/*权限任务表尝试次数+1 */
	@Override
	public boolean tryTimesPlus(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.tryTimesPlus(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "更新权限任务表尝试次数");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "更新权限任务表尝试次数");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新失败");
			logDao.addLog(log);
			return false;
		}
		
	return true;
	}

	/*获取尝试次数 */
	@Override
	public Integer getTryTime(Map map) {
		return peopleAuthorityManagerDao.getTryTime(map);
	}

	/*获取单个授权任务 */
	public Map getSingleTask(Map map){
		return peopleAuthorityManagerDao.getSingleTask(map);
	}
	
	/*删除授权任务 */
	@Override
	public boolean delTkAuthTask(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.delTkAuthTask(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "授权任务");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "授权任务");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除失败");
			logDao.addLog(log);
			return false;
		}
		
	    return true;
	}

	/*新增授权任务历史记录 */
	@Override
	public boolean addTkAuthTaskHis(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.addTkAuthTaskHis(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "授权任务历史记录");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_MSG", "新增成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "授权任务历史记录");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_MSG", "新增失败");
			logDao.addLog(log);
			return false;
		}
		
	    return true;
	}

	/*添加权限记录 */
	@Override
	public boolean addTkAuthRecord(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=false;
/*		if("".equals(map.get("card_num").toString())){
*/ 		if(null == map.get("card_num") || "".equals(map.get("card_num").toString())){
			result=peopleAuthorityManagerDao.delNoCardRepeTkAuthRecord(map);
		}else{
			result=peopleAuthorityManagerDao.delRepeatTkAuthRecord(map);
		}
		boolean result2=peopleAuthorityManagerDao.addTkAuthRecord(map);
		if(result && result2){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_MSG", "新增成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_MSG", "新增失败");
			logDao.addLog(log);
			return false;
		}
		
	    return true;
	}
	
	/*修改权限记录任务标记（删除时用）*/
	public boolean  updTkAuthRecord(Map map){
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.updTkAuthRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新失败");
			logDao.addLog(log);
			return false;
		}
		
	    return true;
	}
	
	/*修改权限记录任务标记（删除时用）*/
	public boolean  updTkAuthRecordA(Map map){
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.updTkAuthRecordA(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}
	
	/*修改权限记录任务标记（新增时用） */
	public boolean  updTkAuthRecord2(Map map){
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.updTkAuthRecord2(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}
	
	
	
	/*删除权限记录*/
	@Override
	public boolean delTkAuthRecord(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.delTkAuthRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}
	/*删除权限记录 （伪删除）*/
	@Override
	public boolean updateTkAuthRecord(Map map) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.updateTkAuthRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "更新");
			log.put("V_OP_MSG", "更新失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}

	
	/* 删除重复权限表记录 */
	public boolean  delRepeatTkAuthRecord(Map map){
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.delRepeatTkAuthRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}
	
	
	/* 删除重复权限表记录(组织下没有卡时用) */
	public boolean  delNoCardRepeTkAuthRecord(Map map){
		Map<String,String> log = new HashMap<String,String>();
		boolean result=peopleAuthorityManagerDao.delNoCardRepeTkAuthRecord(map);
		if(result){
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除成功");
			logDao.addLog(log);
		}else{
			log.put("V_OP_TYPE", "业务");
			log.put("V_OP_NAME", "权限记录 ");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除失败");
			logDao.addLog(log);
			return false;
		}
		
		return true;
	}
	
	
	

	@Override
	public Grid getAuthsByStaffNumNew(Map map) {
		Grid grid=new Grid();
		grid.setRows(peopleAuthorityManagerDao.getAuthsByStaffNumNew(map));
		grid.setTotal(peopleAuthorityManagerDao.countAuthsByStaffNumNew(map));
		return grid;
	}

	@Override
	public void delByID(Map map) {
		peopleAuthorityManagerDao.delByID(map);
	}


/*------------------------------定时下发权限调用方法止-------------------------------------------*/
	@Override
	public void sendIntoTask(Integer flag, List<PeopleAuthorityManager> list,String today,String createUser) {
		if(flag==0){
			updateAuthState2(list,"00");
			addPeopleAuthority(list, today, createUser);
		}else if(flag==1){
			updateAuthState(list,"10");
			updatePeopleAuthority(list, today, createUser);
		}
	}

	/**
	 * 新增人员权限前
	 * by huixian.pan
	 */
	private void updateAuthState2(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type","1");
			map.put("start_time", m.getStart_time());
			map.put("end_time", m.getEnd_time());
			map.put("state", state);
			addTkAuthRecord(map);
		}
	}
	
	/**
	 * 删除人员权限前
	 * by huixian.pan
	 */
	private void updateAuthState(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("id",m.getId()+"");
			map.put("state", state);
			updTkAuthRecord(map);
		}
	}
	

	
}
