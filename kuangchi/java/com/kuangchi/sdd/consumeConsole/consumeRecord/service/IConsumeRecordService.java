package com.kuangchi.sdd.consumeConsole.consumeRecord.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.consumeRecord.model.ConsumeRecord;


public interface IConsumeRecordService {
	
	public Grid selectAllConsumeRecords(ConsumeRecord consume_record,
		String page, String size);//模糊查询消费所有信息
	
	public List<ConsumeRecord> exportConsumeRecords(ConsumeRecord consume_record);//导出消费信息
	
	/**
	 * 电子地图显示最新3条记录
	 * @author minting.he
	 * @return
	 */
	public List<ConsumeRecord> getNewRecords();
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午2:54:16
	 * @功能描述: 密码验证
	 * @参数描述:
	 */
	public Integer getUser(String userName, String password);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午3:52:27
	 * @功能描述: 根据记录流水号查询记录
	 * @参数描述:
	 */
	public ConsumeRecord getRecordByNum(String record_flow_num);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-9-2 下午3:53:21
	 * @功能描述: 退款
	 * @参数描述:
	 */
	public boolean refund(ConsumeRecord consumeRecord, String type);
	
	/**
	 * 查看是否有销户的账户
	 * @author minting.he
	 * @param account_num
	 * @return
	 */
	public Integer ifCancelAccount(String account_num);
	
}
