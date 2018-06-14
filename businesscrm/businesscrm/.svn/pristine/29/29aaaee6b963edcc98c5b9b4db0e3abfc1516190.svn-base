package com.xkd.service;

import com.xkd.mapper.DictionaryMapper;
import com.xkd.mapper.MeetingMapper;
import com.xkd.mapper.SpreadMapper;
import com.xkd.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SpreadService {

    @Autowired
    private SpreadMapper spreadMapper;
    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private MeetingMapper meetingMapper;

    public Integer insertSpreadSetting(String loginUsrId,String inviteTitle,String productionId,String productionTypeId,String getRate) throws Exception {


        SpreadSetting spreadSetting = new SpreadSetting();
        String id = UUID.randomUUID().toString();
        Dictionary dictionary = dictionaryMapper.selectDictionaryById(productionTypeId);
        String value = null;
        if(dictionary != null){
            value = dictionary.getValue();
        }else{
            throw new Exception("根据ID没有查找到数据字典的数据");
        }

        //一个活动可能对应会务，也可能对应视频，属于一对多，那就在多的一方建立外键
        if("会务".equals(value)){
            spreadSetting.setProductionTableType(0);
            Meeting meeting = new Meeting();
            meeting.setId(productionId);
            meeting.setSpreadSettingId(id);
            meetingMapper.updateMeetingByActivityId(meeting);
            Meeting meetingExists = meetingMapper.selectMeetingById(productionId);
            spreadSetting.setProductionName(meetingExists.getMeetingName());
        }else if("视频".equals(value)){
            spreadSetting.setProductionTableType(1);
        }

        spreadSetting.setId(id);
        spreadSetting.setCreateDate(new Date());
        spreadSetting.setCreatedBy(loginUsrId);
        spreadSetting.setUpdatedBy(loginUsrId);
        spreadSetting.setUpdateDate(new Date());
        spreadSetting.setGetRate(getRate);
        spreadSetting.setInviteTitle(inviteTitle);
        spreadSetting.setProductionTypeId(productionTypeId);
        spreadSetting.setProductionId(productionId);
        spreadSetting.setStatus(0);
        spreadSetting.setVflag(0);
        return spreadMapper.insertSpreadSetting(spreadSetting);
    }

    public Integer updateSpreadSetting(SpreadSetting spreadSetting) {
        return spreadMapper.updateSpreadSetting(spreadSetting);
    }

    public List<SpreadSetting> selectSpreadSettings(Map<String, Object> paramMap) {
        return spreadMapper.selectSpreadSettings(paramMap);
    }

    public Integer selectSpreadSettingsTotal(Map<String, Object> paramMap) {
        return spreadMapper.selectSpreadSettingsTotal(paramMap);
    }

    /*
        根据数据字典类型查不同的表
     */
    public List<Map<String,Object>> selectProductionByTypeId(String productionTypeId,String content) throws Exception {

        Dictionary dictionary = dictionaryMapper.selectDictionaryById(productionTypeId);
        String value = null;
        if(dictionary != null){
            value = dictionary.getValue();
        }else{
            throw new Exception("根据ID没有查找到数据字典的数据");
        }
        List<Map<String,Object>> list = null;
        if("会务".equals(value)){
            list = meetingMapper.selectMeetingByName(content);
        }else if("视频".equals(value)){
            //视频暂时没有做，有的话在加上
            return null;
        }
        return list;
    }

    public Integer insertSpreadUser(SpreadUser spreadUser) {
        return spreadMapper.insertSpreadUser(spreadUser);
    }

    public SpreadUser selectSpreadUserByMobile(String mobile) {
        return spreadMapper.selectSpreadUserByMobile(mobile);
    }

    public Integer updateSpreadUser(SpreadUser spreadUser) {
        return spreadMapper.updateSpreadUser(spreadUser);
    }

    public SpreadUser selectSpreadUserById(String id) {
        return spreadMapper.selectSpreadUserById(id);
    }

    public SpreadSetting selectSpreadSettingById(String id) {
        return spreadMapper.selectSpreadSettingById(id);
    }

    public List<UserGetMoney> selectSpreadUserGetMoneyLogs(Map<String, Object> paramMap) {
        return spreadMapper.selectSpreadUserGetMoneyLogs(paramMap);
    }

    public Integer insertGetMoneyLog(UserGetMoney userGetMoney) {
        return spreadMapper.insertGetMoneyLog(userGetMoney);
    }

    public Integer selectSpreadUserGetMoneyLogsTotal(Map<String, Object> paramMap) {
        return spreadMapper.selectSpreadUserGetMoneyLogsTotal(paramMap);
    }

    public Map<String,Object> selectSpreadGetMoneyLogDetail(String userGetMoneyLogId) {
        return spreadMapper.selectSpreadGetMoneyLogDetail(userGetMoneyLogId);
    }

    public List<Map<String,Object>> selectSpreadUsers(Map<String, Object> map) {
        return spreadMapper.selectSpreadUsers(map);
    }

    public Integer selectSpreadUsersTotal(Map<String, Object> map) {
        return spreadMapper.selectSpreadUsersTotal(map);
    }

    public List<Map<String,Object>> selectPcSpreadUserDetail(Map<String, Object> map) {
        return spreadMapper.selectPcSpreadUserDetail(map);
    }

    public Integer selectPcSpreadUserDetailTotal(Map<String, Object> map) {
        return spreadMapper.selectPcSpreadUserDetailTotal(map);
    }

    public List<Map<String,Object>> selectSpreadUserlogs(Map<String, Object> map) {
        return spreadMapper.selectSpreadUserlogs(map);
    }

    public Integer selectSpreadUserlogsTotal(Map<String, Object> map) {
        return spreadMapper.selectSpreadUserlogsTotal(map);
    }

    public List<Map<String,Object>> selectGetMoneys(Map<String, Object> paramMap) {
        return spreadMapper.selectGetMoneys(paramMap);
    }

    public Integer selectGetMoneysTotal(Map<String, Object> paramMap) {
        return spreadMapper.selectGetMoneysTotal(paramMap);
    }

    public void doGetMoneyStatus(String id, String logStauts) throws Exception{

         UserGetMoney userGetMoney = spreadMapper.selectGetMoneyLogById(id);
        //申请成功，钱包的金额减去
        if(userGetMoney == null){
            throw new Exception("根据ID没有查到提现申请信息");
        }else if("1".equals(logStauts) && "0".equals(userGetMoney.getLogStauts().toString())){

             SpreadUser spreadUser = new SpreadUser();
             spreadUser.setId(userGetMoney.getUserSpreadId());
             spreadUser.setUpdatedBy(id);
             spreadUser.setUpdateDate(new Date());
             //去掉小数点、保留整数
             SpreadUser spreadUserExists = spreadMapper.selectSpreadUserById(userGetMoney.getUserSpreadId());
             String accountSaving = spreadUserExists.getAccountSaving() != null?spreadUserExists.getAccountSaving():"0";
             double accountSavingInt = Double.parseDouble(accountSaving)*100;
             double getMoneyDouble = Double.parseDouble(userGetMoney.getGetMoney())*100;
             spreadUser.setAccountSaving(new Double(accountSavingInt-getMoneyDouble+"").intValue()+"");
             spreadMapper.updateSpreadUser(spreadUser);

             UserGetMoney userGetMoneyUpdate = new UserGetMoney();
             userGetMoneyUpdate.setId(userGetMoney.getId());
             userGetMoney.setLogStauts(1);
             spreadMapper.updateGetMoneyLog(userGetMoney);

             //如果是已经提现了，撤销提现
         }else if("0".equals(logStauts) && "1".equals(userGetMoney.getLogStauts().toString())){
            SpreadUser spreadUser = new SpreadUser();
            spreadUser.setId(userGetMoney.getUserSpreadId());
            spreadUser.setUpdatedBy(id);
            spreadUser.setUpdateDate(new Date());
            //去掉小数点、保留整数
            SpreadUser spreadUserExists = spreadMapper.selectSpreadUserById(userGetMoney.getUserSpreadId());
            String accountSaving = spreadUserExists.getAccountSaving() != null?spreadUserExists.getAccountSaving():"0";
            double accountSavingInt = Double.parseDouble(accountSaving)*100;
            double getMoneyDouble = Double.parseDouble(userGetMoney.getGetMoney())*100;
            spreadUser.setAccountSaving(new Double(accountSavingInt+getMoneyDouble+"").intValue()+"");
            spreadMapper.updateSpreadUser(spreadUser);

            UserGetMoney userGetMoneyUpdate = new UserGetMoney();
            userGetMoneyUpdate.setId(userGetMoney.getId());
            userGetMoney.setLogStauts(0);
            spreadMapper.updateGetMoneyLog(userGetMoney);

        }else{
            throw new Exception("数据信息出现错乱，请研发查看");
        }


    }

    public SpreadUser selectSpreadUserByOpenId(String openId) {
        return spreadMapper.selectSpreadUserByOpenId(openId);
    }
}
