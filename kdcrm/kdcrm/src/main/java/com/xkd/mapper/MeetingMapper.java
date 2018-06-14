package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Meeting;

public interface MeetingMapper {
	
	List<Meeting> selectMeetingByParam(@Param("startTime") String startTime, @Param("endTime") String endTime,
									   @Param("meetingType") String meetingType, @Param("meetingStatus") String meetingStatus, @Param("meetingName") String meetingName, @Param("address") String address, @Param("teacherIds") String teacherIds,
									   @Param("mname") String mname, @Param("currentPageInt") int currentPageInt,
									   @Param("pageSizeInt") int pageSizeInt,@Param("departmentIdList")List<String> departmentIdList,@Param("pcCompanyId") String pcCompanyId);

	Integer updateMeetingById(Meeting meeting);

	Integer saveMeeting(Meeting meeting);

	Meeting selectMeetingById(@Param("meetingId") String meetingId);

	Integer getMeetingCountByParam(@Param("meetingType") String meetingType, @Param("meetingStatus") String meetingStatus, @Param("meetingName") String meetingName,
								   @Param("address") String address, @Param("teacherIds") String teacherIds, @Param("mname") String mname, @Param("startTime") String startTime, @Param("endTime") String endTime,
								   @Param("departmentIdList")List<String> departmentIdList,@Param("pcCompanyId") String pcCompanyId);

	Integer deleteMeetingById(@Param("ids") String ids);

	List<Map<String, Object>> selectCompanysLogo(@Param("meetingId") String meetingId);
			
	
	List<Map<String,Object>> selectMeetingByCompanyId(@Param("companyId") String companyId);
	
	
	List<Map<String,Object>> selectUserExcerciseByCompanyId(@Param("companyId") String companyId);
	
	void deleteMeetingUserByCompanyIds(@Param("companyIds") String companyIds);

	Integer saveUserSpread(Map<String, Object> map);

	List<Map<String,Object>> selectUserSpreadsByMeetingId(@Param("meetingId") String meetingId);

	Integer deleteUserSpreadsByIds(@Param("idList") List<String> idList);

	List<Map<String,Object>> selectDetailByspreadId(@Param("userSpreadId")String userSpreadId);

	Integer selectCountDetailByspreadId(@Param("userSpreadId")String userSpreadId);

	Integer selectTotalUserSpreadsByMeetingId(@Param("meetingId")String meetingId);

	Map<String,Object> selectUserSpreadByMeetingIdUserId(@Param("meetingId")String meetingId, @Param("userId")String userId);
}
