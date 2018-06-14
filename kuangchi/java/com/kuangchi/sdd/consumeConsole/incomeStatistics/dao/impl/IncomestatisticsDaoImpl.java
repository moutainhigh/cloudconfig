package com.kuangchi.sdd.consumeConsole.incomeStatistics.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.dao.IIncomestatisticsDao;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;

@Repository("IncomestatisticsDaoImpl")
public class IncomestatisticsDaoImpl extends BaseDaoImpl<IncomeStatistics> implements IIncomestatisticsDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	

	/**
	 * 查询所有消费信息
	 * guibo.chen
	 */
	@Override
	public List<IncomeStatistics> selectAllIncomeStatistics(
			IncomeStatistics income_record, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("staff_name", income_record.getStaff_name());
		mapState.put("dept_num", income_record.getDept_num());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		mapState.put("type", income_record.getType());
		return this.getSqlMapClientTemplate().queryForList("selectAllIncomeStatistics", mapState);
	}
	/**
	 * 查询总条数
	 */
	@Override
	public Integer getAllIncomeStatisticsCount(IncomeStatistics income_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("staff_name", income_record.getStaff_name());
		mapState.put("dept_num", income_record.getDept_num());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		mapState.put("type", income_record.getType());
		return queryCount("getAllIncomeStatisticsCount",mapState);
	}
	/**
	 * 导出消费信息
	 */
	@Override
	public List<IncomeStatistics> exportAllIncomestatistics(
			IncomeStatistics income_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("staff_name", income_record.getStaff_name());
		mapState.put("dept_num", income_record.getDept_num());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		mapState.put("type", income_record.getType());
		return this.getSqlMapClientTemplate().queryForList("exportAllIncomestatistics", mapState);
	}
	
	/**
	 * 查询所有员工编号
	 */
	@Override
	public List<IncomeStatistics> selectByStaffno() {
		return this.getSqlMapClientTemplate().queryForList("selectByStaffno", "");
	}
	
	/**
	 * 查询个人收支报表信息
	 */
	@Override
	public List<IncomeStatistics> selectStatistics(
			IncomeStatistics income_record,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectStatistics", mapState);
	}

	@Override
	public Integer selectStatisticsCount(IncomeStatistics income_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		return queryCount("selectStatisticsCount",mapState);
	}
	
	//导出报表汇总
	@Override
	public List<IncomeStatistics> ExportAllStatistics(
			IncomeStatistics income_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", income_record.getStaff_no());
		mapState.put("begin_time", income_record.getBegin_time());
		mapState.put("end_time", income_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("ExportAllStatistics", mapState);
		
	}
	
	
	
	
}
