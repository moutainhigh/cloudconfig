package com.kuangchi.sdd.baseConsole.deviceGroup.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceGroup.dao.DeviceGroupDao;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceDoorBean;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.baseConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;

@Transactional
@Service("deviceGroupService")
public class DeviceGroupServiceImpl  implements DeviceGroupService{
	
	public static final Logger LOG = Logger.getLogger(DeviceGroupServiceImpl.class);

	@Resource(name ="deviceGroupDaoImpl")
	private DeviceGroupDao deviceGroupDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "cDeviceService")
	IDeviceService xfDeviceService;
	
	private Tree allTree;
	
	@Override
	public  Tree getDeviceDoorTree() {
		List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
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
		 InitTree(tree, allNodes);
		 return tree;
	}
	
	
	@Override
	public  Tree getDeviceDoorTreeLazy(String id) {
		List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroupById(id);
//		List<DeviceModel> allDevice=new ArrayList<DeviceModel>();
		List<DoorModel> allDoor=new ArrayList<DoorModel>();
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
//		List<DoorModel> allDoor=deviceGroupDao.getDoorList();
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
		InitTreeA(tree, allNodes);
		return tree;
	}
	
	@Override
	public  List<Tree> getDeviceDoorTreeLazyList(String id) {
		List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroupByIdA(id);
		//List<DeviceModel> allDevice=new ArrayList<DeviceModel>();
		//if(allDeviceGroup.size()==0){
			//allDevice=new ArrayList<DeviceModel>();
		//}else{
			List<DeviceGroupModel> tmpList=new ArrayList<DeviceGroupModel>();
			DeviceGroupModel model=new DeviceGroupModel();
			model.setGroupNum(id);
			tmpList.add(model);
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModelA(tmpList);
		//}
//		List<DeviceModel> allDevice=new ArrayList<DeviceModel>();
//		List<DoorModel> allDoor=new ArrayList<DoorModel>();
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
/*		for(DoorModel door:allDoor){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(door.getDoorName());
			deviceDoorBean.setNum(door.getDoorNum());
			deviceDoorBean.setParentNum(door.getDeviceNum());
			deviceDoorBean.setType(2);
			deviceDoorBean.setDoorId(door.getDoorId());
			allNodes.add(deviceDoorBean);
		}*/
		List<Tree> treeList=new ArrayList<Tree>();
		for(DeviceDoorBean deviceDoorBean:allNodes){
			Tree tree=new Tree();
			toTree(tree,deviceDoorBean);
			treeList.add(tree);
		} 		 
		return treeList;
	}
	
	@Override
	public  List<Tree> getDeviceDoorTreeLazyListA(String deviceId) {
		List<DeviceDoorBean> allNodes=new ArrayList<DeviceDoorBean>();
		List<DoorModel> allDoor=deviceGroupDao.getDoorListA(deviceId);
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
		List<Tree> treeList=new ArrayList<Tree>();
		for(DeviceDoorBean deviceDoorBean:allNodes){
			Tree tree=new Tree();
			toTree(tree,deviceDoorBean);
			treeList.add(tree);
		} 		 
		return treeList;
	}
	
	private void toTree(Tree treeNode,DeviceDoorBean deviceDoorBean){
				treeNode.setId(deviceDoorBean.getNum());
				treeNode.setPid(deviceDoorBean.getParentNum());
				treeNode.setText(deviceDoorBean.getName());
			   if(deviceDoorBean.getParentNum()!=null&&!"".equals(deviceDoorBean.getParentNum())){
			    	Map<String,Object> map=new HashMap<String, Object>();
			    	map.put("isDevice",deviceDoorBean.getType());
			    	if(deviceDoorBean.getType()==2){
			    		map.put("doorId",deviceDoorBean.getDoorId());
			    		map.put("deviceNumAndDoorNum",deviceDoorBean.getParentNum()+""+deviceDoorBean.getNum());
			    	}else if(deviceDoorBean.getType()==1){
			    		map.put("ip",deviceDoorBean.getIp());
			    	}
			    	treeNode.setAttributes(map);
			    	treeNode.setState("closed");
			    	if(deviceDoorBean.getType()==0){
			    		treeNode.setIconCls("icon-deviceGroup_v1");
			    	}else if(deviceDoorBean.getType()==1){
			    		treeNode.setIconCls("icon-device_v1");
			    	}else{
			    		treeNode.setIconCls("icon-door");
			    		treeNode.setState("open");
			    	}
			    }
		}
	
	
	@Override
	public Tree getOnlyDeviceGroupTree() {
		List<DeviceGroupModel> getAllDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
		List<DeviceDoorBean> deviceGroupNodes=new ArrayList<DeviceDoorBean>();
		//遍历设备组
		for(DeviceGroupModel dg:getAllDeviceGroup){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(dg.getGroupName());
			deviceDoorBean.setNum(dg.getGroupNum());
			deviceDoorBean.setParentNum(dg.getParentGroupNum());
			deviceDoorBean.setType(0);
			deviceGroupNodes.add(deviceDoorBean);
		}
		//遍历设备  父节点和标志
		for(DeviceModel device:allDevice){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(device.getDeviceName());
			deviceDoorBean.setNum(device.getDeviceNum());
			deviceDoorBean.setParentNum(device.getDeciveGroupNum());
			deviceDoorBean.setType(1);
			deviceGroupNodes.add(deviceDoorBean);
		}		
		 Tree tree=new Tree();
		 for(DeviceDoorBean deviceDoorBean:deviceGroupNodes){
			 if(null==deviceDoorBean.getParentNum()&&!"".equals(deviceDoorBean.getParentNum())){
				 tree.setId(deviceDoorBean.getNum());
				 tree.setText(deviceDoorBean.getName());
				 tree.setState("open");
				 tree.setIconCls("icon-enterprise");
			    Map<String,Object> map=new HashMap<String, Object>();
			    map.put("isDevice",deviceDoorBean.getType());
			    tree.setAttributes(map);
			 }
		 } 		 
		 InitTree(tree, deviceGroupNodes);
		 return tree;
	}
	
	
	@Override
	public Tree getOnlyDevGroupTree() {
		List<DeviceGroupModel> getAllDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<Map> allDevice=deviceGroupDao.getAllDevInfo();
		List<DeviceDoorBean> deviceGroupNodes=new ArrayList<DeviceDoorBean>();
		//遍历设备组
		for(DeviceGroupModel dg:getAllDeviceGroup){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(dg.getGroupName());
			deviceDoorBean.setNum(dg.getGroupNum());
			deviceDoorBean.setParentNum(dg.getParentGroupNum());
			deviceDoorBean.setType(0);
			deviceGroupNodes.add(deviceDoorBean);
		}
		//遍历设备  父节点和标志
		for(Map device:allDevice){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName((String)device.get("device_name"));
			deviceDoorBean.setNum((String)device.get("device_num"));
			deviceDoorBean.setParentNum((String)device.get("device_group_num"));
			deviceDoorBean.setDeviceType((String)device.get("device_type"));
			deviceDoorBean.setType(1);
			deviceGroupNodes.add(deviceDoorBean);
		}		
		Tree tree=new Tree();
		for(DeviceDoorBean deviceDoorBean:deviceGroupNodes){
			if(null==deviceDoorBean.getParentNum()&&!"".equals(deviceDoorBean.getParentNum())){
				tree.setId(deviceDoorBean.getNum());
				tree.setText(deviceDoorBean.getName());
				tree.setState("open");
				tree.setIconCls("icon-enterprise");
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("isDevice",deviceDoorBean.getType());
				tree.setAttributes(map);
			}
		} 		 
		InitTree(tree, deviceGroupNodes);
		return tree;
	}
	
	@Override
	public Tree onlyGetDeviceGroupTree() {
		List<DeviceGroupModel> getAllDeviceGroup=deviceGroupDao.getAllDeviceGroup();
		List<DeviceDoorBean> deviceGroupNodes=new ArrayList<DeviceDoorBean>();
		//遍历设备组
		for(DeviceGroupModel dg:getAllDeviceGroup){
			DeviceDoorBean deviceDoorBean=new DeviceDoorBean();
			deviceDoorBean.setName(dg.getGroupName());
			deviceDoorBean.setNum(dg.getGroupNum());
			deviceDoorBean.setParentNum(dg.getParentGroupNum());
			deviceDoorBean.setType(0);
			deviceGroupNodes.add(deviceDoorBean);
		}
		 Tree tree=new Tree();
		 for(DeviceDoorBean deviceDoorBean:deviceGroupNodes){
			 if(null==deviceDoorBean.getParentNum()&&!"".equals(deviceDoorBean.getParentNum())){
				 tree.setId(deviceDoorBean.getNum());
				 tree.setText(deviceDoorBean.getName());
				 tree.setState("open");
				 tree.setIconCls("icon-enterprise");
			    Map<String,Object> map=new HashMap<String, Object>();
			    map.put("isDevice",deviceDoorBean.getType());
			    tree.setAttributes(map);
			 }
		 } 		 
		 InitTree(tree, deviceGroupNodes);
		 return tree;
	}

	void InitTree(Tree tree,List<DeviceDoorBean> deviceDoorBeanList){
	   for(DeviceDoorBean deviceDoorBean:deviceDoorBeanList){
		   if(deviceDoorBean.getParentNum()!=null&&!"".equals(deviceDoorBean.getParentNum())&&tree.getId().equals(deviceDoorBean.getParentNum())){
		    	Tree treeNode=new Tree();
		    	treeNode.setId(deviceDoorBean.getNum());
		    	Map<String,Object> map=new HashMap<String, Object>();
		    	map.put("isDevice",deviceDoorBean.getType());
		    	map.put("deviceType",deviceDoorBean.getDeviceType());
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
		    	
		    	// by yuman.gao
		    	Map attrMap = new HashMap();
		    	attrMap.put("device_type", deviceDoorBean.getDeviceType());
		    	attrMap.put("isDevice", deviceDoorBean.getType());
		    	treeNode.setAttributes(attrMap);
		    	
		    	tree.getChildren().add(treeNode);
			    InitTree(treeNode, deviceDoorBeanList);
		    }
	   }
	}
	
	void InitTreeA(Tree tree,List<DeviceDoorBean> deviceDoorBeanList){
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
				treeNode.setState("closed");
				if(deviceDoorBean.getType()==0){
					treeNode.setIconCls("icon-deviceGroup_v1");
				}else if(deviceDoorBean.getType()==1){
					treeNode.setIconCls("icon-device_v1");
				}else{
					treeNode.setIconCls("icon-door");
					treeNode.setState("open");
				}
				treeNode.setPid(tree.getId());
				tree.getChildren().add(treeNode);
				InitTreeA(treeNode, deviceDoorBeanList);
			}
		}
	}
	
	/*查询全部设备组信息*/
	@Override
	public Grid<DeviceGroupModel> searchDeviceGroup(String groupName, Integer skip, Integer rows) {
		    Grid<DeviceGroupModel> grid = new Grid<DeviceGroupModel>();
	        List<DeviceGroupModel> resultList = deviceGroupDao.searchDeviceGroup(groupName, skip, rows);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(deviceGroupDao.searchDeviceGroupCount(groupName));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}
	
	/*查询全部设备组设备信息*/
	@Override
	public Grid<DeviceInfo> searchDeviceInfo(
			String device_name, Integer skip, Integer rows) {
		Grid<DeviceInfo> grid = new Grid<DeviceInfo>();
        List<DeviceInfo> resultList = deviceGroupDao.searchDeviceInfo(device_name, skip, rows);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(deviceGroupDao.searchDeviceInfoCount(device_name));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}
	
	/*通过设备编号查询设备信息*/
	@Override
	public Grid<DeviceInfo> searchDeviceInfoByNum(String device_num,Integer skip,Integer rows) {
		Grid<DeviceInfo> grid=new Grid<DeviceInfo>();
		if(device_num==null||"".equals(device_num)){
			grid.setTotal(0);
			return grid;
		}
		List<DeviceInfo> resultList=deviceGroupDao.searchDeviceInfoByNum(device_num,skip,rows);
		grid.setTotal(deviceGroupDao.searchDeviceInfoCountByNum(device_num));
		grid.setRows(resultList);
		return grid;
	}
	
	/*通过设备编号查询设备组信息*/
	@Override
	public Grid<DeviceGroupModel> searchDeviceGroupByNum(DeviceGroupModel bgm) {
		Grid<DeviceGroupModel> grid=new Grid<DeviceGroupModel>();
		if(bgm.getGroupId()==null){
			grid.setTotal(0);
			return grid;
		}
		List<DeviceGroupModel> resultList=deviceGroupDao.searchDeviceGroupByNum(bgm);
		grid.setRows(resultList);
		if(null != resultList){
			grid.setTotal(deviceGroupDao.searchDeviceGroupCountByNum(bgm));
		}else{
			grid.setTotal(0);
		}
		
		return grid;
	}
	
	/*通过设备id查询设备组设备信息*/
	@Override
	public DeviceInfo searchDeviceInfoById(String device_id) {
		DeviceInfo  deviceInfo=deviceGroupDao.searchDeviceInfoById(device_id);
		return deviceInfo;
	}

	

	/*通过设备编号查询单个设备组信息到form表单*/
	@Override
	public DeviceGroupModel searchOnlyDeviceGroupByNum(String groupNum) {
		
		DeviceGroupModel  dgm=deviceGroupDao.searchOnlyDeviceGroupByNum(groupNum);
		
		return dgm;
	}
	
	
	/*通过设备编号查询单个设备组信息到datagrid*/
	@Override
	public Grid<DeviceGroupModel> searchOnlyDeviceGroup(DeviceGroupModel bgm) {
		Grid<DeviceGroupModel> grid=new Grid<DeviceGroupModel>();
		List<DeviceGroupModel> resultList=deviceGroupDao.searchOnlyDeviceGroup(bgm);
		grid.setRows(resultList);
		if(null != resultList){
			grid.setTotal(1);
		}else{
			grid.setTotal(0);
		}
		
		return grid;
	}
	
	
	/*增加设备组信息*/
	@Override
	public void addDeviceGroup(String groupName,
			String parentGroupNum, String createUser, String createTime,
			String description) {
		     deviceGroupDao.addDeviceGroup(groupName, parentGroupNum, createUser, createTime, description);
		     Map<String, String> log = new HashMap<String, String>();
		     log.put("V_OP_NAME", "设备组管理");
			 log.put("V_OP_FUNCTION", "新增");
			 log.put("V_OP_ID",createUser);
			 log.put("V_OP_MSG", "新增设备组");
			 log.put("V_OP_TYPE", "业务");
			 logDao.addLog(log);
	}

	

	
	/*修改设备组*/
	@Override
	public void editDeviceGroup(String groupName,
			String description, String  groupNum,String login_user) {
		boolean result = deviceGroupDao.editDeviceGroup(groupName, description, groupNum);
    	Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "设备组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID",login_user);
		log.put("V_OP_MSG", "修改设备组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
	}
   
	/*修改设备*/
	@Override
	public void editDeviceInfo(String device_name, String description,
			String device_num,String login_user) {
        deviceGroupDao.editDeviceInfo(device_name, description, device_num);		
	}
	
	/*修改设备组的父设备编号*/
	@Override
	public void changeParentGroupNum(String groupNum, String parentGroupNum) {
		deviceGroupDao.changeParentGroupNum(groupNum, parentGroupNum);
	}
	
	/*修改设备的父设备编号*/
	@Override
	public void changeDeviceGroupNum(String device_num, String device_group_num,String login_user) {
		deviceGroupDao.changeDeviceGroupNum(device_num, device_group_num);
	}
	
	/*删除设备组*/
	@Override
	public void delDeviceGroup(String groupNum,String login_user) {
		boolean r1 = true;	//xfDeviceService.selXFDeviceInGroup(groupNum, login_user);	//把该设备组下的消费设备放进未分配组
		if(r1){
			boolean result = deviceGroupDao.delDeviceGroup(groupNum);
	    	Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "设备组管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID",login_user);
			log.put("V_OP_MSG", "删除设备组");
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			logDao.addLog(log);
		}
	}

	/**
	 * 修改设备时间组
	 * @return 
	 */              //Map<String,List<DeviceTimeGroup>> dtg=new HashMap<String, List<DeviceTimeGroup>>();
	@Override         
	public boolean modifyDeviceTimeGroup(Map<String,List<DeviceTimeGroup>> deviceTimeGroupMap,String createUser) {
		boolean result=false;
		List<DeviceTimeGroup> list=null;
		for(int i=0;i<deviceTimeGroupMap.size();i++){
			list=(List<DeviceTimeGroup>)deviceTimeGroupMap.get("deviceGroup"+i);
			for(int n = 0; n < list.size(); n++){
			  result=deviceGroupDao.modifyDeviceTimeGroup(list.get(n));
			}
		}
		
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "时段组时间维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", createUser);
		log.put("V_OP_MSG", "修改设备时间组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
		
	}
	/**
     * 根据设备id查询设备、门、设备时间组相关数据
     */
	@Override
	public List<DeviceTimeGroup> getDeviceTimeGroupById(String device_num) {
		List<DeviceTimeGroup> deviceTimeGroup=deviceGroupDao.getDeviceTimeGroupById(device_num);
		return deviceTimeGroup;
	}
	
	/**
	 * 返回设备时段组之前先将库里设备时段组下发下去
	 * by gengji.yang
	 */
	public Map downLoadDeviceTimeGroupFirst(List<DeviceTimeGroup> list){
		String[] nameArr={"sunday","monday","tuesday","wedsday","thursday","friday","saturday","vocation"};
		String[] dayArr={"sunday_time","monday_time","tuesday_time","wedsday_time","thursday_time","friday_time","saturday_time","vacation_time"};
		String[] typeArr={"sunday_action_type","monday_action_type","tuesday_action_type","wedsday_action_type","thursday_action_type","friday_action_type","saturday_action_type","vacation_action_type"};
		Map map=new HashMap();
		for(int i=0;i<nameArr.length;i++){
			try {
				map.put(nameArr[i],makeList(list,dayArr[i],typeArr[i]));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				LOG.info("查看设备时段组前的下发失败了");
			} catch (SecurityException e) {
				e.printStackTrace();
				LOG.info("查看设备时段组前的下发失败了");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				LOG.info("查看设备时段组前的下发失败了");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				LOG.info("查看设备时段组前的下发失败了");
			}
		}
		return map;
	}
	
	/**
	 * 抽象,参数化，简化代码
	 * by gengji.yang
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private ArrayList<Map<String,String>> makeList(List<DeviceTimeGroup> totalList,String dayField,String typeField) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<totalList.size();i++){
			Map<String,String> map=new HashMap<String,String>();
			Field day=totalList.get(i).getClass().getDeclaredField(dayField);
			day.setAccessible(true);
			Field type=totalList.get(i).getClass().getDeclaredField(typeField);
			type.setAccessible(true);
			map.put("hour",day.get(totalList.get(i)).toString().split(":")[0]);
			map.put("minute",day.get(totalList.get(i)).toString().split(":")[1]);
			map.put("actionType",type.get(totalList.get(i)).toString());
			map.put("retain",totalList.get(i).getId().toString());
			list.add(map);
		}
		return list;
	}

	public List<DeviceGroupModel> getDeviceGroup(String flag){
		List<DeviceGroupModel> deviceGroupModel=deviceGroupDao.getDeviceGroup(flag);
		return deviceGroupModel;
	}

	@Override
	public boolean selDeviceInGroup(String device_group_num, String login_user) {
		try{
			List<String> devices = deviceGroupDao.selDeviceInGroup(device_group_num);
		//	List<String> nums = new ArrayList();
			String deviceStr = "";
			for(int i=0; i<devices.size(); i++){
				deviceStr+="'"+devices.get(i)+"',";
			}
			deviceStr = deviceStr.substring(0, deviceStr.length()-1);
	//		String deviceStr = nums.toString().substring(1, nums.toString().length()-1);
			if (!"".equals(deviceStr)) {
				changeDeviceGroupNum(deviceStr, "0", login_user);
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public boolean modifyTargetDeviceTimeGroup(List<DeviceTimeGroup> list,
			String login_user) {
		boolean result = false;
		for(DeviceTimeGroup deviceTimeGroup:list){
			result=deviceGroupDao.modifyDeviceTimeGroup(deviceTimeGroup);
		}
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "时段组时间维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "修改设备时间组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public List<Object> selNodeChild(String id, String type){
		try{
			List<String> group_list = new ArrayList<String>();
			List<String> device_list = new ArrayList<String>();
			List<String> devices = new ArrayList<String>();
			List<String> doors = new ArrayList<String>();
			List<DeviceGroupModel> allDeviceGroup=deviceGroupDao.getAllDeviceGroup();
			List<DeviceModel> allDevice=deviceGroupDao.getAllDeviceModel();
			List<DoorModel> allDoor=deviceGroupDao.getDoorList();
			//设备组，递归找组，再找设备，再找门
			if("0".equals(type)){
				group_list.add(id);
				getGroupChild(id, allDeviceGroup, group_list);
				for(String group_num : group_list){
					for(DeviceModel device:allDevice){
						if(group_num.equals(device.getDeciveGroupNum())){
							device_list.add(device.getDeviceNum());
						}
					}		
					for(String device_num : device_list){
						for(DoorModel door:allDoor){
							if(device_num.equals(door.getDeviceNum())){
								doors.add(door.getDoorNum());
								devices.add(door.getDeviceNum());
							}
						}
					}
				}
			}
			//设备，直接找门
			else {
				for(DoorModel door:allDoor){
					if(id.equals(door.getDeviceNum())){
						doors.add(door.getDoorNum());
						devices.add(door.getDeviceNum());
					}
				}
			}
			List<Object> list = new ArrayList<Object>();
			list.add(devices);
			list.add(doors);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void getGroupChild(String num, List<DeviceGroupModel> allDeviceGroup, List<String> group_list){
		for(DeviceGroupModel group:allDeviceGroup){
			if(num.equals(group.getParentGroupNum())){
				group_list.add(group.getGroupNum());
				getGroupChild(group.getGroupNum(), allDeviceGroup, group_list);
			}
		}
	}
	
}
