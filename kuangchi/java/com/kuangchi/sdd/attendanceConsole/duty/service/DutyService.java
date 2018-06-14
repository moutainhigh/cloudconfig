package com.kuangchi.sdd.attendanceConsole.duty.service;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.base.model.easyui.Grid;


/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-26 下午2:45:25
 * @功能描述:工作班次模块-业务类
 */
public interface DutyService {
	 /**新增工作班次*/
	 boolean insertDuty(Duty duty,String create_user);
	 
	 /**删除工作班次*/
	 boolean deleteDutyById(String duty_ids,String create_user);
	 
	 /**修改班次时，若设置当前班次为默认，则先修改其他班次为非默认*/
	 void updateAllIsDefault(String id);
	 
	 /**新增班次时，若新增的班次为默认，则先将其他班次改为非默认*/
	 void updateAllIsDefaultt(int id);
	 
	 /**查询当前数据库中记录的最大id值*/
	 Integer selectMaxId();
	 
	 /**修改工作班次*/
	 boolean updateDuty(Duty duty,String create_user);
	 
	 /**按条件查询工作班次*/
	 List<Duty> getDutyByParam(Duty duty);
	 
	 /**按条件查询工作班次（分页）*/
	 Grid<Duty> getDutyByParamPage(Duty duty);
	 
	 /** 批量导入工作班次*/
	 void batchAddDuty(List<Duty> dutyList);
	 
	 /**根据id查询工作班次信息*/
	 Duty getDutyById(String id);
	 
	 /**按班次类型精确查询工作班次总数*/ 
	 Integer getDutyByParamCounts(Duty duty);
	 
	 /**根据排班类型查询是否有员工排班*/
	 Integer getDutyUserByDutyId(String duty_id);
	 
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
	public boolean updateIsDefault(String id, String login_user);
	
	/**
	 * 设置为默认班次
	 * @author minting.he
	 * @param old_id
	 * @param new_id
	 * @return
	 */
	public boolean setDefaultDuty(String new_id, String login_user);

}
