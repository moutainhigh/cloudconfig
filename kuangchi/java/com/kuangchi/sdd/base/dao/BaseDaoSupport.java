package com.kuangchi.sdd.base.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public abstract class BaseDaoSupport<E> extends SqlMapClientDaoSupport {

    protected static final String SQL_FIND_BY_ID = "[find-by-id]";

    protected static final String SQL_FIND_BY_OBJECT = "[find-by-object]";

    protected static final String SQL_FIND_BY_MAP = "[find-by-map]";
	
    protected static final String SQL_DELETE_BY_ID = "[delete-by-id]";

    protected static final String SQL_DELETE_BY_MAP = "[delete-by-map]";

    protected static final String SQL_DELETE_BY_MASTER_ID = "[delete-by-masterid]";

    protected static final String SQL_DELETE_BY_MASTER_MAP = "[delete-by-mastermap]";

    protected static final String SQL_DELETE_BY_MASTER_OBJECT = "[delete-by-mastervo]";

    protected static final String SQL_DELETE_BY_STRING = "[delete-by-string]";

    protected static final String SQL_DELETE_BY_OBJECT = "[delete-by-object]";

    protected static final String SQL_INSERT_BY_MAP = "[insert-by-map]";

    protected static final String SQL_INSERT_BY_OBJECT = "[insert-by-object]";

    protected static final String SQL_UPDATE_BY_MAP = "[update-by-map]";

    protected static final String SQL_UPDATE_BY_OBJECT = "[update-by-object]";

    protected static final String SQL_QUERY_LIST_BY_MAP = "[query-list-by-map]";
    
    protected static final String SQL_QUERY_COUNT = "[query-query-count]";

    // protected static final String SQL_DELETE_I18N_BY_MAP = "[delete-i18n-by-map]";

    // protected static final String SQL_DELETE_I18N_BY_MASTER_MAP = "[delete-i18n-by-mastermap]";

    // protected static final String SQL_INSERT_I18N_BY_VO = "[insert-i18n-by-vo]";

    // protected static final String SQL_UPDATE_I18N_BY_VO = "[update-i18n-by-vo]";

    // protected static final String SQL_SEARCH_BY_MAP = "[search-by-map]";

    // protected static final String SQL_SEARCH_PAGE = "[search-page]";

    // protected static final String SQL_SEARCH_PAGE_BY_MAP = "[search-page-by-map]";

    // protected static final String SQL_SEARCH_PAGE_BY_CONDITION = "[search-page-by-condition]";

    // protected static final String SQL_SEARCH_LIST = "[search-list]";

    // protected static final String SQL_SEARCH_LIST_BY_CONDITION = "[search-list-by-condition]";

    // protected static final String SQL_SEARCH_BY_CONDITION = "[search-by-condition]";

    // protected static final String SQL_SEARCH_BY_MASTER_ID = "[search-by-masterid]";

    // protected static final String SQL_SEARCH_BY_MASTER_MAP = "[search-by-mastermap]";

    // protected static final String SQL_SEARCH_BY_MASTER_OBJECT = "[search-by-masterobject]";

    // protected static final String SQL_FIND_I18N_BY_OBJECT = "[find-i18n-by-object]";

    protected Object find(String dbkey, String statementName, Object parameterObject) throws DataAccessException {
        return getSqlMapClientTemplate().queryForObject(statementName, parameterObject);
    }

    protected Object find(String statementName, Object parameterObject) throws DataAccessException {
        return find(null, statementName, parameterObject);
    }

    protected boolean delete(String statementName, Object parameterObject) throws DataAccessException {
        int rows = getSqlMapClientTemplate().delete(statementName, parameterObject);
        return rows >= 0;
    }

    protected boolean insert(String statementName, Object parameterObject) throws DataAccessException {
        getSqlMapClientTemplate().insert(statementName, parameterObject);
        return true;
    }

    protected boolean update(String statementName, Object parameterObject) throws DataAccessException {
        int rows = getSqlMapClientTemplate().update(statementName, parameterObject);
        return rows >= 0;
    }

    @SuppressWarnings("unchecked")
    protected List<E> queryForList(String statementName, Object parameterObject) throws DataAccessException {
        List<E> list = getSqlMapClientTemplate().queryForList(statementName, parameterObject);
        return list;
    }
    
	public int queryCount(String statementName, Object parameterObject) {
		Integer records = (Integer)getSqlMapClientTemplate().queryForObject(statementName,parameterObject);
		return records.intValue();
	}	

    @SuppressWarnings("unchecked")
    protected List<E> queryForListSkip(String statementName, Object parameterObject, int skip, int pageSize) throws DataAccessException {
        List<E> list = getSqlMapClientTemplate().queryForList(statementName, parameterObject, skip, pageSize);
        return list;
    }

    public boolean isCreate(String type) {
        if ((type == null) || (type.length() == 0))
            return false;
        return "C".equals(type);
    }

    public boolean isUpdate(String type) {
        if ((type == null) || (type.length() == 0))
            return false;
        return "U".equals(type);
    }

    public boolean isDelete(String type) {
        if ((type == null) || (type.length() == 0))
            return false;
        return "D".equals(type);
    }
}