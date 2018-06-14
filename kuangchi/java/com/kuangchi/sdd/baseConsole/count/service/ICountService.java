package com.kuangchi.sdd.baseConsole.count.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.model.CardInfo;
import com.kuangchi.sdd.baseConsole.count.model.CountCardAuthority;
import com.kuangchi.sdd.baseConsole.count.model.CountWarining;
import com.kuangchi.sdd.baseConsole.count.model.DepartmentInfo;
import com.kuangchi.sdd.baseConsole.count.model.KcardState;
import com.kuangchi.sdd.baseConsole.count.model.ParamState;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-1下午5:46:45
 * @功能描述:通过状态名称查询信息Service
 * @参数描述:
 */
public interface ICountService {
	/**
	 * 根据条件查询卡片日志(分页)
	*/
	public Grid<CardHistoryModel> getCardHistoryListByParam(Map map);
	
	/**获取卡片日志状态*/
	public List<Map> getCardHistoryOperate();
	
	/**获取卡片日志记录（按条件导出）*/
	public List<CardHistoryModel> getCardHistoryList(Map map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-3 下午3:30:08
	 * @功能描述:新增卡片日志信息 
	 * @参数描述:
	 */
	public boolean insertCardHistoryInfo(CardHistoryModel cardHistoryModel,String create_user);
	
	
	
	/**
	 * 获取卡片状态名称
	 */
	public List<KcardState> getAllCardStateNames();
	
	/**
	 * 根据条件查询
	 */
	/*public List<CardHistoryModel> getBoundByParamList(ParamState state_name);*/
	
	/**
	 * 已下发卡权限统计(根据员工姓名查询--分页)
	 */
	public Grid<CountCardAuthority> getCardAuthorityByName(ParamState staff_name, String Page, String size);
	
	/**
	 * 已下发卡权限统计(根据员工姓名查询)
	 */
	public List<CountCardAuthority> getCardAuthorityByNameList(ParamState staff_name);
	
	/**
	 * 权限统计  动态查询
	 * by gengji.yang
	 * @param map
	 * @return
	 */
	public Grid<Map> getPeopleAuthorityInfoByStaffNumForCount(Map map);
	public List<Map> getPeopleAuthorityInfoByStaffNumForExcel(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29
	 * @功能描述: 查询报警事件信息-Service
	 */
	public Grid<CountWarining> getCountWariningInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29
	 * @功能描述: 导出报警事件信息-Service
	 */
	public List<CountWarining> exportCountWariningInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 组织信息统计-Service
	 */
	public Grid<DepartmentInfo> getCountDepartmentInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 导出组织信息-Service
	 */
	public List<DepartmentInfo> exportCountDepartmentInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 卡片信息统计-Service
	 */
	public Grid<CardInfo> getCountCardInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 导出卡片信息-Service
	 */
	public List<CardInfo> exportCountCardInfo(Map map);
}
