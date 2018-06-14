package com.kuangchi.sdd.visitorConsole.checkUserLogin.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.visitorConsole.checkUserLogin.dao.CheckUserLoginDao;

@Repository("checkUserLoginDaoImpl")
public class CheckUserLoginDaoImpl extends BaseDaoImpl<Map> implements CheckUserLoginDao {

	@Override
	public String getNameSpace() {
		return "visitConsole.checkUserLogin";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean checkUserLogin(Map map) {
		if(queryCount("selectUserByNamePwd",map)==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean ifBlackList(Map map) {
		if(queryCount("selectVisitNumFromBlackList",map)>0){
			return true;
		}else{
			
			return false;
		}
	}

	@Override
	public List<Map> selectRecordInfoByCardnum(Map map) {
		return this.getSqlMapClientTemplate().queryForList("selectRecordInfoByCardnum",map);
	}

	@Override
	public boolean ifPassiveBook(Map map) {
		if(queryCount("ifPassiveBook",map)>0){
			return true;
		}else{
			
			return false;
		}
	}

	@Override
	public Map queryVisitorByNum(Map map) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("queryVisitorInfoByNum",map);
	}

	@Override
	public Map queryBookingVisitor(Map map) {
		
		return (Map) this.getSqlMapClientTemplate().queryForObject("queryBookingVisitorInfo",map);
	}

	@Override
	public List<Map> queryBookingFollowVisitor(Map map) {
		return this.getSqlMapClientTemplate().queryForList("queryBookingFollowVisitorInfo",map);
	}

}
