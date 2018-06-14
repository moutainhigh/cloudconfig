package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.DC_PC_User;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;


@Controller
@RequestMapping("/user")
@Transactional
public class UserController  extends BaseController{
	

	
	@Autowired
    private DC_PC_UserService pcUserService ;
	@Autowired
	private CustomerService customerService;
	@Autowired
	DC_UserService userService;
	@Autowired
	UserService loginUserService;
	@Autowired
	UserService newUserService;
	@Autowired
	private UserService userService1;
	@Autowired
	private RedisCacheUtil redisCacheUtil;
	@Autowired
	private  ApiCallFacadeService apiCallFacadeService;

	String ASSET_URL = PropertiesUtil.ASSET_URL;

	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月24日 
	 * @功能描述:根据token获得redis中的userId再根据userId获得user对象
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUserByToken")
	public ResponseDbCenter selectUserByToken(HttpServletRequest req,HttpServletResponse rsp){
		String token  = req.getParameter("token");
		
		if(StringUtils.isBlank(token)){

			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;
		try {

			userId = req.getSession().getAttribute(token).toString();

		} catch (Exception e) {

			System.out.println(e);
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}
		
        if(StringUtils.isBlank(userId)){
        	
        	return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
        }
        
        Map<String, Object> userResult = loginUserService.selectUserById(userId);
        
        if(userResult == null || userResult.size() == 0){
        	
        	return ResponseConstants.USER_NOTEXIST;
        }
        
        req.getSession().setAttribute(token, userId);

        
        
		ResponseDbCenter ResponseDbCenter = new ResponseDbCenter();
		userResult.put("token",token);
		ResponseDbCenter.setResModel(userResult);
		return ResponseDbCenter;
		
	}





	/**
	 *
	 * @author: xiaoz
	 * @2017年5月24日
	 * @功能描述:根据token获得redis中的userId再根据userId获得user对象
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "查询用户详情")
	@ResponseBody
	@RequestMapping(value = "/selectUserById",method = RequestMethod.POST)
	public ResponseDbCenter selectUserById(HttpServletRequest req,HttpServletResponse rsp,
										   @ApiParam(value = "用户Id", required = false) @RequestParam(required = false) String userId){


		Map<String, Object> userResult = loginUserService.selectUserById(userId);

		if(userResult == null || userResult.size() == 0){

			return ResponseConstants.USER_NOTEXIST;
		}
		ResponseDbCenter ResponseDbCenter = new ResponseDbCenter();
 		ResponseDbCenter.setResModel(userResult);
		return ResponseDbCenter;

	}


	@ResponseBody
	@RequestMapping("/selectUserInfo")
	public ResponseDbCenter selectUserInfo(HttpServletRequest req,HttpServletResponse rsp){
		ResponseDbCenter ResponseDbCenter = new ResponseDbCenter();
		String uname = req.getParameter("uname");
		try{
			List<HashMap<String, Object>> maps = pcUserService.selectUserInfo(uname);
			ResponseDbCenter.setResModel(maps);
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		return ResponseDbCenter;
	}
	
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:发送验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userRegSendSms")
	public ResponseDbCenter userRegSendSms(HttpServletRequest req){
		System.out.println("--------------发送短信----------------");
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String tel = req.getParameter("tel");
		
		if(StringUtils.isBlank(tel)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		
		//req.getSession().setAttribute("code" +tel, "1234");
		String code = SmsApi.sendSms(tel);
		req.getSession().setAttribute("code"+tel, code);
		System.out.println("Us----------------key="+tel);
		String telCode = (String) req.getSession().getAttribute("code"+tel);
		System.out.println("UserController.session()-----set-----------"+req.getSession().getId());
		System.out.println("发送短信code = "+telCode);
	    
		return res;
	}

	/**
	 *
	 * @author: gaodd
	 * @2017年3月22日
	 * @功能描述:发送验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userRegSendSmsInTicket")
	public ResponseDbCenter userRegSendSmsInTicket(HttpServletRequest req){
		System.out.println("--------------发送短信----------------");
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String tel = req.getParameter("tel");

		if(StringUtils.isBlank(tel)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}

		//req.getSession().setAttribute("code" +tel, "1234");
		/*
		sendSms(String moblie,String toContent)
		 */
		String code = SmsApi.sendSmsInTicket(tel);
		req.getSession().setAttribute("code"+tel, code);
		System.out.println("Us----------------key="+tel);
		String telCode = (String) req.getSession().getAttribute("code"+tel);
		System.out.println("UserController.session()-----set-----------"+req.getSession().getId());
		System.out.println("发送短信code = "+telCode);

		return res;
	}



	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:用户登录
	 * @return
	 */
	@ApiOperation(value = "登录")
	@ResponseBody
	@RequestMapping(value = "/pcLogin" ,method = RequestMethod.POST)
	public ResponseDbCenter pcLogin(HttpServletRequest req, HttpServletResponse rsp,
									@ApiParam(value="2:服务端，3技师端，4客户端，如果不传就是pc端登录",required = false) @RequestParam(required = false) String appFlag,
									@ApiParam(value="密码，必填",required = false) @RequestParam(required = false) String password,
									@ApiParam(value="电话，必填",required = false) @RequestParam(required = false) String tel,
									@ApiParam(value="验证码",required = false) @RequestParam(required = false) String code){

		/*
			PC端要传验证码
		 */

		String a = "";

		if(StringUtils.isBlank(tel) || StringUtils.isBlank(password) || (StringUtils.isBlank(appFlag) && StringUtils.isBlank(code))){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		if(StringUtils.isNotBlank(appFlag) && "234".indexOf(appFlag) < 0){
			return ResponseConstants.ILLEGAL_PARAM;
		}

		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String strCode = (String) req.getSession().getAttribute("strCode");

		if(StringUtils.isBlank(appFlag) && code != null && !code.equals(strCode)){
			return ResponseConstants.TEL_CODE_ERROR;
		}
		if(StringUtils.isBlank(tel) && StringUtils.isBlank(password)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		Map<String, Object> userMap = null;
		if(tel.contains("@")){
			userMap = newUserService.selectUserByEmail(tel,1);
			if(userMap == null || userMap.size() == 0){
				return ResponseConstants.USER_ERROR_SIGN;
			}
		}else{
			//PC端登陆
			if(StringUtils.isBlank(appFlag)){
				userMap = newUserService.selectUserByMobile(tel,1);
			}else{
				//APP端登陆
				userMap = newUserService.selectUserByMobileAndappFlag(tel,1,appFlag);
			}
		}
		
		//password = password == "" || password == null ? "a654123":new String(MD5Code.decode(password));
		if(userMap == null || userMap.size() == 0){
			return ResponseConstants.USER_ERROR_SIGN;
		}else if(StringUtils.isNotBlank(password)&&!password.equals(userMap.get("password")+"")){
			return ResponseConstants.NAME_PWD_ERROR_WJ;
		}

		String token = UUID.randomUUID().toString();
		Map<String, String> map = new HashMap<>();
		map.put("token",token);
		map.put("userId",userMap.get("id").toString());
		map.put("uname",userMap.get("uname")+"");
		
		
		/*
		 * 一个用户多台电脑登录，让其他用户下线，只允许当前用户下线
		 */
		req.getSession().removeValue(userMap.get("id")+"");
		req.getSession().setAttribute(token,userMap.get("id").toString());
		req.getSession().setAttribute(token+"uname",userMap.get("uname"));//不要删除session里面存的uname
		req.getSession().setAttribute("user",userMap);

		/*
		 * 一个用户多处登录，让其他用户下线，只允许当前用户在线上
		 * 用于单点登录，将用户ID存入到redis中,删除旧的
		 */
		/*String oldToken = redisCacheUtil.getCacheObject(userMap.get("id")+"")+"";
		redisCacheUtil.deleteCacheByKey(oldToken);*/
		//加入新的
		redisCacheUtil.setCacheObject(token,userMap.get("id")+"");
		redisCacheUtil.setCacheObject(userMap.get("id")+"",token);

		//记录登录的IP和地址
        newUserService.saveLastLoginIp(req.getRemoteHost(),(String)userMap.get("id"));

		req.getSession().setMaxInactiveInterval(60*60*5);//以秒为单位
		res.setResModel(map);
		return res;
	}


	/**
	 *
	 * @author: gaodd
	 * @2018年3月3日
	 * @功能描述:app端用户注册
	 * @return
	 *
	 */
	@ApiOperation(value = "app端用户注册")
	@ResponseBody
	@RequestMapping(value = "/appRegister" , method = RequestMethod.POST)
	public ResponseDbCenter appRegister(HttpServletRequest req, HttpServletResponse rsp,
									 @ApiParam(value="2:服务端，3技师端，4客户端，必传",required = false) @RequestParam(required = false) String appFlag,
									 @ApiParam(value="手机号码",required = false) @RequestParam(required = false) String tel,
									 @ApiParam(value="密码，必传",required = false) @RequestParam(required = false) String password,
										@ApiParam(value="确认密码，必传",required = false) @RequestParam(required = false) String passwordRepeat,
									 @ApiParam(value="验证码",required = false) @RequestParam(required = false) String code


	) throws Exception{



		if(StringUtils.isBlank(tel) || StringUtils.isBlank(password) ||
				StringUtils.isBlank(passwordRepeat) || StringUtils.isBlank(appFlag) || StringUtils.isBlank(code)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}

		if(!password.equals(passwordRepeat)){
			return ResponseConstants.PASSWORD_NOT_SAME;
		}

		String existsCode = MapCacheManager.getCacheMapFiveMinute(tel);
		//String existsCode1 = MapCacheManager.getCacheMap(tel);

		if(StringUtils.isBlank(existsCode)){
			return ResponseConstants.USER_REGISTER_CODE_OUTOT_DATE;
		}else if(!existsCode.equals(code)){
			return ResponseConstants.TEL_CODE_ERROR;
		}


		//移除数据
		MapCacheManager.removeCacheMap(tel);


		Map<String, Object> userMap = null;
		Map<String, Object> emailUserMap = null;

		userMap = newUserService.selectUserByMobile(tel,1);
		if(userMap != null && userMap.size() > 0){
			return ResponseConstants.MOBILE_EXIST;
		}
		/*emailUserMap = newUserService.selectUserByEmail(email,1);
		if(emailUserMap != null && emailUserMap.size() > 0){
			return ResponseConstants.EMAIL_EXIST;
		}*/

		try {

			String pw = EncryptUtil.encryptPassword(password);

			Map<String,Object> userParamMap = new HashedMap();
			String id = UUID.randomUUID().toString();
			String idd = id.replace("-","");
			userParamMap.put("id",idd);
			userParamMap.put("roleId",appFlag);
			userParamMap.put("password",pw);
			userParamMap.put("mobile",tel);
			userParamMap.put("useStatus","0");
			userParamMap.put("status",0);
			userParamMap.put("platform",1);
			userParamMap.put("createdBy",id);
			userParamMap.put("createDate",new Date());
			userParamMap.put("appPushFlag",1);
			userParamMap.put("messagePushFlag",1);
			userParamMap.put("messagePushFlag",1);
			userParamMap.put("emailPushFlag",1);

			newUserService.insertDcUser(userParamMap);
			newUserService.insertDcUserDetail(userParamMap);

			/*
			 * account 15915793639
			 账户， 必填
			 pw AD05F5F2F796152092C7A00DB10EE405
			 密码密文， 必填
			 identity_type xx
			 账户类型，必填
			 user_id 2a2a50beb34c404b9cbd219b96782ca0
			 用户id， 必填
			 mob xxxx
			 电话， 选填
			 email xxxx
			 邮箱， 选填
			 */
			apiCallFacadeService.registerUserCallFacede(tel,pw,appFlag,idd,tel,"");

			ResponseDbCenter res = new ResponseDbCenter();
			return res;
		}catch (Exception e){
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

	}

	@ApiOperation(value = "app端用找回密码")
	@ResponseBody
	@RequestMapping(value = "/appRepeatPassword" , method = RequestMethod.POST)
	public ResponseDbCenter appRepeatPassword(HttpServletRequest req, HttpServletResponse rsp,
										@ApiParam(value="手机号码",required = false) @RequestParam(required = false) String tel,
										@ApiParam(value="密码，必传",required = false) @RequestParam(required = false) String password,
										@ApiParam(value="确认密码，必传",required = false) @RequestParam(required = false) String passwordRepeat,
										@ApiParam(value="验证码",required = false) @RequestParam(required = false) String code


	) throws Exception{



		if(StringUtils.isBlank(tel) || StringUtils.isBlank(password) ||
				StringUtils.isBlank(passwordRepeat)  || StringUtils.isBlank(code)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}

		if(!password.equals(passwordRepeat)){
			return ResponseConstants.PASSWORD_NOT_SAME;
		}

		String existsCode = MapCacheManager.getCacheMapFiveMinute(tel);
		//移除数据
		MapCacheManager.removeCacheMap(tel);
		if(StringUtils.isBlank(existsCode)){
			return ResponseConstants.USER_REGISTER_CODE_OUTOT_DATE;
		}else if(!existsCode.equals(code)){
			return ResponseConstants.TEL_CODE_ERROR;
		}


		Map<String, Object> userMap = newUserService.selectUserByMobile(tel,1);
		if(userMap == null && userMap.size() == 0){
			return ResponseConstants.USER_MOBILE_NO_REGISTER;
		}


		try {

			String pw = EncryptUtil.encryptPassword(password);
			Map<String,Object> userParamMap = new HashedMap();
			String userId = userMap.get("id").toString();
			userParamMap.put("id",userId);
			userParamMap.put("password",pw);
			userParamMap.put("updatedBy",userId);
			userParamMap.put("updateDate",new Date());

			newUserService.updateDcUser(userParamMap);

			/*
			 * account 2
			 账户， 选填
			 pw 1
			 密码密文， 选填
			 identity_type 1
			 账户类型， 选填
			 user_id 1
			 用户id， 必填
			 old_account 1
			 如果修改account的话，必须传这个值，否则可以不传
			 mob xxxx
			 电话，选填
			 email xxxx
			 邮箱，选填
			 conf_email
			 邮箱推送设置， 0， 1选填
			 conf_voice
			 语音推送设置， 0， 1选填
			 conf_push
			 app推送设置， 0， 1选填
			 conf_sms
			 短信推送设置， 0， 1选填
			 * @return
			 * @throws Exception
			 */
			apiCallFacadeService.updateUserCallFacede("",pw,"",
					userId,"","","","","","","");

			ResponseDbCenter res = new ResponseDbCenter();
			return res;
		}catch (Exception e){
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

	}


	

	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:验证用户是否有权限进入小系统
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkIsUser")
	public ResponseDbCenter checkIsUser(@RequestParam("token") String token){
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(false);
		return res;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:注销
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public ResponseDbCenter logout(HttpServletRequest request,@RequestParam("token") String token){
		ResponseDbCenter res = new ResponseDbCenter();
		//删除缓存的接口权限
		OperateCacheUtil.clear(token);
		request.getSession().removeAttribute(token);
		
		res.setResModel(null);
		return res;
	}
	
	/**
	 *
	 * @author: gaodd
	 * @2017年6月26日
	 * @功能描述:查询用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserInfo")
	public ResponseDbCenter getUserInfo(HttpServletRequest request,@RequestParam("token")String token){
		ResponseDbCenter res = new ResponseDbCenter();
		String uid = request.getSession().getAttribute(token).toString();
		if(StringUtils.isBlank(uid)){
			return ResponseConstants.FUNC_BP_TOKENISWRONG;
		}
		DC_PC_User uuser = pcUserService.getUserById(uid);
		uuser.setToken(token);
		res.setResModel(uuser);
		return res;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年6月26日 
	 * @功能描述:pc登录时，返回的图像验证码
	 * @return
	 */
	@RequestMapping({"authCode"})
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response,HttpSession session)
            throws IOException {
        int width = 63;
        int height = 37;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman",0,28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for(int i=0;i<40;i++){
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        //绘制字符
        String strCode = "";
        for(int i=0;i<4;i++){
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(rand, 13*i+6, 28);
        }
        //将字符保存到session中用于前端的验证
        //session.setAttribute("strCode", strCode);
        request.getSession(true).setAttribute("strCode",strCode);
        g.dispose();

        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();

    }
	Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255)
            fc = 255;
        if(bc>255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }
	
	@RequestMapping("checkToken")
	public ModelAndView checkToken(HttpServletRequest request,HttpServletResponse resp,ModelAndView modelAndView) throws IOException{
		String token = request.getParameter("token");
		String url = request.getParameter("entryUrl");
		resp.setHeader("token", token);
		modelAndView.setViewName("forward:"+url);
        return modelAndView;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:解密
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPassword")
	public ResponseDbCenter getPassword(@RequestParam(required=true)String password){
		ResponseDbCenter res = new ResponseDbCenter();
		try {
			res.setResModel(new String(MD5Code.decode(password)));
			return res;
		} catch (Exception e) {
			return ResponseConstants.ILLEGAL_PARAM;
		}
	}
	
	

	@ResponseBody
	@RequestMapping("/selectUserByMobile")
	public ResponseDbCenter selectUserByMobile(String mobile){
		ResponseDbCenter res = new ResponseDbCenter();

		 try {
			
			Map<String,Object> map= newUserService.selectUserByOnlyMobile(mobile);
			 res.setResModel(map);
			 return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		 
		 
		 
		 
	}



	/**
	 * 根据登录用户的token得到userId，然后查看该用户绑定的企业信息
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectUserCompanys")
	public ResponseDbCenter selectUserCompanys(HttpServletRequest req,String token){

		try {
			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel(userService.selectUserCompanys(req.getSession().getAttribute(token).toString()));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}


	



	@ApiOperation(value = "查询用户列表")
	@ResponseBody
	@RequestMapping(value = "/selectUsers" ,method = RequestMethod.GET)
	public ResponseDbCenter selectUsers(HttpServletRequest req , HttpServletResponse rsp,
										@ApiParam(value = "查询内容", required = false) @RequestParam(required = false) String content){
		//返回数据总对象，0000表示成功，其它都表示错误

		String loginUserId = (String)req.getAttribute("loginUserId");
		try{
			String pcCompanyId = null;
			Map<String, Object> mapp = userService1.selectUserById(loginUserId);
			if(mapp !=null && mapp.size() > 0){
				String roleId = (String)mapp.get("roleId");
				if(!"1".equals(roleId)){
					pcCompanyId = (String)mapp.get("pcCompanyId");
				}
			}

			List<Map<String,Object>> userMaps = userService1.selectUsers(pcCompanyId,content);
			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel(userMaps);

			return res;

		}catch (Exception e){
			e.printStackTrace();
			return   ResponseConstants.FUNC_SERVERERROR;
		}
	}

	@ApiOperation(value = "查询用户列表")
	@ResponseBody
	@RequestMapping(value = "/searchUserByMobile" ,method = RequestMethod.GET)
	public ResponseDbCenter searchUserByMobile(HttpServletRequest req , HttpServletResponse rsp,
										@ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile){
		//返回数据总对象，0000表示成功，其它都表示错误

		try{
			List<Map<String,Object>> userMaps = userService1.searchUserByMobile(mobile);
			ResponseDbCenter res = new ResponseDbCenter();
			res.setResModel(userMaps);

			return res;

		}catch (Exception e){
			e.printStackTrace();
			return   ResponseConstants.FUNC_SERVERERROR;
		}
	}


	@ApiOperation(value = "按角色和手机查询用户列表")
	@ResponseBody
	@RequestMapping(value = "/searchAllUserByMobileAndRole" ,method = RequestMethod.POST)
	public ResponseDbCenter searchAllUserByMobileAndRole(HttpServletRequest req , HttpServletResponse rsp,
											   @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
														 @ApiParam(value = "角色Id  3技师 4 客户", required = true) @RequestParam(required = true) String roleId,
														 @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
														 @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer  pageSize){

		try{
//			String loginUserId = "818";
				String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String, Object> loginUserMap = userService1.selectUserById(loginUserId);
			List<Map<String,Object>> userMaps = userService1.searchAllUserByMobileAndRole(roleId,mobile,1, (String) loginUserMap.get("pcCompanyId"),currentPage,pageSize);
			Integer total=userService1.searchAllUserCountByMobileAndRole(roleId,mobile,1 ,(String) loginUserMap.get("pcCompanyId"));

			ResponseDbCenter res = new ResponseDbCenter();
			res.setTotalRows(total+"");
			res.setResModel(userMaps);
			return res;

		}catch (Exception e){
			e.printStackTrace();
			return   ResponseConstants.FUNC_SERVERERROR;
		}
	}

	/**
	 * 有电验证码接口
	 * @param req
	 * @param rsp
	 * @param tel
	 * @param appType: app类型      （区分app推送时是哪个端的极光key;"0"客户端，"1"技师端，"2"服务商端）
	 * @return
	 */
	@ApiOperation(value = "发送手机验证码")
	@ResponseBody
	@RequestMapping(value = "/YDsendCode" ,method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter sendCodeByMobile(HttpServletRequest req , HttpServletResponse rsp,
											 @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String tel,
											 @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
											 @ApiParam(value = "0客户端，1技师端，2服务商端", required = false) @RequestParam(required = false) String appType
	){

		if((StringUtils.isBlank(tel) && StringUtils.isBlank(email)) || StringUtils.isBlank(appType)){
			return  ResponseConstants.MISSING_PARAMTER;
		}

		Random random = new Random();
		String code = random.nextInt(8999) + 1000+"";

		System.out.println("============验证码为:"+code+"===================");
		System.out.println("============验证码为:"+code+"===================");

		String content = null;
		try {
			//防止乱码，转两次
			content = new String (("验证码 :"+code).getBytes("UTF-8"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!StringUtils.isBlank(tel)){
			ObjectNewsService.pushMessage(PropertiesUtil.YOUDIAN_LIGHTPUSH_IP,new Date(),PropertiesUtil.YOUDIAN_LIGHTPUSH_TOKEN,
					"",tel,"", "",content,
					"YD", appType,"0","1","0","0");
			MapCacheManager.putCacheMap(tel,code);
		}
		if(!StringUtils.isBlank(email)){
			ObjectNewsService.pushMessage(PropertiesUtil.YOUDIAN_LIGHTPUSH_IP,new Date(),PropertiesUtil.YOUDIAN_LIGHTPUSH_TOKEN,
					"","",email, "",content,
					"YD", appType,"0","0","0","1");
			MapCacheManager.putCacheMap(email,code);
		}

		ResponseDbCenter res = new ResponseDbCenter();
		return res;
	}

	/**
	 * 保存用户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "app端更改用户信息")
	@ResponseBody
	@RequestMapping(value = "/updateUserApp", method = RequestMethod.POST )
	public ResponseDbCenter updateUserApp(HttpServletRequest req,HttpServletResponse rsp,
										 @ApiParam(value = "用户Id 修改时需要传，添加时不用传" ,required = false)  @RequestParam(required = false) String id,
										 @ApiParam(value = "姓名" ,required = false) @RequestParam(required = false) String name,
										 @ApiParam(value = "昵称" ,required = false) @RequestParam(required = false) String nickName,
										 @ApiParam(value = "邮箱" ,required = false) @RequestParam(required = false) String email,
										 @ApiParam(value = "邮箱验证码" ,required = false) @RequestParam(required = false) String emailCode,
										 @ApiParam(value = "手机" ,required = false) @RequestParam(required = false) String mobile,
										 @ApiParam(value = "app个人信息_公司" ,required = false) @RequestParam(required = false) String userCompanyName,
										 @ApiParam(value = "app个人信息_行业" ,required = false) @RequestParam(required = false) String userIndustry,
										 @ApiParam(value = "app个人信息_联系地址" ,required = false) @RequestParam(required = false) String userContactAddress,
										 @ApiParam(value = "app个人信息_服务电话" ,required = false) @RequestParam(required = false) String userServerMobile,
										 @ApiParam(value = "app推送通知，0不推送，1推送" ,required = false) @RequestParam(required = false) String appPushFlag,
										 @ApiParam(value = "短信推送通知，0不推送，1推送" ,required = false) @RequestParam(required = false) String messagePushFlag,
										 @ApiParam(value = "语音推送通知，0不推送，1推送" ,required = false) @RequestParam(required = false) String voicePushFlag,
										 @ApiParam(value = "邮箱推送通知，0不推送，1推送" ,required = false) @RequestParam(required = false) String emailPushFlag

	) throws Exception{




		if(StringUtils.isBlank(id)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> map = new HashedMap();



		try {


			boolean callFlag = false;
			if (StringUtils.isNotBlank(email)) {

				if(StringUtils.isBlank(emailCode)){
					return ResponseConstants.MISSING_PARAMTER;
				}else{
					String codeExists = MapCacheManager.getCacheMapFiveMinute(email);
					if(emailCode.equals(codeExists)){
						map.put("email",email);
						callFlag = true;
					}else{
						return ResponseConstants.TEL_CODE_ERROR;
					}
				}

				//如果email已经存在了，则不允许添加
				Map<String, Object> userMap = newUserService.selectUserByEmail(email, 1);
				if (userMap != null) {
					String idExists = (String) userMap.get("id");
					if (userMap != null && userMap.size() > 0 && !idExists.equals(id)) {
						return ResponseConstants.EMAIL_EXIST;
					}
				}
			}
			/*
				app端登录
			 */
			map.put("uname",name);
			map.put("id",id);
			map.put("nickName",nickName);
			map.put("platform",1);
			map.put("updatedBy", loginUserId);
			map.put("updateDate", new Date());
			map.put("userCompanyName",userCompanyName);
			map.put("userIndustry",userIndustry);
			map.put("userContactAddress",userContactAddress);
			map.put("userServerMobile",userServerMobile);
			if(StringUtils.isNotBlank(appPushFlag)){
				map.put("appPushFlag",Integer.parseInt(appPushFlag));
			}
			if(StringUtils.isNotBlank(messagePushFlag)){
				map.put("messagePushFlag",Integer.parseInt(messagePushFlag));
			}
			if(StringUtils.isNotBlank(voicePushFlag)){
				map.put("voicePushFlag",Integer.parseInt(voicePushFlag));
			}
			if(StringUtils.isNotBlank(emailPushFlag)){
				map.put("emailPushFlag",Integer.parseInt(emailPushFlag));
			}
			newUserService.updateDcUser(map);
			newUserService.updateDcUserDetail(map);

			apiCallFacadeService.updateUserCallFacede(null,null,null,
					id,null,null,callFlag == true?email:null,emailPushFlag,voicePushFlag,appPushFlag,messagePushFlag);

	} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

}



