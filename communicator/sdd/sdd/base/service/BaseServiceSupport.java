package com.kuangchi.sdd.base.service;

import org.springframework.dao.DataAccessException;

public abstract class BaseServiceSupport implements IBaseServiceSupport {

	public Object doFind(String dbkey, String statementName,
			Object parameterObject) throws DataAccessException {
		return null;
	}

	public Object doFind(String statementName, Object parameterObject)
			throws DataAccessException {
		return null;
	}

	public boolean doDelete(String statementName, Object parameterObject)
			throws DataAccessException {
		return true;
	}

	public boolean doInsert(String statementName, Object parameterObject)
			throws DataAccessException {
		return true;
	}

	public boolean doUpdate(String statementName, Object parameterObject)
			throws DataAccessException {
		return true;
	}

}