package com.kuangchi.sdd.baseConsole.doorinfo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;

import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.dao.DeviceGroupDao;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceDoorBean;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.doorinfo.dao.IDoorInfoDao;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.baseConsole.doorinfo.service.IDoorInfoService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
@Transactional
@Service("DoorInfoServiceImpl")
public class DoorInfoServiceImpl  implements IDoorInfoService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "DoorInfoDaoImpl")
	private IDoorInfoDao doorinfoDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "deviceDao")
	private DeviceDao deviceDao;
	
	@Resource(name ="deviceGroupDaoImpl")
	private DeviceGroupDao deviceGroupDao;
	
	/**
	 * 新增门信息
	 */
	@Override
	public Boolean insertDoorinfo(DoorInfoModel doorinfo) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门信息维护");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", doorinfo.getCreate_user());
        try{
        	Boolean obj=doorinfoDao.insertDoorinfo(doorinfo);
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
	 * 删除门信息
	 */
	@Override
	public Integer deleteDoorinfo(DoorInfoModel doorinfo) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门信息维护");
        log.put("V_OP_FUNCTION", "删除");
        log.put("V_OP_ID", doorinfo.getCreate_user());
        try{
        	Integer obj=doorinfoDao.deleteDoorinfo(doorinfo);
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
	 * 删除门卡绑定
	 */
	@Override
	public Integer deleteDoorPeopleauthorityService(DoorInfoModel doorinfo) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门信息维护");
        log.put("V_OP_FUNCTION", "删除门绑定");
        log.put("V_OP_ID", doorinfo.getCreate_user());
        try{
        	Integer obj=doorinfoDao.deleteDoorPeopleauthority(doorinfo);
    		if(obj==1){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除成功");
    	        logDao.addLog(log);
    	    return 1;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "没有卡绑定");
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
	 * 根据ID查一行全部信息
	 */
	@Override
	public List<DoorInfoModel> selectDoorinfoById(Integer door_id) {
		List<DoorInfoModel> door=doorinfoDao.selectDoorinfoById(door_id);
		return door;
	}
	
	/**
	 * 查询全部信息
	 */
	@Override
	public Grid selectAllDoorinfos(DoorInfoModel doorinfo,String page, String size) {
		Integer count=this.doorinfoDao.getAllDoorinfoCount(doorinfo);
		List<DoorInfoModel> door= this.doorinfoDao.selectAllDoorinfos(doorinfo, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(door);
		return grid;
	}
	
	/**
	 * 门编号自动设置加一
	 */
	@Override
	public String getDoorID(String door_num) {
		return String.valueOf(this.doorinfoDao.getDoorID(door_num));
	}

	@Override
	public Integer getAllDoorinfoCount(DoorInfoModel doorinfo) {
		return null;
	}
	
	
	//修改门信息
	@Override
	public Boolean updateDoorNameAndDes(DoorInfoModel doorinfo) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门信息维护");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", doorinfo.getCreate_user());
        try{
        	Boolean obj=doorinfoDao.updateDoorNameAndDes(doorinfo);
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
	//新增时查询设备编号
	@Override
	public List<DoorInfoModel> selectDeviceNumAdd() {
		List<DoorInfoModel> door=this.doorinfoDao.selectDeviceNumAdd();
		return door;
	}

	//根据设备编号查询设备mac地址
	@Override
	public DeviceInfo queryMacByDeviceNum(String device_num) {
		
		return doorinfoDao.queryMacByDeviceNum(device_num);
	}
	
	//新增删除的门
	@Override
	public Integer updateInsertDoor(DoorInfoModel doorinfo) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门信息维护");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", doorinfo.getCreate_user());
        try{
        	Integer obj= doorinfoDao.updateInsertDoor(doorinfo);
    		if(obj==1){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "新增成功");
    	        logDao.addLog(log);
    	        return 1; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "新增失败");
    			logDao.addLog(log);
    			return 0;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return 0;
        }
		
		
	}

	@Override
	public String querydevicemin(String device_num) {
		return doorinfoDao.querydevicemin(device_num);
	}

	@Override
	public Integer updateDoorinfo(Integer first_open, String device_num,String door_num,String login_user) {
		Integer num=doorinfoDao.updateDoorinfo(first_open,device_num,door_num);
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门参数维护");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", login_user);
        log.put("V_OP_MSG", "取消首卡开门");
		if(num==1){
			log.put("V_OP_TYPE", "业务");
   	        logDao.addLog(log);
			return 1;
		}else{
			log.put("V_OP_TYPE", "异常");
			logDao.addLog(log);
			return 0;
		}
	}

	@Override
	public List<DoorInfoModel> selectDoorInfo(Map<String, Object> map) {
		try{
			/*Grid<DoorInfoModel> grid = new Grid<DoorInfoModel>();
			List<DoorInfoModel> resultList = doorinfoDao.selectDoorInfo(map);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(1);
			} else {
				grid.setTotal(0);
			}
			return grid;*/
			List<DoorInfoModel> list = doorinfoDao.selectDoorInfo(map);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer updatehaddenflag(Integer hadden_flag, String device_num,
			String door_num,String login_user) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("hadden_flag", hadden_flag);
		map.put("device_num", device_num);
		map.put("door_num", door_num);
		Integer num=doorinfoDao.updatehaddenflag(map);
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "门参数维护");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", login_user);
        log.put("V_OP_MSG", "取消屏蔽门");
		if(num==1){
			log.put("V_OP_TYPE", "业务");
   	        logDao.addLog(log);
			return 1;
		}else{
			log.put("V_OP_TYPE", "异常");
			logDao.addLog(log);
			return 0;
		}
		
	}
    
	/*根据设备编号获取设备信息*/
	@Override
	public List<DoorInfoModel> selectDoorInfoByDeviceNum(String device_mac) {
		String  device_num=deviceDao.getDeviceNumByMac(device_mac);
		return  doorinfoDao.selectDoorInfoByDeviceNum(device_num);
	}

	@Override
	public Grid<Map> getCardsInfoDynamic(Map map) {
		Grid<Map> grid=new Grid<Map>();
		grid.setRows(doorinfoDao.getCardsInfoDynamic(map));
		grid.setTotal(doorinfoDao.countCardsInfoDynamic(map));
		return grid;
	}

	@Override
	public List<Map> getStaffInfoByMultiCardnum(String multi_open_card_num) {
		return doorinfoDao.getStaffInfoByMultiCardnum(multi_open_card_num);
	}
	
	@Override
	public Map<String, Object> selectDeviceDoor(Map<String, Object> map){
		try{
			return doorinfoDao.selectDeviceDoor(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateMonitor(String device_yes, String door_yes, String device_no, String door_no, String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			//监控
			String[] device_num = device_yes.split(",");
			String[] door_num = door_yes.split(",");
			List<Map<String, Object>> list_yes = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> list_no = new ArrayList<Map<String, Object>>();
			for(int i=0; i<device_num.length; i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("device_num", device_num[i]);
				map.put("door_num", door_num[i]);
				list_yes.add(map);
			//	doorinfoDao.updateMonitor(map);
			}
			//不监控
			device_num = device_no.split(",");
			door_num = door_no.split(",");
			for(int i=0; i<device_num.length; i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("device_num", device_num[i]);
				map.put("door_num", door_num[i]);
				list_no.add(map);
			//	doorinfoDao.updateNoMonitor(map);
			}
			doorinfoDao.updateMonitorList(list_yes);
			doorinfoDao.updateNoMonitorList(list_no);
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "门信息管理");
	        log.put("V_OP_FUNCTION", "修改");
	        log.put("V_OP_ID", login_user);
	        log.put("V_OP_MSG", "修改监控门");
	        logDao.addLog(log);
		}
	}
	
	@Override
	public Tree getMonitorTree(){
		List<String> group_list = new ArrayList<String>();
		List<String> device_list = new ArrayList<String>();
		List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
		List<DoorModel> allDoor=doorinfoDao.getMonitorDoor();
		List<DeviceDoorBean> allNodes=new ArrayList<DeviceDoorBean>();
		//遍历门  
		d1:
		for(DoorModel door:allDoor){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(door.getDoorName());
			deviceDoorBean.setNum(door.getDoorNum());
			deviceDoorBean.setParentNum(door.getDeviceNum());
			deviceDoorBean.setType(2);
			deviceDoorBean.setDoorId(door.getDoorId());
			allNodes.add(deviceDoorBean);
			for(String num: device_list){
				if(num.equals(door.getDeviceNum())){
					continue d1;
				}
			}
			device_list.add(door.getDeviceNum());
		}
		//遍历设备  父节点和标志
		d2:
		for(String device_num : device_list){
			for(DeviceModel device:allDevice){
				if(device_num.equals(device.getDeviceNum())){
					DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
					deviceDoorBean.setName(device.getDeviceName());
					deviceDoorBean.setNum(device.getDeviceNum());
					deviceDoorBean.setParentNum(device.getDeciveGroupNum());
					deviceDoorBean.setType(1);
					deviceDoorBean.setIp(device.getLocalIpAddress());
					allNodes.add(deviceDoorBean);
					for(String num : device_list){
						if(num.equals(device.getDeciveGroupNum())){
							continue d2;
						}
					}
					group_list.add(device.getDeciveGroupNum());
				}
			}
		}
		//遍历设备组
		for(DeviceGroupModel dg:allDeviceGroup){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(dg.getGroupName());
			deviceDoorBean.setNum(dg.getGroupNum());
			deviceDoorBean.setParentNum(dg.getParentGroupNum());
			deviceDoorBean.setType(0);
			allNodes.add(deviceDoorBean);
		}
		 Tree tree=new Tree();
		 for(DeviceDoorBean deviceDoorBean:allNodes){
			 if(null==deviceDoorBean.getParentNum()){
				 tree.setId(deviceDoorBean.getNum());
				 tree.setText(deviceDoorBean.getName());
				 tree.setState("open");
				 tree.setIconCls("icon-enterprise");
			    Map<String,Object> map=new HashMap<String, Object>();
			    map.put("isDevice",deviceDoorBean.getType());
			    tree.setAttributes(map);
			 }
		 } 
		 InitTree(tree, allNodes);
		 return tree;
	}
	
	
	
	void InitTree(Tree tree,List<DeviceDoorBean> deviceDoorBeanList){
		   for(DeviceDoorBean deviceDoorBean:deviceDoorBeanList){
			   if(deviceDoorBean.getParentNum()!=null&&!"".equals(deviceDoorBean.getParentNum())&&tree.getId().equals(deviceDoorBean.getParentNum())){
			    	Tree treeNode=new Tree();
			    	treeNode.setId(deviceDoorBean.getNum());
			    	Map<String,Object> map=new HashMap<String, Object>();
			    	map.put("isDevice",deviceDoorBean.getType());
			    	if(deviceDoorBean.getType()==2){
			    		map.put("doorId",deviceDoorBean.getDoorId());
			    		map.put("deviceNumAndDoorNum",deviceDoorBean.getParentNum()+""+deviceDoorBean.getNum());
			    	}else if(deviceDoorBean.getType()==1){
			    		map.put("ip",deviceDoorBean.getIp());
			    	}
			    	treeNode.setAttributes(map);
			    	treeNode.setText(deviceDoorBean.getName());
			    	treeNode.setState("open");
			    	if(deviceDoorBean.getType()==0){
			    		treeNode.setIconCls("icon-deviceGroup_v1");
			    	}else if(deviceDoorBean.getType()==1){
			    		treeNode.setIconCls("icon-device_v1");
			    	}else{
			    		treeNode.setIconCls("icon-door");
			    	}
			    	treeNode.setPid(tree.getId());
			    	tree.getChildren().add(treeNode);
				    InitTree(treeNode, deviceDoorBeanList);
			    }
		   }
		}
	
	@Override
	public Tree getCheckedTree(){
		List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
		List<DoorModel> monitorDoor=doorinfoDao.getMonitorDoor();
		List<DoorModel> allDoor=deviceGroupDao.getDoorList();
		List<DeviceDoorBean> allNodes=new ArrayList<DeviceDoorBean>();
		//遍历设备组
		for(DeviceGroupModel dg:allDeviceGroup){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(dg.getGroupName());
			deviceDoorBean.setNum(dg.getGroupNum());
			deviceDoorBean.setParentNum(dg.getParentGroupNum());
			deviceDoorBean.setType(0);
			allNodes.add(deviceDoorBean);
		}
		//遍历设备  父节点和标志
		for(DeviceModel device:allDevice){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(device.getDeviceName());
			deviceDoorBean.setNum(device.getDeviceNum());
			deviceDoorBean.setParentNum(device.getDeciveGroupNum());
			deviceDoorBean.setType(1);
			deviceDoorBean.setIp(device.getLocalIpAddress());
			allNodes.add(deviceDoorBean);
		}		
		//遍历门  
		for(DoorModel door:allDoor){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(door.getDoorName());
			deviceDoorBean.setNum(door.getDoorNum());
			deviceDoorBean.setParentNum(door.getDeviceNum());
			deviceDoorBean.setType(2);
			deviceDoorBean.setDoorId(door.getDoorId());
			allNodes.add(deviceDoorBean);
		}
		 Tree tree=new Tree();
		 for(DeviceDoorBean deviceDoorBean:allNodes){
			 if(null==deviceDoorBean.getParentNum()){
				 tree.setId(deviceDoorBean.getNum());
				 tree.setText(deviceDoorBean.getName());
				 tree.setState("open");
				 tree.setIconCls("icon-enterprise");
			    Map<String,Object> map=new HashMap<String, Object>();
			    map.put("isDevice",deviceDoorBean.getType());
			    tree.setAttributes(map);
			 }
		 } 	
		 initCheckedTree(monitorDoor, tree, allNodes);
		 return tree;
	}
	
	void initCheckedTree(List<DoorModel> monitorDoor, Tree tree,List<DeviceDoorBean> deviceDoorBeanList){
	   for(DeviceDoorBean deviceDoorBean:deviceDoorBeanList){
		   if(deviceDoorBean.getParentNum()!=null&&!"".equals(deviceDoorBean.getParentNum())&&tree.getId().equals(deviceDoorBean.getParentNum())){
				Tree treeNode=new Tree();
				treeNode.setId(deviceDoorBean.getNum());
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("isDevice",deviceDoorBean.getType());
				if(deviceDoorBean.getType()==2){
					map.put("doorId",deviceDoorBean.getDoorId());
				map.put("deviceNumAndDoorNum",deviceDoorBean.getParentNum()+""+deviceDoorBean.getNum());
				}else if(deviceDoorBean.getType()==1){
					map.put("ip",deviceDoorBean.getIp());
				}
				treeNode.setAttributes(map);
				treeNode.setText(deviceDoorBean.getName());
				treeNode.setState("open");
				if(deviceDoorBean.getType()==0){
					treeNode.setIconCls("icon-deviceGroup_v1");
				}else if(deviceDoorBean.getType()==1){
					treeNode.setIconCls("icon-device_v1");
				}else{
					treeNode.setIconCls("icon-door");
					for(int i=0; i<monitorDoor.size(); i++){
						DoorModel door = monitorDoor.get(i);
						if(deviceDoorBean.getParentNum().equals(door.getDeviceNum()) 
								&& deviceDoorBean.getNum().equals(door.getDoorNum())){	//找到门正在被监控
							treeNode.setChecked(true);
							monitorDoor.remove(i);
							break;
						}
					}
		    	}
		    	treeNode.setPid(tree.getId());
		    	tree.getChildren().add(treeNode);
		    	initCheckedTree(monitorDoor, treeNode, deviceDoorBeanList);
		    }
	   }
	}
	
	@Override
	public Grid<Map<String, Object>> getNoMonitorParam(Map<String, Object> map) {
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		List<Map<String, Object>> resultList = doorinfoDao.getNoMonitorParam(map);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(doorinfoDao.getNoMonitorCount(map));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}
	
	@Override
	public List<Map<String, Object>> getMonitor(Map<String, Object> map){
		try{
			return doorinfoDao.getMonitor(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean updateMonitorParam(String device_yes, String door_yes, String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			boolean r = doorinfoDao.setNoMonitor();
			if(r){
				//监控
				String[] device_num = device_yes.split(",");
				String[] door_num = door_yes.split(",");
				List<Map<String, Object>> list_yes = new ArrayList<Map<String, Object>>();
				for(int i=0; i<device_num.length; i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("device_num", device_num[i]);
					map.put("door_num", door_num[i]);
					list_yes.add(map);
				}
				doorinfoDao.updateMonitorList(list_yes);
				log.put("V_OP_TYPE", "业务");
				return true;
			}else {
				log.put("V_OP_TYPE", "异常");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "门信息管理");
	        log.put("V_OP_FUNCTION", "修改");
	        log.put("V_OP_ID", login_user);
	        log.put("V_OP_MSG", "修改监控门");
	        logDao.addLog(log);
		}
	}
	

	
	
}
