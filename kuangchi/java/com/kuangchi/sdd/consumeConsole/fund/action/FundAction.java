package com.kuangchi.sdd.consumeConsole.fund.action;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;
import com.kuangchi.sdd.consumeConsole.fund.service.IFundService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-17 上午11:07:34
 * @功能描述: 资金流水表模块-action
 */
@Controller("fundAction")
public class FundAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "fundServiceImpl")
	private IFundService fundService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:07:02
	 * @功能描述: 根据参数查询资金流水信息[分页]
	 * @参数描述:
	 */
	public void getFundByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		FundModel fund = GsonUtil.toBean(data, FundModel.class);
		String op_type = fund.getOp_type();
		String card_num = fund.getCard_num();
		String organiztion_name = fund.getOrganiztion_name();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("op_type", op_type);
		map.put("card_num", card_num);
		map.put("organiztion_name", organiztion_name);
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<FundModel> fundList =  fundService.getFundByParamPage(map);
		Integer fundCount = fundService.getFundByParamCount(map);
		
		Grid<FundModel> grid = new Grid<FundModel>();
		grid.setRows(fundList);
		grid.setTotal(fundCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
}
