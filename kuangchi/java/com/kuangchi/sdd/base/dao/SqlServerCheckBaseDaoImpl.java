package com.kuangchi.sdd.base.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.base.exception.IDataAccessException;
public abstract class SqlServerCheckBaseDaoImpl<E extends Object> extends BaseDaoSupport<E>{

    // 第二种写法
    @Resource(name = "sqlServerCheckSqlMapClient")    
    private SqlMapClient sqlMapClient;    
    
    @PostConstruct        
    public void initSqlMapClient(){         
    	super.setSqlMapClient(sqlMapClient);    
    } 
    
    public boolean doDeleteById(long id) throws IDataAccessException {
        try {
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_ID, new Long(id));
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteByMap(Map<String, Object> map) throws DataAccessException {
        try {
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_MAP, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteWithStatementName(String statementName, Map<String, Object> map) throws IDataAccessException {
        try {
            return delete(this.getNameSpace() + "." + statementName, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }    
    public boolean doDeleteByObject(Object e) throws IDataAccessException {
        try {
//            e.setLast_upd_date(new Date());
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_OBJECT, e);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteByMasterId(long masterId) throws IDataAccessException {
        try {
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_MASTER_ID, new Long(masterId));
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteByMasterMap(Map<String, Object> map) throws IDataAccessException {
        try {
            boolean result = delete(this.getNameSpace() + "." + SQL_DELETE_BY_MASTER_MAP, map);
            return result;
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteByMasterObject(Object masterObject) throws IDataAccessException {
        try {
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_MASTER_OBJECT, masterObject);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doDeleteByString(String id) throws IDataAccessException {
        try {
            return delete(this.getNameSpace() + "." + SQL_DELETE_BY_STRING, new String(id));
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }

    public Object doFindById(long id) throws IDataAccessException {
        try {
            return find(this.getNameSpace()+"."+SQL_FIND_BY_ID, new Long(id));
        } catch(DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public Object doFindByMap(Map<String, Object> map) throws IDataAccessException {
        try {
            return find(this.getNameSpace()+"."+SQL_FIND_BY_MAP, map);
        } catch(DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public Object doFindWithStatementName(String statementName, Map<String, Object> map) throws IDataAccessException {
        try {
            return find(this.getNameSpace()+"."+statementName, map);
        } catch(DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }    

    public Object doFindByObject(Object e) throws IDataAccessException {
        try {
            return find(this.getNameSpace()+"."+SQL_FIND_BY_OBJECT, e);
        } catch(DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doInsertByMap(Map<String, Object> map) throws IDataAccessException {
        try {
            return insert(this.getNameSpace() + "." + SQL_INSERT_BY_MAP, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }

    public boolean doInsertByObject(Object e) throws IDataAccessException {
        try {
            return insert(this.getNameSpace() + "." + SQL_INSERT_BY_OBJECT, e);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doInsertWithStatementName(String statementName, Object e) throws IDataAccessException {
        try {
            return insert(this.getNameSpace() + "." + statementName, e);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }    
    
    public boolean doUpdateByMap(Map<String, Object> map) throws IDataAccessException {
        try {
            return update(this.getNameSpace() + "." + SQL_UPDATE_BY_MAP, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }

    public boolean doUpdateByObject(Object e) throws IDataAccessException {
        try {
            boolean result = update(this.getNameSpace() + "." + SQL_UPDATE_BY_OBJECT, e);
            return result;
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public boolean doUpdateWithStatementName(String statementName, Object e) throws IDataAccessException {
        try {
//            vo.setLast_upd_date(new Date());
            return update(this.getNameSpace() + "." + statementName, e);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public List<E> doQueryForListByMap(Map<String, Object> map) throws IDataAccessException {
        try {
            return queryForList(this.getNameSpace() + "." + SQL_QUERY_LIST_BY_MAP, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
        
    }
    
    public List<E> doQueryForListWithStatement(String statementName, Map<String, Object> map) throws IDataAccessException {
        try {
            return queryForList(this.getNameSpace() + "." + statementName, map);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public List<E> doQueryForListSkipByMap(Map<String, Object> map, int skip, int pageSize) throws IDataAccessException {
        try {
            return queryForListSkip(this.getNameSpace() + "." + SQL_QUERY_LIST_BY_MAP, map, skip, pageSize);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
        
    }
    
    public List<E> doQueryForListSkipWithStatement(String statementName, Map<String, Object> map, int skip, int pageSize) throws IDataAccessException {
        try {
            return queryForListSkip(this.getNameSpace() + "." + statementName, map, skip, pageSize);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }
    
    public int doQueryCount(Object parameterObject) throws IDataAccessException {
        try {
            return queryCount(this.getNameSpace() + "." + SQL_QUERY_COUNT, parameterObject);
        } catch (DataAccessException ex) {
            throw new IDataAccessException(ex.getCause().getCause().getMessage());
        }
    }    

    public abstract String getNameSpace();
    
    public abstract String getTableName();
}
