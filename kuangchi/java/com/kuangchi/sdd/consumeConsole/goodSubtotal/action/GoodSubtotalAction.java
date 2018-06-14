package com.kuangchi.sdd.consumeConsole.goodSubtotal.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.GoodSubtotal;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.SearchModel;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.service.IGoodSubtotalService;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.util.DownloadExcel;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * 商品清算
 * @author minting.he
 *
 */
@Controller("goodSubtotalAction")
public class GoodSubtotalAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "goodSubtotalService")
	private IGoodSubtotalService goodSubtotalService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 按条件查询清算信息
	 * @author minting.he
	 */
	public void getGoodSubtotalByParam(){
		HttpServletRequest request = getHttpServletRequest();
        String beanObject = request.getParameter("data");
        SearchModel searchModel = GsonUtil.toBean(beanObject, SearchModel.class);  
        if(BeanUtil.isNotEmpty(searchModel)){
	        if(!EmptyUtil.atLeastOneIsEmpty(searchModel.getVendor_name())){
	        	searchModel.setVendor_name(searchModel.getVendor_name().trim().replace("<","&lt").replace(">","&gt"));
	        }
        }
        String vendor_name = searchModel.getVendor_name();
        String start_date = searchModel.getStart_date();
        String end_date = searchModel.getEnd_date();
        if(end_date!=null){
        	end_date=end_date+" 23:59:59";
        }
        Integer page=Integer.parseInt(request.getParameter("page"));
        Integer rows=Integer.parseInt(request.getParameter("rows"));
    	Integer skip=(page-1)*rows;
        Grid<GoodSubtotal> subtotalGrid = goodSubtotalService.getGoodSubtotalByParam(vendor_name, start_date, end_date, skip, rows);
    	printHttpServletResponse(GsonUtil.toJson(subtotalGrid));
	}
	
	/**
	 * 导出全部清算信息
	 * @author minting.he
	 */
	public void exportAllGoodSubtotal(){
		HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse response=getHttpServletResponse();
        List<GoodSubtotal> subtotalList = goodSubtotalService.exportAllGoodSubtotal();
        for(GoodSubtotal g : subtotalList){
        	Integer i = (int) (Double.valueOf(g.getDiscount_balance())*100);
        	g.setDiscount_balance(i.toString().concat("%"));
        }
		String jsonList=GsonUtil.toJson(subtotalList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		cloTitleList.add("清算日期");
		cloTitleList.add("商户名称");
		cloTitleList.add("交易笔数");
		cloTitleList.add("折扣率");
		cloTitleList.add("实际金额（元）");
		cloTitleList.add("实付金额（元）");
		cloTitleList.add("减免金额（元）");
		cloTitleList.add("创建时间");
		colList.add("sub_date");
		colList.add("vendor_name");
		colList.add("trading_volume");
		colList.add("discount_balance");
		colList.add("fee_balance");
		colList.add("real_balance");
		colList.add("relief_balance");
		colList.add("create_time");
		
		String[] colTitles=new String[colList.size()];
		String[] cols=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=cloTitleList.get(i);
		}
	    OutputStream  out=null;
	    try {
			out=response.getOutputStream();
			response.setContentType("application/x-msecxel");
			String fileName="商户清算表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=DownloadExcel.exportExcel("商户清算表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 手动清算商户
	 * @author minting.he
	 */
	public void manualSubVendor(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		if (loginUser == null) {
			result.setSuccess(false);
			result.setMsg("操作失败，请先登录");
		} else {
			String login_user = loginUser.getYhMc();
			boolean r = goodSubtotalService.manualSubVendor(login_user);
			if(r){
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
}
