package com.xkd.utils;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WeixinCorpUtil {

    public static final String get_corp_weixin_access_token = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    public static final String get_corp_weixin_department_list = "https://qyapi.weixin.qq.com/cgi-bin/department/list";
    public static final String corp_weixin_create_department = "https://qyapi.weixin.qq.com/cgi-bin/department/create";
    public static final String corp_weixin_delete_department = "https://qyapi.weixin.qq.com/cgi-bin/department/delete";
    public static final String get_corp_weixin_department_user = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist";
    public static final String get_corp_weixin_agent = "https://qyapi.weixin.qq.com/cgi-bin/agent/get";
    public static final String corp_weixin_send_message = "https://qyapi.weixin.qq.com/cgi-bin/message/send";

    /**
     * 获得企业微信的access_token，如果修改通讯录的部门、人员信息，就要获取编辑权限的access_token
     * @return
     */
    public static String getAccessTokenByUpdate(){
        String access_token = null;
        try {
            //corpid、corpsecret是唯一的
            /*
             *新增修改部门，要用通讯录同步里面的secret，普通的secret只能查询信息，不能编辑
             */
            //String url = get_corp_weixin_access_token+"?corpid=ww60583bfe4e343c0d&corpsecret=mOf2h7W5KCjXV-znKcnT4faPv6_mulzlHiA0Y0-l8jE";
            String url = get_corp_weixin_access_token+"?corpid=ww60583bfe4e343c0d&corpsecret=9gt-f6Gnn1I9aiKGv79-9ebfmKNUFZYmc3MyZDNz6rk";
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                JSONObject resultJson = JSONObject.fromObject(result);
                Integer errcode = (Integer) resultJson.get("errcode");
                String errmsg = null;
                if(0 == errcode.intValue()){
                    access_token = (String) resultJson.get("access_token");
                }else{
                    errmsg = (String) resultJson.get("errmsg");
                    System.out.println("企业微信返回errcode："+errcode+" errmsg:"+errmsg);
                    return "企业微信返回errcode："+errcode+" errmsg:"+errmsg;
                }
            }
        } catch (Exception e) {
            System.out.println("获取企业微信access_token发生异常！");
            e.printStackTrace();
            return "获取企业微信修改权限的access_token发生异常！";
        }
        System.out.println("获取企业微信access_token:"+access_token);
        return access_token;
    }

    public static String getAccessToken(){
        String access_token = null;
        try {
            //corpid、corpsecret是唯一的
            String url = get_corp_weixin_access_token+"?corpid=ww60583bfe4e343c0d&corpsecret=mOf2h7W5KCjXV-znKcnT4faPv6_mulzlHiA0Y0-l8jE";
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                JSONObject resultJson = JSONObject.fromObject(result);
                Integer errcode = (Integer) resultJson.get("errcode");
                String errmsg = null;
                if(0 == errcode.intValue()){
                    access_token = (String) resultJson.get("access_token");
                }else{
                    errmsg = (String) resultJson.get("errmsg");
                    System.out.println("企业微信返回errcode："+errcode+" errmsg:"+errmsg);
                    return "企业微信返回errcode："+errcode+" errmsg:"+errmsg;
                }
            }
        } catch (Exception e) {
            System.out.println("获取企业微信access_token发生异常！");
           e.printStackTrace();
            return "获取企业微信access_token发生异常！";
        }
        System.out.println("获取企业微信access_token:"+access_token);
        return access_token;
    }

    /**
     * 获取部门列表
     * @param accessToken
     * @param departmentId,部门ID为空则查询全部部门
     * @return
     */
    public static Map<String,Object> getDepartmentList(String accessToken,Integer departmentId){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = get_corp_weixin_department_list+"?access_token="+accessToken+"&id="+departmentId;
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            System.out.println("获取部门发生异常！");
            e.printStackTrace();
        }
            return resultJson;
    }

    /**
     *
     * @param accessToken
     * @param departmentId
     * @param fetchChild  1/0：是否递归获取子部门下面的成员
     * @return
     */
    public static Map<String,Object> getDepartmentUser(String accessToken,Integer departmentId,int fetchChild){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = get_corp_weixin_department_user+"?access_token="+accessToken+"&department_id="+departmentId+"&fetch_child="+fetchChild;
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            System.out.println("获取部门发生异常！");
            e.printStackTrace();
        }
        return resultJson;
    }

    /**
     * 获取企业应用
     * @param accessToken
     * @param agentId 企业应用ID
     * @return
     */
    public static Map<String,Object> getAgentById(String accessToken,String agentId){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = get_corp_weixin_agent+"?access_token="+accessToken+"&agentid="+agentId;
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            System.out.println("获取应用发生异常！");
           e.printStackTrace();
        }
        return resultJson;
    }

    /**
     * 删除部门
     * @param accessToken
     * @param departmentId,部门ID
     * @return
     */
    public static boolean deleteDepartmentById(String accessToken,int departmentId){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = corp_weixin_delete_department+"?access_token="+accessToken+"&id="+departmentId;
            String result = readContentFromPost(url, null, "GET");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
                Integer errcode = (Integer) resultJson.get("errcode");
                String errmsg = null;
                if(0 == errcode.intValue()){
                    return true;
                }else{
                    errmsg = (String) resultJson.get("errmsg");
                    System.out.println("企业微信返回errcode："+errcode+" errmsg:"+errmsg);
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("删除部门发生异常！");
           e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取部门列表
     * @param accessToken
     * @param departmentMap
     * {
        "name": "广州研发中心",
        "parentid": 1,//0表示一级菜单
        "order": 1,
        "id": 2
        }
     * @return
     */
    public static boolean createDepartment(String accessToken,Map<String,Object> departmentMap){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = corp_weixin_create_department+"?access_token="+accessToken;
            String departmentMapStr = JSON.toJSONString(departmentMap);
            String result = readContentFromPost(url,departmentMapStr, "POST");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
                Integer errcode = (Integer) resultJson.get("errcode");
                if(0 == errcode.intValue()){
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("创建部门发生异常！");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 推送消息
     * @param accessToken
     * @param messageMap
     * {
        "touser" : "UserID1|UserID2|UserID3",
        "toparty" : "PartyID1|PartyID2",
        "totag" : "TagID1 | TagID2",
        "msgtype" : "text",
        "agentid" : 1,
        "text" : {
        "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"
            },
        "safe":0
     }
     * @return
     */
    public static boolean sendMessage(String accessToken,Map<String,Object> messageMap){
        JSONObject resultJson = null;
        try {
            //corpid、corpsecret是唯一的
            String url = corp_weixin_send_message+"?access_token="+accessToken;
            String departmentMapStr = JSON.toJSONString(messageMap);
            String result = readContentFromPost(url,departmentMapStr, "POST");
            if(StringUtils.isNotBlank(result)){
                resultJson = JSONObject.fromObject(result);
                Integer errcode = (Integer) resultJson.get("errcode");
                if(0 == errcode.intValue()){
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("推送信息发生异常！");
           e.printStackTrace();
        }
        return false;
    }


    /**
     *  组装成post请求
     * @param url
     * @param param
     * @param method,GET,POST
     * @return
     * @throws Exception
     */
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
            out.print(param);
            out.flush();
            out.close();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

    //解析xml字符串
    public static  Document parseStr(String xmlStr) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        return doc;
    }


    public static void main(String[] args){

       /* System.out.println(new Date().getTime()+"");

        String access_token = getAccessToken();
        System.out.println(access_token);

        Map<String,Object> map = getDepartmentList(access_token,null);
        System.out.println(map);

        Map<String,Object> map1 =  getDepartmentUser(access_token,2,0);
        System.out.println(map1);

        Map<String,Object> map2 =  getAgentById(access_token,"1000006");
        System.out.println(map2);*/

        //添加部门
      /*  Map<String,Object> departmentMap = new HashMap<>();
        departmentMap.put("id",6);
        departmentMap.put("name","测试部门");
        departmentMap.put("parentid",1);
        departmentMap.put("order",99996000);

        boolean flag = createDepartment(getAccessTokenByUpdate(),departmentMap);

        System.out.println(flag);*/

        //修改部门要用有修改权限的secret的accessToken
        /*boolean flag = deleteDepartmentById(getAccessTokenByUpdate(),6);
        System.out.println(flag);*/

        //推送消息
        /*
        {
        "touser" : "UserID1|UserID2|UserID3",
        "toparty" : "PartyID1|PartyID2",
        "totag" : "TagID1 | TagID2",
        "msgtype" : "text",
        "agentid" : 1,
        "text" : {
        "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"
            },
        "safe":0
     }
         */

        /*Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("touser","GaoShiWei|XiaoZheng|FangShaoJun");
        messageMap.put("toparty","2");
        messageMap.put("totag",null);
        messageMap.put("msgtype","text");
        messageMap.put("agentid",1000006);

        Map<String,Object> textMap = new HashMap<>();
        textMap.put("content","消息推送，测试的，请不要在意");

        messageMap.put("safe",0);
        messageMap.put("text",textMap);
        sendMessage(access_token,messageMap);*/


        String s = "<xml><ToUserName><![CDATA[ww60583bfe4e343c0d]]></ToUserName>" +
                "<FromUserName><![CDATA[XiaoZheng]]></FromUserName><CreateTime>1515064694</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType><Content><![CDATA[test]]></Content><MsgId>464075743</MsgId>" +
                "<AgentID>1000006</AgentID></xml>";




            String userId = s.substring(s.indexOf("<FromUserName><![CDATA[")+"<FromUserName><![CDATA[".length(),s.indexOf("]]></FromUserName>"));
            String corpID = s.substring(s.indexOf("<ToUserName><![CDATA[")+"<ToUserName><![CDATA[".length(),s.indexOf("]]></ToUserName>"));
            String agentID = s.substring(s.indexOf("<AgentID>")+"<AgentID>".length(),s.indexOf("</AgentID>"));
            String msgType = s.substring(s.indexOf("<MsgType><![CDATA[")+"<MsgType><![CDATA[".length(),s.indexOf("]]></MsgType>"));
            String content = s.substring(s.indexOf("<Content><![CDATA[")+"<Content><![CDATA[".length(),s.indexOf("]]></Content>"));



    }


}
