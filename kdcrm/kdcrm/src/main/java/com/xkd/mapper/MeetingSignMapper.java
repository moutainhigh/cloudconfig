package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Company;

public interface MeetingSignMapper {

	List<HashMap<String, Object>> getCompanyList(@Param("id") String id, @Param("uid") String uid, @Param("province") String province, @Param("sonIndustryid") String sonIndustryid, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

	int getTotalRows(@Param("id") String id, @Param("province") String province, @Param("sonIndustryid") String sonIndustryid);

	HashMap<String, Object> getMyCompany(@Param("uid") String uid, @Param("companyId") String companyId, @Param("meetingId") String meetingId);

	

	HashMap<String, Object> getMeetingFollowById(@Param("myCid") String myCid, @Param("cid") String cid, @Param("mid") String mid);
	
	int insertMeetingFollow(HashMap<String, Object> obj);
	
	int delMeetingFollow(Object id);
	
	List<HashMap<String, Object>> getMyFollowCompany(@Param("uid") String uid, @Param("meetingId") String meetingid);
	
	List<HashMap<String, Object>> getMyCompanyPiPei(@Param("uid") String uid, @Param("meetingId") String meetingid);
	
	List<HashMap<String, Object>> getMyCompanyGroup(@Param("uid") String uid, @Param("meetingId") String meetingid);

 
	List<HashMap<String, Object>> meetingExercise(String meetingid);


	int updateCompany(HashMap<String, Object> company);
	
	int updateCompanyDetail(HashMap<String, Object> company);
	
	


	List<HashMap<String, Object>> getMeetingCompanyIndustry(String meetingId);

	List<HashMap<String, Object>> getMeetingCompanyCity(String meetingId);

	List<HashMap<String, Object>> getCompanyAdviser(Object companyId);

	List<HashMap<String, Object>> getAdviserList();

	//List<HashMap<String, Object>> getSignCompany(@Param("meetingId")String meetingId, @Param("begin_data")String begin_data, @Param("end_date")String end_date);

	List<HashMap<String, Object>> getUserFilterHangYe(@Param("meetingId") String meetingId,
													  @Param("hangye") List<String> hangye,
													  @Param("uid") String uid,
													  @Param("xuqiu") List<String> xuqiu,
													  @Param("provinceId") String provinceId);

	Company getCompanyByMeetingIdAndUid(@Param("meetingId") String meetingId, @Param("uid") String uid);
	

}
