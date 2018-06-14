package com.kuangchi.sdd.businessConsole.dm.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.dm.model.DmbBxx;
import com.kuangchi.sdd.businessConsole.dm.model.DmbLxx;
import com.kuangchi.sdd.businessConsole.user.model.User;

public interface IDmbBxxService {

	Grid<DmbBxx> selectDmbBxx(DmbBxx model);

	/**
	 * 代码表名是否存在
	 * @param dmbBmc
	 * @return
	 */
	boolean isExistDmbBmc(String dmbBmc);

	/**
	 * 创建代码表
	 * @param lrry
	 * @param dmbBxx
	 * @param dmlList
	 */
	void addDmB(User lrry, DmbBxx dmbBxx, List<DmbLxx> dmlList);

}
