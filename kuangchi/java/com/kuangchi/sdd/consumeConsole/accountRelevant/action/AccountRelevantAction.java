package com.kuangchi.sdd.consumeConsole.accountRelevant.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.util.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.accountRelevant.service.AccountRelevantService;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("accountRelevantAction")
public class AccountRelevantAction extends BaseActionSupport{

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Autowired 
	private AccountRelevantService accountRelevantService;

	/**
	 * 查询账户类型
	 * by gengji.yang
	 */
	public void getAccountTypeByMap(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=accountRelevantService.getAccountTypeByMap(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 新增账户类型
	 * by gengji.yang
	 */
	public void addAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		map.put("createUser",loginUser.getYhMc());
		JsonResult result=new JsonResult();
		map.put("regulationName","".equals(map.get("regulationName"))?null:Integer.parseInt((String)map.get("regulationName")));
		try{
			accountRelevantService.addAccountType(map);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除 账户类型
	 * by gengji.yang
	 */
	public void deleteType(){
		HttpServletRequest request=getHttpServletRequest();
		String nums=request.getParameter("nums");
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.deleteAccountType(nums);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 判断是否已经使用了账户类型
	 * 用于删除账户类型前的判断
	 * by gengji.yang
	 */
	public void ifAlreadyUseAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		String typeNum=request.getParameter("typeNum");
		JsonResult result=new JsonResult();
		result.setSuccess(accountRelevantService.ifExistUsingAccountType(typeNum));
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取账户类型Map
	 * by gengji.yang
	 */
	public void getAccountTypeMap(){
		HttpServletRequest request=getHttpServletRequest();
		String typeNum=request.getParameter("typeNum");
		Map map=accountRelevantService.getAccountTypeMap(typeNum);
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * 更新账户类型
	 * by gengji.yang
	 */
	public void updateAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		map.put("createUser",loginUser.getYhMc());
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.updateAccountType(map);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取所有的账户类型
	 * @author minting.he
	 */
	public void getAllAccountType(){
		List<DeviceType> list = accountRelevantService.getAllAccoutType();
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化账户类型下拉列表
	 * by gengji.yang
	 */
	public void initType(){
		List<Map> list=accountRelevantService.initType();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化规则定义下拉框
	 * by gengji.yang
	 */
	public void initRegularNames(){
		List<Map> list=accountRelevantService.getRegulationNames();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化岗位下拉框
	 * by gengji.yang
	 */
	public void getPositions(){
		List<Map> list=accountRelevantService.getPositions();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化员工姓名下拉框
	 * by gengji.yang
	 */
	public void getStaffNames(){
		List<Map> list=accountRelevantService.getStaffNames();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化性别下拉框
	 * by gengji.yang
	 */
	public void getStaffSex(){
		List<Map> list=new ArrayList();
		Map m=new HashMap();
		m.put("valueField","0");
		m.put("textField", "女");
		Map m1=new HashMap();
		m1.put("valueField","1");
		m1.put("textField", "男");
		list.add(m);
		list.add(m1);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 初始化部门下拉框
	 * by gengji.yang
	 */
	public void getStaffDepts(){
		List<Map> list=accountRelevantService.getStaffDepts();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 保存规则 
	 * by gengji.yang
	 */
	public void submitRegulations(){
		HttpServletRequest request=getHttpServletRequest();
		String description=request.getParameter("description");
		String regulationName=request.getParameter("regulationName");
		ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) GsonUtil.getListFromJson(request.getParameter("data"),ArrayList.class);
		String staffNumStr=" staff_num in (";
		String staffDeptStr=" staff_dept in (";
		String  staffPositionStr=" staff_num in (select yh_dm from kc_staff_gw where gw_dm in (";
		String staffHiredateStr=" 1=1 and ";
		String staffSexStr=" staff_sex in (";
		String endStr="'')";
		String conditionStr=" 1=1 and ";
		String conditionEndStr=" 1=1";
		
		for(int i=0;i<list.size();i++){
			String str=(String) list.get(i).get("condition");
			if(str.indexOf("&gt;")!=-1){
				str=str.replace("&gt;",">");
			}else if(str.indexOf("&lt;")!=-1){
				str=str.replace("&lt;","<");
			}
			String[] strArr=str.split(" ");
			if(strArr[0].equals("staff_num")){
				staffNumStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_dept")){
					staffDeptStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_position")){
					staffPositionStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_hiredate")){
					staffHiredateStr+=strArr[0]+strArr[1]+"'"+strArr[strArr.length-1]+"' and ";
				}else if(strArr[0].equals("staff_sex")){
					staffSexStr+="'"+strArr[strArr.length-1]+"',";
				}
		}
		staffNumStr+=endStr;
		staffDeptStr+=endStr;
		staffPositionStr+=endStr;
		staffHiredateStr+=conditionEndStr;
		staffSexStr+=endStr;
		if(staffNumStr.indexOf(",")!=-1){
			conditionStr+=staffNumStr+" and ";
		}
		if(staffDeptStr.indexOf(",")!=-1){
			conditionStr+=staffDeptStr+" and ";
		}
		if(staffPositionStr.indexOf(",")!=-1){
			conditionStr+=staffPositionStr+" and ";
		}
		if(staffHiredateStr.indexOf("staff_hiredate")!=-1){
			conditionStr+=staffHiredateStr+" and ";
		}
		if(staffSexStr.indexOf(",")!=-1){
			conditionStr+=staffSexStr+" and ";
		}
		if(conditionStr.indexOf("gw_dm")!=-1){
			conditionStr+=conditionEndStr+")";
		}else{
			conditionStr+=conditionEndStr;
		}
		
		Map m=new HashMap();
		m.put("condition", "\""+conditionStr+"\"");
		m.put("description","\""+description+"\"");
		m.put("regulationName",regulationName);
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.addRegulation(m);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
/*	public void submitRegulations(){
		HttpServletRequest request=getHttpServletRequest();
		String description=request.getParameter("description");
		String regulationName=request.getParameter("regulationName");
		ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) GsonUtil.getListFromJson(request.getParameter("data"),ArrayList.class);
		String staffNumStr=" staff_num in (";
		String staffDeptStr=" staff_dept in (";
		String  staffPositionStr=" staff_position in (";
		String staffHiredateStr=" 1=0 or ";
		String staffSexStr=" staff_sex in (";
		String endStr="'')";
		String conditionStr="1=0 or ";
		String conditionEndStr=" 1=0";
		
		for(int i=0;i<list.size();i++){
			String str=(String) list.get(i).get("condition");
			if(str.indexOf("&gt;")!=-1){
				str=str.replace("&gt;",">");
			}else if(str.indexOf("&lt;")!=-1){
				str=str.replace("&lt;","<");
			}
			String[] strArr=str.split(" ");
			if(strArr[0].equals("staff_num")){
				staffNumStr+="'"+strArr[strArr.length-1]+"',";
			}else if(strArr[0].equals("staff_dept")){
				staffDeptStr+="'"+strArr[strArr.length-1]+"',";
			}else if(strArr[0].equals("staff_position")){
				staffPositionStr+="'"+strArr[strArr.length-1]+"',";
			}else if(strArr[0].equals("staff_hiredate")){
				staffHiredateStr+=strArr[0]+strArr[1]+"'"+strArr[strArr.length-1]+"' or ";
			}else if(strArr[0].equals("staff_sex")){
				staffSexStr+="'"+strArr[strArr.length-1]+"',";
			}
		}
		staffNumStr+=endStr;
		staffDeptStr+=endStr;
		staffPositionStr+=endStr;
		staffHiredateStr+=conditionEndStr;
		staffSexStr+=endStr;
		if(staffNumStr.indexOf(",")!=-1){
			conditionStr+=staffNumStr+" or ";
		}
		if(staffDeptStr.indexOf(",")!=-1){
			conditionStr+=staffDeptStr+" or ";
		}
		if(staffPositionStr.indexOf(",")!=-1){
			conditionStr+=staffPositionStr+" or ";
		}
		if(staffHiredateStr.indexOf("staff_hiredate")!=-1){
			conditionStr+=staffHiredateStr+" or ";
		}
		if(staffSexStr.indexOf(",")!=-1){
			conditionStr+=staffSexStr+" or ";
		}
		conditionStr+=conditionEndStr;
		
		Map m=new HashMap();
		m.put("condition", "\""+conditionStr+"\"");
		m.put("description","\""+description+"\"");
		m.put("regulationName",regulationName);
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.addRegulation(m);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
*/	
	/**
	 * 查询规则
	 * by gengji.yang
	 */
	public void getRegulations(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid<Map> list=accountRelevantService.getAllRegulations(map);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 删除规则
	 * by gengji.yang
	 */
	public void delRegulation(){
		HttpServletRequest request=getHttpServletRequest();
		String id=request.getParameter("id");
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.delRegulation(id);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 判断账户类型是否已存在规则
	 * by gengji.yang
	 */
	public void ifExistRegulation(){
		HttpServletRequest request=getHttpServletRequest();
		String accountTypeNum=request.getParameter("accountTypeNum");
		JsonResult result=new JsonResult();
		result.setSuccess(accountRelevantService.ifExistRegulation(accountTypeNum));
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 更新账户类型规则
	 * by gengji.yang
	 */
	public void submitUpdateRegulations(){

		HttpServletRequest request=getHttpServletRequest();
		String description=request.getParameter("description");
		String regulationName=request.getParameter("regulationName");
		ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) GsonUtil.getListFromJson(request.getParameter("data"),ArrayList.class);
		String staffNumStr=" staff_num in (";
		String staffDeptStr=" staff_dept in (";
		String  staffPositionStr=" staff_position in (";
		String staffHiredateStr=" 1=0 or ";
		String staffSexStr=" staff_sex in (";
		String endStr="'')";
		String conditionStr="1=0 or ";
		String conditionEndStr=" 1=0";
		
		for(int i=0;i<list.size();i++){
			String str=(String) list.get(i).get("condition");
			if(str.indexOf("&gt;")!=-1){
				str=str.replace("&gt;",">");
			}else if(str.indexOf("&lt;")!=-1){
				str=str.replace("&lt;","<");
			}
			String[] strArr=str.split(" ");
			if(strArr[0].equals("staff_num")){
				staffNumStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_dept")){
					staffDeptStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_position")){
					staffPositionStr+="'"+strArr[strArr.length-1]+"',";
				}else if(strArr[0].equals("staff_hiredate")){
					staffHiredateStr+=strArr[0]+strArr[1]+"'"+strArr[strArr.length-1]+"' or ";
				}else if(strArr[0].equals("staff_sex")){
					staffSexStr+="'"+strArr[strArr.length-1]+"',";
				}
		}
		staffNumStr+=endStr;
		staffDeptStr+=endStr;
		staffPositionStr+=endStr;
		staffHiredateStr+=conditionEndStr;
		staffSexStr+=endStr;
		if(staffNumStr.indexOf(",")!=-1){
			conditionStr+=staffNumStr+" or ";
		}
		if(staffDeptStr.indexOf(",")!=-1){
			conditionStr+=staffDeptStr+" or ";
		}
		if(staffPositionStr.indexOf(",")!=-1){
			conditionStr+=staffPositionStr+" or ";
		}
		if(staffHiredateStr.indexOf("staff_hiredate")!=-1){
			conditionStr+=staffHiredateStr+" or ";
		}
		if(staffSexStr.indexOf(",")!=-1){
			conditionStr+=staffSexStr+" or ";
		}
		conditionStr+=conditionEndStr;
		
		Map m=new HashMap();
		m.put("condition", "\""+conditionStr+"\"");
		m.put("description","\""+description+"\"");
		m.put("regulationName",regulationName);
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.updateRegulation(m);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 初始化规则下拉框，用于账户类型的新增和修改
	 * by gengji.yang
	 */
	public void initRegulation(){
		List<Map> list=accountRelevantService.initRegulation();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 根据账户类型充值
	 * by gengji.yang
	 */
	public  void rechargeByAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String money=request.getParameter("money");
		String regulationId=request.getParameter("regulationId");
		Map recordMap=accountRelevantService.isEnoughByAccountType(accountTypeNum, money,regulationId);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByAccountType(accountTypeNum, money,regulationId,loginUser.getYhMc());
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleRecharge();
				}
			}).start();
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 查询所有的账户流水
	 * by gengji.yang
	 */
	public void getAllAccountFlow(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=accountRelevantService.getAllAccountFlow(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 根据员工编号充值
	 * by gengji.yang
	 */
	public  void rechargeByStaffNum(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String staffNum=request.getParameter("staffNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByStaffNum(staffNum, accountTypeNum, money);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByStaffNum(staffNum, accountTypeNum, money, loginUser.getYhMc());
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleRecharge();
				}
			}).start();
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 根据部门编号 充值
	 * by gengji.yang
	 */
	public  void rechargeByDeptNum(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String deptNum=request.getParameter("deptNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByDeptNum(deptNum, accountTypeNum, money);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByDeptNum(deptNum, accountTypeNum, money, loginUser.getYhMc());
			
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleRecharge();
				}
			}).start();
			
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 处理recordMap的组件
	 * by gengji.yang
	 */
	public JsonResult handleRecordMap(Map recordMap){
		JsonResult result=new JsonResult();
		String msg="";
		boolean flag=true;
		boolean isSuccess=true;
		for(int i=0;i<recordMap.keySet().size()/4;i++){
			if(recordMap.get("exception"+i).toString().length()>0){
				flag=false;
				isSuccess=false;
				msg+=recordMap.get("exception"+i).toString();
			}
		}
		if(flag){
			for(int i=0;i<recordMap.keySet().size()/4;i++){
				if(recordMap.get("message"+i).toString().length()>0){
					isSuccess=false;
					msg+=recordMap.get("message"+i).toString()+"; ";
				}
			}
		}
		result.setSuccess(isSuccess);
		result.setMsg(msg);
		return result;
	}
	
	
	/**
	 * 根据账户类型充值，资金池是否够钱
	 * by gengji.yang
	 */
	public void isEnoughByAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		String accountTypeNum=request.getParameter("accountTypeNum");
		String money=request.getParameter("money");
		String regulationId=request.getParameter("regulationId");
		Map recordMap=accountRelevantService.isEnoughByAccountType(accountTypeNum, money,regulationId);
		printHttpServletResponse(GsonUtil.toJson(handleRecordMap(recordMap)));
	}
	
	/**
	 * 根据部门编号充值，资金池是否够钱
	 * by gengji.yang
	 */
	public void isEnoughByDeptNum(){
		HttpServletRequest request=getHttpServletRequest();
		String accountTypeNum=request.getParameter("accountTypeNum");
		String deptNum=request.getParameter("deptNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByDeptNum(deptNum, accountTypeNum, money);
		printHttpServletResponse(GsonUtil.toJson(handleRecordMap(recordMap)));
	}
	
	/**
	 * 根据员工编号充值，资金池是否够钱
	 * by gengji.yang
	 */
	public void isEnoughByStaffNum(){
		HttpServletRequest request=getHttpServletRequest();
		String accountTypeNum=request.getParameter("accountTypeNum");
		String staffNum=request.getParameter("staffNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByStaffNum(staffNum, accountTypeNum, money);
		printHttpServletResponse(GsonUtil.toJson(handleRecordMap(recordMap)));
	}
	
	/**
	 * 根据员工编号补助
	 * 补助='2'
	 * by gengji.yang
	 */
	public  void subsidizeByStaffNum(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String staffNum=request.getParameter("staffNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByStaffNum(staffNum, accountTypeNum, money);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByStaffNum(staffNum, accountTypeNum, money, loginUser.getYhMc(),"2");
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleBZ();
				}
			}).start();
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 根据部门编号补助
	 * by gengji.yang
	 */
	public  void subsidizeByDeptNum(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String deptNum=request.getParameter("deptNum");
		String money=request.getParameter("money");
		Map recordMap=accountRelevantService.isEnoughByDeptNum(deptNum, accountTypeNum, money);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByDeptNum(deptNum, accountTypeNum, money, loginUser.getYhMc(),"2");
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleBZ();
				}
			}).start();
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 根据账户类型补助
	 * by gengji.yang
	 */
	public  void subsidizeByAccountType(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String accountTypeNum=request.getParameter("accountTypeNum");
		String money=request.getParameter("money");
		String regulationId=request.getParameter("regulationId");
		Map recordMap=accountRelevantService.isEnoughByAccountType(accountTypeNum, money,regulationId);
		JsonResult valid=handleRecordMap(recordMap);
		if(valid.isSuccess()){
			boolean flag=accountRelevantService.rechargeByAccountType(accountTypeNum, money,regulationId,loginUser.getYhMc(),"2");
			new Thread(new Runnable() {
				public void run() {
					accountRelevantService.handleBZ();
				}
			}).start();
			JsonResult result=new JsonResult();
			result.setSuccess(flag);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 查看某个规则
	 * by gengji.yang
	 */
	public void viewRegulation(){
		HttpServletRequest request=getHttpServletRequest();
		String id=request.getParameter("id");
		Map map=accountRelevantService.viewRegulation(id);
		String str=map.get("description").toString().replace("&gt;", ">");
		str=str.replace("&lt;","<");
		map.put("description", str);
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * 账户查询
	 * by gengji.yang
	 */
	public void searchAccount(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=accountRelevantService.searchAccount(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 销户前判断，资金池是否有足够的资金用来销户后退款
	 * by gengji.yang
	 */
	public void isEnoughToCloseAccount(){
		HttpServletRequest request=getHttpServletRequest();
		String staffNum=request.getParameter("staffNum");
		String accountTypeNum=request.getParameter("accountTypeNum");
		String accountNum=request.getParameter("accountNum");
		Map recordMap=accountRelevantService.isEnoughToCloseAccount(staffNum, accountTypeNum, accountNum);
		printHttpServletResponse(GsonUtil.toJson(handleRecordMap(recordMap)));
	}
	
	/**
	 * 销户
	 * by gengji.yang
	 */
	public void closeAccount(){
		HttpServletRequest request=getHttpServletRequest();
		User loginUser=(User)getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String staffNum=request.getParameter("staffNum");
		String accountTypeNum=request.getParameter("accountTypeNum");
		String accountNum=request.getParameter("accountNum");
		boolean flag=accountRelevantService.toCloseAccount(staffNum, accountTypeNum, accountNum, loginUser.getYhMc());
		JsonResult result=new JsonResult();
		result.setSuccess(flag);
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 销户查询
	 * by gengji.yang
	 */
	public void searchClosedAccount(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=accountRelevantService.searchClosedAccount(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 冻结账户
	 * by gengji.yang
	 */
	public void frozenAccount(){
		HttpServletRequest request=getHttpServletRequest();
		String accountNum=request.getParameter("accountNum");
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.frozenAccount(accountNum);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 解冻账户
	 * by gengji.yang
	 */
	public void reFrozenAccount(){
		HttpServletRequest request=getHttpServletRequest();
		String accountNum=request.getParameter("accountNum");
		JsonResult result=new JsonResult();
		try{
			accountRelevantService.reFrozenAccount(accountNum);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取部门列表
	 * by gengji.yang
	 */
	public void getDeptList(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=accountRelevantService.getDeptList(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 判断是否存在账户类型名字
	 * by gengji.yang
	 */
	public void checkAccTypeName(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		String accountTypeName=request.getParameter("accountTypeName");
		result.setSuccess(accountRelevantService.checkAccTypeName(accountTypeName));
		printHttpServletResponse(GsonUtil.toJson(result));
	}
}
