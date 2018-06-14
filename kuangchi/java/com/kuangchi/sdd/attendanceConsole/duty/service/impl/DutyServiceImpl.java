package com.kuangchi.sdd.attendanceConsole.duty.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.duty.dao.DutyDao;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.duty.service.DutyService;
import com.kuangchi.sdd.attendanceConsole.dutyuser.dao.DutyUserDao;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;


@Service("dutySerivceImpl")
public class DutyServiceImpl implements DutyService {
	
	@Resource(name = "dutyDaoImpl")
	DutyDao dutyDao;
	
	@Resource(name = "LogDaoImpl")
    LogDao logDao;
	
	@Resource(name = "dutyUserDaoImpl")
	DutyUserDao dutyUserDao;
	
	@Resource(name="dutyUserServiceImpl")
	DutyUserService dutyUserService;
	
	@Override
	public boolean insertDuty(Duty duty,String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result = false;
		try{
			final String old_id = dutyDao.getDefautlId();
			if("1".equals(duty.getIs_default())){
				dutyDao.updateIsDefault(old_id);
			}
			duty.setCreate_user(create_user);
			result = dutyDao.insertDuty(duty);
			
			if(result){
				//修改默认班次
				if("1".equals(duty.getIs_default())){
					final Duty new_duty = duty;
					new Thread(new Runnable() {
						public void run() {
							try {
								dutyUserService.editDefaultDutyReg(old_id, new_duty.getId().toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "工作班次维护");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "新增班次信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteDutyById(String duty_ids,String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result = dutyDao.deleteDutyById(duty_ids);
		log.put("V_OP_NAME", "工作班次维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "删除班次信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	
	

	@Override
	public boolean updateDuty(Duty duty,String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		boolean result = dutyDao.updateDuty(duty);
		log.put("V_OP_NAME", "工作班次维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "修改班次信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public List<Duty> getDutyByParam(Duty duty) {
		return dutyDao.getDutyByParam(duty);
	}

	@Override
	public Grid<Duty> getDutyByParamPage(Duty duty) {
		Grid<Duty> grid = new Grid<Duty>();
		List<Duty> resultList = dutyDao.getDutyByParamPage(duty);
		grid.setRows(resultList);
		if(null != resultList){
			grid.setTotal(dutyDao.getDutyByParamPageCounts(duty));
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public void batchAddDuty(List<Duty> dutyList) {
		dutyDao.batchAddDuty(dutyList);
	}

	@Override
	public Duty getDutyById(String id) {
		return dutyDao.getDutyById(id);
	}

	@Override
	public Integer getDutyByParamCounts(Duty duty) {
		return dutyDao.getDutyByParamCounts(duty);
	}

	@Override
	public Integer  getDutyUserByDutyId(String duty_id){ 
		Integer dutyUserCount = null;
		dutyUserCount = dutyUserDao.getDutyUserByDutyId(duty_id);
		return dutyUserCount;
	}

	@Override
	public void updateAllIsDefault(String id) {
		dutyDao.updateAllIsDefault(id);
		
	}

	@Override
	public void updateAllIsDefaultt(int id) {
		dutyDao.updateAllIsDefaultt(id);
		
	}

	@Override
	public Integer selectMaxId() {
		
		return dutyDao.selectMaxId();
	}

	@Override
	public Duty getDefaultDuty() {
		 
		return dutyDao.getDefaultDuty();
	}
	
	@Override
	public String getDefautlId(){
		try{
			return dutyDao.getDefautlId();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public boolean updateIsDefault(String id, String login_user){
		Map<String,String> log = new HashMap<String,String>();
		try{
			log.put("V_OP_TYPE", "业务");
			return dutyDao.updateIsDefault(id);
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "工作班次维护");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改默认班次");
			logDao.addLog(log);
		}
	}
	
	@Override
	public boolean setDefaultDuty(final String new_id, String login_user){
		Map<String,String> log = new HashMap<String,String>();
		try{
			final String old_id = dutyDao.getDefautlId();
			dutyDao.updateIsDefault(old_id);
			boolean r = dutyDao.setDefaultDuty(new_id);
			if(r){
				//新线程
				new Thread(new Runnable() {
					public void run() {
						try {
							dutyUserService.editDefaultDutyReg(old_id, new_id);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				log.put("V_OP_TYPE", "业务");
			}else {
				log.put("V_OP_TYPE", "异常");
			}
			return r; 
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "工作班次维护");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "设置默认班次");
			logDao.addLog(log);
		}
	}

}
