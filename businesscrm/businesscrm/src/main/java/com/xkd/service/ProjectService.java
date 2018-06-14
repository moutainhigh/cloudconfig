package com.xkd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xkd.model.RedisTableKey;
import com.xkd.utils.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.ProjectMapper;

@Service
public class ProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	UserService userService;
	@Autowired
	UserDataPermissionService userDataPermissionService;
	@Autowired
	CompanyRelativeUserService companyRelativeUserService;

	@Autowired
	RedisCacheUtil redisCacheUtil;
	
	public Map<String,Object> selectProjectsByContent(Map<String, Object> paramMap,String loginUserId) {



		Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<String>  list=null;
		if ("1".equals(loginUserMap.get("roleId"))){
			list = userDataPermissionService.getDataPermissionDepartmentIdList("1",loginUserId);
		}else{
			list = userDataPermissionService.getDataPermissionDepartmentIdList((String)loginUserMap.get("pcCompanyId"),loginUserId);
		}
		paramMap.put("departmentIdList",list);

		List<Map<String,Object>> listMap =  projectMapper.selectProjectsByContent(paramMap);
		Integer total = projectMapper.selectTotalProjectsByContent(paramMap);


		//查询相关人员
		List<String> companyIds = new ArrayList<>();
		for (int i = 0; i < listMap.size(); i++) {
			Map<String, Object> data = listMap.get(i);
			String comId=(String) data.get("companyId");
			if (StringUtils.isNotBlank(comId)){
				if (!companyIds.contains(comId)) {
					companyIds.add(comId);
				}
			}
		}

		List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);

		Map<String,Object> localMap=new HashMap<>();

		//整理数据，将相关人员整合到相应的记录中去
		for (int i = 0; i < listMap.size(); i++) {
			Map<String, Object> data = listMap.get(i);
			List<Map<String, Object>> relUserList = new ArrayList<>();
			data.put("relativeUserList", relUserList);
			for (int j = 0; j < relativeList.size(); j++) {
				if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
					relUserList.add(relativeList.get(j));
				}
			}


			/**
			 * 设置数据字典值，之所以在这里设置，是为了直接从Redis中取值,避免查询时与数据字典表关联过多引起性能问题
			 */

			String projectTypeId_= (String) data.get("projectTypeId");
			String departmentTypeId_= (String) data.get("departmentTypeId");
			String serveObjectTypeId_= (String) data.get("serveObjectTypeId");
			String customerTypeId_= (String) data.get("customerTypeId");
			if (localMap.get(projectTypeId_)==null){
				String projectType=projectTypeId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, projectTypeId_);
				if (projectTypeId_!=null){
					localMap.put(projectTypeId_,projectType);
				}
			}

			if (localMap.get(departmentTypeId_)==null){
				String departmentType=departmentTypeId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, departmentTypeId_);
				if (departmentTypeId_!=null){
					localMap.put(departmentTypeId_,departmentType);
				}
			}

			if (localMap.get(serveObjectTypeId_)==null){
				String serveObjectType=serveObjectTypeId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, serveObjectTypeId_);
				if (serveObjectTypeId_!=null){
					localMap.put(serveObjectTypeId_,serveObjectType);
				}
			}
			if (localMap.get(customerTypeId_)==null){
				String customerType=customerTypeId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, customerTypeId_);
				if (customerTypeId_!=null){
					localMap.put(customerTypeId_,customerType);
				}
			}

			if (localMap.get((String) data.get("projectTypeId"))!=null) {
				data.put("projectType", localMap.get((String) data.get("projectTypeId")));
			}

			if (localMap.get((String) data.get("departmentTypeId"))!=null) {
				data.put("departmentType", localMap.get((String) data.get("departmentTypeId")));
			}

			if (localMap.get((String) data.get("serveObjectTypeId"))!=null) {
				data.put("serveObjectType", localMap.get((String) data.get("serveObjectTypeId")));
			}

			if (localMap.get((String) data.get("customerTypeId"))!=null) {
				data.put("customerType", localMap.get((String) data.get("customerTypeId")));
			}

		}




		Map<String,Object> resultMap=new HashMap<>() ;
		resultMap.put("list",listMap);
		resultMap.put("total",total);
		return resultMap;
	}



	public int deleteProjectByIdList(List<String>  idList) {
		if (idList.size()==0){
			return 0;
		}
		return projectMapper.deleteProjectByIdList(idList);
		
	}


	public Map<String, Object> selectProjectById(String id) {
		
		Map<String ,Object> map=projectMapper.selectProjectById(id);
		//查询相关人员
		List<String> companyIds = new ArrayList<>();
		{
			Map<String, Object> data = map;
			String comId = (String) data.get("companyId");
			if (StringUtils.isNotBlank(comId)) {
				if (!companyIds.contains(comId)) {
					companyIds.add(comId);
				}
			}
		}

		List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);

		{
			//整理数据，将相关人员整合到相应的记录中去
			Map<String, Object> data = map;
			List<Map<String, Object>> relUserList = new ArrayList<>();
			data.put("relativeUserList", relUserList);
			for (int j = 0; j < relativeList.size(); j++) {
				if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
					relUserList.add(relativeList.get(j));
				}
			}

		}

	return map;
	}





	public int insertProject(Map<String,Object> map){
		return projectMapper.insertProject(map);
	}



	public Map<String,Object> selectProjectUnDeleted(String projectCode){
		return projectMapper.selectProjectUnDeleted(projectCode);
	}


	public List<Map<String,Object>> selectProjectByIds(List<String> idList){
		if (idList.size()==0){
			return new ArrayList<>(0) ;
		}
		return projectMapper.selectProjectByIds(idList);
	}



	public int updateProject(Map<String,Object> map){
		return projectMapper.updateProject(map);
	}





}
