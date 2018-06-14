package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.Operate;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Api(description = "PC端用户的增删改查")
@Controller
@RequestMapping("/pcUser")
@Transactional
public class pcUserController  extends BaseController{

	@Autowired
	private DC_PC_UserService pcUserService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;


	@Autowired
	private SysUserOperateService sysOperateService;
	@Autowired
	private SysUserOperateService sysUserOperateService;




	@Autowired
	DepartmentService departmentService;

	@Autowired
	ApiCallFacadeService apiCallFacadeService;



	

	
	
	/**
	 * 重置密码，将密码设置成123
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "重置用户密码")
	@ResponseBody
	@RequestMapping(value = "/repeatPcUserPasswordsByIds",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter repeatPcUserPasswordsByIds(HttpServletRequest req,HttpServletResponse rsp ,
													   @ApiParam(value = "Ids ，多人以逗号分隔",required = true) @RequestParam(required = true) String ids) throws Exception{


		String loginUserId = (String) req.getAttribute("loginUserId");

		if(StringUtils.isBlank(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] iids = ids.split(",");
		
			
		String idsString = "";
		for(int i = 0;i<iids.length;i++){
			
			idsString += iids[i]+",";
		}
		
		idsString = "id in ('" + idsString.substring(0, idsString.length()-1) +"')";
		
		String encodeRepeatPassWord = EncryptUtil.encryptPassword("a123456");
		
		
		try {
			
			userService.repeatPcUserPasswordsByIds(idsString,encodeRepeatPassWord,loginUserId);
			for (int i = 0; i <iids.length ; i++) {
				apiCallFacadeService.updateUserCallFacede(null,encodeRepeatPassWord,null,iids[i],null,null,null,null,null,null,null);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	


	
	/**
	 * 更改用户信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "更新用户头标，用户名，邮箱，手机")
	@ResponseBody
	@RequestMapping(value = "/updatePcUserInfoById",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter updatePcUserInfoById(HttpServletRequest req,HttpServletResponse rsp,
												 @ApiParam(value = "id",required = true) @RequestParam(required = true) String id,
												 @ApiParam(value = "头像Logo地址",required = false) @RequestParam(required = false) String userLogo,
												 @ApiParam(value = "用户姓名",required = false) @RequestParam(required = false) String uname,
												 @ApiParam(value = "邮箱",required = false) @RequestParam(required = false) String email,
												 @ApiParam(value = "手机",required = false) @RequestParam(required = false) String mobile,
												 @ApiParam(value = "性别 男，女",required = false) @RequestParam(required = false) String sex,
												 @ApiParam(value = "入职时间" ,required = false) @RequestParam(required = false) String enrollDate,
												 @ApiParam(value = "工龄" ,required = false) @RequestParam(required = false) String workAge,
												 @ApiParam(value = "学历" ,required = false) @RequestParam(required = false) String degree,
												 @ApiParam(value = "学历证书编号" ,required = false) @RequestParam(required = false) String qualification,
												 @ApiParam(value = "毕业证院校" ,required = false) @RequestParam(required = false) String graduation,
												 @ApiParam(value = "专业" ,required = false) @RequestParam(required = false) String major,
												 @ApiParam(value = "特长" ,required = false) @RequestParam(required = false) String strongpoint,
												 @ApiParam(value = "毕业证图片" ,required = false) @RequestParam(required = false) String graduationImg,
												 @ApiParam(value = "职称" ,required = false) @RequestParam(required = false) String professionTitle,
												 @ApiParam(value = "专业证书编号" ,required = false) @RequestParam(required = false) String professionNo,
												 @ApiParam(value = "专业证书图片" ,required = false) @RequestParam(required = false) String professionImg
												 ) throws Exception{





		if(StringUtils.isBlank(id)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("id",id);
		map.put("userLogo",userLogo);
		map.put("uname",uname);
		map.put("email",email);
		map.put("mobile",mobile);
		map.put("sex",sex);
		map.put("enrollDate",enrollDate);
		map.put("workAge",workAge);
		map.put("degree",degree);
		map.put("qualification",qualification);
		map.put("graduation",graduation);
		map.put("major",major);
		map.put("strongpoint",strongpoint);
		map.put("graduationImg",graduationImg);
		map.put("professionTitle",professionTitle);
		map.put("professionNo",professionNo);
		map.put("professionImg",professionImg);
		try {

			Integer num = userService.updatePcUserInfoById(map);
			Map<String,Object>  user=userService.selectUserById(id);
			apiCallFacadeService.updateUserCallFacede(mobile,null,(String)user.get("roleId"),(String)user.get("id"),mobile,mobile,email,null,null,null,null);
			if(num.intValue() == 0){
				return ResponseConstants.ACTIVE_ERROR;
			}else if(num.intValue() == 2){
				return ResponseConstants.FUNC_USER_MOBILEHASREGISTER;
			}else if(num.intValue() == 3){
				return ResponseConstants.EMAIL_EXIST;
			}else{
				ResponseDbCenter responseDbCenter = new ResponseDbCenter();
				return responseDbCenter;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	
	/**
	 * 用户修改密码
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "修改用户密码")
	@ResponseBody
	@RequestMapping(value = "/updateUserPasswordById",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter updateUserPasswordById(HttpServletRequest req,HttpServletResponse rsp,
												   @ApiParam(value = "用户ID",required = true) @RequestParam(required = true) String id,
												   @ApiParam(value = "旧密码",required = true) @RequestParam(required = true) String oldPassword,
												   @ApiParam(value = "新密码",required = true) @RequestParam(required = true) String password,
												   @ApiParam(value = "重复密码",required = true) @RequestParam(required = true) String repeatPassword) throws Exception{
	
		//传过来的密码已经是加密的了
		String token = req.getParameter("token");

		
		if(StringUtils.isBlank(id) || StringUtils.isBlank(password) || StringUtils.isBlank(repeatPassword)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		if(!password.equals(repeatPassword)){
			
			return ResponseConstants.PASSWORD_NOT_SAME;
		}
		
		try {
			
			Map<String, Object> map = userService.selectUserById(id);
			
 			String upassword = (String)map.get("password");
			
			if(!oldPassword.equals(upassword)){
				return ResponseConstants.PASSWORD_ERROR;
			}

			userService.updateUserPasswordById(id,password);
			apiCallFacadeService.updateUserCallFacede(null,password,null,id,null,null,null,null,null,null,null);

			req.getSession().removeAttribute(token);;

			//用于单点登录，将用户
			MapCacheManager.removeCacheMapByValue(map.get("id")+"");
			MapCacheManager.putCacheMap(token, map.get("id")+"");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}


























	/**
	 *
	 * @param departmentId
	 * @param userName
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@ApiOperation(value = "分页检索查询部门下的用户")
	@ResponseBody
	@RequestMapping(value = "/selectUserByDepartmentId",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter selectUserByDepartmentId(HttpServletRequest req,  String departmentId,
													 @ApiParam(value = "用户姓名",required = false) @RequestParam(required = false) String userName,
													 @ApiParam(value = "每页多少条记录",required = true)@RequestParam(required=true)Integer pageSize,
													 @ApiParam(value = "当前页码",required = true)@RequestParam(required=true)Integer currentPage){
		ResponseDbCenter res = new ResponseDbCenter();
		try {
			// 当前登录用户的Id
			String loginUserId = (String) req.getAttribute("loginUserId");

			List<Map<String,Object>> list= userService.selecUserByDepartmentId(departmentId,userName,pageSize,currentPage,loginUserId);
			Integer total=userService.selecTotalUserByDepartmentId(departmentId,userName,loginUserId);
			res.setTotalRows(total+"");
			res.setResModel(list);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
			









	/**
	 *
	 * @param req
	 * @param rsp
	 * @param operateIds   json格式的数组
	 * ["1","2"]
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "临时接口")
	@ResponseBody
	@RequestMapping(value = "/aaa", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter aaa(HttpServletRequest req, HttpServletResponse rsp
											  )
			throws Exception {
		try {
		    List<Map<String,Object>> allUsers=userService.selectAllUsers();

 			for (int i = 0; i <allUsers.size() ; i++) {
				Map<String,Object> user=allUsers.get(i);
				try {
					apiCallFacadeService.registerUserCallFacede((String)user.get("mobile"), (String)user.get("password"), (String) user.get("roleId"), (String) user.get("id"), (String) user.get("mobile"), (String)user.get("email"));

				}catch (Exception ee){
					ee.printStackTrace();
				}
			}
 			return new ResponseDbCenter();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
 	}





}
