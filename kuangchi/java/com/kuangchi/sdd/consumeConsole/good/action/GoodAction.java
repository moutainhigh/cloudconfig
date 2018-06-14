package com.kuangchi.sdd.consumeConsole.good.action;

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
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;
import com.kuangchi.sdd.consumeConsole.good.model.Discount;
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.good.model.Param;
import com.kuangchi.sdd.consumeConsole.good.model.Vendor;
import com.kuangchi.sdd.consumeConsole.good.service.IGoodService;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;

/**
 * @author:guofei.lian
 * @since:2016-07-27 下午16:18
 * @description: 商品信息维护action
 * */
@Controller("goodAction")
public class GoodAction extends BaseActionSupport{
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "goodServiceImpl")
	private IGoodService goodService;
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
     * guofei.lian
     * 2016-07-27
     * 查询商品类别
     * */
    public void getGoodType(){
    	List<Map> list=new ArrayList<Map>();
    	List<GoodType> goodtypeList=goodService.getGoodType();
    	for(GoodType gt:goodtypeList){
    		Map<String, String> map = new HashMap<String, String>();
            map.put("VALUE", gt.getType_num());
            map.put("TEXT", gt.getType_name());
            list.add(map);
    	}
    	printHttpServletResponse(GsonUtil.toJson(list));
    }
    
    /**
     * guofei.lian
     * 2016-07-28
     * 查询商户名称，商户编号
     * */
    public void getVendornum(){
    	List<Map> list=new ArrayList<Map>();
    	List<Vendor> vendorList=goodService.getVendornum();
    	for(Vendor v:vendorList){
    		Map<String, String> map = new HashMap<String, String>();
            map.put("VALUE", v.getVendor_num());
            map.put("TEXT", v.getVendor_name());
            list.add(map);
    	}
    	printHttpServletResponse(GsonUtil.toJson(list));
    }
    
    /**
     * giuofei.lian
     * 2016-07-28
     * 查询折扣
     * */
    public void getDiscountnum(){
    	List<Map> list=new ArrayList<Map>();
    	List<Discount> discountList=goodService.getDiscountnum();
    	Map<String,String> initMap=new HashMap<String,String>();
    	initMap.put("VALUE", "");
    	initMap.put("TEXT", "请选择");
    	list.add(initMap);
    	for(Discount d:discountList){
    		Map<String, String> map = new HashMap<String, String>();
            map.put("VALUE", d.getDiscount_num());
            map.put("TEXT", d.getDiscount_name());
            list.add(map);
    	}
    	printHttpServletResponse(GsonUtil.toJson(list));
    	
    	
    }
    
    /**
     * giuofei.lian
     * 2016-09-07
     * 查询折扣有效期
     * */
    public void getAvailableTimeByDiscountNum(){
    	HttpServletRequest request = getHttpServletRequest();
		String discount_num = request.getParameter("discount_num");
		Discount discount=goodService.selectAvailableTimeByNum(discount_num);
		printHttpServletResponse(GsonUtil.toJson(discount));
    }
    
    /**
     * guofei.lian
     * 2016-07-27
     * 查询商品类别
     * */
    public void getGoodInfoByParam(){
    	HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		Param param=GsonUtil.toBean(data,Param.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("good_name",param.getGood_name() );
		map.put("good_type_num", param.getGood_type_num());
		map.put("start_time", param.getStart_time());
		map.put("end_time",param.getEnd_time() );
		map.put("page", page);
		map.put("rows", rows);
		Grid good=goodService.getGoodInfoByParam(map);
		printHttpServletResponse(new Gson().toJson(good));
    }
    
    /**
     * guofei.lian
     * 2016-07-28
     * 验证商品编号的唯一性
     * */
    public void checkGoodnumExist(){
    	HttpServletRequest request = getHttpServletRequest();
		String good_num=request.getParameter("goodNum");
		JsonResult result = new JsonResult();
		if(goodService.selectGoodByNum(good_num)!=null){
			result.setSuccess(false);
			result.setMsg("该商品编号已存在");
		}else{
				result.setSuccess(true);
				result.setMsg("编号可用");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
    }
    
    /**
     * guofei.lian
     * 2016-07-28
     * 新增商品
     * */
    public void addGood(){
    	HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Good good=GsonUtil.toBean(data,Good.class);
		JsonResult result = new JsonResult();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		boolean flag=goodService.insertNewGood(good,create_user);
		if(flag){
			result.setSuccess(true);
			result.setMsg("新增成功");
		}else{
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
    }
    
    //跳转到修改页面
    public String toEditGoodPage(){
    	HttpServletRequest request = getHttpServletRequest();
    	String good_num=request.getParameter("good_num");
    	Good good=goodService.selectGoodByNum(good_num);
    	request.setAttribute("good", good);
    	return "success";
    }
    
    /**
     * guofei.lian
     * 2016-07-28
     * 修改商品
     * */
    public void editGood(){
    	HttpServletRequest request = getHttpServletRequest();
    	String data=request.getParameter("data");
    	Good newGood=GsonUtil.toBean(data,Good.class);
    	JsonResult result = new JsonResult();
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		
		Good oldGood=goodService.selectGoodByNum(newGood.getGood_num());
		PriceHistoryModel priceHistoryModel=new PriceHistoryModel();
    	boolean flag=goodService.modifyGood(newGood,create_user);
		if(flag){
			if((Double.valueOf(oldGood.getPrice()))-(Double.valueOf(newGood.getPrice()))!=0){
				priceHistoryModel.setGood_or_type_num(newGood.getGood_num());
				priceHistoryModel.setIs_type("0");
				priceHistoryModel.setOld_price(oldGood.getPrice());
				priceHistoryModel.setNew_price(newGood.getPrice());
				goodService.insertPriceHistory(priceHistoryModel);
			}
			result.setSuccess(true);
			result.setMsg("修改成功");
		}else{
			result.setSuccess(false);
			result.setMsg("修改失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
    }
    
    /**
     * guofei.lian
     * 2016-07-28
     * 删除商品
     * */
    public void delGoods(){
    	HttpServletRequest request = getHttpServletRequest();
    	String del_nums=request.getParameter("del_nums");
    	JsonResult result = new JsonResult();
    	User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
    	String[] str=del_nums.split(",");
    	for(int i=0;i<str.length;i++){
    		Integer count=goodService.selectDeviceByGoodNum(str[i]);
    		if(count!=null&&count==0){
    			boolean flag=goodService.delGoods(str[i],create_user);
    			if(flag){
    				result.setMsg("删除成功");
    	    		result.setSuccess(true);
    			}else{
    				result.setMsg("删除失败");
    	    		result.setSuccess(false);
    			}
    		}else{
    			result.setMsg("部分商品与设备关联，请先到“设备维护”中解除关联，再进行删除操作");
	    		result.setSuccess(false);
	    		printHttpServletResponse(GsonUtil.toJson(result));
    		}
    	}
    	
    	printHttpServletResponse(GsonUtil.toJson(result));
    	
    }
    
    public String toViewGoodPage(){
    	HttpServletRequest request = getHttpServletRequest();
    	String good_num=request.getParameter("good_num");
    	Good good=goodService.selectGoodInfoByNum(good_num);
    	request.setAttribute("good", good);
    	return "success";
    }
    
    public void getAllGood(){
    	List<Good> list = goodService.getAllGood();
    	printHttpServletResponse(GsonUtil.toJson(list));
    }
    
    public void getGoodByNum(){
    	HttpServletRequest request = getHttpServletRequest();
    	String good_num=request.getParameter("num");
    	Good good=goodService.getGoodByNum(good_num);
    	printHttpServletResponse(GsonUtil.toJson(good));
    }
    
    //查看历史价格
    public void getPriceHistory(){
    	HttpServletRequest request = getHttpServletRequest();
    	String good_num = request.getParameter("good_num");
    	String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num",good_num );
		map.put("page", (Integer.valueOf(page)-1)*Integer.valueOf(rows));
		map.put("rows", Integer.valueOf(rows));
		Grid priceHistoryList=goodService.getPriceHistoryList(map);
		printHttpServletResponse(GsonUtil.toJson(priceHistoryList));
    }
    
    /**
     * 查询商户下的商品
     * @author minting.he
     */
    public void getGoodByVendor(){
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	try{
	    	HttpServletRequest request = getHttpServletRequest();
	    	String vendor_num = request.getParameter("vendor_num");
	    	list = goodService.getGoodByVendor(vendor_num);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		printHttpServletResponse(GsonUtil.toJson(list));
    	}
    }
    
}
