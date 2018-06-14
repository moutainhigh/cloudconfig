package com.kuangchi.sdd.elevatorConsole.recordMonitor.dao;





import java.util.List;
import java.util.Map;


/**
 * 实时监控（梯控刷卡记录）
 * @author yuman.gao
 */
public interface IElevatorRecordMonitorDao {

	/**
	 * 查询所有梯控刷卡记录[分页]
	 * @author yuman.gao
	 */
	public List<Map<String,Object>> getAllRecord(Map<String, Object> map);
	
	/**
	 * 查询所有梯控刷卡记录[总数]
	 * @author yuman.gao
	 */
	public Integer getAllRecordCount(Map<String, Object> map);
	
}
