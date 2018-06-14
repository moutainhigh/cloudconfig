package com.kuangchi.sdd.consumeConsole.nameList.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.consumeConsole.nameList.model.NameList;
import com.kuangchi.sdd.consumeConsole.nameList.service.NameListService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("nameListAction")
public class NameListAction  extends BaseActionSupport{
	
	@Resource(name="nameListServiceImpl")
	private NameListService nameListService;
	
	@Override
	public Object getModel() {
		return null;
	}
    /**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-5 
	 * @功能描述: 获取下发名单全部记录-Action
	 */
	public void getNameList(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<NameList> grid = new Grid<NameList>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = nameListService.getNameList(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-6 
	 * @功能描述: 获取所有卡类型-Action
	 */
	public void getAllCardType(){
		List<CardType> allCardType=nameListService.getAllCardType();
		List<Map> list=new ArrayList<Map>();
		for(CardType cardType:allCardType){
			Map<String,String> map=new HashMap<String,String>();
			map.put("text", cardType.getType_name());
			map.put("id", cardType.getType_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
	
	
	//主页面显示
    public String toNameListPage(){
    	return "success";
    }
}
