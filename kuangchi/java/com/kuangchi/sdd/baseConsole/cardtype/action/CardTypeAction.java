package com.kuangchi.sdd.baseConsole.cardtype.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;

import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.cardtype.model.ConModel;
import com.kuangchi.sdd.baseConsole.cardtype.service.ICardTypeServcie;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-3-30下午6:16:01
 * @功能描述:卡片类型管理模块-Action
 * @参数描述:
 */
@Controller("cardTypeAction")
public class CardTypeAction extends BaseActionSupport {
	private CardType model;
	private ConModel conModel = new ConModel();
	private UUIDUtil uui;// 自动生成卡片类型代码

	public CardTypeAction() {
		model = new CardType();

	}

	@Resource(name = "cardTypeServiceImpl")
	private ICardTypeServcie cardTypeService;

	@Override
	public Object getModel() {
		return model;
	}

	/**
	 * 新增/修改卡片类型
	 */

	public void addCardtype() {
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String type_name = request.getParameter("type_name");
			type_name=type_name.replace("<","&lt").replace(">","&gt");
		String type_validity = request.getParameter("type_validity");
		String description = request.getParameter("description");
	
		// 后台判断，不能为空
		if (EmptyUtil.atLeastOneIsEmpty(type_name, type_validity)) {
			JsonResult result = new JsonResult();
			result.setMsg("输入的参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		model.setType_name(type_name);
		model.setType_validity(type_validity);
		model.setDescription(description);
		// 获得登陆用户的名字
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		model.setCreate_user(loginUser.getYhMc());

		// 自动设置类型代码
		JsonResult result = new JsonResult();
		if ("add".equals(flag)) {
			String dm = uui.uuidStr();
			model.setType_dm(dm);
			CardType cardTypeName = cardTypeService.getByName(model.getType_name());
			if (cardTypeName == null) {

				cardTypeService.addCardType(model);
				result.setMsg("添加成功");
				result.setSuccess(true);
			} else {
				// 存在相同的数据
				result.setMsg("已经存在该卡片名称，请重新输入另一个！");
				result.setSuccess(false);
			}
		} else if ("update".equals(flag)) {
			CardType cardTypeName = cardTypeService.getByName(model.getType_name());
			if (cardTypeName == null) {
				cardTypeService.updateCardType(model);
				result.setMsg("修改成功");
				result.setSuccess(true);
				printHttpServletResponse(GsonUtil.toJson(result));
			} else if ((cardTypeName.getCard_type_id().equals(model.getCard_type_id()))&& (cardTypeName.getType_name()
							.equals(model.getType_name()))) {
				cardTypeService.updateCardType(model);
				result.setMsg("修改成功");
				result.setSuccess(true);
			} else {
				// 存在相同的数据
				result.setMsg("已经存在该卡片名称，请重新输入另一个！");
				result.setSuccess(false);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 全部查询
	 */
	public void getAllCT() {
		HttpServletRequest request = getHttpServletRequest();

		Grid<CardType> cardTypeGrid = null;
		String beanObject = getHttpServletRequest().getParameter("data"); // 获取前台序列化的数据
		ConModel conModel = GsonUtil.toBean(beanObject, ConModel.class); // 将数据转化为javabean

		conModel.setPage(Integer.parseInt(request.getParameter("page")));
		conModel.setRows(Integer.parseInt(request.getParameter("rows")));

		cardTypeGrid = cardTypeService.getAllCT(conModel);

		printHttpServletResponse(GsonUtil.toJson(cardTypeGrid));
	}

	public void getCTByParam() {
		HttpServletRequest request = getHttpServletRequest();

		Grid<CardType> cardTypeGrid = null;
		String beanObject = getHttpServletRequest().getParameter("data"); // 获取前台序列化的数据
		ConModel conModel = GsonUtil.toBean(beanObject, ConModel.class); // 将数据转化为javabean

		conModel.setPage(Integer.parseInt(request.getParameter("page")));
		conModel.setRows(Integer.parseInt(request.getParameter("rows")));

		cardTypeGrid = cardTypeService.getCTByParam(conModel);

		printHttpServletResponse(GsonUtil.toJson(cardTypeGrid));
	}

	/**
	 * 删除
	 */
	public void delCardtype() {

		// 记录日志
		// 获得登陆用户的名字
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		model.setCreate_user(loginUser.getYhMc());

		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String ct_id = request.getParameter("data_ids");
		//判断是否被引用
		List<CardType> ctList=cardTypeService.getListCardInfoByDMService(ct_id);
		if(ctList.size()>0){
			result.setMsg("该卡片类型被卡片引用，不能删除！");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}else{
		cardTypeService.delCardType(ct_id);
		result.setMsg("删除成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
		}

	}

	/**
	 * 界面action
	 * 
	 * @return
	 */
	public String view() {
		return "success";
	}
	
	/**
	 * 根据id查询
	 */
	
	public void getCardtypeById(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String ct_id = request.getParameter("card_type_id");
		CardType ca=cardTypeService.getCTById(ct_id);
		if(ca==null){
			result.setMsg("该卡片类型已经不存在！");
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
}
