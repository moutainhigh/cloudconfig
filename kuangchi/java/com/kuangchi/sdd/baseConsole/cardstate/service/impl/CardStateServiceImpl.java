package com.kuangchi.sdd.baseConsole.cardstate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.cardstate.dao.ICardStateDao;
import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;
import com.kuangchi.sdd.baseConsole.cardstate.service.ICardStateService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
@Service("cardStateServiceImpl")
public class CardStateServiceImpl implements ICardStateService {
	private static final int CARD_ID_LENGTH = 6;
	@Autowired
	private ICardStateDao cardStateDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	/**
	 * 新增卡片状态信息
	 */
	@Override
	public Boolean insertCardState(CardState cardState) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "卡片状态管理");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", cardState.getCreate_user());
        try{
        	Boolean obj=cardStateDao.insertCardState(cardState);
        	
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "新增成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "新增失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return false;
        }
		
	}
	/**
	 * 删除卡片状态信息
	 */
	@Override
	public Boolean deleteCardState(String card_state_id) {
		Boolean obj=cardStateDao.deleteCardState(card_state_id);
		if(obj!=null){
			Map<String, String> log = new HashMap<String, String>();
	        log.put("V_OP_NAME", "卡片状态管理");
	        log.put("V_OP_FUNCTION", "删除");
	        log.put("V_OP_ID", card_state_id);
	        log.put("V_OP_TYPE", "业务");
	        log.put("V_OP_MSG", "删除成功");
	        logDao.addLog(log);
	        return true; 
		}
		return false;
	}
	/**
	 * 修改卡片状态信息
	 */
	@Override
	public Boolean updateCardState(CardState cardState) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "卡片状态管理");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", cardState.getCreate_user());
        try{
        	Boolean obj=cardStateDao.updateCardState(cardState);
    		if(obj==true){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改成功");
    	        logDao.addLog(log);
    	    return true;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改失败");
    			logDao.addLog(log);
    			return false;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	}
	
	/**
	 * 通过ID查询信息
	 */
	@Override
	public List<CardState> selectCardStateById(Integer card_state_id) {
		List<CardState> cardState=(List<CardState>)cardStateDao.selectCardStateById(card_state_id);
		return cardState;
	}
	/**
	 * 查询卡片状态信息
	 */
	@Override
	public Grid selectAllCardStates(CardState cardState,String page, String size) {
			Integer count=this.cardStateDao.getAllCardStateCount(cardState);
			List<CardState> state= this.cardStateDao.selectAllCardStates(cardState, page, size);
			Grid grid=new Grid();
			grid.setTotal(count);
			grid.setRows(state);
			return grid;
	}
	
	/**
	 * 状态代码唯一查询
	 */
	@Override
	public List<CardState> selectUniqueState(String state_dm) {
		return this.cardStateDao.selectUniqueCardState(state_dm);
	}
	/**
	 * 状态名称唯一查询
	 */
	public List<CardState> selectUniqueStateName(String state_name) {
		return this.cardStateDao.selectCardStateName(state_name);
	}
	
	
	/**
	 * 查询卡片状态信息
	 */
	@Override
	public Integer selectCard(String card_state_id) {
		return this.cardStateDao.selectCard(card_state_id);
	}
	
	/**
	 * 查询是不是内置状态
	 */
	
	public Integer selectEditable(String card_state_id){
		return this.cardStateDao.selectEditable(card_state_id);
	}
	
	
	
	
}
