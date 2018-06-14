package com.xkd.mapper;

import com.xkd.model.MeetingTicketPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/20.
 */
public interface MeetingTicketMapper {

    public int insertMeetingTicket(MeetingTicketPo meetingTicketPo);

    public int updateMeetingTicket(MeetingTicketPo meetingTicketPo);

    public int deleteMeetingTicketByIds(@Param("idList") List<String> idList);

    public int deleteMeetingTicketNotInIds(@Param("idList") List<String> idList,@Param("meetingId")String meetingId);

}
