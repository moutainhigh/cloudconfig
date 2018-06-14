package com.xkd.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xkd.mapper.ObjectNewsMapper;
import com.xkd.utils.DateUtils;
import com.xkd.utils.MD5Util;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.util.*;

@Service
public class ObjectNewsService {

    @Autowired
    private ObjectNewsMapper objectNewsMapper;


    @Autowired
    private AllowHistoryService allowHistoryService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    ApiCallFacadeService apiCallFacadeService;


    @Autowired
    DepartmentService departmentService;

    /**
     * 保存消息,推送消息
     * @param ObjectNews
     * Map里面的pushFlag字段
     * @return
     *
     *  Map<String,Object> p = new HashedMap();
     *  p.put("id",UUID.randomUUID().toString());
        p.put("objectId",id);
        p.put("appFlag",2); 2 服务端，3技师端，4客户端
        p.put("userId",technicianId);
        p.put("title","工单待领取");
        p.put("content","接收到新的维修工单任务"+repaireNo+"，请及时领取。");
        p.put("createDate",new Date());
        p.put("createdBy",loginUserId);
        p.put("flag",0);  0未读，1已读
        p.put("newsType",0);//0维修报修,1设备消息
        p.put("status",0);
        p.put("pushFlag","0"); "0"激光推送消息，"1"激光不推送消息
     *
     */
    public Integer saveObjectNews(List<Map<String,Object>> ObjectNews) throws Exception{


        if(ObjectNews != null && ObjectNews.size() > 0){
            //因为app端推送还没有做好，先注销掉
        for(Map<String,Object> map:ObjectNews){
            Object userId = map.get("userId");
            Object title = map.get("title");
            Object content = map.get("content");
            Integer appFlag = (Integer)map.get("appFlag");
            Object pushFlag = map.get("pushFlag");
            //服务端的用户
            if(userId != null && pushFlag != null && pushFlag != null && "0".equals(pushFlag.toString())){
                pushMessage(PropertiesUtil.YOUDIAN_LIGHTPUSH_IP,new Date(),PropertiesUtil.YOUDIAN_LIGHTPUSH_TOKEN,userId.toString(),"",
                "",title == null?"":title.toString(),content == null?"":content.toString(),"YD",
                appFlag.toString(),"1","0","0","0");
            }
        }


            if (ObjectNews.size()>0) {
                return objectNewsMapper.saveObjectNews(ObjectNews);
            }else {
                return 0;
            }
        }else{
            return 0;
        }




    }

    /**
     * 统一消息推送接口：http://120.24.47.184:6969/message/push

     参数：{timemodel："",token: "",data:{user_id:"",phone:"",email:"",title:"",content:"",company:"",appType:"",is_push:"",is_sms:"",is_voice:"",is_email:""}}

     timemodel:时间戳      例："2018-01-18 13:14:18"     Str
     token:加密校验            通过时间戳跟约定秘钥（"#@OD)kkOK$5$66#1@jPE%$^&*(I!~"）MD5得出   Str
     user_id:接收推送的用户id   Str
     phone：用户电话号码        Str
     email：用户邮箱            Str
     title：推送标题            Str
     content：推送内容          Str
     company：公司标识     (用于区分不同公司的短信签名) 目前固定值"YD"    Str
     appType: app类型      （区分app推送时是哪个端的极光key;"0"客户端，"1"技师端，"2"服务商端） Str
     is_push：是否app内推送  （0否1是）  int
     is_sms：是否短信推送    （0否1是）  int
     is_voice：是否电话通知   （0否1是）  int
     is_email：是否发送邮件   （0否1是）  int

     统一推送接口，参数不一定要传值但字段一定要有；可发送设备的告警推送，报修推送、续签提醒、任务发布以及系统消息推送等等全部的需推送操作。

     响应：{"code":0,"data":{},"msg":"success"}；{"code":1,"data":{},"msg":"contenttype is not json"}
     code: 0成功；1失败
     msg：接口调用详情信息
     */
    public static String pushMessage(String url,Date timemodel,String token, String userId,String phone,String email,
                                          String title,String content,String company,String appType,String is_push,String is_sms,String is_voice,String is_email){

        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        DataOutputStream  out = null;
        try {
            postUrl = new URL(url);
            connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            //设置本次连接是否自动重定向
            connection.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            out = new DataOutputStream(connection
                    .getOutputStream());
            /*out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));*/
            String nowStr = DateUtils.dateToString(timemodel,"yyyy-MM-dd HH:mm:ss");
            //String token = nowStr+"#@OD)kkOK$5$66#1@jPE%$^&*(I!~";
            String tokenStr = nowStr+token;
            String md5Token = MD5Util.MD5Encode(tokenStr,null);

            Map<String,Object> paramMap = new HashedMap();
            Map<String,Object> dataMap = new HashedMap();
            dataMap.put("user_id",userId);
            dataMap.put("phone",phone);
            dataMap.put("email",email);
            dataMap.put("title",title);
            dataMap.put("content",content);
            dataMap.put("company",company);
            dataMap.put("appType",appType);
            dataMap.put("is_push",Integer.parseInt(is_push));
            dataMap.put("is_sms",Integer.parseInt(is_sms));
            dataMap.put("is_voice",Integer.parseInt(is_voice));
            dataMap.put("is_email",Integer.parseInt(is_email));

            paramMap.put("data",dataMap);
            paramMap.put("timemodel",nowStr);
            paramMap.put("token",md5Token);
            String contentStr = JSONObject.toJSONString(paramMap);

            out.write(contentStr.getBytes("UTF-8"));
            //获取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            int code = jsonObject.getInteger("code");
            if(0 != code){
                System.out.println(jsonObject.getString("msg"));
                //返回失败
                return "1,"+jsonObject.getString("msg");
            }else{
                //返回成功
                return "0";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //流用完记得关
                if(out != null){
                    out.close();
                }
                if(reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //该干的都干完了,记得把连接断了
            if(connection != null){
                connection.disconnect();
            }
        }
        return "1";
    }

    public static void main(String args[]){
        //pushMessage(PropertiesUtil.YOUDIAN_LIGHTPUSH_IP,new Date(),PropertiesUtil.YOUDIAN_LIGHTPUSH_TOKEN,"818","15889421186","745550948@qq.com","ces","cesde","YD", "0","1","0","0","0");
        pushMessage(PropertiesUtil.YOUDIAN_LIGHTPUSH_IP,new Date(),PropertiesUtil.YOUDIAN_LIGHTPUSH_TOKEN,"15287027351","15889421186","745550948@qq.com","ces1","cesde11","YD", "2","1","0","0","0");
    }


    /**
     *
     * @param userId
     * @param appFlag 2：服务端，3：技师端，4：客户端
     * @param newsType,0维修保修消息、1设备消息 2 邀请消息
     * @return
     */
    public List<Map<String,Object>> selectObjectNewsByUserId(String userId,String appFlag,List<Integer> newsTypeList,int pageSize,int currentPage) {

        /*
        0：服务端，1：技师端，2：客户端
         */
        if("2".equals(appFlag)){
            return objectNewsMapper.selectObjectNewsByUserId(userId,2, newsTypeList,pageSize,currentPage);
        }else if("3".equals(appFlag)){
            return objectNewsMapper.selectObjectNewsByUserId(userId,3,newsTypeList,pageSize,currentPage);
        }else if("4".equals(appFlag)){
            return objectNewsMapper.selectObjectNewsByUserId(userId,4,newsTypeList,pageSize,currentPage);
        }
        return null;
    }

    public Integer getNoReadNewsCount(String userId) {
        return objectNewsMapper.getNoReadNewsCount(userId);
    }

    public Integer updateAllNewsRead(String userId) {
        return objectNewsMapper.updateAllNewsRead(userId);
    }

    public Integer updateReadById(String newsId) {
        return objectNewsMapper.updateReadById(newsId);
    }


    public  List<Map<String,Object>> selectCustomerPushNews(String userId,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;
        return objectNewsMapper.selectCustomerPushNews(userId,start,pageSize);
    }

    public  Integer selectCustomerPushNewsCount(String userId){
        return objectNewsMapper.selectCustomerPushNewsCount(userId);
    }

    public List<Map<String,Object>> selectTechnicanPushNews(String userId,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;
        return objectNewsMapper.selectTechnicanPushNews(userId,start,pageSize);
    }

    public  Integer selectTechnicanPushNewsCount(String userId){
        return objectNewsMapper.selectTechnicanPushNewsCount(userId);
    }

    public List<Map<String,Object>> selectPushNewsByTaskId(String taskId,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
          start=(currentPage-1)*pageSize;
        return objectNewsMapper.selectPushNewsByTaskId(taskId,start,pageSize);
    }

    public Integer selectPushNewsByTaskIdCount(String taskId){
        return  objectNewsMapper.selectPushNewsByTaskIdCount(taskId);
    }


    public void objectNewsCallBack(String id){
       Map<String,Object> newsMap= objectNewsMapper.selectById(id);
        Integer newsType= (Integer) newsMap.get("newsType");
        if (newsType==0){

        }else if (newsType==1){

        }else if (newsType==2){
                Map<String,Object> allowMap=allowHistoryService.selectById((String) newsMap.get("objectId"));
                Integer flag= (Integer) allowMap.get("flag");
            if (1==flag){//如果是邀请技师
                    Map<String,Object> allowedUserMap=userService.selectUserById((String) allowMap.get("alloweeId"));
                    String oldDepartmentId= (String) allowedUserMap.get("departmentId");
                    if (StringUtils.isNotBlank(oldDepartmentId)){
                        /**
                         * 如果该员工在之前的公司是班组长，则需要将该班组长信息清 掉
                         */
                        Map<String,Object>  oldDepartment=departmentService.selectDepartmentById(oldDepartmentId);
                        if (allowMap.get("alloweeId").equals(oldDepartment.get("principalId"))) {
                            Map<String, Object> departmentMap = new HashMap<>();
                            departmentMap.put("id", oldDepartmentId);
                            departmentMap.put("principalId", "");
                            departmentService.update(departmentMap);
                        }
                     }
                          Map<String,Object> map=new HashMap<>();
                        map.put("id",allowMap.get("alloweeId"));
                        map.put("pcCompanyId",allowMap.get("pcCompanyId"));
                        map.put("departmentId","");
                        userService.updateDcUser(map);
                        objectNewsMapper.deleteById(id);

                //将该员工为班组长的班组的负责人Id清理掉




             }else {//如果是邀请联系人
                List<Map<String, Object>> list = companyContactorService.selectCompanyContactorByMutileCondition((String) allowMap.get("pcCompanyId"), null, (String) allowMap.get("alloweeId"));
                    if (list.size()==0){
                        Map<String,Object>  map=new HashMap<>();
                        map.put("id", UUID.randomUUID().toString());
                        map.put("pcCompanyId", allowMap.get("pcCompanyId"));
                        map.put("userId",allowMap.get("alloweeId"));
                        map.put("createdBy",allowMap.get("allowerId"));
                        map.put("createDate",new Date());
                        List<Map<String,Object>>  mapList=new ArrayList<>();
                        mapList.add(map);
                        companyContactorService.insertCompanyContactorList(mapList);
                     }
                objectNewsMapper.deleteById(id);
            }
        }
    }

    public Integer selectTotalObjectNewsByUserId(String loginUserId, String appFlag, List<Integer> newsTypeList) {
        /*
        0：服务端，1：技师端，2：客户端
         */
        if("2".equals(appFlag)){
            return objectNewsMapper.selectTotalObjectNewsByUserId(loginUserId,2, newsTypeList);
        }else if("3".equals(appFlag)){
            return objectNewsMapper.selectTotalObjectNewsByUserId(loginUserId,3,newsTypeList);
        }else if("4".equals(appFlag)){
            return objectNewsMapper.selectTotalObjectNewsByUserId(loginUserId,4,newsTypeList);
        }
        return 0;
    }
}
