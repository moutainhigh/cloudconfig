package com.kuangchi.sdd.consumeConsole.goodConsumeReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.model.GoodConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-16 
 * @功能描述: 商品消费报表-dao
 */
public interface GoodConsumeReportDao {
    
/*获取所有商品消费报表信息（分页）*/
public List<GoodConsumeReportModel> getGoodConsumeReportByParam(Map<String, Object> map);
	
/*获取所有商品消费报表信息的总数量*/
public Integer getGoodConsumeReportCount(Map<String, Object> map);

/*获取所有商品消费报表信息(导出用)*/
public List<GoodConsumeReportModel> getGoodConsumeReportNoLimit(Map<String, Object> map);

/*按商品编号统计商品消费报表信息（分页）*/
public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNum(Map<String, Object> map);

/*按商品编号统计商品消费报表信息的总数量*/
public Integer countGoodConsumeReportByGoodNumCounts(Map<String, Object> map);

/*按商品编号统计商品消费报表信息(导出用)*/
public List<GoodConsumeReportModel> countGoodConsumeReportByGoodNumNoLimit(Map<String, Object> map);

/*按商品类型统计商品消费报表信息（分页）*/
public List<GoodConsumeReportModel> countGoodConsumeReportByGoodType(Map<String, Object> map);

/*按商品类型统计商品消费报表信息的总数量*/
public Integer countGoodConsumeReportByGoodTypeCounts(Map<String, Object> map);

/*按商品类型统计商品消费报表信息(导出用)*/
public List<GoodConsumeReportModel> countGoodConsumeReportByGoodTypeNoLimit(Map<String, Object> map);

/*获取所有商品类型*/
public List<GoodType>  getGoodType();

/*获取所有商品名称，编号*/
public List<Good>  getGoodName();

	
}
