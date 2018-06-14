package com.kuangchi.sdd.consumeConsole.fundPool.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.fundPool.model.FundPoolModel;
import com.kuangchi.sdd.consumeConsole.fundPool.model.JsonTreeResult;
import com.kuangchi.sdd.consumeConsole.fundPool.service.IFundPoolService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-16 下午2:16:39
 * @功能描述: 机构资金池模块-action
 * 
 */
@Controller("fundPoolAction")
public class FundPoolAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "fundPoolServiceImpl")
	private IFundPoolService fundPoolService;
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 上午10:53:04
	 * @功能描述: 跳转到企业资金池主页面
	 * @参数描述:
	 */
	public String toFundPoolPage(){
		return "success";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:11:30
	 * @功能描述: 根据参数查询资金池[分页]
	 * @参数描述:
	 */
	public void getFundPoolByParamPage(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		FundPoolModel fundPool = GsonUtil.toBean(data, FundPoolModel.class);
		String organiztion_name = fundPool.getOrganiztion_name();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organiztion_name", organiztion_name);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<FundPoolModel> fundPoolList =  fundPoolService.getFundPoolByParamPage(map);
		Integer fundPoolCount = fundPoolService.getFundPoolByParamCount(map);
		
		Grid<FundPoolModel> grid = new Grid<FundPoolModel>();
		grid.setRows(fundPoolList);
		grid.setTotal(fundPoolCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:16:13
	 * @功能描述: 新增机构资金池
	 * @参数描述:
	 */
	public void addFundPool(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		FundPoolModel fundPoolModel = GsonUtil.toBean(data, FundPoolModel.class);
		
		boolean addResult = fundPoolService.addFundPool(fundPoolModel, loginUserName);
		
		if(addResult){
			result.setSuccess(true);
			result.setMsg("新增成功");
		} else {
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午4:16:25
	 * @功能描述: 充值
	 * @参数描述:
	 */
	public void recharge(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String loginUserName = loginUser.getYhMc();
		String data = request.getParameter("data");
		FundPoolModel fundPoolModel = GsonUtil.toBean(data, FundPoolModel.class);
		
		boolean rechargeResult = fundPoolService.recharge(fundPoolModel, loginUserName);

		if(rechargeResult){
			result.setSuccess(true);
			result.setMsg("充值成功");
		} else {
			result.setSuccess(false);
			result.setMsg("充值失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午3:17:44
	 * @功能描述: 冻结资金池
	 * @参数描述:
	 */
	public void freezeFundPool(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String ids = request.getParameter("ids");
		
		boolean freezeResult =fundPoolService.freezeFundPool(ids, loginUserName);
		if(freezeResult){
			result.setSuccess(true);
			result.setMsg("冻结成功");
		} else {
			result.setSuccess(false);
			result.setMsg("冻结失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午3:17:44
	 * @功能描述: 解冻资金池
	 * @参数描述:
	 */
	public void unfreezeFundPool(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String loginUserName = loginUser.getYhMc();
		String ids = request.getParameter("ids");
		
		boolean unfreezeResult =fundPoolService.unfreezeFundPool(ids, loginUserName);
		if(unfreezeResult){
			result.setSuccess(true);
			result.setMsg("解冻成功");
		} else {
			result.setSuccess(false);
			result.setMsg("解冻失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:38:46
	 * @功能描述: 根据编号查询资金池（判断该机构编号是否已存在）
	 * @参数描述:
	 */
	public void getFundPoolByNum(){
		HttpServletRequest request = getHttpServletRequest();
		
		String organiztion_num = request.getParameter("organiztion_num");
		List<FundPoolModel> fundPoolList = fundPoolService.getFundPoolByNum(organiztion_num);
		
		if(fundPoolList != null && fundPoolList.size() != 0){
			printHttpServletResponse(new Gson().toJson(1));
		} else {
			printHttpServletResponse(new Gson().toJson(0));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-16 下午2:38:46
	 * @功能描述: 根据ID查询资金池
	 * @参数描述:
	 */
	public void getFundPoolById(){
		HttpServletRequest request = getHttpServletRequest();
		
		String id = request.getParameter("id");
		List<FundPoolModel> fundPoolList = fundPoolService.getFundPoolById(id);
		
		printHttpServletResponse(new Gson().toJson(fundPoolList.get(0)));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-3 下午5:06:28
	 * @功能描述:初始化部门树 
	 * @参数描述:
	 */
	public void initDeptTree(){
		HttpServletRequest request = getHttpServletRequest();
		String organiztion_num = request.getParameter("organiztion_num");
		JsonTreeResult jsonTreeResult  = new JsonTreeResult();
		
		
        
        // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*Map<String, Object> map = new HashMap<String, Object>();
		
		boolean isLayer = roleService.isLayer();
		if(isLayer){
 			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			map.put("jsDm", role.getJsDm());
 			
 			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			map.put("yhDm", user.getYhDm());
 		} else {
 			map.put("jsDm", "0");
 		}
 		
 		User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
        map.put("yhDm", user.getYhDm());*/
        
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		
 		jsonTreeResult = departmentService.getDeptTree(organiztion_num,layerDeptNum);
		StringBuilder build = new StringBuilder();
		build.append("[");
		build.append(jsonTreeResult.getTree());
		build.append("]");
		printHttpServletResponse(GsonUtil.toJson(jsonTreeResult));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 下午5:53:19
	 * @功能描述:根据资金池查看绑定的部门树 
	 * @参数描述:
	 */
	public void getBundDeptTree(){
		HttpServletRequest request = getHttpServletRequest();
		String organiztion_num = request.getParameter("organiztion_num");
		Tree tree = departmentService.getMyDeptTree(organiztion_num);
		StringBuilder build = new StringBuilder();
		build.append("[");
		build.append(GsonUtil.toJson(tree));
		build.append("]");
		printHttpServletResponse(build);
	}
   
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-5 下午9:34:34
	 * @功能描述:校验部门是否绑定资金池 
	 * @参数描述:
	 */
	public void checkSelectDeptIsBand(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		String[] bmDms = request.getParameter("bmDms").split(",");
		for(String bmDm:bmDms){
			Integer count = departmentService.checkSelectDeptIsBand(bmDm);
			if(count>0){
				jsonResult.setMsg("该部门已绑定资金池");
				jsonResult.setSuccess(true);
			}else{
				jsonResult.setSuccess(false);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	public void isHavedFundPool(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		String organiztion_num = request.getParameter("organiztion_num");
		Integer count = departmentService.isBundDeptByOrganiztion_num(organiztion_num);
		if(count>0){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setMsg("该资金池没有绑定的部门");
			jsonResult.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-31 上午11:04:59
	 * @功能描述:根据部门代码绑定资金池 
	 * @参数描述:
	 */
	public void bandBanlancePool(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		Map<String, String> map0 = new HashMap<String, String>();
		String organiztion_num = request.getParameter("organiztion_num");
		
	    User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String login_user = loginUser.getYhMc();
		
		//先删除之前资金池绑定的所有部门,再绑定
		List<Department> list = departmentService.getBandDeptByZjDm(organiztion_num);//获取之前资金池绑定的部门
		if(list.size()>0){
			for(Department dept:list){
				String deptDm = dept.getBmDm();
				map0.put("bmDm", deptDm);
				map0.put("organiztion_num", null);
				departmentService.bandBalancePool(map0);//删除之前资金池已绑定的部门
			}
		}
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("organiztion_num", organiztion_num);
		String bmDm = request.getParameter("bmDms");
	
		boolean result = departmentService.bandBalancePoolBybmDm(map,bmDm,login_user);
		
		if(!result){
			jsonResult.setSuccess(false);
			jsonResult.setMsg("绑定资金池失败！");
		}else{
			jsonResult.setSuccess(true);
			jsonResult.setMsg("绑定资金池成功！");
		}
		/*for(String bmDm:bmDms){
			map.put("bmDm", bmDm);
			boolean result = departmentService.bandBalancePool(map);//新的部门与资金池绑定
			if(!result){
				jsonResult.setSuccess(false);
				jsonResult.setMsg("绑定资金池失败！");
				break;
			}else{
				jsonResult.setSuccess(true);
				jsonResult.setMsg("绑定资金池成功！");
			}
		}*/
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	}
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-31 下午3:39:31
	 * @功能描述: 根据资金池代码获取绑定的部门 
	 * @参数描述:
	 */
	public void getBandDeptByZjDm(){
		HttpServletRequest request = getHttpServletRequest();
		String organiztion_num = request.getParameter("organiztion_num");
		List<Department> list = departmentService.getBandDeptByZjDm(organiztion_num);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
}
