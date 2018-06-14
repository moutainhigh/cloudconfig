package com.kuangchi.sdd.interfaceConsole.cardSender.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.CardSynchronizeDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.CardSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.CardSynchronizeService;

@Transactional
@Service("cardSynchronizeServiceImpl")
public class CardSynchronizeServiceImpl extends BaseServiceSupport implements
		CardSynchronizeService {

	@Resource(name = "cardSynchronizeDaoImpl")
	private CardSynchronizeDao cardSynchronizeDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public List<CardSyncModel> queryBoundCardInfo() {

		return this.cardSynchronizeDao.queryBoundCardInfo();
	}

	@Override
	public boolean isExistCard(String cardNum) {
		Integer cardCount = cardSynchronizeDao.getCardCount(cardNum);
		if (cardCount > 0) {
			return true;
		} else {

			return false;
		}
	}

	@Override
	public List<CardSyncModel> queryIDByCardNum(String cardNum) {
		return this.cardSynchronizeDao.queryIDByCardNum(cardNum);
	}

	@Override
	public String getCardNumByBoundID(String boundCardId) {
		return this.cardSynchronizeDao.getCardNumByBoundID(boundCardId);
	}
}
