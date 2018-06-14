package com.kuangchi.sdd.interfaceConsole.cardSender.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.card.dao.ICardDao;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardOperaSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.ResultCard;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.CardSynchronizeService;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.EmpCardSenSyncService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.PropertyUtils;

/**
 * 同步发卡信息接口（供c/s用）
 * 
 * @author xuewen.deng
 * 
 */
@Controller("cardSynchronizeAction")
public class CardSynchronizeAction extends BaseActionSupport {

	@Resource(name = "cardSynchronizeServiceImpl")
	private CardSynchronizeService cardSynchronizeService;
	@Resource(name = "cardServiceImpl")
	private ICardService cardService;
	@Resource(name = "empCardSenSyncServiceImpl")
	private EmpCardSenSyncService empCardSenSyncService;
	@Resource(name = "cardDaoImpl")
	private ICardDao cardDao;

	/**
	 * 新增绑卡信息
	 * 
	 * @author xuewen.deng
	 */
	public void addBoundCardInfo() {

		HttpServletRequest request = getHttpServletRequest();
		String cardOpModelListStr = request.getParameter("oprateModelList");
		ResultCard myResult = new ResultCard();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateCModelTMap = gson.fromJson(
				cardOpModelListStr, ArrayList.class);
		List<CardOperaSyncModel> operacardSyncModelList = getOprateCardModel(oprateCModelTMap);
		if (null == operacardSyncModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}

		List<CardOperaSyncModel> resultList = new ArrayList<CardOperaSyncModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (CardOperaSyncModel opCModel : operacardSyncModelList) {

			JsonResult result = new JsonResult();
			Map<String, Object> resultMap = new HashMap<String, Object>();

			if (null == opCModel.getCardNo()
					|| "".equals(opCModel.getCardNo().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getPersonId()
					|| "".equals(opCModel.getPersonId().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getCardStatus()
					|| "".equals(opCModel.getCardStatus().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

			if (null == opCModel.getCardType()
					|| "".equals(opCModel.getCardType().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getEndDate()
					|| "".equals(opCModel.getEndDate().trim())) {
				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

			// 做好判断，封装好model准备新增
			// 判断人是否存在，若不存在直接报错
			String staffNum = empCardSenSyncService.getStaffNumByID(opCModel
					.getPersonId());
			if (null == staffNum) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				opCModel.setBsId("");
				opCModel.setErrorCode("2");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

			// 判断卡片是否存在，若存在则判断是否已经绑定（若绑定则报错，没绑定则绑定此人），若不存在则直接新增一张卡片并绑定此人
			if (cardSynchronizeService.isExistCard(opCModel.getCardNo())) {// 若存在此卡片

				if (cardService.validBoundCard(opCModel.getCardNo()) > 0) {// 判断是否已经绑定，若已经绑定则判断绑定的人是否相同（若相同则成功，不同则返回父ID不存在）
					List<CardSyncModel> cL = cardSynchronizeService
							.queryIDByCardNum(opCModel.getCardNo());

					if (cL == null || cL.size() == 0 || cL.get(0) == null) {
						// cardService.modifyCardState(opCModel.getCardNo(),
						// "1");// 将卡片状态改为发卡不成功
						opCModel.setBsId("");
						opCModel.setErrorCode("1");// 失败
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
					/*
					 * if
					 * (opCModel.getPersonId().equals(cL.get(0).getStaff_num()))
					 * {// 判断人员编号是否相同
					 * 
					 * opCModel.setBsId(cL.get(0).getBc_id());
					 * resultList.add(opCModel); isExistSucc = 1; } else {
					 * 
					 * opCModel.setBsId(""); opCModel.setErrorCode("3");// 已存在
					 * resultList.add(opCModel); isExistFail = 1; continue; }
					 */
					opCModel.setBsId("");
					opCModel.setErrorCode("3");// 已存在
					resultList.add(opCModel);
					isExistFail = 1;
					continue;
				} else {// 若未绑定则绑定此人
					String[] strArr = new String[1];
					strArr[0] = opCModel.getCardNo();
					cardService.people_bound_card("发卡软件_admin", strArr,
							opCModel.getPersonId());
					List<CardSyncModel> cardSyncModList = cardSynchronizeService
							.queryIDByCardNum(opCModel.getCardNo());
					if (cardSyncModList == null || cardSyncModList.size() == 0
							|| cardSyncModList.get(0) == null) {
						// 将卡片状态改为发卡不成功
						// cardService.modifyCardState(opCModel.getCardNo(),
						// "1");
						opCModel.setBsId("");
						opCModel.setErrorCode("1");// 失败
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
					// 修改卡片信息
					Card cardCurrent = cardService.getCardByCardNum(Integer
							.valueOf(opCModel.getCardNo()));// 获得当前卡片信息 //
															// 修改卡片信息
					cardCurrent.setCard_type_id(getType_dm(opCModel
							.getCardType()));// 设置卡片类型（先将卡片类型编号转变为卡片类型代码）

					cardCurrent.setCard_validity(opCModel.getEndDate());
					cardService.modifyCard(cardCurrent, "发卡软件_admin");// 修改bs端卡片信息

					cardService.modifyCardState_AllState(opCModel.getCardNo(),
							opCModel.getCardStatus());
					opCModel.setBsId(cardSyncModList.get(0).getBc_id());
					resultList.add(opCModel);
					isExistSucc = 1;
				}
			} else {
				// 添加授权码限制
				String propertyFile = request
						.getSession()
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
				Properties properties = PropertyUtils
						.readProperties(propertyFile);
				String lisenceKey = properties.getProperty("lisenceKey");
				String[] key = lisenceKey.split(";");
				if (Integer.valueOf(key[2]) >= 0) {
					if (cardService.getNormalCount() >= Integer.valueOf(key[2])) {
						opCModel.setBsId("");
						opCModel.setErrorCode("1");// 失败
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
				}

				// 新增一张卡片并绑定此人
				Card cardS = new Card();
				cardS.setCard_num(opCModel.getCardNo());
				// cardS.setCard_pledge("0");
				cardS.setCard_type_id(getType_id(opCModel.getCardType()));
				cardS.setCard_validity(opCModel.getEndDate());
				cardS.setCard_validity_state("0");
				cardS.setCreate_time(DateUtil.getSysDateTime());
				cardS.setCreate_user("发卡软件_admin");
				cardS.setState_dm("00");
				cardS.setValidity_flag("0");

				if (cardService.addNewCard(cardS)) {// 新增一张卡片
					// 人卡绑定
					String[] strArr = new String[1];
					strArr[0] = opCModel.getCardNo();
					JsonResult jResult = cardService.people_bound_card(
							"发卡软件_admin", strArr, opCModel.getPersonId());
					if (jResult.isSuccess()) {
						// 修改卡片状态
						cardService.modifyCardState_AllState(
								opCModel.getCardNo(), opCModel.getCardStatus());
						// 加入卡片历史表
						/*
						 * cardDao.addCardHistory2(opCModel.getCardNo(),
						 * opCModel .getPersonId(), opCModel.getCardStatus(),
						 * DateUtil.getDateString(new Date(),
						 * "yyyy-MM-dd HH:mm:ss"), "发卡软件_admin");//
						 * 修改卡片状态成功后，往卡历史表里插入数据
						 */// cardService.modifyCardState(opCModel.getCardNo(),
							// "0");
						List<CardSyncModel> cardSMList = cardSynchronizeService
								.queryIDByCardNum(opCModel.getCardNo());
						if (cardSMList == null || cardSMList.size() == 0
								|| cardSMList.get(0) == null) {
							// 将卡片状态改为发卡不成功
							/*
							 * cardService.modifyCardState(opCModel.getCardNo(),
							 * "1");
							 */
							opCModel.setBsId("");
							opCModel.setErrorCode("1");// 失败
							resultList.add(opCModel);
							isExistFail = 1;
							continue;
						}
						opCModel.setBsId(cardSMList.get(0).getBc_id());
						resultList.add(opCModel);
						isExistSucc = 1;
					} else {
						// 将卡片状态改为发卡不成功
						// cardService.modifyCardState(opCModel.getCardNo(),
						// "1");
						opCModel.setBsId("");
						opCModel.setErrorCode("1");// 失败
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}

				} else {
					opCModel.setBsId("");
					opCModel.setErrorCode("1");// 失败
					resultList.add(opCModel);
					isExistFail = 1;
					continue;
				}

			}

		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部新增成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部新增失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分新增失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("新增失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));

	}

	/**
	 * 修改绑卡信息
	 * 
	 * @author xuewen.deng
	 */
	public void updateBoundCardInfo() {

		HttpServletRequest request = getHttpServletRequest();
		String cardOpModelListStr = request.getParameter("oprateModelList");
		ResultCard myResult = new ResultCard();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateCModelTMap = gson.fromJson(
				cardOpModelListStr, ArrayList.class);
		List<CardOperaSyncModel> operacardSyncModelList = getOprateCardModel(oprateCModelTMap);
		if (null == operacardSyncModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<CardOperaSyncModel> resultList = new ArrayList<CardOperaSyncModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (CardOperaSyncModel opCModel : operacardSyncModelList) {

			JsonResult result = new JsonResult();
			Map<String, Object> resultMap = new HashMap<String, Object>();

			if (null == opCModel.getCardNo()
					|| "".equals(opCModel.getCardNo().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getPersonId()
					|| "".equals(opCModel.getPersonId().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getCardStatus()
					|| "".equals(opCModel.getCardStatus().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

			if (null == opCModel.getCardType()
					|| "".equals(opCModel.getCardType().trim())) {

				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			if (null == opCModel.getEndDate()
					|| "".equals(opCModel.getEndDate().trim())) {
				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

			// 做好判断，封装好model准备修改
			// 判断卡片状态，如果为挂失，则直接挂失卡片，如果为冻结，则直接冻结卡片，如果为正常，
			// 则查询旧的卡片状态（若旧的卡片状态是挂失则解挂，若旧的卡片状态是冻结则解冻）
			if ("40".equals(opCModel.getCardStatus())
					|| "401".equals(opCModel.getCardStatus())) {// 若状态为挂失，则挂失卡片
				Integer r = cardService.reportLoss(opCModel.getCardNo(),
						"发卡软件_admin");
				if (r == 0) {// 挂失成功
					resultList.add(opCModel);
					isExistSucc = 1;
				} else {
					opCModel.setBsId("");
					opCModel.setErrorCode("1");
					resultList.add(opCModel);
					isExistFail = 1;
					continue;
				}
			} else if ("352".equals(opCModel.getCardStatus())
					|| "252".equals(opCModel.getCardStatus())) {// 若状态为冻结，则冻结卡片
				Integer r2 = cardService.frozenCard(opCModel.getCardNo(),
						"发卡软件_admin");
				if (r2 == 0) {// 冻结成功
					resultList.add(opCModel);
					isExistSucc = 1;
				} else {
					opCModel.setBsId("");
					opCModel.setErrorCode("1");
					resultList.add(opCModel);
					isExistFail = 1;
					continue;
				}
			} else if ("10".equals(opCModel.getCardStatus())) {// 若状态为未发卡，则直接更新卡片状态
				cardService.updateCardState(opCModel.getCardNo(), "10");
				// 加入卡片历史表
				cardDao.addCardHistory2(opCModel.getCardNo(), opCModel
						.getPersonId(), "10", DateUtil.getDateString(
						new Date(), "yyyy-MM-dd HH:mm:ss"), "发卡软件_admin");// 修改卡片状态成功后，往卡历史表里插入数据
				resultList.add(opCModel);
				isExistSucc = 1;
			} else if ("30".equals(opCModel.getCardStatus())) {// 若状态为未发卡，则直接更新卡片状态
				cardService.reportDamage(opCModel.getCardNo(), "发卡软件_admin");
				resultList.add(opCModel);
				isExistSucc = 1;
			} else if ("20".equals(opCModel.getCardStatus())) {// 若状态为正常，则判断原来旧的卡片状态

				List<CardSyncModel> cardSMList = cardSynchronizeService
						.queryIDByCardNum(opCModel.getCardNo());
				if (cardSMList == null || cardSMList.size() == 0
						|| cardSMList.get(0) == null) {
					opCModel.setBsId("");
					opCModel.setErrorCode("5");// 编号不存在
					resultList.add(opCModel);
					isExistFail = 1;
					continue;
				}
				if ("20".equals(cardSMList.get(0).getState_dm())) {// 如果卡片状态为正常则修改成功
					resultList.add(opCModel);
					isExistSucc = 1;
				} else if ("40".equals(cardSMList.get(0).getState_dm())
						|| "401".equals(cardSMList.get(0).getState_dm())) {// 如果卡片状态为挂失则解挂
					Integer r3 = cardService.dissLoss(opCModel.getCardNo(),
							"发卡软件_admin");
					if (r3 == 0) {// 解挂成功
						resultList.add(opCModel);
						isExistSucc = 1;
					} else {
						opCModel.setBsId("");
						opCModel.setErrorCode("1");
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
				} else if ("352".equals(cardSMList.get(0).getState_dm())
						|| "252".equals(cardSMList.get(0).getState_dm())) {// 如果卡片状态为冻结则解冻
					Integer r4 = cardService.unfreezeCard(opCModel.getCardNo(),
							"发卡软件_admin");
					if (r4 == 0) {// 解冻成功
						resultList.add(opCModel);
						isExistSucc = 1;
					} else {
						opCModel.setBsId("");
						opCModel.setErrorCode("1");
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
				} else if ("30".equals(cardSMList.get(0).getState_dm())) {// 若卡片状态为报损，则报损修复卡片
					Integer r5 = cardService.damageRepair(opCModel.getCardNo(),
							"发卡软件_admin");
					if (r5 == 0) {// 报损修复成功
						resultList.add(opCModel);
						isExistSucc = 1;
					} else {
						opCModel.setBsId("");
						opCModel.setErrorCode("1");
						resultList.add(opCModel);
						isExistFail = 1;
						continue;
					}
				} else {
					cardService.updateCardState(opCModel.getCardNo(), "20");
					// 加入卡片历史表
					cardDao.addCardHistory2(opCModel.getCardNo(), opCModel
							.getPersonId(), "20", DateUtil.getDateString(
							new Date(), "yyyy-MM-dd HH:mm:ss"), "发卡软件_admin");// 修改卡片状态成功后，往卡历史表里插入数据
					resultList.add(opCModel);
					isExistSucc = 1;
				}

			} else {
				opCModel.setBsId("");
				opCModel.setErrorCode("1");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}

		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部修改成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部修改失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分修改失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("修改失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));
	}

	/**
	 * 删除绑卡信息
	 * 
	 * @author xuewen.deng
	 */
	public void deleteBoundCardInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String cardOpModelListStr = request.getParameter("oprateModelList");
		ResultCard myResult = new ResultCard();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateCModelTMap = gson.fromJson(
				cardOpModelListStr, ArrayList.class);
		List<CardOperaSyncModel> operacardSyncModelList = getOprateCardModel(oprateCModelTMap);
		if (null == operacardSyncModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<CardOperaSyncModel> resultList = new ArrayList<CardOperaSyncModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (CardOperaSyncModel opCModel : operacardSyncModelList) {
			JsonResult result = new JsonResult();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (null == opCModel.getBsId()
					|| "".equals(opCModel.getBsId().trim())) {
				opCModel.setBsId("");
				opCModel.setErrorCode("4");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			String cardNub = cardSynchronizeService
					.getCardNumByBoundID(opCModel.getBsId());
			if (cardNub == null) {
				opCModel.setBsId("");
				opCModel.setErrorCode("5");
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
			Integer r = cardService.returnCard(cardNub, "发卡软件_admin");
			if (r == 0) {// 成功
				resultList.add(opCModel);
				isExistSucc = 1;
			} else {
				// 将卡片状态改为发卡不成功
				opCModel.setBsId("");
				opCModel.setErrorCode("1");// 失败
				resultList.add(opCModel);
				isExistFail = 1;
				continue;
			}
		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部删除成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部删除失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分删除失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("删除失败");
		}
		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));
	}

	/**
	 * 获取所有绑卡信息
	 * 
	 * @author xuewen.deng
	 */
	public void queryBoundCardInfo() {
		ResultCard result = new ResultCard();
		List<CardSyncModel> cardSyncList = null;
		try {
			cardSyncList = cardSynchronizeService.queryBoundCardInfo();
			List<CardOperaSyncModel> opCardList = new ArrayList<CardOperaSyncModel>();
			for (CardSyncModel cardSyncModel : cardSyncList) {
				CardOperaSyncModel cardOperaSyncModel = new CardOperaSyncModel();
				cardOperaSyncModel.setBsId(cardSyncModel.getBc_id());
				cardOperaSyncModel.setCardNo(cardSyncModel.getCard_num());
				cardOperaSyncModel.setCardType(getType(cardSyncModel
						.getType_dm()));
				cardOperaSyncModel.setCardStatus(cardSyncModel.getState_dm());
				cardOperaSyncModel.setEndDate(cardSyncModel.getCard_validity());
				cardOperaSyncModel.setPersonId(cardSyncModel.getStaff_num());

				opCardList.add(cardOperaSyncModel);
			}
			result.setCode("0");
			result.setMsg("查询成功");
			result.setResultList(opCardList);
		} catch (Exception e) {
			result.setCode("1");
			result.setMsg("查询失败");
			e.printStackTrace();
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	@Override
	public Object getModel() {
		return null;
	}

	private String getType(String type_dm) {
		if (type_dm != null) {
			if ("A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2".equals(type_dm)) {
				return "2";// 手机光钥匙
			} else if ("7E8F99A0-068F-4579-A839-AC04E875ACC8".equals(type_dm)) {
				return "0";// 光子卡
			} else if ("02A0343C-AC4B-499E-BC60-FB5A7BA2582A".equals(type_dm)) {
				return "1";// IC卡
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private String getType_dm(String type) {
		if (type != null) {
			if ("2".equals(type)) {
				// return "A995A2CC-2A2C-4942-BC23-5F6A4AF0DFC2";// 手机光钥匙
				return GlobalConstant.TYPE_DM1;
			} else if ("0".equals(type)) {
				// return "7E8F99A0-068F-4579-A839-AC04E875ACC8";// 光子卡
				return GlobalConstant.TYPE_DM2;
			} else if ("1".equals(type)) {
				// return "02A0343C-AC4B-499E-BC60-FB5A7BA2582A";// IC卡
				return GlobalConstant.TYPE_DM3;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private String getType_id(String type) {
		if (type != null) {
			if ("2".equals(type)) {
				return "1";// 手机光钥匙
			} else if ("0".equals(type)) {
				return "2";// 光子卡
			} else if ("1".equals(type)) {
				return "3";// IC卡
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private List<CardOperaSyncModel> getOprateCardModel(
			List<LinkedTreeMap> oprateEModelTMap) {
		if (oprateEModelTMap != null) {
			try {
				List<CardOperaSyncModel> oprateEmpModelList2 = new ArrayList();
				for (int i = 0; i < oprateEModelTMap.size(); i++) {
					LinkedTreeMap map = oprateEModelTMap.get(i);
					CardOperaSyncModel cardOperaSyncModel2 = new CardOperaSyncModel();
					cardOperaSyncModel2.setId(map.get("id") == null ? "" : map
							.get("id").toString().trim());
					cardOperaSyncModel2.setBsId(map.get("bsId") == null ? ""
							: map.get("bsId").toString().trim());
					cardOperaSyncModel2
							.setCardNo(map.get("cardNo") == null ? "" : map
									.get("cardNo").toString().trim());
					cardOperaSyncModel2
							.setPersonId(map.get("personId") == null ? "" : map
									.get("personId").toString().trim());
					cardOperaSyncModel2
							.setCardType(map.get("cardType") == null ? "" : map
									.get("cardType").toString().trim());
					cardOperaSyncModel2
							.setCardStatus(map.get("cardStatus") == null ? ""
									: map.get("cardStatus").toString().trim());
					cardOperaSyncModel2
							.setEndDate(map.get("endDate") == null ? "" : map
									.get("endDate").toString().trim());
					cardOperaSyncModel2
							.setErrorCode(map.get("errorCode") == null ? ""
									: map.get("errorCode").toString().trim());

					oprateEmpModelList2.add(cardOperaSyncModel2);
				}
				return oprateEmpModelList2;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {

			return null;
		}

	}

}
