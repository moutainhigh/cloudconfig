package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.mapper.DC_UserMapper;
import com.xkd.model.*;
import com.xkd.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/hotelSetting")
@Transactional
public class HotelSettingController  extends BaseController{

	
	@Autowired
	HotelSettingService hotelSettingService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private DC_UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private CompanyService companyServie;
	@Autowired
	private LabelService labelService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserAttendMeetingService userAttendMeetingService;
	@Autowired
	private SolrService solrService;
	@Autowired
	private UserDynamicService userDynamicService;
	@Autowired
	private UserService userInviteService;
	@Autowired
	DC_UserMapper mapper;
	@Autowired
	private UserService userService1;
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月15日 
	 * @功能描述:根据手机号查询企业的基础信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUserAttendMeetingBaseInfos")
	public ResponseDbCenter selectUserAttendMeetingBaseInfos(HttpServletRequest req,HttpServletResponse rsp){
		
		String meetingId = req.getParameter("meetingId");
		String mobile = req.getParameter("mobile");
		String uid = req.getParameter("uid");
		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(mobile)){

			return ResponseConstants.MISSING_PARAMTER;
		}
		Map<String,Object> map = null;
		Map<String,Object> userMap = null;
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try{
			map = userService.selectUserAttendMeetingBaseInfos(meetingId,mobile,uid);
			if(map == null || map.isEmpty()){
				userMap = userService.selectUserAndHotelSetingByMobile(meetingId,mobile);
				if(userMap != null && !userMap.isEmpty()){
					String userId = (String)userMap.get("id");
					userMap.put("userId",userId);
					responseDbCenter.setResModel(userMap);
				}
			}else{
				responseDbCenter.setResModel(map);
			}
			return responseDbCenter;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月17日 
	 * @功能描述:根据手机号查询企业的基础信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteUserHotelByMeetingUserId")
	public ResponseDbCenter deleteUserHotelByMeetingUserId(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		try{
			String meetingUserId = req.getParameter("meetingUserId");
			if(StringUtils.isBlank(meetingUserId)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			hotelService.deleteUserHotelByMeetingUserId(meetingUserId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月14日 
	 * @功能描述:插入更新酒店设置信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeHotelSetting")
	public ResponseDbCenter changeHotelSetting(HttpServletRequest req) throws Exception{
		
		String useFlag = req.getParameter("useFlag");
		String hotelBasicInfo = req.getParameter("hotelBasicInfo");
		
		try {
			if(StringUtils.isBlank(hotelBasicInfo) || StringUtils.isBlank(useFlag)){
				
				return ResponseConstants.MISSING_PARAMTER;
			}
			List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(hotelBasicInfo,Object.class); 
			for(Map<String, Object> map : results){
				map.put("userId", req.getSession().getAttribute("token"));
				if(StringUtils.isNotBlank(useFlag)){
					map.put("useFlag", useFlag);
				}
				hotelSettingService.saveHotelSetting(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月14日 
	 * @功能描述:用户填写入住信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeUserHotels")
	public ResponseDbCenter changeUserHotels(HttpServletRequest req,String token) throws Exception{
		
		
		try {
			
				Hotel hotel = new Hotel();
				hotel.setEndTime(req.getParameter("endTime"));
				hotel.setMeetingUserId(req.getParameter("meetingUserId"));
				hotel.setPerson((String)req.getSession().getAttribute(token));
				hotel.setRoomNumber(req.getParameter("roomNumber"));
				hotel.setBigRoomNumber(req.getParameter("bigRoomNumber"));
				hotel.setStartTime(req.getParameter("startTime"));
				hotel.setHotelId(req.getParameter("hotelId"));
				if(StringUtils.isBlank(hotel.getMeetingUserId()) || StringUtils.isBlank(hotel.getHotelId())){
					return ResponseConstants.MISSING_PARAMTER;
				}
				hotelService.updateHotelById(hotel);
				
			
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月15日 
	 * @功能描述:邀请函填写用户参会信息，基础信息月详细越好
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveUserAttendMeetingBaseInfos")
	public ResponseDbCenter saveUserAttendMeetingBaseInfos(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		String companyId = req.getParameter("companyId");
		String meetingId = req.getParameter("meetingId");
		String userId = req.getParameter("userId");
		String meetingUserId = req.getParameter("meetingUserId");
		String addressId = req.getParameter("addressId");
		String uname = req.getParameter("uname");
//		String mobile = req.getParameter("mobile");
		String companyName = req.getParameter("companyName");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String station = req.getParameter("station");
		String establishTime = req.getParameter("establishTime");
		String parentIndustryId  = req.getParameter("parentIndustryId");
//		String sonIndustryId  = req.getParameter("sonIndustryId");
		String companyPropertyId = req.getParameter("companyPropertyId");
		String manageScope = req.getParameter("manageScope");
		String companySize = req.getParameter("companySize");
		String annualSalesVolume = req.getParameter("annualSalesVolume");
		String annualProfit = req.getParameter("annualProfit");
		//0新训，1复训
		String learnStatus = req.getParameter("learnStatus");
		String need = req.getParameter("need");
		String needDetail = req.getParameter("needDetail");
//		String financingUseing = req.getParameter("financingUseing");
//		String financingMoney = req.getParameter("financingMoney");
//		String labels = req.getParameter("labels");

		if(StringUtils.isBlank(loginUserId)){
			loginUserId = req.getSession().getAttribute(req.getParameter("token")).toString();
		}

		if(StringUtils.isBlank(userId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		Address address = null;
		try{
			if(StringUtils.isNotBlank(companyId)){
				Company companyExistsID = companyServie.selectCompanyInfoById(companyId);

				String pcCompanyId = null;
				Map<String, Object> mapp = userService1.selectUserById(loginUserId);
				if(mapp !=null && mapp.size() > 0){
					pcCompanyId = (String)mapp.get("pcCompanyId");
				}

				List<Company> companyExistsNAMEs = companyServie.selectCompanyByName(companyName,pcCompanyId);
				for(Company ccompany : companyExistsNAMEs){
						if(ccompany != null && !ccompany.getCompanyName().equals(companyExistsID.getCompanyName())){
						return ResponseConstants.FUNC_COMPANY_EXIST;
					}
				}
				Map<String,Object> paramMapp = new HashMap<>();
				paramMapp.put("id", userId);
				paramMapp.put("uname", uname);
				userInviteService.updateDcUser(paramMapp);
				userInviteService.updateStationByCompanyIdUserId(companyId,userId,station);

				Map<String,Object> map = new HashMap<>();
				map.put("id", companyId);
				map.put("annualProfit", annualProfit);
				map.put("annualSalesVolume", annualSalesVolume);
				map.put("companyName", companyName);
				map.put("companySize", companySize);
				map.put("establishTime", establishTime);
				map.put("parentIndustryId", parentIndustryId);
				map.put("manageScope", manageScope);
				map.put("companyPropertyId", companyPropertyId);
				Company company =  companyServie.selectCompanyInfoById(companyId);

				if(StringUtils.isNotBlank(userId) && userId.equals(company.getContactUserId())){
					map.put("contactUserId", userId);
					map.put("contactName", uname);
				}
				companyServie.updateCompanyInfoById(map);
				companyServie.updateCompanyDetailInfoById(map);
				userDynamicService.addUserDynamic(loginUserId, companyId, companyName, "更新", "手机端邀请函  "+uname+"修改了\""+companyName+"\"", 0,null,null,null);
				if(StringUtils.isNotBlank(companyId) && !"undefined".equals(companyId)){
					address = new Address();
					address.setProvince(province);
					address.setCity(city);
					address.setCounty(county);
					Address tempAddress = addressService.selectAddressByUserId(companyId);
					if(tempAddress != null){
						address.setId(tempAddress.getId());
						addressService.updateAddressInfoByIdByInvitation(address);
						String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"'  where id ='"+companyId+"'";
						companyServie.updateCompanyBySql(sql);
					}else {
						address = new Address();
						String uuid = UUID.randomUUID().toString();
						address.setUserId(companyId);
						address.setProvince(province);
						address.setCity(city);
						address.setCounty(county);;
						address.setId(uuid);
						addressService.saveUserAddress(address);
						String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"'  where id ='"+companyId+"'";
						companyServie.updateCompanyBySql(sql);
					}
				}
				UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
				Map<String,Object> umap = new HashMap<>();
				if("1".equals(learnStatus)){
					umap.put("learnStatus", 1);
					userAttendMeeting.setLearnStatus(1);
				}else{
					umap.put("learnStatus", 0);
					userAttendMeeting.setLearnStatus(0);
				}

				if(StringUtils.isNotBlank(meetingUserId)){
					umap.put("id",meetingUserId);
					umap.put("need", need);
					umap.put("needDetail", needDetail);
					userAttendMeetingService.updateMeetingUserNeedByIdByInvitation(umap);
				}else {
					userAttendMeeting.setCompanyId(companyId);
					userAttendMeeting.setMeetingId(meetingId);
					userAttendMeeting.setUserId(userId);
					userAttendMeeting.setNeedDetail(needDetail);
					userAttendMeeting.setNeed(need);
					userAttendMeeting.setCreatedBy(userId);
					userAttendMeeting.setUpdatedBy(userId);
					userAttendMeetingService.saveUserMeeting(userAttendMeeting);
				}
			}else{
				List<Company> allStatusCompany = companyServie.selectAllStatusCompanyByName(companyName,null);

				if(allStatusCompany == null || allStatusCompany.size() ==0){

					companyId = UUID.randomUUID().toString();
					Map<String,Object> paramMapp = new HashMap<>();
					paramMapp.put("id", userId);
					paramMapp.put("uname", uname);
					userInviteService.updateDcUser(paramMapp);

					Map<String,Object> stationMap = new HashMap<>();
					stationMap.put("id",UUID.randomUUID().toString());
					stationMap.put("userId",userId);
					stationMap.put("companyId",companyId);
					stationMap.put("station",station);
					stationMap.put("createdBy",userId);
					userInfoService.insertUserInfo(stationMap);

					Map<String,Object> map = new HashMap<>();

					map.put("id", companyId);
					map.put("annualProfit", annualProfit);
					map.put("annualSalesVolume", annualSalesVolume);
					map.put("companyName", companyName);
					map.put("companySize", companySize);
					map.put("establishTime", establishTime);
					map.put("parentIndustryId", parentIndustryId);
					map.put("manageScope", manageScope);
					map.put("companyPropertyId", companyPropertyId);
					map.put("contactUserId", userId);
					map.put("contactName", uname);

					companyServie.insertCompanyInfo(map);
					companyServie.insertCompanyDetailInfo(map);
					userDynamicService.addUserDynamic(loginUserId, companyId, companyName, "新增", "手机端邀请函  "+uname+"新增了\""+companyName+"\"", 0,null,null,null);

					address = new Address();
					String uuid = UUID.randomUUID().toString();
					address.setUserId(companyId);
					address.setProvince(province);
					address.setCity(city);
					address.setCounty(county);;
					address.setId(uuid);
					addressService.saveUserAddress(address);

					String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"'  where id ='"+companyId+"'";
					companyServie.updateCompanyBySql(sql);

					UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
					Map<String,Object> umap = new HashMap<>();
					if("1".equals(learnStatus)){
						umap.put("learnStatus", 1);
						userAttendMeeting.setLearnStatus(1);
					}else{
						umap.put("learnStatus", 0);
						userAttendMeeting.setLearnStatus(0);
					}

					if(StringUtils.isNotBlank(meetingUserId)){
						umap.put("id",meetingUserId);
						umap.put("need", need);
						umap.put("needDetail", needDetail);
						userAttendMeetingService.updateMeetingUserNeedByIdByInvitation(umap);
					}else {
						userAttendMeeting.setCompanyId(companyId);
						userAttendMeeting.setMeetingId(meetingId);
						userAttendMeeting.setUserId(userId);
						userAttendMeeting.setNeedDetail(needDetail);
						userAttendMeeting.setNeed(need);
						userAttendMeeting.setCreatedBy(userId);
						userAttendMeeting.setUpdatedBy(userId);
						userAttendMeeting.setStatus("未审核");
						userAttendMeetingService.saveUserMeeting(userAttendMeeting);
					}
				}else{
					companyId = allStatusCompany.get(0).getId();
					Map<String,Object> paramMapp = new HashMap<>();
					paramMapp.put("id", userId);
					paramMapp.put("uname", uname);
					userInviteService.updateDcUser(paramMapp);

					Map<String,Object> stationMap = new HashMap<>();
					stationMap.put("id",UUID.randomUUID().toString());
					stationMap.put("userId",userId);
					stationMap.put("companyId",companyId);
					stationMap.put("station",station);
					stationMap.put("createdBy",userId);
					userInfoService.insertUserInfo(stationMap);

					Map<String,Object> map = new HashMap<>();
					map.put("id", companyId);
					map.put("annualProfit", annualProfit);
					map.put("annualSalesVolume", annualSalesVolume);
					map.put("companyName", companyName);
					map.put("companySize", companySize);
					map.put("establishTime", establishTime);
					map.put("parentIndustryId", parentIndustryId);
					map.put("manageScope", manageScope);
					map.put("companyPropertyId", companyPropertyId);
					map.put("status",0);
					Company company =  companyServie.selectCompanyInfoById(companyId);

					if(StringUtils.isNotBlank(userId) && userId.equals(company.getContactUserId())){
						map.put("contactUserId", userId);
						map.put("contactName", uname);
					}
					companyServie.updateCompanyInfoById(map);
					companyServie.updateCompanyDetailInfoById(map);
					userDynamicService.addUserDynamic(loginUserId, companyId, companyName, "更新", "手机端邀请函  "+uname+"修改了\""+companyName+"\"", 0,null,null,null);

					address = new Address();
					address.setProvince(province);
					address.setCity(city);
					address.setCounty(county);

					Address tempAddress = addressService.selectAddressByUserId(companyId);

					if(tempAddress != null){
						address.setId(tempAddress.getId());
						addressService.updateAddressInfoByIdByInvitation(address);
						String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"'  where id ='"+companyId+"'";
						companyServie.updateCompanyBySql(sql);
					}else {
						address = new Address();
						String uuid = UUID.randomUUID().toString();
						address.setUserId(companyId);
						address.setProvince(province);
						address.setCity(city);
						address.setCounty(county);;
						address.setId(uuid);
						addressService.saveUserAddress(address);
						String sql = "update dc_company set province = '"+province+"',city = '"+city+"',county = '"+county+"'  where id ='"+companyId+"'";
						companyServie.updateCompanyBySql(sql);
					}

					UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
					Map<String,Object> umap = new HashMap<>();
					if("1".equals(learnStatus)){
						umap.put("learnStatus", 1);
						userAttendMeeting.setLearnStatus(1);
					}else{
						umap.put("learnStatus", 0);
						userAttendMeeting.setLearnStatus(0);
					}

					if(StringUtils.isNotBlank(meetingUserId)){
						umap.put("id",meetingUserId);
						umap.put("need", need);
						umap.put("needDetail", needDetail);
						userAttendMeetingService.updateMeetingUserNeedByIdByInvitation(umap);
					}else {
						userAttendMeeting.setCompanyId(companyId);
						userAttendMeeting.setMeetingId(meetingId);
						userAttendMeeting.setUserId(userId);
						userAttendMeeting.setNeedDetail(needDetail);
						userAttendMeeting.setNeed(need);
						userAttendMeeting.setCreatedBy(userId);
						userAttendMeeting.setUpdatedBy(userId);
						userAttendMeeting.setStatus("未审核");
						userAttendMeetingService.saveUserMeeting(userAttendMeeting);
					}
				}
			}
			solrService.updateCompanyIndex(companyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年8月14日 
	 * @功能描述:根据会务id查询酒店信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectHotelSettingsByMeetingId")
	public ResponseDbCenter selectHotelSettingsByMeetingId(HttpServletRequest req,HttpServletResponse rsp){
		try {
			String meetingId = req.getParameter("meetingId");
			if(StringUtils.isBlank(meetingId)){
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(hotelSettingService.selectHotelSettingsByMeetingId(meetingId));
			
			return responseDbCenter;
		}  catch (Exception e) {
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		
	}
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年8月15日 
	 * @功能描述:查询参会人填写的酒店信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUserInfosByMobileByInvitation")
	public ResponseDbCenter selectUserInfosByMobileByInvitation(HttpServletRequest req,String token,String meetingId){
		
		String userId = (String) req.getSession().getAttribute(token);
		
		if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(userId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(hotelSettingService.selectUserInfosByMobileByInvitation(meetingId,userId));
			return responseDbCenter;
			
		}  catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		
	}

	/**
	 *
	 * @author: gaodd
	 * @2017年8月15日
	 * @功能描述:保存用户企业关联信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveUserCompany")
	public ResponseDbCenter saveUserCompany(HttpServletRequest req,String token){

		String companyId = req.getParameter("companyId");
		String meetingId = req.getParameter("meetingId");

		String userId = (String) req.getSession().getAttribute(token);

		if(StringUtils.isBlank(companyId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		try {

			List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,req.getSession().getAttribute(token).toString());
			if(userAttendMeetings != null && userAttendMeetings.size() >0){
				String ids = "";
				for(UserAttendMeeting at : userAttendMeetings){
					ids += "'"+at.getId()+"',";
				}
				ids = "id in ("+ids.substring(0,ids.lastIndexOf(","))+")";
				userAttendMeetingService.deleteMeetingUserByIds(ids);
			}

			HashMap<String, Object> meetingUser = new HashMap<>();
			meetingUser.put("companyid", companyId);
			meetingUser.put("userId",req.getSession().getAttribute(token).toString());
			meetingUser.put("meetingid", meetingId);
			meetingUser.put("status", "未审核");
			mapper.saveMeetingUser(meetingUser);

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			return responseDbCenter;

		}  catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

	/**
	 *
	 * @author: gaodd
	 * @2017年8月15日
	 * @功能描述:清除该用户绑定的企业信息
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteUserCompany")
	public ResponseDbCenter deleteUserCompany(HttpServletRequest req,String token){

		String meetingId = req.getParameter("meetingId");
		if(StringUtils.isBlank(meetingId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		try {

			List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,req.getSession().getAttribute(token).toString());
			if(userAttendMeetings != null && userAttendMeetings.size() >0){
				String ids = "";
				for(UserAttendMeeting at : userAttendMeetings){
					ids += "'"+at.getId()+"',";
				}
				ids = "id in ("+ids.substring(0,ids.lastIndexOf(","))+")";
				userAttendMeetingService.deleteMeetingUserByIds(ids);
			}

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			return responseDbCenter;

		}  catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
	
}
