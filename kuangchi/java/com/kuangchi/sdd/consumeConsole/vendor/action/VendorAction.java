package com.kuangchi.sdd.consumeConsole.vendor.action;

import java.util.ArrayList;
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
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.consumeConsole.vendor.service.IVendorService;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * guofei.lian
 * 2016-08-17
 * 商户信息维护action
 * */
@Controller("vendorAction")
public class VendorAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="vendorServiceImpl")
	private IVendorService vendorService;
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * guofei.lian 
	 * 2016-08-17
	 * 查询商户类型
	 * */
	public void getVendorType(){
		List<Map> list=new ArrayList<Map>();
		List<VendorType> vendorTypeList=vendorService.getVendorType();
		for(VendorType vt:vendorTypeList){
			Map<String,String> map=new HashMap<String,String>();
			map.put("VALUE", vt.getVendor_type_num());
			map.put("TEXT", vt.getVendor_type_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	//模糊查询
	public void getVendorInfoByParam(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		Vendor vendor=GsonUtil.toBean(data,Vendor.class);
		Grid vendorGrid=vendorService.getVendorInfoByParam(vendor,page,rows);
		printHttpServletResponse(GsonUtil.toJson(vendorGrid));
	}
	
	//查询所有商户信息
	public void getVendorInfoList(){
		List<Vendor> vendorList=vendorService.getAllVendor();
		printHttpServletResponse(new Gson().toJson(vendorList));
	}
	
	/**
	 * guofei.lian
	 * 2016-08-18
	 * 新增时验证商户编号是否已存在
	 * */
	public void checkNumExist(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_num=request.getParameter("vendorNum");
		JsonResult result = new JsonResult();
		if(vendorService.selectVendorByNum(vendor_num)!=null){
			result.setSuccess(false);
			result.setMsg("该商品编号已存在");
		}else{
				result.setSuccess(true);
				result.setMsg("编号可用");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//新增商户
	public void addVendor(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		Vendor vendor=GsonUtil.toBean(data, Vendor.class);
		boolean flag=vendorService.insertNewVendor(vendor,create_user);
		if(flag){
			result.setMsg("新增成功");
			result.setSuccess(true);
		}else{
			result.setMsg("新增失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//跳转到修改商户页面
	public String toEditVendorPage(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_num=request.getParameter("vendor_num");
		Vendor vendor=vendorService.selectVendorByNum(vendor_num);
		request.setAttribute("vendor", vendor);
		return "success";
	}
	
	//修改商户
	public void editVendor(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Vendor vendor=GsonUtil.toBean(data, Vendor.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=vendorService.updateVendor(vendor,create_user);
		if(flag){
			result.setMsg("修改成功");
			result.setSuccess(true);
		}else{
			result.setMsg("修改失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	//查看商户
	public String toViewVendorPage(){
		HttpServletRequest request=getHttpServletRequest();
		String vendor_num=request.getParameter("vendor_num");
		Vendor vendor=vendorService.selectVendorInfoByNum(vendor_num);
		vendor.setRegister_date(vendor.getRegister_date().substring(0, 19));
		vendor.setContract_start_date(vendor.getContract_start_date().substring(0, 19));
		vendor.setContract_end_date(vendor.getContract_end_date().substring(0, 19));
		request.setAttribute("vendor", vendor);
		return "success";
	}
	
	//伪删除商户
	public void delVendor(){
		HttpServletRequest request = getHttpServletRequest();
    	String nums=request.getParameter("del_nums");
    	JsonResult result = new JsonResult();
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
    	String[] str=nums.split(",");
    	for(int i=0;i<str.length;i++){
    		Integer count=vendorService.selectDeviceByVendorNum(str[i]);
    		if(count==0){
    			boolean flag=vendorService.delVendor(str[i],create_user);
    			if(flag){
    				result.setMsg("删除成功");
    	    		result.setSuccess(true);
    			}else{
    				result.setMsg("删除失败");
    	    		result.setSuccess(false);
    			}
    		}else{
    			result.setMsg("部分商户与设备关联，请先到“设备维护”中解除关联，再进行删除操作");
	    		result.setSuccess(false);
	    		printHttpServletResponse(GsonUtil.toJson(result));
    		}
    	}
    	printHttpServletResponse(GsonUtil.toJson(result));
	}
	
}
