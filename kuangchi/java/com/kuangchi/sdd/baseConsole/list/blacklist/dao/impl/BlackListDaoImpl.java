package com.kuangchi.sdd.baseConsole.list.blacklist.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.list.blacklist.dao.BlackListDao;
import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
import com.kuangchi.sdd.baseConsole.list.blacklist.service.BlackListService;
@Repository("blackListDaoImpl")
public class BlackListDaoImpl extends BaseDaoImpl<Holiday> implements BlackListDao{

	@Override
	public boolean addBlackList(BlackList blackList) {
		return insert("addBlackList",blackList);
	}

	@Override
	public List<BlackList> getBlackListByParam(BlackList blackList) {
		return getSqlMapClientTemplate().queryForList("getBlackListByParam",blackList);
	}

	@Override
	public List<BlackList> getBlackListByParamPage(BlackList blackList) {
		return getSqlMapClientTemplate().queryForList("getBlackListByParamPage", blackList);
	}
	
	@Override
	public int getBlackListByParamCount(BlackList blackList) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("getBlackListByParamCount",blackList);
	}
	

	@Override
	public boolean deleteBlackListByStaffNum(String staffNum) {
		return delete("deleteBlackListByStaffNum", staffNum) ;
	}
	
	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean deleteAutorityByCardNum(String cardNum) {
		return delete("deleteAutorityByCardNum", cardNum);
	}

	@Override
	public Integer getBlackListByStaffNumAndCardNum(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getBlackListByStaffNumAndCardNum",map);
	}

	@Override
	public boolean updateBlackListByStaffNumAndCardNum(Map map) {
		return update("updateBlackListByStaffNumAndCardNum",map);
	}

	@Override
	public Integer getBlackListByStaffNum(String yhDm) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getBlackListByStaffNum",yhDm);
	}

	@Override
	public boolean updateBlackListByStaffNum(String yhDm) {
		return update("updateBlackListByStaffNum",yhDm);
	}

	/*@Override
	public boolean insertDelAuthorityInfo(Map map) {
		return insert("insertDelAuthorityInfo",map);
	}*/

	
}
