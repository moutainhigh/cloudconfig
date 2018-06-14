package com.kuangchi.sdd.businessConsole.randomCode.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.businessConsole.randomCode.model.RandomCode;

public interface RandomCodeDao {
	
	public boolean insertRandCodeInfo(Map map);
	public List<RandomCode> getRandCodeByUserMail(String randCode);
	public Integer getCountByRandCode(Map map);
}
