package com.xkd.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mchange.v2.codegen.bean.BeangenUtils;
import com.xkd.mapper.MeetingMapper;
import com.xkd.mapper.MeetingTicketMapper;
import com.xkd.model.Meeting;
import com.xkd.model.MeetingFieldPo;
import com.xkd.model.MeetingPo;
import com.xkd.model.MeetingTicketPo;
import com.xkd.model.vo.MeetingFieldVo;
import com.xkd.model.vo.MeetingTicketVo;
import com.xkd.model.vo.MeetingVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by dell on 2018/4/20.
 */
@Service
public class MeetingService {


    @Autowired
    MeetingMapper meetingMapper;
    @Autowired
    MeetingFieldService meetingFieldService;
    @Autowired
    MeetingTicketService meetingTicketService;

    public void saveMeeting(MeetingVo meetingVo, String loginUserId) {
        MeetingPo meetingPo = new MeetingPo();
        try {
            BeanUtils.copyProperties(meetingPo, meetingVo);
            String meetingId = UUID.randomUUID().toString();
            meetingPo.setId(meetingId);
            meetingPo.setCreatedBy(loginUserId);
            meetingPo.setCreateDate(new Date());
            meetingPo.setUpdatedBy(loginUserId);
            meetingPo.setUpdateDate(new Date());
            meetingMapper.insertMeeting(meetingPo);
            List<MeetingFieldVo> meetingFieldVoList = meetingVo.getMeetingFieldVoList();
            List<MeetingTicketVo> meetingTicketVoList = meetingVo.getMeetingTicketVoList();
            for (int i = 0; i < meetingFieldVoList.size(); i++) {
                MeetingFieldPo meetingFieldPo = new MeetingFieldPo();
                BeanUtils.copyProperties(meetingFieldPo, meetingFieldVoList.get(i));
                meetingFieldPo.setId(UUID.randomUUID().toString());
                meetingFieldPo.setMeetingId(meetingId);
                meetingFieldPo.setCreateDate(new Date());
                meetingFieldPo.setCreatedBy(loginUserId);
                meetingFieldPo.setUpdateDate(new Date());
                meetingFieldPo.setUpdatedBy(loginUserId);
                meetingFieldService.insertMeetingField(meetingFieldPo);
            }

            for (int i = 0; i < meetingTicketVoList.size(); i++) {
                MeetingTicketPo meetingTicketPo = new MeetingTicketPo();
                MeetingTicketVo meetingTicketVo = meetingTicketVoList.get(i);
                BeanUtils.copyProperties(meetingTicketPo, meetingTicketVo);

                meetingTicketPo.setId(UUID.randomUUID().toString());
                meetingTicketPo.setMeetingId(meetingId);
                meetingTicketPo.setCreateDate(new Date());
                meetingTicketPo.setCreatedBy(loginUserId);
                meetingTicketPo.setUpdateDate(new Date());
                meetingTicketPo.setUpdatedBy(loginUserId);
                meetingTicketService.insertMeetingTicket(meetingTicketPo);

            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    public void updateMeeting(MeetingVo meetingVo, String loginUserId) {
        MeetingPo meetingPo = new MeetingPo();
        try {
            BeanUtils.copyProperties(meetingPo, meetingVo);
            meetingMapper.updateMeeting(meetingPo);
            List<MeetingFieldVo> meetingFieldVoList = meetingVo.getMeetingFieldVoList();
            List<MeetingTicketVo> meetingTicketVoList = meetingVo.getMeetingTicketVoList();
            //删除旧的
            meetingFieldService.deleteByMeetingId(meetingPo.getId());
            //循环插入新的
            for (int i = 0; i < meetingFieldVoList.size(); i++) {
                MeetingFieldPo meetingFieldPo = new MeetingFieldPo();
                BeanUtils.copyProperties(meetingFieldPo, meetingFieldVoList.get(i));
                meetingFieldPo.setId(UUID.randomUUID().toString());
                meetingFieldPo.setMeetingId(meetingVo.getId());
                meetingFieldPo.setCreateDate(new Date());
                meetingFieldPo.setCreatedBy(loginUserId);
                meetingFieldPo.setUpdateDate(new Date());
                meetingFieldPo.setUpdatedBy(loginUserId);
                meetingFieldService.insertMeetingField(meetingFieldPo);
            }

            List<String> meetingTicketIdList = new ArrayList<>();


            //更新旧的值
            for (int i = 0; i < meetingTicketVoList.size(); i++) {
                MeetingTicketPo meetingTicketPo = new MeetingTicketPo();
                MeetingTicketVo meetingTicketVo = meetingTicketVoList.get(i);
                BeanUtils.copyProperties(meetingTicketPo, meetingTicketVo);
                if (StringUtils.isNotBlank(meetingTicketVo.getId())) {
                    //Id不为空说明是该条记录是修改操作
                    meetingTicketPo.setUpdateDate(new Date());
                    meetingTicketPo.setUpdatedBy(loginUserId);
                    meetingTicketPo.setMeetingId(meetingPo.getId());
                    meetingTicketService.updateMeetingTicket(meetingTicketPo);
                    meetingTicketIdList.add(meetingTicketPo.getId());
                }
            }

            //删除已经被删除的值
            meetingTicketService.deleteMeetingTicketNotInIds(meetingTicketIdList, meetingPo.getId());


            //插入新的值
            for (int i = 0; i < meetingTicketVoList.size(); i++) {
                MeetingTicketPo meetingTicketPo = new MeetingTicketPo();
                MeetingTicketVo meetingTicketVo = meetingTicketVoList.get(i);
                BeanUtils.copyProperties(meetingTicketPo, meetingTicketVo);
                if (StringUtils.isBlank(meetingTicketVo.getId())) {
                    //插入新的值
                    meetingTicketPo.setId(UUID.randomUUID().toString());
                    meetingTicketPo.setMeetingId(meetingVo.getId());
                    meetingTicketPo.setCreateDate(new Date());
                    meetingTicketPo.setCreatedBy(loginUserId);
                    meetingTicketPo.setUpdateDate(new Date());
                    meetingTicketPo.setUpdatedBy(loginUserId);
                    meetingTicketService.insertMeetingTicket(meetingTicketPo);
                }

            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    public int deleteMeetingByIds(List<String> idList) {
        if (idList.size() == 0) {
            return 0;
        }
        return meetingMapper.deleteMeetingByIds(idList);
    }


    public Integer searchMeetingCount(Map<String ,Object> map){
        return meetingMapper.searchMeetingCount(map);
    }


    public List<Map<String,Object>> searchMeeting(Map<String,Object> map){
        return meetingMapper.searchMeeting(map);
    }


    public Integer updateMeetingFlag(String id,int flag) {
        return meetingMapper.updateMeetingFlag(id,flag);
    }

    public List<Meeting> selectMeetingByParam(String startTime, String endTime,
                                              String meetingType, String meetingStatus, String meetingName, String address, String teacherIds, String mname,
                                              int currentPageInt, int pageSizeInt, List<String> list, String pcCompanyId,String createdByName,Integer meetingFlag){

        return meetingMapper.selectMeetingByParam(startTime,endTime,
                meetingType,meetingStatus,meetingName,address,teacherIds,mname,currentPageInt,pageSizeInt,list,pcCompanyId,createdByName,meetingFlag);
    }


    public Integer updateMeetingById(Meeting meeting) {
        return meetingMapper.updateMeetingById(meeting);
    }


    public Integer saveMeeting(Meeting meeting) {
        return meetingMapper.saveMeeting(meeting);
    }


    public Meeting selectMeetingById(String meetingId) {
        return meetingMapper.selectMeetingById(meetingId);
    }


    public Integer getMeetingCountByParam(String meetingType, String meetingStatus, String meetingName,
                                          String address, String teacherIds, String mname, String startTime,
                                          String endTime, List<String> list, String pcCompanyId,String createdByName,Integer meetingFlag) {

        return meetingMapper.getMeetingCountByParam(meetingType,meetingStatus,meetingName,address,teacherIds,
                mname,startTime,endTime,list,pcCompanyId,createdByName,meetingFlag);
    }


    public Integer deleteMeetingById(String ids) {
        return meetingMapper.deleteMeetingById(ids);
    }


    public List<Map<String, Object>> selectCompanysLogo(String meetingId) {
        return meetingMapper.selectCompanysLogo(meetingId);
    }


    public List<Map<String,Object>> selectMeetingByCompanyId(  String companyId){
        return meetingMapper.selectMeetingByCompanyId(companyId);
    }

    public List<Map<String,Object>> selectUserExcerciseByCompanyId(String companyId){
        return meetingMapper.selectUserExcerciseByCompanyId(companyId);
    }


    public void deleteMeetingUserByCompanyIds(String companyIds){
        meetingMapper.deleteMeetingUserByCompanyIds(companyIds);
    }

    public Integer saveUserSpread(Map<String, Object> map) {
        return meetingMapper.saveUserSpread(map);
    }

    public List<Map<String,Object>> selectUserSpreadsByMeetingId(String meetingId) {
        return meetingMapper.selectUserSpreadsByMeetingId(meetingId);
    }

    public Integer deleteUserSpreadsByIds(List<String> idList) {
        return meetingMapper.deleteUserSpreadsByIds(idList);
    }

    public List<Map<String,Object>> selectDetailByspreadId(String userSpreadId) {
        return meetingMapper.selectDetailByspreadId(userSpreadId);
    }

    public Integer selectCountDetailByspreadId(String userSpreadId) {
        return meetingMapper.selectCountDetailByspreadId(userSpreadId);
    }

    public Integer selectTotalUserSpreadsByMeetingId(String meetingId) {
        return meetingMapper.selectTotalUserSpreadsByMeetingId(meetingId);
    }

    public Map<String,Object> selectUserSpreadByMeetingIdUserId(String meetingId, String userId) {
        return meetingMapper.selectUserSpreadByMeetingIdUserId(meetingId,userId);
    }


    public List<Map<String,Object>> selectMeetingTicketStatistics() {
        return meetingMapper.selectMeetingTicketStatistics();
    }

    public Integer selectMeetingTicketStatisticsTotal() {
        return meetingMapper.selectMeetingTicketStatisticsTotal();
    }

    public List<Map<String,Object>> selectMeetingSign(String meetingId) {
        return meetingMapper.selectMeetingSign(meetingId);
    }

    public Integer selectMeetingSignTotal(String meetingId) {
        return meetingMapper.selectMeetingSignTotal(meetingId);
    }

    public Integer userMeetingSignByMeetingUserId(String meetingUserId) {
        return meetingMapper.userMeetingSignByMeetingUserId(meetingUserId);
    }
}
