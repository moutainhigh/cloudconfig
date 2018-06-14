package com.xkd.mapper;

import com.xkd.model.UserAttendMeeting;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserAttendMeetingMapper {

	UserAttendMeeting selectUserAttendMeetingByCompanyId(@Param("companyId") String companyId);

	Integer getTotoalByMeetingId(@Param("meetingId") String meetingId, @Param("content") String content,
                                 @Param("companyAdviserStr") String companyAdviserStr, @Param("companyDirectorStr") String companyDirectorStr,
                                 @Param("mgroupStr") String mgroupStr, @Param("userTypeStr") String userTypeStr);

	Integer getAttendedByMeetingId(@Param("meetingId") String meetingId, @Param("content") String content);

	List<UserAttendMeeting> selectUserInfoByMeetingId(@Param("meetingId") String meetingId,
                                                      @Param("notGroupStr") String notGroupStr, @Param("currentPageInt") int currentPageInt, @Param("pageSizeInt") int pageSizeInt);

	Integer updateMeetingUserContentById(UserAttendMeeting userAttendMeeting);

	List<UserAttendMeeting> selectMeetingUserByParam(@Param("meetingId") String meetingId,
                                                     @Param("companyIds") String companyIds, @Param("currentPageInt") int currentPageInt, @Param("pageSizeInt") int pageSizeInt);

	Integer saveUserMeeting(UserAttendMeeting userAttendMeeting);

	List<UserAttendMeeting> selectMeetingUserById(@Param("meetingId") String meetingId);

	Integer getNotGroupCount(@Param("meetingId") String meetingId, @Param("notGroupStr") String notGroupStr);

	Integer getTotoalByMeetingIdAttend(@Param("meetingId") String meetingId, @Param("content") String content,
                                       @Param("companyAdviserStr") String companyAdviserStr, @Param("companyDirectorStr") String companyDirectorStr,
                                       @Param("mgroupStr") String mgroupStr, @Param("userTypeStr") String userTypeStr);

	List<HashMap<String, Object>> selectUserInfoMapByMeetingId(Map<String, Object> paramMap);


	Integer updateUserStarsById(@Param("id") String id, @Param("star") String star);

	Integer deleteMeetingUserByIds(@Param("idString") String idString);

	List<Map<String, Object>> selectExcluedCompanyUserMapsByMeetingId(Map<String, Object> paramMap);

	Integer selectTotalExcluedCompanyUserMapsByMeetingId(Map<String, Object> paramMap);

	List<Map<String, Object>> selectUsersHotelByMeetingId(@Param("userId") String userId, @Param("meetingId") String meetingId, @Param("content") String content, @Param("currentPageInt") int currentPageInt, @Param("pageSizeInt") int pageSizeInt);

	Integer selectTotalUsersHotelByMeetingId(@Param("meetingId") String meetingId, @Param("content") String content);


	List<Map<String, Object>> selectMeetingProjectsMapsByMeetingId(Map<String, Object> paramMap);


	Integer selectTotalMeetingProjectsMapsByMeetingId(Map<String, Object> paramMap);

	List<Map<String, Object>> selectMeetingUserMapsByCompanyId(@Param("companyId") String companyId, @Param("meetingId") String meetingId);

	List<UserAttendMeeting> selectMeetingUserByUserIdAndMeetingId(@Param("meetingId") String meetingId, @Param("userId") String userId);

	Integer updateMeetingUserStatusById(@Param("id") String id, @Param("ustatus") String ustatus, @Param("status") String status);

	Integer updateMeetingUserStatus(@Param("id") String id, @Param("status") String status);

	List<UserAttendMeeting> selectMeetingUserByIds(@Param("ids") String ids);

	Integer deleteMeetingUserAndStatus(@Param("id") String id, @Param("status") String status);

	Integer deleteMeetingUserByUserId(@Param("userId") String userId);

	Integer deleteMeetingUserByUserIdAndCompanyId(@Param("userId") String userId, @Param("companyId") String companyId);

	Integer getTotoalByMeetingIdNeedCheck(@Param("meetingId") String meetingId, @Param("content") String content,
                                          @Param("companyAdviserStr") String companyAdviserStr, @Param("companyDirectorStr") String companyDirectorStr,
                                          @Param("mgroupStr") String mgroupStr, @Param("userTypeStr") String userTypeStr);

	Integer updateMeetingUserNeedByIdByInvitation(Map<String, Object> umap);

	List<HashMap<String, Object>> selectAdviserDirectorMaps(@Param("meetingId") String meetingId);

	Integer deleteMeetingUserByUserIds(@Param("userIdList") List<String> userIdList);

    List<Map<String,Object>> selectMeetingUsersByIds(@Param("ids") String ids);

	List<HashMap<String,Object>> selectProjectAdvisersMeetingId(@Param("meetingId") String meetingId);

	Integer updateUserMeeting(UserAttendMeeting userAttendMeeting);
}
