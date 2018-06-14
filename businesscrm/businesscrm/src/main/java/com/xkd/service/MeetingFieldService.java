package com.xkd.service;

import com.xkd.mapper.MeetingFieldMapper;
import com.xkd.model.MeetingFieldPo;
import com.xkd.model.MeetingPo;
import com.xkd.utils.RedisCacheUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/20.
 */
@Service
public class MeetingFieldService {
    @Autowired
    MeetingFieldMapper meetingFieldMapper;

    public int insertMeetingField(MeetingFieldPo meetingFieldPo) {
        return meetingFieldMapper.insertMeetingField(meetingFieldPo);
    }

    public int updateMeetingField(MeetingFieldPo meetingFieldPo) {
        return meetingFieldMapper.updateMeetingField(meetingFieldPo);
    }

    public int deleteMeetingFieldByIds(List<String> idList) {
        if (idList.size() == 0) {
            return 0;
        }
        return meetingFieldMapper.deleteMeetingFieldByIds(idList);
    }

    public int deleteByMeetingId(String meetingId){
        return meetingFieldMapper.deleteByMeetingId(meetingId);
    }


}
