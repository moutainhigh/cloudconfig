package com.xkd.service;

import com.xkd.mapper.StatisticMapper;
import org.aspectj.weaver.patterns.DeclareParentsMixin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人：巫建辉
 * 创建时间:2017-11-23
 * 功能描述:登录用户首页数据统计
 */
@Service
public class StatisticService {
    @Autowired
    StatisticMapper statisticMapper;
    @Autowired
    SolrService solrService;
    @Autowired
    UserService userService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    UserDataPermissionService userDataPermissionService;

    /**
     * 获取首页数据
     *
     * @param loginUserId 登录人员iD
     * @param token       登录人员token
     * @return
     */
    public Map<String, Object> getHomeData(String loginUserId, String token) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);

        String departmentId=null;
        if ("1".equals(loginUserMap.get("roleId"))){
            departmentId="1";
        }else{
            departmentId=(String)loginUserMap.get("pcCompanyId");
        }

        //获取有权限的部门列表
        //List<Map<String,Object>>  childDepartmentList=userDataPermissionService.selectDepartmentByUserId(loginUserId);
        List<String> departmentIdList=new ArrayList<>();
        if(departmentId.equals("1")){
            departmentIdList = null;
        }else{
            departmentIdList=userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);
            /*for (int i = 0; i <childDepartmentList.size() ; i++) {
                departmentIdList.add((String)childDepartmentList.get(i).get("id"));
            }*/
        }



        Integer customerCount = statisticMapper.selectCustomerCount(loginUserId,  departmentIdList);  //查询我的客户总数据
        Integer thisMonthCustomerCount = statisticMapper.selectThisMonthCustomerCount(loginUserId,departmentIdList);//本月客户数量
        Integer scheduleCount = statisticMapper.selectTotalScheduleTimes(loginUserId,departmentId); //出差总数量
        Integer thisMonthScheduleCount = statisticMapper.selectThisMonthScheduleTimes(loginUserId,departmentId);//本月出差数量
        List<Map<String, Object>> userTypeMapList = statisticMapper.selectUserTypeStatistic(loginUserId,departmentIdList);//客户类型分类统计
        List<Map<String, Object>> userLevelMapList = statisticMapper.selectUserLevelStatistics(loginUserId,departmentIdList);//客户等级分类统计
        resultMap.put("customerCount", customerCount);
        resultMap.put("thisMonthCustomerCount", thisMonthCustomerCount);
        resultMap.put("scheduleCount", scheduleCount);
        resultMap.put("thisMonthScheduleCount", thisMonthScheduleCount);
        resultMap.put("userTypeMapList", userTypeMapList);
        resultMap.put("userLevelMapList", userLevelMapList);

        Map<String, Object> alreadyAttendMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", null, "已参会", null, null, null, loginUserId, loginUserId, token,1,departmentId,2, null,0,null,1,
                10);


        Map<String, Object> unAttendMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", null, "未参会", null, null, null, loginUserId, loginUserId, token,1,departmentId,2,null,0,null, 1,
                10);

        Map<String, Object> attendMap = new HashMap<>();
        resultMap.put("attendInfo", attendMap);
        attendMap.put("attendCount", alreadyAttendMap.get("totalCount")); //已参会人数
        attendMap.put("attendData", alreadyAttendMap.get("data"));//已参会人员
        attendMap.put("unAttendCount", unAttendMap.get("totalCount"));//未参会人数
        attendMap.put("unAttendData", unAttendMap.get("data"));//未参会人员

        List<String> customerDegreeList= new ArrayList<>();
        customerDegreeList.add("A类");
        Map<String, Object> alreadyCommunicatMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(),customerDegreeList, null, null,
                null, null, null, null, null, null, null, null, "1", null, null, "已沟通", null, null, loginUserId, loginUserId, token,1,departmentId,2, null,1,null,1,
                10);

        Map<String, Object> unCommunicatMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), customerDegreeList, null, null,
                null, null, null, null, null, null, null, null, "1", null, null,"未沟通", null, null, loginUserId, loginUserId, token,1,departmentId,2,null,2,null, 1,
                10);
        Map<String, Object> communicatMap = new HashMap<>();
        resultMap.put("communicatInfo", communicatMap);
        communicatMap.put("communicatCount", alreadyCommunicatMap.get("totalCount")); //已沟通人数
        communicatMap.put("communicatData", alreadyCommunicatMap.get("data"));//已沟通人员
        communicatMap.put("unCommunicatCount", unCommunicatMap.get("totalCount"));//未沟通人数
        communicatMap.put("unCommunicatData", unCommunicatMap.get("data"));//未沟通人员


        Map<String, Object> unCompletePayMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", null, null, null, "定金", null, loginUserId, loginUserId, token,1,departmentId,2,null,0,null, 1,
                10);

        Map<String, Object> completePayMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", null, null, null, "全款", null, loginUserId, loginUserId, token,1,departmentId,2,null, 0,null,1,
                10);

        Map<String, Object> payMap = new HashMap<>();
        resultMap.put("payInfo", payMap);
        payMap.put("unCompletePayCount", unCompletePayMap.get("totalCount"));//未完款人数
        payMap.put("unCompletePayData", unCompletePayMap.get("data"));//未完款人员
        payMap.put("completePayCount", completePayMap.get("totalCount"));//已完款人数
        payMap.put("completePayData", completePayMap.get("data"));//已完款人员


        Map<String, Object> unAchieveMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", "1", null, null, null, 79, null, loginUserId, token,1,departmentId,2,null,0,null, 1,
                1);


        Map<String, Object> totalMap = solrService.queryCompany(null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), null, null,
                null, null, null, null, null, null, null, null, "1", "1", null, null, null, null, null, loginUserId, token,1,departmentId,2,null,0,null, 1,
                1);

        Map<String, Object> infoMap = new HashMap<>();
        resultMap.put("infoMap", infoMap);
        infoMap.put("unAchieveCount", unAchieveMap.get("totalCount"));//公司信息完整度未达到80分的公司个数
        infoMap.put("achiveCount", (Integer) totalMap.get("totalCount") - (Integer) unAchieveMap.get("totalCount"));//公司信息完整度达到80分的公司个数


        return resultMap;
    }


}
