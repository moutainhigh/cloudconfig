package com.kuangchi.sdd.attendanceConsole.duty.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.attendanceConsole.duty.dao.DutyDao;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("dutyDaoImpl")
public class DutyDaoImpl extends BaseDaoImpl<Duty> implements DutyDao {

	@Override
	public boolean insertDuty(Duty duty) {
		return insert("insertDuty",duty);
	}

	@Override
	public boolean deleteDutyById(String duty_ids) {
		return delete("deleteDutyById",duty_ids);
	}

	@Override
	public boolean updateDuty(Duty duty) {
		return update("updateDuty",duty);
	}

	@Override
	public List<Duty> getDutyByParamPage(Duty duty) {
		return getSqlMapClientTemplate().queryForList("getDutyByParamPage", duty);
	}

	@Override
	public Integer getDutyByParamPageCounts(Duty duty) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyByParamPageCounts", duty);
	}

	@Override
	public List<Duty> getDutyByParam(Duty duty) {
		return getSqlMapClientTemplate().queryForList("getDutyByParam", duty);
	}

	@Override
	public void batchAddDuty(List<Duty> dutyList) {
		SqlMapClient sqlMapClient = getSqlMapClient();
		try {
			sqlMapClient.startBatch();
			sqlMapClient.startTransaction();
			for(int i = 0; i < dutyList.size(); i++){
				Duty duty = dutyList.get(i);
				sqlMapClient.insert("insertDuty", duty);
			}
		} catch (Exception e) {
			try {
				sqlMapClient.getCurrentConnection().rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Duty getDutyById(String id) {
		return (Duty) getSqlMapClientTemplate().queryForObject("getDutyById", id);
	}
	
	
	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public Integer getDutyByParamCounts(Duty duty) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyByParamCounts", duty);
	}

	@Override
	public void updateAllIsDefault(String id) {
		update("updateAllIsDefault",id);
	}

	@Override
	public void updateAllIsDefaultt(int id) {
		update("updateAllIsDefaultt",id);
		
	}

	@Override
	public Integer selectMaxId() {
		return (Integer) getSqlMapClientTemplate().queryForObject("selectMaxId");
	}

	@Override
	public List<Duty> getDutyClassesInfo() {
		return getSqlMapClientTemplate().queryForList("getDutyClassesInfo", "");
	}

	@Override
	public Duty getDefaultDuty() {
		return (Duty) getSqlMapClientTemplate().queryForObject("getDefaultDuty");
	}

	@Override
	public String getDefautlId(){
		return (String) getSqlMapClientTemplate().queryForObject("getDefautlId");
	}
	
	@Override
	public boolean updateIsDefault(String id){
		return update("updateIsDefault", id);
	}
	
	@Override
	public boolean setDefaultDuty(String id){
		return update("setDefaultDuty", id);
	}

	
}
