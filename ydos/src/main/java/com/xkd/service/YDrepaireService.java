package com.xkd.service;

import com.xkd.mapper.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class YDrepaireService {


    @Autowired
    private YDrepaireMapper repaireMapper;
    @Autowired
    private YDrepaireApplyMapper repaireApply;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private ApplyDeviceMapper applyDeviceMapper;
    @Autowired
    private RepaireDeviceMapper repaireDeviceMapper;
    @Autowired
    private ObjectNewsMapper objectNewsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ObjectNewsService objectNewsService;

    @Autowired
    private DeviceGroupService deviceGroupService;



    /**
     *
     * @param loginUserId 当前登录用户
     * @param deviceIds 设备ID
     * @param description 设备故障描述
     * @param pictureList 图片URL
     * @throws Exception
     * 创建报修单只向app服务端保存消息
     *
     */
    public void createRepaireApply(String loginUserId, String deviceIds, String description, List<String> pictureList,String createFlag)  throws Exception {


         /*
         * 如果3个设备对应两个服务商（pcCompanyId），也可能对应不同过的客户（companyId），只有供应商和客户相同时才生成一个单
         * 其他的都生成多个单
         */
        String[] ds = deviceIds.split(",");
        List<String> ids = new ArrayList<>();
        for(int i=0;i<ds.length;i++){
            ids.add(ds[i]);
        }

        //Map<String,String> returnList =  deviceService.selectDeviceDistinctPcCompanyIdByIds(ids);
        //查出所有设备信息
        List<Map<String, Object>> returnList = deviceMapper.selectDeviceByIds(ids);
        Set<String> distinctPcCompanyIdList = new HashSet<>();
        for(Map<String, Object> m : returnList){
            String pcCompanyIdd = m.get("pcCompanyId")==null?"":(String)m.get("pcCompanyId");
            String companyId = m.get("companyId")==null?"":(String)m.get("companyId");
            distinctPcCompanyIdList.add(pcCompanyIdd+","+companyId);
        }


        //只有一个供应商，或者没有
        if(distinctPcCompanyIdList.size() < 2){
            String id = UUID.randomUUID().toString();
            String applyNo = DateUtils.getCurrTime("yyyyMMddHHmmss");

            Map<String,Object> paramMap = new HashedMap();
            paramMap.put("id",id);
            paramMap.put("applyNo",applyNo);
            paramMap.put("pcCompanyId",distinctPcCompanyIdList.iterator().next().split(",")[0]);
            paramMap.put("companyId",distinctPcCompanyIdList.iterator().next().split(",")[1]);
            paramMap.put("description",description);
            paramMap.put("statusFlag",0);
            paramMap.put("createDate",new Date());
            paramMap.put("updateDate",new Date());
            paramMap.put("createdBy",loginUserId);
            paramMap.put("updatedBy",loginUserId);
            paramMap.put("createFlag",Integer.parseInt(createFlag));
            paramMap.put("status",0);

            repaireApply.insert(paramMap);

            Map<String, Object> userMap = userMapper.selectUserById(loginUserId);
            List<Map<String,Object>> list = new ArrayList<>();

            if(pictureList != null && pictureList.size() > 0){
                List<Map<String,Object>> attachmentList=new ArrayList<>();
                for (String pictureUrl : pictureList) {
                    Map<String,Object>  m = new HashMap<>();
                    m.put("id",UUID.randomUUID().toString());
                    m.put("objectId",id);
                    m.put("url",pictureUrl);
                    m.put("imgType",1);
                    m.put("createdBy",loginUserId);
                    m.put("createDate",new Date());
                    attachmentList.add(m);
                }
                attachmentMapper.insertAttachmentList(attachmentList);
            }

            List<Map<String,Object>> applyDeviceList = new ArrayList<>();
            if(StringUtils.isNotBlank(deviceIds)){
                String[] dids = deviceIds.split(",");
                for(int i = 0;i<dids.length;i++){
                    Map<String,Object> map = new HashedMap();
                    map.put("id",UUID.randomUUID().toString());
                    map.put("applyId",id);
                    map.put("deviceId",ids.get(i));
                    applyDeviceList.add(map);

                    //获得该设备的pcCompanyId服务商ID,查出用户为管理员角色的用户
                    List<String> uids = deviceMapper.selectRoleUserByDeviceId(dids[i],"2");
                    for(String uid : uids){
                    /*
                    向服务端发送消息
                     */
                        Map<String,Object> m = new HashedMap();
                        m.put("id",UUID.randomUUID().toString());
                        m.put("userId",uid);
                        m.put("objectId",id);
                        m.put("appFlag",2);
                        m.put("title","报修待审核");
                        m.put("content","0".equals(createFlag)?("工程师-巡检"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"提交报修申请，请及时处理"):
                                ("客户-"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"提交报修申请，请及时处理"));
                        m.put("createDate",new Date());
                        m.put("createdBy",loginUserId);
                        m.put("flag",0);
                        m.put("status",0);
                        m.put("newsType",7);

                        //发送推送消息，0需要推送，1不需要推送
                        m.put("pushFlag","0");
                        m.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");
                        list.add(m);
                    }
                }
                applyDeviceMapper.insert(applyDeviceList);
                if(list.size() > 0){
                    objectNewsService.saveObjectNews(list);
                }
            }
        }else{
            for(String distinctPcCompanyId : distinctPcCompanyIdList) {

                List<String> distinctDeviceIds = new ArrayList<>();
                /*
                 * 从服务商pccompanyId中挑出deviceId
                 */
                for (Map<String, Object> map : returnList) {
                    String pcCompanyId1 = map.get("pcCompanyId") == null ? "" : (String) map.get("pcCompanyId");
                    String companyId1 = map.get("companyId")==null?"":(String)map.get("companyId");
                    if (distinctPcCompanyId.equals(pcCompanyId1+","+companyId1)) {
                        String id1 = (String) map.get("id");
                        distinctDeviceIds.add(id1);
                    }
                }

                String id = UUID.randomUUID().toString();
                String applyNo = DateUtils.getCurrTime("yyyyMMddHHmmss");

                Map<String,Object> paramMap = new HashedMap();
                paramMap.put("id",id);
                paramMap.put("applyNo",applyNo);
                paramMap.put("pcCompanyId",distinctPcCompanyId.split(",")[0]);
                paramMap.put("companyId",distinctPcCompanyId.split(",")[1]);
                paramMap.put("description",description);
                paramMap.put("statusFlag",0);
                paramMap.put("createDate",new Date());
                paramMap.put("updateDate",new Date());
                paramMap.put("createdBy",loginUserId);
                paramMap.put("updatedBy",loginUserId);
                paramMap.put("createFlag",Integer.parseInt(createFlag));
                paramMap.put("status",0);

                repaireApply.insert(paramMap);

                Map<String, Object> userMap = userMapper.selectUserById(loginUserId);
                List<Map<String,Object>> list = new ArrayList<>();

                if(pictureList != null && pictureList.size() > 0){
                    List<Map<String,Object>> attachmentList=new ArrayList<>();
                    for (String pictureUrl : pictureList) {
                        Map<String,Object>  m = new HashMap<>();
                        m.put("id",UUID.randomUUID().toString());
                        m.put("objectId",id);
                        m.put("url",pictureUrl);
                        m.put("imgType",1);
                        m.put("createdBy",loginUserId);
                        m.put("createDate",new Date());
                        attachmentList.add(m);
                    }
                    attachmentMapper.insertAttachmentList(attachmentList);
                }

                List<Map<String,Object>> applyDeviceList = new ArrayList<>();

                    for(int i = 0;i<distinctDeviceIds.size();i++){
                        Map<String,Object> map = new HashedMap();
                        map.put("id",UUID.randomUUID().toString());
                        map.put("applyId",id);
                        map.put("deviceId",distinctDeviceIds.get(i));
                        applyDeviceList.add(map);

                        //获得该设备的pcCompanyId服务商ID,查出用户为管理员角色的用户
                        List<String> uids = deviceMapper.selectRoleUserByDeviceId(distinctDeviceIds.get(i),"2");
                        for(String uid : uids){
                    /*
                    向服务端发送消息
                     */
                            Map<String,Object> m = new HashedMap();
                            m.put("id",UUID.randomUUID().toString());
                            m.put("userId",uid);
                            m.put("objectId",id);
                            m.put("appFlag",2);
                            m.put("title","报修待审核");
                            m.put("content","0".equals(createFlag)?("工程师-巡检"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"提交报修申请，请及时处理"):
                                    ("客户-"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"提交报修申请，请及时处理"));
                            m.put("createDate",new Date());
                            m.put("createdBy",loginUserId);
                            m.put("flag",0);
                            m.put("status",0);
                            m.put("newsType",7);

                            //发送推送消息，0需要推送，1不需要推送
                            m.put("pushFlag","0");
                            m.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");
                            list.add(m);
                        }
                    }
                    applyDeviceMapper.insert(applyDeviceList);
                    if(list.size() > 0){
                        objectNewsService.saveObjectNews(list);
                    }
            }
        }
    }

    /**
     * 查询报修列表
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectRepaireApplys(Map<String, Object> paramMap) {
        List<Map<String,Object>> list = repaireMapper.selectRepaireApplys(paramMap);
        List<Map<String,Object>> returnList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(Map<String,Object> map : list){
                String applyId = (String)map.get("id");
                List<Map<String,Object>> applyDevices =  applyDeviceMapper.selectByApplyId(applyId);
                if(applyDevices == null || applyDevices.size()==0){
                    map.put("applyDevices",null);
                }else{
                    map.put("applyDevices",applyDevices);
                }
                returnList.add(map);
            }
        }
        return returnList;
    }

    /**
     * 统计报修列表
     * @param paramMap
     * @return
     */
    public Integer selectTotalRepaireApplys(Map<String, Object> paramMap) {
        return repaireMapper.selectTotalRepaireApplys(paramMap);
    }

    public Integer updateById(Map<String, Object> map) {
        return repaireMapper.updateById(map);
    }

    public Integer updateRepaireApplyById(Map<String, Object> map) {
        return repaireApply.updateById(map);
    }

    /**
     * 获得报修单信息，包含图片信息
     * @param id
     * @return
     */
    public Map<String,Object> selectRepaireApplyById(String id) {
         Map<String,Object> map = repaireMapper.selectRepaireApplyById(id);
         if(map != null && map.size() > 0){
             List<Map<String,Object>> achments = attachmentMapper.selectAttachmentByObjectId(id);
             if(achments == null || achments.size() == 0){
                 map.put("pictures",null);
             }else{
                 map.put("pictures",achments);
             }
             List<Map<String,Object>> maps = applyDeviceMapper.selectByApplyId(id);
             if(maps == null && maps.size() == 0){
                 map.put("applyDevices",null);
             }else{
                 map.put("applyDevices",maps);
             }
         }
        return map;
    }


    /**
     *
     * @param loginUserId
     * @param applyId
     * @param deviceIds
     * @param dueTime
     * @param description
     * @param dealDescription
     * @param priority
     * @param pictureList
     * @param technicianList
     * @throws Exception
     * 创建维修工单需要向相关的技师发送消息
     */
    public void createRepaire(String loginUserId, String applyId, String deviceIds,
                              String dueTime, String description, String dealDescription, String priority,
                                 List<String> pictureList, List<String> technicianList,String solution)  throws Exception{



         /*
         * 如果3个设备对应两个服务商，就要拆分出来(维修单只对应这一个服务商，报修单有多个)
         */
        String[] ds = deviceIds.split(",");
        List<String> ids = new ArrayList<>();
        for(int i=0;i<ds.length;i++){
            ids.add(ds[i]);
        }

        //Map<String,String> returnList =  deviceService.selectDeviceDistinctPcCompanyIdByIds(ids);
        List<Map<String, Object>> returnList = deviceMapper.selectDeviceByIds(ids);
        //通过设备ID查询设备，并且对供应商去重
        Set<String> distinctPcCompanyIdList = new HashSet<>();
        for(Map<String, Object> m : returnList){
            String pcCompanyIdd = m.get("pcCompanyId")==null?"":(String)m.get("pcCompanyId");
            String companyId = m.get("companyId")==null?"":(String)m.get("companyId");
            distinctPcCompanyIdList.add(pcCompanyIdd+","+companyId);
        }


        //只有一个供应商，或者没有
        if(distinctPcCompanyIdList.size() < 2){

            String id = UUID.randomUUID().toString();
            String repaireNo = DateUtils.getCurrTime("yyyyMMddHHmmss");

            Map<String,Object> paramMap = new HashedMap();
            paramMap.put("id",id);
            paramMap.put("applyId",applyId);
            paramMap.put("pcCompanyId",distinctPcCompanyIdList.iterator().next().split(",")[0]);
            paramMap.put("companyId",distinctPcCompanyIdList.iterator().next().split(",")[1]);
            paramMap.put("repaireNo",repaireNo);
            paramMap.put("dueTime",dueTime);
            paramMap.put("description",description);
            paramMap.put("dealDescription",dealDescription);
            paramMap.put("solution",solution);
            paramMap.put("statusFlag",0);
            paramMap.put("priority",priority);
            paramMap.put("dealDescription",dealDescription);
            paramMap.put("createDate",new Date());
            paramMap.put("updateDate",new Date());
            paramMap.put("createdBy",loginUserId);
            paramMap.put("updatedBy",loginUserId);
            paramMap.put("status",0);

            repaireMapper.insert(paramMap);

            List<Map<String,Object>> list = new ArrayList<>();

            List<Map<String,Object>> repaireDeviceList = new ArrayList<>();

            for(int i = 0;i<ids.size();i++){
                Map<String,Object> map = new HashedMap();
                map.put("id",UUID.randomUUID().toString());
                map.put("repaireId",id);
                map.put("deviceId",ids.get(i));
                repaireDeviceList.add(map);
            }
            //插入维修单设备信息
            repaireDeviceMapper.insert(repaireDeviceList);


            if(pictureList != null && pictureList.size() > 0){
                List<Map<String,Object>> attachmentList=new ArrayList<>();
                for (String pictureUrl : pictureList) {
                    Map<String,Object>  m = new HashMap<>();
                    m.put("id",UUID.randomUUID().toString());
                    m.put("objectId",id);
                    m.put("url",pictureUrl);
                    m.put("imgType",1);
                    m.put("createdBy",loginUserId);
                    m.put("createDate",new Date());
                    attachmentList.add(m);
                }
                //图片信息
                attachmentMapper.insertAttachmentList(attachmentList);
            }
            if(technicianList != null && technicianList.size() > 0){
                List<Map<String,Object>> tList=new ArrayList<>();
                for (String technicianId : technicianList) {
                    Map<String,Object>  m = new HashMap<>();
                    m.put("id",UUID.randomUUID().toString());
                    m.put("repaireId",id);
                    m.put("technicianId",technicianId);
                    tList.add(m);

                    Map<String,Object> p = new HashedMap();
                    p.put("id",UUID.randomUUID().toString());
                    p.put("objectId",id);
                    p.put("appFlag",3);
                    p.put("userId",technicianId);
                    p.put("title","工单待领取");
                    p.put("content","接收到新的维修工单任务"+repaireNo+"，请及时领取。");
                    p.put("createDate",new Date());
                    p.put("createdBy",loginUserId);
                    p.put("flag",0);
                    p.put("newsType",0);//维修报修
                    p.put("status",0);

                    p.put("pushFlag","0");
                    list.add(p);

                }
                //插入技师信息
                repaireMapper.insertTechnicianList(tList);
                if(list.size() > 0) {
                    objectNewsService.saveObjectNews(list);
                }
            }


            //如果是从保修单里面创建维修单就有applyId,要更改保修单里面的状态为2
            if(StringUtils.isNotBlank(applyId)){
                Map<String,Object> applyMap = new HashedMap();
                applyMap.put("id",applyId);
                applyMap.put("statusFlag",2);
                repaireApply.updateById(applyMap);
            }


        }else{



            for(String distinctPcCompanyId : distinctPcCompanyIdList){

                List<String> distinctDeviceIds = new ArrayList<>();
                /*
                 * 从服务商pccompanyId中挑出deviceId
                 */
                for(Map<String, Object> map : returnList){
                    String pcCompanyId1 = map.get("pcCompanyId")==null?"":(String)map.get("pcCompanyId");
                    String companyId1 = map.get("companyId")==null?"":(String)map.get("companyId");
                    if(distinctPcCompanyId.equals(pcCompanyId1+","+companyId1)){
                        String id1 = (String)map.get("id");
                        distinctDeviceIds.add(id1);
                    }
                }

                String id = UUID.randomUUID().toString();
                String repaireNo = DateUtils.getCurrTime("yyyyMMddHHmmss");

                Map<String,Object> paramMap = new HashedMap();
                paramMap.put("id",id);
                paramMap.put("applyId",applyId);
                paramMap.put("pcCompanyId",distinctPcCompanyId.split(",")[0]);
                paramMap.put("companyId",distinctPcCompanyId.split(",")[1]);
                paramMap.put("repaireNo",repaireNo);
                paramMap.put("dueTime",dueTime);
                paramMap.put("description",description);
                paramMap.put("dealDescription",dealDescription);
                paramMap.put("solution",solution);
                paramMap.put("statusFlag",0);
                paramMap.put("priority",priority);
                paramMap.put("dealDescription",dealDescription);
                paramMap.put("createDate",new Date());
                paramMap.put("updateDate",new Date());
                paramMap.put("createdBy",loginUserId);
                paramMap.put("updatedBy",loginUserId);
                paramMap.put("status",0);

                repaireMapper.insert(paramMap);

                List<Map<String,Object>> list = new ArrayList<>();

                List<Map<String,Object>> repaireDeviceList = new ArrayList<>();

                    for(int i = 0;i<distinctDeviceIds.size();i++){
                        Map<String,Object> map = new HashedMap();
                        map.put("id",UUID.randomUUID().toString());
                        map.put("repaireId",id);
                        map.put("deviceId",distinctDeviceIds.get(i));
                        repaireDeviceList.add(map);
                    }
                    repaireDeviceMapper.insert(repaireDeviceList);


                if(pictureList != null && pictureList.size() > 0){
                    List<Map<String,Object>> attachmentList=new ArrayList<>();
                    for (String pictureUrl : pictureList) {
                        Map<String,Object>  m = new HashMap<>();
                        m.put("id",UUID.randomUUID().toString());
                        m.put("objectId",id);
                        m.put("url",pictureUrl);
                        m.put("imgType",1);
                        m.put("createdBy",loginUserId);
                        m.put("createDate",new Date());
                        attachmentList.add(m);
                    }
                    attachmentMapper.insertAttachmentList(attachmentList);
                }
                if(technicianList != null && technicianList.size() > 0){
                    List<Map<String,Object>> tList=new ArrayList<>();
                    for (String technicianId : technicianList) {
                        Map<String,Object>  m = new HashMap<>();
                        m.put("id",UUID.randomUUID().toString());
                        m.put("repaireId",id);
                        m.put("technicianId",technicianId);
                        tList.add(m);

                        Map<String,Object> p = new HashedMap();
                        p.put("id",UUID.randomUUID().toString());
                        p.put("objectId",id);
                        p.put("appFlag",3);
                        p.put("userId",technicianId);
                        p.put("title","工单待领取");
                        p.put("content","接收到新的维修工单任务"+repaireNo+"，请及时领取。");
                        p.put("createDate",new Date());
                        p.put("createdBy",loginUserId);
                        p.put("flag",0);
                        p.put("newsType",0);//维修报修
                        p.put("status",0);

                        p.put("pushFlag","0");
                        p.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");
                        list.add(p);
                    }
                    repaireMapper.insertTechnicianList(tList);
                    if(list.size() > 0) {
                        objectNewsService.saveObjectNews(list);
                    }
                }


                //如果是从保修单里面创建维修单就有applyId,要更改保修单里面的状态为2
                if(StringUtils.isNotBlank(applyId)){
                    Map<String,Object> applyMap = new HashedMap();
                    applyMap.put("id",applyId);
                    applyMap.put("statusFlag",2);
                    repaireApply.updateById(applyMap);
                }


            }
        }


    }

    /**
     * 查询维修列表
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectRepaires(Map<String, Object> paramMap) {
        List<Map<String,Object>> list = repaireMapper.selectRepaires(paramMap);
        List<Map<String,Object>> returnList = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(Map<String,Object> map : list){
                String repaireId = (String)map.get("id");
                List<Map<String,Object>> applyDevices =  repaireDeviceMapper.selectByRepaireId(repaireId);
                if(applyDevices == null || applyDevices.size()==0){
                    map.put("repaireDevices",null);
                }else{
                    map.put("repaireDevices",applyDevices);
                }
                List<Map<String,Object>> technicians = repaireMapper.selectTechniciansByRepaireId(repaireId);
                if(applyDevices == null || applyDevices.size()==0){
                    map.put("technicians",null);
                }else{
                    map.put("technicians",technicians);
                }
                returnList.add(map);
            }
        }
        return returnList;
    }

    public Integer selectTotalSelectRepaires(Map<String, Object> paramMap) {
        return repaireMapper.selectTotalSelectRepaires(paramMap);
    }

    /**
     * 获得维修的详情
     * @param id
     * @return
     */
    public Map<String,Object> selectRepaireDetailById(String id) {

        Map<String,Object> map = repaireMapper.selectRepaireDetailById(id);

        if(map != null && map.size() > 0){
            List<Map<String,Object>> achments = attachmentMapper.selectAttachmentByObjectId(id);
            if(achments == null || achments.size() == 0){
                map.put("pictures",null);
            }else{
                map.put("pictures",achments);
            }

            List<Map<String,Object>> maps = repaireDeviceMapper.selectByRepaireId(id);
            if(maps == null || maps.size() == 0){
                map.put("repaireDevices",null);
            }else{
                map.put("repaireDevices",maps);
            }

            List<Map<String,Object>> userContentList = repaireDeviceMapper.selectUserContentByRepaireId(id);
            if(maps == null || maps.size() == 0){
                map.put("repaireUserContent",null);
            }else{
                map.put("repaireUserContent",userContentList);
            }

        }
        return map;
    }


    public  List<Map<String,Object>> selectCurrentRepaireByGroupId( String groupId,String pcCompanyId){
        List<String> groupIdList=new ArrayList<>();
        groupIdList.add(groupId);
         return repaireMapper.selectCurrentRepaireByGroupId(groupIdList, pcCompanyId);
    }



    public Map<String,Object> selectRepaireStatusById(String repaireId) {
        return repaireMapper.selectRepaireStatusById(repaireId);
    }

    /**
     * 更改工单状态，会向服务端、技师端、客户端都发送消息
     * @param repaireId
     * @param statusFlag
     * @param loginUserId
     * @return
     */
    public Integer updateRepaireStatusByRepaireId(String repaireId, String statusFlag,String loginUserId,String dealDescription,String summary)  throws Exception {
        /*
        1 已受理（接单） 2 进行中（出发）   3 进行中（开始） 4 完成 （待评价）   5 完成（结单）
         `updateDate1` datetime DEFAULT NULL COMMENT '接单时间',
      `updateDate2` datetime DEFAULT NULL COMMENT '出发时间',
      `updateDate3` datetime DEFAULT NULL COMMENT '开始时间',
      `updateDate4` datetime DEFAULT NULL COMMENT '完成时间',
      `updatedBy1` char(40) DEFAULT NULL COMMENT '接单人',
      `updatedBy2` char(40) DEFAULT NULL COMMENT '出发的人',
      `updatedBy3` char(40) DEFAULT NULL COMMENT '开始的人',
      `updatedBy4` char(40) DEFAULT NULL COMMENT '完成的人',
         */
        Map<String,Object> paramMap = new HashedMap();
        List<Map<String,Object>> list = new ArrayList<>();
        paramMap.put("id",repaireId);
        paramMap.put("statusFlag",Integer.parseInt(statusFlag));
        if(StringUtils.isNotBlank(dealDescription)){
            paramMap.put("dealDescription",dealDescription);
        }
        if(StringUtils.isNotBlank(summary)){
            paramMap.put("summary",summary);
        }

        Map<String,Object> repaireMap = repaireMapper.selectById(repaireId);
        List<Map<String,Object>> technicians = repaireMapper.selectTechniciansByRepaireId(repaireId);
        List<String> deviceIds = repaireMapper.selectDeviceIdListByRepaireId(repaireId);
        Map<String, Object> userMap = userMapper.selectUserById(loginUserId);

        /*
            维护状态并且保存消息
         */
        if("1".equals(statusFlag)){
            paramMap.put("updatedBy1",loginUserId);
            paramMap.put("updateDate1",new Date());

            if(repaireMap != null && repaireMap.size() > 0 && technicians != null && technicians.size() > 0){

                setAllAppNewsList(loginUserId,repaireMap,deviceIds,userMap,list,technicians,"工单已接单","维修工单"+repaireMap.get("repaireNo")==null?"":
                        (String)repaireMap.get("repaireNo")+"由工程师"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"设置为接单。点击查看详情","012");
            }
        }else if("2".equals(statusFlag)){
            paramMap.put("updatedBy2",loginUserId);
            paramMap.put("updateDate2",new Date());

            if(repaireMap != null && repaireMap.size() > 0 && technicians != null && technicians.size() > 0){

                setAllAppNewsList(loginUserId,repaireMap,deviceIds,userMap,list,technicians,"工单已出发","维修工单"+repaireMap.get("repaireNo")==null?"":
                        (String)repaireMap.get("repaireNo")+"由工程师"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"设置为出发。点击查看详情","2");
            }
        }else if("3".equals(statusFlag)){
            paramMap.put("updatedBy3",loginUserId);
            paramMap.put("updateDate3",new Date());

            if(repaireMap != null && repaireMap.size() > 0 && technicians != null && technicians.size() > 0){
                setAllAppNewsList(loginUserId,repaireMap,deviceIds,userMap,list,technicians,"工单已开始","维修工单"+repaireMap.get("repaireNo")==null?"":
                        (String)repaireMap.get("repaireNo")+"由工程师"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"设置为开始。点击查看详情","2");
            }
        }else if("4".equals(statusFlag)){
            paramMap.put("updatedBy4",loginUserId);
            paramMap.put("updateDate4",new Date());

            if(repaireMap != null && repaireMap.size() > 0 && technicians != null && technicians.size() > 0){
                setAllAppNewsList(loginUserId,repaireMap,deviceIds,userMap,list,technicians,"工单已完成","维修工单"+repaireMap.get("repaireNo")==null?"":
                        (String)repaireMap.get("repaireNo")+"由工程师"+userMap.get("uname")==null?"":(String)userMap.get("uname")+"完成。点击查看详情","02");
            }
        }

        if(list.size() > 0){
            objectNewsService.saveObjectNews(list);
        }

        return repaireMapper.updateById(paramMap);
    }

    /**
     *
     * @param loginUserId
     * @param repaireMap
     * @param deviceIds
     * @param userMap
     * @param returnList
     * @param technicians
     * @param title
     * @param content
     * @param appFlag 2:服务端，3：技师端，4：客户端，如："234"表示服务端、技师端、客户端都要推送消息
     */
    private void setAllAppNewsList(String loginUserId,Map<String,Object> repaireMap,List<String> deviceIds,Map<String,Object> userMap,
                                   List<Map<String,Object>> returnList,List<Map<String,Object>> technicians,String title,String content,
                                   String appFlag) {

        String repaireId = (String) repaireMap.get("id");

        //将消息保存给该维修单的所有技师
        for (Map<String, Object> technician : technicians) {
            Map<String, Object> map = new HashedMap();
            map.put("id", UUID.randomUUID().toString());
            map.put("appFlag", 3);
            map.put("objectId", repaireId);
            map.put("userId", (String) technician.get("id"));
            map.put("title", title);
            map.put("content", content);
            map.put("createDate", new Date());
            map.put("createdBy", loginUserId);
            map.put("flag", 0);
            map.put("newsType",0);//维修报修
            map.put("status", 0);

            if(appFlag.indexOf("3") > 0){
                map.put("pushFlag", "0");
            }
            map.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");

            returnList.add(map);
        }

        //将信息保存给客户
        List<String> companyUserIds = repaireMapper.selectCompanyUserByRepaireId(repaireId);
        for (String userId : companyUserIds) {
            Map<String, Object> map = new HashedMap();
            map.put("id", UUID.randomUUID().toString());
            map.put("appFlag", 4);
            map.put("objectId", repaireId);
            map.put("userId", userId);
            map.put("title", title);
            map.put("content", content);
            map.put("createDate", new Date());
            map.put("createdBy", loginUserId);
            map.put("flag", 0);
            map.put("status", 0);
            map.put("newsType",0);//维修报修

            if(appFlag.indexOf("4") > 0){
                map.put("pushFlag", "0");
            }
            returnList.add(map);
        }


        for (String deviceId : deviceIds) {
            //获得该设备的pcCompanyId服务商ID,查出用户为管理员角色的用户
            List<String> uids = deviceMapper.selectRoleUserByDeviceId(deviceId, "2");
            for (String uid : uids) {
                /*
                向服务端发送消息
                 */
                Map<String, Object> m = new HashedMap();
                m.put("id", UUID.randomUUID().toString());
                m.put("userId", uid);
                m.put("objectId", repaireId);
                m.put("appFlag", 2);
                m.put("title", title);
                m.put("content", content);
                m.put("createDate", new Date());
                m.put("createdBy", loginUserId);
                m.put("flag", 0);
                m.put("status", 0);
                m.put("newsType",0);//维修

                if(appFlag.indexOf("2") > 0){
                    m.put("pushFlag", "0");
                }
                returnList.add(m);
            }

        }
    }

    public List<String> selectDeviceIdListByRepaireId(String repaireId){
        return repaireMapper.selectDeviceIdListByRepaireId(repaireId);
    }

   public List<String> selectDeviceIdByApplyId(String applyId){
        return repaireApply.selectDeviceIdByApplyId(applyId);
    }

    public List<Map<String,Object>> selectOutOfDateRepaire(){
        return repaireMapper.selectOutOfDateRepaire();
    }

    public Integer deleteRepaireByIds(List<String> repaireIds) {
        return repaireMapper.deleteRepaireByIds(repaireIds);
    }

    private List<String> getUserIdsHasOutOfDateNews(String repaireId){
        return repaireMapper.getUserIdsHasOutOfDateNews(repaireId);
    }


    public List<Map<String,Object>> selectTechniciansByRepaireId(String repaireId) {
        return repaireMapper.selectTechniciansByRepaireId(repaireId);
    }

    public Integer countRepairApplyByStatusAndPcCompanyId(String pcCompanyId){
        return repaireMapper.countRepairApplyByStatusAndPcCompanyId(pcCompanyId);
    }

    public Integer countRepairByStatusAndPcCompanyId(String pcCompanyId){
        return repaireMapper.countRepairByStatusAndPcCompanyId(pcCompanyId);
    }

    public Integer countRepairByStatusAndPcCompanyIdAndDate(String pcCompanyId){
        return repaireMapper.countRepairByStatusAndPcCompanyIdAndDate(pcCompanyId);
    }

    public List<Map<String, Object>> selectRepairByUserAndDate(String userId, Integer type, String date){
        return repaireMapper.selectRepairByUserAndDate(userId, type, date);
    }

    public List<Map<String,Object>> selectHistoryRepaireByGroupId(String groupId,String startDate,String toDate,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;


        return repaireMapper.selectHistoryRepaireByGroupId(groupId,startDate,toDate,start,pageSize);
    }


    public Integer selectHistoryRepaireCountByGroupId(String groupId,String startDate,String toDate){
        return repaireMapper.selectHistoryRepaireCountByGroupId(groupId,startDate,toDate);
    }


    public Integer saveRepaireUserContent(Map<String, Object> paramMap) {
        return repaireMapper.saveRepaireUserContent(paramMap);
    }

    public List<Map<String, Object>> selectCurrentOrder(String deviceId){
        return repaireMapper.selectCurrentRepairOrder(deviceId);
    }

    public Integer countUserRepair(String pcCompanyId, String userId){
        return repaireMapper.countUserRepair(pcCompanyId, userId);
    }

    public Integer countCompanyRepair(String userId){
        return repaireMapper.countCompanyRepair(userId);
    }

}
