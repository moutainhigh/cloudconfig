package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.BoxIdInvalidException;
import com.xkd.utils.HttpRequestUtil;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对外API调用门面类，所有对外调用均放在此类中
 */
@Service
public class ApiCallFacadeService {

    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceShareService deviceShareService;

    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    CustomerService customerService;

    public static List<String> successCodeList = new ArrayList<>();

    static {
        successCodeList.add("0");
        successCodeList.add("201");
        successCodeList.add("200");
    }


    public void sendAddDeviceWarning(List<String> boxIdList, List<String> userIdList, Integer control, Integer warningLevel)  {
        if (boxIdList.size() == 0) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("box_id_list", boxIdList);
        map.put("user_id_list", userIdList);
        map.put("is_ctrl", control + "");
        map.put("receive_level", warningLevel + "");

        String result = HttpRequestUtil.sendPostRequestBody(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/BatchShareDevice/", new HashMap<>(), JSON.toJSONString(map));
        System.out.println(result);

        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("服务端调用发送告警名单失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");

            if (!successCodeList.contains(code + "")) {
                if ("400".equals(code+"")){
                    throw new BoxIdInvalidException();
                }
                throw new RuntimeException("服务端调用发送告警名单失败");
            }

            ;

        }
    }


    public void sendDeleteDeviceWarning(List<String> boxIdList, String userId)  {
        if (boxIdList.size() == 0) {
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("box_id_list", boxIdList);
        map.put("user_id", userId);

        String result = HttpRequestUtil.sendPutRequestBody(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/ShareDevice/", new HashMap<>(), JSON.toJSONString(map));
        System.out.println(result);

        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("服务端调用发送告警名单失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code + "")) {
                if ("400".equals(code+"")){
                    throw new BoxIdInvalidException();
                }
                throw new RuntimeException("服务端调用发送告警名单失败");
            }
            ;

        }

    }


    /**
     * 绑定客户联系人时，分发设备告警名单
     *
     * @param companyId
     * @param userId
     */
    public void addDeviceWarningWhenBindingCustomer(String companyId, String pcCompanyId, String userId)  {
        List<String> companyIdList = new ArrayList<>();
        companyIdList.add(companyId);
        if (companyIdList.size() == 0) {
            return;
        }
        List<String> pcCompanyIdList = new ArrayList<>();
        pcCompanyIdList.add(pcCompanyId);
        //查询某一服务商下某一客户下的所有设备
        List<Map<String, Object>> list = deviceService.selectDeviceByGroupIds(null, pcCompanyIdList, companyIdList, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> device = list.get(i);
            String boxId=(String) device.get("boxId");
            if (StringUtils.isNotBlank(boxId)) {
                boxIdList.add(boxId);
            }
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        sendAddDeviceWarning(boxIdList, userIdList, 0, 0);
    }

    /**
     * 添加服务商时，分发设备告警名单
     *
     * @param pcCompanyId
     * @param userId
     */
    public void addDeviceWarningWhenBindServerAccount(String pcCompanyId, String userId)   {
        List<String> pcCompanyIdList = new ArrayList<>();
        if (pcCompanyIdList.size() == 0) {
            return;
        }
        pcCompanyIdList.add(pcCompanyId);
        //查找某一服务商 下的所有设备
        List<Map<String, Object>> list = deviceService.selectDeviceByGroupIds(null, pcCompanyIdList, null, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> device = list.get(i);
            String boxId=(String) device.get("boxId");
            if (StringUtils.isNotBlank(boxId)) {
                boxIdList.add((String) device.get("boxId"));
            }
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        sendAddDeviceWarning(boxIdList, userIdList, 0, 0);
    }


    /***
     * 当把某一个技师设置为某一个组的负责人时，分发告警名单
     */
    public void addDeviceWarningWhenSetResponsibleTechnican(String groupId, String pcCompanyId, String userId)  {
        //查找出某一设备组及其下的所有子组ID
        List<String> groupIdList = deviceGroupService.selectChildDeviceGroupIdsByGroupId(groupId);
        if (groupIdList.size() == 0) {
            return;
        }
        List<String> pcCompanyIdList = new ArrayList<>();
        pcCompanyIdList.add(pcCompanyId);
        //查找某一个服务商下某一设备组下的所有设备
        List<Map<String, Object>> list = deviceService.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, null, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> device = list.get(i);
            String boxId=(String) device.get("boxId");
            if (StringUtils.isNotBlank(boxId)) {
                boxIdList.add(boxId);
            }
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        sendAddDeviceWarning(boxIdList, userIdList, 0, 0);
    }


    /***
     * 当把某一技师取消设备组负责人时，删除告警名单
     */
    public void deleteDeviceWarningWhenUnSetResponsibleTechnican(String groupId, String pcCompanyId, String userId)  {
        List<String> groupIdList = deviceGroupService.selectChildDeviceGroupIdsByGroupId(groupId);
        if (groupIdList.size() == 0) {
            return;
        }
        List<String> pcCompanyIdList = new ArrayList<>();
        pcCompanyIdList.add(pcCompanyId);
        //获取某一个服务商某一个组下的所有设备
        List<Map<String, Object>> list = deviceService.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, null, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> device = list.get(i);
            String boxId=(String) device.get("boxId");
            if (StringUtils.isNotBlank(boxId)){
                boxIdList.add(boxId);
            }
        }

        sendDeleteDeviceWarning(boxIdList, userId);
    }


    /**
     * 添加设备时发送，分发告警名单
     *
     * @param groupId
     * @param boxId
     * @param pcCompanyId
     * @param companyId
     */
    public void addDeviceWarningWhenAddDevice(String groupId, String boxId, String pcCompanyId, String companyId)  {


        /**
         * 获取往上查找所有设备组的负责人
         */
        List<String> groupIds = deviceGroupService.selectParentDeviceGroupIdsByGroupId(groupId);
        List<Map<String, Object>> groupList = deviceGroupService.selectDeviceGroupByIds(groupIds);
        List<String> responsibleUserIds = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            responsibleUserIds.add((String) groupList.get(i).get("responsibleUserId"));
        }
        /**
         * 被分享者
         */
        List<String> shareeList = deviceShareService.selectShareeByCompanyIdAndPcCompanyId(pcCompanyId, companyId);


        /**
         * 对应的客户联系人
         */
        List<String> contactorList = companyContactorService.selectAllUserIdByPcCompanyIdAndCompanyId(pcCompanyId, companyId);


        /**
         * 查找所有服务商管理员Id
         */
        List<Map<String, Object>> serverAccountList = customerService.selectPcAdminUserByPcCompanyId(pcCompanyId);
        List<String> serverIdList = new ArrayList<>();
        for (int i = 0; i < serverAccountList.size(); i++) {
            serverIdList.add((String) serverAccountList.get(i).get("id"));
        }

        List<String> allUserList = new ArrayList<>();
        allUserList.addAll(responsibleUserIds);
        allUserList.addAll(shareeList);
        allUserList.addAll(contactorList);
        allUserList.addAll(serverIdList);

        List<String> boxIdList = new ArrayList<>();
        boxIdList.add(boxId);
        sendAddDeviceWarning(boxIdList, allUserList, 0, 0);


    }


    /**
     * 分享时，下发告警名单
     *
     * @param sharer
     * @param sharee
     */
    public void addDeviceWarningWhenShareDevice(String sharer, String sharee, Integer control, Integer warningLevel)  {
        //查找分享者对应的公司列表，但是不包含别人分享给自己的列表
        List<String> sharerCompanyIdList = companyContactorService.selectCompanyIdListByContactor(sharer, 0);
        //查询对应客户下的所有设备
        List<Map<String, Object>> deviceMapList = deviceService.selectDeviceByGroupIds(null, null, sharerCompanyIdList, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < deviceMapList.size(); i++) {
            String boxId=(String) deviceMapList.get(i).get("boxId");
            if (StringUtils.isNotBlank(boxId)) {
                boxIdList.add(boxId);
            }
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.add(sharee);
        sendAddDeviceWarning(boxIdList, userIdList, control, warningLevel);
    }


    /**
     * 解除分享时，删除告警名单
     *
     * @param sharer
     * @param sharee
     */
    public void deleteDeviceWarningWhenUnShareDevice(String sharer, String sharee)   {
        //查找分享者对应的公司列表，但是不包含别人分享给自己的列表
        List<String> sharerCompanyIdList = companyContactorService.selectCompanyIdListByContactor(sharer, 0);


        //获取被分享者对应的公司列表，但是不包含别人分享给它的列表
        List<String> shareeCompanyIdList = companyContactorService.selectCompanyIdListByContactor(sharee, 0);
        //取差集，剔除分享者列表中与被分享者重复的内容
        sharerCompanyIdList.removeAll(shareeCompanyIdList);
        //查询对应客户下的全部设备
        List<Map<String, Object>> deviceMapList = deviceService.selectDeviceByGroupIds(null, null, sharerCompanyIdList, 1, 100000);
        List<String> boxIdList = new ArrayList<>();
        for (int i = 0; i < deviceMapList.size(); i++) {
            String boxId=(String) deviceMapList.get(i).get("boxId");
            if (StringUtils.isNotBlank(boxId)){
                boxIdList.add(boxId);
            }
        }
        //删除分享者之前分享给被分享者的告警配置
        sendDeleteDeviceWarning(boxIdList, sharee);
    }

    public void deleteDevice(String boxId)   {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("box_id", boxId);
        String result = HttpRequestUtil.sendPost(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/DeleteDevice/", new HashMap<>(), paramMap);
        if (StringUtil.isBlank(result)) {
            throw new RuntimeException("服务端调用删除设备接口失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code+"")) {
                if ("400".equals(code+"")){
                    throw new BoxIdInvalidException();
                }
                throw new RuntimeException("服务端调用删除设备接口失败");
            }
        }
    }



    public void deleteDevices(List<String> boxIdList)  {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("box_id_list",boxIdList);
        String result = HttpRequestUtil.sendPutRequestBody(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/DeleteDevice/", new HashMap<>(), JSON.toJSONString(paramMap));
        System.out.println(result);

        if (StringUtil.isBlank(result)) {
            throw new RuntimeException("服务端调用删除设备接口失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code+"")) {
                if ("400".equals(code+"")){
                    throw new BoxIdInvalidException();
                }
                throw new RuntimeException("服务端调用删除设备接口失败");
            }
        }
    }




    public Map<String,Object> addDevice(String boxId, String protocol, String inlines, String outlines, String model)  {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("box_id", boxId);
        paramMap.put("protocol", protocol);
        paramMap.put("inlines", inlines);
        paramMap.put("outlines", outlines);
        paramMap.put("model", model);
        String result = HttpRequestUtil.sendPost(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/AddDevice/", new HashMap<>(), paramMap);
        System.out.println(result);

        if (StringUtil.isBlank(result)) {
            throw new RuntimeException("服务商调用添加设备接口失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code + "")) {
                if ("400".equals(code+"")){
                    throw new BoxIdInvalidException();
                }
                throw new RuntimeException("服务商调用添加设备接口失败");
            }
            return (Map<String, Object>) resultMap.get("data");
        }

    }

    public Map<String, String> getProtocolAndDevTypeByBoxId(String boxId) {
        try {
            String responseByBoxId = HttpRequestUtil.sendGet(PropertiesUtil.YOUDIAN_OLDAPI_URL+"/api/crm/GetBoxProtocolAndDevtype/",
                    new HashMap<>(), new HashMap<String, String>(){{
                        this.put("box_id",boxId);
                    }});
            JSONObject object = JSON.parseObject(responseByBoxId);
            String abc = object.getString("data");
            JSONObject data =  JSON.parseObject(abc);
            String devtype = data.getString("devtype");
            String protocol = data.getString("protocol");
            return new HashMap<String, String>() {{this.put("devtype", devtype); this.put("protocol", protocol);}};
        }catch (Exception e){
            throw new  RuntimeException("设备类型/协议接口调用失败, 使用的boxId为: "+boxId);
        }

    }



    public Map<String,Object> getDeviceIcons(String protocol   ){
        Map<String,String> paramMap=new HashMap<>();
         paramMap.put("protocol",protocol);
       String result= HttpRequestUtil.sendGet(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/GetProtocolPic/", new HashMap<>(), paramMap);

        Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
        });
        Integer code = (Integer) resultMap.get("code");
        if (!successCodeList.contains(code + "")) {
            throw new RuntimeException("服务商调用添加设备接口失败");
        }
        return (Map<String, Object>) resultMap.get("data");
    }

    /**
     *
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
     * @return
     * @throws Exception
     */
    public Map<String, Object> registerUserCallFacede(String account,String pw,String identity_type,String user_id,String mob,String email) throws Exception{
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("pw", pw);
        paramMap.put("identity_type", identity_type);
        paramMap.put("user_id", user_id);
        paramMap.put("mob", mob);
        paramMap.put("email", email);
        String result = HttpRequestUtil.sendPost(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/account/", new HashMap<>(), paramMap);
        System.out.println(result);

        if (StringUtil.isBlank(result)) {
            throw new RuntimeException("服务商调用注册用户接口失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code + "")) {
                throw new RuntimeException("服务商调用注册用户接口失败");
            }
            return (Map<String, Object>) resultMap.get("data");
        }
    }

    /**
     *
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
    public Map<String, Object> updateUserCallFacede (String account,String pw,String identity_type,String user_id,String old_account,String mob,String email,
                                                     String conf_email,String conf_voice,String conf_push,String conf_sms) throws Exception{
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("pw", pw);
        paramMap.put("identity_type", identity_type);
        paramMap.put("user_id", user_id);
        paramMap.put("mob", mob);
        paramMap.put("old_account", old_account);
        paramMap.put("email", email);
        paramMap.put("conf_email", conf_email);
        paramMap.put("conf_voice", conf_voice);
        paramMap.put("conf_push", conf_push);
        paramMap.put("conf_sms", conf_sms);
        String result = HttpRequestUtil.sendPut(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/api/crm/account/", new HashMap<>(), paramMap);
        System.out.println(result);

        if (StringUtil.isBlank(result)) {
            throw new RuntimeException("服务商调用修改用户接口失败");
        } else {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });

            Integer code = (Integer) resultMap.get("code");
            if (!successCodeList.contains(code + "")) {
                throw new RuntimeException("服务商调用修改用户接口失败");
            }
            return (Map<String, Object>) resultMap.get("data");
        }
    }

    public Map<String, Object> getDeviceConfByBoxIdList(List<String> boxIdList)   {
        try {
            Map<String, String> header = new HashMap<String, String>() {{this.put("Content-Type", "application/json");}};
            String boxIdString = JSON.toJSONString(boxIdList);
            Map<String, String> param = new HashMap<String, String>() {{this.put("box_id_list", boxIdString);}};
            String response =  HttpRequestUtil.sendPost(PropertiesUtil.YOUDIAN_OLDAPI_URL+"/api/crm/BatchQueryDevinfo/", header, param);
            if (response.equals("-1")){
                return  new HashMap<>();
            }else {
                Map<String, Object> data = JSON.parseObject(response, new TypeReference<Map<String, Object>>(){{}});
                return (Map<String, Object>) ((Map<String, Object>) data.get("data")).get("data");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  new HashMap<>();
        }
    }


    public void confDevices(String boxId,Integer num,List<String>  idList   ){
        if (StringUtils.isBlank(boxId)){
            return ;
        }
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("box_id",boxId);
        if (num!=null) {
            paramMap.put("num", num + "");
        }
        if (idList.size()>0) {
            paramMap.put("id_list", idList);
        }
        System.out.println(JSON.toJSONString(paramMap));
        String result= HttpRequestUtil.sendPostRequestBody(PropertiesUtil.YOUDIAN_OLDAPI_URL + "/notoken/device/data/config", new HashMap<>(), JSON.toJSONString(paramMap));

        Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
        });
        Integer code = (Integer) resultMap.get("code");

        if (1==code){
            System.out.println("该设备不需要配置");
        }else if (!successCodeList.contains(code + "")) {
            throw new RuntimeException("服务商调用配置设备接口失败");
        }

     }

}
