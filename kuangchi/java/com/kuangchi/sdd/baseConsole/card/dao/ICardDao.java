package com.kuangchi.sdd.baseConsole.card.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
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
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;

public interface ICardDao {
	/**
	 * Description:新增卡片 date:2016年3月10日
	 * 
	 * @author xuewen.deng
	 * @param card
	 * @return
	 */
	boolean addNewCard(Card card);

	/**
	 * Description:修改卡片信息 date:2016年3月10日
	 * 
	 * @author xuewen.deng
	 * @param card
	 */
	void modifyCard(Card card);

	/**
	 * 
	 * Description:获取所以卡片信息 date:2016年3月11日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	List<Card> getAllCard(String Page, String size);

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年3月14日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	List<CardState> getAllCardStates();

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年7月12日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	List<CardState> getAllCardStates2();

	/**
	 * 
	 * Description:获取卡片状态集合 date:2016年7月12日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	List<CardState> getAllCardStates3();

	/**
	 * 
	 * Description:根据查询条件查询卡片信息 date:2016年3月14日
	 * 
	 * @author xuewen.deng
	 * @param param
	 * @return
	 */
	List<Card> getAllByParam(Param param, String page, String size);

	/**
	 * 
	 * Description:根据查询条件查询人员及卡片信息 date:2017年4月7日
	 * 
	 * @author xuewen.deng
	 * @param param
	 * @return
	 */
	public List<StaffCard> getStaff_CardByParam(Param param, String Page,
			String size);

	/**
	 * 
	 * Description:根据卡片ID删除卡片信息 date:2016年3月14日
	 * 
	 * @author xuewen.deng
	 * @param id
	 */
	void deleteCardById(String id);

	/**
	 * 
	 * Description:获取最大卡片自增长号 date:2016年3月23日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	int getCardID();

	/**
	 * 
	 * Description:通过Id获得卡片信息 date:2016年3月28日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public Card getCardById(Integer card_id);

	/**
	 * 
	 * Description:获取所有卡片了类型名称 date:2016年3月28日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<CardType> getAllCardType();

	/**
	 * 
	 * Description:根据类型Id获得类型名称 date:2016年3月28日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public String getCardTypeNameById(Integer type_id);

	/**
	 * 
	 * Description:根据类型名称获得类型Id date:2016年3月28日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public CardType getCardTypeIdByTypeName(String card_type_name);

	/**
	 * 获取卡片条数
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public Integer getAllCardCount(Param param);

	/**
	 * 获取卡片条数
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public Integer getAllStaff_CardCount(Param param);

	public Card getCardById2(Integer card_id);

	public List<StaffInfo> getStaffInfo(String staff_no, String staff_name,
			String staff_dept, Integer page, Integer size);

	public Integer getStaffInfoCount(String staff_no, String staff_name,
			String staff_dept);

	/**
	 * 
	 * Description:人卡绑定 date:2016年3月30日
	 * 
	 * @return
	 */
	JsonResult people_bound_card(String admin_id, String[] card_num,
			String staff_num);

	List<BoundCard> selectBoundCardByYHDM(String yhdm);

	/**
	 * 
	 * Description:判断卡片是否被绑定 date:2016年3月30日
	 * 
	 * @return
	 */
	public Integer isBoundCard(String id);

	/**
	 * 
	 * Description:判断卡片是否被绑定 date:2016年4月12日
	 * 
	 * @return
	 */
	public String getCStateDmByName(String stateName);

	// 绑卡之前判断卡号和工号是否存在
	boolean isExistUserAndCard(String card_num, String staff_num);

	public Integer isExistStaff(String staff_num);

	/**
	 * 判断是否存在卡片
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public Integer isExistCardnums(String card_num);

	// 查询所有未发卡状态的发卡信息
	public List<QuerySentcardInfo> querySentcardInfo(Param param);

	/**
	 * 修改卡片状态
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState20(String card_num);

	/**
	 * 修改卡片状态
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState_AllState(String card_num, String state);

	/**
	 * 修改卡片状态
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState100(String card_num);

	/**
	 * 将卡片状态改为挂失
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState40(String card_num);

	/**
	 * 将卡片状态改为退卡
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState60(String card_num);

	/**
	 * 将卡片状态改为退卡
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState30(String card_num);

	/**
	 * 将卡片状态改为冻结
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState352(String card_num);

	// 按工号查询发卡状态信息
	public List<QuerySentcardInfo> querySentcardInfoByEmpnum(String query_type);

	/**
	 * 根据设备编号获取卡片有权限的设备的mac地址
	 * 
	 * @author xuewen.deng
	 */
	public DeviceInfo2Model getMac_Type(String deviceNum);

	/**
	 * 根据卡号获取卡片有权限的设备和门的编号
	 * 
	 * @author xuewen.deng
	 */
	public List<DeviceAndDoor> getDeviceAndDoor(String cardNum);

	/**
	 * 修改卡片状态后往卡历史表里插入数据 String staff_num,
	 * 
	 * @author xuewen.deng
	 */
	public boolean addCardHistory(String cardNum, String staff_num,
			String state_dm, String date);

	/**
	 * 修改卡片状态后往卡历史表里插入数据 String staff_num,
	 * 
	 * @author xuewen.deng
	 */
	public boolean addCardHistory2(String cardNum, String staff_num,
			String state_dm, String date, String admin);

	/**
	 * 删除卡片的所有权限
	 * 
	 * @author xuewen.deng
	 */
	public void deleteAuthority(String cardNum);

	/**
	 * 根据卡号获取权限信息
	 */
	public List<AuthorityInfo> getAuthorityInfo(String cardNum);

	/**
	 * 重新设置卡片状态为有效
	 * 
	 * @param card_num
	 */
	public void reSetAuthority(String cardNum);

	/**
	 * 获取卡号
	 * 
	 * @author xuewen.deng 2016.7.26
	 * 
	 */
	public List<String> getCardNumber(Integer start, Integer end);

	/**
	 * 验证卡号
	 * 
	 * @author xuewen.deng 2016.7.26
	 * 
	 */
	public Integer validCardNum(String cardNum);

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
	 * @功能描述:新增卡片绑定信息
	 * @参数描述:
	 */
	public boolean addCardBound(Map map);

	/**
	 * @author xuewen.deng 2016.8.8 根据卡片类型Id获取卡片类型代码
	 */
	public String getCardTypeDmByTypeId(String card_type_id);

	/**
	 * @author xuewen.deng 根据卡号码获取卡片类型代码
	 */
	public String getCardStateDmByCardNum(String cardNum);

	/**
	 * 
	 * Description:根据卡号删除卡片信息
	 * 
	 * @author xuewen.deng
	 * @param cardNum
	 */
	public void deleteCardByCardNum(String cardNum);

	/**
	 * 
	 * Description:根据卡号删除卡片绑定的信息（操作人卡绑定表）
	 * 
	 * @author xuewen.deng
	 * @param cardNum
	 */
	public void deleteBoundInfo(String cardNum);

	/**
	 * 将卡片状态改为未绑定
	 * 
	 * @author xuewen.deng
	 * @param card_num
	 */
	public void modifyCardState00(String card_num);

	/**
	 * 根据卡号获取权限信息2
	 * 
	 * @author xuewen.deng
	 */
	public List<AuthorityInfo> getAuthorityInfo2(String cardNum);

	/**
	 * 根据卡号获取权限信息3
	 * 
	 * @author xuewen.deng
	 */
	public List<AuthorityInfo> getAuthorityInfo3(String cardNum);

	/**
	 * 删除卡片的所有权限，真删除
	 * 
	 * @author xuewen.deng
	 */
	public void deleteAuthority2(String cardNum);

	/**
	 * 根据参数导出卡片信息
	 * 
	 * @param param
	 * @return
	 * @author xuewen.deng
	 */
	public List<StaffCard> exportAllByParam(Param param);

	/**
	 * 修改人卡绑定信息
	 * 
	 * @author xuewen.deng
	 */

	public void modifyBoundState(String card_num, String State);

	/**
	 * 修改卡片状态
	 */
	public void updateCardState(String card_num, String card_state);

	/**
	 * 根据卡号获取卡片状态代码
	 * 
	 * @author xuewen.deng
	 * @param cardNum
	 * @return
	 */
	public String getCardTypeDm(String cardNum);

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
	 * 
	 * Description:通过cardNum获得卡片信息 date:2017年1月24日
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public Card getCardByCardNum(Integer cardNum);

	/**
	 * 查询员工的光钥匙 by gengji.yang
	 */
	public String getPCard(String staffNum);
}
