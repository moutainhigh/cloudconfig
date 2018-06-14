package com.kuangchi.sdd.commConsole.search.service;

import java.util.List;

import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;

public interface ISearchEquipment {
	/**
	 * 
	 * Description:搜索控制器设备，按M1、M2、M4类型搜索
	 * date:2016年4月27日
	 * @return
	 */
	public List<EquipmentBean> searchEquipment(String doorType,String[] ips);
}
