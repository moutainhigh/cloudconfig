package com.xkd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.SysOperateDao;
import com.xkd.model.Operate;

@Service
public class SysOperateService {
	@Autowired
	SysOperateDao sysOperateDao;
	@Autowired
	SysUserOperateService sysUserOperateService;
	@Autowired
	SysRoleOperateService sysRoleOperateService;
	@Autowired
	UserService userService;
	@Autowired
	CustomerService customerService;

	public int insert(Map<String, Object> map) {
		return sysOperateDao.insert(map);
	}

	public int update(Map<String, Object> map) {
		return sysOperateDao.update(map);
	}

	public int delete(String id) {
		sysUserOperateService.deleteByOperateId(id);// 删除用户权限关系
		sysRoleOperateService.deleteByOperateId(id);// 删除角色权限关系
		return sysOperateDao.delete(id);

	}

	public List<Operate> selectAllOperate() {
		return sysOperateDao.selectAllOperate();
	}

	public List<Map<String, Object>> selectOperateByRoleId(String roleId,String loginUserId) {
		// 查询出所有权限列表
		List<Operate> operateList =null;

		Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);

//		if ("1".equals(loginUserMap.get("roleId"))){
			//如果是超级管理员，则返回所有权限列表
			operateList=sysOperateDao.selectAllOperate();
//		}
//		else {
//			//否则返回配置表中的权限列表
//			operateList=sysOperateDao.selectOperatesByDepartmentId((String) loginUserMap.get("pcCompanyId"));
//		}

		// 查询某一个角色拥有的权限列表
		List<String> operateIds = sysRoleOperateService.selectOpereateIdsByRoleId(roleId);

		/**
		 * 将拥有的权限标记出来
		 */
		for (int i = 0; i < operateIds.size(); i++) {
			for (int j = 0; j < operateList.size(); j++) {
				if (operateIds.get(i).equals(operateList.get(j).getId())) {
					operateList.get(j).setChecked(true);
					operateList.get(j).setFromRole(true);
				}
			}
		}

		/**
		 * 找出权限对应了哪些菜单
		 */
		List<String> menuIdList = new ArrayList<>();
		for (int i = 0; i < operateList.size(); i++) {
			String menuId = operateList.get(i).getMenuId();
			if (!menuIdList.contains(menuId)) {
				menuIdList.add(menuId);
			}
		}

		/**
		 * 组装数据结构，将权限列表挂到菜单下面
		 */
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (int i = 0; i < menuIdList.size(); i++) {
			String menuId = menuIdList.get(i);
			Map<String, Object> menuOperateMap = new HashMap<>();
			menuOperateMap.put("menuId", menuId);
			List<Operate> opList = new ArrayList<>();
			for (int j = 0; j < operateList.size(); j++) {
				if (menuId.equals(operateList.get(j).getMenuId())) {
					menuOperateMap.put("menuName", operateList.get(j).getMenuName());
					menuOperateMap.put("content", operateList.get(j).getContent());
					menuOperateMap.put("parentContent", operateList.get(j).getParentContent());
					menuOperateMap.put("parentMenuName",operateList.get(j).getParentMenuName());
					menuOperateMap.put("parentMenuId",operateList.get(j).getParentMenuId());
					opList.add(operateList.get(j));
				}
			}
			menuOperateMap.put("operateList", opList);
			mapList.add(menuOperateMap);
		}

		return mapList;

	}



	public List<Map<String, Object>> selectOperateByUserId(String userId,String  loginUserId) {

		Map<String, Object> userMap = userService.selectUserById(userId);

		Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);

		// 查询出所有权限列表
		List<Operate> operateList =null;

			operateList= sysOperateDao.selectAllOperate();

//		else{
//			operateList=sysOperateDao.selectOperatesByDepartmentId((String) loginUserMap.get("pcCompanyId"));
//		}

		// 查询某一个角色拥有的权限列表
		List<String> operateIdsOfRole = sysRoleOperateService
				.selectOpereateIdsByRoleId((String)userMap.get("roleId"));

		/**
		 * 将角色的权限标记出来
		 */
		for (int i = 0; i < operateIdsOfRole.size(); i++) {
			for (int j = 0; j < operateList.size(); j++) {
				if (operateIdsOfRole.get(i).equals(operateList.get(j).getId())) {
					operateList.get(j).setChecked(true);
					operateList.get(j).setFromRole(true);
				}
			}
		}

		/**
		 * 将用户的权限标记出来
		 */
		List<String> operateIdsOfUser = sysUserOperateService.selectOperateIdsByUserId(userId);
		for (int i = 0; i < operateIdsOfUser.size(); i++) {
			for (int j = 0; j < operateList.size(); j++) {
				if (operateIdsOfUser.get(i).equals(operateList.get(j).getId())) {
					operateList.get(j).setChecked(true);
				}
			}
		}

		/**
		 * 找出权限对应了哪些菜单
		 */
		List<String> menuIdList = new ArrayList<>();
		for (int i = 0; i < operateList.size(); i++) {
			String menuId = operateList.get(i).getMenuId();
			if (!menuIdList.contains(menuId)) {
				menuIdList.add(menuId);
			}
		}

		/**
		 * 组装数据结构，将权限列表挂到菜单下面
		 */
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (int i = 0; i < menuIdList.size(); i++) {
			String menuId = menuIdList.get(i);
			Map<String, Object> menuOperateMap = new HashMap<>();
			menuOperateMap.put("menuId", menuId);
			List<Operate> opList = new ArrayList<>();
			for (int j = 0; j < operateList.size(); j++) {
				if (menuId.equals(operateList.get(j).getMenuId())) {
					menuOperateMap.put("menuName", operateList.get(j).getMenuName());
					opList.add(operateList.get(j));
				}
			}
			menuOperateMap.put("operateList", opList);
			mapList.add(menuOperateMap);
		}

		return mapList;

	}

	public List<Operate> searchOperate(String operateName, String menuId, Integer pageNum, Integer pageSize) {
		if (pageNum < 1) {
			pageNum = 1;
		}
		return sysOperateDao.searchOperate(operateName, menuId, (pageNum - 1) * pageSize, pageSize);
	}

	public Integer searchOperateCount(String operateName, String menuId) {
		return sysOperateDao.searchOperateCount(operateName, menuId);
	}


	public List<Map<String,Object>> selectOperateByOperateCode( String operateCode){
		return sysOperateDao.selectOperateByOperateCode(operateCode);
	}




}
