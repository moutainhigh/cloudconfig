package com.kuangchi.sdd.consumeConsole.goodType.action;

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
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;
import com.kuangchi.sdd.consumeConsole.goodType.service.IGoodTypeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @author:guofei.lian
 * @since:2016-08-01 下午16:28
 * @description: 商品类别维护action
 * */
@Controller("goodTypeAction")
public class GoodTypeAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "goodTypeServiceImpl")
	private IGoodTypeService goodTypeService;
	@Override
	public Object getModel() {
		return null;
	}
	
	public void getGoodtypeInfoByParam(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		GoodType goodTypeModel=GsonUtil.toBean(data, GoodType.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("type_name", goodTypeModel.getType_name());
		map.put("discount_num", goodTypeModel.getDiscount_num());
		map.put("page", page);
		map.put("rows", rows);
		Grid goodType=goodTypeService.getGoodtypeInfoByParam(map);
		printHttpServletResponse(new Gson().toJson(goodType));
	}
	
	//新增之前验证类别编号是否存在   guofei.lian  2016-08-03
	public void checkNumExist(){
		HttpServletRequest request=getHttpServletRequest();
		String type_num=request.getParameter("typeNum");
		JsonResult result = new JsonResult();
		if(goodTypeService.selectTypeByNum(type_num)!=null){
			result.setSuccess(false);
			result.setMsg("该类别编号已存在");
		}else{
			result.setSuccess(true);
			result.setMsg("编号可用");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//新增商品类别    guofei.lian 2016-08-03
	public void addGoodType(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		GoodType goodType=GsonUtil.toBean(data,GoodType.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=goodTypeService.insertNewGoodtype(goodType,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("新增成功");
		}else{
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//跳转到修改页面  guofei.lian   2016-08-03
	public String toEditGoodTypePage(){
		HttpServletRequest request=getHttpServletRequest();
		String type_num=request.getParameter("type_num");
		GoodType goodType=goodTypeService.selectTypeByNum(type_num);
		request.setAttribute("goodType", goodType);
		return "success";
	}
	
	//修改商品类别  guofei.lian  2016-08-03
	public void editGoodType(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		GoodType newGoodType=GsonUtil.toBean(data,GoodType.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		
		GoodType oldGoodType=goodTypeService.selectTypeByNum(newGoodType.getType_num());
		if(oldGoodType.getPrice()==null){
			oldGoodType.setPrice("0");
		}
		PriceHistoryModel priceHistoryModel=new PriceHistoryModel();
    	boolean flag=goodTypeService.modifyGood(newGoodType,create_user);
    	if(newGoodType.getPrice()==null){
    		newGoodType.setPrice("0");
    	}
		if(flag){
			if(Double.valueOf(oldGoodType.getPrice())-Double.valueOf(newGoodType.getPrice())!=0){
				priceHistoryModel.setGood_or_type_num(newGoodType.getType_num());
				priceHistoryModel.setIs_type("1");
				priceHistoryModel.setOld_price(oldGoodType.getPrice());
				priceHistoryModel.setNew_price(newGoodType.getPrice());
				goodTypeService.insertNewPriceHistory(priceHistoryModel);
			}
			result.setSuccess(true);
			result.setMsg("修改成功");
		}else{
			result.setSuccess(false);
			result.setMsg("修改失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//跳转到查看页面  guofei.lian  2016-08-03
	public String toViewGoodTypePage(){
		HttpServletRequest request=getHttpServletRequest();
		String type_num=request.getParameter("type_num");
		GoodType goodType=goodTypeService.selectTypeByNum(type_num);
		request.setAttribute("goodType", goodType);
		return "success";
	}
	
	//伪删除商品类别  guofei.lian  2016-08-03
	public void delGoodType(){
		/**
		 * 删除商品类别要同时考虑到商品表或设备商品表有无正在使用该商品类别
		 * */
		HttpServletRequest request = getHttpServletRequest();
    	String nums=request.getParameter("del_nums");
    	JsonResult result = new JsonResult();
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
    	String[] str=nums.split(",");
    	for(int i=0;i<str.length;i++){
    		Integer goodCount=goodTypeService.selectGoodByNum(str[i]);
    		Integer deviceCount=goodTypeService.selectDeviceByTypeNum(str[i]);
    		if(goodCount!=null&&goodCount==0){
    			if(deviceCount!=null&&deviceCount==0){
    				boolean flag=goodTypeService.delGoodtypes(str[i],create_user);
        			if(flag){
        				result.setMsg("删除成功");
        	    		result.setSuccess(true);
        			}else{
        				result.setMsg("删除失败");
        	    		result.setSuccess(false);
        			}
    			}else{
    				result.setMsg("部分类别使用中，请先解除商品类别与设备的关联，再进行删除操作");
    	    		result.setSuccess(false);
    	    		printHttpServletResponse(GsonUtil.toJson(result));
    			}
    			
    		}else{
    				result.setMsg("部分类别正在商品或设备中使用，请先解除与类别的关联，再进行删除操作");
    	    		result.setSuccess(false);
    	    		printHttpServletResponse(GsonUtil.toJson(result));
    		}
    	}
    	
    	printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public void getAllGoodType(){
		List<GoodType> list = goodTypeService.getAllGoodType();
    	printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	public void getGoodTypeByNum(){
		HttpServletRequest request = getHttpServletRequest();
    	String num = request.getParameter("num");
		GoodType goodType = goodTypeService.getGoodTypeByNum(num);
    	printHttpServletResponse(GsonUtil.toJson(goodType));
	}
	
	
	//查看历史价格
	public void getPriceHistoryList(){
		HttpServletRequest request = getHttpServletRequest();
    	String type_num = request.getParameter("type_num");
    	String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type_num",type_num );
		map.put("page", (Integer.valueOf(page)-1)*Integer.valueOf(rows));
		map.put("rows", Integer.valueOf(rows));
		Grid priceHistoryList=goodTypeService.getPriceHistoryList(map);
		printHttpServletResponse(GsonUtil.toJson(priceHistoryList));
	}
	
	/**
	 * 查询商品类型是否有单价
	 * @author minting.he
	 */
	public void validPrice(){
		HttpServletRequest request = getHttpServletRequest();
    	String type_num = request.getParameter("type_num");
    	String price = goodTypeService.validPrice(type_num);
    	JsonResult result = new JsonResult();
    	if("".equals(price) || price==null){
    		result.setSuccess(false);
    	}else {
    		result.setSuccess(true);
    	}
    	printHttpServletResponse(GsonUtil.toJson(result));
	}
}
