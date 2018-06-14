package com.kuangchi.sdd.visitorConsole.connCs.dao;

import java.util.List;
import java.util.Map;

public interface ConnCsDao {
	
	public List<Map> getDoorAuth(String groupNum);
	
	public Map getTimeZone(String mVisitorNum);
	
	public String getDoorState(Map map);
	
	public String getFloorState(Map map);
	
	public List<Map> getFloorAuth(String groupNum);
	
	public void addTkAuthTask(List<Map> list);

	public String getGroupNameByNum(Map map);
	
	public void updateCardNum(Map map);
	
	public void updateFkCardState(Map map);
	
	public void updtDAuthState(Map map);
	
	public List<Map> getDAuthByCardNum(Map map);
	
	public List<Map> getFAuthByCardNum(Map map);
	
	public void addTkTask(List<Map> list);
	
	public void updtTAuthState(Map map);
	
	public String getGZCardNum();
	
	public String getICCardNum();
	
	public List<String> getAllGZCard();
	
	public List<String> getAllICCard();
	
	public void addBoundCardRe(Map map);
	
	public void addCardInfoRe(Map map);
	
	public String getCardTypeNum(String cardType);
	
	public void addBlackName(Map map);
	
	public void updateVisitorState();
	
	public List<Map> getNYetEmailRecd();
	
	public Integer countOverTimeRecd();
	
	public void updateEmailState(Map map);
	
	public List<Map> getInformList();
	
	public boolean isHandCardNum(Map map);
	
	public void updateMainVisitorState(Map map);
	
	public void makeVisitorVisiting(Map map);
	
	public void delBoundCardRec(Map map);
	
	public void delCardRec(Map map);
	
	public void updateVisitorStateA();
	
	public void updateShouJiVisiting();
	
	public void updateShouJiLeaving();
	
	public void deleteShouJiLeaveCard();
	
	public void deleteShouJiLeaveBoundCard();
	
	public void delGuoQiMenJinQuanXianJilu();
	
	public void delGuoQiTkQuanXianJilu();
}
