package com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:43:40
 * @功能描述: 消费分组管理-dao
 */
public interface IGroupMapDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费分组[分页]
	 * @参数描述:
	 */
	public List<GroupMapModel> getGroupMapByParamPage(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午2:44:37
	 * @功能描述: 根据参数查询消费分组
	 * @参数描述:
	 */
	public List<GroupMapModel> getGroupMapByParam(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-4 上午11:04:59
	 * @功能描述: 根据参数查询消费分组[总数]
	 * @参数描述:
	 */
	public Integer getGroupMapByParamCount(Map<String, Object> map);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午5:24:52
	 * @功能描述: 新增默认员工分组（添加员工时使用）
	 * @参数描述:
	 */
	public boolean addGroupMap(GroupMapModel groupMap);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:06:28
	 * @功能描述: 修改消费分组
	 * @参数描述:
	 */
	public boolean modifyGroupMap(GroupMapModel groupMap);
	
	/**
	 * 对未分组员工进行分组
	 * @author yuman.gao
	 */
	public boolean newGroupMap(GroupMapModel groupMap);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午8:06:40
	 * @功能描述: 删除消费分组
	 * @参数描述:
	 */
	public boolean removeGroupMap(String delete_ids);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-29 下午4:30:51
	 * @功能描述: 根据员工编号查询姓名
	 * @参数描述:
	 */
	public List<GroupMapModel> getStaffNameByNums(String staffNums);
	
	
	/**
	 * 查询未分组员工
	 * @author yuman.gao
	 */
	public List<Employee> searchEmployee(DepartmentPage departmentPage);
	
	/**
	 * 查询未分组员工总数
	 * @author yuman.gao
	 */
	public Integer searchEmployeeCount(DepartmentPage departmentPage);
	
	
	
	
	
	/**
	 * 查询员工已存在的餐次
	 * @author minting.he
	 * @param staff_num
	 * @return
	 */
	public List<Map> getMealsByStaff(String staff_num);
	
	/**
	 * 查询消费组对应的餐次
	 * @author minting.he
	 * @param group_num
	 * @return
	 */
	public List<String> getMealsByGroup(String group_num);
	
	/**
	 * 删除员工和餐次的映射
	 * @author minting.he
	 * @param staff_num
	 * @param group_num
	 * @return
	 */
	public boolean delByStaffAndMeal(Map<String, Object> map);
	
	/**
	 * 删除员工的默认组
	 * @author minting.he
	 * @param staff_num
	 * @return
	 */
	public boolean delDefaultByStaff(String staff_num);
	
	/**
	 * 新增消费分组映射
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean insertGroupMap(Map<String, Object> map);
	
	/**
	 * 删除后员工是否还有消费分组
	 * @author minting.he
	 * @param staff_num
	 * @return
	 */
	public Integer ifExistGroupByStaff(String staff_num);
	
}
