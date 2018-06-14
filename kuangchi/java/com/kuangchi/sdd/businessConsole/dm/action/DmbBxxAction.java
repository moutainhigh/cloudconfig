package com.kuangchi.sdd.businessConsole.dm.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.dm.model.DmbBxx;
import com.kuangchi.sdd.businessConsole.dm.model.DmbLxx;
import com.kuangchi.sdd.businessConsole.dm.service.IDmbBxxService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("dmbBxxAction")
public class DmbBxxAction extends BaseActionSupport {
	
	private static final  Logger LOG = Logger.getLogger(DmbBxxAction.class);


	private static final long serialVersionUID = -6949101049429392026L;
	
	@Resource(name="dmbBxxService")
	private IDmbBxxService  dmbBxxService;
	
	private DmbBxx model;

	@Override
	public Object getModel() {
		model = new DmbBxx();
		return model;
	}
	
	
	public void listDmbBxx(){
		
		Grid<DmbBxx> grid = dmbBxxService.selectDmbBxx(model);
		
		model = new DmbBxx();
		
		printHttpServletResponse(GsonUtil.toJson(grid));
				
	}
	
	public void validDmbBmc(){
		String dmbBmc = getHttpServletRequest().getParameter("dmbBmc");
		
		boolean isExist = dmbBxxService.isExistDmbBmc(dmbBmc);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		if (isExist) {
			result.setSuccess(false);
			result.setMsg("表明已存在");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	
	}
	
	/**
	 * 新增代码表
	 */
	public void addDmB(){
		
		User loginUser = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		String dmbBxxStr = getHttpServletRequest().getParameter("dmbBxx");	
		String dmbLxxSStr = getHttpServletRequest().getParameter("dmbLxxS");
		
		
		DmbBxx dmbBxx = GsonUtil.toBean(dmbBxxStr, DmbBxx.class);
		List<DmbLxx> dmlList = new Gson().fromJson(dmbLxxSStr,new TypeToken<List<DmbLxx>>(){}.getType());
		
	
		JsonResult result= new JsonResult();
		result.setSuccess(true);
		
		try {
			dmbBxxService.addDmB(loginUser,dmbBxx,dmlList);
		} catch (Exception e) {
			
			result.setSuccess(false);
			
			LOG.error("新增代码表失败", e);
		}
				
		printHttpServletResponse(GsonUtil.toJson(result));
	
	}

}
