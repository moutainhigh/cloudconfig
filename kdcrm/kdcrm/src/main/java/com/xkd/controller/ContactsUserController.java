package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.DC_User;
import com.xkd.model.Exercise;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/contacts")
@Transactional
public class ContactsUserController {


	@Autowired
	ContactsUserService contactsUserService;

	@Autowired
	UserDataPermissionService userDataPermissionService;

	@Autowired
	UserService userService;

	@Autowired
	private UserAttendMeetingService userAttendMeetingService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private SolrService solrService;

	@Autowired
	private CompanyService companyServie;

	@Autowired
	private UserDynamicService userDynamicService;

	//查看所有联系人
	@ResponseBody
	@RequestMapping("/getContactsUserList")
	public ResponseDbCenter getContactsUserList(HttpServletRequest req,int currentPage,int pageSize,String queryType,String queryName)throws Exception{
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object> userInfo = userDataPermissionService.selectUserById(loginUserId);
		List<String>  depList = null;

		if(!userInfo.get("roleId").equals("1")){
			depList = userDataPermissionService.getDataPermissionDepartmentIdList(null,loginUserId);
		}
		currentPage = (currentPage - 1)*pageSize;
		Boolean showMobile = userService.getPrivatePermission(loginUserId, "company/private");
		List<Map<String,String>>  contactsUserList = contactsUserService.getContactsUserList(queryType,queryName,loginUserId,showMobile,depList,currentPage,pageSize);
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(contactsUserList);
		res.setTotalRows(contactsUserService.getContactsUserListTotal(queryType,queryName,loginUserId,showMobile,depList)+"");
		return res;
	}

	//我的联系人统计
	@ResponseBody
	@RequestMapping("/getContactsStatistics")
	public ResponseDbCenter getContactsStatistics(HttpServletRequest req,String startDate,String endDate)throws Exception{
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String, Object> userInfo = userDataPermissionService.selectUserById(loginUserId);
		List<String>  depList = null;

		if(!userInfo.get("roleId").equals("1")){
			depList = userDataPermissionService.getDataPermissionDepartmentIdList(null,loginUserId);
		}
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(contactsUserService.getContactsStatistics(loginUserId,startDate,endDate,depList));
		return res;
	}
	@ApiOperation(value = "添加或修改企业联系人")
	@ResponseBody
	@RequestMapping(value = "/changeCompanyUser",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter changeUserInfo(HttpServletRequest req, HttpServletResponse rsp,
										   @ApiParam(value = "姓名",required = true)@RequestParam(required = true)  String uname,
										   @ApiParam(value = "公司Id" ,required = true)@RequestParam (required = true)  String companyId,
										   @ApiParam(value = "手机",required = true)@RequestParam(required = true)  String mobile,
										   @ApiParam(value = "岗位",required = false)@RequestParam(required = false)     String station,
										   @ApiParam(value = "电话",required = false)@RequestParam(required = false)  String phone,
										   @ApiParam(value = "邮箱",required = false)@RequestParam(required = false)  String email,
										   @ApiParam(value = "用户Id",required = false)@RequestParam(required = false)   String userId,
										   @ApiParam(value = "性别 男  女",required = false)@RequestParam(required = false)  String sex,
										   @ApiParam(value = "描述",required = false)@RequestParam(required = false)  String desc,
										   @ApiParam(value = "出生日期",required = false)@RequestParam(required = false)  String birth,
										   @ApiParam(value = "年龄",required = false)@RequestParam(required = false)  Integer age,
										   @ApiParam(value = "用户资源",required = false)@RequestParam(required = false)  String userResource,
										   @ApiParam(value = "zhongjianbiaoid",required = false)@RequestParam(required = false)  String id,
										   @ApiParam(value = "人物分析",required = false)@RequestParam(required = false)  String personAnalysis
	) throws Exception {

		List<Map<String,String>> companyConcat = contactsUserService.getContactsUserByCompanyIdAndUserId(companyId,mobile);

		if( companyConcat.size() > 0 && StringUtils.isBlank(id)){
			return  ResponseConstants.YICUNZAI;
		}else{
			String loginUserId = (String) req.getAttribute("loginUserId");
			return userInfoService.changeUserInfo(StringUtils.isBlank(userId)?"insert":"update",uname,station,phone,companyId,email,"0",id,sex,desc,mobile,userResource,personAnalysis,birth,loginUserId,age);
		}
	}

	@ApiOperation(value = "删除联系人")
	@ResponseBody
	@RequestMapping(value = "/deleteUserInfo",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter deleteUserInfo(HttpServletRequest req, HttpServletResponse rsp,
										   @ApiParam(value = "员工公司关系Id",required = true)@RequestParam(required = true)  String ids) throws Exception {
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		try {
			List<String> userList = new ArrayList<>(Arrays.asList(ids.split(",")));
			for (String userCompanyId:userList) {
				Map<String, Object> userCompany = userInfoService.selectUserCompanyById(userCompanyId);
				Map<String, Object> userMap = userService.selectUserById((String) userCompany.get("userId"));
				String companyId = (String) userCompany.get("companyId");
				//判断企业是否有权限被修改
				boolean hasPermission=userDataPermissionService.hasPermission(companyId,loginUserId);
				if (!hasPermission){
					return  ResponseConstants.DATA_NOT_PERMITED;
				}
				userInfoService.deleteUserCompany(userCompanyId);
				userAttendMeetingService.deleteMeetingUserByUserIdAndCompanyId((String) userCompany.get("userId"), companyId);

				List<Map<String, Object>> userInfoList = userInfoService.selectUserInfoByCompanyId(companyId);
				if (userInfoList.size() > 0) {
					//设置列表第一个用户为默认联系人
					Map<String, Object> companyMap = new HashMap<>();
					Map<String, Object> user = userService.selectUserById((String) userInfoList.get(0).get("userId"));
					companyMap.put("id", companyId);
					companyMap.put("contactUserId", user.get("id"));
					companyMap.put("contactName", user.get("uname"));
					companyMap.put("contactPhone", StringUtils.isBlank((String) user.get("mobile")) ? (String) user.get("phone") : (String) user.get("mobile"));
					companyServie.updateCompanyInfoById(companyMap);
				} else {
					//删除企业默认联系人
					Map<String, Object> companyMap = new HashMap<>();
					companyMap.put("id", companyId);
					companyMap.put("contactUserId", "");
					companyMap.put("contactName", "");
					companyMap.put("contactPhone", "");
					companyServie.updateCompanyInfoById(companyMap);
				}
				companyServie.updateInfoScore(companyId);
				solrService.updateCompanyIndex(companyId);
				userDynamicService.addUserDynamic(loginUserId, companyId, "", "删除", "删除了员工" + userMap.get("uname"), 0,null,null,null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;

	}



}
