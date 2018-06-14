package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.mapper.CompanyMapper;
import com.xkd.mapper.DictionaryMapper;
import com.xkd.model.DC_PC_User;
import com.xkd.model.DC_User;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	
	//每隔1分钟导入联想信息到redis中
	public static boolean ideaRedisFlag = false;
	
	@Autowired
    private DC_PC_UserService pcUserService ;
	
//	@Autowired
//	private IdeaRedisService ideaRedisService;

	String ASSET_URL = PropertiesUtil.ASSET_URL;

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
	
	public static CompanyMapper companyMapperIdea;

	public static DictionaryMapper dictionaryMapperIdea;

	
	
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
        
//        /*
//         * 每隔一分钟更新联想库中的数据
//         *
//         *
//         *
//         *
//         * 备注： 后面可以做成线程
//         */
//       if(!ideaRedisFlag){
//
//        	ideaRedisFlag = true;
//
//        	if(companyMapperIdea == null && companyMapper != null){
//
//        		companyMapperIdea = companyMapper;
//        	}
//
//
//
//			if(dictionaryMapperIdea == null && dictionaryMapper != null){
//
//				dictionaryMapperIdea = dictionaryMapper;
//			}
//
//
//        	ideaRedisService.importIdeaRedis();
//
//        }
        
        
		ResponseDbCenter ResponseDbCenter = new ResponseDbCenter();
		userResult.put("token",token);
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
	 * @功能描述:用户修改密码
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/resetUser")
	public ResponseDbCenter resetUser(HttpServletRequest req){
		String telcode = req.getParameter("telCode");
		String tel  = req.getParameter("tel");
		String password = req.getParameter("password");
		password = password == "" ? "654123":new String(Base64.decode(password));
		
		if(StringUtils.isBlank(tel)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		String usercode = (String) req.getSession().getAttribute("code" + tel);
		if(StringUtils.isBlank(usercode)||!usercode.equals(telcode)){
			return ResponseConstants.TEL_CODE_ERROR;
		}
		DC_PC_User user =pcUserService.getUserByTel(tel);
		if(user == null){
			return ResponseConstants.USER_EXIST_ERROR_WJ;
		}
		user.setPassword(password );
		pcUserService.saveUser(user);
		String token = UUID.randomUUID().toString();
		ResponseDbCenter res = new ResponseDbCenter();
		redisCache.setCacheObject(token, String.valueOf(user.getId()), 60 * 30);
		req.getSession().setAttribute(token, String.valueOf(user.getId()));
		Map<String, String> map = new HashMap<>();
		map.put("token",token);
		map.put("uname", user.getUname());
		res.setResModel(map);
		return res;
	}*/
	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:用户登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pcLogin")
	public ResponseDbCenter pcLogin(HttpServletRequest req){
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String password = req.getParameter("password");//密码
		String tel  = req.getParameter("tel");
		String code  = req.getParameter("code");
		String strCode = (String) req.getSession().getAttribute("strCode");
		if(!code.equals(strCode)){
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
			userMap = newUserService.selectUserByMobile(tel,1);
		}
		
		//password = password == "" || password == null ? "a654123":new String(MD5Code.decode(password));
		if(userMap == null || userMap.size() == 0){
			return ResponseConstants.USER_ERROR_SIGN;
		}else if(StringUtils.isNotBlank(password)&&!password.equals(userMap.get("password")+"")){
			return ResponseConstants.NAME_PWD_ERROR_WJ;
		}
		if (!"1".equals(userMap.get("roleId"))) {//如果不是超级管理员
			/**
			 * 判断用户所属公司是否过期，过期了则不能继续登录
			 */
			Map<String, Object> pcCompanyMap = customerService.selectPcCompanyById((String) userMap.get("pcCompanyId"));
			String dateTo = (String) pcCompanyMap.get("dateTo");
			Integer enableStatus = (Integer) pcCompanyMap.get("enableStatus");
			if (dateTo != null) {
				Date date = DateUtils.stringToDate(dateTo, "yyyy-MM-dd");
				Date nowDate = DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
				if (date.before(nowDate)) {
					return ResponseConstants.ACCOUNT_EXPIRE;
				}
			}
			if (enableStatus != null) {
				if (1 == enableStatus) {
					return ResponseConstants.ACCOUNT_DIABLED;
				}
			}
		}

		String token = UUID.randomUUID().toString();
		Map<String, String> map = new HashMap<>();
		map.put("token",token);
		map.put("uname",userMap.get("uname")+"");
		
		
		/*
		 * 一个用户多台电脑登录，让其他用户下线，只允许当前用户下线
		 */


		req.getSession().removeValue(userMap.get("id")+"");
		req.getSession().setAttribute(token,userMap.get("id"));
		req.getSession().setAttribute(token+"uname",userMap.get("uname"));//不要删除session里面存的uname
		req.getSession().setAttribute("user",userMap);


		//用于单点登录，将用户
		MapCacheManager.removeCacheMapByValue(userMap.get("id")+"");
		MapCacheManager.putCacheMap(token, userMap.get("id")+"");

		req.getSession().setMaxInactiveInterval(60*60*5);//以秒为单位
		res.setResModel(map);
		return res;
	}
	
	
	
/*	*//**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:PC用户注册
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/pcRegister")
	public ResponseDbCenter pcRegister(HttpServletRequest req){
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String password = req.getParameter("password");
		String tel  = req.getParameter("tel");
		String uname  = req.getParameter("uname");

		if(StringUtils.isBlank(tel) || StringUtils.isBlank(password) || StringUtils.isBlank(uname)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		
		DC_PC_User user = pcUserService.getUserByTel(tel);
		
		if(user == null){
			password = password == "" || password == null ? "a654123":new String(Base64.decode(password));
			user = new DC_PC_User();
			user.setPassword(password);
			user.setUpdateDate(DateUtils.currtime());
			user.setUname(uname);
			user.setMobile(tel);
			pcUserService.saveUser(user);
		}else{
			return ResponseConstants.USER_TEL_NOT_NULL;
		}
		String token = UUID.randomUUID().toString();
		Map<String, String> map = new HashMap<>();
		map.put("token",token);
		map.put("uname", user.getUname());
		
		redisCache.setCacheObject(token, String.valueOf(user.getId()), 60 * 30);
		
		
		res.setResModel(map);
		return res;
	}*/
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年3月22日 
	 * @功能描述:用户登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public ResponseDbCenter login(HttpServletRequest req){
		
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		String code = req.getParameter("code");//用户微信唯一标识
		String telcode = req.getParameter("telCode");//验证码
		String openId = req.getParameter("openId");//用户微信唯一标识
		String tel  = req.getParameter("tel");
		String token2  = req.getParameter("token");
		String meetingId  = req.getParameter("meetingId");
		String uname  = req.getParameter("uname");
		
		//值为noCheckTel时表示不用验证用户手机号码，根据openid就可以登录
		//值为myCrm时表示要去关联客户
		String ttype = req.getParameter("ttype");
		
		if(StringUtils.isBlank(openId) && StringUtils.isBlank(code) && StringUtils.isBlank(token2)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		
		DC_User user = null;
		String token = UUID.randomUUID().toString();
		if(StringUtils.isNotBlank(openId) && user == null){
			user = userService.getUserByObj(openId,"2");
		}
		if(StringUtils.isNotBlank(token2) && user == null){
			Object userid = req.getSession().getAttribute(token2);
			if(userid != null){
				user = userService.getUserById(userid);
				token = token2;
			}else{
				return ResponseConstants.FUNC_BP_TOKENISWRONG;
			}
		}
		if(StringUtils.isNotBlank(tel)){//根据手机号码登录
			if(StringUtils.isNotBlank(telcode)){
				String usercode = (String) req.getSession().getAttribute("code"+tel);
				if(usercode == null || !usercode.equals(telcode)){
					return ResponseConstants.TEL_CODE_ERROR;
				}
			}
			if(user != null){
				DC_User user2 = userService.getUserByObj(tel,"1");
				if(StringUtils.isBlank(user.getMobile()) && null == user2){
					user.setMobile(tel);
					user.setUname(StringUtils.isNotBlank(uname)?uname:user.getUname());
					userService.saveUser(user);
				}else if(StringUtils.isBlank(user.getMobile()) || (!user.getMobile().equals(tel))){
					
					if(user2 ==null){
						user.setMobile(tel);
						user.setUname(StringUtils.isNotBlank(uname)?uname:user.getUname());
						userService.saveUser(user);
					}else{
						//交换openId
						String otherOpenId = user2.getWeixin();
						user2.setWeixin(user.getWeixin());
						user.setWeixin(user.getWeixin()+"temp");
						user.setStatus("2");
						user2.setStatus("0");
						userService.saveUser(user);//先修改登录用户的openId为假openId
						user2.setUname(StringUtils.isNotBlank(uname)?uname:user2.getUname());
						userService.saveUser(user2);//然后把当前的openId修改到当前用户输入的手机号码对应用户的openId
						if(StringUtils.isNotBlank(otherOpenId)){
							user.setWeixin(otherOpenId);
							user.setStatus("0");
							user.setUname(StringUtils.isNotBlank(uname)?uname:user.getUname());
							userService.saveUser(user);//最后把登录用户此次填的手机号码修改进来
						}
						user = null;
						user = user2;
					}
				}
			}
		}
		if(null == user && StringUtils.isNotBlank(code)){//根据code创建用户并且手机号码为空时提示绑定手机号码
				HashMap<String, String> wx = SysUtils.getOpenId(code);
				if(wx == null&&!code.equals("123")){
					return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ; 
				}
				openId = code.equals("123") ? "o2DOPwUMugpWcLs9swzBX6FgEwNk" : wx.get("openId");
				if(openId == null){
					return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
				}
				if(user == null ){//用户不存在时根据用户openId创建一个用户
//					user = userService.getUserByObj(wx.get("unionid"), "3");
//					if(user == null){
//						user = userService.getUserByObj(openId,"2");
//					}
					user = userService.getUserByObj(openId,"2");
					if(user ==null){
						Map<String,String> map_WX = SysUtils.getWeiXinInfoByOpenid(wx.get("wxToken"),wx.get("openId"));
						user = new DC_User();
						user.setMobile(tel);
						user.setUname(map_WX.get("nickName"));
						user.setSex(map_WX.get("sex"));
						user.setUserLogo(map_WX.get("headImgUrl"));
						user.setWeixin(openId);
						user.setUpdateDate(DateUtils.currtime());
						//user.setUnionId(wx.get("unionid"));
						userService.saveUser(user);
					}
				}
		}
		if(null == user){
			return ResponseConstants.FUNC_SERVERERROR;
		}

		//用于单点登录,允许同一个用户在移动端和pc端同时登录，加上mobile标志，表示移动端
		MapCacheManager.removeCacheMap(user.getId()+"mobile");
		MapCacheManager.putCacheMap(token,user.getId()+"mobile");

		req.getSession().setAttribute(token, user.getId());
		req.getSession().setAttribute(token+"user",user);
		req.getSession().setAttribute("getOpenId"+token, user.getWeixin());

		if(StringUtils.isNotBlank(ttype) && ttype.contains("myCrm")){//去绑定客户表
			Map<String, Object> userExise = new HashMap();
			userExise.put("token", token);
			userExise.put("openId", user.getWeixin());
			userExise.put("mobile", user.getMobile());
			if("myCrm".equals(ttype) && user.getMobile() == null ){
				res = new ResponseConstants().USER_BIND_TEL;
				res.setResModel(userExise);
				return res;
			}
			meetingId = StringUtils.isBlank(meetingId) ? "62" : meetingId;
			String companyId  = req.getParameter("companyId");
			String companyName  = req.getParameter("companyName");
			int status =userService.userRelationUserInfo(user,meetingId,companyId, companyName,uname,ttype);
			if("myCrm2".equals(ttype)&&status != 1 && status !=5){

				res = new ResponseConstants().USER_NOCHONGFU_SHENHE;
				//你已报名
				res.setResModel(userExise);
				return res;
			}
			if(status == 0){
				//申请成功，请联系现场顾问进行审核
				res = new ResponseConstants().USER_SHENQING_SUCCESS;
			}else if(status == 1){
				//没有权限参加会议的时候，让用户填写申请
				res = new ResponseConstants().USER_NO_CHECK;
			}else if(status == 2){
				res = new ResponseConstants().USER_QIANDAO_SHENHE;
				//请顾问审批后进入签到系统
			}else if(status == 3){
				res = new ResponseConstants().USER_SIGN_SUCCESS;
				//用户签到成功后返回顾问信息
				userExise.put("sign", userService.getSignInfo(meetingId,user.getId()));
				userExise.put("touxiang", user.getUserLogo());
			}else if(status == 5){

				if("myCrm2".equals(ttype)){
					res = new ResponseConstants().USER_SET_COMPANY;
				}else{
					res = new ResponseConstants().USER_NO_CHECK;
				}

			}
			res.setResModel(userExise);
			return res;
		}
		res.setResModel(token);
		return res;
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
	 * 小程序登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/xcxLogin")
	public ResponseDbCenter xcxLogin(
			String code
			,HttpServletRequest req){
		 ResponseDbCenter res = null;
		 try {
			 if(StringUtils.isNotBlank(code)){
				 res = ResponseConstants.MISSING_PARAMTER;
			 }
		 	HttpRequest request = new HttpRequest();
			//String result  = request.getData("https://api.weixin.qq.com/sns/jscode2session?appid=wx36ad2730eaeb3973&secret=45d56ee1626e06c00aefa270307fc4a2&js_code="+code+"&grant_type=authorization_code", "UTF-8");
			 String result  = request.getData("https://api.weixin.qq.com/sns/jscode2session?appid="+PropertiesUtil.APPID_XCX+"&secret="+PropertiesUtil.APPSECRET_XCX+"&js_code="+code+"&grant_type=authorization_code", "UTF-8");
			System.out.println("小程xu:"+result);
			Map<String, String> map2 = (Map<String, String>) JSON.parseObject(result, Object.class);
			String unionid = map2.get("unionid");
			 DC_User user = userService.getUserByObj(unionid,"3");
			 Map<String, String> map = new HashMap<>();

			 String token = UUID.randomUUID().toString();
			 if(null == user){
				 res = ResponseConstants.USER_BIND_TEL;
			 }else if(!user.getStatus().equals("0")){
				 return ResponseConstants.USER_NOTEXIST;
			 }else if(user.getPlatform().equals("0")){
				 res = ResponseConstants.USER_BIND_TEL;
			 }else{
				 req.getSession().removeValue(user.getId());
				 res = new ResponseDbCenter();

				 //用于单点登录,允许同一个用户在移动端和pc端同时登录，加上xcx标志，表示移动端小程序
				 MapCacheManager.removeCacheMap(user.getId()+"xcx");
				 MapCacheManager.putCacheMap(token,user.getId()+"xcx");

				 req.getSession().setAttribute(token, user.getId());
				 req.getSession().setAttribute(token + "uname", user.getUname());
				 map.put("userId",user.getId());
				 map.put("uname",user.getUname());
			 }
			 if (null != user && !"1".equals(user.getRoleId()) && null != user.getPcCompanyId()) {//如果不是超级管理员
				 /**
				  * 判断用户所属公司是否过期，过期了则不能继续登录
				  */
				 Map<String, Object> pcCompanyMap = customerService.selectPcCompanyById((String) user.getPcCompanyId());
				 String dateTo = (String) pcCompanyMap.get("dateTo");
				 Integer enableStatus = (Integer) pcCompanyMap.get("enableStatus");
				 if (dateTo != null) {
					 Date date = DateUtils.stringToDate(dateTo, "yyyy-MM-dd");
					 Date nowDate = DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
					 if (date.before(nowDate)) {
						 return ResponseConstants.ACCOUNT_EXPIRE;
					 }
				 }
				 if (enableStatus != null) {
					 if (1 == enableStatus) {
						 return ResponseConstants.ACCOUNT_DIABLED;
					 }
				 }
			 }
			 req.getSession().setAttribute(unionid+"xcxOpenId", map2.get("openId"));
			 req.getSession().setAttribute(token+"unionid", unionid);
			 
			 map.put("token", token);
			 map.put("sessionId", req.getSession().getId());
			 System.out.println("-------------------小程序登录----:"+res.getRepCode());
			 res.setResModel(map);
			 return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
	/**
	 * 小程序登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkUserTel")
	public ResponseDbCenter checkUserTel(@RequestParam(required=true)String tel,
			HttpServletRequest req){
		ResponseDbCenter res = new ResponseDbCenter();
		 try {
			 DC_User user = userService.getUserByObj(tel, "1");
			 if(null == user){
				 return ResponseConstants.NOT_PERMITTED;
			 }
			 if (!"1".equals(user.getRoleId())) {//如果不是超级管理员
				 /**
				  * 判断用户所属公司是否过期，过期了则不能继续登录
				  */
				 Map<String, Object> pcCompanyMap = customerService.selectPcCompanyById((String) user.getPcCompanyId());
				 String dateTo = (String) pcCompanyMap.get("dateTo");
				 Integer enableStatus = (Integer) pcCompanyMap.get("enableStatus");
				 if (dateTo != null) {
					 Date date = DateUtils.stringToDate(dateTo, "yyyy-MM-dd");
					 Date nowDate = DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
					 if (date.before(nowDate)) {
						 return ResponseConstants.ACCOUNT_EXPIRE;
					 }
				 }
				 if (enableStatus != null) {
					 if (1 == enableStatus) {
						 return ResponseConstants.ACCOUNT_DIABLED;
					 }
				 }
			 }
			 return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
	/**
	 * 小程序登录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userBindTel")
	public ResponseDbCenter userBindTel(HttpServletRequest req,@RequestParam(required=true)String tel,String code,String token,String uname,String userLogo,
			String nickName){
		ResponseDbCenter res = new ResponseDbCenter();
		 try {
			 if(StringUtils.isBlank(token)){
				 return ResponseConstants.MISSING_PARAMTER;
			 }
			 String telCode = (String) req.getSession().getAttribute("code"+tel);
			 if(!code.equals(telCode)){
				 return ResponseConstants.TEL_CODE_ERROR;
			 }
			 DC_User user = userService.getUserByObj(tel, "1");
			 String unionid =  (String) req.getSession().getAttribute(token+"unionid");
			 if(null == user){
				 return ResponseConstants.NOT_PERMITTED;
			 }else if(StringUtils.isBlank(user.getUnionId()) || !unionid.equals(user.getUnionId())){
				 DC_User user2 = userService.getUserByObj(unionid, "3");
				 if(user2 !=null){
					 user2.setUnionId(user2.getUnionId()+"temp");
					 userService.saveUser(user2);
				 }else{
					 user.setXcxOpenId((String)req.getSession().getAttribute(token+"xcxOpenId"));
					 user.setMobile(tel);
				 }
				 user.setPlatform("1");
				 user.setUnionId(unionid);
				 
				 userService.saveUser(user);
				
			 }else if(user.getPlatform().equals("0")){
				 user.setPlatform("1");
				 userService.saveUser(user);
			 }

			 //req.getSession().removeValue(user.getId());
			 //token = UUID.randomUUID().toString();


			 //用于单点登录,允许同一个用户在移动端和pc端同时登录，加上xcx标志，表示移动端小程序
			 MapCacheManager.removeCacheMap(user.getId()+"xcx");
			 MapCacheManager.putCacheMap(token,user.getId()+"xcx");

			 req.getSession().setAttribute(token, user.getId());
			 req.getSession().setAttribute(token+"uname", user.getUname());
			 req.getSession().setAttribute("getOpenId"+token, String.valueOf(user.getWeixin()));

			 Map<String,String> map = new HashMap<>();
			 map.put("token",token);
			 map.put("userId",user.getId());
			 map.put("uname",user.getUname());
			 map.put("sessionId", req.getSession().getId());
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


	/**
	 * 根据code获得openId
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOpenIdByCode")
	public ResponseDbCenter getOpenIdByCode(HttpServletRequest req){

		String code = req.getParameter("code");

		try {
			ResponseDbCenter res = new ResponseDbCenter();
			HashMap<String, String> wx = SysUtils.getOpenId(code);
			if(wx == null&&!code.equals("123")){
				return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
			}
			String openId = code.equals("123") ? "o2DOPwUMugpWcLs9swzBX6FgEwNk" : wx.get("openId");

			res.setResModel(openId);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}
	
	 @ResponseBody
		@RequestMapping("getWxConfig")
		public Map<String, String> getWxConfig(HttpServletRequest request,String url,String APPID,String APPSECRET){

			if(StringUtils.isBlank(url) || StringUtils.isBlank(APPID) ||StringUtils.isBlank(APPSECRET)){
				return null;
			}
	       
	  
	        Map<String, String> ret = WxHttpUtil.sign( url, APPID, APPSECRET);
	        System.out.println("计算出的签名-----------------------");  
	        System.out.println("-----------------------"); 
	        ret.put("appid", APPID);
	        return ret;  
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
}



