package com.kuangchi.sdd.consumeConsole.nameList.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.consumeConsole.nameList.dao.NameListDao;
import com.kuangchi.sdd.consumeConsole.nameList.model.NameList;
import com.kuangchi.sdd.consumeConsole.nameList.service.NameListService;

@Transactional
@Service("nameListServiceImpl")
public class NameListServiceImpl extends BaseServiceSupport implements NameListService {
	
	@Resource(name="nameListDaoImpl")
	private NameListDao nameListDao;
	
	
	@Override
	public Grid<NameList> getNameList(Map map) {
		Grid<NameList> grid = new Grid<NameList>();
		List<NameList> list =nameListDao.getNameList(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	nameListDao.getNameListCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}


	@Override
	public List<CardType> getAllCardType() {
		return nameListDao.getAllCardType();
	}


}
