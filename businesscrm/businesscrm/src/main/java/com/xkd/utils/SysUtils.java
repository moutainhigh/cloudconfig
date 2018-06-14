package com.xkd.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.ResultSetMetaData;
import com.xkd.model.DC_User;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

public class SysUtils {
	//获得token的接口，一天可以调用几千上万次吧
	public final static String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?";
	public final static String url = PropertiesUtil.DB_URL;
	public final static String username = PropertiesUtil.DB_USER;
	public final static String password = PropertiesUtil.DB_PWD;
	
	
	

	
	/**
	 * 获取微信服务号的token
	 * @return token微信token
	 */
	public static String getWeixinToken() {
		HttpRequest request = new HttpRequest();
		String access_token = null;
		String params = "appid="+PropertiesUtil.APPID+"&secret="+PropertiesUtil.APPSECRET+"&grant_type=client_credential";
		String url = GET_TOKEN_URL + params;
		try {
			System.out.println("----------------------------------------------------");
			System.out.println(url);
			String result = request.getData(url, "UTF-8");
			JsonParser parse =new JsonParser();  //创建json解析器
			JsonObject json = (JsonObject) parse.parse(result);  //创建jsonObject对象
			access_token = json.get("access_token").getAsString();
			System.out.println(access_token);
		} catch (Exception e) {
			System.out.println(e);
		}
		return access_token;
	}
	
	public static void main(String[] args) {
		getWeixinToken();
	}
	
	/**
	 * 根据openid获取用户微信信息（昵称，头像，性别，省份）
	 * @param openId
	 * @return
	 */
	public static Map<Object,Object> getWeiXinInfoByOpenid(String openId){
		
		HttpRequest request = new HttpRequest();
		//https://api.weixin.qq.com/cgi-bin/token?''''''可以得到access_token,因为access_token每隔2小时失效，所以最好不断的从服务器获取，单每天有次数限制大概几千上万次，
		//access_token实际上是一个令牌，让客户端和服务器端进行匹配，openId通过code获得，openId和access_token可以获得微信用户头像、昵称、性别、国家、省份、城市、openid(用户的唯一标识)、unionid的信息
		String wxToken = getWeixinToken();
		
		Map<Object,Object> map = new HashMap<Object,Object>();
		
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+wxToken+"&openid="+openId+"&lang=zh_CN";
		JsonParser parse =new JsonParser();  //创建json解析器
		try {
			System.out.println(url);
			String result = request.getData(url, "UTF-8");
			System.out.println(result);
			JsonObject json = (JsonObject) parse.parse(result);  //创建jsonObject对象
			String openid = json.get("openid").getAsString();
			Object nickname=json.get("nickname");
			if(nickname != null && !StringUtils.isBlank(nickname+"")){
				String sex = json.get("sex").getAsString();
				String headimgurl = json.get("headimgurl").getAsString();
				map.put("nickName", nickname);
				map.put("sex", sex);
				map.put("headImgUrl", headimgurl);
				map.put("province", json.get("province").getAsString());
				map.put("city", json.get("city").getAsString());
				map.put("country", json.get("country").getAsString());
			}			
			map.put("openId", openid);
			
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getMessage());
		}
		
		return map;
	}
	
	
	/**
	 * 执行数据库Mysql语句
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Object selectTablesDropdown(String sql) {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;

		List<String> tables = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int count=rsmd.getColumnCount();
			while(rs.next()){
				String column = null;
				for(int i=1;i<=count;i++){
					column = column + "    " + rs.getString(i);
				}
				
				tables.add(column);				
			}
			return tables;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
public static HashMap<String, String> getOpenId(String code) {
		
		String openId = null;
		
		String APPID = PropertiesUtil.APPID;
		String SECRET = PropertiesUtil.APPSECRET;
		String GRANT_TYPE = "authorization_code";
		
		
		HttpRequest request = new HttpRequest();
		
		
		String params = "appid="+APPID+"&secret="+SECRET+"&code="+code+"&grant_type="+GRANT_TYPE;
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + params;
		try {
			String result  = request.getData(url, "UTF-8");
			System.out.println("获取openId:"+result);

			Map<String, String> map2 = (Map<String, String>) JSON.parseObject(result, Object.class);
			if(map2.get("access_token") == null){
				return null;
			}
			
			//得到openId
			openId = map2.get("openid");
			String unionid = map2.get("unionid");
			
			HashMap<String, String> map = new HashMap<>();
			map.put("openId", openId);
			map.put("unionid", unionid);
			map.put("wxToken", map2.get("access_token"));
			System.out.println("openId:"+openId);
			System.out.println("wxToken:"+map.get("wxToken"));
			return map;
		}catch (Exception e) {
			return null;
		}
	}
public static Map<String,String> getWeiXinInfoByOpenid(String wxToken,String openId){
	HttpRequest request = new HttpRequest();
	//https://api.weixin.qq.com/cgi-bin/token?''''''可以得到access_token,因为access_token每隔2小时失效，所以最好不断的从服务器获取，单每天有次数限制大概几千上万次，
	//access_token实际上是一个令牌，让客户端和服务器端进行匹配，openId通过code获得，openId和access_token可以获得微信用户头像、昵称、性别、国家、省份、城市、openid(用户的唯一标识)、unionid的信息
	//String wxToken = getWeixinToken1(code);
	Map<String, String> map = new HashMap<String, String>();

	//String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + wxToken + "&openid=" + openId+"&lang=zh_CN";
	String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + wxToken + "&openid=" + openId+"&lang=zh_CN";
	JsonParser parse = new JsonParser(); // 创建json解析器
	try {
		System.out.println(url);
		String result = request.getData(url, "UTF-8");
		System.out.println(result);
		JsonObject json = (JsonObject) parse.parse(result); // 创建jsonObject对象
		String openid = json.get("openid").getAsString();
		String nickname = json.get("nickname").toString();
		if (nickname != null && !StringUtils.isBlank(nickname + "")) {
			String sex = json.get("sex").getAsString();
			String headimgurl = json.get("headimgurl").getAsString();

			map.put("nickName", nickname.substring(1, nickname.length()-1));
			map.put("sex", sex.equals("0")?"女":"男");
			map.put("headImgUrl", headimgurl);
			map.put("province", json.get("province").getAsString());
			map.put("city", json.get("city").getAsString());
			map.put("country", json.get("country").getAsString());
		}
		map.put("openId", openid);

	} catch (Exception e) {
	e.printStackTrace();
	}

	return map;
}
	/**
	 * 执行数据库Mysql语句
	 * @param sql
	 * @param params
	 * @return
	 */
	@Transactional
	public static void executeWjDataSql(Map<String, List<Map<String, String>>> dataMap, String uidKey,String meetingid,Object mobile,Object companyName) {
		Connection con = null;
		Statement statement = null;
		
		String uuid = UUID.randomUUID().toString();//企业ID
		String userid = UUID.randomUUID().toString();//User_infoID
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			statement = con.createStatement();
			String meetId = meetingid;
			
			//String sqlQueryUserInfo = "select companyid,id from dc_user_info where userid = '"+uidKey+"'";
			String sqlQueryUserInfo ="select u.id,m.companyid from dc_user_info u LEFT JOIN dc_meeting_user m on m.userid = u.id LEFT JOIN dc_company c on c.id = m.companyid where m.meetingid = '"+meetId+"' and (u.mobile = '"+mobile+"' or m.uid = '"+uidKey+"') and m.userid = u.id and m.ustatus = 0 and c.status = 0 limit 1";
			if(mobile ==null ||StringUtils.isBlank(mobile.toString())){
				sqlQueryUserInfo ="select u.id,m.companyid from dc_user_info u LEFT JOIN dc_meeting_user m on m.userid = u.id LEFT JOIN dc_company c on c.id = m.companyid  where m.meetingid = '"+meetId+"' and  m.uid = '"+uidKey+"' and m.userid = u.id and m.ustatus = 0 and c.status = 0 limit 1";
			}
			System.out.println(sqlQueryUserInfo);
			ResultSet rs = statement.executeQuery(sqlQueryUserInfo);
			Boolean isUpdateCompany = false;
			Boolean isUpdateUserInfo = false;
			Boolean isSaveUserInfo = true;
			Boolean isUpdateMeetingUser = false;
			
			if(rs.next()){
				String userinfoid = rs.getString("id");
				String companyid = rs.getString("companyid");
				isUpdateCompany = StringUtils.isBlank(companyid) ? false:true;
				isUpdateUserInfo = StringUtils.isBlank(userinfoid) ? false:true;
				System.out.println("数据库已经有的：userinfoId="+userinfoid+"   companyid="+companyid);
				userid = userinfoid == null || StringUtils.isBlank(userinfoid) ? userid :userinfoid;
				uuid = companyid == null || StringUtils.isBlank(companyid) ? uuid :companyid;
				
			}else{
				System.out.println("UUID自动生成：userinfoId="+userid+"   companyid="+uuid);
			}
			
			if(null != companyName){
				String queryCompanyByName = "select id from dc_company  where company_name = '"+companyName+"'  limit 1";
				ResultSet rsCompany = statement.executeQuery(queryCompanyByName);
				System.out.println("查询用户所填的企业是否存在"+queryCompanyByName);
				if(rsCompany.next()){
					uuid = rsCompany.getString("id");
					isUpdateCompany = true;
					
					System.out.println("用户所填的企业存在，所以不创建企业。直接将用户="+uidKey+"客户="+userid+"添加到企业id="+uuid+"下");
				}
			}
			
			String sqlQueryMeetingUser = "select userid,companyid,meetingid,status from dc_meeting_user where userid = '"+userid+"' and companyid = '"+uuid+"' and meetingid = '"+meetId+"'";
			ResultSet rs_mu = statement.executeQuery(sqlQueryMeetingUser);
			if(rs_mu.next()){
				if(StringUtils.isNotBlank(rs_mu.getString("companyid"))){
					isUpdateMeetingUser = true;
				}
			}
			
			
			//保存到相应表中
			for (Entry<String, List<Map<String, String>>> entry : dataMap.entrySet()) {
				StringBuilder colBuild = new StringBuilder(" ");
				StringBuilder valBuild = new StringBuilder(" ");
				StringBuilder updateBuild = new StringBuilder(" ");
				
				String sql = null;
				
				String table = entry.getKey();
				List<Map<String, String>> colList = entry.getValue();
				for (Map<String, String> map2 : colList) {
					for (Entry<String, String> col : map2.entrySet()) {						
						String value = col.getValue().replace("'", "");
						if("dc_label".equalsIgnoreCase(table)){
							sql = "REPLACE into dc_label (uid,cid,ttype,labels) values ('" + userid + "','" + uuid + "',"
									+ col.getKey() +",'" + value + "') ";
							statement.execute(sql);
							System.out.println("dc_label:"+sql);
						}else{
							String colm = col.getKey();
							colBuild.append(colm+",");
							valBuild.append(StringUtils.isBlank(col.getValue()) ? "''," : ("'"+value +"',"));
							if(StringUtils.isNotBlank(col.getValue())){
								updateBuild.append(colm+"='"+value +"',");
							}
						}
					}
				}
				if(!"dc_label".equalsIgnoreCase(table)){
					
					if("dc_company".equalsIgnoreCase(table)){
						if(isUpdateCompany){
							sql = "update "+table+" set status=0,learnStatus=1,"+updateBuild.substring(0,updateBuild.length()-1)+" where id = '"+uuid+"'";
						}else{
							sql = "insert into " + table +"( id,status,learnStatus," + colBuild.substring(0,colBuild.length()-1) 
							+ ") values ('" + uuid + "',0,1," + valBuild.substring(0,valBuild.length()-1) + ") ";
						}
						
						/*boolean solrFlag = solrService.updateIndex(insertCompanyId, 1);
						
						if(!solrFlag){
							
							SolrLogger.loggerInfo("企业名称："+companyName+",企业ID："+insertCompanyId+"----该企业更新索引库失败");
						}*/
						
						System.out.println(table + "  :  " + sql);
					}else if("dc_user_info".equalsIgnoreCase(table)){
						if(isUpdateUserInfo){
							sql = "update "+table+" set companyid = '"+uuid+"',"+updateBuild.substring(0,updateBuild.length()-1)+" where id = '"+userid+"'";
						}else{
							sql = "insert into " + table +"( id,userid,companyid,uflag,status," + colBuild.substring(0,colBuild.length()-1) 
							+ ") values ('" + userid + "','" + uidKey + "','" + uuid + "',1,0," + valBuild.substring(0,valBuild.length()-1) + ") ";
						}
						isSaveUserInfo = false;
						
						System.out.println(table + "  :  " + sql);
					}else if("dc_company_project".equalsIgnoreCase(table)){
						sql = "insert into " + table +"( userid,companyid," + colBuild.substring(0,colBuild.length()-1) 
						+ ") values ('" + uidKey + "','" + uuid + "'," + valBuild.substring(0,valBuild.length()-1) + ") ";
						System.out.println(table + "  :  " + sql);
					}else if("dc_meeting_user".equalsIgnoreCase(table)){
						if(isUpdateMeetingUser){
							sql = "update "+table+" set uid='"+uidKey+"',ustatus='0',"+updateBuild.substring(0,updateBuild.length()-1)+" where companyid = '"+uuid+"' and userid = '"+userid+"' and meetingid = '"+meetId+"'";
						}else{
							isUpdateMeetingUser = true;
							sql = "insert into " + table +"( uid,userid,companyid,meetingid,updateTime,status,ustatus," + colBuild.substring(0,colBuild.length()-1) 
							+ ") values ('"+uidKey+"','" + userid + "','" + uuid + "','"+meetId+"','" + DateUtils.currtime() + "','未审核','0'," + valBuild.substring(0,valBuild.length()-1) + ") ";
						}
						System.out.println(table + "  :  " + sql);
					}else if("dc_user_address".equalsIgnoreCase(table)){
						if(isUpdateCompany){
							String sqlQueryUserAddres = "select id from dc_user_address where userid = '"+uuid+"' and utype = 1";
							ResultSet rs_ua =  statement.executeQuery(sqlQueryUserAddres);
							if(rs_ua.next()){
								sql = "update "+table+" set "+updateBuild.substring(0,updateBuild.length()-1)+" where id = '"+rs_ua.getString("id")+"'";
							}else{
								sql = "insert into " + table +"(userid,utype," + colBuild.substring(0,colBuild.length()-1) 
								+ ") values ('" + uuid + "','1'," + valBuild.substring(0,valBuild.length()-1) + ")";
							}
						}else{
							sql = "insert into " + table +"(userid,utype," + colBuild.substring(0,colBuild.length()-1) 
							+ ") values ('" + uuid + "','1'," + valBuild.substring(0,valBuild.length()-1) + ")";
						}
						System.out.println(table + "  :  " + sql);
					}else{
						sql = "REPLACE into " + table +"(userid," + colBuild.substring(0,colBuild.length()-1) 
						+ ") values ('" + uidKey + "'," + valBuild.substring(0,valBuild.length()-1) + ")";
						System.out.println(table + "  :  " + sql);
					}
					statement.execute(sql);
				}
			}
			if(isUpdateMeetingUser == false){
				String meetSql = "insert into dc_meeting_user(uid,userid,companyid,attendmeeting_time,meetingid,status,ustatus) "
						+ "values ('"+uidKey+"','" + userid + "','" + uuid + "','" + DateUtils.currtime() + "','" + meetId + "','未审核','0')";
				System.out.println("最后一条meeting_user sql："+meetSql);
				statement.execute(meetSql);
			}
			if(isSaveUserInfo && isUpdateUserInfo == false){
				String userInfoSql = "insert into dc_user_info( id,userid,companyid,uflag,status"
				+ ") values ('" + userid + "','" + uidKey + "','" + uuid + "',1,0) ";
				System.out.println("最后一条user_nfo sql："+userInfoSql);
				statement.execute(userInfoSql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				statement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
