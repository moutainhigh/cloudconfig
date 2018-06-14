package com.kuangchi.sdd.baseConsole.card.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.card.dao.ICardDao;
import com.kuangchi.sdd.baseConsole.card.model.AuthorityInfo;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.model.CardState;
import com.kuangchi.sdd.baseConsole.card.model.DeviceAndDoor;
import com.kuangchi.sdd.baseConsole.card.model.DeviceInfo2Model;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.card.model.QuerySentcardInfo;
import com.kuangchi.sdd.baseConsole.card.model.StaffCard;
import com.kuangchi.sdd.baseConsole.card.model.StaffInfo;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.CardSynchronizeService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.HttpUtil;

@Repository("cardDaoImpl")
public class CardDaoImpl extends BaseDaoImpl<Card> implements ICardDao {
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "cardSynchronizeServiceImpl")
	private CardSynchronizeService cardSynchronizeService;

	@Override
	public boolean addNewCard(Card card) {
		if (card != null) {
			Integer cardExistCount = isExistCardnums(card.getCard_num() == null ? ""
					: card.getCard_num());
			if (cardExistCount <= 0) {
				if (getSqlMapClientTemplate().insert("addNewCard", card) != null) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void modifyCard(Card card) {
		this.getSqlMapClientTemplate().update("modifyCard", card);
	}

	@Override
	public List<Card> getAllCard(String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList("getAllCard",
				mapParam);
	}

	@Override
	public List<CardState> getAllCardStates() {
		return this.getSqlMapClientTemplate().queryForList("getAllCardStates");
	}

	@Override
	public List<CardState> getAllCardStates2() {
		return this.getSqlMapClientTemplate().queryForList("getAllCardStates2");
	}

	@Override
	public List<CardState> getAllCardStates3() {
		return this.getSqlMapClientTemplate().queryForList("getAllCardStates3");
	}

	@Override
	public List<Card> getAllByParam(Param param, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("type_dm", param.getType_dm());// 现在用来存卡片类型
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());

		mapParam.put("state_dm", param.getState());
		return this.getSqlMapClientTemplate().queryForList("getAllByParam",
				mapParam);
	}

	@Override
	public List<StaffCard> getStaff_CardByParam(Param param, String Page,
			String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("type_dm", param.getType_dm());// 现在用来存卡片类型
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());

		mapParam.put("state_dm", param.getState());
		return this.getSqlMapClientTemplate().queryForList(
				"getStaff_CardByParam", mapParam);
	}

	@Override
	public void deleteCardById(String id) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", id);
		this.getSqlMapClientTemplate().update("deleteCardById", mapParam);
	}

	@Override
	public String getNameSpace() {
		return "common.Card";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public int getCardID() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getLastCardId");
	}

	@Override
	public Card getCardById(Integer card_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("card_id", card_id);
		return (Card) this.getSqlMapClientTemplate().queryForObject(
				"getCardById", map);
	}

	@Override
	public List<CardType> getAllCardType() {
		return this.getSqlMapClientTemplate().queryForList("getAllCardType");
	}

	@Override
	public String getCardTypeNameById(Integer type_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("card_type_id", type_id);
		CardType cardType = (CardType) this.getSqlMapClientTemplate()
				.queryForObject("getCardTypeNameById", map);
		return cardType.getType_name();

	}

	@Override
	public CardType getCardTypeIdByTypeName(String card_type_name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("card_type_name", card_type_name);
		return (CardType) this.getSqlMapClientTemplate().queryForObject(
				"getCardTypeIdByTypeName", map);
	}

	@Override
	public Integer getAllCardCount(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("type_dm", param.getType_dm());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("state_dm", param.getState());
		return queryCount("getAllCardCount", mapParam);
	}

	@Override
	public Integer getAllStaff_CardCount(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("type_dm", param.getType_dm());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("state_dm", param.getState());
		return queryCount("getAllStaff_CardCount", mapParam);
	}

	@Override
	public Card getCardById2(Integer card_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("card_id", card_id);
		return (Card) this.getSqlMapClientTemplate().queryForObject(
				"getCardById2", map);
	}

	@Override
	public List<StaffInfo> getStaffInfo(String staff_no, String staff_name,
			String staff_dept, Integer page, Integer size) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", staff_no);
		map.put("staff_name", staff_name);
		map.put("staff_dept", staff_dept);
		map.put("page", page * size);
		map.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getStaffInfo", map);
	}

	@Override
	public Integer getStaffInfoCount(String staff_no, String staff_name,
			String staff_dept) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", staff_no);
		map.put("staff_name", staff_name);
		map.put("staff_dept", staff_dept);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getStaffInfoCount", map);
	}

	@Transactional
	@Override
	public JsonResult people_bound_card(String admin_id, String[] card_num,
			String staff_num) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "人卡绑定");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", admin_id);

		JsonResult jsonresult = new JsonResult();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_num", staff_num);
		mapParam.put("create_user", admin_id);
		mapParam.put("description", "人卡绑定");

		try {
			for (String cardN : card_num) {
				mapParam.put("card_num", cardN);
				String typeDm = getCardTypeDm(cardN);
				if (GlobalConstant.TYPE_DM1.equals(typeDm)) {// 判断是否是手机光钥匙
					// 如果是手机光钥匙，则判断是否已经有绑定了手机光钥匙
					if (isExistTypeBoundCard(staff_num, cardN, typeDm)) {// 如果有，则直接报错
						jsonresult.setMsg("人卡绑定失败(一个人只能绑定一个手机光钥匙)");
						throw new RuntimeException();

					} else { // 如果没有，则直接增加

						if (this.getSqlMapClientTemplate().insert(
								"addCardBound", mapParam) != null) {
							jsonresult.setMsg("人卡绑定失败");
							throw new RuntimeException();
						} else {
							// 手机光钥匙人卡绑定之后直接将状态改为正常状态
							modifyCardState_AllState(cardN, "20");
							List<CardSyncModel> cardSMList = cardSynchronizeService
									.queryIDByCardNum(cardN);
							// 获取卡片绑定的人员代码
							String staffNum = null;
							if (cardSMList != null && cardSMList.size() > 0
									&& cardSMList.get(0) != null) {
								staffNum = cardSMList.get(0).getStaff_num();
							}
							Map<String, String> map = new HashMap<String, String>();
							map.put("cardNum", cardN);
							map.put("state_dm", "20");
							map.put("create_user", admin_id);
							map.put("staff_num", staffNum);
							map.put("date", DateUtil.getDateString(new Date(),
									"yyyy-MM-dd HH:mm:ss"));
							this.getSqlMapClientTemplate().insert(
									"addCardHistory", map);
						}

					}
				} else {

					if (this.getSqlMapClientTemplate().insert("addCardBound",
							mapParam) != null) {
						jsonresult.setMsg("人卡绑定失败");
						throw new RuntimeException();
					} else {
						if (GlobalConstant.TYPE_DM1.equals(typeDm)) {// 判断是否是光钥匙
							modifyCardState_AllState(cardN, "20");
							List<CardSyncModel> cardSMList = cardSynchronizeService
									.queryIDByCardNum(cardN);
							// 获取卡片绑定的人员代码
							String staffNum = null;
							if (cardSMList != null && cardSMList.size() > 0
									&& cardSMList.get(0) != null) {
								staffNum = cardSMList.get(0).getStaff_num();
							}
							Map<String, String> map = new HashMap<String, String>();
							map.put("cardNum", cardN);
							map.put("state_dm", "20");
							map.put("staff_num", staffNum);
							map.put("date", DateUtil.getDateString(new Date(),
									"yyyy-MM-dd HH:mm:ss"));
							map.put("create_user", admin_id);
							this.getSqlMapClientTemplate().insert(
									"addCardHistory", map);
						} else {
							modifyCardState_AllState(cardN, "10");
							List<CardSyncModel> cardSMList = cardSynchronizeService
									.queryIDByCardNum(cardN);
							// 获取卡片绑定的人员代码
							String staffNum = null;
							if (cardSMList != null && cardSMList.size() > 0
									&& cardSMList.get(0) != null) {
								staffNum = cardSMList.get(0).getStaff_num();
							}
							Map<String, String> map = new HashMap<String, String>();
							map.put("cardNum", cardN);
							map.put("state_dm", "10");
							map.put("staff_num", staffNum);
							map.put("date", DateUtil.getDateString(new Date(),
									"yyyy-MM-dd HH:mm:ss"));
							map.put("create_user", admin_id);
							this.getSqlMapClientTemplate().insert(
									"addCardHistory", map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "人卡绑定失败，异常");
			logDao.addLog(log);
			if (jsonresult.getMsg() == null) {
				jsonresult.setMsg("人卡绑定失败");
			}
			jsonresult.setSuccess(false);
			return jsonresult;
		}
		log.put("V_OP_TYPE", "业务");
		log.put("V_OP_MSG", "人卡绑定成功");
		logDao.addLog(log);
		jsonresult.setMsg("人卡绑定成功");
		jsonresult.setSuccess(true);
		return jsonresult;
	}

	@Override
	public List<BoundCard> selectBoundCardByYHDM(String yhdm) {
		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardByYhdm", yhdm);
	}

	public Integer isBoundCard(String id) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("ids", id);
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"isBoundCard", mapParam);
	}

	@Override
	public String getCStateDmByName(String stateName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("stateName", stateName);
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getCStateDmByName", map);
	}

	@Override
	public boolean isExistUserAndCard(String card_num, String staff_num) {
		String[] card_nums = card_num.split(",");
		if (isExistStaff(staff_num) > 0
				&& isExistCardnums(card_num) == card_nums.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Integer isExistStaff(String staff_num) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"isExistStaff", staff_num);
	}

	@Override
	public Integer isExistCardnums(String card_num) {
		// int sum = 0;
		// for (int i = 0; i < card_num.length; i++) {
		int sum1 = (Integer) this.getSqlMapClientTemplate().queryForObject(
				"isExistCardnums", card_num);
		// sum += sum1;
		// }
		return sum1;
	}

	// 发卡信息查询
	@Override
	public List<QuerySentcardInfo> querySentcardInfo(Param param) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("card_num", param.getCard_num());
		map.put("type_dm", param.getType_dm());
		map.put("begin_time", param.getBegin_time());
		map.put("end_time", param.getEnd_time());

		return this.getSqlMapClientTemplate().queryForList("querySentcardInfo",
				map);
	}

	@Override
	public void modifyCardState20(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState20", card_num);
	}

	@Override
	public void modifyCardState_AllState(String card_num, String state) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("state", state);
		map.put("card_num", card_num);
		getSqlMapClientTemplate().update("modifyCardState_AllState", map);
	}

	@Override
	public void modifyCardState100(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState100", card_num);
	}

	@Override
	public void modifyCardState40(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState40", card_num);
	}

	@Override
	public void modifyCardState60(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState60", card_num);
	}

	@Override
	public List<QuerySentcardInfo> querySentcardInfoByEmpnum(String query_type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("query_type", query_type);
		return this.getSqlMapClientTemplate().queryForList(
				"querySentcardInfoByEmpnum", map);
	}

	@Override
	public DeviceInfo2Model getMac_Type(String deviceNum) {

		return (DeviceInfo2Model) this.getSqlMapClientTemplate()
				.queryForObject("getMac_Type", deviceNum);
	}

	@Override
	public List<DeviceAndDoor> getDeviceAndDoor(String cardNum) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceAndDoor",
				cardNum);
	}

	@Override
	public boolean addCardHistory(String cardNum, String staff_num,
			String state_dm, String date) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cardNum", cardNum);
		map.put("state_dm", state_dm);
		map.put("date", date);
		map.put("staff_num", staff_num);
		User user = (User) HttpUtil.getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		map.put("create_user", user.getYhMc());
		if (getSqlMapClientTemplate().insert("addCardHistory", map) != null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean addCardHistory2(String cardNum, String staff_num,
			String state_dm, String date, String admin) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cardNum", cardNum);
		map.put("state_dm", state_dm);
		map.put("date", date);
		map.put("staff_num", staff_num);
		map.put("create_user", admin);
		if (getSqlMapClientTemplate().insert("addCardHistory", map) != null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public void deleteAuthority(String cardNum) {
		this.getSqlMapClientTemplate().update("deleteAuth", cardNum);
	}

	@Override
	public List<AuthorityInfo> getAuthorityInfo(String cardNum) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityInfo",
				cardNum);
	}

	@Override
	public void reSetAuthority(String cardNum) {
		this.getSqlMapClientTemplate().update("reSetAuthority", cardNum);
	}

	@Override
	public void modifyCardState30(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState30", card_num);
	}

	@Override
	public void modifyCardState352(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState352", card_num);
	}

	@Override
	public List<String> getCardNumber(Integer start, Integer end) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("end", end);
		return this.getSqlMapClientTemplate()
				.queryForList("getCardNumber", map);
	}

	@Override
	public Integer validCardNum(String cardNum) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"validCardNum", cardNum);
	}

	@Override
	public Integer validBoundCard(String cardNum) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"validBoundCard", cardNum);
	}

	@Override
	public boolean addCardBound(Map map) {
		return insert("addCardBound", map);
	}

	@Override
	public String getCardTypeDmByTypeId(String card_type_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("card_type_id", card_type_id);
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getCardTypeDmByTypeId", map);
	}

	@Override
	public String getCardStateDmByCardNum(String cardNum) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cardNum", cardNum);
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getCardStateDmByCardNum", map);
	}

	@Override
	public void deleteCardByCardNum(String cardNum) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cardNum", cardNum);
		this.getSqlMapClientTemplate().update("deleteCardByCardNum", mapParam);
	}

	@Override
	public void deleteBoundInfo(String cardNum) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cardNum", cardNum);
		this.getSqlMapClientTemplate().update("deleteBoundInfo", mapParam);
	}

	@Override
	public void modifyCardState00(String card_num) {
		getSqlMapClientTemplate().update("modifyCardState00", card_num);

	}

	@Override
	public List<AuthorityInfo> getAuthorityInfo2(String cardNum) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityInfo2",
				cardNum);
	}

	@Override
	public List<AuthorityInfo> getAuthorityInfo3(String cardNum) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityInfo3",
				cardNum);
	}

	@Override
	public void deleteAuthority2(String cardNum) {
		this.getSqlMapClientTemplate().update("deleteAuth2", cardNum);
	}

	@Override
	public List<StaffCard> exportAllByParam(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("type_dm", param.getType_dm());// 现在用来存卡片类型
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());

		mapParam.put("state_dm", param.getState());
		return this.getSqlMapClientTemplate().queryForList("exportAllByParam",
				mapParam);
	}

	@Override
	public void modifyBoundState(String cardNum, String state) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("state", state);
		mapParam.put("cardNum", cardNum);
		this.getSqlMapClientTemplate().update("modifyBoundState", mapParam);
	}

	@Override
	public void updateCardState(String card_num, String card_state) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("state", card_state);
		mapParam.put("cardNum", card_num);
		getSqlMapClientTemplate().update("updateCardState2", mapParam);
	}

	@Override
	public String getCardTypeDm(String cardNum) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getCardTypeDm", cardNum);
	}

	@Override
	public boolean isExistTypeBoundCard(String staff_dm, String cardNum,
			String typeDm) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_dm", staff_dm);
		mapParam.put("cardNum", cardNum);
		mapParam.put("typeDm", typeDm);
		// 获取某位员工的所有卡号
		List<String> staffCardNumList = getSqlMapClientTemplate().queryForList(
				"getStaffCardNums", mapParam);
		for (String cardN : staffCardNumList) {
			// 根据卡号获取卡片类型代码
			String cardTypeDM = getCardTypeDm(cardN);
			// 判断卡片类型是不是手机光钥匙
			if (typeDm != null && typeDm.equals(cardTypeDM)) {
				return true;
			}
		}
		return false;
		/*
		 * Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
		 * "isExistTypeBoundCard", mapParam); if (count > 0) {
		 * 
		 * }
		 */
	}

	@Override
	public List<String> getOverdueCardNumber() {
		return (List<String>) this.getSqlMapClientTemplate().queryForList(
				"getOverdueCardNumber", "1");
	}

	@Override
	public Integer getNormalCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getNormalCount");
	}

	@Override
	public Card getCardByCardNum(Integer cardNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("cardNum", cardNum);
		return (Card) this.getSqlMapClientTemplate().queryForObject(
				"getCardByCardNum", map);
	}

	@Override
	public String getPCard(String staffNum) {
		return (String) getSqlMapClientTemplate().queryForObject("getPCard",
				staffNum);
	}
}
