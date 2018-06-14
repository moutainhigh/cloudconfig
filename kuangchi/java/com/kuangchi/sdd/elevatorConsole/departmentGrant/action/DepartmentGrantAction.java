package com.kuangchi.sdd.elevatorConsole.departmentGrant.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DeviceModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.FloorGroupInfoModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.service.PeopleAuthorityManagerService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * guofei.lian
 * 2016-09-26
 * 组织授权action
 * */
@Controller("departmentGrantAction")
public class DepartmentGrantAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "departmentGrantServiceImpl")
	private IDepartmentGrantService departmentGrantService;
	@Resource(name = "peopleAuthorityManagerServiceImpl")
	private PeopleAuthorityManagerService peopleAuthorityManagerService;
	@Override
	public Object getModel() {
		return null;
	}
	
	public void getGrantsByParam(){
		HttpServletRequest request=getHttpServletRequest();
		String deptIds=request.getParameter("deptIds");
		String page=request.getParameter("page");
		String rows = request.getParameter("rows");
		Grid departmentGrantModel=departmentGrantService.getDepartmentGrantsByParam(deptIds,page,rows);
		printHttpServletResponse(GsonUtil.toJson(departmentGrantModel));
	}
	
	//新增时查询所有设备信息  2016-09-27  guofei.lian
	public void getDevicesInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		String page=request.getParameter("page");
		String rows = request.getParameter("rows");
		DeviceModel deviceModel=GsonUtil.toBean(data,DeviceModel.class);
		Grid<DeviceModel> grid=departmentGrantService.getDevicesInfo(deviceModel,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//初始化楼层组下拉框  2016-09-27 guofei.lian
	public void getFloorGroupInfoForSelect(){
		List<Map> list=new ArrayList<Map>();
		List<FloorGroupInfoModel> FloorGroupInfoModelList=departmentGrantService.getFloorGroupInfoForSelect();
		for(FloorGroupInfoModel fgim:FloorGroupInfoModelList){
			Map<String,String> map=new HashMap<String,String>();
			map.put("VALUE", fgim.getFloor_group_num());
			map.put("TEXT", fgim.getFloor_group_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	//新增组织权限之前验证所选部门中员工是否绑卡  2016-09-28 guofei.lian
	public void checkCardInDept(){
		HttpServletRequest request=getHttpServletRequest();
		String object_nums=request.getParameter("object_nums");
		JsonResult result = new JsonResult();
		boolean flag=departmentGrantService.checkCardInDept(object_nums);
		if(flag){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setMsg("所选部门中员工没有可授权的卡，请重新选择部门");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//新增组织权限  2016-09-27  guofei.lian
	public void addDeptGrant(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		DepartmentGrantModel departmentGrantModel=GsonUtil.toBean(data,DepartmentGrantModel.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=departmentGrantService.addDeptGrant(departmentGrantModel,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("新增成功");
		}else{
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//保存组织权限  2016-09-27  guofei.lian
	public void saveDeptGrant(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		String data=request.getParameter("data");
		List<Map> list = gson.fromJson(data,new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		try{
			//updateAuthState3(list,"00");
			departmentGrantService.saveDeptAuth(list,create_user);
			result.setSuccess(true);
			result.setMsg("保存成功");
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("保存失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 保存组织权限前
	 */
	public void updateAuthState3(List<Map> list,String state){
		for(Map m:list){
			Map map=new HashMap();
			map.put("card_num", m.get("cardNum"));
			map.put("card_type", m.get("card_type"));
			map.put("object_num", m.get("object_num"));
			map.put("floor_group_num", m.get("groupNum"));
			map.put("floor_list", m.get("floor_list"));
			map.put("device_num", m.get("device_num"));
			map.put("object_type","0");
			map.put("start_time", m.get("begin_valid_time"));
			map.put("end_time", m.get("end_valid_time"));
			map.put("id", m.get("id"));
			map.put("state", state);
			peopleAuthorityManagerService.delByID(map);
			peopleAuthorityManagerService.addTkAuthRecord(map);
		}
	}
	
	//删除组织权限  2016-09-28  guofei.lian
	public void removeDeptGrant(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		/*String ids=request.getParameter("ids");*/
/*		String object_nums=request.getParameter("object_nums");
*/		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=departmentGrantService.removeDeptGrant(map,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("删除成功");
		}else{
			result.setSuccess(false);
			result.setMsg("删除失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//复制权限前，判断源部门中员工的卡是否有权限  2016-10-26  guofei.lian
	public void checkCardAuthInDept(){
		HttpServletRequest request=getHttpServletRequest();
		String object_nums=request.getParameter("object_nums");
		JsonResult result = new JsonResult();
		boolean flag=departmentGrantService.checkCardAuthInDept(object_nums);
		if(flag){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setMsg("所选部门中员工的卡没有权限，请重新选择源部门");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//复制组织权限 2016-10-11 guofei.lian
	public void copyDeptGrant(){
		HttpServletRequest request=getHttpServletRequest();
		String main_deptnums=request.getParameter("main_deptnums");
		String target_deptnums=request.getParameter("target_deptnums");
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=departmentGrantService.copyDeptGrant(main_deptnums,target_deptnums,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("复制成功");
		}else{
			result.setSuccess(false);
			result.setMsg("复制失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//查询部门权限任务表    2016-10-17  guofei.lian
	public void selectDeptAuthorityInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Grid grid=departmentGrantService.selectDeptAuthorityInfo(map,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//查询员工权限任务表  2016-10-19  guofei.lian
	public void selectStaffAuthorityInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Grid grid=departmentGrantService.selectStaffAuthorityInfo(map,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//查询部门权限 2016-10-19 guofei.lian
	public void selectDeptAuthorityForView(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Map map=GsonUtil.toBean(data, HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Grid grid=departmentGrantService.selectDeptAuthorityForView(map,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	//查询人员权限    2016-10-19  guofei.lian
	public void selectStaffAuthorityForView(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Map map=GsonUtil.toBean(data, HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Grid grid=departmentGrantService.selectStaffAuthorityForView(map,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//按条件下载人员权限  2016-10-19  guofei.lian
	public void downloadStaffAuthorityByTiaojian(){
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		try{
		List<Map> staffAuthorityList= departmentGrantService.searchStaffAuthority(map);
		for(Map map2:staffAuthorityList){
			if("00".equals(map2.get("taskState"))){
				map2.put("taskState", "待下载");
			}else if("01".equals(map2.get("taskState"))){
				map2.put("taskState", "下载成功");
			}else if("02".equals(map2.get("taskState"))){
				map2.put("taskState", "下载失败");
			}else if("10".equals(map2.get("taskState"))){
				map2.put("taskState", "待删除");
			}else if("12".equals(map2.get("taskState"))){
				map2.put("taskState", "删除失败");
			}
			
			if("0".equals(map2.get("object_type"))){
				map2.put("object_type", "组织权限");
			}else{
				map2.put("object_type", "人员权限");
			}
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(staffAuthorityList), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
		colTitleList.add("部门名称");
		colList.add("deptName");
		colTitleList.add("员工工号");
		colList.add("staffName");
		colTitleList.add("员工名称");
		colList.add("cardNum");
		colTitleList.add("卡号");
		colList.add("staffNo");
		colList.add("deviceName");
		colTitleList.add("设备名称");
		colList.add("deviceIp");
		colTitleList.add("设备Ip");
		colList.add("begin_valid_time");
		colTitleList.add("开始生效时间");
		colList.add("end_valid_time");
		colTitleList.add("结束生效时间");
		colList.add("object_type");
		colTitleList.add("权限类型");
		colList.add("taskState");
		colTitleList.add("权限状态");
		colList.add("floor_list");
		colTitleList.add("楼层");
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="人员权限表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("人员权限表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//按条件下载部门权限  2016-10-19  guofei.lian
	public void downloadDeptAuthorityByTiaojian(){
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		List<Map> deptAuthorityList= departmentGrantService.searchDeptAuthority(map);
		/*for(Map map2:deptAuthorityList){
			if("00".equals(map2.get("taskState"))){
				map2.put("taskState", "待下载");
			}else if("01".equals(map2.get("taskState"))){
				map2.put("taskState", "下载成功");
			}else if("02".equals(map2.get("taskState"))){
				map2.put("taskState", "下载失败");
			}else if("10".equals(map2.get("taskState"))){
				map2.put("taskState", "待删除");
			}else if("12".equals(map2.get("taskState"))){
				map2.put("taskState", "删除失败");
			}
			
		}*/
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(deptAuthorityList), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
	/*	colList.add("cardNum");
		colTitleList.add("卡号");*/
		colList.add("deptName");
		colTitleList.add("部门名称");
		colList.add("deviceName");
		colTitleList.add("设备名称");
		colList.add("deviceIp");
		colTitleList.add("设备Ip");
		colList.add("begin_valid_time");
		colTitleList.add("开始生效时间");
		colList.add("end_valid_time");
		colTitleList.add("结束生效时间");
		/*colList.add("taskState");
		colTitleList.add("权限状态");*/
		colList.add("floor_list");
		colTitleList.add("楼层");
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		try{
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="部门权限表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("部门权限表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
