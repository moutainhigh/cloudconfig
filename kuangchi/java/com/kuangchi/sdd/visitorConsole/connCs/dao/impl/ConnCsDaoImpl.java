package com.kuangchi.sdd.visitorConsole.connCs.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.visitorConsole.connCs.dao.ConnCsDao;
@Repository("connCsDao")
public class ConnCsDaoImpl extends BaseDaoImpl<Map> implements ConnCsDao {

	@Override
	public String getNameSpace() {
		return "visitConsole.connCs";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> getDoorAuth(String groupNum) {
		return getSqlMapClientTemplate().queryForList("getDoorAuth", groupNum);
	}

	@Override
	public Map getTimeZone(String mVisitorNum) {
		return (Map)getSqlMapClientTemplate().queryForObject("getTimeZone", mVisitorNum);
	}

	@Override
	public String getDoorState(Map map) {
		return (String)getSqlMapClientTemplate().queryForObject("getDoorState", map);
	}

	@Override
	public String getFloorState(Map map) {
		return (String)getSqlMapClientTemplate().queryForObject("getFloorStates", map);
	}

	@Override
	public List<Map> getFloorAuth(String groupNum) {
		return getSqlMapClientTemplate().queryForList("getFloorAuth", groupNum);
	}

	@Override
	public void addTkAuthTask(List<Map> list) {
		for(Map map:list){
			getSqlMapClientTemplate().insert("addTkAuthTask", map);
		}
	}
	
	@Override
	public String getGroupNameByNum(Map map){
		return (String) this.getSqlMapClientTemplate().queryForObject("getGroupNameByNum1", map);
	}
	
	@Override
	public void updateCardNum(Map map) {
		getSqlMapClientTemplate().update("updateCardNum", map);
	}

	@Override
	public void updateFkCardState(Map map) {
		getSqlMapClientTemplate().update("updateFkCardState", map);
	}

	@Override
	public void updtDAuthState(Map map) {
		getSqlMapClientTemplate().update("updtDAuthState", map);
	}

	@Override
	public List<Map> getDAuthByCardNum(Map map) {
		return getSqlMapClientTemplate().queryForList("getDAuthByCardNum", map);
	}

	@Override
	public List<Map> getFAuthByCardNum(Map map) {
		return getSqlMapClientTemplate().queryForList("getFAuthByCardNum", map);
	}

	@Override
	public void addTkTask(List<Map> list) {
		for(Map map:list){
			getSqlMapClientTemplate().insert("addTkTask", map);
		}
	}

	@Override
	public void updtTAuthState(Map map) {
		getSqlMapClientTemplate().update("updtTAuthState", map);
	}

	@Override
	public String getGZCardNum() {
		return (String)getSqlMapClientTemplate().queryForObject("getGZCardNum");
	}

	@Override
	public String getICCardNum() {
		return (String)getSqlMapClientTemplate().queryForObject("getICCardNum");
	}

	@Override
	public List<String> getAllGZCard() {
		return getSqlMapClientTemplate().queryForList("getAllGZCard");
	}

	@Override
	public List<String> getAllICCard() {
		return getSqlMapClientTemplate().queryForList("getAllICCard");
	}

	@Override
	public void addBoundCardRe(Map map) {
		getSqlMapClientTemplate().insert("addBoundCardRe", map);
	}

	@Override
	public void addCardInfoRe(Map map) {
		getSqlMapClientTemplate().insert("addCardInfoRe", map);
	}

	@Override
	public String getCardTypeNum(String cardType) {
		return (String)getSqlMapClientTemplate().queryForObject("getCardTypeNum", cardType);
	}

	@Override
	public void addBlackName(Map map) {
		getSqlMapClientTemplate().insert("addBlackName", map);
	}

	@Override
	public void updateVisitorState() {
		getSqlMapClientTemplate().update("updateVisitorState");
	}

	@Override
	public List<Map> getNYetEmailRecd() {
		return getSqlMapClientTemplate().queryForList("getNYetEmailRecd");
	}

	@Override
	public Integer countOverTimeRecd() {
		return (Integer)getSqlMapClientTemplate().queryForObject("countOverTimeRecd");
	}

	@Override
	public void updateEmailState(Map map) {
		getSqlMapClientTemplate().update("updateEmailState", map);
	}

	@Override
	public List<Map> getInformList() {
		return getSqlMapClientTemplate().queryForList("getInformList");
	}

	@Override
	public boolean isHandCardNum(Map map) {
		Integer flag=(Integer)getSqlMapClientTemplate().queryForObject("isHandCardNum", map);
		if(flag>0){//不是手输卡号
			return false;
		}else{//是手输卡号
			return true;
		}
	}

	@Override
	public void updateMainVisitorState(Map map) {
		getSqlMapClientTemplate().update("updateMainVisitorState", map);
	}

	@Override
	public void makeVisitorVisiting(Map map) {
		getSqlMapClientTemplate().update("makeVisitorVisiting", map);
	}

	@Override
	public void delBoundCardRec(Map map) {
		getSqlMapClientTemplate().delete("delBoundCardRec", map);
	}

	@Override
	public void delCardRec(Map map) {
		getSqlMapClientTemplate().delete("delCardRec", map);
	}

	@Override
	public void updateVisitorStateA() {
		getSqlMapClientTemplate().update("updateVisitorStateA");
	}

	@Override
	public void updateShouJiVisiting() {
		getSqlMapClientTemplate().update("updateShouJiVisiting");
	}

	@Override
	public void updateShouJiLeaving() {
		getSqlMapClientTemplate().update("updateShouJiLeaving");
	}

	@Override
	public void deleteShouJiLeaveCard() {
		getSqlMapClientTemplate().delete("deleteShouJiLeaveCard");
	}

	@Override
	public void deleteShouJiLeaveBoundCard() {
		getSqlMapClientTemplate().delete("deleteShouJiLeaveBoundCard");
	}

	@Override
	public void delGuoQiMenJinQuanXianJilu() {
		getSqlMapClientTemplate().delete("delGuoQiMenJinQuanXianJilu");
	}

	@Override
	public void delGuoQiTkQuanXianJilu() {
		getSqlMapClientTemplate().delete("delGuoQiTkQuanXianJilu");
	}


	
}
