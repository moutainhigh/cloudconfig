package com.kuangchi.sdd.consumeConsole.consumeType.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.impl.AttendExceptionDaoImpl;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.consumeType.dao.IConsumeTypeDao;
import com.kuangchi.sdd.consumeConsole.consumeType.model.ConsumeTypeModel;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:01
 * @功能描述: 消费类型管理-dao实现层
 */
@Repository("consumeTypeDaoImpl")
public class ConsumeTypeDaoImpl extends BaseDaoImpl<Object> implements IConsumeTypeDao{

	@Override
	public String getNameSpace() {
		return "common.ConsumeType";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<ConsumeTypeModel> getConsumeTypeByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeTypeByParamPage", map);
	}
	
	@Override
	public List<ConsumeTypeModel> getConsumeTypeByParam(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeTypeByParam", map);
	}

	@Override
	public boolean addConsumeType(ConsumeTypeModel consumeType) {
		return this.insert("addConsumeType", consumeType);
	}

	@Override
	public boolean modifyConsumeType(ConsumeTypeModel consumeType) {
		return this.update("modifyConsumeType", consumeType);
	}

	@Override
	public boolean removeConsumeType(String delete_ids) {
		return this.update("removeConsumeType", delete_ids);
	}

	@Override
	public List<ConsumeTypeModel> getConsumeTypeByNum(String consume_type_num) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeTypeByNum", consume_type_num);
	}
	
	@Override
	public ConsumeTypeModel getConsumeTypeById(String id) {
		return (ConsumeTypeModel) this.getSqlMapClientTemplate().queryForObject("getConsumeTypeById", id);
	}

	@Override
	public Integer getConsumeTypeByParamCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getConsumeTypeByParamCount", map);
	}

	@Override
	public List<MealModel> getMealNum() {
		return getSqlMapClientTemplate().queryForList("getMealNum");
	}
	
	@Override
	public List<ConsumeTypeModel> getConsumeSameType(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getConsumeSameType", map);
	}
	
	@Override
	public Integer getConsumeSameTypeCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getConsumeSameTypeCount", map);
	}

}
