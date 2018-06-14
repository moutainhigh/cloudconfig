package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.EmployeeSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.StaffGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.EmployeeSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.StaffGwService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("employeeSyncAction")
public class EmployeeSyncAction extends BaseActionSupport {
 
	@Resource(name = "employeeSyncServiceImpl")
	 private EmployeeSyncService employeeSyncService;
	
	@Resource(name = "staffGwServiceImpl")
	private StaffGwService staffGwService;
	
	@Resource(name = "employeeService")
	private  EmployeeService employeeService;
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	
	@Override
	public Object getModel() {
		return null;
	}
	/**
	 * 添加员工(供C/S端使用)
	 */
	public void addEmployeeSync(){
		
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String deptID = request.getParameter("deptID");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String workState = request.getParameter("workState");
		String birthday = request.getParameter("birthday"); //1998/6/6
		String workNo = request.getParameter("workNo"); //员工工号
		String mobile = request.getParameter("mobile");
		String IDNumber = request.getParameter("IDNumber"); //身份证号
		String address = request.getParameter("address");
		String description = request.getParameter("description");
		String photoPath = request.getParameter("photoPath");
		String staff_num=null;
		
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工ID不能为空");		
			}
			if(null==deptID||"".equals(deptID.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门ID不能为空");		
			}if(null==name||"".equals(name.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工姓名不能为空");		
			}if(null==workNo||"".equals(workNo.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工工号不能为空");		
			}if(null==workState||"".equals(workState.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工状态不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap));  
			return;
		}
		
		
		if(null!=id && !"".equals(id.trim())){
			String StaffNum=employeeSyncService.getStaffNumByID(id);
			if(null!=StaffNum){
				result.setMsg("已存在该ID的员工");
				result.setCode("2");
				resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
				printHttpServletResponse(GsonUtil.toJson(resultMap));
			    return;
			}
		}
		
		EmployeeSyncModel employeeSync=new EmployeeSyncModel();
		StaffGw staffGw=new StaffGw();
		
		if(null!=deptID && !"".equals(deptID.trim())){
			String deptNum=employeeSyncService.getDeptNum(deptID);
			if(deptNum==null){
				result.setMsg("没有存在该ID的部门");
				result.setCode("2");
				resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
				printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			    return;
			}
			
				employeeSync=employeeSyncService.defaultMC_DM(deptNum);//查到该部门的默认岗位的名称和代码
				if(employeeSync==null){
					result.setMsg("没有该部门的默认岗位");
					result.setCode("2");
					resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
					printHttpServletResponse(GsonUtil.toJson(resultMap)); 
				    return;
				}
				UUID uuid=UUID.randomUUID();
				staff_num=uuid.toString();
				employeeSync.setStaff_num(staff_num);
				employeeSync.setStaff_no(workNo); //员工工号
				employeeSync.setStaff_dept(deptNum);
				employeeSync.setRemote_staff_id(id);
				employeeSync.setStaff_name(name);
				employeeSync.setStaff_address(address);
				employeeSync.setPapers_type("100"); //身份证编号 100
				employeeSync.setPapers_num(IDNumber);
				employeeSync.setStaff_mobile(mobile);
				employeeSync.setStaff_img(photoPath);
				employeeSync.setStaff_remark(description);
				employeeSync.setStaff_position(employeeSync.getGw_mc());
				
				staffGw.setGw_dm(employeeSync.getGw_dm());
				staffGw.setYh_dm(staff_num);
				
		}
		//几个特殊的 1.性别 2.状态3.年龄
		if("男".equals(sex)||null==sex){ 
			employeeSync.setStaff_sex("1"); //默认为男
		}else if("女".equals(sex)){
			employeeSync.setStaff_sex("0");
		} 
			// C/S端 0在职  1离职  ,本地端 员工状态,1 正常 2 冻结
		if("0".equals(workState)||"".equals(workState)){
			employeeSync.setStaff_state("1");  //默认为正常
		}else if("1".equals(workState)){
			employeeSync.setStaff_state("2");
		}
		
		//根据用户生日计算年龄
		if(null!=birthday && !"".equals(birthday.trim())){
			 SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd");
			 try {
				Date birth=formatter.parse(birthday);
				int age=getAgeByBirthday(birth);
				employeeSync.setStaff_age(String.valueOf(age));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		 try {
			   int resultCode= employeeSyncService.addEmployeeSync(employeeSync);
			 	if(resultCode==1){
			 		
			 		//继承部门排班 
			        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			 		peopleAuthorityInfoService.makeDutyByDeptNum(employeeSync.getStaff_dept(), employeeSync.getStaff_num(), date);
			 		
			 		staffGwService.delStaffGw(staff_num);  //先删除kc_staff_gw中记录，再添加新纪录
			 		staffGwService.addStaffGw(staffGw);
			 		String create_time=DateUtil.getSysDateTime();
			 		employeeService.addAccountToEmployee(staff_num, create_time, "CS_Admin");//添加员工同时添加默认账户
			 		result.setMsg("添加成功");
			 		result.setCode("0");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}else{
			 		result.setMsg("该员工工号存在，请更改员工工号");
			 		result.setCode("3");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}
	        } catch (Exception e) {
	        	result.setMsg("添加失败");
	        	result.setCode("1");
	        	resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
	        }
	        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	
	/**
	 * 根据id修改员工(供C/S端使用)
	 */
	public void modifyEmployeeSync(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String deptID = request.getParameter("deptID");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String workState = request.getParameter("workState");
		String birthday = request.getParameter("birthday"); //1998/6/6
		String workNo = request.getParameter("workNo");     //员工工号
		String mobile = request.getParameter("mobile");
		String IDNumber = request.getParameter("IDNumber"); //身份证号
		String address = request.getParameter("address");
		String description = request.getParameter("description");
		String photoPath = request.getParameter("photoPath");
		
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工ID不能为空");		
			}
			if(null==deptID||"".equals(deptID.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门ID不能为空");		
			}if(null==name||"".equals(name.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工姓名不能为空");		
			}if(null==workNo||"".equals(workNo.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工工号不能为空");		
			}if(null==workState||"".equals(workState.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工状态不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap));   
			return;
		}
		EmployeeSyncModel employeeSync=employeeSyncService.getDeptNumByEmpID(id);//根据员工id，查找员工编号和原本所属部门代码
		StaffGw staffGw=new StaffGw();
		
		String oldDeptNum=employeeSync.getStaff_dept();//原本所属部门代码
		String deptNum=employeeSyncService.getDeptNum(deptID);
		if(deptNum==null){
			result.setMsg("没有存在该ID的部门");
			result.setCode("2");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
		    return;
		}else if(!deptNum.equals(oldDeptNum)){ 
			//新旧部门代码不相等，表示修改了部门，则员工默认岗位也必须修改
			employeeSync.setStaff_dept(deptNum);
			EmployeeSyncModel empModel=employeeSyncService.defaultMC_DM(deptNum);//查到该部门的默认岗位的名称和代码
			if(empModel==null){
				result.setMsg("没有该部门的默认岗位");
				result.setCode("2");
				resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
				printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			    return;
			}else{
				staffGw.setGw_dm(empModel.getGw_dm());
				staffGw.setYh_dm(employeeSync.getStaff_num());
			}
			
			// 员工换部门，更换排班
			try {
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				peopleAuthorityInfoService.chgDutyOnDeptChg(deptNum,oldDeptNum,employeeSync.getStaff_num(),date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 employeeSync.setRemote_staff_id(id);
		 employeeSync.setStaff_no(workNo);
		 employeeSync.setStaff_name(name);
		 employeeSync.setStaff_address(address);
		 employeeSync.setPapers_num(IDNumber);
		 employeeSync.setStaff_mobile(mobile);
		 employeeSync.setStaff_img(photoPath);
		 employeeSync.setStaff_remark(description);
		//几个特殊的 1.性别 2.状态3.年龄
		 if("男".equals(sex)||null==sex){ 
				employeeSync.setStaff_sex("1"); //默认为男
			}else if("女".equals(sex)){
				employeeSync.setStaff_sex("0");
			} 
				// C/S端 0在职  1离职  ,本地端 员工状态,1 正常 2 冻结
		if("0".equals(workState)||null==workState){
			employeeSync.setStaff_state("1");  //默认为正常
		}else if("1".equals(workState)){
			employeeSync.setStaff_state("2");
		}
		if(null!=birthday && !"".equals(birthday.trim())){
			//根据用户生日计算年龄
			 SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd");
			 try {
				Date birth=formatter.parse(birthday);
				int age=getAgeByBirthday(birth);
				employeeSync.setStaff_age(String.valueOf(age));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		 try {
			 int resultCode= employeeSyncService.modifyEmployeeSync(employeeSync);
			 	if(resultCode==1){
			 		if(!deptNum.equals(oldDeptNum)){
			 			staffGwService.delStaffGw(staffGw.getYh_dm());  //先删除kc_staff_gw中记录，再添加新纪录
				 		staffGwService.addStaffGw(staffGw);
			 		}
			 		result.setMsg("修改成功");
			 		result.setCode("0");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}else{
			 		result.setMsg("该员工工号存在，请更改员工工号");
			 		result.setCode("3");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}
	        } catch (Exception e) {
	        	result.setMsg("修改失败");
	        	result.setCode("1");
	        	resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
	        }
	        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
		
	/**
	 * 根据员工id删除员工信息(供C/S端使用)
	 * 单删除
	 */
	public void delEmployeeSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		
		String del_ids = request.getParameter("id");
		try {
			if(null==del_ids||"".equals(del_ids.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("员工id不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
	    try {
	    	String staffNum=employeeSyncService.getStaffNumByID(del_ids);
	    	if(staffNum!=null){
	    		staffGwService.delStaffGw(staffNum); //根据员工编号删除员工岗位
	    	}
	    	employeeSyncService.delEmployeeSync(del_ids);//伪删除员工
	    	result.setMsg("删除成功");
	    	result.setCode("0");
	    	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        } catch (Exception e) {
        	result.setMsg("删除失败");
        	result.setCode("1");
        	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        }
        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
		
	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();  //01 18:53:09 EEST 2012

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth 
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return age;
	}
		
		
	
	
	
	
	
}
