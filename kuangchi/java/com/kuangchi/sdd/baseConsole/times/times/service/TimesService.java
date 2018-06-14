package com.kuangchi.sdd.baseConsole.times.times.service;

import java.util.List;

import com.kuangchi.sdd.baseConsole.times.times.model.Times;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:55
 * @功能描述: 时段管理模块-业务类
 */
public interface TimesService {
	
    // 新增时段信息 
	boolean addTimes(Times times, String loginUser);
	
	// 删除时段信息
	boolean deleteTimesById(String times_ids, String loginUser);
	
	// 修改时段信息
	boolean modifyTimes(Times times, String loginUser);
    
    // 根据条件查询时段信息 (分页)
    List<Times> getTimesByParamPage(Times times,int page, int size);
   
    // 根据条件查询时段信息总数 
    int getTimesByParamCount(Times times);
    
    // 根据编号查询关联时段组名称 
    List<String> getTimesGroupByTimesNum(String times_num);
    
    // 时段下发
    void issuedTime(String deviceNums) throws Exception;
}
	