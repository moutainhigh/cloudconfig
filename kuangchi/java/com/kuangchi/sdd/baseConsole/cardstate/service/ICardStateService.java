package com.kuangchi.sdd.baseConsole.cardstate.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;

public interface ICardStateService {
	 	public Boolean insertCardState(CardState cardState);
	    public Boolean deleteCardState(String card_state_id);
	    public Boolean updateCardState(CardState cardState);
	    public List<CardState> selectCardStateById(Integer card_state_id);
	    public Grid selectAllCardStates(CardState cardState,String page, String size);
	    public List<CardState> selectUniqueState(String state_dm);
	    public Integer selectCard(String card_state_id);
	    public List<CardState> selectUniqueStateName(String state_name);
	    public Integer selectEditable(String card_state_id);
}
