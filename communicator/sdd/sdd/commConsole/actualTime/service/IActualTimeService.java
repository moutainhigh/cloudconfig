package com.kuangchi.sdd.commConsole.actualTime.service;

import java.util.List;

import com.kuangchi.sdd.commConsole.actualTime.model.ActualTimeBean;

/**
 * 获取门禁控制器的接口服务
 * @author 
 *
 */
public interface IActualTimeService {
	/**
	 * 
	 * Description:设置实时上传参数
	 * date:2016年5月11日
	 * @return
	 */
	public String setActualTime(String mac,String sign,int actualTime);
	public ActualTimeBean getActualTime(String mac,String actualTime);
}
