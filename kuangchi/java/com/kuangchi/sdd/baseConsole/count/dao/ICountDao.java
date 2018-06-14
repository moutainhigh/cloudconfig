package com.kuangchi.sdd.baseConsole.count.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.model.CardInfo;
import com.kuangchi.sdd.baseConsole.count.model.CountCardAuthority;
import com.kuangchi.sdd.baseConsole.count.model.CountWarining;
import com.kuangchi.sdd.baseConsole.count.model.DepartmentInfo;
import com.kuangchi.sdd.baseConsole.count.model.KcardState;
import com.kuangchi.sdd.baseConsole.count.model.ParamState;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-1下午5:25:51
 * @功能描述:	通过状态名称查询信息Dao
 * @参数描述: @param state_name
 * @参数描述: @return
 */
public interface ICountDao {
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-3 下午2:58:11
	 * @功能描述: 新增卡片日志信息
	 * @参数描述:
	 */
	public boolean insertCardHistoryInfo(CardHistoryModel cardHistoryModel);
	
	
	/**
	 * 根据条件查询卡片日志(分页)
	*/
	public List<CardHistoryModel> getCardHistoryByParam(Map map);
	
	/**
	 * 根据条件查询卡片日志总数
	 * */
	public Integer getCardHistoryCount(Map map);
	
	/**获取卡片日志记录（按条件导出）*/
	public List<CardHistoryModel> getCardHistoryList(Map map);
	
	/**
	 * 获取卡片日志状态名称
	 */
	public List<Map> getCardHistoryOperate();
	/**
	 * 获取卡片状态名称
	 */
	public List<KcardState> getAllCardStateNames();
	
	/**
	 * 已下发卡权限统计(根据员工姓名查询--分页)
	 */
	public List<CountCardAuthority> getCardAuthorityByName(ParamState staff_name,String Page, String size);
	
	/**
	 * 查询记录总数
	 */
	public Integer getCardAuthorityCount(ParamState staff_name);
	
	/**
	 * 已下发卡权限统计(根据员工姓名查询)
	 */
	public List<CountCardAuthority> getCardAuthorityByNameList(ParamState staff_name);
	
	public List<Map> getPeopleAuthorityInfoByStaffNumForCount(Map map);
	
	public Integer countPeopleAuthorityInfoByStaffNumForCount(Map map);
	
	public List<Map> getPeopleAuthorityInfoByStaffNumForExcel(Map map);
	
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29 
	 * @功能描述: 查询报警事件信息-Dao
	 */
	public List<CountWarining> getCountWariningInfo(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29
	 * @功能描述: 查询报警事件信息总条数-Dao
	 */
	public Integer getCountWariningInfoCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29
	 * @功能描述: 导出报警事件信息-Dao
	 */
	public List<CountWarining> exportCountWariningInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25 
	 * @功能描述: 组织信息统计-Dao
	 */
	public List<DepartmentInfo> getCountDepartmentInfo(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 组织信息统计总条数-Dao
	 */
	public Integer getCountDepartmentInfoCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 导出组织信息-Dao
	 */
	public List<DepartmentInfo> exportCountDepartmentInfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25 
	 * @功能描述: 卡片信息统计-Dao
	 */
	public List<CardInfo> getCountCardInfo(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 卡片信息统计总条数-Dao
	 */
	public Integer getCountCardInfoCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-25
	 * @功能描述: 导出卡片信息-Dao
	 */
	public List<CardInfo> exportCountCardInfo(Map map);
	
}
