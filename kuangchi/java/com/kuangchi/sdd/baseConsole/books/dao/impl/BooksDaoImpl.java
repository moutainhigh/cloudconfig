package com.kuangchi.sdd.baseConsole.books.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.books.dao.IBooksDao;
import com.kuangchi.sdd.baseConsole.books.model.Books;
import com.kuangchi.sdd.baseConsole.books.model.Param;

@Repository("booksDaoImpl")
public class BooksDaoImpl extends BaseDaoImpl<Books> implements IBooksDao {
	@Override
	public void addNewBooks(Books books) {
		this.getSqlMapClientTemplate().insert("addNewBooks", books);
		
	}


	@Override
	public void modifyBooks(Books books) {
		this.getSqlMapClientTemplate().update("modifyBooks", books);
		
	}


	@Override
	public void deleteBooks(String id) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("id", id);
		this.getSqlMapClientTemplate().update("deleteBooks", mapParam);
	}

	@Override
	public List<Books> getAllBooksByParam(Param param, String page, String size) {
		int pages = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", (pages - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("value", param.getValue());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("type", param.getType());
		return this.getSqlMapClientTemplate().queryForList("getAllBooksByParam", mapParam);
	}
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getNameSpace() {
		return "common.Card";  
	}


	@Override
	public List<Books> getBooksByValue(String value) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("value", value);
		return this.getSqlMapClientTemplate().queryForList("getCountByValue", mapParam);
	}


	
	public int getAllBooksCount(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("begin_time",param.getBegin_time());
		mapParam.put("end_time",param.getEnd_time());
		mapParam.put("type", param.getType());
		mapParam.put("value", param.getValue());
		return queryCount("getAllBooksCount",mapParam);
	}


	@Override
	public List<Books> getWorkBookByNameAndType(String name, String type) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("wordbook_name", name);
		 map.put("wordbook_type", type);
		 
		return getSqlMapClientTemplate().queryForList("getWorkBookByNameAndType",map);
	}

}
