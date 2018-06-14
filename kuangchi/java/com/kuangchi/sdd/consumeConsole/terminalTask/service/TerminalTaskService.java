package com.kuangchi.sdd.consumeConsole.terminalTask.service;

import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskHistoryModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.TerminalTaskModel;

public interface TerminalTaskService {
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-24 下午8:16:40
	 * @功能描述: 获取全部任务
	 * @参数描述:
	 */
	public Grid<TerminalTaskModel> getAllTerminalTask(Map map);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-24 下午8:17:17
	 * @功能描述:获取执行中数据
	 * @参数描述:
	 */
	public Grid<CardTaskModel> getAllCardTask(Map map);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-25 下午3:56:56
	 * @功能描述: 获取未完成，执行成功任务的数据
	 * @参数描述:
	 */
	public Grid<CardTaskHistoryModel> getAllCardTaskHistory(Map map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 上午9:53:04
	 * @功能描述:删除卡片历史任务 
	 * @参数描述:
	 */
	public boolean delCardTaskHistory(String id);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 下午2:13:50
	 * @功能描述:取消下发名单 
	 * @参数描述:
	 */
	public boolean cancelDistributes(String id);
	
}
