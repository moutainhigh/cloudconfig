package com.kuangchi.sdd.attendanceConsole.duty.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-27 上午11:38:27
 * @功能描述:工作班次模块-Dao类
 */
public interface DutyDao {
	/**新增工作班次*/
	boolean insertDuty(Duty duty);
	
	/**删除工作班次*/
	boolean deleteDutyById(String duty_ids);
	
	/**修改当前班次为默认时，先把其他班次改为非默认*/
	void updateAllIsDefault(String id);
	
	/**新增班次时，若将当前班次设为默认，则得先把其他班次都设为非默认*/
	void updateAllIsDefaultt(int id);
	
	/**查询当前数据库中记录的最大id值*/
	Integer selectMaxId();
	
	/**修改工作班次*/
	boolean updateDuty(Duty duty);
	
	/**按条件查询工作班次*/
	List<Duty> getDutyByParam(Duty duty);
	
	/**按条件查询工作班次(分页)*/
	List<Duty> getDutyByParamPage(Duty duty);
	
	/**按条件查询工作班次总数*/
	Integer getDutyByParamPageCounts(Duty duty);

	/** 批量导入工作班次*/
	void batchAddDuty(List<Duty> dutyList);
	
	 /**根据id查询工作班次信息*/
	 Duty getDutyById(String id);
	 
	 /**按班次类型精确查询工作班次总数*/
	 Integer getDutyByParamCounts(Duty duty);

	 List<Duty> getDutyClassesInfo();//查询排班信息guibo.chen
	 
	public  Duty getDefaultDuty();
	
	/**
	 * 查看默认班次id
	 * @author minting.he
	 * @return
	 */
	public String getDefautlId();
	
	/**
	 * 将默认班次改为非默认
	 * @author minting.he
	 * @param id
	 * @return
	 */
	public boolean updateIsDefault(String id);
	
	/**
	 * 设置为默认班次
	 * @author minting.he
	 * @param id
	 * @return
	 */
	public boolean setDefaultDuty(String id);
	

}
