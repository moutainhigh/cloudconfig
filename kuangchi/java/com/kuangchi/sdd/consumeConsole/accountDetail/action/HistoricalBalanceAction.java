package com.kuangchi.sdd.consumeConsole.accountDetail.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.accountDetail.model.HistoricalBalanceModel;
import com.kuangchi.sdd.consumeConsole.accountDetail.service.HistoricalBalanceService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
@Controller("historicalBalanceAction")
public class HistoricalBalanceAction extends BaseActionSupport{
	@Resource(name="historicalBalanceServiceImpl")
	private HistoricalBalanceService historicalBalanceService;
	
	@Override
	public Object getModel() {
		return null;
	}
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-29 下午2:10:11
	 * @功能描述:历史余额信息查询 
	 * @参数描述:
	 */
	public void getAllHistoricalBalance(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<HistoricalBalanceModel> grid = new Grid<HistoricalBalanceModel>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("skips", skips);
		map.put("rows", rows);
		grid = historicalBalanceService.getAllHistoricalBalance(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-29 下午3:32:20
	 * @功能描述:获取全部余额信息 
	 * @参数描述:
	 */
	public void getHistoricalBalanceList(){
		HttpServletRequest request = getHttpServletRequest();
		List<HistoricalBalanceModel> list = new ArrayList<HistoricalBalanceModel>();
		list = historicalBalanceService.getHistoricalBalanceList();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
}
