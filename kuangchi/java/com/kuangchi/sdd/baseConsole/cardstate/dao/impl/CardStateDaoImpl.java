package com.kuangchi.sdd.baseConsole.cardstate.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;
import com.kuangchi.sdd.baseConsole.cardstate.dao.ICardStateDao;
@Repository("cardStateDaoImpl")
public class CardStateDaoImpl extends BaseDaoImpl<CardState> implements ICardStateDao {
	
	/**
	 * 新增卡片
	 */
	@Override
	public Boolean insertCardState(CardState cardState) {
		Object obj=this.getSqlMapClientTemplate().insert("insertCardState", cardState);
		
		if(obj==null){
			return true;
		}
		return false;
	}
	/**
	 * 删除
	 */
	@Override
	public Boolean deleteCardState(String card_state_id) {
		Object obj=this.getSqlMapClientTemplate().update("deleteCardState", card_state_id);
		if(obj==null){
			return false;
		}
		return true;
	}
	
	/**
	 * 修改
	 */
	@Override
	public Boolean updateCardState(CardState cardState) {
		Object obj=this.getSqlMapClientTemplate().update("updateCardState", cardState);
		if(obj==null){
			return false;
		}
		return true;
	}
	/**
	 * 通过ID查
	 */
	@Override
	public List<CardState> selectCardStateById(Integer card_state_id) {
		return this.getSqlMapClientTemplate().queryForList("selectCardStateById", card_state_id);
		
	}
	
	/**
	 * 查询
	 */
	@Override
	public List<CardState> selectAllCardStates(CardState cardState,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("state_name", cardState.getState_name());
		mapState.put("begin_time", cardState.getBegin_time());
		mapState.put("end_time", cardState.getEnd_time());
		mapState.put("state_dm", cardState.getState_dm());
		return this.getSqlMapClientTemplate().queryForList("selectAllCardStates", mapState);
	}
	
	/**
	 * 查询状态代码是否唯一
	 */
	@Override
	public List<CardState> selectUniqueCardState(String state_dm) {
		return this.getSqlMapClientTemplate().queryForList("selectUniqueCardState", state_dm);
	}
	
	@Override
	public Integer getCountCardState(CardState cardState) {
		return null;
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	/**
	 * 查询总行数
	 */
	@Override
	public Integer getAllCardStateCount(CardState cardState) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("state_name", cardState.getState_name());
		mapState.put("begin_time", cardState.getBegin_time());
		mapState.put("end_time", cardState.getEnd_time());
		mapState.put("state_dm", cardState.getState_dm());
		return queryCount("getAllCardStateCount",mapState);
	}
	
	/**
	 * 查询卡片状态信息是否存在
	 */
	@Override
	public Integer selectCard(String card_state_id) {
		return queryCount("selectCard",card_state_id);
	}
	
	/**
	 * 查询是不是内置状态
	 */
	public Integer selectEditable(String card_state_id){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("card_state_id", card_state_id);
		map.put("editable", 0);
		return queryCount("selectEditable",map);
	}
	
	@Override
	public List<CardState> selectCardStateName(String state_name) {
		return this.getSqlMapClientTemplate().queryForList("selectCardStateName", state_name);
		
	}
	
	
	
}
