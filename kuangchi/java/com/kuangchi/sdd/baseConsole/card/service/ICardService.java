package com.kuangchi.sdd.baseConsole.card.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.model.CardState;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.card.model.QuerySentcardInfo;
import com.kuangchi.sdd.baseConsole.card.model.StaffCard;
import com.kuangchi.sdd.baseConsole.card.model.StaffInfo;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;

public interface ICardService {
	/**
	 * Description:新增卡片 date:2016年3月10日
	 * 
	 * @param card
	 */
	boolean addNewCard(Card card);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-3 下午4:28:08
	 * @功能描述:导入员工时,验证卡片是否已经绑定
	 * @参数描述:
	 */
	public Integer validBoundCard(String cardNum);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-3 下午4:28:08
	 * @功能描述:插入绑定卡片信息
	 * @参数描述:
	 */
	public boolean addCardBound(Map map);

	/**
	 * Description:修改卡片信息 date:2016年3月10日
	 * 
	 * @param card
	 */
	public boolean modifyCard(Card card, String operator);

	/**
	 * 
	 * Description:获取所以卡片信息 date:2016年3月11日
	 * 
	 * @return
	 */
	List<Card> getAllCard(String page, String size);

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年3月14日
	 * 
	 * @return
	 */
	List<CardState> getAllCardStates();

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年7月12日
	 * 
	 * @return
	 */
	List<CardState> getAllCardStates2();

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年7月12日
	 * 
	 * @return
	 */
	List<CardState> getAllCardStates3();

	/**
	 * 
	 * Description:根据查询条件查询信息 date:2016年3月14日
	 * 
	 * @param param
	 * @return
	 */
	Grid getAllByParam(Param param, String page, String size);

	/**
	 * 
	 * Description:根据查询条件查询人员及卡片信息 date:2017年4月7日
	 * 
	 * @param param
	 * @return
	 */
	public Grid getStaff_CardByParam(Param param, String page, String size);

	/**
	 * 
	 * Description:根据卡片ID删除卡片信息 date:2016年3月14日
	 * 
	 * @param id
	 */
	boolean deleteCardById(String id, String operator);

	/**
	 * 
	 * Description:系统生成卡号 date:2016年3月23日
	 * 
	 * @return
	 */
	String getCardID();

	/**
	 * Description:通过card_id拿到card信息 date:2016年3月24日
	 * 
	 * @return
	 */
	public Card getCardById(Integer card_id);

	/**
	 * Description:获取所有卡片类型名称 date:2016年3月24日
	 * 
	 * @return
	 */
	public List<CardType> getAllCardType();

	public String getCardTypeNameById(Integer type_id);

	public CardType getCardTypeIdByTypeName(String card_type_name);

	/**
	 * Description:根据ID获取卡片信息 date:2016年3月31日
	 * 
	 * @return
	 */
	public Card getCardById2(Integer card_id);

	public List<StaffInfo> getStaffInfo(String staff_no, String staff_name,
			String staff_dept, Integer page, Integer size);

	public Integer getStaffInfoCount(String staff_name, String staff_dept,
			String staff_dept2);

	/**
	 * 
	 * Description:人卡绑定 date:2016年3月30日
	 * 
	 * @return
	 */
	JsonResult people_bound_card(String admin_id, String[] card_num,
			String staff_num);

	Grid<BoundCard> selectBoundCardByYHDM(String yhdm);

	/**
	 * 
	 * Description:判断卡片是否呗绑定 date:2016年3月30日
	 * 
	 * @return
	 */
	public boolean isBoundCard(String id);

	/**
	 * 
	 * Description:根据状态名称获取状态代码 date:2016年4月12日
	 * 
	 * @return
	 */
	public String getCStateDmByName(String stateName);

	/**
	 * 绑卡之前判断卡号和工号是否存在
	 */
	boolean isExistUserAndCard(String card_num, String staff_num);

	/**
	 * 查询所有未发卡状态的发卡信息
	 */
	public List<QuerySentcardInfo> querySentcardInfo(Param param);

	/**
	 * 修改卡片状态
	 */
	public void modifyCardState(String card_num, String card_state);

	/**
	 * 按工号查询发卡状态信息
	 */

	public List<QuerySentcardInfo> querySentcardInfoByEmpnum(String query_type);

	/**
	 * 挂失卡片
	 * 
	 * @author xuewen.deng 2016.7.12
	 * 
	 */
	public Integer reportLoss(String cardNums, String admin);

	/**
	 * 解挂卡片
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public Integer dissLoss(String cardNums, String admin);

	/**
	 * 退卡
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public Integer returnCard(String cardNums, String admin);

	/**
	 * 报损
	 * 
	 * @author xuewen.deng 2016.7.13
	 * 
	 */
	public Integer reportDamage(String cardNums, String admin);

	/**
	 * 获取卡号
	 * 
	 * @author xuewen.deng 2016.7.26
	 * 
	 */
	public long getCardNumber(String cardType);

	/**
	 * 验证卡号
	 * 
	 * @author xuewen.deng 2016.7.26
	 * 
	 */
	public Integer validCardNum(String cardNum);

	/**
	 * 验证卡号区间
	 * 
	 * @author xuewen.deng 2016.7.27
	 * 
	 */
	public Integer validCardNum2(String cardNum, String cardType);

	/**
	 * 根据卡片类型Id获取卡片类型代码
	 */
	public String getCardTypeDmByTypeId(String card_type_id);

	/**
	 * 批量导入卡片
	 * 
	 * @author xuewen.deng2016/9/30
	 */
	public int batchAddCard(List<Card> cardList);

	/**
	 * 解绑卡片
	 * 
	 * @author xuewen.deng
	 * 
	 */
	public Integer unBoundCard(String cardNums, String admin);

	/**
	 * 根据参数导出卡片信息
	 * 
	 * @param param
	 * @return
	 * @author xuewen.deng
	 */
	public List<StaffCard> exportAllByParam(Param param);

	/**
	 * 冻结卡片
	 * 
	 * @author xuewen.deng 2016.11.15
	 * 
	 */
	public Integer frozenCard(String cardNums, String admin);

	/**
	 * 解冻卡片
	 * 
	 * @author xuewen.deng 2016.11.15
	 * 
	 */
	public Integer unfreezeCard(String cardNums, String admin);

	/**
	 * 修改卡片状态
	 * 
	 * @author xuewen.deng
	 */
	public void updateCardState(String card_num, String card_state);

	/**
	 * 报损修复
	 * 
	 * @author xuewen.deng
	 * @param cardNums
	 * @param admin
	 * @return
	 */
	public Integer damageRepair(String cardNums, String admin);

	/**
	 * 修改卡片状态
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState_AllState(String card_num, String state);

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-12-16 下午4:28:08
	 * @功能描述:绑定卡片是，判断人员是否已经存在某张卡片（一人只能绑定一手机光钥匙）
	 * @参数描述:
	 */
	public boolean isExistTypeBoundCard(String staff_dm, String cardNum,
			String typeDm);

	/**
	 * 获取过期卡号
	 * 
	 * @author xuewen.deng 2016.12.23
	 * 
	 */
	public List<String> getOverdueCardNumber();

	/**
	 * 获取正常卡的数量
	 * 
	 * @author minting.he
	 * @return
	 */
	public Integer getNormalCount();

	/**
	 * Description:通过cardnum拿到card信息 date:2017年1月24日
	 * 
	 * @return
	 */
	public Card getCardByCardNum(Integer cardNum);

	/**
	 * 对接光钥匙 by gengji.yang
	 */
	public String getPhoton(String staffNum);
}
