package com.kuangchi.sdd.consumeConsole.consumeGroupMap.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:47:19
 * @功能描述: 消费分组管理-业务层
 */
public interface IGroupMapService {
	
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
	 * @功能描述: 新增消费分组
	 * @参数描述:
	 */
	public boolean addGroupMap(GroupMapModel consumeGroupMap, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午7:07:28
	 * @功能描述: 修改消费分组
	 * @参数描述:
	 */
	public boolean modifyGroupMap(GroupMapModel consumeGroupMap, String loginUserName);
	
	/**
	 * 对未分组员工进行分组
	 * @author yuman.gao
	 */
	public boolean newGroupMap(GroupMapModel consumeGroupMap, String loginUserName);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-27 下午8:06:40
	 * @功能描述: 删除消费分组
	 * @参数描述:
	 */
	public boolean removeGroupMap(String delete_ids, String loginUserName);
	
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
	public Grid<Employee> searchEmployee(DepartmentPage departmentPage);
	
	
	
	
	
	
	/**
	 * 新增员工和消费组映射
	 * @author minting.he
	 * @param groupNums
	 * @param staffNums
	 * @param login_user
	 * @return
	 * @throws Exception 
	 */
	public boolean insertGroupMap(GroupMapModel groupMap, String login_user) throws Exception;
	
	/**
	 * 删除消费分组
	 * @author minting.he
	 * @param group_num
	 * @param staff_num
	 * @param login_user
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteGroupMap(String group_num, String staff_num, String login_user) throws Exception;
	
	/**
	 * 查询消费组对应的餐次
	 * @author minting.he
	 * @param group_num
	 * @return
	 */
	public List<String> getMealsByGroup(String group_num);
	
}
