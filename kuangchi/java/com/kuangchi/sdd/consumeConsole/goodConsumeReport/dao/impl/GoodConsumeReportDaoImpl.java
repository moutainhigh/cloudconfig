package com.kuangchi.sdd.consumeConsole.goodConsumeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.dao.GoodConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.model.GoodConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-16 
 * @功能描述: 商品消费报表-dao实现层
 */

@Repository("goodConsumeReportDaoImpl")
public class GoodConsumeReportDaoImpl extends BaseDaoImpl<Object> implements GoodConsumeReportDao{


	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	/*获取所有商品消费报表信息（分页）*/
	public List<GoodConsumeReportModel> getGoodConsumeReportByParam(Map<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("getGoodConsumeReport", map);
	}
    
	/*获取所有商品消费报表信息的总数量*/
	@Override
	public Integer getGoodConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getGoodConsumeReportCounts", map);
	}
	
	/*获取所有商品消费报表信息(导出用)*/
	@Override
	public List<GoodConsumeReportModel> getGoodConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getGoodConsumeReportNoLimits", map);
	}
    
	/*按照商品编号统计商品消费报表信息（分页）*/
	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNum(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countGoodConsumeReportByGoodNum", map);
	}

	/*按照商品编号统计商品消费报表信息的总数量*/
	@Override
	public Integer countGoodConsumeReportByGoodNumCounts(Map<String, Object> map) {
		return  (Integer) this.getSqlMapClientTemplate().queryForObject("countGoodConsumeReportByGoodNumCounts", map);
	}

	/*按照商品编号统计商品消费报表信息(导出用)*/
	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNumNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countGoodConsumeReportByGoodNumNoLimit", map);
	}

	/*按照商品类型统计商品消费报表信息（分页）*/
	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodType(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countGoodConsumeReportByGoodType", map);
	}

	/*按照商品类型统计商品消费报表信息的总数量*/
	@Override
	public Integer countGoodConsumeReportByGoodTypeCounts(
			Map<String, Object> map) {
		return  (Integer) this.getSqlMapClientTemplate().queryForObject("countGoodConsumeReportByGoodTypeCounts", map);
	}

	/*按照商品类型统计商品消费报表信息(导出用)*/
	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodTypeNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countGoodConsumeReportByGoodTypeNoLimit", map);
	}
    
	
	/*获取所有商品类型*/
	@Override
	public List<GoodType> getGoodType() {
		return this.getSqlMapClientTemplate().queryForList("getGoodType1");
	}
    
	/*获取所有商品名称，编号*/
	@Override
	public List<Good> getGoodName() {
		return this.getSqlMapClientTemplate().queryForList("getGoodName");
	}

	
}
