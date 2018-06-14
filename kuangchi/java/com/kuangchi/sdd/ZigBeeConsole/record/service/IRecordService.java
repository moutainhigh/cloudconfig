package com.kuangchi.sdd.ZigBeeConsole.record.service;



import java.util.List;
import java.util.Map;


/**
 * 光子锁记录 - service
 * @author yuman.gao
 */
public interface IRecordService {
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:02:29
	 * @功能描述: 根据参数查询光子锁记录[分页]
	 * @参数描述:
	 */
	public List<Map> getRecordByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:03:26
	 * @功能描述: 根据参数查询光子锁记录[总数]
	 * @参数描述:
	 */
	public Integer getRecordByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:02:29
	 * @功能描述: 根据参数查询报警记录[分页]
	 * @参数描述:
	 */
	public List<Map> getWarningRecordByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-17 上午11:03:26
	 * @功能描述: 根据参数查询报警记录[总数]
	 * @参数描述:
	 */
	public Integer getWarningRecordByParamCount(Map<String, Object> map);
	
	
}
