package com.kuangchi.sdd.baseConsole.shortcutKey.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.shortcutKey.model.ShortcutKey;
import com.kuangchi.sdd.baseConsole.shortcutKey.service.ShortcutKeyService;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("shortcutKeyAction")
public class shortcutKeyAction extends BaseActionSupport {

	@Resource(name="shortcutKeyServiceImpl")
	private ShortcutKeyService shortcutKeyService;
	
	@Override
	public Object getModel() {
		return null;
	}
	//页面显示
    public String toMenuTree(){
    	return "success";
    }
    /**
     * chudan.guo
     * 根据用户编号和菜单标记查找已有的快捷菜单
     */
    public void getShortcutKeys(){
    	JsonResult result = new JsonResult();
    	HttpServletRequest request = getHttpServletRequest();
    	String CDFlag=request.getParameter("CDFlag"); //菜单标记
    	 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
    	 String jsdm=role.getJsDm(); //用户当前角色
		 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		 String yh_dm=user.getYhDm();
		
		try {
			if(null==yh_dm||"".equals(yh_dm.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户编号不能为空");		
			}if(null==CDFlag||"".equals(CDFlag.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("菜单标记不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}
		List<ShortcutKey> shortcutKey=shortcutKeyService.getShortcutKeys(yh_dm, CDFlag,jsdm);
		
		printHttpServletResponse(GsonUtil.toJson(shortcutKey)); 
    }
    
    /**
     * chudan.guo
     * 添加快捷菜单
     */
    public void addshortcutKey(){
    	JsonResult result = new JsonResult();
    	HttpServletRequest request = getHttpServletRequest();
    	String CDFlag=request.getParameter("CDFlag"); //菜单标记
    	String remark=request.getParameter("remark"); //备注
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
    	Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
    	String jsdm=role.getJsDm(); //用户当前角色
		 String yh_dm=loginUser.getYhDm();//用户编号
		String login_User = loginUser.getYhMc();//用户名称
		try {
			if(null==yh_dm||"".equals(yh_dm.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户编号不能为空");		
			}if(null==CDFlag||"".equals(CDFlag.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("菜单标记不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}
		
		String[] cd_dms = request.getParameter("cd_dm").split(","); //菜单编号
		String[] image_ids = request.getParameter("image_id").split(","); //快捷菜单图标id
		for (int i = 0; i < cd_dms.length; i++) {
			String cd_dm = cd_dms[i];
			String image_id = image_ids[i];
			//根据菜单编号  查出菜单代码，名称，url
			Menu menu=shortcutKeyService.getMenuByID(cd_dm);
			if(menu==null){
				result.setMsg("没有存在该编号的菜单");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result)); 
				return;
			}

			ShortcutKey shortcutKey=new ShortcutKey();
			shortcutKey.setYh_dm(yh_dm);
			shortcutKey.setCd_dm(menu.getCdDM());
			shortcutKey.setName(menu.getCdMc());
			shortcutKey.setUrl(menu.getCdUrl());
			shortcutKey.setImage_id(image_id);
			shortcutKey.setRemark(remark);
			shortcutKey.setCDFlag(CDFlag);
			shortcutKey.setJs_dm(jsdm);
			shortcutKeyService.addShortcutKey(shortcutKey,login_User);
		}
		 result.setMsg("快捷菜单添加成功");
		 result.setSuccess(true);
		 printHttpServletResponse(GsonUtil.toJson(result));
		
		
    }
    /**
     * chudan.guo
     * 根据快捷菜单id删除快捷菜单（单个，真删除）
     */
    public void delShortcutKey(){
    	JsonResult result = new JsonResult();
    	HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("id");	//菜单id
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		if(EmptyUtil.atLeastOneIsEmpty(id)){
				result.setMsg("菜单id不能为空");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
		}else{
			try {
				shortcutKeyService.delShortcutKey(id,login_User);
				 result.setMsg("快捷菜单删除成功");
				 result.setSuccess(true);
			 } catch (Exception e) {
				 result.setMsg("删除失败");
			     result.setSuccess(false);
			 }
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
    }
    
    
    
    
}
