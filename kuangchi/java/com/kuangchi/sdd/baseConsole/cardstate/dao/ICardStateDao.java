package com.kuangchi.sdd.baseConsole.cardstate.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;
public interface ICardStateDao {
	public Boolean insertCardState(CardState cardState);//新增卡片状态信息
	public Boolean deleteCardState(String card_state_id);//删除卡片状态信息
	public Boolean updateCardState(CardState cardState);//修改卡片状态信息
	public List<CardState> selectCardStateById(Integer card_state_id);//通过ID查信息
	public List<CardState> selectAllCardStates(CardState cardState,String page, String size);//模糊查询卡片所有信息
	public Integer getCountCardState(CardState cardState);
	public List<CardState> selectUniqueCardState(String state_dm);//查询状态码是否唯一
	public Integer getAllCardStateCount(CardState cardState);//查询总的行数
	public Integer selectCard(String card_state_id);//查询卡片状态信息
	public List<CardState> selectCardStateName(String state_name);//查询卡片状态名称唯一
	public Integer selectEditable(String card_state_id);//查询是否是内置状态
}
