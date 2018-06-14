package com.xkd.service;

import com.xkd.mapper.MeetingTicketMapper;
import com.xkd.model.MeetingTicketPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/20.
 */
@Service
public class MeetingTicketService {
    @Autowired
    MeetingTicketMapper meetingTicketMapper;

    public int insertMeetingTicket(MeetingTicketPo meetingTicketPo) {
        return meetingTicketMapper.insertMeetingTicket(meetingTicketPo);
    }

    public int updateMeetingTicket(MeetingTicketPo meetingTicketPo) {
        return meetingTicketMapper.updateMeetingTicket(meetingTicketPo);
    }

    public int deleteMeetingTicketByIds(@Param("idList") List<String> idList) {
        if (idList.size() == 0) {
            return 0;
        }
        return meetingTicketMapper.deleteMeetingTicketByIds(idList);
    }

    public int deleteMeetingTicketNotInIds( List<String> idList,String meetingId){
        return meetingTicketMapper.deleteMeetingTicketNotInIds(idList,meetingId);
    }

}
