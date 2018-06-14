package com.kuangchi.sdd.attendanceConsole.noCheckCard.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.noCheckCard.dao.INoCheckCardDao;
import com.kuangchi.sdd.attendanceConsole.noCheckCard.model.NoCheckCard;
import com.kuangchi.sdd.attendanceConsole.noCheckCard.service.INoCheckCardService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
@Transactional
@Service("NoCheckCardServiceImpl")
public class NoCheckCardServiceImpl  implements INoCheckCardService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "NoCheckCardDaoImpl")
	private INoCheckCardDao iNoCheckCardDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	
	/**
	 * 查询所有员工免打卡信息  
	 */
	@Override
	public Grid getAllNoCheckCardByStaff(NoCheckCard noCheck_info,String page, String size) {
		List<NoCheckCard> noCheck=iNoCheckCardDao.getAllNoCheckCardByStaff(noCheck_info, page, size);
		Integer count=iNoCheckCardDao.getAllNoCheckCardByStaffCount(noCheck_info);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(noCheck);
		return grid;
		
	}
	
	/**
	 * 新增员工免打卡信息
	 */
	@Override
	public Boolean insertNoCheckCard(NoCheckCard noCheck_info) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "免打卡信息");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", noCheck_info.getCreate_user());
        try{
        	Boolean obj=iNoCheckCardDao.insertNoCheckCard(noCheck_info);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "新增成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "新增失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return false;
        }
	}
	/**
	 * 根据id查询员工信息
	 */
	@Override
	public List<NoCheckCard> selectNoCheckCardByStaff(Integer id) {
		List<NoCheckCard> noCheck=iNoCheckCardDao.selectNoCheckCardByStaff(id);
		return noCheck;
	}
	/**
	 * 根据id查询部门信息
	 */
	@Override
	public List<NoCheckCard> selectNoCheckCardByDept(Integer id) {
		List<NoCheckCard> noCheck=iNoCheckCardDao.selectNoCheckCardByDept(id);
		return noCheck;
	}
	
	/**
	 * 修改员工免打卡信息
	 */
	@Override
	public Boolean updateNoCheckCardByStaff(NoCheckCard noCheck_info) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "免打卡信息");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", noCheck_info.getCreate_user());
        try{
        	Boolean obj=iNoCheckCardDao.updateNoCheckCardByStaff(noCheck_info);
    		if(obj==true){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改成功");
    	        logDao.addLog(log);
    	    return true;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改失败");
    			logDao.addLog(log);
    			return false;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	}
	/**
	 * 删除员工免打卡信息
	 */
	@Override
	public Integer deleNoCheckCardByStaff(String id,String create_user) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "免打卡信息");
        log.put("V_OP_FUNCTION", "删除");
        log.put("V_OP_ID", create_user);
        try{
        	Integer obj=iNoCheckCardDao.deleNoCheckCardByStaff(id);
    		if(obj==1){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除成功");
    	        logDao.addLog(log);
    	    return 1;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除失败");
    			logDao.addLog(log);
    			return 0;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "删除异常");
    		logDao.addLog(log);
    		return 0;
        }
	}
	
	
	
	
	
	/**
	 * 查询所有部门免打卡信息  
	 */
	@Override
	public Grid getAllNoCheckCardByDept(NoCheckCard noCheck_info,String page, String size) {
		List<NoCheckCard> noCheck=iNoCheckCardDao.selectNoCheckCardsByDept(noCheck_info, page, size);
		Integer count=iNoCheckCardDao.selectNoCheckCardsByDeptCount(noCheck_info);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(noCheck);
		return grid;
		
	}
	@Override
	public int selectNoCheckCardByDeptNum(String dept_num) {
		return iNoCheckCardDao.selectNoCheckCardByDeptNum(dept_num);
	}

	@Override
	public Boolean insertNoCheckCardByDept(NoCheckCard noCheck_info) {

		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "免打卡信息");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", noCheck_info.getCreate_user());
        try{
        	Boolean obj=iNoCheckCardDao.insertNoCheckCardByDept(noCheck_info);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "新增成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "新增失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return false;
        }
	}
	@Override
	public Boolean updateNoCheckCardByDept(NoCheckCard noCheck_info) {

		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "免打卡信息");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", noCheck_info.getCreate_user());
        try{
        	Boolean obj=iNoCheckCardDao.updateNoCheckCardByDept(noCheck_info);
    		if(obj==true){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改成功");
    	        logDao.addLog(log);
    	    return true;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改失败");
    			logDao.addLog(log);
    			return false;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	}
	
	/**
	 * 删除部门免打卡信息
	 */
	@Override
	public Integer deleNoCheckCardByDept(String id,String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "免打卡信息");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		try{
			Integer obj=iNoCheckCardDao.deleNoCheckCardByDept(id);
			if(obj==1){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "删除成功");
				logDao.addLog(log);
				return 1;    
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "删除失败");
				logDao.addLog(log);
				return 0;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "删除异常");
			logDao.addLog(log);
			return 0;
		}
	}

	@Override
	public Boolean getNoCheckCardByStaffNum(NoCheckCard noCheck_info) {
		boolean flage=false;
		String[] getDm = noCheck_info.getStaff_num().split(",");
		int l=getDm.length;
		for (int n = 0; n < getDm.length; n++) {
			noCheck_info.setStaff_num(getDm[n]);
			List<NoCheckCard> noCheck= iNoCheckCardDao.getNoCheckCardByStaffNum(noCheck_info);
			for(int i=0;i<noCheck.size();i++){
			 String fromTime=noCheck_info.getFrom_time();
			 String toTime=noCheck_info.getTo_time();
			 String oldFromTime=noCheck.get(i).getFrom_time();
			 String oldToTime=noCheck.get(i).getTo_time();
			 //若s1>s2,则 s1.compareTo(s2)>0 
			 if((fromTime.compareTo(oldFromTime) >= 0 && fromTime.compareTo(oldToTime)<=0) || 
			    (fromTime.compareTo(oldFromTime) <= 0 && toTime.compareTo(oldToTime)>=0) ||
			    (toTime.compareTo(oldFromTime) >= 0 && toTime.compareTo(oldToTime)<=0) ){
				 flage=true; //时间有重叠部分
			 }
			}
		}
		return flage;
	}
	
	@Override
	public Boolean getNoCheckCardByDeptNum(NoCheckCard noCheck_info) {
		boolean flage=false;
		List<NoCheckCard> noCheck= iNoCheckCardDao.getNoCheckCardByDeptNum(noCheck_info);
		for(int i=0;i<noCheck.size();i++){
		 String fromTime=noCheck_info.getFrom_time();
		 String toTime=noCheck_info.getTo_time();
		 String oldFromTime=noCheck.get(i).getFrom_time();
		 String oldToTime=noCheck.get(i).getTo_time();
		 //若s1>s2,则 s1.compareTo(s2)>0 
		 if((fromTime.compareTo(oldFromTime) >= 0 && fromTime.compareTo(oldToTime)<=0) || 
		    (fromTime.compareTo(oldFromTime) <= 0 && toTime.compareTo(oldToTime)>=0) ||
		    (toTime.compareTo(oldFromTime) >= 0 && toTime.compareTo(oldToTime)<=0) ){
			 flage=true; //时间没有重叠部分
		 }
		}
		return flage;
	}
	

	
}
