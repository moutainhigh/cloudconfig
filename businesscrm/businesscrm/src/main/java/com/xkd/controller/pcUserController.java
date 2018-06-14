package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.Operate;
import com.xkd.model.RedisTableKey;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.Base64;
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
	private UserService userService;


	@Autowired
	private SysUserOperateService sysOperateService;
	@Autowired
	private SysUserOperateService sysUserOperateService;
	@Autowired
	private StatisticService statisticService;

	@Autowired
	UserDataPermissionService userDataPermissionService;

	@Autowired
	DepartmentService departmentService;


	@Autowired
	OperateCacheService operateCacheService;
	

	@Autowired
	RedisCacheUtil redisCacheUtil;

	/**
	 * 查询用户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "分页检索用户")
	@ResponseBody
	@RequestMapping(value = "/selectPcUsersByContent",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter selectPcUsersByContent(HttpServletRequest req,HttpServletResponse rsp,
												   @ApiParam(value = "检索值，包括姓名，手机，邮箱")  @RequestParam(required = false) String userContent,
												   @ApiParam(value = "当前页",required = true) @RequestParam(required = true)  String currentPage,
												   @ApiParam(value = "每页多少条",required = true)  @RequestParam(required = true) String pageSize){
	
		


		if(StringUtils.isBlank(pageSize) || StringUtils.isBlank(currentPage)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		String contentStr = "";
		if(StringUtils.isNotBlank(userContent)){
			
			contentStr = "and (u.uname like '%"+userContent+"%' or u.mobile like '%"+userContent+"%' or u.email like '%"+userContent+"%')";
		}
		
		List<Map<String,Object>> maps = null;
		Integer total = null;
		
		try {
			
			maps = userService.selectUsersByContent(contentStr,currentPageInt,pageSizeInt);
			
			total = userService.selectUsersByContentCount(contentStr);
			
			for(Map<String,Object> map : maps){
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Date createDate = map.get("createDate") == null?null:(Date)map.get("createDate");
				Date updateDate = map.get("updateDate") == null?null:(Date)map.get("updateDate");
				
				if(createDate != null){
					
					String createDateStr = sf.format(createDate);
					
					map.put("createDateStr", createDateStr);
				}
				
				if(updateDate != null){
					
					String updateDateStr = sf.format(updateDate);
					
					map.put("updateDateStr", updateDateStr);
				}
				
				
				String createBy = map.get("createdBy") == null?null:(String)map.get("createdBy");
				String updateBy = map.get("updatedBy") == null?null:(String)map.get("updatedBy");
				
				Map<String, Object> mmap = null;
				
				if(StringUtils.isNotBlank(createBy)){
					
					mmap = userService.selectUserById(createBy);
					
					if(mmap != null){
						
						String name = (String)mmap.get("name");
						map.put("createByName",name);
					}
				}
				
				if(StringUtils.isNotBlank(updateBy)){
					
					mmap = userService.selectUserById(updateBy);
					
					if(mmap != null){
						
						String name = (String)mmap.get("name");
						map.put("updateByName",name);
					}
				}
			}
			
		} catch (Exception e) {
			
			log.error("异常栈:",e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		
		if(total != null){
			
			responseDbCenter.setTotalRows(total.toString());
			
		}else{
			
			responseDbCenter.setTotalRows("0");
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 根据id查询用户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "根据Id查询用户详情")
	@ResponseBody
	@RequestMapping(value = "/selectPcUsersById",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter selectPcUsersById(HttpServletRequest req,HttpServletResponse rsp,
											  @ApiParam(value = "用户Id ",required = true)  @RequestParam(required = true) String id){
	
		

		if(StringUtils.isBlank(id)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		Map<String,Object> map = null;
		
		try {
			
			map = userService.selectUserById(id);
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(map);
		
		return responseDbCenter;
	}
	
	
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
		
	    List<String> idList=Arrays.asList(iids);

		

		String encodeRepeatPassWord = Base64.encode("a123456");
		
		
		try {
			
			userService.repeatPcUserPasswordsByIds(idList,encodeRepeatPassWord,loginUserId);
			
		} catch (Exception e) {
			
			log.error("异常栈:",e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 保存用户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "添加或修改PC用户信息")
	@ResponseBody
	@RequestMapping(value = "/changePcUser", method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter changePcUser(HttpServletRequest req,HttpServletResponse rsp,
										 @ApiParam(value = "操作状态  insert表示插入 update 表示更新",required = true)  @RequestParam(required = true) String flag,
										 @ApiParam(value = "用户Id 修改时需要传，添加时不用传" ,required = false)  @RequestParam(required = false) String id,
										 @ApiParam(value = "姓名" ,required = false) @RequestParam(required = false) String name,
										 @ApiParam(value = "角色Id" ,required = false) @RequestParam(required = false) String roleId,
										 @ApiParam(value = "邮箱" ,required = false) @RequestParam(required = false) String email,
										 @ApiParam(value = "手机" ,required = false) @RequestParam(required = false) String mobile,
										 @ApiParam(value = "性别 男，女",required = false) @RequestParam(required = false) String sex,
										 @ApiParam(value = "部门Id" ,required = false) @RequestParam(required = false) String departmentId,
										 @ApiParam(value = "头衔Id",required = false) @RequestParam(required = false)  String titleId,
										 @ApiParam(value = "入职时间" ,required = false) @RequestParam(required = false) String enrollDate,
										 @ApiParam(value = "职位" ,required = false) @RequestParam(required = false) String titleValue


	) throws Exception{




		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
 		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);



		//传过来的密码已经是加密的了
		String token = req.getHeader("token");
		
		if(StringUtils.isBlank(flag) || StringUtils.isBlank(name) || StringUtils.isBlank(email) ||
				(StringUtils.isBlank(id) && "update".equals(flag)) || StringUtils.isBlank(roleId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		Map<String,Object> map = new HashedMap<>();

		Map<String,Object> existUser=userService.selectUserByOnlyMobile(mobile);
		if (existUser!=null) { //如果库中已经有，但删除状态为删除的记录，则直接更新
			
 
 
			if (!existUser.get("id").equals(id)&&0==(Integer)existUser.get("status")&&(1==(Integer)existUser.get("platform"))) {   //如果存在的用户不是删除状态，则报错给前台
				return ResponseConstants.MOBILE_EXIST;
			}

 	        flag="update";
	        map.put("id", existUser.get("id"));
			map.put("platform",1);
	        map.put("status", "0");  //恢复为未删除状态
		}
		
		
		if("0".equals(sex)){
			
			sex = "男";
			
		}else if("1".equals(sex)) {
			
			sex = "女";
		}
		
		map.put("uname",name);
		map.put("sex",sex);
		map.put("platform",1);
		map.put("email",email);
		map.put("mobile", mobile);
		map.put("enrollDate", enrollDate);



		if (StringUtils.isBlank(departmentId)) { 
//			map.put("departmentId", 2); //默认在部门Id为2下
			return ResponseConstants.DEPARTMENT_NOT_NULL;
		}else{
			map.put("departmentId", departmentId);
			/**
			 * 查询某一个部门对应的公司
			 */
			Map<String,Object> companyDepartMap= departmentService.getCompanyIdByDepartmentId(departmentId);
			map.put("pcCompanyId",companyDepartMap.get("id"));

		}
		map.put("titleId", titleId);
		
		if(StringUtils.isNotBlank(roleId)){
			
			map.put("roleId",roleId);
		}
		
		
		try {
			
			if("insert".equals(flag)){
				map.put("id", UUID.randomUUID().toString());
				map.put("createDate",new Date());

				Map<String, Object> userMaps = null;
				try {
					
					userMaps = userService.selectUserByEmail(email,1);
					
				} catch (Exception e) {
					
					return ResponseConstants.EMAIL_EXIST;
				}
				
				if(userMaps != null ){
					
					return ResponseConstants.EMAIL_EXIST;
				}
				
				
				Random random = new Random();
				
				char[] stonesEnglish = {'a','b','c','d','e','f','g','h','i',
				        'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
				
				char[] stones = {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i',
			        'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
				
				StringBuffer sb = new StringBuffer();
			
				sb.append(stonesEnglish[random.nextInt(stonesEnglish.length)]);
				
				for(int i = 0 ;i<5;i++){
					
					sb.append(stones[random.nextInt(stones.length)]);
				}
				
				String password = sb.toString();
				
				String enPassWord = new String(Base64.encode(password));
				
				map.put("password",enPassWord);
				
				if(StringUtils.isNotBlank(loginUserId)){
					
					map.put("createdBy", loginUserId);
				}
				map.put("titleValue",titleValue);
				userService.insertDcUser(map);
				userService.insertDcUserDetail(map);






				String href = PropertiesUtil.USER_LOGIN_HREF;
				
				String hrefStr = "<a href='"+href+"'>&nbsp;点此处登录&nbsp;</a>";
				String content = "尊敬的  "+name+"用户，感谢您注册小蝌蚪CRM。<br>"+
			    				"您的账号是:"+email+"<br>"+
			    				"您的密码是:"+password+"<br>"+
			    				"本邮件为系统自动邮件，请勿回复。"+
			    				"<br>感谢您对小蝌蚪的支持！请访问登录界面"+hrefStr;
				
			   MailUtils.send(email, "小蝌蚪CRM注册", content);
			   
				
			}else{


				/**
				 * 如果非超级管理员去修改超级管理员的角色是不允许的
				 */
				if (StringUtils.isNotBlank(roleId)){
					Map<String,Object> toBeModifiedUser=userService.selectUserById(id);
					if (toBeModifiedUser!=null) {
						if ("1".equals(toBeModifiedUser.get("roleId")) && !"1".equals(loginUserMap.get("roleId"))) {
							return ResponseConstants.NOT_AUTHORIZED_TO_MODIFY_SUPERROLE;
						}
					}

				}





				if (StringUtils.isNotBlank(email)) {
					//如果email已经存在了，则不允许添加
					Map<String, Object> userMap = userService.selectUserByEmail(email, 1);
					if (userMap != null) {
						String idExists = (String) userMap.get("id");
						if (userMap != null && userMap.size() > 0 && !idExists.equals(id)) {
							return ResponseConstants.EMAIL_EXIST;
						}
					}
				}

				if (existUser==null) {//如果不存在手机码的用户

					
					map.put("updatedBy", loginUserId);
					map.put("updateDate", new Date());
					map.put("id",id);
				}

				map.put("titleValue",titleValue);
				userService.updateDcUser(map);
				userService.updateDcUserDetail(map);
				//清 除旧的数据权限
				userDataPermissionService.clearCacheData((String)map.get("id"));

			}


			//清除权限缓存
			operateCacheService.clear(token);



		} catch (Exception e) {
			
			log.error("异常栈:",e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 批量删除用户
	 * @param req
	 * @param rsp
	 * @return
	 */

	@ApiOperation(value = "批量删除用户")
	@ResponseBody
	@RequestMapping(value = "/deletePcUserByIds",method =  { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter deletePcUserByIds(HttpServletRequest req,HttpServletResponse rsp,
											  @ApiParam(value = "ids  多个以逗号分隔",required = true)  @RequestParam(required = true)  String ids) throws Exception{
	
		

		if(StringUtils.isBlank(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] iids = ids.split(",");
		
			
 		List<String> userIdList = new ArrayList<>();
		
		for(int i = 0;i<iids.length;i++){
			userIdList.add(iids[i]);
		}

		try {
			
			userService.deleteUserByIds(userIdList);


			/**
			 * 删除用户权限
			 */
		   sysOperateService.deleteByUserIds(userIdList);

			
		} catch (Exception e) {
			
			log.error("异常栈:",e);
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
												 @ApiParam(value = "手机",required = false) @RequestParam(required = false) String mobile) throws Exception{
	

		if(StringUtils.isBlank(id)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("id",id);
		map.put("userLogo",userLogo);
		map.put("uname",uname);
		map.put("email",email);
		map.put("mobile",mobile);
		
		try {

			Integer num = userService.updatePcUserInfoById(map);
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
			log.error("异常栈:",e);
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
		String token = req.getHeader("token");

		
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

			req.getSession().removeAttribute(token);;

			//用于单点登录，将用户
			redisCacheUtil.delete(map.get("id")+"");
			redisCacheUtil.setCacheObject(token,map.get("id")+"",RedisTableKey.EXPIRE);



			
		} catch (Exception e) {
			
			log.error("异常栈:",e);
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
			log.error("异常栈:",e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}







		/**
		 * 根据员工名称模糊查询员工
		 * @param userName
		 * @return
		 */
		@ApiOperation(value = "根据名称模糊查询员工")
			@ResponseBody
			@RequestMapping(value = "/searchPcUserbyName",method =  { RequestMethod.GET, RequestMethod.POST })
			public ResponseDbCenter searchPcUserbyName(HttpServletRequest req,  @ApiParam(value = "员工姓名",required = false) @RequestParam(required = false)  String userName){
				ResponseDbCenter res = new ResponseDbCenter();
				 try {
					 // 当前登录用户的Id
					 String loginUserId = (String) req.getAttribute("loginUserId");
					 Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
					 List<String > departmentIdList=new ArrayList<>();
					 if ("1".equals(loginUserMap.get("roleId"))){
						 departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList("1", loginUserId);
					 }else {
						 departmentIdList=userDataPermissionService.getDataPermissionDepartmentIdList((String)loginUserMap.get("pcCompanyId"),loginUserId);
					 }

					 List<Map<String,Object>> list= userService.searchPcUserbyName(userName,departmentIdList);
 					res.setResModel(list);
					 return res;
				} catch (Exception e) {
					log.error("异常栈:",e);
					return ResponseConstants.FUNC_SERVERERROR;
				}	 
			}
			
			
			/**
			 * 根据名称模糊查询没有头衔的用户
			 * @return
			 */
			  @ApiOperation(value = "根据员工姓名检索没有头衔的员工")
				@ResponseBody
				@RequestMapping(value = "/searchUserNoTitleByName",method =  { RequestMethod.GET, RequestMethod.POST })
				public ResponseDbCenter searchUserNoTitleByName(HttpServletRequest req,
																@ApiParam(value = "用户名称",required = false) @RequestParam(required = false) String userName,
																@ApiParam(value = "当前页",required = true) @RequestParam(required = true) String currentPage,
																@ApiParam(value = "每页多少条" ,required = true) @RequestParam(required = true) String pageSize){

					if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
						currentPage = "1";
						pageSize = "10";
					}

				    int  pageSizeInt = Integer.parseInt(pageSize);
				    int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

					// 当前登录用户的Id
				 	 String loginUserId = (String) req.getAttribute("loginUserId");
					ResponseDbCenter res = new ResponseDbCenter();
					 try {
						List<Map<String,Object>> list= userService.searchUserNoTitleByName(userName,currentPageInt,pageSizeInt,loginUserId);
						Integer total = userService.searchUserNoTitleByNameTotal(userName,loginUserId);
	 					res.setResModel(list);
	 					res.setTotalRows(total.intValue()+"");
	 					return res;
					} catch (Exception e) {
						log.error("异常栈:",e);
						return ResponseConstants.FUNC_SERVERERROR;
					}	 
				}


	/**
	 *
	 * @param userIds json格式的Id列表
	 * 如 ["1","2"]
	 * @param departmentId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "批量修改员工部门")
				@ResponseBody
				@RequestMapping(value = "/changeDepartment",method =  { RequestMethod.GET, RequestMethod.POST })
				public ResponseDbCenter changeDepartment(
			@ApiParam(value = "员工Id json格式组成 的数组 如[\"1\",\"2\"]",required = true) @RequestParam(required=true)  String userIds,
			@ApiParam(value = "部门Id" ,required = true)@RequestParam(required=true) String departmentId) throws Exception{
 					 try {
						 List<String> userIdList = JSON.parseObject(userIds, new TypeReference<List<String>>() {
						 });

						 /**
						  * 客户公司信息
						  */
						Map<String,Object> customerMap= departmentService.getCompanyIdByDepartmentId(departmentId);

						 if (customerMap==null){
							 return ResponseConstants.FUNC_SERVERERROR;
						 }


						 userService.changeDepartmentByUserIds(userIdList, departmentId,(String)customerMap.get("id"));

						 userDataPermissionService.deleteUserDataPermissionByUserIds(userIdList);

						 /**
						  * 清除用户的权限缓存
						  */
						 for (int i = 0; i <userIdList.size(); i++) {
							 userDataPermissionService.clearCacheData(userIdList.get(i));
						 }

 						 return ResponseConstants.SUCCESS;
					} catch (Exception e) {
						log.error("异常栈:",e);
						throw new GlobalException(ResponseConstants.FUNC_SERVERERROR) ;
					}	 
				}


		@ApiOperation(value = "加载权限")
				@ResponseBody
				@RequestMapping(value = "/loadPermission",method =  { RequestMethod.GET, RequestMethod.POST })
				public ResponseDbCenter loadPermission(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
					String loginUserId = (String) req.getAttribute("loginUserId");
					List<Operate> list=null;
 					 try {
						list= userService.selectAllOperateByUSerId(loginUserId);
 						 ResponseDbCenter responseDbCenter= ResponseConstants.SUCCESS;
 						 responseDbCenter.setResModel(list);
 						 return responseDbCenter;
					} catch (Exception e) {
						log.error("异常栈:",e);
						throw new GlobalException(ResponseConstants.FUNC_SERVERERROR) ;
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
	@ApiOperation(value = "更新用户权限")
				@ResponseBody
				@RequestMapping(value = "/updateUserOperate", method = { RequestMethod.GET, RequestMethod.POST })
				public ResponseDbCenter updateUserOperate(HttpServletRequest req, HttpServletResponse rsp,
														  @ApiParam(value = "权限Id,json格式的数组 [\"1\"，\"2\"]" ,required = true)@RequestParam(required = true) String operateIds,
														  @ApiParam(value = "用户Id",required = true) @RequestParam(required = true) String userId)
								throws Exception {
					try {
						
						String token = req.getHeader("token");
						//删除旧的用户权限关系
						sysUserOperateService.deleteByUserId(userId);

						if (!StringUtils.isBlank(operateIds)) {
							List<String> list = JSON.parseObject(operateIds, new TypeReference<List<String>>() {
							});


							List<Map<String, Object>> mapList = new ArrayList<>();
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> map = new HashMap<>();
								map.put("id", UUID.randomUUID().toString());
								map.put("userId", userId);
								map.put("operateId", list.get(i));
								mapList.add(map);
							}
							if (list.size()>0) {
								//插入用户权限关系
								sysUserOperateService.insertList(mapList);
							}

							
							
							//清除权限缓存
							operateCacheService.clear();
						}
					} catch (Exception e) {
						log.error("异常栈:",e);
						throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
					}
					ResponseDbCenter responseDbCenter = new ResponseDbCenter();
					return responseDbCenter;
				}


	/**
	 * 获取首页数据
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "获取首页数据")
	@ResponseBody
	@RequestMapping(value = "/getHomeData", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseDbCenter getHomeData(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try {

			String token = req.getHeader("token");
			String loginUserId = (String) req.getAttribute("loginUserId");
			responseDbCenter.setResModel(statisticService.getHomeData(loginUserId,token));
		} catch (Exception e) {
			log.error("异常栈:",e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;
	}





}
