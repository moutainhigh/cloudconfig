package com.kuangchi.sdd.baseConsole.cardstate.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;
import com.kuangchi.sdd.baseConsole.cardstate.service.ICardStateService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


@Controller("cardstateAction")
public class CardstateAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(CardstateAction.class);
	
	private CardState model;
	
	public CardstateAction(){
		model=new CardState();
	}
	
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "cardStateServiceImpl")
	private ICardStateService cardStateService;

	@Override
	public Object getModel() {
		return model;
	}
	public String toMyCardstate(){
		return "success";
	}
	/**
	 * 新增/修改卡片信息
	 */
	public void addCardstate(){
		 HttpServletRequest request = getHttpServletRequest();
		 String flag=request.getParameter("flag");
	     String data= request.getParameter("data");
	     CardState cardState=GsonUtil.toBean(data,CardState.class);
	     if(cardState.getState_name()!=null){
				cardState.setState_name(cardState.getState_name().trim());
			}else if(cardState.getState_dm()!=null){
				cardState.setState_dm(cardState.getState_name().trim());
			}
	     Integer card_state_id=cardState.getCard_state_id();
	     String state_dm=cardState.getState_dm().trim();
		 String state_name=cardState.getState_name().trim();
		 //String state_validity=cardState.getState_validity();
		// 后台判断，不能为空
			if (EmptyUtil.atLeastOneIsEmpty(state_dm, state_name)) {
				JsonResult result = new JsonResult();
				result.setMsg("输入的参数不能为空");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
			}
	     JsonResult result = new JsonResult();
		try
		{
			 User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			 cardState.setCreate_user(loginUser.getYhMc());
			 if(flag.equals("add")){
				 Boolean obj=cardStateService.insertCardState(cardState);
				 if(true==obj){
					 result.setMsg("添加成功");
				     result.setSuccess(true);
				     printHttpServletResponse(GsonUtil.toJson(result));
				 }else
				 {
					 result.setMsg("添加失败");
				     result.setSuccess(false);
				     printHttpServletResponse(GsonUtil.toJson(result));
				 }
			     
			 }else if(flag.equals("edit")){
				 Boolean obj=cardStateService.updateCardState(cardState);
				 if(true==obj){
					 result.setMsg("修改成功");
				     result.setSuccess(true);
				     printHttpServletResponse(GsonUtil.toJson(result));
				 }else
				 {
					 result.setMsg("修改失败");
				     result.setSuccess(false);
				     printHttpServletResponse(GsonUtil.toJson(result));
				 }
			 }
		 
		}catch(Exception e)
		{
			result.setMsg("操作失败");
	        result.setSuccess(false);
			LOG.error("card",e);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/**
	 * 
	 * Description:根据条件查询卡片信息分页
	 * date:2016年3月14日
	 */
	public void getAllCardstates(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		CardState cardState=GsonUtil.toBean(data,CardState.class);
		if(cardState.getState_name()!=null){
			cardState.setState_name(cardState.getState_name().trim());
		}else if(cardState.getState_dm()!=null){
			cardState.setState_dm(cardState.getState_dm().trim());
		}
		Grid allCard=cardStateService.selectAllCardStates(cardState, page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}
	/**
	 * 
	 * Description:根据ID删除卡片信息
	 * date:2016年3月15日
	 */
	public void deleteCardById(){
		HttpServletRequest request = getHttpServletRequest();
		String card_state_id=request.getParameter("data_ids");
		String[] sourceStrArray = card_state_id.split(",");
		JsonResult result = new JsonResult();
		for (int i = 0; i < sourceStrArray.length; i++){
    	Integer del=cardStateService.selectCard(sourceStrArray[i]);
    	if(del!=0){
		    result.setSuccess(false);
		    printHttpServletResponse(GsonUtil.toJson(result));
		 }else{
			 Integer sel=cardStateService.selectEditable(sourceStrArray[i]);
			 if(sel!=0){
				 result.setSuccess(false);
				 printHttpServletResponse(GsonUtil.toJson(result));
			 }else
			 cardStateService.deleteCardState(sourceStrArray[i]);
			 result.setMsg("删除成功");
		 	}	    
		}
	    result.setSuccess(true);
	    printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 新增、修改、查看之前先执行
	 * @return
	 */
	public String toOperatePage(){
  	  HttpServletRequest request=getHttpServletRequest();
  	  String flag=request.getParameter("flag");
  	
  	if("view".equals(flag)){
  		Integer card_state_id=Integer.valueOf(request.getParameter("card_state_id"));
  		List<CardState> cardState=cardStateService.selectCardStateById(card_state_id);
  		if(cardState.size()==0){
  			return "error";
  		}else
  		for (CardState cardState2 : cardState) {
  			request.setAttribute("cardState", cardState2);
		}
  		return "view";
  	}else if("edit".equals(flag)){
  		Integer card_state_id=Integer.valueOf(request.getParameter("card_state_id"));
  		List<CardState> cardState=cardStateService.selectCardStateById(card_state_id);
  		if(cardState.size()==0){
  			return "error";
  		}else
  		for (CardState cardState2 : cardState) {
  			request.setAttribute("cardState", cardState2);
		}
  		return "edit";
  	}else{
  		return "success";
  	}
  	
  }
	/**
	 * 卡片状态代码唯一查询
	 */
	public void getuniqueCardState(){
		HttpServletRequest request = getHttpServletRequest();
		
		String state_dm=request.getParameter("state_dm");
		if(state_dm==null ){
			JsonResult result = new JsonResult();
		    result.setMsg("该状态码不能为空");
		    printHttpServletResponse(GsonUtil.toJson(result));
		}
		List<CardState> cardstate=cardStateService.selectUniqueState(state_dm);
			
		if(cardstate.size()==0){
			JsonResult result = new JsonResult();
		    result.setSuccess(false);
		    printHttpServletResponse(GsonUtil.toJson(result));
		   
		}else {
			JsonResult result = new JsonResult();
		    result.setMsg("该状态代码已经存在");
		    result.setSuccess(true);
		    printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	/**
	 * 卡片状态名称唯一查询
	 */
	public void getuniqueCardStateName(){
		HttpServletRequest request = getHttpServletRequest();
		String state_name=request.getParameter("state_name");
		String card_state_id=request.getParameter("card_state_id");
		if(state_name==null){
			JsonResult result = new JsonResult();
		    result.setMsg("该状态码不能为空");
		    printHttpServletResponse(GsonUtil.toJson(result));
		   
		}
		List<CardState> unique=cardStateService.selectUniqueStateName(state_name);
		if(unique.size()==0){
			JsonResult result = new JsonResult();
		    result.setSuccess(false);
		    printHttpServletResponse(GsonUtil.toJson(result));
		   
		}else{
			JsonResult result = new JsonResult();
		    result.setMsg("该状态名称已经存在");
		    result.setSuccess(true);
		    printHttpServletResponse(GsonUtil.toJson(result));
		   
		}
		
		
	}
}
