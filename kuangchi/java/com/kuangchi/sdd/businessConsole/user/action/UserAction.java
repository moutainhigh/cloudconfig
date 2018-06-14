package com.kuangchi.sdd.businessConsole.user.action;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.businessConsole.user.service.IUserService;
import com.kuangchi.sdd.consumeConsole.consumeRecord.service.IConsumeRecordService;
import com.kuangchi.sdd.util.algorithm.MD5;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.DES;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.EncodeUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.PropertyUtils;

@Controller("userAction")
public class UserAction extends BaseActionSupport implements  Serializable{
	
	private static final long serialVersionUID = -6194143783922371041L;
	private static final  Logger LOG = Logger.getLogger(UserAction.class);
	
	private User model;
	
	public UserAction() {
		this.model = new User();
	}
	
	 @Resource(name = "userServiceImpl")
	 private IUserService userService;
	 
	 @Resource(name = "roleServiceImpl")
	 private IRoleService roleService;
	 
	 @Resource(name = "cardServiceImpl")
	 private ICardService cardService;
	 
	 @Resource(name = "ConsumeRecordServiceImpl")
	 private IConsumeRecordService consumeRecordService;

	@Override
	public Object getModel() {
		
		return model;
	}
	
	public String  userloginOut(){
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		HttpServletRequest request = getHttpServletRequest();
		String fla = request.getParameter("fla");
		HttpSession session = getHttpServletRequest().getSession();
		session.removeAttribute(GlobalConstant.LOGIN_USER);
		session.removeAttribute(GlobalConstant.LOGIN_USER_DEFAULT_ROLE);
		session.removeAttribute("flag");
		session.removeAttribute("token");
		session.removeAttribute("yhMc");
		userService.addLogger(loginUser.getYhMc(),fla);
		StringBuffer url=getHttpServletRequest().getRequestURL();
		session.invalidate();
		return SUCCESS;
		
	}
	
	/**
	 * 查询系统配置参数---CS登陆之后是否显示退出按钮
	 * by gengji.yang
	 * @return
	 */
	public void isSeeLogOutBtn(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("isSeeLogOutBtn",userService.isSeeLoginOutBtn());
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * CS 登陆 
	 * by gengji.yang
	 * @return
	 */
	public String csLogin(){
		HttpSession session=getHttpSession();
		String token=(String) session.getAttribute("token");
		String yhMc=(String)session.getAttribute("yhMc");
		if(token!=null&&yhMc!=null){//若是CS端带着口令过来，走这个分支
			User loginUser=userService.getLoginUserByYhMc(yhMc);
			if(loginUser!=null){
				Role userDefaultRole = roleService.getUserDefaultRole(loginUser.getYhDm());
				if(null!=userDefaultRole){
					getHttpSession().setAttribute(GlobalConstant.LOGIN_USER,loginUser);
					getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_DEFAULT_ROLE,userDefaultRole);
					getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE,userDefaultRole);
					LOG.info(loginUser.getYhDm() + " ==> 登录 默认角色==》" + userDefaultRole.getJsMc());
					return "success";
				}else{
					return "noRole";
				}
			}else{
				return "noUser";
			}
		}else{
			return "fail";
		}
	}
	

	/**
	 * 登录转发
	 * @return
	 */
	public String getLoginUser() {
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		if("0".equals(flag)){
			return "validLisence";
		}else if("1".equals(flag)){
			return "alertDialog";
		}else {
			return "writeLisence";
		}
		
	/*	
		HttpServletRequest request = getHttpServletRequest();
		JsonResult jsonResult = new JsonResult();
		User user = new User();
		String yhMc=request.getParameter("yhMc");
		String yhMm=request.getParameter("yhMm");
		System.out.println("【登录用户名:】"+yhMc);
		//BeanUtils.copyProperties(model, user);
		user.setYhMc(yhMc);
		user.setYhMm(yhMm);
		resetUser();
		user.setYhMm(EncodeUtil.encode(user.getYhMm()));// 密码加密
		User loginUser = null;
		jsonResult.setSuccess(true);
		try {
			// 获取登录用户
			loginUser = userService.getLoginUser(user);
			if (null != loginUser) {
				HttpSession session=getHttpSession();
				String isLikeCSStr=userService.isLikeCSLogin();
				if("0".equals(isLikeCSStr)){
					session.setAttribute("likeCS",true);
				}else{
					session.setAttribute("likeCS",false);
				}
				//	没修改，可能是loginUser没有被序列化的问题
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER,
						loginUser);
				// 获取用户默认角色
				Role userDefaultRole = roleService.getUserDefaultRole(loginUser.getYhDm());
				//	没修改，可能是userDefaultRole没有被序列化的问题		
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_DEFAULT_ROLE,
						userDefaultRole);
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE,userDefaultRole);
				LOG.info(loginUser.getYhDm() + " ==> 登录 默认角色==》" + userDefaultRole.getJsMc());
			} else {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("6");
			}
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("6");
			LOG.error("获取用户失败", e);
		}
		printHttpServletResponse(GsonUtil.toJson(jsonResult));
	*/
	}
	
	/**
	 * 用户登录
	 * @return
	 */
	public JsonResult loginUser(HttpServletRequest request){
		JsonResult jsonResult = new JsonResult();
		User user = new User();
		String yhMc=request.getParameter("yhMc");
		String yhMm=request.getParameter("yhMm");
		LOG.info("【登录用户名:】"+yhMc);
		//BeanUtils.copyProperties(model, user);
		user.setYhMc(yhMc);
		user.setYhMm(yhMm);
		resetUser();
		user.setYhMm(EncodeUtil.encode(user.getYhMm()));// 密码加密
		User loginUser = null;
		jsonResult.setSuccess(true);
		try {
			// 获取登录用户
			loginUser = userService.getLoginUser(user);
			if (null != loginUser) {
				String userMc = loginUser.getYhMc();
				HttpSession session=getHttpSession();
				String isLikeCSStr=userService.isLikeCSLogin();
				if("0".equals(isLikeCSStr)){
					session.setAttribute("likeCS",true);
				}else{
					session.setAttribute("likeCS",false);
				}
	/*----------没修改，可能是loginUser没有被序列化的问题------*/
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER,
						loginUser);
				// 获取用户默认角色
				Role userDefaultRole = roleService.getUserDefaultRole(loginUser.getYhDm());
	/*----------没修改，可能是userDefaultRole没有被序列化的问题------*/			
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_DEFAULT_ROLE,
						userDefaultRole);
				getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE,userDefaultRole);
				LOG.info(loginUser.getYhDm() + " ==> 登录 默认角色==》" + userDefaultRole.getJsMc());
				String flag = "1";
				userService.addLogger(userMc,flag); 
				/* 创建session */
		        ServletContext application = getHttpSession().getServletContext();
		        // 在application范围由一个HashSet集保存所有的session
		        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
		        if (sessions == null) {
		               sessions = new HashSet<HttpSession>();
		        }
		        sessions.add(session);
		        application.setAttribute("sessions", sessions);
				
			} else {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("6");
			}
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("6");
			LOG.error("获取用户失败", e);
		}
		return jsonResult;
	}
	
	/**
	 * 验证存在的lisence授权码
	 * @author minting.he
	 */
	public void validLisence(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		try{
			String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
			Properties properties = PropertyUtils.readProperties(propertyFile);
			String lisenceKey = properties.getProperty("lisenceKey");
			if(lisenceKey==null || "".equals(lisenceKey)){
				result.setMsg("0");
			//	result.setMsg("系统未激活，是否输入授权码激活？");
			}else {
				Date key_date = new Date();
				String[] key = lisenceKey.split(";");
				if(key.length!=4){
					result.setMsg("1");
				//	result.setMsg("授权码校验失败，是否使用合法校验码激活？");
				}else {
					//MAC地址
					String ServerMac = getMac();
					if(!"Kuang-Chi".equals(key[0]) && !ServerMac.equals(key[0])){
						result.setMsg("2");
					//	result.setMsg("授权码校验失败，是否使用合法校验码激活？");
					}else {
						//有效期
						if(key[1].length()!=8){
							result.setMsg("2");
						//	result.setMsg("授权码校验失败，是否使用合法校验码激活？");
						}else {
							String y = key[1].substring(0, 4); 
							String m = key[1].substring(4, 6);
							String d = key[1].substring(6, 8);
							String keyDate=y+"-"+m+"-"+d+" 23:59:59";
							Calendar c = Calendar.getInstance();
							c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyDate));
//							c.set(Integer.valueOf(y), Integer.valueOf(m)-1, Integer.valueOf(d), 23, 59, 59);
							key_date = c.getTime();
							Date now_date = new Date();	
							if(now_date.compareTo(key_date)>0){
								result.setMsg("3");
							//	result.setMsg("授权码有效期失效，是否重新激活？");
							}else {
								//客户端数量
								if(-1 == Integer.valueOf(key[3])){	//无限制
									//正常卡数量
									if(-1 == Integer.valueOf(key[2])){	//无限制
										//登录，Msg=6
										result = loginUser(request);
									}else {
										Integer max_card = Integer.valueOf(key[2]);
										Integer now_card = cardService.getNormalCount();
										if(now_card>max_card){
											result.setMsg("5");
										//	result.setMsg("系统发卡数已超过授权码限制数量，是否重新激活？");
										}else {
											//登录，Msg=6
											result = loginUser(request);
										}
									}
								}else{
									ServletContext application = request.getSession().getServletContext();
							        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
									Integer now_client = sessions.size();
									Integer max_client = Integer.valueOf(key[3]);
									if(now_client>=max_client){
										result.setMsg("4");
									//	result.setMsg("目前连接数已超过授权码数量，是否重新激活？");
									}else {
										//正常卡数量
										if(-1 == Integer.valueOf(key[2])){	//无限制
											//登录，Msg=6
											result = loginUser(request);
										}else {
											Integer max_card = Integer.valueOf(key[2]);
											Integer now_card = cardService.getNormalCount();
											if(now_card>max_card){
												result.setMsg("5");
											//	result.setMsg("系统发卡数已超过授权码限制数量，是否重新激活？");
											}else {
												//登录
												//Msg=6
												result = loginUser(request);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("7");
		//	result.setMsg("激活失败，请使用合法授权码激活");
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 弹出授权码输入框
	 * @author minting.he
	 * @return
	 */
	public String alertDialog(){
		return "success";
	}
	
	/**
	 * 验证输入的lisence
	 * @author minting.he
	 */
	public void writeLisence(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		try{
			String before_lisence = request.getParameter("lisence");
			if(EmptyUtil.atLeastOneIsEmpty(before_lisence)){
				result.setMsg("校验失败，数据不合法");
			}else {
				String after_lisence = DES.decryptBasedDes(before_lisence);
				Date key_date = new Date();
				String[] key = after_lisence.split(";");
				if(key.length!=4){
				//	result.setMsg("1");
					result.setMsg("授权码校验失败，请使用合法授权码激活");
				}else {
					//MAC地址
					String ServerMac = getMac();
					if(!"Kuang-Chi".equals(key[0]) && !ServerMac.equals(key[0])){
					//	result.setMsg("2");
						result.setMsg("授权码校验失败，请使用合法授权码激活");
					}else {
						//有效期
						if(key[1].length()!=8){
						//	result.setMsg("2");
							result.setMsg("授权码校验失败，请使用合法授权码激活");
						}else {
							String y = key[1].substring(0, 4); 
							String m = key[1].substring(4, 6);
							String d = key[1].substring(6, 8);
							String keyDate=y+"-"+m+"-"+d+" "+"23:59:59";
							Calendar c = Calendar.getInstance();
							c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyDate));
//							c.set(Integer.valueOf(y), Integer.valueOf(m)-1, Integer.valueOf(d), 23, 59, 59);
							key_date = c.getTime();
							Date now_date = new Date();	
							if(now_date.compareTo(key_date)>0){
							//	result.setMsg("3");
								result.setMsg("授权码有效期失效，请重新激活");
							}else {
								//客户端数量
								if(-1 == Integer.valueOf(key[3])){	//无限制
									//正常卡数量
									if(-1 == Integer.valueOf(key[2])){	//无限制
										//把lisence写进配置文件中
										String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
										boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
										if(flag){
											//登录，Msg=6
											result = loginUser(request);
										//	userService.licenseLog("0", "0", loginUser.getYhMc());
										}else {
											result.setSuccess(false);
											result.setMsg("激活失败");
										//	userService.licenseLog("0", "1", loginUser.getYhMc());
										}
									}else {
										Integer max_card = Integer.valueOf(key[2]);
										Integer now_card = cardService.getNormalCount();
										if(now_card>max_card){
										//	result.setMsg("5");
											result.setMsg("系统发卡数已超过授权码限制数量，请重新激活");
										}else {
											//把lisence写进配置文件中
											String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
											boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
											if(flag){
												//登录，Msg=6
												result = loginUser(request);
											//	userService.licenseLog("0", "0", loginUser.getYhMc());
											}else {
												result.setSuccess(false);
												result.setMsg("激活失败");
											//	userService.licenseLog("0", "1", loginUser.getYhMc());
											}
										}
									}
								}else {
									ServletContext application = request.getSession().getServletContext();
							        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
									Integer now_client = sessions.size();
									Integer max_client = Integer.valueOf(key[3]);
									if(now_client>=max_client){
									//	result.setMsg("4");
										result.setMsg("目前连接数已超过授权码数量，请重新激活");
									}else {
										//正常卡数量
										if(-1 == Integer.valueOf(key[2])){	//无限制
											//把lisence写进配置文件中
											String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
											boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
											if(flag){
												//登录，Msg=6
												result = loginUser(request);
											//	userService.licenseLog("0", "0", loginUser.getYhMc());
											}else {
												result.setSuccess(false);
												result.setMsg("激活失败");
											//	userService.licenseLog("0", "1", loginUser.getYhMc());
											}
										}else {
											Integer max_card = Integer.valueOf(key[2]);
											Integer now_card = cardService.getNormalCount();
											if(now_card>max_card){
											//	result.setMsg("5");
												result.setMsg("系统发卡数已超过授权码限制数量，请重新激活");
											}else {
												//把lisence写进配置文件中
												String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
												boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
												if(flag){
													//登录，Msg=6
													result = loginUser(request);
												//	userService.licenseLog("0", "0", loginUser.getYhMc());
												}else {
													result.setSuccess(false);
													result.setMsg("激活失败");
												//	userService.licenseLog("0", "1", loginUser.getYhMc());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			//7
			result.setMsg("激活失败，请使用合法授权码激活");
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 获取服务器MAC地址
	 * @author minting.he
	 * @return
	 */
	public String getMac(){
		String macId = "";
		InetAddress ip = null;
		NetworkInterface ni = null;
		try {
			boolean bFindIP = false;
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				if (bFindIP) {
					break;
				}
				ni = (NetworkInterface) netInterfaces.nextElement();
				// ----------特定情况，可以考虑用ni.getName判断
				// 遍历所有ip
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}")) {	// 非127.0.0.1
						bFindIP = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != ip) {
			try {
				macId = getMacFromBytes(ni.getHardwareAddress());
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		return macId;
	}
	
	/**
	 * MAC地址转换
	 * @author minting.he
	 */
	private String getMacFromBytes(byte[] bytes) {
		StringBuffer mac = new StringBuffer();
		byte currentByte;
		boolean first = false;
		for (byte b : bytes) {
			if (first) {
				mac.append("-");
			}
			currentByte = (byte) ((b & 240) >> 4);
			mac.append(Integer.toHexString(currentByte));
			currentByte = (byte) (b & 15);
			mac.append(Integer.toHexString(currentByte));
			first = true;
		}
		return mac.toString().toUpperCase();
	}
	
	/**
	 * 切换角色
	 */
	/*public String switchRole(){
		String jsDm = getHttpServletRequest().getParameter("jsDm");
		Role userCurrentRole = roleService.getRole(jsDm);
		getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE,userCurrentRole);
		return "success";
	}*/
	public void switchRole(){
		String jsDm = getHttpServletRequest().getParameter("jsDm");
		JsonResult result = new JsonResult();
		try {
			Role userCurrentRole = roleService.getRole(jsDm);
			getHttpSession().setAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE,userCurrentRole);
			result.setMsg("");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("服务调用异常！");
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 用户管理
	 */
	public void manageSysUser(){
		
		User userPage = new User();
		BeanUtils.copyProperties(model, userPage);
		
		//性别过滤条件
		if(BeanUtil.isEqual(userPage.getXb(), "all")){
			userPage.setXb(null);//不过滤性别
		}
		
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		userPage.setYhDm(loginUser.getYhDm());
		
		// 判断是否开启分层功能，若无开启，则查询所有用户；若开启，则根据录入人员查询用户
		boolean isLayer = roleService.isLayer();
		if(isLayer){
			String lrryDm =  userService.getLrryDm(loginUser.getYhDm());
			if(!"".equals(lrryDm) && lrryDm.length() != 0){
				lrryDm = lrryDm.substring(0, lrryDm.length()-1);
				userPage.setLrryDm(lrryDm);
			}
		}
		
		resetUser();
		Grid<User> grid = userService.getUsers(userPage);	
		printHttpServletResponse(GsonUtil.toJson(grid));
		
//		if(!"".equals(lrryDm) && lrryDm.length() != 0){
//			lrryDm = lrryDm.substring(0, lrryDm.length()-1);
//			User userPage = new User();
//			BeanUtils.copyProperties(model, userPage);
//			
//			//性别过滤条件
//			if(BeanUtil.isEqual(userPage.getXb(), "all")){
//				userPage.setXb(null);//不过滤性别
//			}
//			userPage.setLrryDm(lrryDm);
//			resetUser();
//			Grid<User> grid = userService.getUsers(userPage);	
//			printHttpServletResponse(GsonUtil.toJson(grid));
//		} else {
//			Grid<User> grid = new Grid<User>();
//			grid.setTotal(0);
//			printHttpServletResponse(grid);
//		}
		
		
	}
	
	/**
	 * 新增用户
	 */
	public void addNewUser(){
		HttpServletRequest request = getHttpServletRequest();
		
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
//		User pageUser = new User();
//		BeanUtils.copyProperties(model, pageUser);
//		resetUser();
		
		String data = request.getParameter("data");
		User pageUser = GsonUtil.toBean(data, User.class);
		String jsDm = request.getParameter("jsDm");//新增时 默认角色
		JsonResult result = new JsonResult();
		result.setMsg("");
		result.setSuccess(true);
		try{
			
			pageUser.setLrryDm(loginUser.getYhDm());
			userService.addNewUser(pageUser,jsDm);
		}catch(Exception e){
			result.setMsg("");
			result.setSuccess(false);
			LOG.error("user", e);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	
	private void resetUser(){
		model = new User();
	} 
	
	/**
	 * 配置用户默认角色 页面
	 * @return
	 */
	public String getUserAllRole(){
		
		HttpServletRequest request = getHttpServletRequest();
		String userId = request.getParameter("userId");
		
		List<Role> roles =  roleService.getUserAllRoles(userId);//用户所有角色
		request.setAttribute("roles", roles);	
		return SUCCESS;
	}
	
	public void setUserDefaultRole(){
		HttpServletRequest request = getHttpServletRequest();
		
		String userId = request.getParameter("userId");//用户代码
		String JS_DM = request.getParameter("JS_DM");//角色代码
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			roleService.setUserDefaultRole(userId,JS_DM);
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("设置默认角色", e);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	/**
	 * 获取用户额外菜单
	 */
	public void getAdditionMenu(){
		String userId = (String) getHttpServletRequest().getParameter("userId");//用户编号
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			result.setMsg(userService.getUserAdditionMenu(userId));
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("获取用户额外菜单", e);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	/**
	 * 设置用户额外菜单
	 */
	public void changeUserAdditionMenu(){
		String userId = (String) getHttpServletRequest().getParameter("userId");//用户编号
		String ids = (String) getHttpServletRequest().getParameter("ids");//菜单编号
		
		String startTime = (String) getHttpServletRequest().getParameter("startTime");//有效时间
		String endTime = (String) getHttpServletRequest().getParameter("endTime");//有效时间
		
		Timestamp start = DateUtil.getTimestamp(startTime);
		Timestamp end = DateUtil.getTimestamp(endTime);

		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			userService.changeUserAdditionMenu(userId,ids.split(","),loginUser.getYhDm(),start,end);
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("获取用户额外菜单", e);
		}
		
		
		printHttpServletResponse(new Gson().toJson(result));
		
	}
	
	/**
	 * 用户修改 页面
	 */
	public String userModify(){
		HttpServletRequest request = getHttpServletRequest();
		String yhDm = (String) request.getAttribute("yhDm");
		
		User user = userService.getUserByYhDm(yhDm);
		
		request.setAttribute("user", user);
		return SUCCESS;
	}
	
	/**
	 * 更新用户
	 */
	public void updateUser(){
		User pageUser = new User();
		BeanUtils.copyProperties(model, pageUser);
		
		resetUser();
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			userService.updateUser(pageUser);
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("update user error", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/**
	 * 旧密码验证
	 * @author yuman.gao
	 */
	public void validPwd(){
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String yhDm = loginUser.getYhDm();
		String password = request.getParameter("password");
		String md5Password=MD5.getInstance().encryption(password);
		Integer user = userService.validPwd(yhDm, md5Password);
		if(user > 0){
			printHttpServletResponse(GsonUtil.toJson(true));
		} else {
			printHttpServletResponse(GsonUtil.toJson(false));
		}
	}
	
	/**
	 * 修改用户密码
	 */
	public void modifyUserPwd(){
		HttpServletRequest request = getHttpServletRequest();
		
		String pwd = request.getParameter("pwd");//新密码	
		String yhDm = request.getParameter("yhDm");//用户代码
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			userService.modifyUserPwd(yhDm,EncodeUtil.encode(pwd));
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("modify user pwd error", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 用户指派角色 页面
	 * @return
	 */
	public String userAssignRole(){
		HttpServletRequest request = getHttpServletRequest();
		
		List<Role> allRoles = new ArrayList<Role>();
		
		
		// 判断是否开启分层功能，若无开启，则查询所有用户；若开启，则根据录入人员查询用户
		boolean isLayer = roleService.isLayer();
		if(isLayer){
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String lrryDm =  userService.getLrryDm(loginUser.getYhDm());
			if(!"".equals(lrryDm) && lrryDm.length() != 0){
				lrryDm = lrryDm.substring(0, lrryDm.length()-1);
				allRoles = roleService.getAllRoles(lrryDm); //获得系统分层所有角色
			}
		} else {
			allRoles = roleService.getAllRoles(null); //获得系统所有角色
		}
		
		//获得用户现用的角色
		String yhDm = request.getParameter("yhDm");//用户代码
		List<Role> userOwnRoles = roleService.getUserAllRoles(yhDm);

		request.setAttribute("roles",allRoles);
		request.setAttribute("rolesId",GsonUtil.toJson(userOwnRoles));
	
		return SUCCESS;
	}
	
	/**
	 * 更新用户角色
	 */
	public void updateUserRoles(){
		HttpServletRequest request = getHttpServletRequest();
		String yhDm = request.getParameter("yhDm");//用户代码
		String jsDms = request.getParameter("jsDms");//角色代码
		
		Gson gson = new Gson();
		List<Role> roles =  gson.fromJson(jsDms, new TypeToken<List<Role>>(){}.getType());
		
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			roleService.updateUserRoles(yhDm, roles, loginUser.getYhDm());
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("update user roles erroe", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除用户
	 */
	public void deleteUser(){
		String yhDms = getHttpServletRequest().getParameter("YhDms");
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			userService.deleteUser(yhDms);
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("delete User error", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 变更用户部门
	 */
	public void changeUserDepartment(){
		
		HttpServletRequest request = getHttpServletRequest();
		String yhDm = request.getParameter("userId");//用户代码
		String bmDmS = request.getParameter("ids");//部门代码
		
		User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		try{
			userService.changeUserDepartment(yhDm,bmDmS.split(","),loginUser.getYhDm());
		}catch(Exception e){
			result.setSuccess(false);
			LOG.error("delete User erroe", e);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 用户代码和用户名验证
	 */
	public void validUser(){
		HttpServletRequest request = getHttpServletRequest();
		String yhDm = request.getParameter("yhDm");	
		String yhMc = request.getParameter("yhMc");
		
		printHttpServletResponse(GsonUtil.toJson(userService.validUser(yhDm,yhMc)));
	}

    /**
     * 设置用户岗位页面
     * @return
     */
    public String userAssignStation(){
        HttpServletRequest request = getHttpServletRequest();
        String yhDm = request.getParameter("yhDm");
        Map<String,List<Station>> allStation = userService.getUserDepartStations(yhDm);
        String gws = userService.getUserGws(yhDm);
        request.setAttribute("allStation",allStation);
        request.setAttribute("gws",gws);
        return SUCCESS;
    }
    
    /**
     * 修改用户岗位
     */
    public void updateUserStations(){
        HttpServletRequest request = getHttpServletRequest();

        String gws = request.getParameter("gws");
        String yhDm = request.getParameter("yhDm");

        User loginUser = (User) request.getSession().getAttribute(GlobalConstant.LOGIN_USER);

        JsonResult result = new JsonResult();
        result.setSuccess(true);

        try{
            userService.updateUserStations(yhDm,gws.split(","),loginUser.getYhDm());
        }catch(Exception e){
            result.setSuccess(false);
            LOG.error("updateUserStations error", e);
        }

        printHttpServletResponse(GsonUtil.toJson(result));
    }
    
    /**
     * 查询用户代码是否存在
     * @return
     */
    public void selectYhDm(){
    	HttpServletRequest request = getHttpServletRequest();
		String yhDm = request.getParameter("yhDm");
		Integer count = userService.selectYhDm(yhDm.trim().replace("<","&lt").replace(">","&gt"));
		printHttpServletResponse(GsonUtil.toJson(count));
    }
    

	/**
	 * 将员工设为管理员（因参数不同，故不与新增用户使用同个方法）
	 */
	public void setUser(){
		HttpServletRequest request = getHttpServletRequest();
		
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		
		User pageUser = new User();
		BeanUtils.copyProperties(model, pageUser);
		resetUser();
		
		
		String jsDm = request.getParameter("jsDm");//新增时 默认角色
		JsonResult result = new JsonResult();
		result.setMsg("");
		result.setSuccess(true);
		try{
			
			pageUser.setLrryDm(loginUser.getYhDm());
			userService.setUser(pageUser,jsDm);
		}catch(Exception e){
			result.setMsg("");
			result.setSuccess(false);
			LOG.error("user", e);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	/**
	 * 修改lisence
	 * @author minting.he
	 */
	public void updateLisence(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		try{
			String password = request.getParameter("password");
			String before_lisence = request.getParameter("lisence");
			if(EmptyUtil.atLeastOneIsEmpty(before_lisence, password)){
				result.setMsg("校验失败，数据不合法");
			}else {
				//验证密码
				String md5Password = MD5.getInstance().encryption(password);
				User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
				String yhDm = loginUser.getYhDm();
				Integer user = consumeRecordService.getUser(yhDm, md5Password);
				if(user > 0){
					//解码授权码
					String after_lisence = DES.decryptBasedDes(before_lisence);
					Date key_date = new Date();
					String[] key = after_lisence.split(";");
					if(key.length!=4){
					//	result.setMsg("1");
						result.setMsg("授权码校验失败，请使用合法授权码激活");
					}else {
						//MAC地址
						String ServerMac = getMac();
						if(!"Kuang-Chi".equals(key[0]) && !ServerMac.equals(key[0])){
						//	result.setMsg("2");
							result.setMsg("授权码校验失败，请使用合法授权码激活");
						}else {
							//有效期
							if(key[1].length()!=8){
							//	result.setMsg("2");
								result.setMsg("授权码校验失败，请使用合法授权码激活");
							}else {
								String y = key[1].substring(0, 4); 
								String m = key[1].substring(4, 6);
								String d = key[1].substring(6, 8);
								String keyDate=y+"-"+m+"-"+d+" "+"23:59:59";
								Calendar c = Calendar.getInstance();
								c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyDate));
	//							c.set(Integer.valueOf(y), Integer.valueOf(m)-1, Integer.valueOf(d), 23, 59, 59);
								key_date = c.getTime();
								Date now_date = new Date();	
								if(now_date.compareTo(key_date)>0){
								//	result.setMsg("3");
									result.setMsg("授权码有效期失效，请重新激活");
								}else {
									//客户端数量
									if(-1 == Integer.valueOf(key[3])){	//无限制
										//正常卡数量
										if(-1 == Integer.valueOf(key[2])){	//无限制
											//把lisence写进配置文件中
											String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
											boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
											if(flag){
												result.setSuccess(true);
												result.setMsg("修改成功");
												userService.licenseLog("1", "0", loginUser.getYhMc());
											}else {
												result.setSuccess(false);
												result.setMsg("修改失败");
												userService.licenseLog("1", "1", loginUser.getYhMc());
											}
										}else {
											Integer max_card = Integer.valueOf(key[2]);
											Integer now_card = cardService.getNormalCount();
											if(now_card>max_card){
											//	result.setMsg("5");
												result.setMsg("系统发卡数已超过授权码限制数量，请重新激活");
											}else {
												//把lisence写进配置文件中
												String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
												boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
												if(flag){
													result.setSuccess(true);
													result.setMsg("修改成功");
													userService.licenseLog("1", "0", loginUser.getYhMc());
												}else {
													result.setSuccess(false);
													result.setMsg("修改失败");
													userService.licenseLog("1", "1", loginUser.getYhMc());
												}
											}
										}
									}else {
										ServletContext application = request.getSession().getServletContext();
								        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
										Integer now_client = sessions.size();
										Integer max_client = Integer.valueOf(key[3]);
										if(now_client>=max_client){
										//	result.setMsg("4");
											result.setMsg("目前连接数已超过授权码数量，请重新激活");
										}else {
											//正常卡数量
											if(-1 == Integer.valueOf(key[2])){	//无限制
												//把lisence写进配置文件中
												String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
												boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
												if(flag){
													result.setSuccess(true);
													result.setMsg("修改成功");
													userService.licenseLog("1", "0", loginUser.getYhMc());
												}else {
													result.setSuccess(false);
													result.setMsg("修改失败");
													userService.licenseLog("1", "1", loginUser.getYhMc());
												}
											}else {
												Integer max_card = Integer.valueOf(key[2]);
												Integer now_card = cardService.getNormalCount();
												if(now_card>max_card){
												//	result.setMsg("5");
													result.setMsg("系统发卡数已超过授权码限制数量，请重新激活");
												}else {
													//把lisence写进配置文件中
													String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
													boolean flag = PropertyUtils.setProperties(propertyFile, "lisenceKey", after_lisence, null);
													if(flag){
														result.setSuccess(true);
														result.setMsg("修改成功");
														userService.licenseLog("1", "0", loginUser.getYhMc());
													}else {
														result.setSuccess(false);
														result.setMsg("修改失败");
														userService.licenseLog("1", "1", loginUser.getYhMc());
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}else {
					result.setSuccess(false);
					result.setMsg("密码错误");
				}
			}
		}catch(Exception e){
			result.setSuccess(false);
			e.printStackTrace();
			//7
			result.setMsg("激活失败，请使用合法授权码激活");
		}finally{
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	

}
