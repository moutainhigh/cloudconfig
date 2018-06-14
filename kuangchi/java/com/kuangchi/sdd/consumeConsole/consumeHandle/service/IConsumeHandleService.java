package com.kuangchi.sdd.consumeConsole.consumeHandle.service;

import com.kuangchi.sdd.consumeConsole.consumeHandle.model.ConsumeRecordPack;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.DepositDown;
import com.kuangchi.sdd.consumeConsole.consumeHandle.model.JsonResult;

/**
 * 消费记录上报及处理-Service
 * @author yuman.gao
 */
public interface IConsumeHandleService {

	/**
	 * 记录上报
	 * by yuman.gao
	 */
	public JsonResult recordReport(ConsumeRecordPack recordPack);
	
	
	/**
	 * 消费余额下发
	 * by yuman.gao
	 */
	public DepositDown issuedBalance(String cardNum, String deviceNum);
	
	
}
