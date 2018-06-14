package com.kuangchi.sdd.consumeConsole.vendorType.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
import com.kuangchi.sdd.consumeConsole.vendorType.service.IVendorTypeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * guofei.lian
 * 2016-08-16
 * 商户类型维护action
 * */
@Controller("vendorTypeAction")
public class VendorTypeAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="vendorTypeServiceImpl")
	private IVendorTypeService vendorTypeService;
	@Override
	public Object getModel() {
		return null;
	}
	
	//模糊查询
	public void getVendorTypeInfoByParam(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		VendorType vendorTypeModel=GsonUtil.toBean(data, VendorType.class);
		Map<String,String> map=new HashMap<String,String>();
		map.put("vendor_type_num",vendorTypeModel.getVendor_type_num());
		map.put("vendor_type_name",vendorTypeModel.getVendor_type_name());
		map.put("page", page);
		map.put("rows", rows);
		Grid vendorType=vendorTypeService.getVendorTypeInfoByParam(map);
		printHttpServletResponse(new Gson().toJson(vendorType));
	}
	
	//新增时验证商户编号是否已存在
	public void checkNumExist(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_type_num=request.getParameter("vendor_type_num");
		VendorType vendorType=vendorTypeService.selectVendortypeByNum(vendor_type_num);
		if(vendorType!=null){
			printHttpServletResponse(new Gson().toJson(1));
		}else{
			printHttpServletResponse(new Gson().toJson(0));
		}
		
	}
	
	public void addVendortype(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		VendorType vendorType=GsonUtil.toBean(data, VendorType.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=vendorTypeService.insertNewVendortype(vendorType,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("新增成功");
		}else{
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public String toEditVendortypePage(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_type_num=request.getParameter("vendor_type_num");
		VendorType vendorType=vendorTypeService.selectVendortypeByNum(vendor_type_num);
		request.setAttribute("vendorType", vendorType);
		return "success";
	}
	
	public void editVendortype(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		VendorType vendorType=GsonUtil.toBean(data, VendorType.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=vendorTypeService.updateVendorType(vendorType,create_user);
		if(flag){
			result.setMsg("修改成功");
			result.setSuccess(true);
		}else{
			result.setMsg("修改失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public void delVendorType(){
		HttpServletRequest request = getHttpServletRequest();
    	String nums=request.getParameter("del_nums");
    	JsonResult result = new JsonResult();
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
    	String[] str=nums.split(",");
    	for(int i=0;i<str.length;i++){
    		Integer count=vendorTypeService.selectVendorByNum(str[i]);
    		if(count==0){
    			boolean flag=vendorTypeService.delVendorType(str[i],create_user);
    			if(flag){
    				result.setMsg("删除成功");
    	    		result.setSuccess(true);
    			}else{
    				result.setMsg("删除失败");
    	    		result.setSuccess(false);
    			}
    		}else{
    			result.setMsg("部分类型使用中，不能删除");
	    		result.setSuccess(false);
	    		printHttpServletResponse(GsonUtil.toJson(result));
    		}
    	}
    	printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public String toViewVendorTypePage(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_type_num=request.getParameter("vendor_type_num");
		VendorType vendorType=vendorTypeService.selectVendortypeByNum(vendor_type_num);
		request.setAttribute("vendorType",vendorType );
		return "success";
	}
}
