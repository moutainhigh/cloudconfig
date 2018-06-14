package com.kuangchi.sdd.consumeConsole.discount.action;


import java.util.List;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.fop.datatypes.Numeric;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.discount.model.Discount;
import com.kuangchi.sdd.consumeConsole.discount.service.IDiscountService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("DiscountAction")
public class DiscountAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "DiscountServiceImpl")
	private IDiscountService discountService;
	
	private Discount model;
	public DiscountAction(){
		model=new Discount();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到门信息主页面
	public String toMyDiscount(){
		return "success";
	}
	
	//查询全部信息
	public void getSelDiscount(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
			Discount discount_info=GsonUtil.toBean(data,Discount.class);
		/*if(discount_info.getDiscount_num()!=null){
			discount_info.setDiscount_num(discount_info.getDiscount_num());
		}*/
		Grid allDiscount=discountService.selectAllDiscounts(discount_info, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//新增门信息
	public void getAddDiscount(){
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String data= request.getParameter("data");
		String old_discount_num=request.getParameter("old_discount_num");
		Discount discount_info=GsonUtil.toBean(data,Discount.class);
		double a=discount_info.getDiscount_rate();
		a=a/100;
		discount_info.setDiscount_rate(a);
		// 后台判断，不能为空
				/*if (EmptyUtil.atLeastOneIsEmpty(device_num, door_name)) {
					JsonResult result = new JsonResult();
					result.setMsg("输入的参数不能为空");
					result.setSuccess(false);
					printHttpServletResponse(GsonUtil.toJson(result));
					return;
				}*/
		// 获得登陆用户的名字
			User loginUser = (User) getHttpServletRequest().getSession()
					.getAttribute(GlobalConstant.LOGIN_USER);
			discount_info.setCreate_user(loginUser.getYhMc());
			JsonResult result = new JsonResult();
			if ("add".equals(flag)) {
			List<Discount> discount=discountService.selectDiscountByNum(discount_info);
			if(discount.size()==1){
				result.setMsg("该折扣编号已存在，请重新输入");
	  			result.setSuccess(false);
			}else{
				Boolean obj=discountService.insertDiscount(discount_info);
					if(obj){
						result.setMsg("添加成功");
			  			result.setSuccess(true);
					}else{
						result.setMsg("添加失败");
			  			result.setSuccess(false);
					}
				}
				printHttpServletResponse(GsonUtil.toJson(result));
			}else if ("edit".equals(flag)){
				List<Discount> discount=discountService.selectDiscountByNum(discount_info);
				if(discount.size()!=0){
		  			if(old_discount_num.equals(discount_info.getDiscount_num())){
		  				Boolean obj=discountService.updateDiscount(discount_info);
						if(obj){
							result.setMsg("修改成功");
					        result.setSuccess(true);
						}else{
							result.setMsg("修改失败");
					        result.setSuccess(false);
						}
		  			}else{
		  				result.setMsg("该折扣代码已存在，请重新输入");
			  			result.setSuccess(false);
		  			}
				}else{
				Boolean obj=discountService.updateDiscount(discount_info);
					if(obj){
						result.setMsg("修改成功");
				        result.setSuccess(true);
					}else{
						result.setMsg("修改失败");
				        result.setSuccess(false);
					}
				}	
		        printHttpServletResponse(GsonUtil.toJson(result));
	}
}	
	
	//删除折扣信息
	public void getDelDiscount(){
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("data_ids");//多个值传入进行批量删除
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		//discount_info.setCreate_user(loginUser.getYhMc());
		JsonResult result =null;
		String [] str= id.split(",");
		for (int i = 0; i < str.length; i=i+2) {
			Integer objN=discountService.selectGoodByNumCount(str[i]);
			Integer objNs=discountService.selectGoodTypeByNumCount(str[i]);
			if(objN==0&&objNs==0){
				Integer obj=discountService.deleteDiscount(str[i+1],loginUser.getYhMc());
				 result = new JsonResult();
				if(obj==1){
					 result.setMsg("删除成功");
				    result.setSuccess(true);
				   
				}else{
					 result.setMsg("删除失败");
				    result.setSuccess(false);
				}
				
			}else{
				 result = new JsonResult();
				 result.setMsg("该折扣正在打折，不能删除");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
			}
			
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	// 分发页面
		public String toOperatePage() {
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag");
			if ("view".equals(flag) ) {
				Integer id=Integer.valueOf(request.getParameter("id"));
				List<Discount> discounts=discountService.selectDiscountById(id);
				double rate=discounts.get(0).getDiscount_rate();
				rate=rate*100;
				discounts.get(0).setDiscount_rate(rate);
				for (Discount discount : discounts) {
					request.setAttribute("discount", discount);
				}
				return "view";
			}else if("edit".equals(flag)){
				Integer id=Integer.valueOf(request.getParameter("id"));
				List<Discount> discounts=discountService.selectDiscountById(id);
				double rate=discounts.get(0).getDiscount_rate();
				int rates=(int) (rate*100);
				discounts.get(0).setDiscount_rates(rates);
				for (Discount discount : discounts) {
					request.setAttribute("discount", discount);
				}
				return "edit";
			}	
			else {
				return "success";
			}
		}	
		
}