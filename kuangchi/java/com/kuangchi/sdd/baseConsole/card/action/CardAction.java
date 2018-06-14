package com.kuangchi.sdd.baseConsole.card.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.model.CardState;
import com.kuangchi.sdd.baseConsole.card.model.CardStateResult;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.card.model.QuerySentcardInfo;
import com.kuangchi.sdd.baseConsole.card.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.card.model.SendCardModel;
import com.kuangchi.sdd.baseConsole.card.model.StaffCard;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.baseConsole.card.util.ExcelExportTemplate;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EasyUiTable;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.util.file.PropertyUtils;

@Controller("cardAction")
public class CardAction extends BaseActionSupport {
	private static final Logger LOG = Logger.getLogger(CardAction.class);

	private Card model;
	private Card card;
	private File uploadCardFile;

	@Resource(name = "cDeviceService")
	IDeviceService cDeviceService;

	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public CardAction() {
		model = new Card();
	}

	private static final long serialVersionUID = -4559409507804703403L;

	@Resource(name = "cardServiceImpl")
	private ICardService cardService;

	@Override
	public Object getModel() {
		return model;
	}

	/**
	 * 响应前台请求
	 */
	public String toMyCard() {

		return "success";
	}

	/**
	 * 新增/修改卡片信息
	 * 
	 * @author xuewen.deng
	 */
	public void addNewCard() {
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String data = request.getParameter("data");
		Card cardPage = GsonUtil.toBean(data, Card.class);
		JsonResult result = new JsonResult();
		try {
			User loginUser = (User) getHttpServletRequest().getSession()
					.getAttribute(GlobalConstant.LOGIN_USER);
			if (loginUser != null) {
				cardPage.setCreate_user(loginUser.getYhMc());
			} else {
				result.setMsg("操作失败,请先登录");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
			}

			cardPage.setCard_type_id(cardPage.getCard_type_id());
			cardPage.setState_dm(cardPage.getState_dm());
			cardPage.setValidity_flag("0");// 默认设为有效数据

			if (flag.equals("add")) {

				String propertyFile = request
						.getSession()
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
				Properties properties = PropertyUtils
						.readProperties(propertyFile);
				String lisenceKey = properties.getProperty("lisenceKey");
				String[] key = lisenceKey.split(";");
				if(Integer.valueOf(key[2])>=0){
					if ((cardService.getNormalCount() + 1) > Integer
							.valueOf(key[2])) {
						result.setMsg("新增失败，卡号数量已达到授权码限制数量");
						// 卡已达到授权码上限，
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				}
				

				cardPage.setState_dm("00");// 初始状态默认设为00(未绑定)
				if (cardPage.getCard_validity() != null
						&& cardPage.getCard_type_id() != null
						// && cardPage.getCard_pledge() != null
						&& cardPage.getCard_num() != null) {
					if (cardService.validCardNum(cardPage.getCard_num()) > 0) {
						result.setMsg("新增失败，此卡号已存在");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
					if (cardService.validCardNum2(cardPage.getCard_num(),
							cardPage.getCard_type_id()) == 1) {
						result.setMsg("卡号填写错误，注意卡号范围");

						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}

					if (cardService.addNewCard(cardPage)) {
						result.setMsg("添加成功");
						result.setSuccess(true);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					} else {
						if (cardPage.getDescription() != null
								&& cardPage.getDescription().length() > 150) {
							result.setMsg("新增失败，描述输入过长");
							result.setSuccess(false);
						} else {
							result.setMsg("新增失败");
							result.setSuccess(false);
						}
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				} else {
					result.setMsg("新增失败，带*号为必填信息");

					result.setSuccess(false);
					printHttpServletResponse(GsonUtil.toJson(result));
					return;
				}

			} else if (flag.equals("edit")) {
				if (cardPage.getCard_validity() != null
				// && cardPage.getCard_pledge() != null
						&& cardPage.getCard_type_id() != null) {
					try {
						cardPage.setState_dm(cardService
								.getCStateDmByName(cardPage.getState_dm()));

						if (cardService.modifyCard(cardPage,
								loginUser.getYhMc())) {
							result.setMsg("修改成功");
							result.setSuccess(true);
						} else {
							result.setMsg("修改失败");
							result.setSuccess(false);
						}
					} catch (Exception e) {

						if (cardPage.getDescription().length() > 150) {
							result.setMsg("描述输入过长");
						}
						result.setSuccess(false);
						e.printStackTrace();
					}
				} else {
					result.setMsg("修改失败(带*号为必填信息)");
					result.setSuccess(false);
				}
			}

		} catch (Exception e) {
			result.setMsg("操作失败");
			result.setSuccess(false);
			LOG.error("card", e);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 获取所有卡片信息
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCard() {
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		List<Card> allCard = cardService.getAllCard(page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}

	/**
	 * 获取除了未绑定和未发卡之外的其他状态
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCardStates() {
		List<CardState> allStates = cardService.getAllCardStates();// 获取所有状态类型
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		for (CardState cardState : allStates) {
			Map<String, String> map = new HashMap<String, String>();

			String cState_dm = cardState.getState_dm();
			if (!("00".equals(cState_dm)) && !("10".equals(cState_dm))
					&& !("100".equals(cState_dm))) {// 只能修改为除了未绑定和未发卡之外的其他状态

				map.put("TEXT", cardState.getState_name());
				map.put("ID", cardState.getState_dm());
				list.add(map);

			}
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 获取所有卡片状态
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCardStates2() {
		List<CardState> allStates = cardService.getAllCardStates3();// 获取所有状态类型
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();

		for (CardState cardState : allStates) {
			Map<String, String> map = new HashMap<String, String>();

			String cState_dm = cardState.getState_dm();
			if (!("50".equals(cState_dm)) && !("353".equals(cState_dm))
					&& !("57".equals(cState_dm)) && !("60".equals(cState_dm))
					&& !("70".equals(cState_dm))) {
				// 只能修改为除了未绑定和未发卡之外的其他状态

				map.put("TEXT", cardState.getState_name());
				map.put("ID", cardState.getState_dm());
				list.add(map);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 获取所有卡片状态
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCardStates3() {
		List<CardState> allStates = cardService.getAllCardStates3();// 获取所有状态类型
		List<Map> list = new ArrayList<Map>();
		for (CardState cardState : allStates) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TEXT", cardState.getState_name());
			map.put("ID", cardState.getState_dm());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 获取所有卡片类型
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCardType() {
		List<CardType> allCardType = cardService.getAllCardType();// 获取所有卡片类型
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();

		for (CardType cardType : allCardType) {
			Map<String, String> map = new HashMap<String, String>();
			String cType_dm = cardType.getType_dm();
			map.put("TEXT", cardType.getType_name());
			map.put("ID", cardType.getType_dm());
			list.add(map);

		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 获取所有卡片类型
	 */
	public void getAllCardTypeOfCount() {
		List<CardType> allCardType = cardService.getAllCardType();
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		for (CardType cardType : allCardType) {
			Map<String, String> map = new HashMap<String, String>();
			String cType_dm = cardType.getType_dm();
			map.put("type_name", cardType.getType_name());
			map.put("type_name", cardType.getType_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 根据条件查询卡片信息 date:2016年3月14日
	 * 
	 * @author xuewen.deng
	 */
	public void getAllByParam() {
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		if (param.getCard_num() != null) {
			param.setCard_num(param.getCard_num().trim());
		}// 去掉前后的空格
		Grid allCard = cardService.getAllByParam(param, page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}

	/**
	 * 根据条件查询卡片信息 date:2017年4月7日
	 * 
	 * @author xuewen.deng
	 */
	public void getStaff_CardByParam() {
		String page = getHttpServletRequest().getParameter("page");
		String rows = getHttpServletRequest().getParameter("rows");
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		if (param.getCard_num() != null) {
			param.setCard_num(param.getCard_num().trim());
		}// 去掉前后的空格
		if (param.getStaff_no() != null) {
			param.setStaff_no(param.getStaff_no().trim());
		}// 去掉前后的空格
		if (param.getStaff_name() != null) {
			param.setStaff_name(param.getStaff_name().trim());
		}// 去掉前后的空格
		Grid allCard = cardService.getStaff_CardByParam(param, page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}

	/**
	 * 根据卡片类型获取卡号 date:2016年7月26日
	 * 
	 * @author xuewen.deng
	 */
	public void getCardNumber() {
		String cardType = getHttpServletRequest().getParameter("cardType");

		long cardNumber = cardService.getCardNumber(cardType);// 获取卡号
		ResultMsg result = new ResultMsg();
		if (0 == cardNumber) {
			result.setResult_code("1");
			result.setResult_msg("获取卡号失败");
		} else {
			result.setResult_code("0");
			result.setResult_msg("" + cardNumber);
		}

		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 根据ID删除卡片信息 date:2016年3月15日
	 * 
	 * @author xuewen.deng
	 */
	public void deleteCardById() {
		String id = getHttpServletRequest().getParameter("data_ids");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		JsonResult result = new JsonResult();
		if (cardService.isBoundCard(id)) {
			result.setMsg("删除失败,不能删除被绑定的卡");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		} else {
			if (cardService.deleteCardById(id, loginUser.getYhMc())) {
				result.setMsg("删除成功");
				result.setSuccess(true);
				printHttpServletResponse(GsonUtil.toJson(result));
			} else {
				result.setMsg("删除失败");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
	}

	/**
	 * 分发页面
	 * 
	 * @author xuewen.deng
	 */
	public String toOperatePage() {
		HttpServletRequest request = getHttpServletRequest();

		String flag = request.getParameter("flag");

		if ("view".equals(flag)) {
			Card c;
			Integer card_id = Integer.valueOf(request.getParameter("card_id"));
			String staff_no = request.getParameter("staff_no");
			String staff_name = request.getParameter("staff_name");
	  /*      byte[] b;
	        String staff_name=null;
			try {
				b = staff_name0.getBytes("ISO-8859-1");
				staff_name = new String(b, "UTF-8");//解码:用什么字符集编码就用什么字符集解码  
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//编码  
*/			// c = (Card) cardService.getCardById(card_id);
			c = (Card) cardService.getCardById2(card_id);
			if (c == null)
				return "error";// 如果没有这个卡，则跳到错误页面
			String cardType = c.getCard_type_id();// 根据ID获取卡片类型(实际上拿到的是名称)
			String state_dm = c.getState_dm();

			request.setAttribute("staff_no", "undefined".equals(staff_no) ? ""
					: staff_no);
			request.setAttribute("staff_name",
					"undefined".equals(staff_name) ? "" : staff_name);
			request.setAttribute("cardType", cardType);
			request.setAttribute("state_dm", state_dm);
			request.setAttribute("card", c);

			return "view";
		} else if ("edit".equals(flag)) {
			Card c;
			Card c2;
			Integer card_id = Integer.valueOf(request.getParameter("card_id"));
			String staff_no = request.getParameter("staff_no");
			String staff_name = request.getParameter("staff_name");
	/*        byte[] b;
	        String staff_name=null;
			try {
				b = staff_name0.getBytes("ISO-8859-1");
				staff_name = new String(b, "UTF-8");//解码:用什么字符集编码就用什么字符集解码  
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//编码  
*/			c2 = (Card) cardService.getCardById2(card_id);// 根据ID获取卡片信息2
			if (c2 == null)
				return "error";// 如果没有这个卡，则跳到错误页面

			c = (Card) cardService.getCardById(card_id);// 根据ID获取卡片信息（返回值不同于上一行）
			String cardType_dm = c.getCard_type_id();// 根据ID获取卡片类型
			String cardType_n = c2.getCard_type_id();// 根据ID获取卡片类型(拿到的是名称)
			String state_dm = c.getState_dm(); // 根据ID获取卡片状态代码
			String state_n = c2.getState_dm(); // 根据ID获取卡片状态代码名称(拿到的是名称)
			request.setAttribute("staff_no", "undefined".equals(staff_no) ? ""
					: staff_no);
			request.setAttribute("staff_name",
					"undefined".equals(staff_name) ? "" : staff_name);
			request.setAttribute("cardType_dm", cardType_dm);// 放到request中，方便页面拿到
			request.setAttribute("cardType_n", cardType_n);// 放到request中，方便页面拿到
			request.setAttribute("state_dm", state_dm);
			request.setAttribute("state_n", state_n);
			request.setAttribute("card", c);

			return "edit";
		} else {
			return "success";
		}
	}

	/**
	 * 获得所有类型名称
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCard_Type() {
		List<CardType> allCardType = cardService.getAllCardType();
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		for (CardType cardType : allCardType) {
			Map<String, String> map = new HashMap<String, String>();

			map.put("TEXT", cardType.getType_name());// 绑定到页面combobox中的下拉框

			map.put("ID", String.valueOf(cardType.getType_dm()));// 绑定到页面combobox中的下拉框
			LOG.info("id=" + cardType.getType_dm() + "    text="
					+ cardType.getType_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 查询人员信息
	 * 
	 */

	public void getStaffInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String staff_no = request.getParameter("staff_no");
		String staff_name = request.getParameter("staff_name");
		String staff_dept = request.getParameter("staff_dept");
		Integer page = Integer.parseInt(request.getParameter("page")) - 1;
		Integer size = Integer.parseInt(request.getParameter("rows"));
		EasyUiTable easyUiTable = new EasyUiTable();
		try {
			easyUiTable.setTotal(cardService.getStaffInfoCount(staff_no,
					staff_name, staff_dept));
			easyUiTable.setRows(cardService.getStaffInfo(staff_no, staff_name,
					staff_dept, page, size));
			printHttpServletResponse(new Gson().toJson(easyUiTable));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行人卡绑定方法之前先判断卡号和工号是否存在
	 * 
	 */
	public void isExistCardnumsAndUser() {
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_nums");
		String[] card_nums = card_num.split(",");
		String staff_num = request.getParameter("staff_num");
		boolean flag = cardService.isExistUserAndCard(card_nums[0], staff_num);
		JsonResult result = new JsonResult();
		result.setSuccess(flag);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * Description:人卡绑定 date:2016年3月31日
	 */
	public void people_bound_card() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		final String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("card_num");
		final String[] card_nums = card_num.split(",");
		final String staff_num = getHttpServletRequest().getParameter(
				"staff_num");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(card_num, staff_num)) {// 判断参数是否为空
			result.setMsg("绑定失败");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		result = cardService.people_bound_card(admin_id, card_nums, staff_num);

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	public void showBoundcard() {
		HttpServletRequest request = getHttpServletRequest();
		String yhdm = request.getParameter("yhdm");
		Grid<BoundCard> boundCard = cardService.selectBoundCardByYHDM(yhdm);
		printHttpServletResponse(GsonUtil.toJson(boundCard));
	}

	// 发卡信息查询
	public void querySentcardInfo() {
		SendCardModel sendCardModel = new SendCardModel();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		String type = request.getParameter("type");
		String query_type = request.getParameter("query_type");
		List<QuerySentcardInfo> list = null;
		if ("1".equals(type)) {
			list = cardService.querySentcardInfo(param);
		} else if ("2".equals(type)) {
			list = cardService.querySentcardInfoByEmpnum(query_type);
		}

		try {
			if (list != null) {
				sendCardModel.setCard_info(list);
				sendCardModel.setResult_code("0");
				sendCardModel.setResult_msg("成功");
				sendCardModel.setCell_id("03");
			} else {
				sendCardModel.setCard_info(list);
				sendCardModel.setResult_code("1");
				sendCardModel.setResult_msg("失败");
				sendCardModel.setCell_id("03");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		printHttpServletResponse(GsonUtil.toJson(sendCardModel));
	}

	// 发卡状态上报
	public void modifyCardstate() {
		String card_num = getHttpServletRequest().getParameter("card_num");
		String card_state = getHttpServletRequest().getParameter("card_state");
		CardStateResult cardStateResult = new CardStateResult();
		try {
			cardService.modifyCardState(card_num, card_state);
			cardStateResult.setResult_code("0");
			cardStateResult.setResult_msg("发卡成功");
		} catch (Exception e) {
			e.printStackTrace();
			cardStateResult.setResult_code("1");
			cardStateResult.setResult_msg("发卡不成功");

		}
		printHttpServletResponse(GsonUtil.toJson(cardStateResult));
	}

	// 导出发卡信息
	public void exportSentCards() {
		HttpServletResponse response = getHttpServletResponse();
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		List<QuerySentcardInfo> sendCardsList = cardService
				.querySentcardInfo(param);
		for (int i = 0; i < sendCardsList.size(); i++) {
			sendCardsList.get(i).setCell_id("03");
			sendCardsList.get(i).setCard_state("未发卡");
		}
		String jsonList = GsonUtil.toJson(sendCardsList);
		// list中的map为 ： 列键----列值的方式
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		OutputStream out = null;

		Field[] fields = QuerySentcardInfo.class.getDeclaredFields();
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();

		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			if ("emp_name".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工姓名");
			} else if ("staff_no".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工工号");
			} else if ("bm_no".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工部门");
			} else if ("emp_position".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工岗位");
			} else if ("card_num".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡号");
			} else if ("card_state".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡片状态");
			} else if ("binding_time".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("绑卡时间");
			} else if ("cell_id".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("小区号");
			}
		}
		// 列数据键
		String[] cols = new String[colList.size()];
		// 列表题
		String[] colTitles = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		try {
			HttpServletRequest request = getHttpServletRequest();
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "未发卡信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("未发卡信息表",
					colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 挂失卡片
	 * 
	 * @author xuewen.deng 2016.7.12
	 * 
	 */
	public void reportLoss() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("cardNums");
		// String[] card_nums = card_num.split(",");

		JsonResult result = new JsonResult();
		Integer r = cardService.reportLoss(card_num, admin_id);
		if (r == 0) {

			result.setMsg("挂失成功");
			result.setSuccess(true);
		} else if (r == 1 || r == 3) {
			result.setMsg("挂失失败");
			result.setSuccess(false);
		} else if (r == 2) {
			result.setMsg("部分卡片挂失失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

		// result=cardService.people_bound_card(admin_id,card_nums, staff_num);
		// printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 解挂卡片
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public void dissLoss() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("cardNums");
		// String[] card_nums = card_num.split(",");

		JsonResult result = new JsonResult();
		Integer r = cardService.dissLoss(card_num, admin_id);
		if (r == 0) {

			result.setMsg("解挂成功");
			result.setSuccess(true);
		} else if (r == 1 || r == 3) {
			result.setMsg("解挂失败");
			result.setSuccess(false);
		} else if (r == 2) {
			result.setMsg("部分卡片解挂失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 退卡（回收卡号）
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public void returnCard() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("cardNums");
		// String[] card_nums = card_num.split(",");

		JsonResult result = new JsonResult();
		Integer r = cardService.returnCard(card_num, admin_id);
		if (r == 0) {

			result.setMsg("回收卡号成功");
			result.setSuccess(true);
		} else if (r == 1 || r == 3) {
			result.setMsg("回收卡号失败");
			result.setSuccess(false);
		} else if (r == 2) {
			result.setMsg("部分卡号回收失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 报损
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public void reportDamage() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("cardNums");
		// String[] card_nums = card_num.split(",");

		JsonResult result = new JsonResult();
		Integer r = cardService.reportDamage(card_num, admin_id);
		if (r == 0) {

			result.setMsg("报损成功");
			result.setSuccess(true);
		} else if (r == 1 || r == 3) {
			result.setMsg("报损失败");
			result.setSuccess(false);
		} else if (r == 2) {
			result.setMsg("部分卡片报损失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓学文
	 * @创建时间: 2016-9-28
	 * @功能描述: 下载批量导入卡片模板
	 * @参数描述:
	 */
	public void downloadImportCard() {
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
		ExcelExportTemplate excelExport = new ExcelExportTemplate();
		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();

		List<CardType> cardTypeList = cardService.getAllCardType();

		// List<String> list = dutyUserService.getAllDutyName();//获取全部排班类型
		// List<TitleRowCell> tRCs = new ArrayList<TitleRowCell>();
		// List<String> list = employeeService.getAllStaffDept();//获取全部部门代码
		TitleRowCell t1t = new TitleRowCell("卡片押金 ", false);
		TitleRowCell t2t = new TitleRowCell("卡片类型 ", true);
		for (CardType cardT : cardTypeList) {
			if(cardT.getCard_type_id() != 1){
				t2t.addSuggest(cardT.getType_name());// 填好卡片类型下拉框
			}
		}
		TitleRowCell t3t = new TitleRowCell("卡号 ", true);
		TitleRowCell t4t = new TitleRowCell("卡片有效期 ", true);
		/*
		 * Calendar now = Calendar.getInstance(); String dateStr =
		 * (now.get(Calendar.YEAR) + 10) + "-" + (now.get(Calendar.MONTH) + 1) +
		 * "-" + now.get(Calendar.DAY_OF_MONTH); t4t.addSuggest(dateStr);//
		 * 填好卡片有效期下拉框
		 */
		TitleRowCell t5t = new TitleRowCell("描述", false);

		titleRowCells.add(t1t);
		titleRowCells.add(t2t);
		titleRowCells.add(t3t);
		titleRowCells.add(t4t);
		titleRowCells.add(t5t);
		excelExport.createLongTitleRow(titleRowCells);
		try {
			HttpServletRequest request = getHttpServletRequest();
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "批量导入卡片信息模版.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			excelExport.getWorkbook().write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @创建人　: 邓学文
	 * @创建时间: 2016-9-29
	 * @功能描述: 根据模板批量导入卡片信息
	 */
	public String uploadCardByTemplate() {

		HttpServletRequest request = getHttpServletRequest();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(uploadCardFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getLastRowNum();
			List<Card> cardList = new ArrayList<Card>();

			if (totalRow < 6) {
				request.setAttribute("message", "导入失败，文件内容为空！");
				return "success";
			}

			String propertyFile = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
			Properties properties = PropertyUtils.readProperties(propertyFile);
			String lisenceKey = properties.getProperty("lisenceKey");
			String[] key = lisenceKey.split(";");
			if(Integer.valueOf(key[2])>=0){
				if ((cardService.getNormalCount() + (totalRow - 5)) > Integer
						.valueOf(key[2])) {
					request.setAttribute("message", "导入失败，卡号数量已达到授权码限制数量");
					return "success";
				}
			}

			for (int i = 6; i <= totalRow; i++) {
				HSSFRow row = sheet.getRow(i);
				Card card = new Card();

				String card_pledge = null;
				CardType card_type = null;
				String card_validity = "";
				String card_num = "";
				String description = "";
				String now="";
				
				Cell card_pledge_cell = row.getCell(0);// 卡片押金
				/*
				 * if (null != card_pledge_cell) {
				 * card_pledge_cell.setCellType(Cell.CELL_TYPE_STRING);
				 * card_pledge = card_pledge_cell.getStringCellValue(); if (null
				 * == card_pledge || "".equals(card_pledge)) {
				 * request.setAttribute("message", "导入失败，表格第" + (i + 1) +
				 * "行的卡片押金不能为空。"); return "success"; } } else {
				 * request.setAttribute("message", "导入失败，表格第" + (i + 1) +
				 * "行的卡片押金不能为空。"); return "success"; }
				 */
				if (null != card_pledge_cell) {
					card_pledge_cell.setCellType(Cell.CELL_TYPE_STRING);
					card_pledge = card_pledge_cell.getStringCellValue();
				}
				if (card_pledge_cell != null
						&& card_pledge != null
						&& (!card_pledge
								.matches("^(([1-9]{1}\\d{0,15})|([0]{1}))(\\.(\\d){0,4})?$"))) {// ^(([1-9]{1}\\d{1,15})|([0]{1}))(\\.(\\d){0,2})?$
					request.setAttribute("message", "表格第" + (i + 1)
							+ "行的卡片押金格式不正确。");
					return "success";
				}

				Cell card_type_id_cell = row.getCell(1);// 卡片类型
				if (card_type_id_cell == null) {
					request.setAttribute("message", "导入失败，表格第" + (i + 1)
							+ "行的卡片类型不能为空。");
					return "success";
				}
				card_type_id_cell.setCellType(Cell.CELL_TYPE_STRING);
				card_type = cardService
						.getCardTypeIdByTypeName(card_type_id_cell
								.getStringCellValue());// 获取卡片类型
				if (null != card_type) {
					if (null == card_type.getType_dm()
							|| "".equals(card_type.getType_dm())) {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的卡片类型不正确。");
						return "success";
					}

				} else {
					request.setAttribute("message", "导入失败，表格第" + (i + 1)
							+ "行的卡片类型不正确。");
					return "success";
				}

				Cell card_num_cell = row.getCell(2);// 卡号
				if (null != card_num_cell) {
					card_num_cell.setCellType(Cell.CELL_TYPE_STRING);
					card_num = card_num_cell.getStringCellValue();
					if (null == card_num || "".equals(card_num)) {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的卡号不能为空。");
						return "success";
					} else {
						if (!card_num.matches("^\\d*[1-9]\\d*$")) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的卡号格式错误。");
							return "success";
						}
						if (card_num.length() > 10) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的卡号格式错误。");
							return "success";
						}

						Long long_cardNum = Long.valueOf(card_num);
						/*if (card_type.getCard_type_id() == 1) {
							if (long_cardNum < 1 || long_cardNum > 32767) {
								request.setAttribute("message", "导入失败，表格第"
										+ (i + 1)
										+ "行的卡号范围错误。手机光钥匙卡号范围:1~32767");
								return "success";
							}
						} else*/ if (card_type.getCard_type_id() == 2) {
							if (long_cardNum < 1 || long_cardNum >  Long.parseLong("2147483647")) {
								request.setAttribute("message", "导入失败，表格第"
										+ (i + 1)
										+ "行的卡号范围错误。光子卡卡号范围:1~2147483647");
								return "success";
							}
						} else if (card_type.getCard_type_id() == 3) {
							if (long_cardNum < 1
									|| long_cardNum > Long.parseLong("2147483647")) {
								request.setAttribute("message", "导入失败，表格第"
										+ (i + 1)
										+ "行的卡号范围错误。IC卡卡号范围:1~2147483647");
								return "success";
							}
						}

						if (cardService.validCardNum(card_num) > 0) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的卡号" + card_num + "已存在");
							return "success";
						}
					}
				} else {
					request.setAttribute("message", "导入失败，表格第" + (i + 1)
							+ "行的卡号不能为空。");
					return "success";
				}

				Cell card_validity_cell = row.getCell(3);// 卡有效期
				if (null != card_validity_cell) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd");

					Date dt;
					try {
						dt = HSSFDateUtil.getJavaDate(card_validity_cell
								.getNumericCellValue());
						card_validity = dateformat.format(dt);
						now=dateformat.format(new Date());
					} catch (Exception e) {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的卡有效期格式不正确。");
						e.printStackTrace();
						return "success";
					}

					if (null == card_validity || "".equals(card_validity)) {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的卡有效期不能为空。");
						return "success";
					} else {
						if (DateUtil.getDate(card_validity).getTime() < (DateUtil.getDate(now)
								.getTime())) {
							request.setAttribute("message", "导入失败，表格第"
									+ (i + 1) + "行的卡有效期不能为空、不能小于当前系统时间。");
							return "success";
						}
					}

				} else {
					request.setAttribute("message", "导入失败，表格第" + (i + 1)
							+ "行的卡有效期不能为空。");
					return "success";
				}

				Cell description_cell = row.getCell(4);// 卡片描述
				if (null != description_cell) {
					description_cell.setCellType(Cell.CELL_TYPE_STRING);
					description = description_cell.getStringCellValue();
				}

				if (!card_validity
						.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")) {
					request.setAttribute("message", "表格第" + (i + 1)
							+ "行的卡有效期格式不正确！");
					return "success";
				}
				if (description.length() > 150) {
					request.setAttribute("message", "表格第" + (i + 1)
							+ "行的描述超过150个字符！");
					return "success";
				}

				for (Card vCard : cardList) {
					if (card_num.equals(vCard.getCard_num())) {
						request.setAttribute("message", "导入失败，表格第" + (i + 1)
								+ "行的卡号" + card_num + "重复");
						return "success";
					}
				}

				// holidayType = (HolidayType) typeList.get(0);

				// String holiday_type_num = holidayType.getHoliday_type_num();
				User loginUser = (User) request.getSession().getAttribute(
						GlobalConstant.LOGIN_USER);

				card.setCard_pledge(card_pledge);
				card.setCard_type_id(String.valueOf(card_type.getCard_type_id()));
				card.setCard_num(card_num);
				card.setCreate_user(loginUser.getYhMc());
				card.setCreate_time(DateUtil.getDateTimeString(new Date()));
				card.setCard_validity(card_validity);
				card.setState_dm("00");// 初始状态默认设为00(未绑定)
				card.setValidity_flag("0");// 默认设为有效数据
				card.setDescription(description);

				cardList.add(card);
			}

			int result = cardService.batchAddCard(cardList);
			if (result == 0) {
				request.setAttribute("message", "全部导入成功!");
			} else if (result == 1) {
				request.setAttribute("message", "全部导入失败!");
			} else if (result == 2) {
				request.setAttribute("message", "部分导入失败!");
			}

		} catch (Exception e) {
			request.setAttribute("message",
					"导入文件错误，或者选择ms office的.xls格式文档可以解决问题！");
			return "success";
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";

	}

	public File getUploadCardFile() {
		return uploadCardFile;
	}

	public void setUploadCardFile(File uploadCardFile) {
		this.uploadCardFile = uploadCardFile;
	}

	/**
	 * 解绑卡片
	 * 
	 * @author xuewen.deng
	 * 
	 */
	public void unBoundCard() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		String card_num = getHttpServletRequest().getParameter("cardNums");
		// String[] card_nums = card_num.split(",");

		// 消费系统：解绑前删除下发的名单
		String[] card_nums = card_num.split(",");
		for (String card : card_nums) {
			List<String> nums = cDeviceService.cardIfExistList(card);
			for (int i = 0; i < nums.size(); i++) {
				String device_num = nums.get(i);
				cDeviceService.insertNameTask(device_num, card, "1", "9");
			}
		}

		JsonResult result = new JsonResult();
		Integer r = cardService.unBoundCard(card_num, admin_id);
		if (r == 0) {

			result.setMsg("解绑成功");
			result.setSuccess(true);
		} else if (r == 1 || r == 3) {
			result.setMsg("解绑失败");
			result.setSuccess(false);
		} else if (r == 2) {
			result.setMsg("部分卡片解绑失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 导出卡片信息
	 * 
	 * @author xuewen.deng
	 */
	public void exportCards() {
		HttpServletResponse response = getHttpServletResponse();
		String data = getHttpServletRequest().getParameter("data");
		Param param = GsonUtil.toBean(data, Param.class);
		List<StaffCard> cardsList = cardService.exportAllByParam(param);
		String jsonList = GsonUtil.toJson(cardsList);
		// list中的map为 ： 列键----列值的方式
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		OutputStream out = null;

		Field[] fields = StaffCard.class.getDeclaredFields();
		Field[] fatherFields = Card.class.getSuperclass().getDeclaredFields();

		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();

		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			if ("staff_no".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工工号");
			} else if ("staff_name".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工名称");
			} else if ("card_num".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡号");
			} else if ("card_pledge".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡片押金（单位/元）");
			} else if ("card_type_id".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡片类型");
			} else if ("card_validity".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡有效期");
			} else if ("state_name".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("卡片状态");
			} else if ("card_validity_state".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("有效期状态");
			} else if ("create_time".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("创建时间");
			}/*
			 * else if ("create_user".equals(fieldName)) {
			 * colList.add(fieldName); colTitleList.add("创建人"); } else if
			 * ("description".equals(fieldName)) { colList.add(fieldName);
			 * colTitleList.add("描述"); }
			 */
		}
		for (int i = 0; i < fatherFields.length; i++) {
			String fatherFieldName = fatherFields[i].getName();
			if ("create_user".equals(fatherFieldName)) {
				colList.add(fatherFieldName);
				colTitleList.add("创建人员");
			} else if ("description".equals(fatherFieldName)) {
				colList.add(fatherFieldName);
				colTitleList.add("描述");
			}
		}
		// 列数据键
		String[] cols = new String[colList.size()];
		// 列表题
		String[] colTitles = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		try {
			HttpServletRequest request = getHttpServletRequest();
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "卡片信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("卡片信息表",
					colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对接光钥匙
	 * param:staffNo
	 * result:staffNo,staffName,photonId
	 * by gengji.yang
	 */
	public void getPhoton(){
		HttpServletRequest request = getHttpServletRequest();
		String staffNum = getHttpServletRequest().getParameter("staffNum");
		String cardNum=cardService.getPhoton(staffNum);
		printHttpServletResponse(cardNum);
	}

}
