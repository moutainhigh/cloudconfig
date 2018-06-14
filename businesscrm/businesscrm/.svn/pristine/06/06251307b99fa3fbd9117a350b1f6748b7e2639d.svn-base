package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.BankProject;

public interface BankProjectMapper {
    int deleteByPrimaryKey(@Param("id") String id);

    int insert(BankProject record);

    int insertSelective(Map<String, Object> map);

    int updateByIdSelective(Map<String, Object> map);

    int updateById(BankProject record);

	List<Map<String, Object>> selectBankProjectsByContent(Map<String, Object> paramMap);

	int deleteBankProjectByIds(@Param("ids") String ids);

	List<Map<String, Object>> selectBankProjectByCodeAllStatus(@Param("projectCode") String projectCode);


	List<Map<String,Object>>  selectBankProjectByName(@Param("projectName") String projectName,@Param("departmentIdList")List<String> departmentIdList);

	Map<String, Object> selectBankProjectById(@Param("id") String id);

	int selectTotalBankProjectsByContent(Map<String, Object> paramMap);

	Integer deleteBankProjectRealByName(String projectName);

    List<Map<String,Object>> selectPointsByProjectId(@Param("projectId")String projectId);

	List<Map<String,Object>> selectExcludePointsByProjectId(@Param("companyId") String companyId,@Param("projectId") String projectId, @Param("content") String content,@Param("departmentIdList")List<String> departmentIdList,
															@Param("pageSizeInt")int pageSizeInt, @Param("currentPageInt")int currentPageInt);

	Integer saveProjectPoints(@Param("projectId")String projectId, @Param("pointIds")List<String> pointIds);

	Integer selectExcludePointsTotalByProjectId(@Param("companyId") String companyId,@Param("projectId") String projectId,  @Param("content") String content,@Param("departmentIdList")List<String> departmentIdList);

	List<Map<String,Object>> selectQuestionaire(@Param("bankProjectId") String bankProjectId ,@Param("title") String title,@Param("ttypeList") List<String> ttypeList, @Param("start") Integer start,@Param("pageSize") Integer pageSize );
    int selectQuestionaireCount(@Param("bankProjectId") String bankProjectId ,@Param("title") String title,@Param("ttypeList") List<String> ttypeList);





	 List<Map<String,Object>>  selectQuestionaireByProjectId(@Param("bankProjectId") String bankProjectId,@Param("start") Integer start,@Param("pageSize") Integer pageSize);



	int  selectQuestionaireCountByProjectId(@Param("bankProjectId") String bankProjectId);




	int insertBankProjectExerciseList(@Param("list") List<Map<String,Object>> list   );
	int deleteBankProjectExerciseList(@Param("ids") List<String> list   );




	List<Map<String,Object>> selectWjExamByBankPointId(@Param("pointId") String pointId,@Param("bankProjectId") String bankProjectId,@Param("title") String title,@Param("ttypeList") List<String> ttypeList,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("uname") String uname,@Param("start")Integer start,@Param("pageSize") Integer pageSize);

    int selectWjExamCountByBankPointId(@Param("pointId") String pointId,@Param("bankProjectId") String bankProjectId,@Param("title") String title,@Param("ttypeList") List<String> ttypeList,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("uname") String uname);





	List<Map<String ,Object>> selectBankProjectList(@Param("pointId") String pointId,@Param("projectName") String projectName,@Param("revokeStatusList") List<String> revokeStatusList,@Param("start") Integer start,@Param("pageSize") Integer pageSize );


    Integer selectBankProjectListCount(@Param("pointId") String pointId,@Param("projectName") String projectName,@Param("revokeStatusList") List<String> revokeStatusList);

    int updateDcBankProjectPointRevokeStatus(@Param("id") String id,@Param("revokeStatus") String revokeStatus);

     Map<String,Object> selectBankProjectPointById(@Param("id") String id);


	int insertBankProjectWjExcerciseRecord(Map map);

	public List<String> selectBankProjectWjByUserIdProjectIdPointIdExerciseId(@Param("bankProjectId") String bankProjectId,@Param("exerciseId") String exerciseId,@Param("pointId") String pointId,@Param("userId")String userId);



	public List<Map<String,Object>>  selectBankPointByProjectId(@Param("bankProjectId") String bankProjectId);


	public Map<String,Object>  selectProjectPointByProjectIdAndPointId(@Param("pointId") String pointId,@Param("bankProjectId") String bankProjectId );

	public List<Map<String ,Object>> selectBankPosition();

   public  Map<String,Object> selectBankPointStatistic(@Param("pointId")String pointId);



	public Map<String,Object> selectBankProjectRelation(@Param("id")String  id);
}