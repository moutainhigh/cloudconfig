package com.xkd.mapper;

import com.xkd.model.MeetingFieldPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/20.
 */
public interface MeetingFieldMapper {
    public int insertMeetingField(MeetingFieldPo meetingFieldPo);
    public int updateMeetingField(MeetingFieldPo meetingFieldPo);
    public int deleteMeetingFieldByIds(@Param("idList")List<String> idList);
    public int deleteByMeetingId(@Param("meetingId")String meetingId);
}
