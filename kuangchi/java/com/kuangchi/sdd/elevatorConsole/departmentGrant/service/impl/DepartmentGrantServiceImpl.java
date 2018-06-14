package com.kuangchi.sdd.elevatorConsole.departmentGrant.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.dao.IDepartmentGrantDao;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DeviceModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.FloorGroupInfoModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;

@Service("departmentGrantServiceImpl")
public class DepartmentGrantServiceImpl implements IDepartmentGrantService {
	@Resource(name = "departmentGrantDaoImpl")
	private IDepartmentGrantDao departmentGrantDao;
	
	@Resource(name = "peopleAuthorityManagerServiceImpl")
	private PeopleAuthorityManagerService peopleAuthorityManagerService;
	

	@Resource(name = "peopleAuthorityManagerDaoImpl")
	private PeopleAuthorityManagerDao peopleAuthorityManagerDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public Grid getDepartmentGrantsByParam(String deptIds,String page, String rows) {
		List<DepartmentGrantModel> departmentGrantModelList=departmentGrantDao.getDepartmentGrantsByParam(deptIds,page,rows);
		Integer count=departmentGrantDao.getDepartmentGrantsCount(deptIds);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(departmentGrantModelList);
		return grid;
	}

	@Override
	public Grid<DeviceModel> getDevicesInfo(DeviceModel deviceModel,
			String page, String rows) {
		List<DeviceModel> deviceModelList=departmentGrantDao.getDevicesInfo(deviceModel,page,rows);
		Integer count=departmentGrantDao.getDevicesInfoCount(deviceModel);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(deviceModelList);
		return grid;
	}

	@Override
	public List<FloorGroupInfoModel> getFloorGroupInfoForSelect() {
		return departmentGrantDao.getFloorGroupInfoForSelect();
	}

	@Override
	public boolean addDeptGrant(DepartmentGrantModel departmentGrantModel,String create_user) {
		String object_num=departmentGrantModel.getObject_num();//部门编号
		String device_num=departmentGrantModel.getDevice_num();//设备编号
		String pageFloorList=departmentGrantModel.getFloor_list();
		String[] device_nums=device_num.split(",");
		String[] object_nums=object_num.split(",");
		boolean flag=true;
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		for(int i=0;i<object_nums.length;i++){
			for(int q=0;q<device_nums.length;q++){//遍历设备号
				//保存组织权限，特征：cardNum为null 开始----
				departmentGrantModel.setObject_num(object_nums[i].substring(1,object_nums[i].length()-1));
				departmentGrantModel.setDevice_num(device_nums[q]);
				List<Map> floorLists1=peopleAuthorityManagerService.getFloorListByObjectNum(object_nums[i].substring(1,object_nums[i].length()-1),device_nums[q]);
				Map m1=addFloorList3(floorLists1,pageFloorList);
				departmentGrantModel.setFloor_list(m1.get("floorStr1").toString());
				departmentGrantModel.setObject_type(m1.get("object_type").toString());
				beforeIntoTask(1,departmentGrantModel);
				//保存组织权限，特征：cardNum为null 结束----
				List<Map> cardNums=departmentGrantDao.selectCardNumByObjectNum(object_nums[i]);//通过部门号查询部门下员工绑定的卡
			if(cardNums.size()>0){
				for(int j=0;j<cardNums.size();j++){//遍历卡号、卡类型及部门号
					departmentGrantModel.setCard_num(cardNums.get(j).get("cardNum").toString());
					departmentGrantModel.setCard_type("2");
						
						List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(cardNums.get(j).get("cardNum").toString(),device_nums[q]);
						Map m=addFloorList3(floorLists,m1.get("floorStr1").toString());
						departmentGrantModel.setFloor_list(m.get("floorStr1").toString());
						departmentGrantModel.setObject_type(m.get("object_type").toString());
						
						if("0".equals(m.get("object_type").toString())){
							departmentGrantModel.setObject_num(cardNums.get(j).get("deptNum").toString());
						}else{
							departmentGrantModel.setObject_num(cardNums.get(j).get("staffNum").toString());
						}
						
						departmentGrantModel.setDevice_num(device_nums[q]);
						DeviceModel deviceModelinfo=departmentGrantDao.selectInfoByDeviceNum(device_nums[q]);
						departmentGrantModel.setDevice_ip(deviceModelinfo.getDevice_ip());
						departmentGrantModel.setAddress(deviceModelinfo.getAddress());
						departmentGrantModel.setDevice_port(deviceModelinfo.getDevice_port());
							try{
								beforeIntoTask(0,departmentGrantModel);
								/*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
								
								Map map=new HashMap();
								map.put("card_num", departmentGrantModel.getCard_num());
								map.put("device_num",departmentGrantModel.getDevice_num());
								boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
									
								/*----------------------------------------------------*/
								boolean addFlag=departmentGrantDao.addDeptGrant2(departmentGrantModel);//新增到任务表
								if(addFlag && result1){
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "新增成功");
									logDao.addLog(log);
									flag=true;
								}else{
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "新增失败");
									logDao.addLog(log);
									flag=false;
								}
							}catch(Exception e){
								e.printStackTrace();
								log.put("V_OP_TYPE", "异常");
								log.put("V_OP_MSG", "新增失败");
								logDao.addLog(log);
								flag=false;
							}
							
				}
			}
		  }
		}
		return flag;
	}
	
	
	public  boolean  addDeptTask(DepartmentGrantModel departmentGrantModel,String create_user){
		boolean flag=true;
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		try{
			beforeIntoTask(4,departmentGrantModel);
			/*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
			
			Map map=new HashMap();
			map.put("card_num", departmentGrantModel.getCard_num());
			map.put("device_num",departmentGrantModel.getDevice_num());
			boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
				
			/*----------------------------------------------------*/
			boolean addFlag=departmentGrantDao.addDeptGrant2(departmentGrantModel);//新增到任务表
			if(addFlag && result1){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "新增成功");
				logDao.addLog(log);
				flag=true;
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "新增失败");
				logDao.addLog(log);
				flag=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "新增失败");
			logDao.addLog(log);
			flag=false;
		}
		return flag;
	}
	
	
	//叠加楼层
		public Map addFloorList3(List<Map> floorLists,String newFloorList){
			String floorStr1 = "";
			String flag="0"; //权限标识  0:组织权限  1:人员权限
			Map map=new HashMap();
			List<String> list = new ArrayList<String>();
			//遍历查出来的原有的楼层，去掉重复的放进新的list中
			for(Map m:floorLists){
				//判断是否该人员在该设备是否已有人员权限
				if(null !=m.get("object_type")){
					if("1".equals(m.get("object_type").toString())){
						flag="1";
					}
				}
				String[] floors=m.get("floor_list").toString().split("\\|");
				for(String oldFloor:floors){
					 if(!list.contains(oldFloor)) {  
			                list.add(oldFloor);
			            }
				}
			}
			//遍历新增加的楼层，与去重后的原有楼层比较，去掉重复的放进list中
			String[] newFloorLists=newFloorList.split("\\|");
			for(String newFloor:newFloorLists){
				 if(!list.contains(newFloor)) {  
		                list.add(newFloor);
		            }
			}
			
			//遍历去重后的list，把楼层封装成以"|"分隔的字符串
			for(String floor:list){
				floorStr1+=floor+"|";
			}
			
			if(floorStr1.length()>0){
				floorStr1=floorStr1.substring(0, floorStr1.length()-1);
			}
			
			map.put("floorStr1", floorStr1);
			map.put("object_type", flag);
			
			
			return  map;
		}
//======================================================================================	
	/**
	 * 新增组织权限或删除组织权限时
	 * 插入任务表前
	 * 先处理权限记录表
	 * by huixian.pan
	 */
	public void beforeIntoTask(Integer flag,DepartmentGrantModel model){
		if(flag==0){
			updateAuthState2(model,"00");
		}else if(flag==1){//组织下无卡，只记录组织权限，无需下任务
			updateAuthState2(model,"01");
		}else if(flag==2){
			updateAuthState3(model,"01");
		}else{
			updateAuthState4(model,"00");
		}
	}
	
	/**
	 * 删除组织权限前
	 * by huixian.pan
	 */
	public void updateAuthState(Map m,String state){
			m.put("state", state);
			peopleAuthorityManagerService.updTkAuthRecord(m);
	}
	
	public void updateAuthStateA(Map m,String state){
		m.put("state", state);
		peopleAuthorityManagerService.updTkAuthRecordA(m);
	}
	
	/**
	 * 新增组织权限前
	 * by huixian.pan
	 */
	public void updateAuthState2(DepartmentGrantModel m,String state){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("card_type", "2");
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type",m.getObject_type());
			map.put("start_time", m.getBegin_valid_time());
			map.put("end_time", m.getEnd_valid_time());
			map.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(map);
	}
	
	/**
	 * 保存组织权限前
	 * by huixian.pan
	 */
	public void updateAuthState3(DepartmentGrantModel m,String state){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("card_type",m.getCard_type());
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type","0");
			map.put("start_time", m.getBegin_valid_time());
			map.put("end_time", m.getEnd_valid_time());
			map.put("state", state);
			map.put("id", m.getId());
			peopleAuthorityManagerService.delByID(map);
			peopleAuthorityManagerService.addTkAuthRecord(map);
	}
	
	public void updateAuthState4(DepartmentGrantModel m,String state){
		Map map=new HashMap();
		map.put("card_num", m.getCard_num());
		map.put("card_type", m.getCard_type());
		map.put("object_num", m.getObject_num());
		map.put("floor_group_num", m.getFloor_group_num());
		map.put("floor_list", m.getFloor_list());
		map.put("device_num", m.getDevice_num());
		map.put("object_type",m.getObject_type());
		map.put("start_time", m.getBegin_valid_time());
		map.put("end_time", m.getEnd_valid_time());
		map.put("state", state);
		peopleAuthorityManagerService.addTkAuthRecord(map);
	}

//======================================================================================	
	

	@Override
	public boolean checkCardInDept(String object_nums) {
		List<Map> cardNums= departmentGrantDao.selectCardNumByObjectNum("".equals(object_nums)==true?null:object_nums);
		boolean flag=false;
		int count=0;//计数器，当count==cardNums.size()时，说明所选部门中员工都没有绑定卡
		for(int i=0;i<cardNums.size();i++){
			String cardNum=cardNums.get(i).get("cardNum").toString();
			if(cardNum==null||"".equals(cardNum)){
				count+=1;
			}
		}
		if(count<cardNums.size()){
			flag=true;
		}
		return flag;
	}
	
	@Override
	public boolean checkCardAuthInDept(String object_nums) {
		return departmentGrantDao.selectCardAuthInDept(object_nums);
	}

	@Override
	public boolean removeDeptGrant(Map m,String create_user) {
		String[] objectNum=m.get("objectNum").toString().split(",");
		String[] deviceNum=m.get("deviceNum").toString().split(",");
		boolean flag=false;
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		
		for(int i=0;i<objectNum.length;i++){
			List<Map> authorityList=departmentGrantDao.selectDeptAuthorityFromAuthority(objectNum[i],deviceNum[i]);
			List<Map> list=new ArrayList();
			for(Map map:authorityList){
				/*List<Map<String,String>> floorNumlist=departmentGrantDao.selectFloorNumByFloorGroupNum(map.get("floor_group_num"));//根据楼层组编号查询楼层号
				String floor_list=""; 
				for(int m=0;m<floorNumlist.size();m++){
					floor_list=floor_list+"|"+floorNumlist.get(m).get("floorNum");//拼接楼层号
				}
				floor_list=floor_list.substring(1, floor_list.length());
				map.put("floor_list", floor_list);*/
				if(map.get("card_num")!=null){
					try{
						//如果该权限是人员权限就把要删除的组织权限从人员权限中删除，然后再下发一次
						if("1".equals(map.get("object_type"))){  
							DepartmentGrantModel deptGrantModel=new DepartmentGrantModel();
							deptGrantModel.setFloor_group_num(map.get("floor_group_num")==null?null:map.get("floor_group_num").toString());
							/*deptGrantModel.setFloor_list(mainDeptGrantList.get(n).get("floor_list").toString());*/
							deptGrantModel.setBegin_valid_time(map.get("start_time").toString());
							deptGrantModel.setEnd_valid_time(map.get("end_time").toString());
							deptGrantModel.setDevice_num(map.get("device_num").toString());
							deptGrantModel.setDevice_ip(map.get("device_ip").toString());
							deptGrantModel.setAddress(map.get("address").toString());
							//String port=map.get("device_port");
							int pp=Integer.parseInt(map.get("device_port").toString());
							deptGrantModel.setDevice_port(pp);
							deptGrantModel.setCard_num(map.get("card_num").toString());
							deptGrantModel.setCard_type(map.get("card_type").toString());
							deptGrantModel.setObject_num(map.get("object_num").toString());
							List<Map> originDeptfloorLists=peopleAuthorityManagerService.getFloorListByObjectNum(objectNum[i].substring(1, objectNum[i].length()-1),map.get("device_num").toString());
							
							List<Map> floorLists=new ArrayList();
							Map map2=new HashMap();
							map2.put("floor_list", map.get("floor_list").toString());
							map2.put("object_type", "1");
							floorLists.add(map2);
							
							Map floorMap=pingFloorList(originDeptfloorLists,floorLists,"");
							deptGrantModel.setFloor_list(floorMap.get("floor_list").toString());
							deptGrantModel.setObject_type(map.get("object_type").toString());
							beforeIntoTask(4,deptGrantModel);
                            /*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
							
							Map m2=new HashMap();
							m2.put("card_num", deptGrantModel.getCard_num());
							m2.put("device_num",deptGrantModel.getDevice_num());
							boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
								
							/*----------------------------------------------------*/
							boolean addFlag=departmentGrantDao.addDeptGrant2(deptGrantModel);
							if(addFlag && result1){
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "复制成功");
								logDao.addLog(log);
								flag=true;
							}else{
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "复制失败");
								logDao.addLog(log);
								flag=false;
							}
						}else{
							//如果该权限是组织权限，就直接把该权限删除掉
							updateAuthState(map,"10");
							boolean flag1=departmentGrantDao.removeDeptGrant(map);
							if(flag1){
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "删除成功");
								logDao.addLog(log);
								flag=true;
							}else{
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "删除失败");
								logDao.addLog(log);
								flag=false;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "删除失败");
						logDao.addLog(log);
						flag=false;
					}
				}else{//组织下无卡，直接删除组织权限
					map.put("objectNum",map.get("object_num"));
					map.put("deviceNum",map.get("device_num"));
					list.add(map);
				}
		
			}
			for(Map m2:list){
				departmentGrantDao.delTkAuthDirect(m2);
				flag=true;
			}
			
			
		}
		return flag;
	}

	@Override
	public boolean copyDeptGrant(String main_deptnums, String target_deptnums,String create_user) {
		String[] arr=main_deptnums.split("\\|");//获取权限的部门编号和设备编号
		String[] mainDeptNums=arr[0].split(",");//获取权限的部门
		String[] mainDeviceNums=arr[1].split(",");//获取权限的部门
		String[] targetDeptNums=target_deptnums.split(",");//目标部门
		boolean flag=false;
		DepartmentGrantModel deptGrantModel=new DepartmentGrantModel();
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "复制");
		log.put("V_OP_ID", create_user);
		for(int i=0;i<mainDeptNums.length;i++){//遍历源部门
			//根据获取权限部门号mainDeptNums去查权限
			List<Map> mainDeptGrantList=departmentGrantDao.selectGrantByDeptNum(mainDeptNums[i],mainDeviceNums[i]);
			for(int j=0;j<targetDeptNums.length;j++){//遍历目标部门
				//保存组织权限，特征：cardNum为null 开始----
				/*for(int n=0;n<mainDeptGrantList.size();n++){//遍历权限
					try{
					String floor_group_num=mainDeptGrantList.get(n).get("floor_group_num")==null?null:mainDeptGrantList.get(n).get("floor_group_num").toString();
					deptGrantModel.setFloor_group_num(floor_group_num);
					deptGrantModel.setFloor_list(mainDeptGrantList.get(n).get("floor_list").toString());
					deptGrantModel.setBegin_valid_time(mainDeptGrantList.get(n).get("begin_valid_time").toString());
					deptGrantModel.setEnd_valid_time(mainDeptGrantList.get(n).get("end_valid_time").toString());
					deptGrantModel.setDevice_num(mainDeptGrantList.get(n).get("device_num").toString());
					deptGrantModel.setDevice_ip(mainDeptGrantList.get(n).get("device_ip").toString());
					deptGrantModel.setAddress(mainDeptGrantList.get(n).get("address").toString());
					String port=mainDeptGrantList.get(n).get("device_port").toString();
					int pp=Integer.valueOf(port);
					deptGrantModel.setDevice_port(pp);
					
					deptGrantModel.setObject_num(targetDeptNums[i].substring(1,targetDeptNums[i].length()-1));
					List<Map> floorLists=peopleAuthorityManagerService.getFloorListByObjectNum(deptGrantModel.getObject_num(),deptGrantModel.getDevice_num());
					Map map=addFloorList3(floorLists,mainDeptGrantList.get(n).get("floor_list").toString());
					deptGrantModel.setFloor_list(map.get("floorStr1").toString());
					deptGrantModel.setObject_type(map.get("object_type").toString());
					beforeIntoTask(1,deptGrantModel);
					flag=true;
					}catch(Exception e){
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "复制失败");
						logDao.addLog(log);
						flag=false;
					}
				}*/
				
				//保存组织权限，特征：cardNum为null 结束----
				
				
				
				List<Map> cardInfos=departmentGrantDao.selectCardNumByObjectNum(targetDeptNums[j]);//目标部门下的卡信息
				if(cardInfos.size()>0){//目标部门下的卡数目大于0
					for(int m=0;m<cardInfos.size();m++){
						for(int n=0;n<mainDeptGrantList.size();n++){//遍历权限
							//保存组织权限，特征：cardNum为null 开始----
						try{
							String floor_group_num1=mainDeptGrantList.get(n).get("floor_group_num")==null?null:mainDeptGrantList.get(n).get("floor_group_num").toString();
							deptGrantModel.setFloor_group_num(floor_group_num1);
							/*deptGrantModel.setFloor_list(mainDeptGrantList.get(n).get("floor_list").toString());*/
							deptGrantModel.setBegin_valid_time(mainDeptGrantList.get(n).get("begin_valid_time").toString());
							deptGrantModel.setEnd_valid_time(mainDeptGrantList.get(n).get("end_valid_time").toString());
							deptGrantModel.setDevice_num(mainDeptGrantList.get(n).get("device_num").toString());
							deptGrantModel.setDevice_ip(mainDeptGrantList.get(n).get("device_ip").toString());
							deptGrantModel.setAddress(mainDeptGrantList.get(n).get("address").toString());
							String port1=mainDeptGrantList.get(n).get("device_port").toString();
							int pp1=Integer.valueOf(port1);
							deptGrantModel.setDevice_port(pp1);
							
							deptGrantModel.setObject_num(targetDeptNums[i].substring(1,targetDeptNums[i].length()-1));
							List<Map> floorList1=peopleAuthorityManagerService.getFloorListByObjectNum(deptGrantModel.getObject_num(),deptGrantModel.getDevice_num());
							Map m2=addFloorList3(floorList1,mainDeptGrantList.get(n).get("floor_list").toString());
							deptGrantModel.setFloor_list(m2.get("floorStr1").toString());
							deptGrantModel.setObject_type(m2.get("object_type").toString());
							beforeIntoTask(1,deptGrantModel);
							flag=true;
							
							
							//保存组织权限，特征：cardNum为null 结束----
							
							
							deptGrantModel.setCard_num(cardInfos.get(m).get("cardNum").toString());
							/*deptGrantModel.setCard_type(cardInfos.get(m).get("cardTypeNum").toString());*/
							deptGrantModel.setCard_type("2");
							List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(cardInfos.get(m).get("cardNum").toString(),mainDeptGrantList.get(n).get("device_num").toString());
							Map map2=addFloorList3(floorLists,m2.get("floorStr1").toString());
							String floor_group_num=mainDeptGrantList.get(n).get("floor_group_num")==null?null:mainDeptGrantList.get(n).get("floor_group_num").toString();
							deptGrantModel.setFloor_group_num(floor_group_num);
							
							if("0".equals(map2.get("object_type").toString())){
								deptGrantModel.setObject_num(cardInfos.get(m).get("deptNum").toString());
							}else{
								deptGrantModel.setObject_num(cardInfos.get(m).get("staffNum").toString());
							}
							
							/*List<Map<String,String>> floorNumlist=departmentGrantDao.selectFloorNumByFloorGroupNum(floor_group_num);//根据楼层组编号查询楼层号
							String floor_list=""; 
							for(int q=0;q<floorNumlist.size();q++){
								floor_list=floor_list+"|"+floorNumlist.get(q).get("floorNum");//拼接楼层号
							}
							floor_list=floor_list.substring(1, floor_list.length());*/
							//deptGrantModel.setFloor_list(mainDeptGrantList.get(n).get("floor_list").toString());
							deptGrantModel.setFloor_list(map2.get("floorStr1").toString());
							deptGrantModel.setObject_type(map2.get("object_type").toString());
							/*deptGrantModel.setFloor_list(mainDeptGrantList.get(n).get("floor_list").toString());*/
							deptGrantModel.setBegin_valid_time(mainDeptGrantList.get(n).get("begin_valid_time").toString());
							deptGrantModel.setEnd_valid_time(mainDeptGrantList.get(n).get("end_valid_time").toString());
							deptGrantModel.setDevice_num(mainDeptGrantList.get(n).get("device_num").toString());
							deptGrantModel.setDevice_ip(mainDeptGrantList.get(n).get("device_ip").toString());
							deptGrantModel.setAddress(mainDeptGrantList.get(n).get("address").toString());
							String port=mainDeptGrantList.get(n).get("device_port").toString();
							int pp=Integer.valueOf(port);
							deptGrantModel.setDevice_port(pp);
								//boolean addAuthorityFlag=departmentGrantDao.addToAuthorityTable(deptGrantModel);//新增到权限表
								beforeIntoTask(0,deptGrantModel);
	                            /*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
								
								Map map=new HashMap();
								map.put("card_num", deptGrantModel.getCard_num());
								map.put("device_num",deptGrantModel.getDevice_num());
								boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
									
								/*----------------------------------------------------*/
								boolean addFlag=departmentGrantDao.addDeptGrant2(deptGrantModel);
								//if(addAuthorityFlag&&addFlag){
								if(addFlag && result1){
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "复制成功");
									logDao.addLog(log);
									flag=true;
								}else{
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "复制失败");
									logDao.addLog(log);
									flag=false;
								}
							}catch(Exception e){
								e.printStackTrace();
								log.put("V_OP_TYPE", "异常");
								log.put("V_OP_MSG", "复制失败");
								logDao.addLog(log);
								flag=false;
							}
						}
					}
				}/*else{//目标部门下没有卡
					for(int n=0;n<mainDeptGrantList.size();n++){//遍历权限
						String floor_group_num=mainDeptGrantList.get(n).get("floor_group_num")==null?null:mainDeptGrantList.get(n).get("floor_group_num").toString();
						deptGrantModel.setFloor_group_num(floor_group_num);*/
			/*			deptGrantModel.setBegin_valid_time(mainDeptGrantList.get(n).get("begin_valid_time").toString());
						deptGrantModel.setEnd_valid_time(mainDeptGrantList.get(n).get("end_valid_time").toString());
						deptGrantModel.setDevice_num(mainDeptGrantList.get(n).get("device_num").toString());
						deptGrantModel.setDevice_ip(mainDeptGrantList.get(n).get("device_ip").toString());
						deptGrantModel.setAddress(mainDeptGrantList.get(n).get("address").toString());
						String port=mainDeptGrantList.get(n).get("device_port").toString();
						int pp=Integer.valueOf(port);
						deptGrantModel.setDevice_port(pp);
						try{
							deptGrantModel.setObject_num(targetDeptNums[i].substring(1,targetDeptNums[i].length()-1));
							List<Map> floorLists=peopleAuthorityManagerService.getFloorListByObjectNum(deptGrantModel.getObject_num(),deptGrantModel.getDevice_num());
							Map map=addFloorList3(floorLists,mainDeptGrantList.get(n).get("floor_list").toString());
							deptGrantModel.setFloor_list(map.get("floorStr1").toString());
							deptGrantModel.setObject_type(map.get("object_type").toString());
							beforeIntoTask(1,deptGrantModel);
							flag=true;
						}catch(Exception e){
							e.printStackTrace();
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "复制失败");
							logDao.addLog(log);
							flag=false;
						}
					}
				}*/
				
		
			}
		}
		
		return flag;
	}

	@Override
	public Grid selectDeptAuthorityInfo(Map map, String page, String rows) {
		int Page = Integer.valueOf(page);
		int Rows = Integer.valueOf(rows);
		map.put("page", (Page-1)*Rows);
		map.put("rows", Rows);
		
		Grid grid=new Grid();
		grid.setRows(departmentGrantDao.selectDeptAuthorityInfoList(map));
		grid.setTotal(departmentGrantDao.selectDeptAuthorityInfoCount(map));
		return grid;
	}

	@Override
	public Grid selectStaffAuthorityInfo(Map map, String page, String rows) {
		int Page = Integer.valueOf(page);
		int Rows = Integer.valueOf(rows);
		map.put("page", (Page-1)*Rows);
		map.put("rows", Rows);
		
		Grid grid=new Grid();
		grid.setRows(departmentGrantDao.selectStaffAuthorityInfoList(map));
		grid.setTotal(departmentGrantDao.selectStaffAuthorityInfoCount(map));
		return grid;
	}

	@Override
	public Grid selectDeptAuthorityForView(Map map, String page, String rows) {
		int Page = Integer.valueOf(page);
		int Rows = Integer.valueOf(rows);
		map.put("page", (Page-1)*Rows);
		map.put("rows", Rows);
		
		Grid grid=new Grid();
		List<Map> list=departmentGrantDao.selectDeptAuthorityForViewList(map);
		grid.setRows(list);
		grid.setTotal(departmentGrantDao.selectDeptAuthorityForViewCount(map));
		return grid;
	}

	@Override
	public Grid selectStaffAuthorityForView(Map map, String page, String rows) {
		int Page = Integer.valueOf(page);
		int Rows = Integer.valueOf(rows);
		map.put("page", (Page-1)*Rows);
		map.put("rows", Rows);
		
		Grid grid=new Grid();
		List<Map> list=departmentGrantDao.selectStaffAuthorityForViewList(map);
		grid.setRows(list);
		grid.setTotal(departmentGrantDao.selectStaffAuthorityForViewCount(map));
		return grid;
	}

	@Override
	public List<Map> searchStaffAuthority(Map map) {
		return departmentGrantDao.searchStaffAuthority(map);
	}

	@Override
	public List<Map> searchDeptAuthority(Map map) {
		return departmentGrantDao.searchDeptAuthority(map);
	}

	@Override
	public Map selectDeptAuthByDeptNo(String deptno) {
		return departmentGrantDao.selectDeptAuthFromAuthority(deptno);
	}

	@Override
	public String selectCardTypeByNum(String cardNum) {
		return departmentGrantDao.selectCardTypeByNum(cardNum);
	}

	@Override
	public boolean addDeptAuth(DepartmentGrantModel departmentGrantModel) {
		beforeIntoTask(0,departmentGrantModel);
		return departmentGrantDao.addDeptGrant(departmentGrantModel);
	}

	@Override
	public boolean removeDeptAuth(Map<String, String> map) {
		updateAuthStateA(map,"10");
		return departmentGrantDao.removeDeptGrant(map);
	}

	@Override
	public String selectBmdmByStaffNum(String staffNum) {
		return departmentGrantDao.selectBmdmByStaffNum(staffNum);
	}

	@Override
	public List<Map> selectCardNumByObjectNum(String object_num) {
		return departmentGrantDao.selectCardNumByObjectNum(object_num);
	}
	
	//保存权限前，把修改前的组织权限楼层信息移除，把修改后的组织权限楼层信息与该组织下的人员的人员权限楼层信息追加
	public Map pingFloorList(List<Map> originDeptfloorLists,List<Map> floorList,String newFloorList){
		String floorStr1="";
		String flag="0";  // 0:部门权限  1:人员权限
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		
		//把修改前的组织权限遍历放进一个list<String>中
		for(Map m1:originDeptfloorLists){
			String[] originfloors=m1.get("floor_list").toString().split("\\|");
			for(String oldFloor:originfloors){
				 if(!list2.contains(oldFloor)) {  
		                list2.add(oldFloor);
		            }
			}
		}
		
		//当权限类型为人员权限时，遍历获取该人员楼层信息
		for(Map m:floorList){
			if("1".equals(m.get("object_type"))){
				flag="1";
				String[] floors=m.get("floor_list").toString().split("\\|");
				for(String oldFloor:floors){
					 if(!list.contains(oldFloor)) {  
			                list.add(oldFloor);
			            }
				}
			}
		}
		
		//把人员权限中含有修改前的组织权限楼层信息移除
		list.removeAll(list2);
		
		//遍历新增加的楼层，与去重后的原有楼层比较，去掉重复的放进list中
		if(!"".equals(newFloorList)){
			String[] newFloorLists=newFloorList.split("\\|");
			for(String newFloor:newFloorLists){
				 if(!list.contains(newFloor)) {  
		                list.add(newFloor);
		            }
			}
		}
		//遍历去重后的list，把楼层封装成以"|"分隔的字符串
		for(String floor:list){
			floorStr1+=floor+"|";
		}
		
		if(floorStr1.length()>0){
			floorStr1=floorStr1.substring(0, floorStr1.length()-1);
		}
		
		Map map=new HashMap();
		map.put("object_type", flag);
		map.put("floor_list", floorStr1);
		
		return map;
	}
	
	

	@Override
	public void saveDeptAuth(List<Map> list,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		for(int i=0;i<list.size();i++){
			DepartmentGrantModel departmentGrantModel=new DepartmentGrantModel();
			
			//修改前的组织权限的楼层信息
			List<Map> originDeptfloorLists=peopleAuthorityManagerService.getFloorListByObjectNum(list.get(i).get("object_num").toString(),list.get(i).get("device_num").toString());
			
			//保存组织权限，特征：cardNum为null 开始----
			departmentGrantModel.setBegin_valid_time(list.get(i).get("begin_valid_time").toString());
			departmentGrantModel.setEnd_valid_time(list.get(i).get("end_valid_time").toString());
			departmentGrantModel.setFloor_list(list.get(i).get("floor_list").toString());
			departmentGrantModel.setCard_type(list.get(i).get("card_type").toString());
			departmentGrantModel.setId(list.get(i).get("id").toString());
			
			departmentGrantModel.setDevice_num(list.get(i).get("device_num").toString());
			departmentGrantModel.setDevice_ip(list.get(i).get("device_ip").toString());
			departmentGrantModel.setAddress(list.get(i).get("address").toString());
			Integer port=(int) Math.pow((Double) list.get(i).get("device_port"), 1);
			departmentGrantModel.setDevice_port(port);
			
			departmentGrantModel.setObject_num(list.get(i).get("object_num").toString());
			beforeIntoTask(2,departmentGrantModel);
			//保存组织权限，特征：cardNum为null 结束----
			List<Map> cardNums=departmentGrantDao.selectCardNumByObjectNum("\'"+list.get(i).get("object_num").toString()+"\'");//通过部门号查询部门下员工绑定的卡
			if(cardNums.size()>0){
				for(int j=0;j<cardNums.size();j++){//遍历卡号、卡类型及部门号
					departmentGrantModel.setCard_num(cardNums.get(j).get("cardNum").toString());
					
					//获取当前卡的楼层信息
					List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(cardNums.get(j).get("cardNum").toString(),list.get(i).get("device_num").toString());
					Map floorMap=pingFloorList(originDeptfloorLists,floorLists,list.get(i).get("floor_list").toString());
					departmentGrantModel.setFloor_list(floorMap.get("floor_list").toString());
					departmentGrantModel.setObject_type(floorMap.get("object_type").toString());
					//若object_type=1时,此时该权限应改为人员权限，所有Object_num应存staffNum
					if("1".equals(floorMap.get("object_type").toString())){
						departmentGrantModel.setObject_num(cardNums.get(j).get("staffNum").toString());
					}else{
						departmentGrantModel.setObject_num(cardNums.get(j).get("deptNum").toString());
					}
					/*departmentGrantModel.setCard_type(cardNums.get(j).get("cardTypeNum").toString());*/
					/*departmentGrantModel.setCard_type(list.get(i).get("card_type").toString());
					
					departmentGrantModel.setBegin_valid_time(list.get(i).get("begin_valid_time").toString());
					departmentGrantModel.setEnd_valid_time(list.get(i).get("end_valid_time").toString());
					departmentGrantModel.setFloor_list(list.get(i).get("floor_list").toString());
					
					departmentGrantModel.setDevice_num(list.get(i).get("device_num").toString());
					departmentGrantModel.setDevice_ip(list.get(i).get("device_ip").toString());
					departmentGrantModel.setAddress(list.get(i).get("address").toString());
					Integer port=(int) Math.pow((Double) list.get(i).get("device_port"), 1);
					departmentGrantModel.setDevice_port(port);*/
					
							try{
								beforeIntoTask(4,departmentGrantModel);
								boolean addFlag=departmentGrantDao.addDeptGrant2(departmentGrantModel);//新增到任务表
								if(addFlag){
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "新增成功");
									logDao.addLog(log);
								}else{
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "新增失败");
									logDao.addLog(log);
								}
							}catch(Exception e){
								e.printStackTrace();
								log.put("V_OP_TYPE", "异常");
								log.put("V_OP_MSG", "新增失败");
								logDao.addLog(log);
							}
				}
			}/*else{
				departmentGrantModel.setBegin_valid_time(list.get(i).get("begin_valid_time").toString());
				departmentGrantModel.setEnd_valid_time(list.get(i).get("end_valid_time").toString());
				departmentGrantModel.setFloor_list(list.get(i).get("floor_list").toString());
				departmentGrantModel.setCard_type(list.get(i).get("card_type").toString());
				departmentGrantModel.setId(list.get(i).get("id").toString());
				departmentGrantModel.setDevice_num(list.get(i).get("device_num").toString());
				departmentGrantModel.setDevice_ip(list.get(i).get("device_ip").toString());
				departmentGrantModel.setAddress(list.get(i).get("address").toString());
				Integer port=(int) Math.pow((Double) list.get(i).get("device_port"), 1);
				departmentGrantModel.setDevice_port(port);
				departmentGrantModel.setObject_num(list.get(i).get("object_num").toString());
				beforeIntoTask(2,departmentGrantModel);
			}*/

		}
	}

	@Override
	public boolean addDeptGrantA(DepartmentGrantModel departmentGrantModel,
			String create_user) {
		String object_num=departmentGrantModel.getObject_num();//部门编号
		String device_num=departmentGrantModel.getDevice_num();//设备编号
		//String floor_group_num=departmentGrantModel.getFloor_group_num();//楼层组编号
		String[] device_nums=device_num.split(",");
		String[] object_nums=object_num.split(",");
		
		String pageFloorList=departmentGrantModel.getFloor_list();
		
		/*List<Map<String,String>> floorNumlist=departmentGrantDao.selectFloorNumByFloorGroupNum(floor_group_num);//根据楼层组编号查询楼层号
		String floor_list=""; 
		for(int m=0;m<floorNumlist.size();m++){
			floor_list=floor_list+"|"+floorNumlist.get(m).get("floorNum");//拼接楼层号
		}
		floor_list=floor_list.substring(1, floor_list.length());*/
		boolean flag=false;
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "梯控组织授权");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		for(int i=0;i<object_nums.length;i++){
			List<Map> cardNums=departmentGrantDao.selectCardNumByObjectNum("\'"+object_nums[i]+"\'");//通过部门号查询部门下员工绑定的卡
			for(int j=0;j<cardNums.size();j++){//遍历卡号、卡类型及部门号
				departmentGrantModel.setCard_num(cardNums.get(j).get("cardNum").toString());
				departmentGrantModel.setObject_num(cardNums.get(j).get("deptNum").toString());
				/*departmentGrantModel.setCard_type(cardNums.get(j).get("cardTypeNum").toString());*/
				
				departmentGrantModel.setCard_type(departmentGrantModel.getCard_type());
				
				for(int q=0;q<device_nums.length;q++){//遍历设备号
					departmentGrantModel.setDevice_num(device_nums[q]);
					DeviceModel deviceModelinfo=departmentGrantDao.selectInfoByDeviceNum(device_nums[q]);
					departmentGrantModel.setDevice_ip(deviceModelinfo.getDevice_ip());
					departmentGrantModel.setAddress(deviceModelinfo.getAddress());
					departmentGrantModel.setDevice_port(deviceModelinfo.getDevice_port());
						//departmentGrantDao.delDeptGrant(departmentGrantModel);//真删除原记录
						//departmentGrantModel.setFloor_list(floor_list);
						try{
							//boolean addAuthorityFlag=departmentGrantDao.addToAuthorityTable(departmentGrantModel);//新增到权限表
							List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(departmentGrantModel.getCard_num(), device_nums[q]);
							Map map=addFloorList3(floorLists,pageFloorList);
							departmentGrantModel.setFloor_list(map.get("floorStr1").toString());
							departmentGrantModel.setObject_type(map.get("object_type").toString());
							
							beforeIntoTask(0,departmentGrantModel);
						    /*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
							
							Map map2=new HashMap();
							map2.put("card_num", departmentGrantModel.getCard_num());
							map2.put("device_num",device_nums[q]);
							boolean result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map);
								
							/*----------------------------------------------------*/
							boolean addFlag=departmentGrantDao.addDeptGrant(departmentGrantModel);//新增到任务表
							//if(addAuthorityFlag&&addFlag){
							if(addFlag){
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "新增成功");
								logDao.addLog(log);
								flag=true;
							}else{
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "新增失败");
								logDao.addLog(log);
								flag=false;
							}
						}catch(Exception e){
							e.printStackTrace();
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "新增失败");
							logDao.addLog(log);
							flag=false;
						}
						
				}
			}
		}
		
		return flag;
	}

}
