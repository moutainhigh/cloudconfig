package com.kuangchi.sdd.base.service;

import org.springframework.dao.DataAccessException;

public interface IBaseServiceSupport {

	public Object doFind(String dbkey, String statementName,
			Object parameterObject) throws DataAccessException;

	public Object doFind(String statementName, Object parameterObject)
			throws DataAccessException;

	public boolean doDelete(String statementName, Object parameterObject)
			throws DataAccessException;

	public boolean doInsert(String statementName, Object parameterObject)
			throws DataAccessException;

	public boolean doUpdate(String statementName, Object parameterObject)
			throws DataAccessException;

}
