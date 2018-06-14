package com.xkd.service;

import com.ctc.wstx.util.StringUtil;
import com.xkd.mapper.CompanyMapper;
import com.xkd.model.Company;
import com.xkd.model.RedisTableKey;
import com.xkd.utils.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {



	@Autowired
	private CompanyMapper companyMapper;



	@Autowired
	SolrService solrService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	RedisCacheUtil redisCacheUtil;
	

	@Autowired
	private CompanyRelativeUserService companyRelativeUserService;
	
	public List<Company> selectCompanyByName(String companyName,String pcCompanyId){
		
		
		List<Company> companys = companyMapper.selectCompanyByName(companyName,pcCompanyId);

		return companys;
	}
	
	

	


 

	public int updateCompanyInfoById(Map<String,Object> company) {
		
		int num = companyMapper.updateCompanyInfoById(company);
		if(null != company && null!= company.get("companyName")&&!"".equals(company.get("companyName"))){
			companyMapper.updatePagerFileName(company);
		}

		return num;
	}
	
	
	
	public int updateCompanyDetailInfoById(Map<String,Object> company) {
		
		int num = companyMapper.updateCompanyDetailInfoById(company);
		
		return num;
	}

 

	public int deleteCompanyById(String ids) {
		
		int num = companyMapper.deleteCompanyById(ids);
		
		return num;
	}

 

	public Company selectCompanyInfoById(String companyId) {
		
		Company company = companyMapper.selectCompanyInfoById(companyId);
		
		return company;
	}


	public Map<String,Object> selectCompanyById(String id){
		return companyMapper.selectCompanyById(id);
	}
 



	public List<Map<String,Object>>  searchCompanyByName(String companyName,List<String> departmentIdList,Integer start,Integer pageSize){
		return companyMapper.searchCompanyByName(companyName,departmentIdList,start,pageSize);
	}





	public Integer insertCompanyInfo(Map<String,Object> company) {
		Integer num = companyMapper.insertCompanyInfo(company);
		
		return num;
	}
	
	
	public Integer insertCompanyDetailInfo(Map<String,Object> company) {
		
		Integer num = companyMapper.insertCompanyDetailInfo(company);
		
		return num;
	}










	public Integer updateCompanyLabelById(String companyId, String label) {
		
	Integer num = companyMapper.updateCompanyLabelById(companyId,label);
	
	return num;
}




	
	






	public 	void deleteByCompanyById(String id){
		companyMapper.deleteByCompanyById(id);
	}
	public List<String> selecAllCompanyId(){
		return companyMapper.selecAllCompanyId();
	}


	public boolean isRelativePermission(String companyId,String userId){

		boolean relativeFlag=false;
		List<String> companyIdList=new ArrayList<>();
		companyIdList.add(companyId);
		List<Map<String,Object>> relativeUserList=companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
		for (int i = 0; i <relativeUserList.size() ; i++) { //判断相关人员权限
			if (userId!=null&&userId.equals(relativeUserList.get(i).get("userId"))){
				relativeFlag=true;
			}
		}
		return  relativeFlag;
	}



	public void updateInfoScore(String companyId){
		Company company=companyMapper.selectCompanyInfoById(companyId);
		List<Map<String,Object>> userInfos=   userInfoService.selectUserInfoByCompanyId(companyId);
		Integer v_establishTime=StringUtils.isBlank(company.getEstablishTime())?0:1;
		Integer v_registerMoney=StringUtils.isBlank(company.getRegisteredMoney())?0:1;
		Integer v_econKind=StringUtils.isBlank(company.getEconKind())?0:1;
		Integer v_socialCredit=StringUtils.isBlank(company.getSocialCredit())?0:1;
		Integer v_representative=StringUtils.isBlank(company.getRepresentative())?0:1;
		Integer v_address=StringUtils.isBlank(company.getAddress())?0:1;
		Integer v_manageScope=StringUtils.isBlank(company.getManageScope())?0:1;
		Integer v_label=StringUtils.isBlank(company.getLabel())?0:1;
		Integer v_businessScope=StringUtils.isBlank(company.getBusinessScope())?0:1;
		Integer v_companySize=StringUtils.isBlank(company.getCompanySize())?0:1;
 		Integer v_userInfo=userInfos.size()==0?0:1;
		Integer v_userLevelId=StringUtils.isBlank(company.getUserLevelId())?0:1;
		Integer v_channelId=StringUtils.isBlank(company.getChannelId())?0:1;
		Integer v_priorityId=StringUtils.isBlank(company.getPriorityId())?0:1;
		Integer v_companyPhaseId= StringUtils.isBlank(company.getCompanyPhaseId())?0:1;
		Integer v_companyAdviserId=StringUtils.isBlank(company.getCompanyAdviserId())?0:1;
		Integer v_companyDirectorId=StringUtils.isBlank(company.getCompanyDirectorId())?0:1;
		Integer v_industryId=StringUtils.isBlank(company.getIndustryId())?0:1;
		Integer v_website=StringUtils.isBlank(company.getWebsite())?0:1;
		Integer v_province=StringUtils.isBlank(company.getProvince())?0:1;



		Integer score=v_establishTime*5+v_registerMoney*5
				+v_econKind*5+v_socialCredit*5
				+v_representative*5+v_address*5
				+v_manageScope*5
				+v_label*5+v_businessScope*5
				+v_companySize*5+v_companyAdviserId*5
				+v_userLevelId*5+v_channelId*5
				+v_priorityId*5+v_companyDirectorId*5
				+v_companyPhaseId*5
				+v_industryId*5+v_website*5
				+v_province+5
 				+v_userInfo*5;

		 Map<String,Object> companyMap=new HashMap<>();
		companyMap.put("id",companyId);
		companyMap.put("infoScore",score);
		companyMapper.updateCompanyInfoById(companyMap);

	}


	public List<Map<String,Object>> selectCompanyInfoByIdList(@Param("idList")List<String> idList){
		if (idList.size()==0){
			return new ArrayList<>()
					;
		}
		return companyMapper.selectCompanyInfoByIdList(idList);
	}



	public List<String> selectSolrCompanyIdsByDepartmentIdsAndOperate(List<String> departmentIdList,Integer operate, Integer start,Integer pageSize){
		return companyMapper.selectSolrCompanyIdsByDepartmentIdsAndOperate(departmentIdList,operate, start, pageSize);
	}

	public int deleteSolrCompanyIds(List<String> idList,Integer Operate){
		return companyMapper.deleteSolrCompanyIds(idList,Operate);
	}

	public int insertDcSolrCompany( List<String> departmentIdList,Integer operate){
		return companyMapper.insertDcSolrCompany(departmentIdList,operate);
	}






	public List<String> selectAllCompanyIds(){
		return companyMapper.selectAllCompanyIds();
	}

	public List<Map<String,Object>> selectCompanyByNameUnDeleted(String companyName,String pcCompanyId){
		return companyMapper.selectCompanyByNameUnDeleted(companyName,pcCompanyId);
	}

	public List<Map<String,Object>> searchCompany(List<String> departmentIdList,String searchValue,String companyName,String mobile,String userLevelId,
												  String industryId,String province,String city,String  county,String companyAdviserId,
												  String  channelId,String companyDirectorId,Integer publicFlag,String loginUserId,Integer potentialStatus,Integer currentPage,Integer pageSize){
		if (currentPage==null){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		int start=(currentPage-1)*pageSize;
		List<Map<String,Object>> list= companyMapper.searchCompany(departmentIdList,searchValue,companyName,mobile,userLevelId,industryId,province,city,county,companyAdviserId,channelId,companyDirectorId,publicFlag,loginUserId,potentialStatus,start,pageSize);


		//查询相关人员
		List<String> companyIds = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> data = list.get(i);
			String comId=(String) data.get("id");
			if (StringUtils.isNotBlank(comId)){
				if (!companyIds.contains(comId)) {
					companyIds.add(comId);
				}
			}
		}

		List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);

		Map<String,Object> localMap=new HashMap<>();

		for (int i = 0; i <list.size() ; i++) {
			/*
			整理数据，将相关人员整合到相应的记录中去
			 */
			Map<String, Object> data = list.get(i);
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
			String userLevelId_= (String) data.get("userLevelId");
			String userTypeId_= (String) data.get("userTypeId");
			String industryId_= (String) data.get("industryId");
			String channelId_= (String) data.get("channelId");
			String priorityId_= (String) data.get("priorityId");
			String companyPhaseId_= (String) data.get("companyPhaseId");
			if (localMap.get(userLevelId_)==null){
				 String userLevel=userLevelId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, userLevelId_);
				if (userLevelId_!=null){
					localMap.put(userLevelId_,userLevel);
				}
			}
			if (localMap.get(userTypeId_)==null){
				String userType=userTypeId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, userTypeId_);
				if (userTypeId_!=null){
					localMap.put(userTypeId_,userType);
				}
			}
			if (localMap.get(industryId_)==null){
				String industry= industryId_==null?null:(String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, industryId_);
				if (industryId_!=null){
					localMap.put(industryId_,industry);
				}
			}
			if (localMap.get(channelId_)==null){
				String channel= channelId_==null?null:(String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, channelId_);
				if (channelId_!=null){
					localMap.put(channelId_,channel);
				}
			}
			if (localMap.get(priorityId_)==null){
				String priority=priorityId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, priorityId_);
				if (priorityId_!=null){
					localMap.put(priorityId_,priority);
				}
			}
			if (localMap.get(companyPhaseId_)==null){
				String companyPhase=companyPhaseId_==null?null: (String) redisCacheUtil.getCacheMapVaue(RedisTableKey.DICTIONARY, companyPhaseId_);
				if (companyPhaseId_!=null){
					localMap.put(companyPhaseId_,companyPhase);
				}
			}
			if (localMap.get((String) data.get("userLevelId"))!=null) {
				data.put("userLevel", localMap.get((String) data.get("userLevelId")));
			}
			if (localMap.get((String) data.get("userTypeId"))!=null) {
				data.put("userType", localMap.get((String) data.get("userTypeId")));
			}
			if (localMap.get((String) data.get("industryId"))!=null) {
				data.put("industry", localMap.get((String) data.get("industryId")));
			}
			if (localMap.get((String) data.get("channelId"))!=null) {
				data.put("channel", localMap.get((String) data.get("channelId")));
			}
			if (localMap.get((String) data.get("priorityId"))!=null) {
				data.put("priority", localMap.get((String) data.get("priorityId")));
			}
			if (localMap.get((String) data.get("companyPhaseId"))!=null) {
				data.put("companyPhase", localMap.get((String) data.get("companyPhaseId")));
			}
 		}
		return list;
	}

	public Integer searchCompanyCount(List<String> departmentIdList,String searchValue,String companyName,String mobile,String userLevelId,
									  String industryId,String province,String city,String  county,String companyAdviserId,
									  String  channelId,String companyDirectorId,Integer publicFlag,String loginUserId,Integer potentialStatus){
		return companyMapper.searchCompanyCount(departmentIdList,searchValue,companyName,mobile,userLevelId,industryId,province,city,county,companyAdviserId,channelId,companyDirectorId,publicFlag,loginUserId,potentialStatus);
	}


}
