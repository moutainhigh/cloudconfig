package com.xkd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.BankProjectMapper;

@Service
public class BankProjectService {

	@Autowired
	private BankProjectMapper bankProjectMapper;
	@Autowired
	UserService userService;
	@Autowired
	UserDataPermissionService userDataPermissionService;
	@Autowired
	CompanyRelativeUserService companyRelativeUserService;
	
	public Map<String,Object> selectBankProjectsByContent(Map<String, Object> paramMap,String loginUserId) {



		Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<String>  list=null;
		if ("1".equals(loginUserMap.get("roleId"))){
			list = userDataPermissionService.getDataPermissionDepartmentIdList("1",loginUserId);
		}else{
			list = userDataPermissionService.getDataPermissionDepartmentIdList((String)loginUserMap.get("pcCompanyId"),loginUserId);
		}
		paramMap.put("departmentIdList",list);

		List<Map<String,Object>> listMap =  bankProjectMapper.selectBankProjectsByContent(paramMap);
		Integer total = bankProjectMapper.selectTotalBankProjectsByContent(paramMap);


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
		}


		Map<String,Object> resultMap=new HashMap<>() ;
		resultMap.put("list",listMap);
		resultMap.put("total",total);
		return resultMap;
	}

	public int insertSelective(Map<String, Object> map) {
		
		return bankProjectMapper.insertSelective(map);
		
	}

	public int updateByIdSelective(Map<String, Object> map) {
		
		return bankProjectMapper.updateByIdSelective(map);
		
	}

	public int deleteBankProjectByIds(String ids) {

		return bankProjectMapper.deleteBankProjectByIds(ids);
		
	}

	public List<Map<String, Object>> selectBankProjectByCodeAllStatus(String projeCode) {
		
		return bankProjectMapper.selectBankProjectByCodeAllStatus(projeCode);
		
	}




	public List<Map<String,Object>>  selectBankProjectByName(@Param("projectName") String projectName,List<String> departmentIdList){
		return bankProjectMapper.selectBankProjectByName(projectName,departmentIdList);
	}


	public Map<String, Object> selectBankProjectById(String id) {
		
		Map<String ,Object> map= bankProjectMapper.selectBankProjectById(id);
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

	public int selectTotalBankProjectsByContent(Map<String, Object> paramMap) {
		
		return bankProjectMapper.selectTotalBankProjectsByContent(paramMap);
	}

	public Integer deleteBankProjectRealByName(String projectName) {
		
		return bankProjectMapper.deleteBankProjectRealByName(projectName);
	}


    public List<Map<String,Object>> selectPointsByProjectId(String projectId) {
		return bankProjectMapper.selectPointsByProjectId(projectId);
    }

	public List<Map<String,Object>> selectExcludePointsByProjectId(  String companyId,String projectId, String content,List<String> departmentIdList, int pageSizeInt, int currentPageInt) {
		return bankProjectMapper.selectExcludePointsByProjectId(companyId,projectId,content,  departmentIdList,pageSizeInt,currentPageInt);
	}

	public Integer saveProjectPoints(String projectId, List<String> idList) {
 		for (int i = 0; i < idList.size(); i++) {
			Map<String,Object> map= bankProjectMapper.selectProjectPointByProjectIdAndPointId(idList.get(i), projectId);
			if (map!=null){  //如果库中已经存在了这条中间记录，则直接改成已关联
				bankProjectMapper.updateDcBankProjectPointRevokeStatus((String)map.get("id"),"已关联");
			}else{
				List<String>  list=new ArrayList<>();
				list.add(idList.get(i));
				bankProjectMapper.saveProjectPoints(projectId,list);
			}
		}


		return idList.size();
	}



	public  Map<String,Object> selectBankProjectPointById(  String id){
		return bankProjectMapper.selectBankProjectPointById(id);
	}


	public Integer selectExcludePointsTotalByProjectId( String companyId,String projectId, String content,List<String> departmentIdList) {
		return bankProjectMapper.selectExcludePointsTotalByProjectId(companyId,projectId,content,departmentIdList);
	}



	public List<Map<String,Object>> selectBankProjectList( String pointId, String projectName, List<String> revokeStatusList, Integer currentPage, Integer pageSize ){
		if (null==currentPage){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		Integer start=(currentPage-1)*pageSize;
		return bankProjectMapper.selectBankProjectList(pointId,projectName,revokeStatusList,start,pageSize);
	}

	public int selectBankProjectCount( String pointId, String projectName, List<String> revokeStatusList){
		return bankProjectMapper.selectBankProjectListCount(pointId, projectName, revokeStatusList);
	}


	public int updateDcBankProjectPointRevokeStatus( String id, String revokeStatus){
		return  bankProjectMapper.updateDcBankProjectPointRevokeStatus(id,revokeStatus);
	}





	public List<Map<String,Object>> selectQuestionaire(String bankProjectId,String title, List<String> ttypeList , Integer currentPage, Integer pageSize ){

		if (null==currentPage){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		Integer start=(currentPage-1)*pageSize;


		return  bankProjectMapper.selectQuestionaire(bankProjectId,title,ttypeList,start,pageSize);
	}

	public int selectQuestionaireCount(String bankProjectId,String title,  List<String> ttypeList ){
		return  bankProjectMapper.selectQuestionaireCount(bankProjectId,title,ttypeList);
	}

	public List<Map<String,Object>>  selectQuestionaireByProjectId( String bankProjectId,Integer currentPage,Integer pageSize){
		if (null==currentPage){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=1000;
		}
		Integer start=(currentPage-1)*pageSize;

		return  bankProjectMapper.selectQuestionaireByProjectId(bankProjectId,start,pageSize);
	}

	public int  selectQuestionaireCountByProjectId(String bankProjectId){
		return bankProjectMapper.selectQuestionaireCountByProjectId(bankProjectId);
	}



	public int insertBankProjectExerciseList( List<Map<String,Object>> list   ){
		return  bankProjectMapper.insertBankProjectExerciseList(list);
	}

	public int deleteBankProjectExerciseList( List<String> idList   ){
		return bankProjectMapper.deleteBankProjectExerciseList(idList);
	}


	public List<Map<String,Object>> selectWjExamByBankPointId( String pointId,String bankProjectId,String title,List<String> ttypeList,String startDate,String endDate,String uname,Integer currentPage,Integer pageSize){
		if (null==currentPage){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		Integer start=(currentPage-1)*pageSize;


		return  bankProjectMapper.selectWjExamByBankPointId(pointId,bankProjectId,title,ttypeList,startDate,endDate,uname,start,pageSize);
	}


	public  int selectWjExamCountByBankPointId( String pointId,String bankProjectId,String title,List<String> ttypeList,String startDate,String endDate,String uname){
		return  bankProjectMapper.selectWjExamCountByBankPointId(pointId,bankProjectId, title, ttypeList, startDate,endDate, uname);
	}


	public int insertBankProjectWjExcerciseRecord(Map map){
		return  bankProjectMapper.insertBankProjectWjExcerciseRecord(map);
	}

	public List<String> selectBankProjectWjByUserIdProjectIdPointIdExerciseId( String bankProjectId, String exerciseId, String pointId,String userId){
		return bankProjectMapper.selectBankProjectWjByUserIdProjectIdPointIdExerciseId(bankProjectId,exerciseId,pointId,userId);
	}

	public List<Map<String ,Object>>  selectBankPointByProjectId( String bankProjectId){
		return bankProjectMapper.selectBankPointByProjectId(bankProjectId);
	}

	public List<Map<String,Object>> selectBankPosition(){
		return bankProjectMapper.selectBankPosition();
	}


	public  Map<String,Object> selectBankPointStatistic(@Param("pointId")String pointId){
		return bankProjectMapper.selectBankPointStatistic(pointId);
	}


	public Map<String,Object> selectBankProjectRelation(@Param("id")String  id){
		return  bankProjectMapper.selectBankProjectRelation(id);
	}


}
