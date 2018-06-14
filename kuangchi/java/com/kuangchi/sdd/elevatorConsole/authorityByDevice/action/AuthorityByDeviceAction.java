package com.kuangchi.sdd.elevatorConsole.authorityByDevice.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.service.AuthorityByDeviceService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("authorityByDeviceAction")
public class AuthorityByDeviceAction extends BaseActionSupport{
	
	@Autowired
	private AuthorityByDeviceService authorityByDeviceService;
	
	@Resource(name = "peopleAuthorityManagerServiceImpl")
	private PeopleAuthorityManagerService peopleAuthorityManagerService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 查询所有的梯控设备
	 * by gengji.yang
	 */
	public void getAllTkDevices(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=authorityByDeviceService.getAllTkDevices(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 获取员工及卡
	 * by gengji.yang
	 */
	public void getStaffCardsBydeptNums(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=authorityByDeviceService.getStaffCardsBydeptNums(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 初始化楼层组下拉框
	 * by gengji.yang
	 */
	public void getFloorGroups(){
		printHttpServletResponse(GsonUtil.toJson(authorityByDeviceService.getFloorGroups()));
	}
	
	/**
	 * 设备授权
	 * by gengji.yang
	 */
	public void authDevice(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result=new  JsonResult(); 
		result.setSuccess(true);
		Gson gson = new Gson();
		try {
			String jsonHexStr = request.getParameter("data");
			List<Map> list = gson.fromJson(jsonHexStr,new ArrayList<LinkedTreeMap>().getClass());
			/*beforeIntoTask(0,list);*/
//		[{beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1003, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1001, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1002, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1005, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1004, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1006, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=1007, cardTypeNum=A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}, {beginDate=2016-09-27, endDate=2016-09-27, groupNum=1, deviceNum=1, cardNum=47054, cardTypeNum=7E8F99A0-068F-4579-A839-AC04E875ACC8, staffNum=a9b3851a-389b-43a2-bca0-e17d6cc9bdd6}]
			for(Map m:list){
				List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(m.get("cardNum").toString(),m.get("deviceNum").toString());
				String floorStr1=addFloorList2(floorLists,m.get("floorList").toString());
					/*List<String> floors=authorityByDeviceService.getFloorNum2(m.get("groupNum").toString());
					String floorStr1="";
					for(String  floorNum:floors){
						floorStr1+=floorNum+"|";
					}
					floorStr1=floorStr1.substring(0, floorStr1.length()-1);*/
					Map deviceMap=authorityByDeviceService.getTkDeviceIPByDeviceNum(m.get("deviceNum").toString());
					//m.put("floorList", floorStr1);
					m.put("objectType", "1");
					m.put("deviceIP", deviceMap.get("device_ip").toString());
					m.put("devicePort", deviceMap.get("device_port").toString());
					m.put("address",deviceMap.get("address").toString());
					m.put("cardType","2");
					m.put("floorList", floorStr1);
					updateAuthState5(m,"00");
					boolean flag=authorityByDeviceService.addDeviceAuth(m);
					if(!flag){
						result.setSuccess(false);
					}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//叠加楼层
	public String addFloorList2(List<Map> floorLists,String newFloorList){
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
	
	/**
	 * 查询权限,通过员工号串+设备串号
	 * by gengji.yang
	 */
	public void getAuths(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=authorityByDeviceService.getAuths(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 查询权限(用于删除时查看)
	 * by huixian.pan
	 */
	public void getAuthsNoRepeat(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=authorityByDeviceService.getAuthsNoRepeat(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 删除权限
	 * by gengji.yang
	 */
/*	public void deleteAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String idStr=request.getParameter("idStr");
		authorityByDeviceService.deleteAuths(idStr);
	}*/
	
	/**
	 * 删除权限
	 * by huixian.pan
	 */
	public void deleteAuths(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result=new  JsonResult();
        Gson gson = new Gson();
		
        try {
			String data = request.getParameter("datas");
			List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
			beforeIntoTask(1,list);
			for(Map m:list){
				String floorStr1="";
				Map deviceMap=authorityByDeviceService.getTkDeviceIPByDeviceNum(m.get("device_num").toString());
				m.put("floor_list", floorStr1);
				m.put("device_ip", deviceMap.get("device_ip").toString());
				m.put("device_port", deviceMap.get("device_port").toString());
				m.put("address",deviceMap.get("address").toString());
				
			    boolean  flag=authorityByDeviceService.deleteAuths(m);
				if(flag){
					result.setSuccess(true);
				}else{
					result.setSuccess(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));
		
	}
	
	/**
	 * 获取设备信息
	 * by huixian.pan
	 */
	public void getTkDeviceInfo(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=authorityByDeviceService.getTkDeviceInfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/**
	 * 判断设备是否有权限
	 * by huixian.pan
	 */
	public  void  isAuth(){
		HttpServletRequest request = getHttpServletRequest();
        JsonResult result=new  JsonResult();
		
        try {
			String deviceNum=request.getParameter("datas");
			HashMap map2=new HashMap();
			map2.put("device_num", deviceNum);
			List<Map> list=authorityByDeviceService.getTkAuthByDeviceNum(map2);
    		
    		if(list.size()>0){
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
	 * 复制权限
	 * by huixian.pan
	 */
	public void copyAuth(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult  result=new JsonResult();
		result.setSuccess(true);
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("datas"), HashMap.class);
			String deviceNum=map.get("deviceNum").toString();
			String toDeviceNum=map.get("toDeviceNum").toString();
			String[] toDeviceNums=toDeviceNum.split(",");
			HashMap map2=new HashMap();
			map2.put("device_num", deviceNum);
			List<Map> list=authorityByDeviceService.getTkAuthByDeviceNum(map2);
			/*beforeIntoTask(0,list);*/
			
		    for(String device_num:toDeviceNums){
				for(Map m:list){
					
					Map deviceMap=authorityByDeviceService.getTkDeviceIPByDeviceNum(device_num.substring(1, device_num.length()-1));
					m.put("deviceNum", device_num.substring(1, device_num.length()-1));
					m.put("deviceIP", deviceMap.get("device_ip").toString());
					m.put("devicePort", deviceMap.get("device_port").toString());
					m.put("address",deviceMap.get("address").toString());
					m.put("cardType","2");
					
					if(null ==m.get("cardNum")){
						//此处的staffNum是部门编号
						List<Map> floorLists=peopleAuthorityManagerService.getFloorListByObjectNum(m.get("staffNum").toString(),device_num.substring(1, device_num.length()-1)); 
						String floorStr1=addFloorList2(floorLists,m.get("floorList").toString());
						m.put("floorList", floorStr1);
						updateAuthState4(m,"01",device_num);
					}else{	
						List<Map> floorLists=peopleAuthorityManagerService.getFloorListByCardNum(m.get("cardNum").toString(),device_num.substring(1, device_num.length()-1));
						String floorStr2=addFloorList2(floorLists,m.get("floorList").toString());
						m.put("floorList", floorStr2);
						updateAuthState4(m,"00",device_num);
						boolean flag=authorityByDeviceService.addDeviceAuth(m);
						if(!flag){
							result.setSuccess(false);
						}
					}
					/*List<String> floors=authorityByDeviceService.getFloorNum2(m.get("groupNum").toString());
					String floorStr1="";
					for(String  floorNum:floors){
						floorStr1+=floorNum+"|";
					}
					floorStr1=floorStr1.substring(0, floorStr1.length()-1);*/
				
					
					/*updateAuthState4(m,"00",device_num);
					boolean flag=authorityByDeviceService.addDeviceAuth(m);
						if(!flag){
							result.setSuccess(false);
						}*/
				 }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
//===========================================================================		
	/**
	 * 新增人员权限或删除人员权限时
	 * 插入任务表前
	 * 先处理权限记录表
	 * by huixian.pan
	 */
	public void beforeIntoTask(Integer flag,List<Map> list){
		if(flag==0){
			updateAuthState2(list,"00");
		}else if(flag==1){
			updateAuthState(list,"10");
		}else{
			updateAuthState3(list,"00");
		}
	}
	
	/**
	 * 删除人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState(List<Map> list,String state){
		for(Map m:list){
			Map map=new HashMap();
			map.put("id",m.get("id")+"");
			map.put("state", state);
			peopleAuthorityManagerService.updTkAuthRecord(map);
		}
	}
	
	/**
	 * 新增人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState2(List<Map> list,String state){
		for(Map m:list){
			Map map=new HashMap();
			map.put("card_num", m.get("cardNum"));
			map.put("card_type", "2");
			map.put("object_num", m.get("staffNum"));
			map.put("floor_group_num", m.get("groupNum"));
			map.put("floor_list", m.get("floorList"));
			map.put("device_num", m.get("deviceNum"));
			map.put("object_type","1");
			map.put("start_time", m.get("beginDate"));
			map.put("end_time", m.get("endDate"));
			map.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
	
	/**
	 * 新增人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState5(Map m,String state){
			Map map=new HashMap();
			map.put("card_num", m.get("cardNum"));
			map.put("card_type", "2");
			map.put("object_num", m.get("staffNum"));
			map.put("floor_group_num", m.get("groupNum"));
			map.put("floor_list", m.get("floorList"));
			map.put("device_num", m.get("deviceNum"));
			map.put("object_type","1");
			map.put("start_time", m.get("beginDate"));
			map.put("end_time", m.get("endDate"));
			map.put("state", state);
			peopleAuthorityManagerService.addTkAuthRecord(map);
	}
	/**
	 * 复制人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState4(Map m,String state,String device_num){
				Map map=new HashMap();
				map.put("card_num", m.get("cardNum"));
				map.put("card_type", "2");
				map.put("object_num", m.get("staffNum"));
				map.put("floor_group_num", m.get("groupNum"));
				map.put("floor_list", m.get("floorList"));
				map.put("device_num", device_num.substring(1, device_num.length()-1));
				map.put("object_type",m.get("objectType"));
				map.put("start_time", m.get("beginDate"));
				map.put("end_time", m.get("endDate"));
				map.put("state", state);
				peopleAuthorityManagerService.addTkAuthRecord(map);
	}
	
	/**
	 * 保存人员权限前
	 * by huixian.pan
	 */
	public void updateAuthState3(List<Map> list,String state){
		for(Map m:list){
			Map map=new HashMap();
			map.put("card_num", m.get("cardNum"));
			map.put("card_type", m.get("cardType"));
			map.put("object_num", m.get("staffNum"));
			map.put("floor_group_num", m.get("groupNum"));
			map.put("floor_list", m.get("floorList"));
			map.put("device_num", m.get("deviceNum"));
			map.put("object_type","1");
			map.put("start_time", m.get("beginDate"));
			map.put("end_time", m.get("endDate"));
			map.put("state", state);
			peopleAuthorityManagerService.delByID(map);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
//===========================================================================	
	
	/**
	 * 保存权限
	 * by gengji.yang
	 */
	public void saveDevAuth(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result=new  JsonResult(); 
		result.setSuccess(true);
		Gson gson = new Gson();
		try {
			String jsonHexStr = request.getParameter("data");
			List<Map> list = gson.fromJson(jsonHexStr,new ArrayList<LinkedTreeMap>().getClass());
			beforeIntoTask(2,list);
			for(Map m:list){
					Map deviceMap=authorityByDeviceService.getTkDeviceIPByDeviceNum(m.get("deviceNum").toString());
					m.put("deviceIP", deviceMap.get("device_ip").toString());
					m.put("devicePort", deviceMap.get("device_port").toString());
					m.put("address",deviceMap.get("address").toString());
                    m.put("objectType", "1");
					boolean flag=authorityByDeviceService.addDeviceAuth(m);
					if(!flag){
						result.setSuccess(false);
					}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	
	}

}
