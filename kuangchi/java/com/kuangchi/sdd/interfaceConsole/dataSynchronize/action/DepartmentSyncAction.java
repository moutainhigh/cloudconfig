package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DepartmentSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.DepartmentSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.DeptGwService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.EmployeeSyncService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Controller("departmentSyncAction")
public class DepartmentSyncAction extends BaseActionSupport {
 
	@Resource(name = "departmentSyncServiceImpl")
	 private DepartmentSyncService departmentSyncService;
	@Resource(name = "deptGwServiceImpl")
	private DeptGwService deptGwService;
	@Resource(name = "employeeSyncServiceImpl")
	 private EmployeeSyncService employeeSyncService;
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 添加部门(供C/S端使用)
	 */
	public void addDepartmentSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("id");
		String parentID=request.getParameter("parentID");
		String code=request.getParameter("code"); //部门编号
		String name=request.getParameter("name");
		String description=request.getParameter("description");
		String bm_dm=null;
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("id不能为空");		
			}
			if(null==parentID||"".equals(parentID.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("父ID不能为空");		
			}if(null==code||"".equals(code.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门编号不能为空");		
			}if(null==name||"".equals(name.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门名称不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			//result.setSuccess(false);
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
		DepartmentSyncModel departmentSync=new DepartmentSyncModel();
		departmentSync.setBm_no(code);
		departmentSync.setBm_mc(name);
		departmentSync.setRemote_department_id(id);
		departmentSync.setBz(description);
		
		UUID uuid=UUID.randomUUID();
		bm_dm=uuid.toString();
		departmentSync.setBm_dm(bm_dm);
		
		if(null!=id && !"".equals(id.trim())){
			String deptNum=departmentSyncService.getDeptNum(id);
			if(null!=deptNum){
				result.setMsg("已存在该ID的部门");
				result.setCode("2");
				resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
				printHttpServletResponse(GsonUtil.toJson(resultMap));
			    return;
			}
		}
		
		if(null!=parentID && !"".equals(parentID.trim())){
			if(Integer.valueOf(parentID)==0){
				departmentSync.setRemote_parentId("0");
				departmentSync.setSjbm_dm("1");
			}else{
				
				String parentDM =departmentSyncService.getParentDM(parentID);
				if(parentDM==null){
					result.setMsg("没有存在该父级ID的部门");
					result.setCode("2");
					resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
					printHttpServletResponse(GsonUtil.toJson(resultMap));
				    return;
				}else{
					departmentSync.setRemote_parentId(parentID);
					departmentSync.setSjbm_dm(parentDM);
				}
			}
		}
    	
	    departmentSync.setLr_sj(getSysTimestamp()); //自己创建
	    departmentSync.setUuid(UUIDUtil.uuidStr());
		departmentSync.setZf_bj(GlobalConstant.ZF_BJ_N);
		//添加部门时，同时添加部门默认岗位
		DeptGw deptGw=new DeptGw();
		deptGw.setBm_dm(bm_dm);
		deptGw.setGw_mc("员工");
		deptGw.setGw_dm(bm_dm+"YG");
	    try {
	    	 int resultCode= departmentSyncService.addDepartmentSync(departmentSync);
			 	if(resultCode==1){
			 		deptGwService.addDeptGw(deptGw);
			 		result.setMsg("添加成功");
			 		result.setCode("0");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}else{
			 		result.setMsg("该部门编号已经存在，请更改部门编号");
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
	 * 根据部门id修改部门信息(供C/S端使用)
	 */
	public void modifyDepartmentSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		
		String id=request.getParameter("id");
		String code=request.getParameter("code");
		String name=request.getParameter("name");
		String parentID=request.getParameter("parentID");
		String description=request.getParameter("description");
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门id不能为空");		
			}if(null==name||"".equals(name.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门名称不能为空");		
			}if(null==code||"".equals(code.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门编号不能为空");		
			}if(null==parentID||"".equals(parentID.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("父级部门ID不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setCode("1");
			result.setMsg(myException.getMessage());
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap));
			return;
		}
		DepartmentSyncModel departmentSync=new DepartmentSyncModel();
		departmentSync.setRemote_department_id(id);
		departmentSync.setBm_no(code);
		departmentSync.setBm_mc(name);
		departmentSync.setBz(description);
		
		if(null!=parentID && !"".equals(parentID.trim())){
			if(Integer.valueOf(parentID)==0){
				departmentSync.setRemote_parentId("0");
				departmentSync.setSjbm_dm("1");
			}else{
				String parentDM =departmentSyncService.getParentDM(parentID);
				if(parentDM==null){
					result.setMsg("没有存在该父级ID的部门");
					result.setCode("2");
					resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
					printHttpServletResponse(GsonUtil.toJson(resultMap));
				    return;
				}else{
					departmentSync.setRemote_parentId(parentID);
					departmentSync.setSjbm_dm(parentDM);
				}
			}
		}
	 try {
	    	 int resultCode= departmentSyncService.modifyDepartmentSync(departmentSync);
			 	if(resultCode==1){
			 		result.setMsg("修改成功");
			 		result.setCode("0");
			 		resultMap.put("msg",result.getMsg());
					resultMap.put("code", result.getCode());
			 	}else{
			 		result.setMsg("该部门编号已经存在，请更改部门编号");
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
	 * 根据部门id删除部门信息(供C/S端使用)
	 * (单删除)
	 */
	public void delDepartmentSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		String del_id = request.getParameter("id");
		try {
			if(null==del_id||"".equals(del_id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("部门id不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setCode("1");
			result.setMsg(myException.getMessage());
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
	    try {
	    	String deptNum=departmentSyncService.getDeptNum(del_id);
	    	departmentSyncService.delDepartmentSync(del_id);  //伪删除部门
	    	deptGwService.delDeptGw(deptNum); //根据部门代码真删除部门岗位
	    	employeeSyncService.delEmpFromDeptNum(deptNum);	//伪删除部门下的员工,CS端要求有人时部门不能删除，所以这步不用也可以
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
	
	
}
