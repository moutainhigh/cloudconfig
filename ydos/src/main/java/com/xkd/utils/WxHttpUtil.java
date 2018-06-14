package com.xkd.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import com.xkd.model.TicketJson;
import com.xkd.model.TokenJson;
import com.xkd.model.WxParams;



public class WxHttpUtil {
	public final static String get_token_new = "https://api.weixin.qq.com/cgi-bin/token";
	public final static String get_web_info = "https://api.weixin.qq.com/cgi-bin/user/info";
	public final static String get_web_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public final static String get_web_userinfo = "https://api.weixin.qq.com/sns/userinfo";
	public final static String get_web_code = "https://open.weixin.qq.com/connect/oauth2/authorize";
	public final static String get_token = "https://api.weixin.qq.com/cgi-bin/token";
	public final static String get_ticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    public static String readContentFromPost(String url, String param, String method) throws Exception {
    	
    	System.out.println(method+":"+url+"?"+param);
    	
    	PrintWriter out = null;
    	BufferedReader in = null;
    	
    	String result = "";
    	try {
    		URL postUrl = new URL(url);
        	
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            
            connection.setDoOutput(true);

            connection.setDoInput(true);

            connection.setRequestMethod(method);

            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);
            
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept-Encoding", "UTF-8");
            
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));

//            String headPhoto = FtpUtil.test_imgStr2.replaceAll("\\+", "%2B");
            out.print(param);
            
            out.flush();

            out.close();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            String line;
            while ((line = in.readLine()) != null) {
            	 result += line;
            }
		} catch (Exception e) {
			System.out.println(e);
		} finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static Map<String, String> getAccessToken(String code){
    	Map<String, String> map = new HashMap<String, String>();
    	String openid = null;
    	String access_token = null;
    	 try {
    		 //要在小蝌蚪的域名下访问这个接口才能获取数据，其他域名调用该接口没有数据返回，appid、secret是公司公众号唯一的，code是从腾讯获取的
    		 String url = get_web_access_token+"?appid="+PropertiesUtil.APPID+"&secret="+PropertiesUtil.APPSECRET+"&code="+code+"&grant_type=authorization_code";
         	 String result = readContentFromPost(url, null, "GET");
         	 if(StringUtils.isNotBlank(result)){
	   		 	 JSONObject resultJson = JSONObject.fromObject(result);
	   		 	 boolean errcode = resultJson.has("errcode");
	   		 	 String errmsg = null;
	   		 	 if(!errcode){
	   		 		openid = (String) resultJson.get("openid");
	   		 		access_token = (String) resultJson.get("access_token");
	   		 		map.put("openid", openid);
	   		 		map.put("access_token", access_token);
	   		 	 }else{
	   		 		errmsg = (String) resultJson.get("errmsg");
	   		 		System.out.println("wx返回errcode："+errcode+" errmsg:"+errmsg);
	   		 	 }
	   		 }
         } catch (Exception e) {
        	 System.out.println("获取微信用户access_token发生异常！");
             System.out.println(e);
         }
    	 System.out.println("获取微信用户openid:"+openid);
    	 System.out.println("获取微信用户access_token:"+access_token);
    	 return map;
    }
    public static String getAccessTokenNew(){
    	Map<String, String> map = new HashMap<String, String>();
    	String openid = null;
    	String access_token = null;
    	 try {
    		 String url = get_token_new+"?grant_type=client_credential&appid="+PropertiesUtil.APPID+"&secret="+PropertiesUtil.APPSECRET;
         	 String result = readContentFromPost(url, null, "GET");
         	 if(StringUtils.isNotBlank(result)){
	   		 	 JSONObject resultJson = JSONObject.fromObject(result);
	   		 	 boolean errcode = resultJson.has("errcode");
	   		 	 String errmsg = null;
	   		 	 if(!errcode){
	   		 		access_token = (String) resultJson.get("access_token");
	   		 		map.put("access_token", access_token);
	   		 	 }else{
	   		 		errmsg = (String) resultJson.get("errmsg");
	   		 		System.out.println("wx返回errcode："+errcode+" errmsg:"+errmsg);
	   		 	 }
	   		 }
         } catch (Exception e) {
        	 System.out.println("获取微信用户access_token发生异常！");
             System.out.println(e);
         }
    	 System.out.println("获取微信用户openid:"+openid);
    	 System.out.println("获取微信用户access_token:"+access_token);
    	 return access_token;
    }
    public static Map<String, String> getUserInfo(Map<String, String> map){
    	Map<String, String> userMap = new HashMap<String, String>();
    	String openid = map.get("openid");
    	String access_token = map.get("access_token");
    	 try {
    		 String url = get_web_userinfo+"?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
         	 String result = readContentFromPost(url, null, "GET");
         	 if(StringUtils.isNotBlank(result)){
	   		 	 JSONObject resultJson = JSONObject.fromObject(result);
	   		 	 boolean errcode = resultJson.has("errcode");
	   		 	 String errmsg = null;
	   		 	 if(!errcode){
	   		 		userMap.put("openid", (String) resultJson.get("openid"));
	   		 		userMap.put("nickname", (String) resultJson.get("nickname"));
	   		 		userMap.put("headimgurl", (String) resultJson.get("headimgurl"));
	   		 		userMap.put("subscribe", (String) resultJson.get("subscribe"));
	   		 	 }else{
	   		 		errmsg = (String) resultJson.get("errmsg");
	   		 		System.out.println("wx返回errcode："+errcode+" errmsg:"+errmsg);
	   		 	 }
	   		 }
         } catch (Exception e) {
        	 System.out.println("获取微信用户信息发生异常！");
             System.out.println(e);
         }
    	 return userMap;
    }
    
    public static Map<String, String> getUserInfoByUnId(String openid,String access_token){
    	Map<String, String> userMap = new HashMap<String, String>();
//    	String openid = map.get("openid");
//    	String access_token = map.get("access_token");
    	 try {
    		 String url = get_web_info+"?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
         	 String result = readContentFromPost(url, null, "GET");
         	 if(StringUtils.isNotBlank(result)){
	   		 	 JSONObject resultJson = JSONObject.fromObject(result);
	   		 	 boolean errcode = resultJson.has("errcode");
	   		 	 String errmsg = null;
	   		 	 if(!errcode){
	   		 		userMap.put("openid", (String) resultJson.get("openid"));
	   		 		userMap.put("nickname", (String) resultJson.get("nickname"));
	   		 		userMap.put("headimgurl", (String) resultJson.get("headimgurl"));
	   		 		userMap.put("subscribe", String.valueOf(resultJson.get("subscribe")));
	   		 	 }else{
	   		 		errmsg = (String) resultJson.get("errmsg");
	   		 		System.out.println("wx返回errcode："+errcode+" errmsg:"+errmsg);
	   		 	 }
	   		 }
         } catch (Exception e) {
        	 System.out.println("获取微信用户信息发生异常！");
             System.out.println(e);
         }
    	 return userMap;
    }
    
    public static void getCode(){
    	String redirect_uri = URLEncoder.encode(PropertiesUtil.REDIRECT_URI);
    	 try {
    		 String url = get_web_code+"?appid="+PropertiesUtil.APPID+"&redirect_uri="+redirect_uri+"&response_type="+PropertiesUtil.RESPONSE_TYPE+"&scope="+PropertiesUtil.SCOPE+"&state="+PropertiesUtil.STATE+"#wechat_redirect";
         	 readContentFromPost(url, null, "GET");
         } catch (Exception e) {
        	 System.out.println("获取微信用户code发生异常！");
             System.out.println(e);
         }
    }
    public static TokenJson getToken(){
    	 try {
    		 String url = get_token+"?grant_type=client_credential&appid="+PropertiesUtil.APPID+"&secret="+PropertiesUtil.APPSECRET;
    		 String result = readContentFromPost(url, null, "GET");
    		 JSONObject rqJsonObject = JSONObject.fromObject(result);  
             TokenJson tokenJson = (TokenJson) JSONObject.toBean(rqJsonObject,TokenJson.class);  
             return tokenJson;
         } catch (Exception e) {
        	 System.out.println("获取微信用户code发生异常！");
             System.out.println(e);
             return null;
         }
    }
    public static TokenJson getToken(String APPID,String APPSECRET){
   	 try {
   		 String url = get_token+"?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
   		 String result = readContentFromPost(url, null, "GET");
   		 JSONObject rqJsonObject = JSONObject.fromObject(result);
         TokenJson tokenJson = (TokenJson) JSONObject.toBean(rqJsonObject,TokenJson.class);
            return tokenJson;
        } catch (Exception e) {
       	    System.out.println("获取微信用户code发生异常！");
            System.out.println(e);
            return null;
        }
   }
    public static TicketJson getTicket(String token){
        try {
        	String url = get_ticket+"?access_token="+token+"&type=jsapi";
            String result = readContentFromPost(url, null, "GET");  
            System.out.println("微信服务器获取Ticket:"+result);
            JSONObject rqJsonObject = JSONObject.fromObject(result);
            TicketJson ticket = (TicketJson) JSONObject.toBean(rqJsonObject,TicketJson.class);
            return ticket;  
        } catch (Exception e) {
            System.out.println(e);  
            return null;  
        }  
    }
    
    public static Map<String, String> sign(String jsapi_ticket,String url) {  
      Map<String, String> ret = new HashMap<String, String>();  
      String nonce_str = create_nonce_str();  
      String timestamp = create_timestamp();  
      String string1;  
      String signature = "";  

      //注意这里参数名必须全部小写，且必须有序  
      string1 = "jsapi_ticket=" + jsapi_ticket +  
                "&noncestr=" + nonce_str +  
                "&timestamp=" + timestamp +  
                "&url=" + url;  
//      System.out.println(string1);  
      try  
      {  
          MessageDigest crypt = MessageDigest.getInstance("SHA-1");  
          crypt.reset();  
          crypt.update(string1.getBytes("UTF-8"));  
          signature = byteToHex(crypt.digest());  
      }  
      catch (NoSuchAlgorithmException e)  
      {  
          System.out.println(e);  
      }  
      catch (UnsupportedEncodingException e)  
      {  
          System.out.println(e);  
      }  

      ret.put("url", url);  
      ret.put("jsapi_ticket", jsapi_ticket);  
      ret.put("nonceStr", nonce_str);  
      ret.put("timestamp", timestamp);  
      ret.put("signature", signature);  

      return ret;  
  }  
    public static Map<String, String> sign(String url,String APPID,String APPSECRET) {  
    	
    	  //处理token失效的问题  
        try {  
            long tokenTimeLong = Long.parseLong(WxParams.tokenTime);  
            long tokenExpiresLong = Long.parseLong(WxParams.tokenExpires);  
              
            //时间差  
            long differ = (System.currentTimeMillis() - tokenTimeLong) /1000;  
            if (WxParams.token == null ||  differ > (tokenExpiresLong - 1800)) {  
                System.out.println("token为null，或者超时，重新获取");  
                TokenJson tokenJson = WxHttpUtil.getToken(APPID,APPSECRET);
                System.out.println(tokenJson.toString());
                if (tokenJson != null) {  
                    WxParams.token = tokenJson.getAccess_token();  
                    WxParams.tokenTime = System.currentTimeMillis()+"";  
                    WxParams.tokenExpires = tokenJson.getExpires_in()+"";  
                }
                System.out.println("获取："+WxParams.token);
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
            System.out.println(e);  
            TokenJson tokenJson = WxHttpUtil.getToken(APPID,APPSECRET);  
            if (tokenJson != null) {  
                WxParams.token = tokenJson.getAccess_token();  
                WxParams.tokenTime = System.currentTimeMillis()+"";  
                WxParams.tokenExpires = tokenJson.getExpires_in()+"";  
            }  
        }  
  
        //处理ticket失效的问题  
        try {  
            long ticketTimeLong = Long.parseLong(WxParams.ticketTime);  
            long ticketExpiresLong = Long.parseLong(WxParams.ticketExpires);  
              
            //时间差  
            long differ = (System.currentTimeMillis() - ticketTimeLong) /1000;  
            if (WxParams.ticket == null ||  differ > (ticketExpiresLong - 1800)) {  
                System.out.println("ticket为null，或者超时，重新获取");  
                TicketJson ticketJson = WxHttpUtil.getTicket(WxParams.token);  
                if (ticketJson != null) {  
                    WxParams.ticket = ticketJson.getTicket();  
                    WxParams.ticketTime = System.currentTimeMillis()+"";  
                    WxParams.ticketExpires = ticketJson.getExpires_in()+"";  
                }  
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
            System.out.println(e); 
            TicketJson ticketJson = WxHttpUtil.getTicket(WxParams.token);  
            if (ticketJson != null) {  
                WxParams.ticket = ticketJson.getTicket();  
                WxParams.ticketTime = System.currentTimeMillis()+"";  
                WxParams.ticketExpires = ticketJson.getExpires_in()+"";  
            }  
        } 
        
        
        
        Map<String, String> ret = new HashMap<String, String>();  
        String nonce_str = create_nonce_str();  
        String timestamp = create_timestamp();  
        String string1;  
        String signature = "";  
  
        //注意这里参数名必须全部小写，且必须有序  
        string1 = "jsapi_ticket=" + WxParams.ticket +  
                  "&noncestr=" + nonce_str +  
                  "&timestamp=" + timestamp +  
                  "&url=" + url;  
//        System.out.println(string1);  
        try  
        {  
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");  
            crypt.reset();  
            crypt.update(string1.getBytes("UTF-8"));  
            signature = byteToHex(crypt.digest());  
        }  
        catch (NoSuchAlgorithmException e)  
        {  
            System.out.println(e);  
        }  
        catch (UnsupportedEncodingException e)  
        {  
            System.out.println(e);  
        }  
  
        ret.put("url", url);  
        ret.put("jsapi_ticket", WxParams.ticket);  
        ret.put("nonceStr", nonce_str);  
        ret.put("timestamp", timestamp);  
        ret.put("signature", signature);  
  
        return ret;  
    }  
    
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();  
        for (byte b : hash)  
        {  
            formatter.format("%02x", b);  
        }  
        String result = formatter.toString();  
        formatter.close();  
        return result;  
    }  
  
    private static String create_nonce_str() {  
        return UUID.randomUUID().toString();  
    }  
  
    private static String create_timestamp() {  
        return Long.toString(System.currentTimeMillis() / 1000);  
    }
    public static void main(String[] args) {
        try {
//        	String result = getAccessToken();
        	System.out.println(readContentFromPost(null,null,"GET"));
//        	String result = createQrcode("pB7MiwIteoMEe8qCbzBiw7KEoexk", "19911127feifei");
//        	String result = getOpenid("0319LJaz0wKAbl1Lx78z0EaDaz09LJan");
        	//oB7MiwNdafGqRe0abljDcV6vSobA
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
