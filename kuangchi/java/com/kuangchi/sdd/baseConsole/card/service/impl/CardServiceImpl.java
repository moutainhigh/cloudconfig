package com.kuangchi.sdd.baseConsole.card.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.card.dao.ICardDao;
import com.kuangchi.sdd.baseConsole.card.model.AuthorityInfo;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.model.CardState;
import com.kuangchi.sdd.baseConsole.card.model.DeviceInfo2Model;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.card.model.QuerySentcardInfo;
import com.kuangchi.sdd.baseConsole.card.model.StaffCard;
import com.kuangchi.sdd.baseConsole.card.model.StaffInfo;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.CardSynchronizeService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;

@Transactional
@Service("cardServiceImpl")
public class CardServiceImpl extends BaseServiceSupport implements ICardService {

	private static final int CARD_ID_LENGTH = 6;

	@Resource(name = "cardDaoImpl")
	private ICardDao cardDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "cDeviceService")
	// 消费service
	IDeviceService deviceService;
	@Resource(name = "peopleAuthorityService")
	PeopleAuthorityInfoService peopleAuthorityService;
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;
	@Resource(name = "cardSynchronizeServiceImpl")
	private CardSynchronizeService cardSynchronizeService;
	@Autowired
	IDepartmentGrantService departmentGrantService;

	@Override
	public boolean addNewCard(Card card) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片信息新增");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", card.getCreate_user());

		try {
			String cardD_m = cardDao.getCardTypeDmByTypeId(card
					.getCard_type_id());
			card.setCard_type_id(cardD_m);
			boolean flag = cardDao.addNewCard(card);
			;
			if (flag == false) {
				log.put("V_OP_MSG", "卡片信息新增失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				return false;
			}
			log.put("V_OP_MSG", "卡片信息新增成功");
			log.put("V_OP_TYPE", "业务");
			logDao.addLog(log);

			// 加入卡片历史表
			List<CardSyncModel> cardSMList = cardSynchronizeService
					.queryIDByCardNum(card.getCard_num());
			// 获取卡片绑定的人员代码
			String staffNum = null;
			if (cardSMList != null && cardSMList.size() > 0
					&& cardSMList.get(0) != null) {
				staffNum = cardSMList.get(0).getStaff_num();
			}
			// 加入卡片历史表
			cardDao.addCardHistory2(card.getCard_num(), staffNum,
					card.getState_dm(),
					DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
					card.getCreate_user());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "卡片新增时出现异常");
			log.put("V_OP_TYPE", "异常");
			logDao.addLog(log);
			return false;
		}
	}

	@Override
	public boolean addCardBound(Map map) {
		Map<String, String> log = new HashMap<String, String>();
		String create_user = (String) map.get("create_user");
		boolean result = false;
		try {
			result = cardDao.addCardBound(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.put("V_OP_NAME", "绑定卡片管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "新增绑定卡片信息");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public boolean modifyCard(Card card, String operator) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片信息修改");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", operator);
		try {
			this.cardDao.modifyCard(card);
			log.put("V_OP_MSG", "修改卡片信息成功");
			log.put("V_OP_TYPE", "业务");
			logDao.addLog(log);
			return true;
		} catch (Exception e) {
			log.put("V_OP_MSG", "修改卡片信息失败");
			log.put("V_OP_TYPE", "异常");
			e.printStackTrace();
			logDao.addLog(log);
			return false;
		}

	}

	@Override
	public List<Card> getAllCard(String page, String size) {
		return this.cardDao.getAllCard(page, size);
	}

	@Override
	public List<CardState> getAllCardStates() {
		return this.cardDao.getAllCardStates();
	}

	@Override
	public Grid getAllByParam(Param param, String page, String size) {
		Integer count = cardDao.getAllCardCount(param);// 去数据库中查符合条件的记录条数
		List<Card> allCard = this.cardDao.getAllByParam(param, page, size);

		Date day = new Date();// 获取当前日期
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
		String vali_date = df.format(day);
		List<Card> allCard2 = new ArrayList<Card>();
		Date date = null;
		try {
			date = df.parse(vali_date);// 格式化当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sysTime = date.getTime();// 获取当前日期的毫秒数
		for (Card c : allCard) {
			try {
				if (sysTime > df.parse(c.getCard_validity()).getTime()) {
					c.setCard_validity_state("-1");
				} else {
					c.setCard_validity_state("0");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				allCard2.add(c);
			}
		}

		Grid grid = new Grid();
		grid.setTotal(count);
		grid.setRows(allCard2);
		return grid;
	}

	@Override
	public boolean deleteCardById(String id, String operator) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片信息删除");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", operator);
		try {
			this.cardDao.deleteCardById(id);
			log.put("V_OP_MSG", "删除卡片信息成功");
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			log.put("V_OP_MSG", "删除卡片信息失败");
			log.put("V_OP_TYPE", "异常");
			e.printStackTrace();
			return false;
		} finally {
			logDao.addLog(log);
		}
	}

	@Override
	public String getCardID() {
		return String.valueOf(this.cardDao.getCardID());
	}

	@Override
	public Card getCardById(Integer card_id) {
		return cardDao.getCardById(card_id);
	}

	@Override
	public List<CardType> getAllCardType() {
		return this.cardDao.getAllCardType();
	}

	@Override
	public String getCardTypeNameById(Integer type_id) {
		return this.cardDao.getCardTypeNameById(type_id);
	}

	@Override
	public CardType getCardTypeIdByTypeName(String card_type_name) {
		return this.cardDao.getCardTypeIdByTypeName(card_type_name);
	}

	@Override
	public Card getCardById2(Integer card_id) {
		Card c = this.cardDao.getCardById2(card_id);
		if (c == null)
			return null;

		Date day = new Date();// 获取当前日期
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
		String vali_date = df.format(day);

		Date date = null;
		try {
			date = df.parse(vali_date);// 格式化当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sysTime = date.getTime();// 获取当前日期的毫秒数

		try {
			if (sysTime > df.parse(c.getCard_validity()).getTime()) {
				c.setCard_validity_state("-1");
			} else {
				c.setCard_validity_state("0");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return c;
	}

	@Override
	public List<StaffInfo> getStaffInfo(String staff_no, String staff_name,
			String staff_dept, Integer page, Integer size) {
		return this.cardDao.getStaffInfo(staff_no, staff_name, staff_dept,
				page, size);
	}

	@Override
	public Integer getStaffInfoCount(String staff_no, String staff_name,
			String staff_dept) {
		return this.cardDao.getStaffInfoCount(staff_no, staff_name, staff_dept);
	}

	/**
	 * 门禁系统 继承组织权限 by gengji.yang
	 */
	private void takeAuthFromG(String staffNum, String cardNum,
			String createUser) {
		List<Map> oragnAuths = peopleAuthorityInfoService
				.getOrganAuth(staffNum);
		for (int i = 0; i < oragnAuths.size(); i++) {
			oragnAuths.get(i).put("cardNum", cardNum);
			oragnAuths.get(i).put("yhmc", createUser);
			oragnAuths.get(i).put("deviceMac", oragnAuths.get(i).get("mac"));
		}
		if (oragnAuths.size() > 0) {
			modifyAuthState(0, oragnAuths);
			peopleAuthorityInfoService.addAuthTasks(oragnAuths, 0);
		}
	}

	/**
	 * 删除或者新增权限时 及时修改权限状态 flag 0 新增，1 删除 by gengji.yang
	 */
	public void modifyAuthState(int flag, List<Map> authList) {
		if (flag == 0) {// 新增 先往权限表 插权限记录 标记好 task_state=00
			addWorkers(authList, "00");
		} else {// 删除权限 先更新权限表 中 task_state==10
			modifyWorker(authList, "10");
		}
	}

	/**
	 * 新增权限时 先插入权限记录，task_state=00
	 */
	public void addWorkers(List<Map> authList, String state) {
		for (Map map : authList) {
			map.put("state", state);
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}

	/**
	 * 删除权限前 更新每一个权限记录的worker param:state 10->待删除 by gengji.yang
	 */
	public void modifyWorker(List<Map> authList, String state) {
		for (Map map : authList) {
			map.put("state", state);
			peopleAuthorityInfoService.updateTskState(map);
		}
	}

	/**
	 * 梯控系统 员工新增绑卡时，继承组织权限 by guofei.lian 2016-11-01
	 */
	private void implementDeptAuthWhenBoundCard(String staffNum,
			String cardNum, String create_user) {

		String bm_dm = departmentGrantService.selectBmdmByStaffNum(staffNum);
		/*
		 * String card_type =
		 * departmentGrantService.selectCardTypeByNum(cardNum);
		 */
		Map deptAuth = departmentGrantService.selectDeptAuthByDeptNo("\'"
				+ bm_dm + "\'");
		if(deptAuth!=null){
			DepartmentGrantModel departmentGrantModel = new DepartmentGrantModel();
			departmentGrantModel.setCard_num(cardNum);
			departmentGrantModel.setCard_type((String) deptAuth.get("card_type"));
			departmentGrantModel.setBegin_valid_time((String) deptAuth
					.get("start_time"));
			departmentGrantModel.setEnd_valid_time((String) deptAuth
					.get("end_time"));
			departmentGrantModel.setObject_num(bm_dm);
			departmentGrantModel.setDevice_num((String) deptAuth.get("device_num"));
			departmentGrantModel.setDevice_ip((String) deptAuth.get("device_ip"));
			departmentGrantModel.setDevice_port((Integer) deptAuth
					.get("device_port"));
			departmentGrantModel.setAddress((String) deptAuth.get("address"));
			departmentGrantModel.setFloor_group_num((String) deptAuth
					.get("floor_group_num"));
			departmentGrantModel.setFloor_list((String) deptAuth.get("floor_list"));
			departmentGrantService.addDeptGrantA(departmentGrantModel, create_user);// 新增卡组织权限到任务表
		}
	}

	@Override
	public JsonResult people_bound_card(final String admin_id,
			final String[] card_num, final String staff_num) {
		JsonResult result = this.cardDao.people_bound_card(admin_id, card_num,
				staff_num);
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < card_num.length; i++) {
					takeAuthFromG(staff_num, card_num[i], admin_id);
					implementDeptAuthWhenBoundCard(staff_num, card_num[i],
							admin_id);// 继承组织权限
				}
			}
		}).start();
		return result;
	}

	@Override
	public Grid<BoundCard> selectBoundCardByYHDM(String yhdm) {
		List<BoundCard> boundCard = cardDao.selectBoundCardByYHDM(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}

	public boolean isBoundCard(String id) {
		if (this.cardDao.isBoundCard(id) > 0) {
			return true;
		}
		return false;
	}

	public String getCStateDmByName(String stateName) {

		return this.cardDao.getCStateDmByName(stateName);

	}

	// 绑卡之前判断卡号和工号是否存在
	@Override
	public boolean isExistUserAndCard(String card_num, String staff_num) {
		if (cardDao.isExistUserAndCard(card_num, staff_num)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<QuerySentcardInfo> querySentcardInfo(Param param) {
		return this.cardDao.querySentcardInfo(param);
	}

	@Override
	public void modifyCardState(String card_num, String card_state) {
		if ("0".equals(card_state)) {
			cardDao.modifyCardState20(card_num);
		} else if ("1".equals(card_state)) {
			cardDao.modifyCardState100(card_num);
		}

	}

	@Override
	public List<QuerySentcardInfo> querySentcardInfoByEmpnum(String query_type) {
		return cardDao.querySentcardInfoByEmpnum(query_type);
	}

	@Override
	public List<CardState> getAllCardStates2() {
		return this.cardDao.getAllCardStates2();
	}

	@Override
	public List<CardState> getAllCardStates3() {
		return this.cardDao.getAllCardStates3();
	}

	@Override
	public Integer dissLoss(String cardNums, String admin) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片解挂");
		log.put("V_OP_FUNCTION", "解挂");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
				for (String devNum : devNumList) {// 重新下发解挂后的名单
					deviceService.insertNameTask(devNum, card_num, "0", "8");
				}
				peopleAuthorityInfoService.staffBackAuthBack(card_num);// 恢复梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo3(card_num);// 根据卡号获取所有已经删除的权限的
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型
						if(deviceInfo2==null){
							continue;
						}
						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}

					updateAuthState(0, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 0);
					// cardDao.reSetAuthority(card_num);// 重新设置数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}

			try {
				if (isExistSucc == 1) {
					isCardExistSucc = 1;
					if ("401".equals(cardDao.getCardStateDmByCardNum(card_num))) {
						cardDao.modifyCardState_AllState(card_num, "30");// 将状态改为“正常”
						// cardDao.modifyBoundState(card_num, "0");// 修改人卡绑定状态
						// List<String>
						// staffNum_list=cardDao.getStaffNumByCardNum(card_num);//获取卡号当前绑定的人员代码
						List<CardSyncModel> cardSMList = cardSynchronizeService
								.queryIDByCardNum(card_num);
						// 获取卡片绑定的人员代码
						String staffNum = null;
						if (cardSMList != null && cardSMList.size() > 0
								&& cardSMList.get(0) != null) {
							staffNum = cardSMList.get(0).getStaff_num();
						}
						// 加入卡片历史表
						cardDao.addCardHistory2(card_num, staffNum, "50",
								DateUtil.getDateString(new Date(),
										"yyyy-MM-dd HH:mm:ss"), admin);// 修改卡片状态成功后，往卡历史表里插入数据
					} else {
						cardDao.modifyCardState20(card_num);// 将状态改为“正常”
						// cardDao.modifyBoundState(card_num, "0");// 修改人卡绑定状态
						List<CardSyncModel> cardSMList = cardSynchronizeService
								.queryIDByCardNum(card_num);
						// 获取卡片绑定的人员代码
						String staffNum = null;
						if (cardSMList != null && cardSMList.size() > 0
								&& cardSMList.get(0) != null) {
							staffNum = cardSMList.get(0).getStaff_num();
						}
						// 加入卡片历史表
						cardDao.addCardHistory2(card_num, staffNum, "50",
								DateUtil.getDateString(new Date(),
										"yyyy-MM-dd HH:mm:ss"), admin);// 修改卡片状态成功后，往卡历史表里插入数据

					}
					log.put("V_OP_MSG", "卡片" + card_num + "解挂成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "解挂失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "卡片" + card_num + "解挂失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}

		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public Integer reportLoss(String cardNums, String admin) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片挂失");
		log.put("V_OP_FUNCTION", "挂失");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
				for (String devNum : devNumList) {// 删除所有消费设备上的名单
					deviceService.insertNameTask(devNum, card_num, "1", "9");
				}
				peopleAuthorityInfoService.staffLeaveDelAuth(card_num);// 删除梯控权限

				// 删除门禁权限
				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo2(card_num);// 根据卡号获取所有有权限的设备和门等信息（编号）
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}
					updateAuthState(1, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 2);// 伪删除权限
					// cardDao.deleteAuthority(card_num);// 删除数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}
			try {
				if (isExistSucc == 1) {
					if ("30".equals(cardDao.getCardStateDmByCardNum(card_num))
							|| "401".equals(cardDao
									.getCardStateDmByCardNum(card_num))) {
						cardDao.modifyCardState_AllState(card_num, "401");// 卡片状态改为“挂失（报损）”
					} else {
						cardDao.modifyCardState_AllState(card_num, "40");
						// cardDao.modifyBoundState(card_num, "0");// 修改人卡绑定状态
					}

					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "40", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					isCardExistSucc = 1;// 成功标志（有挂失成功的记录）
					log.put("V_OP_MSG", "卡片" + card_num + "挂失成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;// 失败标志（有挂失失败的记录）
					log.put("V_OP_MSG", "卡片" + card_num + "挂失失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				log.put("V_OP_MSG", "卡片" + card_num + "挂失失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}
		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}

	}

	@Override
	public Integer returnCard(String cardNums, String admin) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "退卡");
		log.put("V_OP_FUNCTION", "退卡");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();
				for (String devNum : devNumList) {// 删除所有消费设备上的名单
					deviceService.insertNameTask(devNum, card_num, "1", "9");
				}
				peopleAuthorityInfoService.staffLeaveDelAuth(card_num);// 删除梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo2(card_num);// 根据卡号获取所有有权限的设备和门等信息（编号）
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}

					peopleAuthorityInfoService.addAuthTasks(
							authorityListForDev, 1);// 真删除权限
					cardDao.deleteAuthority2(card_num);// 删除数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}
			try {
				if (isExistSucc == 1) {
					// cardDao.modifyCardState60(card_num);// 将状态改为“退卡”
					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "60", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					cardDao.modifyCardState_AllState(card_num, "60");// 将状态改为“退卡（回收卡号）”
					cardDao.deleteBoundInfo(card_num);// 删除人卡绑定表的记录
					cardDao.deleteCardByCardNum(card_num);// 删除卡片
					isCardExistSucc = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "退卡成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "退卡失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "退卡时出现异常");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}
		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}

	}

	@Override
	public Integer reportDamage(String cardNums, String admin) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "报损");
		log.put("V_OP_FUNCTION", "报损");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
				for (String devNum : devNumList) {// 删除所有消费设备上的名单
					deviceService.insertNameTask(devNum, card_num, "1", "9");
				}
				peopleAuthorityInfoService.staffLeaveDelAuth(card_num);// 删除梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo2(card_num);// 根据卡号获取所有有权限的设备和门等信息（编号）
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}

					updateAuthState(1, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 2);// 伪删除权限
					// cardDao.deleteAuthority(card_num);// 删除数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}
			try {
				if (isExistSucc == 1) {
					// cardDao.modifyBoundState(card_num, "1");// 修改人卡绑定状态
					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "30", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					cardDao.modifyCardState30(card_num);// 将状态改为“报损”
					isCardExistSucc = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "报损成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "报损失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "报损时出现异常");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}
		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public long getCardNumber(String cardType) {
		long cardNum = 0;
		try {
			List<String> cardNums = cardDao.getCardNumber(1, 999999999);// 拿到符合区间的卡号
			if (cardNums.size() == 0) {// 如果没有此区间的卡号，则返回最小值
				return 1;
			}
			long i;

			for (i = 1; i <= 999999999; i++) {
				Boolean flag = true;
				for (String cardN : cardNums) {
					if (i == Long.valueOf(cardN)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					cardNum = i;
					break;
				}
			}
		} catch (Exception e) {
			cardNum = 0;
			e.printStackTrace();
		}
		/*if ("1".equals(cardType)) {
			try {
				List<String> cardNums = cardDao.getCardNumber(1, 32767);// 拿到符合区间的卡号
				if (cardNums.size() == 0) {// 如果没有此区间的卡号，则返回最小值
					return 1;
				}
				long i;

				for (i = 1; i <= 32767; i++) {
					Boolean flag = true;
					for (String cardN : cardNums) {
						if (i == Long.valueOf(cardN)) {
							flag = false;
							break;
						}
					}
					if (flag) {
						cardNum = i;
						break;
					}
				}
			} catch (Exception e) {
				cardNum = 0;
				e.printStackTrace();
			}

		} else if ("2".equals(cardType)) {
			try {
				List<String> cardNums = cardDao.getCardNumber(32768, 65534);// 拿到符合区间的卡号
				if (cardNums.size() == 0) {// 如果没有此区间的卡号，则返回最小值
					return 32768;
				}
				long i;

				for (i = 32768; i <= 65534; i++) {
					Boolean flag = true;
					for (String cardN : cardNums) {
						if (i == Long.valueOf(cardN)) {
							flag = false;
							break;
						}

					}
					if (flag) {
						cardNum = i;
						break;
					}

				}
			} catch (Exception e) {
				cardNum = 0;
				e.printStackTrace();
			}

		} else if ("3".equals(cardType)) {
			try {
				List<String> cardNums = cardDao.getCardNumber(65535, null);// 拿到符合区间的卡号
				if (cardNums.size() == 0) {// 如果没有此区间的卡号，则返回最小值
					return 65535;
				}
				long i;

				for (i = 65535; i <= 999999999; i++) {
					Boolean flag = true;
					for (String cardN : cardNums) {
						if (i == Long.valueOf(cardN)) {
							flag = false;
							break;
						}

					}
					if (flag) {
						cardNum = i;
						break;
					}

				}

			} catch (Exception e) {
				cardNum = 0;
				e.printStackTrace();
			}

		} else {
			return 0;
		}*/

		return cardNum;
	}

	@Override
	public Integer validCardNum(String cardNum) {

		return cardDao.validCardNum(cardNum);
	}

	@Override
	public Integer validCardNum2(String cardNum, String cardType) {
		long cardN = Long.valueOf(cardNum);
		if (cardN < 1 || cardN > Long.parseLong("4294967295")) {
			return 1;
		} else {
			return 0;
		}
/*		if ("1".equals(cardType)) {
			if (cardN < 1 || cardN > 32767) {
				return 1;
			} else {
				return 0;
			}
		} else if ("2".equals(cardType)) {
			if (cardN < 32768 || cardN > 65534) {
				return 1;
			} else {
				return 0;
			}
		} else if ("3".equals(cardType)) {
			if (cardN < 65535) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}*/
	}

	@Override
	public Integer validBoundCard(String cardNum) {
		return cardDao.validBoundCard(cardNum);
	}

	@Override
	public String getCardTypeDmByTypeId(String card_type_id) {
		return this.cardDao.getCardTypeDmByTypeId(card_type_id);
	}

	@Override
	public int batchAddCard(List<Card> cardList) {
		boolean succFlag = false;
		boolean failFlag = false;
		for (Card card : cardList) {
			if (addNewCard(card)) {
				succFlag = true;
			} else {
				failFlag = true;
			}
		}

		if (succFlag && failFlag) {// 部分失败
			return 2;
		} else if (succFlag && !failFlag) {// 全部成功
			return 0;
		} else if (!succFlag && failFlag) {// 全部失败
			return 1;
		} else {// 全部失败
			return 1;
		}

	}

	@Override
	public Integer unBoundCard(String cardNums, String admin) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片解绑");
		log.put("V_OP_FUNCTION", "解绑");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();
			try {
				List<String> devNumList = deviceService.getAllDevNum();
				for (String devNum : devNumList) {// 删除所有消费设备上的名单
					deviceService.insertNameTask(devNum, card_num, "1", "9");
				}
				peopleAuthorityInfoService.staffLeaveDelAuth(card_num);// 删除梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo2(card_num);// 根据卡号获取所有有权限的设备和门等信息（编号）
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}

					peopleAuthorityInfoService.addAuthTasks(
							authorityListForDev, 1);// 真删除权限
					cardDao.deleteAuthority2(card_num);// 删除数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}
			if (isExistSucc == 1) {
				try {
					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "70", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					cardDao.deleteBoundInfo(card_num);// 删除人卡绑定信息
					cardDao.modifyCardState00(card_num);// 将状态改为“未绑定”
					isCardExistSucc = 1;
					// isExistSucc = 1;// 成功标志（有解绑成功的记录）
					log.put("V_OP_MSG", "卡片" + card_num + "解绑成功");
					log.put("V_OP_TYPE", "业务");
				} catch (Exception e) {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "解绑失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
					e.printStackTrace();
				}
			} else {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "卡片" + card_num + "解绑失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
			}
		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public List<StaffCard> exportAllByParam(Param param) {
		List<StaffCard> allCard = this.cardDao.exportAllByParam(param);

		Date day = new Date();// 获取当前日期
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
		String vali_date = df.format(day);
		List<StaffCard> allCard2 = new ArrayList<StaffCard>();
		Date date = null;
		try {
			date = df.parse(vali_date);// 格式化当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sysTime = date.getTime();// 获取当前日期的毫秒数
		for (StaffCard c : allCard) {
			try {
				if (sysTime > df.parse(c.getCard_validity()).getTime()) {
					c.setCard_validity_state("过期");
				} else {
					c.setCard_validity_state("正常");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				allCard2.add(c);
			}
		}

		return allCard2;
	}

	@Override
	public Integer frozenCard(String cardNums, String admin) {

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "冻结");
		log.put("V_OP_FUNCTION", "冻结");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			if ("30".equals(cardDao.getCardStateDmByCardNum(card_num))) {
				// 报损状态的卡片不需要冻结
				continue;
			}
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();
			try {
				List<String> devNumList = deviceService.getAllDevNum();
				// 删除所有消费设备上的名单
				for (String devNum : devNumList) {
					deviceService.insertNameTask(devNum, card_num, "1", "9");
				}
				peopleAuthorityInfoService.staffLeaveDelAuth(card_num);// 删除梯控权限

				// 删除门禁权限
				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo2(card_num);// 根据卡号获取所有有权限的设备和门等信息（编号）
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}

					updateAuthState(1, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 2);// 伪删除权限
					// cardDao.deleteAuthority2(card_num);// 删除数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;

				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}
			try {
				if (isExistSucc == 1) {
					if ("10".equals(cardDao.getCardStateDmByCardNum(card_num))
							|| "252".equals(cardDao
									.getCardStateDmByCardNum(card_num))) {
						if (!("252".equals(cardDao
								.getCardStateDmByCardNum(card_num)))) {
							cardDao.modifyCardState_AllState(card_num, "252");
							List<CardSyncModel> cardSMList = cardSynchronizeService
									.queryIDByCardNum(card_num);
							// 获取卡片绑定的人员代码
							String staffNum = null;
							if (cardSMList != null && cardSMList.size() > 0
									&& cardSMList.get(0) != null) {
								staffNum = cardSMList.get(0).getStaff_num();
							}
							// 加入卡片历史表
							cardDao.addCardHistory2(card_num, staffNum, "252",
									DateUtil.getDateString(new Date(),
											"yyyy-MM-dd HH:mm:ss"), admin);// 修改卡片状态成功后，往卡历史表里插入数据
						}
					} else {
						cardDao.modifyCardState352(card_num);// 将状态改为“冻结(已发卡)”
						// cardDao.modifyBoundState(card_num, "1");// 修改人卡绑定状态
						List<CardSyncModel> cardSMList = cardSynchronizeService
								.queryIDByCardNum(card_num);
						// 获取卡片绑定的人员代码
						String staffNum = null;
						if (cardSMList != null && cardSMList.size() > 0
								&& cardSMList.get(0) != null) {
							staffNum = cardSMList.get(0).getStaff_num();
						}
						// 加入卡片历史表
						cardDao.addCardHistory2(card_num, staffNum, "352",
								DateUtil.getDateString(new Date(),
										"yyyy-MM-dd HH:mm:ss"), admin);// 修改卡片状态成功后，往卡历史表里插入数据
					}
					isCardExistSucc = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "冻结成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "冻结失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "冻结时出现异常");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}
		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}

	}

	@Override
	public Integer unfreezeCard(String cardNums, String admin) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片解冻");
		log.put("V_OP_FUNCTION", "解冻");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			if ("30".equals(cardDao.getCardStateDmByCardNum(card_num))) {
				// 报损状态的卡片不需要解冻
				continue;
			}
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
				for (String devNum : devNumList) {// 重新下发解挂后的名单
					deviceService.insertNameTask(devNum, card_num, "0", "8");
				}
				peopleAuthorityInfoService.staffBackAuthBack(card_num);// 恢复梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo3(card_num);// 根据卡号获取所有已经删除的权限的
				if (authorityInfoList.size() > 0) {

					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						if(deviceInfo2 != null){
							authorityMap.put("cardNum", card_num);
							authorityMap.put("doorNum", authority.getDoor_num());
							authorityMap
							.put("deviceNum", authority.getDevice_num());
							authorityMap.put("deviceMac",
									deviceInfo2.getDevice_mac());
							authorityMap.put("deviceType",
									deviceInfo2.getDevice_type());
							authorityMap.put("startTime",
									authority.getValid_start_time());
							authorityMap.put("endTime",
									authority.getValid_end_time());
							authorityMap.put("groupNum",
									authority.getTime_group_num());
							
							authorityListForDev.add(authorityMap);// 添加到list
						}
					}
					updateAuthState(0, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 0);// 恢复权限
					/*
					 * peopleAuthorityInfoService.addAuthTasks(
					 * authorityListForDev, 0);// 恢复权限
					 */
					// cardDao.reSetAuthority(card_num);// 重新设置数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}

			try {
				if (isExistSucc == 1) {
					isCardExistSucc = 1;
					if ("252".equals(cardDao.getCardStateDmByCardNum(card_num))) {
						cardDao.modifyCardState_AllState(card_num, "10");
					} else {
						cardDao.modifyCardState20(card_num);// 将状态改为“正常”
						// cardDao.modifyBoundState(card_num, "0");// 修改人卡绑定状态
					}

					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "353", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					log.put("V_OP_MSG", "卡片" + card_num + "解冻成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "解冻失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "卡片" + card_num + "解冻失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}

		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public Integer damageRepair(String cardNums, String admin) {// 报损修复
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片解挂");
		log.put("V_OP_FUNCTION", "解挂");
		log.put("V_OP_ID", admin);

		String[] card_nums = cardNums.split(",");
		Integer isExistFail = 0;
		Integer isExistSucc = 0;
		Integer isCardExistFail = 0;
		Integer isCardExistSucc = 0;
		for (String card_num : card_nums) {
			isExistFail = 0;
			isExistSucc = 0;
			List<Map> authorityListForDev = new ArrayList<Map>();

			try {
				List<String> devNumList = deviceService.getAllDevNum();// 获取所有的消费设备编号
				for (String devNum : devNumList) {// 重新下发解挂后的名单
					deviceService.insertNameTask(devNum, card_num, "0", "8");
				}
				peopleAuthorityInfoService.staffBackAuthBack(card_num);// 恢复梯控权限

				List<AuthorityInfo> authorityInfoList = cardDao
						.getAuthorityInfo3(card_num);// 根据卡号获取所有已经删除的权限的
				if (authorityInfoList.size() > 0) {
					for (AuthorityInfo authority : authorityInfoList) {

						Map<String, Object> authorityMap = new HashMap<String, Object>();

						DeviceInfo2Model deviceInfo2 = cardDao
								.getMac_Type(authority.getDevice_num());// 根据设备编号获取设备mac和设备类型

						authorityMap.put("cardNum", card_num);
						authorityMap.put("doorNum", authority.getDoor_num());
						authorityMap
								.put("deviceNum", authority.getDevice_num());
						authorityMap.put("deviceMac",
								deviceInfo2.getDevice_mac());
						authorityMap.put("deviceType",
								deviceInfo2.getDevice_type());
						authorityMap.put("startTime",
								authority.getValid_start_time());
						authorityMap.put("endTime",
								authority.getValid_end_time());
						authorityMap.put("groupNum",
								authority.getTime_group_num());

						authorityListForDev.add(authorityMap);// 添加到list
					}
					updateAuthState(0, authorityListForDev);
					peopleAuthorityService.addAuthTasks(authorityListForDev, 0);// 恢复权限
					/*
					 * peopleAuthorityInfoService.addAuthTasks(
					 * authorityListForDev, 0);// 恢复权限
					 */
					// cardDao.reSetAuthority(card_num);// 重新设置数据库中的此卡片的所有权限
					// isCardExistSucc = 1; // 成功标志（有挂失成功的记录）
					isExistSucc = 1;
				} else {
					// isCardExistSucc = 1;
					isExistSucc = 1;
				}
			} catch (Exception e) {
				// isCardExistFail = 1;// 失败标志（有挂失失败的记录）
				isExistFail = 1;
				e.printStackTrace();
			}

			try {
				if (isExistSucc == 1) {
					isCardExistSucc = 1;
					cardDao.modifyCardState20(card_num);// 将状态改为“正常”
					// cardDao.modifyBoundState(card_num, "0");// 修改人卡绑定状态

					List<CardSyncModel> cardSMList = cardSynchronizeService
							.queryIDByCardNum(card_num);
					// 获取卡片绑定的人员代码
					String staffNum = null;
					if (cardSMList != null && cardSMList.size() > 0
							&& cardSMList.get(0) != null) {
						staffNum = cardSMList.get(0).getStaff_num();
					}
					// 加入卡片历史表
					cardDao.addCardHistory2(card_num, staffNum, "55", DateUtil
							.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
							admin);// 修改卡片状态成功后，往卡历史表里插入数据
					log.put("V_OP_MSG", "卡片" + card_num + "解挂成功");
					log.put("V_OP_TYPE", "业务");
					logDao.addLog(log);
				} else {
					isCardExistFail = 1;
					log.put("V_OP_MSG", "卡片" + card_num + "解挂失败");
					log.put("V_OP_TYPE", "异常");
					logDao.addLog(log);
				}
			} catch (Exception e) {
				isCardExistFail = 1;
				log.put("V_OP_MSG", "卡片" + card_num + "解挂失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
				e.printStackTrace();
			}

		}

		if (isCardExistSucc == 1 && isCardExistFail == 0) {
			return 0;
		} else if (isCardExistSucc == 0 && isCardExistFail == 1) {
			return 1;
		} else if (isCardExistSucc == 1 && isCardExistFail == 1) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public void updateCardState(String card_num, String card_state) {
		cardDao.updateCardState(card_num, card_state);
	}

	@Override
	public void modifyCardState_AllState(String card_num, String card_state) {
		cardDao.modifyCardState_AllState(card_num, card_state);
	}

	public void updateAuthState(int flag, List<Map> authList) {
		if (flag == 0) {// 新增 先往权限表 插权限记录 标记好 task_state=00
			addWorker(authList, "00");
		} else {// 删除权限 先更新权限表 中 task_state==10
			updateWorker(authList, "10");
		}
	}

	/**
	 * 新增权限时 先插入权限记录，task_state=00
	 */
	public void addWorker(List<Map> authList, String state) {
		for (Map map : authList) {
			map.put("state", state);
			peopleAuthorityInfoService.delAuthRecord(map);
			peopleAuthorityInfoService.addAuthRecord(map);
		}
	}

	public void updateWorker(List<Map> authList, String state) {
		for (Map map : authList) {
			map.put("state", state);
			peopleAuthorityInfoService.updateTskState(map);
		}
	}

	@Override
	public boolean isExistTypeBoundCard(String staff_dm, String cardNum,
			String typeDm) {
		return cardDao.isExistTypeBoundCard(staff_dm, cardNum, typeDm);
	}

	@Override
	public List<String> getOverdueCardNumber() {
		return cardDao.getOverdueCardNumber();
	}

	@Override
	public Integer getNormalCount() {
		try {
			return cardDao.getNormalCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Card getCardByCardNum(Integer cardNum) {
		return cardDao.getCardByCardNum(cardNum);
	}

	@Override
	public String getPhoton(String staffNum) {
		Map<String, String> log = new HashMap<String, String>();
		//通过员工工号查询卡号
		String cardNum=cardDao.getPCard(staffNum);
		if(cardNum==null){//该员工还没有光钥匙卡号
			//新增卡号，绑定给该员工，且卡状态为已发卡
			cardNum=getCardNumber("1")+"";
			String[] cardNums={cardNum};
			people_bound_card("admin", cardNums, staffNum);
			Card card=new Card();
			String cardD_m = cardDao.getCardTypeDmByTypeId("1");
			card.setCard_type_id(cardD_m);
			card.setCard_num(cardNum);
			card.setValidity_flag("0");
			card.setState_dm("20");
			card.setCreate_user("admin");
		    Calendar c = Calendar.getInstance();
            Date data = c.getTime();
            c.add(Calendar.YEAR, 10);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			card.setCard_validity(sdf.format(data));
			boolean flag = cardDao.addNewCard(card);
			if (flag == false) {
				log.put("V_OP_MSG", "卡片信息新增失败");
				log.put("V_OP_TYPE", "异常");
				logDao.addLog(log);
			}
			log.put("V_OP_NAME", "手机光钥匙用户注册分配光ID");
			log.put("V_OP_MSG", "卡片信息新增成功");
			log.put("V_OP_TYPE", "业务");
			logDao.addLog(log);
			// 加入卡片历史表
			cardDao.addCardHistory2(cardNum, staffNum,
					"20",
					DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"),
					"admin");
		}
		return cardNum;
	}

	@Override
	public Grid getStaff_CardByParam(Param param, String page, String size) {
		Integer count = cardDao.getAllStaff_CardCount(param);// 去数据库中查符合条件的记录条数
		List<StaffCard> allCard = this.cardDao.getStaff_CardByParam(param,
				page, size);

		Date day = new Date();// 获取当前日期
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
		String vali_date = df.format(day);
		List<StaffCard> allCard2 = new ArrayList<StaffCard>();
		Date date = null;
		try {
			date = df.parse(vali_date);// 格式化当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sysTime = date.getTime();// 获取当前日期的毫秒数
		for (StaffCard c : allCard) {
			try {
				if (sysTime > df.parse(c.getCard_validity()).getTime()) {
					c.setCard_validity_state("-1");
				} else {
					c.setCard_validity_state("0");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				allCard2.add(c);
			}
		}

		Grid grid = new Grid();
		grid.setTotal(count);
		grid.setRows(allCard2);
		return grid;
	}
}
