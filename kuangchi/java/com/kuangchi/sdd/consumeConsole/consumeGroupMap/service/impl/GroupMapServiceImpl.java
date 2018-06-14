
package com.kuangchi.sdd.consumeConsole.consumeGroupMap.service.impl;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.IGroupMapDao;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.service.IGroupMapService;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:51
 * @功能描述: 消费分组管理-业务实现层
 */
@Transactional
@Service("groupMapServiceImpl")
public class GroupMapServiceImpl implements IGroupMapService{
	
	@Resource(name = "groupMapDaoImpl")
	private IGroupMapDao groupMapDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<GroupMapModel> getGroupMapByParamPage(Map<String, Object> map) {
		return groupMapDao.getGroupMapByParamPage(map);
	}
	
	@Override
	public List<GroupMapModel> getGroupMapByParam(Map<String, Object> map) {
		return groupMapDao.getGroupMapByParam(map);
	}

	@Override
	public boolean addGroupMap(GroupMapModel groupMap, String loginUserName) {
		
		boolean result = groupMapDao.addGroupMap(groupMap);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费分组管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "新增消费分组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean modifyGroupMap(GroupMapModel groupMap,String loginUserName) {
		
		boolean result = groupMapDao.modifyGroupMap(groupMap);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费分组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "修改消费分组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}
	
	@Override
	public boolean newGroupMap(GroupMapModel groupMap,String loginUserName) {
		
		boolean result = false;
		// 批量分组时创建时间相同会导致页面排序不稳定，所以分批增加
		for (String staffNum : groupMap.getStaff_num().split(",")) {
			String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
			groupMap.setStaff_num(staffNum);
			groupMap.setCreate_time(createTime);
			result = groupMapDao.newGroupMap(groupMap);
		}
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费分组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "修改消费分组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean removeGroupMap(String delete_ids, String loginUserName) {
		
		boolean result = groupMapDao.removeGroupMap(delete_ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "消费分组管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "删除消费分组");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public List<GroupMapModel> getStaffNameByNums(String staffNums) {
		return groupMapDao.getStaffNameByNums(staffNums);
	}

	@Override
	public Integer getGroupMapByParamCount(Map<String, Object> map) {
		return groupMapDao.getGroupMapByParamCount(map);
	}
	
	 @Override
    public Grid<Employee> searchEmployee(DepartmentPage departmentPage) {
        Grid<Employee> grid = new Grid<Employee>();
        List<Employee> resultList = groupMapDao.searchEmployee(departmentPage);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(groupMapDao.searchEmployeeCount(departmentPage));
        } else {
            grid.setTotal(0);
        }
        return grid;
    }
	 
	@Override
	public boolean insertGroupMap(GroupMapModel groupMap, String login_user) throws Exception{
		Map<String, String> log = new HashMap<String, String>();
		try{
			String[] staffNums = groupMap.getStaff_num().split(",");
			String[] groupNums = groupMap.getGroup_num().split(",");
			
			//找出员工已存在的相同餐次并删除
			List<String> groupMeals = groupMapDao.getMealsByGroup(groupMap.getGroup_num());
			for(String staff_num : staffNums){
				List<Map> staffMeals = groupMapDao.getMealsByStaff(staff_num);
				for(int i=0; i<staffMeals.size(); i++){
					String meal = staffMeals.get(i).get("meal_num").toString();
					for(int j=0; j<groupMeals.size(); j++){
						if(meal.equals(groupMeals.get(j).toString())){
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("staff_num", staff_num);
							map.put("group_num", staffMeals.get(i).get("group_num").toString());
							groupMapDao.delByStaffAndMeal(map);
						}
					}
				}
			}
			//新增
			for(int i=0; i<staffNums.length; i++){
				boolean r1 = true;		//groupMapDao.delDefaultByStaff(staffNums[i]);
				if(r1){
					for(int j=0; j<groupNums.length; j++){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("staff_num", staffNums[i]);
						map.put("group_num", groupNums[j]);
						groupMapDao.insertGroupMap(map);
					}
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e){
			log.put("V_OP_TYPE", "异常");
			throw e;
		}finally{
			log.put("V_OP_NAME", "消费分组管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增消费分组");
			logDao.addLog(log);
		}
	}
	
	@Override
	public boolean deleteGroupMap(String group_num, String staff_num, String login_user) throws Exception{
		Map<String, String> log = new HashMap<String, String>();
		try{
			String[] staffNums = staff_num.split(",");
			String[] groupNums = group_num.split(",");
			for(int i=0; i<staffNums.length; i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("staff_num", staffNums[i]);
				map.put("group_num", groupNums[i]);
				groupMapDao.delByStaffAndMeal(map);
				
				//删除消费组后，查看该员工是否还有消费分组，没有则新增默认消费组
				/*Integer count = groupMapDao.ifExistGroupByStaff(staffNums[i]);
				if(count==0){
					map.put("group_num", "1");
					groupMapDao.insertGroupMap(map);
				}*/
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e){
			log.put("V_OP_TYPE", "异常");
			throw e;
		}finally{
			log.put("V_OP_NAME", "消费分组管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除消费分组");
			logDao.addLog(log);
		}
	}
	
	@Override
	public List<String> getMealsByGroup(String group_num){
		try{
			return groupMapDao.getMealsByGroup(group_num);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
