package com.xkd.service;

import com.xkd.mapper.DC_UserMapper;
import com.xkd.mapper.ScheduleUserMapper;
import com.xkd.model.DC_User;
import com.xkd.model.UserAction;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DC_UserService {

	@Autowired
	DC_UserMapper mapper;
	@Autowired
	SolrService solrService;
	@Autowired
	UserDynamicService userDynamicService;

	@Autowired
	CompanyService companyService;

	@Autowired
	private ScheduleUserMapper scheduleColleagueMapper;

	
	//1mobile,2weixin,3unionid
	public DC_User getUserByObj(String id, String ttype){
		System.out.println("DC_UserService.getUserByObj()"+id+"  "+ttype);
		return mapper.getUserByObj(id,ttype);
	}
	
	public int saveUser(DC_User obj){
		if(StringUtils.isNotBlank(obj.getId())){
			
			return mapper.editUser(obj);
		}else{
			String userId = UUID.randomUUID().toString();
			obj.setId(userId);
			mapper.saveUserDetail(obj);
			return mapper.saveUser(obj);
		}
	}

	public int userRelationUserInfo(DC_User user,String meetingId,String companyId,String companyName,String uname,String ttype) {
		//判断是否是邀约用户
		//判断是否是邀请函或者申请过来的用户
		HashMap<String, Object> checkUser = mapper.checkUserToMeeting(user.getId(),meetingId);
		Object status = checkUser == null ?null:checkUser.get("status");
		if(checkUser == null){
			if((StringUtils.isNotBlank(companyId) || StringUtils.isNotBlank(companyName))){
				HashMap<String, Object> companyInfo = null;
				if(StringUtils.isNotBlank(companyName)){
					companyInfo = mapper.getCompanyById(null, companyName);
					if(companyInfo != null  && !companyInfo.get("id").equals(companyId)){
						companyId = companyInfo.get("id")+"";
						System.out.println(companyInfo.toString());
						if("2".equals(""+companyInfo.get("status"))){
							companyInfo.put("status","0");
							mapper.editCompany(companyInfo);
						}
					}
				}else if(StringUtils.isNotBlank(companyId)){
					companyInfo = mapper.getCompanyById(companyId, null);
				}
				if(StringUtils.isBlank(companyId)){
						//保存企業
						companyId = UUID.randomUUID().toString();
						HashMap<String, String> company = new HashMap<>();
						company.put("id",companyId);
						company.put("companyName",companyName);
						company.put("createdBy",user.getId());
						mapper.saveCompany(company);
						mapper.saveCompanyDetail(company);
						System.out.println("保存企业");
						
						//编辑新增企业会添加动态记录信息--------------------begin--------------------
						Map<String,Object> paramMap = new HashMap<>();
						paramMap.put("id", UUID.randomUUID().toString());
						paramMap.put("groupId", companyId);
						paramMap.put("groupName",companyName);
						paramMap.put("contentValue", "手机端"+uname+"创建了\""+companyName+"\"");
						paramMap.put("updatedBy", user.getId());
						paramMap.put("ttype", 0);
						paramMap.put("createDate", DateUtils.currtime());
						userDynamicService.saveUserDynamic(paramMap);
						//编辑新增企业会添加动态记录信息--------------------end--------------------
				}
				//保存企业用户跟企业的 关系
				HashMap<String, Object> obj = new HashMap<>();
				obj.put("userid",user.getId());
				obj.put("companyid", companyId);
				mapper.saveUserCompany(obj);
				System.out.println("保存企业跟用户的关系");




				//添加参会人到会务
				HashMap<String, Object> meetingUser = new HashMap<>();
				meetingUser.put("companyid", companyId);
				meetingUser.put("userId", user.getId());
				meetingUser.put("meetingid", meetingId);
				meetingUser.put("status", "未审核");
				mapper.saveMeetingUser(meetingUser);
				System.out.println("保存meetingUser");

				solrService.updateCompanyIndex(companyId);



				if(null != companyInfo){

					UserAction userAction = new UserAction();
					userAction.setActionId(meetingId);
					userAction.setActionType("2");
					userAction.setCreateDate(DateUtils.currtime());
					userAction.setCreatedBy(user.getId());
					userAction.setActionTitle(" 你有一个客户需要审核");
					userAction.setStartDate(companyInfo.get("channel")+"");
					userAction.setEndDate(companyInfo.get("userType")+"");
					userAction.setActionContent(companyName);
					if(StringUtils.isNotBlank(companyInfo.get("companyAdviserId")+"")){
						userAction.setActionUserId(companyInfo.get("companyAdviserId")+"");
						scheduleColleagueMapper.saveUserAction(userAction);
					}
					if(StringUtils.isNotBlank(companyInfo.get("companyDirectorId")+"")){
						userAction.setActionUserId(companyInfo.get("companyDirectorId")+"");
						scheduleColleagueMapper.saveUserAction(userAction);
					}
				}


				return 0;//申请书保存成功，报名成功 
			
			}else{
				List<HashMap<String, Object>> userCompanyList = mapper.userCompanyList(user.getId());
				if(null == userCompanyList || userCompanyList.size() ==  0){
					return 1;//请填写申请书	
				}else{
					//请绑定参会企业
					return 5;
				}
			}
		}else if(status!= null && StringUtils.isNotBlank(status.toString())){
			if(checkUser.get("status").equals("未审核")){
				return 2;//请顾问审批后进入签到系统
			}else if(checkUser.get("status").equals("已报名") && ttype.equals("myCrm")){
				mapper.editMeetingUser(checkUser.get("id"),"已参会");
				return 3;//返回用户签到成功数据
			}
			return 4;//提示邀请函用户，已报名or表示用户成功进入主界面
		}
		
		return 1;//请填写申请书	
	}

	public DC_User getUserById(Object userid) {
		return mapper.getUserById(userid);
	}

	public HashMap<String, Object> getSignInfo(String meetingId,String userId) {
		return mapper.getSignInfo(userId,meetingId);
	}

	public DC_User getAnswerUser(String id, String userid) {
		
		return mapper.getAnswerUser(id,userid);
	}

	public Map<String, Object> selectUserAttendMeetingBaseInfos(String meetingId, String mobile, String uid) {
		
		Map<String, Object> map = mapper.selectUserAttendMeetingBaseInfos(meetingId,mobile,uid);
		
		return map;
	}

	public List<Map<String, Object>> selectUserCompanys(String userId) {

		return mapper.selectUserCompanys(userId);

	}

	public Map<String,Object> selectUserAndHotelSetingByMobile(String meetingId, String mobile) {

		return mapper.selectUserAndHotelSetingByMobile(meetingId,mobile);

	}

	public void editUserDetail(DC_User user) {
		mapper.editUserDetail(user);
	}
}
