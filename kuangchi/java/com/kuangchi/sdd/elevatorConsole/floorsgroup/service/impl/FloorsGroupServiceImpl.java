package com.kuangchi.sdd.elevatorConsole.floorsgroup.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.dao.FloorsGroupDao;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.model.FloorsGroupModel;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.service.FloorsGroupService;
@Transactional
@Service("FloorsGroupServiceImpl")
public class FloorsGroupServiceImpl implements FloorsGroupService {
private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "FloorsGroupDaoImpl")
	private FloorsGroupDao floorsGroupDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public Boolean insertFloorGroup(FloorsGroupModel floor, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=floorsGroupDao.insertFloorGroup(floor);
    		if(obj==true){
    	        String [] str= floor.getFloor_num().split(",");
				for (int i = 0; i < str.length; i++) {
					if(str[i]!=null){
						floor.setFloor_num(str[i]);
						floorsGroupDao.insertFloorGroupMap(floor);
					}
				}
				log.put("V_OP_TYPE", "业务");
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "异常");
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
    		return false;
        }finally {
        	log.put("V_OP_NAME", "楼层组信息维护");
            log.put("V_OP_FUNCTION", "新增");
        	log.put("V_OP_ID", login_user);
        	log.put("V_OP_MSG", "新增楼层组");
        	logDao.addLog(log);
        }
	}
	//查询楼层与楼层组表的数据
	@Override
	public Grid selectAllFloorGroup(FloorsGroupModel floor, String Page,
			String size) {
		Integer count=floorsGroupDao.selectAllFloorGroupCount(floor);
		List<FloorsGroupModel> lists=new ArrayList<FloorsGroupModel>();
		List<FloorsGroupModel> list=null;
		FloorsGroupModel floors=new FloorsGroupModel();
		
			List<FloorsGroupModel> floorgroupnum=floorsGroupDao.selectfloorgroupnum(Page,
					size);//查询映射表的的所有楼层组编号
			for (FloorsGroupModel floorsGroupModel : floorgroupnum) {
				floors.setFloor_group_num(floorsGroupModel.getFloor_group_num());
				 list=floorsGroupDao.selectAllFloorGroup(floors, Page, size);
				 FloorsGroupModel floors2=new FloorsGroupModel();
				for (FloorsGroupModel floorsGroupModel2 : list) {
					if(floors2.getFloor_num()==null){
						floors2.setFloor_num("");
					}
					floors2.setFloor_num(floorsGroupModel2.getFloor_num()+","+floors2.getFloor_num());
					floors2.setFloor_group_num(floorsGroupModel2.getFloor_group_num());
					floors2.setFloor_group_name(floorsGroupModel2.getFloor_group_name());
					
				}
				lists.add(floors2);
			}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(lists);
		return grid;
		
	}
	//查询组编号是否存在
	@Override
	public List<FloorsGroupModel> selectByfloorgroupnum(String floorgroupnum) {
		// TODO Auto-generated method stub
		return floorsGroupDao.selectByfloorgroupnum(floorgroupnum);
	}
	//根据编号查询信息
	@Override
	public List<FloorsGroupModel> selectByfloor(String floorgroupnum) {
		
			List<FloorsGroupModel> floors=floorsGroupDao.selectByfloor(floorgroupnum);
			List<FloorsGroupModel> lists=new ArrayList<FloorsGroupModel>();
			 FloorsGroupModel floors2=new FloorsGroupModel();
				for (FloorsGroupModel floorsGroupModel2 : floors) {
					if(floors2.getFloor_num()==null){
						floors2.setFloor_num("");
					}
					floors2.setFloor_num(floorsGroupModel2.getFloor_num()+","+floors2.getFloor_num());
					floors2.setFloor_group_num(floorsGroupModel2.getFloor_group_num());
					floors2.setFloor_group_name(floorsGroupModel2.getFloor_group_name());
				}
				lists.add(floors2);
				return	lists;	
	}
	//更新楼层组信息表
	@Override
	public Boolean updateFloorgroupname(FloorsGroupModel floor, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
        try{
        	Boolean obj=floorsGroupDao.updateFloorgroupname(floor);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        floorsGroupDao.deleteFloorgroupmap(floor.getFloor_group_num());
    	        String [] str= floor.getFloor_num().split(",");
				for (int i = 0; i < str.length; i++) {
					if(str[i]!=null){
						floor.setFloor_num(str[i]);
						floorsGroupDao.insertFloorGroupMap(floor);
					}
				}
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "异常");
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
    		return false;
        }finally {
        	log.put("V_OP_NAME", "楼层组信息维护");
            log.put("V_OP_FUNCTION", "修改");
            log.put("V_OP_MSG", "修改楼层组");
            log.put("V_OP_ID", login_user);
            logDao.addLog(log);
        }
        
	}
	//删除楼层组信息
	@Override
	public Boolean updateDeleteFlag(String floorgroupnum, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=floorsGroupDao.updateDeleteFlag(floorgroupnum);
    		if(obj){
    	        log.put("V_OP_TYPE", "业务");
    	        floorsGroupDao.deleteFloorgroupmap(floorgroupnum);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
    		return false;
        }finally {
        	log.put("V_OP_NAME", "楼层组信息维护");
            log.put("V_OP_FUNCTION", "删除");
            log.put("V_OP_MSG", "删除楼层组");
            log.put("V_OP_ID", login_user);
            logDao.addLog(log);
        }
	}
	//判断授权表是否存在楼层组编号
	@Override
	public List<FloorsGroupModel> selectByAuthority(String floorgroupnum) {
		return floorsGroupDao.selectByAuthority(floorgroupnum);
	}
	//根据楼层组编号查询楼层组名称
	@Override
	public List<FloorsGroupModel> selectByFloorName() {
		return floorsGroupDao.selectByFloorName();
	}
	//页面显示数据
	@Override
	public Grid SelectAllFloorGroups(FloorsGroupModel floor, String Page,
			String size) {
		Integer count=floorsGroupDao.SelectAllFloorGroupsCount(floor);
		List<FloorsGroupModel> lists=floorsGroupDao.SelectAllFloorGroups(floor, Page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(lists);
		return grid;
	}

	@Override
	public List<FloorsGroupModel> getFloorGroupInfo(){
		List<FloorsGroupModel> list = new ArrayList<FloorsGroupModel>();
		try{
			list = floorsGroupDao.getFloorGroupInfo();
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return list;
		}
	}
	
	@Override
	public List<String> getFloorByGroup(String floor_group_num){
		try{
			return floorsGroupDao.getFloorByGroup(floor_group_num);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public String getfloorgroupnum() {
		return floorsGroupDao.getfloorgroupnum();
	}
	
}
