package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.dao.IDepartmentGrantDao;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.FloorGroupModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.TkDeviceModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("PeopleAuthorityManagerAction")
public class PeopleAuthorityManagerAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "peopleAuthorityManagerServiceImpl")
	private PeopleAuthorityManagerService peopleAuthorityManagerService;
	
	@Resource(name = "departmentGrantDaoImpl")
	private IDepartmentGrantDao departmentGrantDao;
	
	@Resource(name = "departmentGrantServiceImpl")
	private IDepartmentGrantService departmentGrantService;

	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	//获取所有梯控设备信息
	public void  getTkDeviceInfo(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TkDeviceModel  tkDevice=GsonUtil.toBean(data,TkDeviceModel.class);
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", tkDevice.getDevice_num());
		map.put("device_name",tkDevice.getDevice_name());
		map.put("device_ip",tkDevice.getDevice_ip());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TkDeviceModel> list=peopleAuthorityManagerService.getTkDeviceInfo(map);
		Integer count=peopleAuthorityManagerService.getTkDeviceInfoCount(map);
		
		Grid<TkDeviceModel> grid=new Grid<TkDeviceModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	//通过设备编号获取所有梯控设备信息
	public void  getTkDeviceInfoByDeviceNum(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TkDeviceModel  tkDevice=GsonUtil.toBean(data,TkDeviceModel.class);
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", tkDevice.getDevice_num());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TkDeviceModel> list=peopleAuthorityManagerService.getTkDeviceInfoByDeviceNum(map);
		Integer count=peopleAuthorityManagerService.getTkDeviceInfoCountByDeviceNum(map);
		
		Grid<TkDeviceModel> grid=new Grid<TkDeviceModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/*获取人卡绑定信息*/
	public void getBoundCardInfo(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=peopleAuthorityManagerService.getBoundCardInfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/* 通过员工工号，卡号查询已有权限 */
	public void getAuthsByStaffNum(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		//Grid grid=peopleAuthorityManagerService.getAuthsByStaffNum(map);
		Grid grid=peopleAuthorityManagerService.getAuthsByStaffNumNew(map); 
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/* 通过员工工号，卡号查询已有权限 (按卡号，设备号分组)*/
	public void getAuthsByStaffNum2(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=peopleAuthorityManagerService.getAuthsByStaffNum2(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	//新增人员权限
	public  void addPeopleAuthority(){
        HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
        Gson gson = new Gson();
		
        try {
		String data = request.getParameter("datas");
		List<LinkedTreeMap> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
		/*String card_num=pamModel.getCard_num();
		String floor_group_num=pamModel.getFloor_group_num();
		String device_num=pamModel.getDevice_num();
		String begin_valid_time=pamModel.getBegin_valid_time();
		String end_valid_time=pamModel.getEnd_valid_time();*/
		
		
		/*String[] deviceNums=device_num.split(",");*/
		
		 Date date=new Date();//创建当前系统时间
         DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String today=format.format(date);
         
         User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
         String createUser=loginUser.getYhMc();
		        List<PeopleAuthorityManager> pamList=new  ArrayList<PeopleAuthorityManager>();
				for(int y=0;y<list.size();y++){
					PeopleAuthorityManager pam2=new PeopleAuthorityManager();
					String floorStr1="";
					List<Map> flooList=peopleAuthorityManagerService.getFloorListByObjectNum(list.get(y).get("dept_num").toString(), list.get(y).get("device_num").toString());
					List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(list.get(y).get("card_num").toString(),list.get(y).get("device_num").toString());
					if(floorLists.size()>0 && null != floorLists ){
						floorStr1+=addFloorList(floorLists,list.get(y).get("floor_list").toString());
					}else{
						floorStr1+=addFloorList(flooList,list.get(y).get("floor_list").toString());
					}
					
				/*	List<String> floorNums=peopleAuthorityManagerService.getFloorNum(list.get(y).get("floor_group_num").toString());
					String floorStr1="";
					for(String  floorNum:floorNums){
						floorStr1+=floorNum+"|";
					}*/
					//String card_type=peopleAuthorityManagerService.getCardType(list.get(y).get("card_num").toString());
					Map deviceMap=peopleAuthorityManagerService.getTkDeviceIPByDeviceNum(list.get(y).get("device_num").toString());
					//floorStr1=floorStr1.substring(0, floorStr1.length()-1);
					pam2.setStart_time(list.get(y).get("begin_valid_time").toString());
					pam2.setEnd_time(list.get(y).get("end_valid_time").toString());
					/*pam2.setFloor_list(list.get(y).get("floor_list").toString());*/
					pam2.setFloor_list(floorStr1);
					pam2.setFloor_group_num(list.get(y).get("floor_group_num") == null? null:list.get(y).get("floor_group_num").toString());
					pam2.setObject_num(list.get(y).get("staff_num").toString());
					pam2.setObject_type("1");
					pam2.setCard_num(list.get(y).get("card_num").toString());
					pam2.setCard_type("2");
					pam2.setDevice_num(list.get(y).get("device_num").toString());
					pam2.setDevice_ip(deviceMap.get("device_ip").toString());
					pam2.setDevice_port(deviceMap.get("device_port").toString());
					pam2.setAddress(deviceMap.get("address").toString());
					pam2.setCreate_time(today);
					pamList.add(pam2);
				}
		
			//boolean  flag1=peopleAuthorityManagerService.deletePeopleAuthority(pamList, today, createUser);
			beforeIntoTask(0,pamList);
			boolean  flag2=peopleAuthorityManagerService.addPeopleAuthority(pamList, today, createUser);
		    if(flag2){
		    	result.setSuccess(true);
		    }else{
		    	result.setSuccess(false);
		    }
		} catch (Exception e) {
               e.printStackTrace();
               result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	//叠加楼层
	public String addFloorList(List<Map> floorLists,String newFloorList){
		String floorStr1 = "";
		List<String> list = new ArrayList<String>();
		//遍历查出来的原有的楼层，去掉重复的放进新的list中
		for(Map m:floorLists){
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
		
		
		return  floorStr1;
	}
	
	
	//判断卡是否有权限
	public  void  isAuth(){
		HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
        Gson gson = new Gson();
		
        try {
    		String cardMap = request.getParameter("cardMap");
    		Map cards = gson.fromJson(cardMap,new LinkedTreeMap().getClass());
    	
    		List<Map> authList=peopleAuthorityManagerService.getAuthsByStaffNumNoLimit(cards);
    		
    		if(authList.size()>0){
    			result.setSuccess(true);
    		}else{
    			result.setSuccess(false);
    		}
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));	
   }
	
	
	
	//复制人员权限
	public void  copyPeopleAuthority(){
		HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
        Gson gson = new Gson();
		
        try {
        	String data = request.getParameter("datas");
    		List<LinkedTreeMap> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
    		String cardMap = request.getParameter("cardMap");
    		Map cards = gson.fromJson(cardMap,new LinkedTreeMap().getClass());
    	
    		List<Map> authList=peopleAuthorityManagerService.getAuthsByStaffNumNoLimit(cards);
    		
    		
    		 Date date=new Date();//创建当前系统时间
             DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String today=format.format(date);
             
             User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
             String createUser=loginUser.getYhMc();
    		        List<PeopleAuthorityManager> pamList=new  ArrayList<PeopleAuthorityManager>();
    				for(int y=0;y<list.size();y++){
    					for(int i=0;i<authList.size();i++){
    					List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(list.get(y).get("card_num").toString(),authList.get(i).get("deviceNum").toString());
    					String floorStr1=addFloorList(floorLists,authList.get(i).get("floorList").toString());
    						
    					/*List<String> floorNums=peopleAuthorityManagerService.getFloorNum(authList.get(i).get("groupNum").toString());
    					String floorStr1="";
    					for(String  floorNum:floorNums){
    						floorStr1+=floorNum+"|";
    					}
    					floorStr1=floorStr1.substring(0, floorStr1.length()-1);*/
						PeopleAuthorityManager pam2=new PeopleAuthorityManager();
						pam2.setObject_num(list.get(y).get("staff_num").toString());
						pam2.setObject_type("1");
						pam2.setCard_num(list.get(y).get("card_num").toString());
						pam2.setCard_type("2");
						
    					pam2.setStart_time(authList.get(i).get("beginDate").toString());
    					pam2.setEnd_time(authList.get(i).get("endDate").toString());
    					//这里应该对楼层进行一个并集处理
    					/*pam2.setFloor_list(authList.get(i).get("floorList").toString());*/
    					pam2.setFloor_list(floorStr1);
    					pam2.setFloor_group_num(authList.get(i).get("groupNum")==null? null:authList.get(i).get("groupNum").toString());
    					pam2.setDevice_num(authList.get(i).get("deviceNum").toString());
    					pam2.setDevice_ip(authList.get(i).get("deviceIP").toString());
    					pam2.setDevice_port(authList.get(i).get("devicePort").toString());
    					pam2.setAddress(authList.get(i).get("address").toString());
    					pam2.setCreate_time(today);
    					pamList.add(pam2);
    					}
    				}
    				beforeIntoTask(0,pamList);
    				boolean  flag2=peopleAuthorityManagerService.addPeopleAuthority(pamList, today, createUser);
    			    if(flag2){
    			    	result.setSuccess(true);
    			    }else{
    			    	result.setSuccess(false);
    			    }
    			} catch (Exception e) {
    	               e.printStackTrace();
    	               result.setSuccess(false);
    			}
    			
    			
    			printHttpServletResponse(new Gson().toJson(result));		
	        
     }
	
	
	//删除人员权限
	public  void deletePeopleAuthority(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result=new  JsonResult();
        Gson gson = new Gson();
        try {
			String data = request.getParameter("datas");
			List<LinkedTreeMap> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
			
			List<PeopleAuthorityManager> pamList=new  ArrayList<PeopleAuthorityManager>();
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String createUser=loginUser.getYhMc();
		
			
			for(int y=0;y<list.size();y++){
				PeopleAuthorityManager pam2=new PeopleAuthorityManager();
			/*	List<String> floorNums=peopleAuthorityManagerService.getFloorNum(list.get(y).get("floor_group_num").toString());
				String floorStr1="";
				for(String  floorNum:floorNums){
					floorStr1+=floorNum+"|";
				}
				if(floorStr1.length()>0){
				floorStr1=floorStr1.substring(0, floorStr1.length()-1);
				}*/
				Map deviceMap=peopleAuthorityManagerService.getTkDeviceIPByDeviceNum(list.get(y).get("device_num").toString());
				pam2.setStart_time(list.get(y).get("begin_valid_time").toString());
				Integer id=(int) Math.pow((Double) list.get(y).get("id"), 1);
				pam2.setId(id);
				pam2.setEnd_time(list.get(y).get("end_valid_time").toString());
				pam2.setFloor_list(list.get(y).get("floor_list").toString());
				pam2.setFloor_group_num(list.get(y).get("floor_group_num")==null?null:list.get(y).get("floor_group_num").toString());
				if("0".equals(list.get(y).get("object_type").toString())){
					pam2.setObject_num(list.get(y).get("dept_num").toString());
				}else{
					pam2.setObject_num(list.get(y).get("staff_num").toString());
				}
				pam2.setObject_type(list.get(y).get("object_type").toString());
				pam2.setCard_num(list.get(y).get("card_num").toString());
				pam2.setCard_type(list.get(y).get("card_type").toString());
				pam2.setDevice_num(list.get(y).get("device_num").toString());
				pam2.setDevice_ip(deviceMap.get("device_ip").toString());
				pam2.setDevice_port(deviceMap.get("device_port").toString());
				pam2.setAddress(deviceMap.get("address").toString());
				pam2.setCreate_time(today);
				pamList.add(pam2);
			}
			beforeIntoTask(1,pamList);
			boolean  flag=peopleAuthorityManagerService.updatePeopleAuthority(pamList, today, createUser);
			if(flag){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	/**
	 * 新增人员权限或删除人员权限时
	 * 插入任务表前
	 * 先处理权限记录表
	 * by huixian.pan
	 */
	public void beforeIntoTask(Integer flag,List<PeopleAuthorityManager> list){
		if(flag==0){
			updateAuthState2(list,"00");
		}else if(flag==1){
			updateAuthState(list,"10");
		}else if(flag==4){
			updateAuthState4(list,"00");
		}else{
			updateAuthState3(list,"00");
		}
	}
	
	/**
	 * 删除人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("id",m.getId()+"");
			map.put("state", state);
			peopleAuthorityManagerService.updTkAuthRecord(map);
		}
	}
	
	/**
	 * 新增人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState2(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("card_type", m.getCard_type());
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type","1");
			map.put("start_time", m.getStart_time());
			map.put("end_time", m.getEnd_time());
			map.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
	
	/**
	 * 保存人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState3(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("card_type", m.getCard_type());
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type","1");
			map.put("start_time", m.getStart_time());
			map.put("end_time", m.getEnd_time());
			map.put("state", state);
			peopleAuthorityManagerService.delByID(map);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
	
	/**
	 * 重新下载人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState4(List<PeopleAuthorityManager> list,String state){
		for(PeopleAuthorityManager m:list){
			Map map=new HashMap();
			map.put("card_num", m.getCard_num());
			map.put("card_type", m.getCard_type());
			map.put("object_num", m.getObject_num());
			map.put("floor_group_num", m.getFloor_group_num());
			map.put("floor_list", m.getFloor_list());
			map.put("device_num", m.getDevice_num());
			map.put("object_type",m.getObject_type());
			map.put("start_time", m.getStart_time());
			map.put("end_time", m.getEnd_time());
			map.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
	
	/**
	 * 重新下载组织权限前 (组织下没卡时，只记录该组织权限)
	 * by huixian.pan
	 */
	public void updateAuthState5(DepartmentGrantModel m,String state){
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
	
	
	//获取所有楼层组信息
	public  void  getFloorGroupSelections(){
		List<FloorGroupModel>  fgList=peopleAuthorityManagerService.getfloorGroupSelections();
		
		printHttpServletResponse(new Gson().toJson(fgList));
	}
	
	
	//查询所有下发权限任务
	public  void getTkAuthorityTask(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=peopleAuthorityManagerService.getTkAuthorityTask(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-11 
	 * @功能描述: 梯控下载未下载权限（作废）
	 * @参数描述: 
	 */
	public  void  downloadTkFailureAuthority(){
		JsonResult  result=new JsonResult();
		
		try {
			Date date=new Date();//创建当前系统时间
	        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String today=format.format(date);
	        
	        User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	        String create_user=loginUser.getYhMc();
			List<PeopleAuthorityManager>  pamList=peopleAuthorityManagerService.getTkFailureAuthorityTask();
			if(pamList.size()>0){
				boolean flag2=peopleAuthorityManagerService.addPeopleAuthority(pamList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("下载成功");
				}else{
					result.setSuccess(false);
					result.setMsg("下载失败");
				}
			}else{
					result.setSuccess(false);
					result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("下载失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	//查询所有人员下发权限任务
	public  void getStaffAuthorityTask(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=peopleAuthorityManagerService.getAllAuthTaskHis(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-25 
	 * @功能描述: 梯控下载未下载人员权限
	 * @参数描述: 
	 */
	public  void  downloadStaffFailureAuthority(){
		JsonResult  result=new JsonResult();
		
		try {
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			List<PeopleAuthorityManager>  pamList=peopleAuthorityManagerService.getStaffFailureAuthorityTask();
			if(pamList.size()>0){
				boolean flag2=peopleAuthorityManagerService.downloadStaffFailureTask(pamList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("操作成功");
				}else{
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}else{
					result.setSuccess(false);
					result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2017-01-06 
	 * @功能描述: 梯控下载已删除人员权限
	 * @参数描述: 
	 */
	public  void  downloadDeletedAuth(){
		JsonResult  result=new JsonResult();
		try {
			HttpServletRequest request=getHttpServletRequest();
			String id=request.getParameter("id");
			
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			List<PeopleAuthorityManager>  pamList=peopleAuthorityManagerService.getAuthorityTaskById(id);
			for(PeopleAuthorityManager pam:pamList){
				pam.setAction_flag("0");
			}
			if(pamList.size()>0){
				beforeIntoTask(4,pamList);
				boolean flag2=peopleAuthorityManagerService.downloadStaffFailureTask(pamList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("操作成功");
				}else{
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}else{
				result.setSuccess(false);
				result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-25 
	 * @功能描述: 梯控重新全部下载人员权限
	 * @参数描述: 
	 */
	public  void  downloadStaffAuthority(){
		JsonResult  result=new JsonResult();
		
		try {
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			
			 List<Map>  list=peopleAuthorityManagerService.getStaffAuthority();
			 List<PeopleAuthorityManager> pamList=new  ArrayList<PeopleAuthorityManager>();
			 if(list.size()>0){
				for(int y=0;y<list.size();y++){
					PeopleAuthorityManager pam2=new PeopleAuthorityManager();
					/*List<String> floorNums=peopleAuthorityManagerService.getFloorNum(list.get(y).get("floor_group_num").toString());
					String floorStr1="";
					for(String  floorNum:floorNums){
						floorStr1+=floorNum+"|";
					}
					floorStr1=floorStr1.substring(0, floorStr1.length()-1);*/
					pam2.setStart_time(list.get(y).get("begin_valid_time").toString());
					pam2.setEnd_time(list.get(y).get("end_valid_time").toString());
					pam2.setFloor_list(list.get(y).get("floor_list").toString());
					pam2.setFloor_group_num(list.get(y).get("floor_group_num")==null?null:list.get(y).get("floor_group_num").toString());
					pam2.setObject_num(list.get(y).get("object_num").toString());
					pam2.setObject_type(list.get(y).get("object_type").toString());
					pam2.setCard_num(list.get(y).get("card_num").toString());
					pam2.setCard_type(list.get(y).get("card_type").toString());
					pam2.setDevice_num(list.get(y).get("device_num").toString());
					pam2.setDevice_ip(list.get(y).get("device_ip").toString());
					pam2.setDevice_port(list.get(y).get("device_port").toString());
					pam2.setAddress(list.get(y).get("address").toString());
					pam2.setCreate_time(today);
					pamList.add(pam2);
				}
			beforeIntoTask(4,pamList);
			boolean flag2=peopleAuthorityManagerService.addPeopleAuthority(pamList, today, create_user);
			if(flag2){
				result.setSuccess(true);
				result.setMsg("操作成功");
			}else{
				result.setSuccess(false);
				result.setMsg("操作失败");
			}
		  }else{
			    result.setSuccess(false);
				result.setMsg("下载完成");
		  }	
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	//查询所有下发部门权限任务
	public  void getDeptAuthorityTask(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=peopleAuthorityManagerService.getDeptAuthorityTask(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-25 
	 * @功能描述: 梯控下载未下载部门权限
	 * @参数描述: 
	 */
	public  void  downloadDeptFailureAuthority(){
		JsonResult  result=new JsonResult();
		
		try {
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			List<PeopleAuthorityManager>  pamList=peopleAuthorityManagerService.getDeptFailureAuthorityTask();
			if(pamList.size()>0){
				boolean flag2=peopleAuthorityManagerService.downloadStaffFailureTask(pamList, today, create_user);
				if(flag2){
					result.setSuccess(true);
					result.setMsg("操作成功");
				}else{
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}else{
				result.setSuccess(true);
				result.setMsg("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-10-25 
	 * @功能描述: 梯控重新全部下载部门权限
	 * @参数描述: 
	 */
	public  void  downloadDeptAuthority(){
		JsonResult  result=new JsonResult();
		
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
		String create_user=loginUser.getYhMc();
		
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "添加部门权限");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		
		try {
			List<Map>  list=peopleAuthorityManagerService.getDeptAuthority();
			 if(list.size()>0){
				for(int y=0;y<list.size();y++){
					DepartmentGrantModel departmentGrantModel=new DepartmentGrantModel();
					/*List<String> floorNums=peopleAuthorityManagerService.getFloorNum(list.get(y).get("floor_group_num").toString());
					String floorStr1="";
					for(String  floorNum:floorNums){
						floorStr1+=floorNum+"|";
					}
					floorStr1=floorStr1.substring(0, floorStr1.length()-1);*/
					departmentGrantModel.setBegin_valid_time(list.get(y).get("begin_valid_time").toString());
					departmentGrantModel.setEnd_valid_time(list.get(y).get("end_valid_time").toString());
					departmentGrantModel.setFloor_list(list.get(y).get("floor_list").toString());
					departmentGrantModel.setFloor_group_num(list.get(y).get("floor_group_num")==null?null:list.get(y).get("floor_group_num").toString());
					departmentGrantModel.setObject_num(list.get(y).get("object_num").toString());
					departmentGrantModel.setObject_type(list.get(y).get("object_type").toString());
					departmentGrantModel.setCard_type(list.get(y).get("card_type").toString());
					departmentGrantModel.setDevice_num(list.get(y).get("device_num").toString());
					departmentGrantModel.setDevice_ip(list.get(y).get("device_ip").toString());
					departmentGrantModel.setDevice_port(Integer.parseInt(list.get(y).get("device_port").toString()));
					departmentGrantModel.setAddress(list.get(y).get("address").toString());
					
					if(null == list.get(y).get("card_num") || "".equals(list.get(y).get("card_num").toString())){
						updateAuthState5(departmentGrantModel,"01");
						result.setSuccess(true);
						result.setMsg("操作成功");
					}else{
						departmentGrantModel.setCard_num(list.get(y).get("card_num").toString());
						boolean addFlag=departmentGrantService.addDeptTask(departmentGrantModel,create_user);
						if(addFlag){
							result.setSuccess(true);
							result.setMsg("操作成功");
						}else{
							result.setSuccess(false);
							result.setMsg("操作失败");
						}
					}
					
				}
			 }else{
				   result.setSuccess(false);
				   result.setMsg("下载完成");
			 }
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "新增失败");
			logDao.addLog(log);
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//通过楼层组编号获取楼层信息
	public  void  getfloorList(){
		HttpServletRequest  request=getHttpServletRequest();
		String floor_group_num=request.getParameter("floor_group_num");
		List<String> floorNums=peopleAuthorityManagerService.getFloorNum(floor_group_num);
		ArrayList<String> floorList=new ArrayList<String>();
		for(int i=0;i<floorNums.size();i++){
			floorList.add(floorNums.get(i));
		}
		printHttpServletResponse(GsonUtil.toJson(floorList));
	}
	
	//保存人员权限
	public  void savePeopleAuthority(){
        HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
        Gson gson = new Gson();
		
        try {
		String data = request.getParameter("datas");
		List<LinkedTreeMap> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
		 Date date=new Date();//创建当前系统时间
         DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String today=format.format(date);
         
         User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
         String createUser=loginUser.getYhMc();
		        List<PeopleAuthorityManager> pamList=new  ArrayList<PeopleAuthorityManager>();
				for(int y=0;y<list.size();y++){
					PeopleAuthorityManager pam2=new PeopleAuthorityManager();
				/*	List<String> floorNums=peopleAuthorityManagerService.getFloorNum(list.get(y).get("floor_group_num").toString());
					String floorStr1="";
					for(String  floorNum:floorNums){
						floorStr1+=floorNum+"|";
					}*/
					Map deviceMap=peopleAuthorityManagerService.getTkDeviceIPByDeviceNum(list.get(y).get("deviceNum").toString());
					//floorStr1=floorStr1.substring(0, floorStr1.length()-1);
					Integer id=(int) Math.pow((Double) list.get(y).get("id"), 1);
					pam2.setId(id);
					pam2.setStart_time(list.get(y).get("beginDate").toString());
					pam2.setEnd_time(list.get(y).get("endDate").toString());
					pam2.setFloor_list(list.get(y).get("floorList").toString());
					pam2.setFloor_group_num(list.get(y).get("floorGroupNum")==null?null:list.get(y).get("floorGroupNum").toString());
					pam2.setObject_num(list.get(y).get("staffNum").toString());
					pam2.setObject_type("1");
					pam2.setCard_num(list.get(y).get("cardNum").toString());
					pam2.setCard_type((String)list.get(y).get("cardType"));
					pam2.setDevice_num(list.get(y).get("deviceNum").toString());
					pam2.setDevice_ip(deviceMap.get("device_ip").toString());
					pam2.setDevice_port(deviceMap.get("device_port").toString());
					pam2.setAddress(deviceMap.get("address").toString());
					pam2.setCreate_time(today);
					pamList.add(pam2);
				}
		
			//boolean  flag1=peopleAuthorityManagerService.deletePeopleAuthority(pamList, today, createUser);
			beforeIntoTask(2,pamList);
			boolean  flag2=peopleAuthorityManagerService.addPeopleAuthority(pamList, today, createUser);
		    if(flag2){
		    	result.setSuccess(true);
		    }else{
		    	result.setSuccess(false);
		    }
		} catch (Exception e) {
               e.printStackTrace();
               result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
