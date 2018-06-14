package com.kuangchi.sdd.commConsole.status.service;

import java.util.List;

import com.kuangchi.sdd.commConsole.actualTime.model.ActualTimeBean;
import com.kuangchi.sdd.commConsole.status.model.GateParamData;

/**
 * 获取门禁控制器的接口服务
 * @author
 *
 */
public interface IStatusService {
	/**
	 * 
	 * Description:读取设备状态
	 * date:2016年5月12日
	 * @return
	 */
	public List<GateParamData> getStatus(String mac);
}
