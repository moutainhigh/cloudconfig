package com.kuangchi.sdd.baseConsole.deviceGroup.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.baseConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.timesgroup.service.TimesGroupService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.file.DownloadFile;

@Scope("prototype")
@Controller("deviceGroupAction")
public class DeviceGroupAction extends BaseActionSupport {
	
	public static final Logger LOG = Logger.getLogger(DeviceGroupAction.class);

	@Override
	public Object getModel() {
		return null;
	}
	
	@Resource(name="deviceGroupService")
	DeviceGroupService deviceGroupService;
	
	@Resource(name = "timesGroupServiceImp")
	transient private TimesGroupService timesGroupService;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	/**
	 * 获取设备组-->设备-->门 的树
	 */
	public void deviceGroup(){
		HttpServletRequest request = getHttpServletRequest();
		Tree deviceGroupTree=deviceGroupService.getDeviceDoorTree();
		printTree(deviceGroupTree);
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(deviceGroupTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	
	public void lazyDeviceDoorTree(){
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("id");//GlobalConstant
		String flag=request.getParameter("flag");//GlobalConstant
		if(null==id||"null".equals(id)){
			Tree deviceGroupTree=deviceGroupService.getDeviceDoorTreeLazy(GlobalConstant.DEVICE_ROOT);
			printTree(deviceGroupTree);
			StringBuilder builder=new StringBuilder();
			builder.append("[");
			builder.append(new Gson().toJson(deviceGroupTree));
			builder.append("]");
			printHttpServletResponse(builder.toString());
		}else{
		  	List<Tree>	treeList=null;
            //否则从id为传下来的id 的结点开始取
        	if(flag!=null&&"0".equals(flag)){//设备组-->设备组，设备getDeviceDoorTreeLazyList
	        	treeList = deviceGroupService.getDeviceDoorTreeLazyList(id);
        	}else{//设备-->门
        		treeList = deviceGroupService.getDeviceDoorTreeLazyListA(id);
        	}
        	printHttpServletResponse(new Gson().toJson(treeList));
		}
	}
	
	/**
	 * 获取设备组和设备的树
	 */
	public void getOnlyDeviceGroupTree(){
		Tree onlyDeviceGroupTree=deviceGroupService.getOnlyDeviceGroupTree();
		printTree(onlyDeviceGroupTree);
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(onlyDeviceGroupTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	/**
	 * 获取设备组和设备的树（包含设备类型信息）
	 */
	public void getOnlyDevGroupTree(){
		Tree onlyDeviceGroupTree=deviceGroupService.getOnlyDevGroupTree();
		printTree(onlyDeviceGroupTree);
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(onlyDeviceGroupTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	
	/**
	 * 获取设备组的树
	 */
	public void onlyGetDeviceGroupTree(){
		Tree onlyDeviceGroupTree=deviceGroupService.onlyGetDeviceGroupTree();
		printTree(onlyDeviceGroupTree);
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(onlyDeviceGroupTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	
	public void printTree(Tree tree){
		LOG.info(tree.getId()+"=="+tree.getText());
		List<Tree> childrenList=tree.getChildren();
		for(Tree treeNode:childrenList){
//			System.out.println(tree.getId()+":"+tree.getText()+" 的子节点是"+treeNode.getId()+":"+treeNode.getText());
			printTree(treeNode);
		}
		
		
	}
	
	/*获取所有设备组信息*/
	public void getAllDeviceGroup(){
		Grid<DeviceGroupModel> grid=null;
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		DeviceGroupModel bean=GsonUtil.toBean(data, DeviceGroupModel.class);//将json字符串转化为bean
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		grid=deviceGroupService.searchDeviceGroup(bean.getGroupName(), skip, rows);
	    printHttpServletResponse(GsonUtil.toJson(grid));
		
	} 
	
	/*获取所有设备组设备信息*/
	public void getAllDeviceInfo(){
		Grid<DeviceInfo> grid=null;
		HttpServletRequest request=getHttpServletRequest();
 		String data=request.getParameter("data");
		DeviceInfo bean=GsonUtil.toBean(data, DeviceInfo.class);//将json字符串转化为bean
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		grid=deviceGroupService.searchDeviceInfo(bean.getDevice_name(), skip, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	} 
	
	/*通过设备id查询设备组设备信息到form表单*/
	public void  getDeviceInfoById(){
		HttpServletRequest request=getHttpServletRequest();
		String device_id=request.getParameter("device_id");
		DeviceInfo deviceInfo=null;   
		if(!"".equals(device_id)){
			deviceInfo=deviceGroupService.searchDeviceInfoById(device_id);
		}
		//System.out.println(GsonUtil.toJson(deviceInfo));
		printHttpServletResponse(GsonUtil.toJson(deviceInfo));
	}
	
	
	/*通过设备编号查询设备信息*/
	public void getDeviceInfoByNum(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		DeviceInfo deviceInfo=GsonUtil.toBean(data, DeviceInfo.class);
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
	    Grid<DeviceInfo> grid=deviceGroupService.searchDeviceInfoByNum(deviceInfo.getDevice_num(),skip,rows);
	    printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/*通过设备编号查询设备组信息*/
	public void getDeviceGroupByNum(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		DeviceGroupModel bgm=GsonUtil.toBean(data, DeviceGroupModel.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		bgm.setPage(Integer.parseInt(page));
		bgm.setRows(Integer.parseInt(rows));
		Grid<DeviceGroupModel> grid=deviceGroupService.searchDeviceGroupByNum(bgm);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/*通过设备编号查询单个设备组到form表单*/
	public void  getOneDeviceGroupByNum(){
	HttpServletRequest request=getHttpServletRequest();
    String groupNum=request.getParameter("groupNum");
    DeviceGroupModel bgm=null;   
    if(!"".equals(groupNum)){
	      bgm=deviceGroupService.searchOnlyDeviceGroupByNum(groupNum);
	   }
       LOG.info(GsonUtil.toJson(bgm));
	   printHttpServletResponse(GsonUtil.toJson(bgm));
	}
	
	/*通过设备编号查询单个设备组到datagrid*/
	public void getOneDeviceGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		DeviceGroupModel  bgm=GsonUtil.toBean(data, DeviceGroupModel.class);
		Grid<DeviceGroupModel>  grid=null;
		if(!"".equals(bgm.getGroupNum())){
		       grid=deviceGroupService.searchOnlyDeviceGroup(bgm);	
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/*增加设备组信息*/
	public void addDeviceGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String groupName=request.getParameter("groupName");
		String  description=request.getParameter("description");
		String   parentGroupNum=request.getParameter("parentGroupNum");
		
		//新增时间  录入人员
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
		String totaltime=getSysTimestamp().toString();
        String systime=totaltime.substring(0, 19);
       
        JsonResult result = new JsonResult();
        result.setSuccess(true);

        try {
        	deviceGroupService.addDeviceGroup(groupName,parentGroupNum,loginUser.getYhMc(), systime, description);
        } catch (Exception e) {
            result.setSuccess(false);
            LOG.error("add new DeviceGroup error", e);
        }

        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/*修改设备组信息*/
	public void editDeviceGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String groupName=request.getParameter("groupName");
		String  description=request.getParameter("description");
		String   groupNum=request.getParameter("groupNum");
		JsonResult result=new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		result.setSuccess(true);
		try {
			deviceGroupService.editDeviceGroup(groupName,description, groupNum,login_user);
		} catch (Exception e) {
            result.setSuccess(false);
            LOG.error("edit  DeviceGroup error", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/*修改设备信息*/
	public void editDeviceInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String device_name=request.getParameter("device_name");
		String  description=request.getParameter("description");
		String   device_num=request.getParameter("device_num");
		JsonResult result=new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		result.setSuccess(true);
		try {
			deviceGroupService.editDeviceInfo(device_name, description, device_num,login_user);
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("edit  DeviceGroup error", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/*修改设备组父设备编号*/
	public void changeParentGroupNum(){
		HttpServletRequest  request=getHttpServletRequest();
 		String flag=request.getParameter("flag");
		String groupNum=request.getParameter("groupNum");
		String parentGroupNum=request.getParameter("parentGroupNum");
		JsonResult result=new JsonResult();
		result.setSuccess(true);
		try {
			if(!"".equals(groupNum)){
			if("0".equals(flag)){
				//parentGroupNum="0";
				deviceGroupService.changeParentGroupNum(groupNum, parentGroupNum);
			}else{
				groupNum="'"+groupNum+"'";
				deviceGroupService.changeParentGroupNum(groupNum, parentGroupNum);
			}
			}
		} catch (Exception e) {
              result.setSuccess(false);
              LOG.error("edit  DeviceGroup error", e);

		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/*修改设备的父设备编号*/
	public void changeDeviceGroupNum(){
		HttpServletRequest  request=getHttpServletRequest();
		String flag=request.getParameter("flag");
		String device_num=request.getParameter("device_num");
		String device_group_num=request.getParameter("device_group_num");
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		JsonResult result=new JsonResult();
		result.setSuccess(true);
		try {
			if(!"".equals(device_num)){
				if("0".equals(flag)){
					//device_group_num="0";
					deviceGroupService.changeDeviceGroupNum(device_num, device_group_num,login_user);
				}else{
					device_num="'"+device_num+"'";
					deviceGroupService.changeDeviceGroupNum(device_num, device_group_num,login_user);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			LOG.error("edit  DeviceGroup error", e);
			
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/*删除设备组信息*/
	public  void  delDeviceGroup(){
		HttpServletRequest  request=getHttpServletRequest();
		String groupNum=request.getParameter("groupNum");
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		JsonResult  result=new  JsonResult();
		result.setSuccess(true);
		
		try {
		   deviceGroupService.delDeviceGroup(groupNum,login_user);	
		} catch (Exception e) {
			e.printStackTrace();
          result.setSuccess(false);
          LOG.error("delete  DeviceGroup  error", e);
		}
	    printHttpServletResponse(GsonUtil.toJson(result));
	    
	}
	
	/*设置设备时间组*/
	public void setDeviceTimeGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String device=request.getParameter("device");
		String sign=request.getParameter("device_type");
		String device_mac=request.getParameter("device_mac");
		
		String deviceNum=mjCommService.getTkDevNumByMac(device_mac);
		
		Integer device_type=Integer.parseInt(sign);
		
		Gson gson=new Gson();
		JsonResult  result= new JsonResult();
		result.setSuccess(true);
		List<LinkedTreeMap> list=gson.fromJson(device,new ArrayList<LinkedTreeMap>().getClass());
		Map<String, List<DeviceTimeGroup>>  deviceTimeGroupMap=new  LinkedTreeMap<String,List<DeviceTimeGroup>>();  
		//获取数据到接口
		ArrayList  sunday=new  ArrayList();
		ArrayList  monday=new  ArrayList();
		ArrayList  tuesday=new  ArrayList();
		ArrayList  wedsday=new  ArrayList();
		ArrayList  thursday=new  ArrayList();
		ArrayList  friday=new  ArrayList();
		ArrayList  saturday=new  ArrayList();
		ArrayList  vocation=new  ArrayList();
		
		
		LinkedTreeMap<String,ArrayList>  commonMap=new LinkedTreeMap<String,ArrayList>();
		for(int i=0;i<list.size();i++){
			ArrayList  array1=(ArrayList) list.get(i).get("sundayTime");
			ArrayList  array2=(ArrayList) list.get(i).get("mondayTime");
			ArrayList  array3=(ArrayList) list.get(i).get("tuesdayTime");
			ArrayList  array4=(ArrayList) list.get(i).get("wedsdayTime");
			ArrayList  array5=(ArrayList) list.get(i).get("thursdayTime");
			ArrayList  array6=(ArrayList) list.get(i).get("fridayTime");
			ArrayList  array7=(ArrayList) list.get(i).get("saturdayTime");
			ArrayList  array8=(ArrayList) list.get(i).get("vocationTime");
			for(int j=0;j<array1.size();j++){
				
				sunday.add(array1.get(j));
				monday.add(array2.get(j));
				tuesday.add(array3.get(j));
				wedsday.add(array4.get(j));
				thursday.add(array5.get(j));
				friday.add(array6.get(j));
				saturday.add(array7.get(j));
				vocation.add(array8.get(j));
			}				
			
		}
		//判断够不够128条数据，不够就自己补00,FF
		if(4==list.size()){
			commonMap.put("sunday", sunday);
			commonMap.put("monday", monday);
			commonMap.put("tuesday", tuesday);
			commonMap.put("wedsday", wedsday);
			commonMap.put("thursday", thursday);
			commonMap.put("friday", friday);
			commonMap.put("saturday", saturday);
			commonMap.put("vocation", vocation);
		}else{
			for(int k=0;k<4-list.size();k++){
				String  str="{'hour':'00','minute':'00','actionType':'FF',retain:''}";
				Map map=GsonUtil.toBean(str, HashMap.class);
				//ArrayList strList=(ArrayList) GsonUtil.getListFromJson(str, new  ArrayList().getClass());
				for(int t=0;t<8;t++){
					sunday.add(map);
					monday.add(map);
					tuesday.add(map);
					wedsday.add(map);
					thursday.add(map);
					friday.add(map);
					saturday.add(map);
					vocation.add(map);
				}
			}
			commonMap.put("sunday", sunday);
			commonMap.put("monday", monday);
			commonMap.put("tuesday", tuesday);
			commonMap.put("wedsday", wedsday);
			commonMap.put("thursday", thursday);
			commonMap.put("friday", friday);
			commonMap.put("saturday", saturday);
			commonMap.put("vocation", vocation);
			
		}
		
		
		//获取数据到后台
		for(int i=0;i<list.size();i++){
			List<DeviceTimeGroup>  deviceTimeGroups=new ArrayList<DeviceTimeGroup>();
			ArrayList  array1=(ArrayList) list.get(i).get("sundayTime");
			ArrayList  array2=(ArrayList) list.get(i).get("mondayTime");
			ArrayList  array3=(ArrayList) list.get(i).get("tuesdayTime");
			ArrayList  array4=(ArrayList) list.get(i).get("wedsdayTime");
			ArrayList  array5=(ArrayList) list.get(i).get("thursdayTime");
			ArrayList  array6=(ArrayList) list.get(i).get("fridayTime");
			ArrayList  array7=(ArrayList) list.get(i).get("saturdayTime");
			ArrayList  array8=(ArrayList) list.get(i).get("vocationTime");
			
			for(int j=0;j<array1.size();j++){
				DeviceTimeGroup  deviceTimeGroup=new DeviceTimeGroup();
				LinkedTreeMap  dayTime0=(LinkedTreeMap) array1.get(j);
				LinkedTreeMap  dayTime1=(LinkedTreeMap) array2.get(j);
				LinkedTreeMap  dayTime2=(LinkedTreeMap) array3.get(j);
				LinkedTreeMap  dayTime3=(LinkedTreeMap) array4.get(j);
				LinkedTreeMap  dayTime4=(LinkedTreeMap) array5.get(j);
				LinkedTreeMap  dayTime5=(LinkedTreeMap) array6.get(j);
				LinkedTreeMap  dayTime6=(LinkedTreeMap) array7.get(j);
				LinkedTreeMap  dayTime7=(LinkedTreeMap) array8.get(j);
				//获取时间点
				String  sunday_time=dayTime0.get("hour").toString();
				String  monday_time=dayTime1.get("hour").toString();
				String  tuesday_time=dayTime2.get("hour").toString();
				String  wedsday_time=dayTime3.get("hour").toString();
				String  thursday_time=dayTime4.get("hour").toString();
				String  friday_time=dayTime5.get("hour").toString();
				String  saturday_time=dayTime6.get("hour").toString();
				String  vacation_time=dayTime7.get("hour").toString();
				
				String  sunday_time2=dayTime0.get("minute").toString();
				String  monday_time2=dayTime1.get("minute").toString();
				String  tuesday_time2=dayTime2.get("minute").toString();
				String  wedsday_time2=dayTime3.get("minute").toString();
				String  thursday_time2=dayTime4.get("minute").toString();
				String  friday_time2=dayTime5.get("minute").toString();
				String  saturday_time2=dayTime6.get("minute").toString();
				String  vacation_time2=dayTime7.get("minute").toString();
			    //获取动作类型
				String  sunday_action_type=dayTime0.get("actionType").toString();
				String  monday_action_type=dayTime1.get("actionType").toString();
				String  tuesday_action_type=dayTime2.get("actionType").toString();
				String  wedsday_action_type=dayTime3.get("actionType").toString();
				String  thursday_action_type=dayTime4.get("actionType").toString();
				String  friday_action_type=dayTime5.get("actionType").toString();
				String  saturday_action_type=dayTime6.get("actionType").toString();
				String  vacation_action_type=dayTime7.get("actionType").toString();
			    //获取设备时间组id
				Integer id=Integer.parseInt(dayTime0.get("retain").toString());
				
				//把值设进model中
				deviceTimeGroup.setId(id);
				deviceTimeGroup.setSunday_time(sunday_time+":"+sunday_time2);
				deviceTimeGroup.setSunday_action_type(sunday_action_type);
				deviceTimeGroup.setSaturday_time(saturday_time+":"+saturday_time2);
				deviceTimeGroup.setSaturday_action_type(saturday_action_type);
				deviceTimeGroup.setMonday_time(monday_time+":"+monday_time2);
				deviceTimeGroup.setMonday_action_type(monday_action_type);
				deviceTimeGroup.setTuesday_time(tuesday_time+":"+tuesday_time2);
				deviceTimeGroup.setTuesday_action_type(tuesday_action_type);
				deviceTimeGroup.setWedsday_time(wedsday_time+":"+wedsday_time2);
				deviceTimeGroup.setWedsday_action_type(wedsday_action_type);
				deviceTimeGroup.setThursday_time(thursday_time+":"+thursday_time2);
				deviceTimeGroup.setThursday_action_type(thursday_action_type);
				deviceTimeGroup.setFriday_time(friday_time+":"+friday_time2);
				deviceTimeGroup.setFriday_action_type(friday_action_type);
				deviceTimeGroup.setVacation_time(vacation_time+":"+vacation_time2);
				deviceTimeGroup.setVacation_action_type(vacation_action_type);
				
				deviceTimeGroups.add(deviceTimeGroup);
			}
			deviceTimeGroupMap.put("deviceGroup"+i,deviceTimeGroups);
		}
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        

		String createUser=loginUser.getYhMc();
		  //远程调用common项目的action
		  /*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
		  String deviceTimeGroupUrl=mjCommService.getMjCommUrl(deviceNum)+"deviceGroupAction/setDeviceGroup.do?";
		  String dataString= GsonUtil.toJson(commonMap);
		  String  deviceTimeGroupRespStr=HttpRequest.sendPost(deviceTimeGroupUrl, "data="+dataString+"&mac="+device_mac+"&sign="+sign);
		  Map deviceTimeMap=GsonUtil.toBean(deviceTimeGroupRespStr,HashMap.class);
		  
		try {
			if(deviceTimeMap!=null){
				if("0".equals(deviceTimeMap.get("result_code"))){
					boolean abc=deviceGroupService.modifyDeviceTimeGroup(deviceTimeGroupMap,createUser);
		             if(abc){
		            	 result.setSuccess(true);
		             }else{
		            	 result.setSuccess(false);
		             }
				}else{
					result.setSuccess(false);
				}
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
                 e.printStackTrace();
                 result.setSuccess(false);
		}  
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//通过设备编号查询设备时间组详细内容
	public  void  getDeviceTimeGroupById(){
		HttpServletRequest  request=getHttpServletRequest();
		String device_num=request.getParameter("device_num");
		List<DeviceTimeGroup>  list=deviceGroupService.getDeviceTimeGroupById(device_num);
//		Map commonMap=deviceGroupService.downLoadDeviceTimeGroupFirst(list);
	/*	Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
//	    String dataString= GsonUtil.toJson(commonMap);
//	    String deviceTimeGroupUrl=mjCommService.getMjCommUrl(device_num)+"deviceGroupAction/setDeviceGroup.do?";
//	    String  deviceTimeGroupRespStr=HttpRequest.sendPost(deviceTimeGroupUrl, "data="+dataString+"&mac="+list.get(0).getDevice_mac()+"&sign="+list.get(0).getDevice_type());
		Gson gson=new Gson();
		String jsonString=gson.toJson(list);
		printHttpServletResponse(jsonString);
		
	} 
	
	//通过设备编号查询设备下是否有门
	public  void  getDoorCountByDeviceNum(){
		HttpServletRequest  request=getHttpServletRequest();
		String device_num=request.getParameter("device_num");
		List<DeviceTimeGroup>  list=deviceGroupService.getDeviceTimeGroupById(device_num);
		JsonResult result=new JsonResult();
		result.setSuccess(true);
		
		if(list.size()<=0){
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	} 
	
	public void  exportDeviceTimeGroup(){
		HttpServletRequest  request=getHttpServletRequest();
		String device_num=request.getParameter("device_num");
		List<TimesGroup> timesGroupList = timesGroupService.getTimesGroupByDevice(device_num);
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
	    
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("设备时间段列表");
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		List<String> content=new ArrayList<String>();
		
		cloTitleList.add("查看内容");
		cloTitleList.add("时段一");
		cloTitleList.add("时段二");
		cloTitleList.add("时段三");
		cloTitleList.add("时段四");
		cloTitleList.add("时段五");
		cloTitleList.add("时段六");
		cloTitleList.add("时段七");
		cloTitleList.add("时段八");
		
		content.add("节假日");
		content.add("星期一");
		content.add("星期二");
		content.add("星期三");
		content.add("星期四");
		content.add("星期五");
		content.add("星期六");
		content.add("星期日");
		
		colList.add("");
		colList.add("times_one_num");
		colList.add("times_two_num");
		colList.add("times_three_num");
		colList.add("times_four_num");
		colList.add("times_five_num");
		colList.add("times_six_num");
		colList.add("times_seven_num");
		colList.add("times_eight_num");
		
		String[] colTitles=new String[cloTitleList.size()];
		String[] cols=new String[colList.size()];
		String[] contents=new String[content.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=cloTitleList.get(i);
		}
		for(int y=0;y<content.size();y++){
		    contents[y]=content.get(y);
		}
	   for(int i=0;i<colTitles.length; i++){
        	sheet.setColumnWidth(i, 5000);
        }
	   int columnsNum=cols.length;
	        
	        //大标题样式
	        Row row0 = sheet.createRow(0);
	        Cell cell00 = row0.createCell(0);
	        cell00.setCellValue("设备时间段列表");
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnsNum-1));
	        Font titleFont=wb.createFont();
	        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        titleFont.setFontHeightInPoints((short) (24));
	        CellStyle titleCellStyle=wb.createCellStyle();
	        titleCellStyle.setFont(titleFont);
	        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
	        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        titleCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
	        titleCellStyle.setBorderBottom((short)1);
	        titleCellStyle.setBorderLeft((short)1);
	        titleCellStyle.setBorderRight((short)1);
	        titleCellStyle.setBorderTop((short)1);
	        cell00.setCellStyle(titleCellStyle);
		    
	        //小标题样式
	        Font columnTitleFont=wb.createFont();
	        columnTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        CellStyle columnTitleStyle=wb.createCellStyle();
	        columnTitleStyle.setFont(columnTitleFont);
	        columnTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        columnTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        columnTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
	        columnTitleStyle.setBorderBottom((short)1);
	        columnTitleStyle.setBorderLeft((short)1);
	        columnTitleStyle.setBorderRight((short)1);
	        columnTitleStyle.setBorderTop((short)1);
            
	        
	        //时段组名称样式
	        Font deviceTimeFont=wb.createFont();
	        deviceTimeFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        CellStyle deviceTimeCellStyle=wb.createCellStyle();
	        deviceTimeCellStyle.setFont(deviceTimeFont);
	        deviceTimeCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
	        deviceTimeCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        deviceTimeCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        deviceTimeCellStyle.setBorderBottom((short)1);
	        deviceTimeCellStyle.setBorderLeft((short)1);
	        deviceTimeCellStyle.setBorderRight((short)1);
	        deviceTimeCellStyle.setBorderTop((short)1);
	        
            //主要内容样式
	        CellStyle contentStyle = wb.createCellStyle();
	        contentStyle.setBorderBottom((short)1);
	        contentStyle.setBorderLeft((short)1);
	        contentStyle.setBorderRight((short)1);
	        contentStyle.setBorderTop((short)1);
	        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
	        
		
		for(int j=0;j<timesGroupList.size();j++){
			List<TimesGroup> timesGroupList2 = timesGroupService.getTimesGroupsByParam("", "", timesGroupList.get(j).getGroup_num(), "");
			Times times = null;
			List<Times> timesList = null;
			
			//时段组名称栏
			int t1=j*10+1;   //时段组栏行数的下标
			int t2=t1+1;         //小标题行数的下标
			
			Row row1 = sheet.createRow(t1);
		    Cell cell1 = row1.createCell(0);
		    cell1.setCellValue("时段组名称:"+timesGroupList2.get(0).getGroup_name());
			sheet.addMergedRegion(new CellRangeAddress(t1,t1,0,columnsNum-1));
			cell1.setCellStyle(deviceTimeCellStyle);
			
			Row row2 = sheet.createRow(t2);
		    for (int i=0;i<columnsNum;i++){
	            Cell cell=row2.createCell(i);
	            cell.setCellValue(colTitles[i]);
	            cell.setCellStyle(columnTitleStyle);
	        }
			
			for (TimesGroup timesGroup :timesGroupList2) {
				times = new Times();
				Class<? extends TimesGroup> tg = timesGroup.getClass();
				Field[] fields = tg.getDeclaredFields();
				for(int i=3;i<=10;i++){
					fields[i].setAccessible(true);
					try {
						if(fields[i].get(timesGroup)==null){
							fields[i].set(timesGroup, "");
						}else{
							times.setTimes_num((String)fields[i].get(timesGroup));
							timesList = timesGroupService.getTimesByParamPageSortByBeginTime(times);
							for (Times time : timesList) {
								Class<? extends Times> t = time.getClass();
								String tfBegin = "";
								String tfEnd = "";
								try {
									Field tfb = t.getDeclaredField("begin_time");
									Field tfe = t.getDeclaredField("end_time");
									tfb.setAccessible(true);
									tfe.setAccessible(true);
									tfBegin = (String)tfb.get(time);
									tfEnd = (String)tfe.get(time);
									tfb.set(time, tfBegin.substring(0, 2)+":"+tfBegin.substring(2, 4)+" - "+tfEnd.substring(0, 2)+":"+tfEnd.substring(2, 4));
									fields[i].set(timesGroup, (String)tfb.get(time));
								} catch (NoSuchFieldException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
			}
		  
		  //导出数据库数据
           for(int k=0;k<timesGroupList2.size();k++){
        	   List<Map<String, Object>> list = (List<Map<String, Object>>) GsonUtil.getListFromJson(GsonUtil.toJson(timesGroupList2), ArrayList.class);
        	   Row row = sheet.createRow(t2+k+1);
   			   for (int c=0;c<columnsNum;c++){
	   				Cell cell=row.createCell(c);
	   				cell.setCellStyle(contentStyle);
	   			if(c==0){
	   				cell.setCellValue(contents[k]);
	   			}else{
	   				Object d;
						d = list.get(k).get(cols[c]);
						if (d!=null) {
							cell.setCellValue(d.toString());
						}
	   			}
   			   }	
			}
		}
		
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="用户时段信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			wb.write(out);
	        out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取设备组信息
	 */
	public void getDeviceGroup(){
		HttpServletRequest request = getHttpServletRequest();
		String flag=request.getParameter("flag");
		List<DeviceGroupModel> list = deviceGroupService.getDeviceGroup(flag);
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 懒加载：设备组——设备——监控门的树
	 * @author minting.he
	 */
	public void selNodeChild(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		List<Object> list = deviceGroupService.selNodeChild(id, type);
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
}
