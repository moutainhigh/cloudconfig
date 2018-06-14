package com.kuangchi.sdd.businessConsole.dm.dao;

import java.util.List;

import com.kuangchi.sdd.businessConsole.dm.model.DmbBxx;
import com.kuangchi.sdd.businessConsole.dm.model.DmbLxx;

public interface IDmbBxxDao {

	List<DmbBxx> selectDmbBxx(DmbBxx dmbBxx);

	int countDmbBxx(DmbBxx dmbBxx);

	int isExistDmbBmc(String dmbBmcFull);

	void addDmbBxx(DmbBxx dmbBxx);

	void addDmblxx(List<DmbLxx> dmlList);

	void createTableDmb(String string);

}
