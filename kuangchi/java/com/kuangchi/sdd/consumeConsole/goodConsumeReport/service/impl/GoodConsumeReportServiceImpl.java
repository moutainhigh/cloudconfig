package com.kuangchi.sdd.consumeConsole.goodConsumeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.dao.GoodConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.model.GoodConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.service.GoodConsumeReportService;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-16 
 * @功能描述: 商品消费报表-业务实现层
 */
@Transactional
@Service("goodConsumeReportServiceImpl")
public class GoodConsumeReportServiceImpl implements GoodConsumeReportService{
    
	@Resource(name = "goodConsumeReportDaoImpl")
	private GoodConsumeReportDao goodConsumeReportDao;
	
	@Override
	public List<GoodConsumeReportModel> getGoodConsumeReportByParam(
			Map<String, Object> map) {
		return goodConsumeReportDao.getGoodConsumeReportByParam(map);
	}

	@Override
	public Integer getGoodConsumeReportCount(Map<String, Object> map) {
		return goodConsumeReportDao.getGoodConsumeReportCount(map);
	}

	@Override
	public List<GoodConsumeReportModel> getGoodConsumeReportNoLimit(
			Map<String, Object> map) {
		return goodConsumeReportDao.getGoodConsumeReportNoLimit(map);
	}
	

	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNum(
			Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodNum(map);
	}

	@Override
	public Integer countGoodConsumeReportByGoodNumCounts(Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodNumCounts(map);
	}

	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNumNoLimit(
			Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodNumNoLimit(map);
	}

	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodType(
			Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodType(map);
	}

	@Override
	public Integer countGoodConsumeReportByGoodTypeCounts(
			Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodTypeCounts(map);
	}

	@Override
	public List<GoodConsumeReportModel> countGoodConsumeReportByGoodTypeNoLimit(
			Map<String, Object> map) {
		return goodConsumeReportDao.countGoodConsumeReportByGoodTypeNoLimit(map);
	}

	@Override
	public List<GoodType> getGoodType() {
		return goodConsumeReportDao.getGoodType();
	}

	@Override
	public List<Good> getGoodName() {
		return goodConsumeReportDao.getGoodName();
	}
}
