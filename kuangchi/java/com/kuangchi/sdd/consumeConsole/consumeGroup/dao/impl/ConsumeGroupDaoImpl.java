package com.kuangchi.sdd.consumeConsole.consumeGroup.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.impl.AttendExceptionDaoImpl;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.consumeGroup.dao.IConsumeGroupDao;
import com.kuangchi.sdd.consumeConsole.consumeGroup.model.ConsumeGroupModel;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:01
 * @功能描述: 消费组管理-dao实现层
 */
@Repository("consumeGroupDaoImpl")
public class ConsumeGroupDaoImpl extends BaseDaoImpl<Object> implements IConsumeGroupDao{

	@Override
	public String getNameSpace() {
		return "common.ConsumeGroup";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<ConsumeGroupModel> getConsumeGroupByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeGroupByParamPage", map);
	}
	
	@Override
	public List<ConsumeGroupModel> getConsumeGroupByParam(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeGroupByParam", map);
	}

	@Override
	public boolean addConsumeGroup(ConsumeGroupModel consumeGroup) {
		return this.insert("addConsumeGroup", consumeGroup);
	}

	@Override
	public boolean modifyConsumeGroup(ConsumeGroupModel consumeGroup) {
		return this.update("modifyConsumeGroup", consumeGroup);
	}

	@Override
	public boolean removeConsumeGroup(String delete_ids) {
		return this.update("removeConsumeGroup", delete_ids);
	}

	@Override
	public List<ConsumeGroupModel> getConsumeGroupByNum(String group_num) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeGroupByNum", group_num);
	}
	
	@Override
	public ConsumeGroupModel getConsumeGroupById(String id) {
		return (ConsumeGroupModel) this.getSqlMapClientTemplate().queryForObject("getConsumeGroupById", id);
	}

	@Override
	public Integer getConsumeGroupByParamCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getConsumeGroupByParamCount", map);
	}

	@Override
	public List<ConsumeGroupModel> getNonDefaultGroup(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getNonDefaultGroup", map);
	}
	
	@Override
	public String getMealNumByType(String consume_type_num){
		return (String) this.getSqlMapClientTemplate().queryForObject("getMealNumByType", consume_type_num);
	}
	

}
