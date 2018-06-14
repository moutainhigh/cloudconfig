package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Company;
import org.springframework.stereotype.Repository;

public interface CompanyMapper {

	List<Company> selectCompanyByName(@Param("companyName") String companyName,@Param("pcCompanyId") String pcCompanyId);
	List<String> selectCompanyNamesList();

 
	int updateCompanyInfoById(Map<String, Object> company);
	
	int updateCompanyDetailInfoById(Map<String, Object> companyDetail);

 
	int deleteCompanyById(@Param("ids") String ids);

 
	Company selectCompanyInfoById(@Param("companyId") String companyId);

	Map<String,Object> selectCompanyById(@Param("companyId")String companyId);

	List<Map<String,Object>> selectCompanyInfoByIdList(@Param("idList") List<String> idList);



  List<Map<String,Object>>  searchCompanyByName(@Param("companyName")String companyName,@Param("departmentIdList")List<String> departmentIdList,@Param("start")Integer start,@Param("pageSize")Integer pageSize);



	Integer insertCompanyInfo(Map<String, Object> company);
	
	Integer insertCompanyDetailInfo(Map<String, Object> companyDetail);
	
	void deleteByCompanyById(@Param("id") String id);

	List<String> selecAllCompanyId();

 
    List<String> selectSolrCompanyIdsByDepartmentIdsAndOperate (@Param("departmentIdList") List<String> departmentIdList,@Param("operate") Integer operate,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
	int deleteSolrCompanyIds (@Param("idList")List<String> idList,@Param("operate") Integer operate);



	public int insertDcSolrCompany(@Param("departmentIdList")List<String> departmentIdList,@Param("operate") Integer operate);







	Integer updateCompanyLabelById(@Param("companyId") String companyId, @Param("label") String label);
	
	

	//修改企业文件夹名称
	void updatePagerFileName(Map<String, Object> company);
	
	List<Company> selectAllStatusCompanyByName(@Param("companyName") String companyName,@Param("pcCompanyId") String pcCompanyId);



	List<String> selectAllCompanyIds();


	List<Map<String,Object>> selectCompanyByNameUnDeleted(@Param("companyName")String companyName,@Param("pcCompanyId")String pcCompanyId);


	public List<Map<String,Object>> searchCompany(@Param("departmentIdList")List<String> departmentIdList,@Param("searchValue")String searchValue,@Param("companyName")String companyName,@Param("mobile")String mobile,@Param("userLevelId")String userLvelId,
												  @Param("industryId")String industryId,@Param("province")String province,@Param("city")String city,@Param("county")String  county,@Param("companyAdviserId")String companyAdviserId,
												  @Param("channelId")String  channelId,@Param("companyDirectorId")String companyDirectorId,@Param("publicFlag")Integer publicFlag,@Param("loginUserId")String loginUserId,@Param("potentialStatus")Integer potentialStatus,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

	public Integer searchCompanyCount(@Param("departmentIdList")List<String> departmentIdList,@Param("searchValue")String searchValue,@Param("companyName")String companyName,@Param("mobile")String mobile,@Param("userLevelId")String userLvelId,
												  @Param("industryId")String industryId,@Param("province")String province,@Param("city")String city,@Param("county")String  county,@Param("companyAdviserId")String companyAdviserId,
												  @Param("channelId")String  channelId,@Param("companyDirectorId")String companyDirectorId,@Param("publicFlag")Integer publicFlag,@Param("loginUserId")String loginUserId,@Param("potentialStatus")Integer potentialStatus);

}
