package com.kuangchi.sdd.baseConsole.cardtype.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.cardtype.dao.ICardTypeDao;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.cardtype.model.ConModel;
import com.kuangchi.sdd.baseConsole.cardtype.service.ICardTypeServcie;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.user.model.User;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-3-30下午6:14:52
 * @功能描述:卡片类型管理-业务实现类
 * @参数描述:
 */
@Service("cardTypeServiceImpl")
public class CardTypeServiceImpl extends BaseServiceSupport implements
		ICardTypeServcie {
	@Resource(name = "cardTypeDaoImpl")
	private ICardTypeDao cardTypeDao;

	// 记录日志
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	private User loginUser;
	private CardType cardType1;

	@Override
	public void addCardType(CardType cardType) {
		this.cardTypeDao.addCardType(cardType);

		// 记录日志
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片类型管理");
		log.put("V_OP_FUNCTION", "添加");
		log.put("V_OP_ID", cardType.getCreate_user());
		log.put("V_OP_TYPE", "业务");
		log.put("V_OP_MSG", "添加卡片类型");
		logDao.addLog(log);

	}

	@Override
	public void delCardType(String ct_id) {
		this.cardTypeDao.delCardType(ct_id);

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片类型管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", ct_id);
		log.put("V_OP_TYPE", "业务");
		log.put("V_OP_MSG", "删除卡片类型");
		logDao.addLog(log);
	}

	@Override
	public void updateCardType(CardType cardType) {
		this.cardTypeDao.updateCardType(cardType);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "卡片类型管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", cardType.getCreate_user());
		log.put("V_OP_TYPE", "业务");
		log.put("V_OP_MSG", "修改卡片类型");
		logDao.addLog(log);
	}

	@Override
	public CardType getCTById(String ct_id) {
		return this.cardTypeDao.getCTById(ct_id);
	}

	@Override
	public Grid<CardType> getAllCT(ConModel conModel) {
		Grid<CardType> grid = new Grid<CardType>();
		List<CardType> resultList = cardTypeDao.getAllCT(conModel);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(cardTypeDao.getCount(conModel));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public Grid<CardType> getCTByParam(ConModel conModel) {
		Grid<CardType> grid = new Grid<CardType>();
		List<CardType> resultList = cardTypeDao.getCTByParam(conModel);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(cardTypeDao.getCount(conModel));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public CardType getByName(String name) {

		return cardTypeDao.getByName(name);
	}

	@Override
	public List<CardType> getListCardInfoByDMService(String ct_id) {
		
		return cardTypeDao.getListCardInfoByDM(ct_id);
	}

}
