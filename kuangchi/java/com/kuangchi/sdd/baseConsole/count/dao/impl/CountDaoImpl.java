package com.kuangchi.sdd.baseConsole.count.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.count.dao.ICountDao;
import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.model.CardInfo;
import com.kuangchi.sdd.baseConsole.count.model.CountCardAuthority;
import com.kuangchi.sdd.baseConsole.count.model.CountWarining;
import com.kuangchi.sdd.baseConsole.count.model.DepartmentInfo;
import com.kuangchi.sdd.baseConsole.count.model.KcardState;
import com.kuangchi.sdd.baseConsole.count.model.ParamState;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-1下午5:47:00
 * @功能描述:通过状态名称查询信息Dao实现类
 * @参数描述:
 */
@Repository("countDaoImpl")
public class CountDaoImpl extends BaseDaoImpl<CardHistoryModel> implements ICountDao{

	@Override
	//分页
	public List<CardHistoryModel> getCardHistoryByParam(Map param) {
		return  getSqlMapClientTemplate().queryForList("getCardHistoryByParam", param); 
	}
	

	@Override
	public Integer getCardHistoryCount(Map param) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCardHistoryCount", param);
	}


	@Override
	public String getNameSpace() {
		return "common.Count";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<KcardState> getAllCardStateNames() {
		return this.getSqlMapClientTemplate().queryForList("getAllCardStateNames");
	}
	@Override
	public List<CountCardAuthority> getCardAuthorityByName(ParamState staff_name, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", staff_name.getStaff_name());
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		List listcard = this.getSqlMapClientTemplate().queryForList("getCardAuthorityByName", mapParam);
		return listcard;
	}

	@Override
	public Integer getCardAuthorityCount(ParamState staff_name) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", staff_name.getStaff_name());
		Integer c = this.queryCount("getCardAuthorityCount", mapParam);
		return c;
	}

	@Override
	public List<CountCardAuthority> getCardAuthorityByNameList(ParamState staff_name) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", staff_name.getStaff_name());
		return this.getSqlMapClientTemplate().queryForList("getCardAuthorityByNameList", mapParam);
	}


	@Override
	public List<Map> getCardHistoryOperate() {
		return getSqlMapClientTemplate().queryForList("getCardHistoryOperate");
	}


	@Override
	public List<CardHistoryModel> getCardHistoryList(Map map) {
		return getSqlMapClientTemplate().queryForList("getCardHistoryList", map);
	}


	@Override
	public List<Map> getPeopleAuthorityInfoByStaffNumForCount(Map map) {
		return getSqlMapClientTemplate().queryForList("getPeopleAuthorityInfoByStaffNumForCount", map);
	}


	@Override
	public Integer countPeopleAuthorityInfoByStaffNumForCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countPeopleAuthorityInfoByStaffNumForCount", map);
	}


	@Override
	public List<Map> getPeopleAuthorityInfoByStaffNumForExcel(Map map) {
		return getSqlMapClientTemplate().queryForList("getPeopleAuthorityInfoByStaffNumForExcel", map);
	}



	@Override
	public boolean insertCardHistoryInfo(CardHistoryModel cardHistoryModel) {
		return insert("insertCardHistoryInfo",cardHistoryModel);	
	}


	@Override
	public List<CountWarining> getCountWariningInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getCountWariningInfo", map);
		
	}


	@Override
	public Integer getCountWariningInfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountWariningInfoCount", map);
	}


	@Override
	public List<CountWarining> exportCountWariningInfo(Map map) {
		return  getSqlMapClientTemplate().queryForList("exportCountWariningInfo", map);
	}
	
	@Override
	public List<DepartmentInfo> getCountDepartmentInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getCountDepartmentInfo", map);
		
	}
	@Override
	public Integer getCountDepartmentInfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountDepartmentInfoCount", map);
	}

	@Override
	public List<DepartmentInfo> exportCountDepartmentInfo(Map map) {
		return  getSqlMapClientTemplate().queryForList("exportCountDepartmentInfo", map);
	}
	
	@Override
	public List<CardInfo> getCountCardInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getCountCardInfo", map);
		
	}
	@Override
	public Integer getCountCardInfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountCardInfoCount", map);
	}
	@Override
	public List<CardInfo> exportCountCardInfo(Map map) {
		return  getSqlMapClientTemplate().queryForList("exportCountCardInfo", map);
	}
	
	

}
