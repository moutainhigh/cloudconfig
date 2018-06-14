package com.xkd.mapper;

import com.xkd.model.Meeting;
import com.xkd.model.MeetingPo;
import com.xkd.utils.LinkTypeData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/20.
 */
public interface MeetingMapper {

    public int insertMeeting(MeetingPo meetingPo);

    public int updateMeeting(MeetingPo meetingPo);


    public List<Map<String,Object>> searchMeeting(Map<String,Object> map);

    public Integer searchMeetingCount(Map<String ,Object> map);

    public int deleteMeetingByIds(@Param("idList")List<String> idList);

    Integer updateMeetingFlag(@Param("id")String id,@Param("flag")int flag);

    List<Meeting> selectMeetingByParam(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                       @Param("meetingType") String meetingType, @Param("meetingStatus") String meetingStatus, @Param("meetingName") String meetingName, @Param("address") String address, @Param("teacherIds") String teacherIds,
                                       @Param("mname") String mname, @Param("currentPageInt") int currentPageInt,
                                       @Param("pageSizeInt") int pageSizeInt,@Param("departmentIdList")List<String> departmentIdList,
                                       @Param("pcCompanyId") String pcCompanyId,@Param("createdByName") String createdByName,
                                       @Param("meetingFlag") Integer meetingFlag);

    Integer updateMeetingById(Meeting meeting);

    Integer saveMeeting(Meeting meeting);

    Meeting selectMeetingById(@Param("meetingId") String meetingId);

    Integer getMeetingCountByParam(@Param("meetingType") String meetingType, @Param("meetingStatus") String meetingStatus, @Param("meetingName") String meetingName,
                                   @Param("address") String address, @Param("teacherIds") String teacherIds, @Param("mname") String mname, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                   @Param("departmentIdList")List<String> departmentIdList,
                                   @Param("pcCompanyId") String pcCompanyId,@Param("createdByName") String createdByName,
                                   @Param("meetingFlag") Integer meetingFlag);

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

    List<Map<String,Object>> selectMeetingTicketStatistics();

    Integer selectMeetingTicketStatisticsTotal();

    List<Map<String,Object>> selectMeetingSign(@Param("meetingId")String meetingId);

    Integer selectMeetingSignTotal(@Param("meetingId")String meetingId);

    Integer userMeetingSignByMeetingUserId(@Param("meetingUserId")String meetingUserId);

    List<Map<String,Object>> selectMeetingByName(@Param("content")String content);

    Integer updateMeetingByActivityId(Meeting meeting);
}
