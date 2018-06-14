package com.xkd.service;

import com.xkd.mapper.BusinessOpportunityMapper;
import com.xkd.mapper.CompanyMapper;
import com.xkd.mapper.PagerFileMapper;
import com.xkd.mapper.UserDataPermissionMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2017/12/22.
 */
@Service
public class BusinessOpportunityService {

    @Autowired
    BusinessOpportunityMapper businessOpportunityMapper;
    @Autowired
    UserDynamicService userDynamicService;
    @Autowired
    CompanyRelativeUserService companyRelativeUserService;
    @Autowired
    UserService userService;
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PagerFileMapper pagerFileMapper;

    @Autowired
    UserDataPermissionService userDataPermissionService;

    public Map<String, Object> getBusinessOpportunityById(String id) {
        return businessOpportunityMapper.selectBusinessOppotunityById(id);
    }


    public int updateBusinessOppotunity(Map<String, Object> map) {
        return businessOpportunityMapper.updateBusinessOppotunity(map);
    }


    /**
     * 更新企业商机
     *
     * @param id                主键
     * @param opportunityName   商机名称
     * @param responsibleUserId 负责人
     * @param contact           联系人名称
     * @param contactMobile     联系电话
     * @param salesMoney        销售金额
     * @param phaseId           阶段Id
     * @param implementDate     实施日期
     * @param remark            备注
     * @param loginUserId       登录人员Id
     * @return
     */
    public int updateBusinessOppotunity(
            String id,
            String companyId,
            String opportunityName,
            String responsibleUserId,
            String contact,
            String contactMobile,
            String salesMoney,
            String phaseId,
            String implementDate,
            String remark,
            Integer status,
            String loginUserId,
            String departmentId) {

        Map<String, Object> opportunityMap = getBusinessOpportunityById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("companyId", companyId);
        map.put("opportunityName", opportunityName);
        map.put("responsibleUserId", responsibleUserId);
        map.put("contact", contact);
        map.put("contactMobile", contactMobile);
        map.put("salesMoney", salesMoney);
        map.put("phaseId", phaseId);
        map.put("implementDate", implementDate);
        map.put("remark", remark);
        map.put("updatedBy",loginUserId);
        map.put("updateDate",new Date());
        map.put("status",status);
        map.put("departmentId",departmentId);
        userDynamicService.addUserDynamic(loginUserId, (String) opportunityMap.get("companyId"), null, "更新", "更新了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        userDynamicService.addUserDynamic(loginUserId, id, null, "更新", "更新了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        if(StringUtils.isNotBlank(opportunityName)){
            map.put("companyName",opportunityName);
            companyMapper.updatePagerFileName(map);
        }
        return updateBusinessOppotunity(map);
    }

    public int insertBusinessOppotunity(Map<String, Object> map) {
        return businessOpportunityMapper.insertBusinessOppotunity(map);
    }


    public int insertBusinessOppotunity(
            String id,
            String companyId,
            String opportunityName,
            String responsibleUserId,
            String contact,
            String contactMobile,
            String salesMoney,
            String phaseId,
            String implementDate,
            String remark,
            String loginUserId,
            String otherContact,
            String otherContactMobile,
            String otherCompanyName,
            String departmentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("companyId", companyId);
        map.put("opportunityName", opportunityName);
        map.put("responsibleUserId", responsibleUserId);
        map.put("contact", contact);
        map.put("contactMobile", contactMobile);
        map.put("salesMoney", salesMoney);
        map.put("phaseId", phaseId);
        map.put("implementDate", implementDate);
        map.put("otherContact", otherContact);
        map.put("otherContactMobile", otherContactMobile);
        map.put("otherCompanyName", otherCompanyName);
        map.put("remark", remark);
        map.put("createdBy",loginUserId);
        map.put("createDate",new Date());
        map.put("status",0);
        Map<String,Object> loginUserMap = userService.selectUserById(loginUserId);
        map.put("pcCompanyId",loginUserMap.get("pcCompanyId").toString());
        map.put("departmentId",departmentId);

        userDynamicService.addUserDynamic(loginUserId, companyId, null, "添加", "添加了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        userDynamicService.addUserDynamic(loginUserId, id, null, "添加", "添加了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        return insertBusinessOppotunity(map);

    }





    public Map<String,Object> selectBusinessOpportunity(String companyId,String phaseId, String opportunityName, String departmentId,
                                                        String userId,Integer flag, Integer currentPage, Integer pageSize,String loginUserId,String beginDate,
                                                        String endDate,String phaseTypeId,String orderFlag) {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Integer start = (currentPage - 1) * pageSize;

        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
        List<Map<String,Object>>  list=null;
        List<String> a = null;
        if ("1".equals(loginUserMap.get("roleId"))){  //如果是超级管理员则查看所有商机
            list= businessOpportunityMapper.selectBusinessOpportunity(companyId, phaseId, opportunityName, departmentId, userId,flag, start, pageSize, null,beginDate,endDate,phaseTypeId,null,orderFlag);
        }else{
            a = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);
            list= businessOpportunityMapper.selectBusinessOpportunity(companyId, phaseId, opportunityName, departmentId, userId,flag, start, pageSize, (String)loginUserMap.get("pcCompanyId"),beginDate,endDate,phaseTypeId,userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId),orderFlag);
        }

        Integer count=0;
        if ("1".equals(loginUserMap.get("roleId"))) {  //如果是超级管理员则查看所有商机
            count =businessOpportunityMapper.selectBusinessOpportunityCount(companyId, phaseId, opportunityName, departmentId, userId,flag,null,beginDate,endDate,phaseTypeId,null);
        }else {
            count =businessOpportunityMapper.selectBusinessOpportunityCount(companyId, phaseId, opportunityName, departmentId, userId,flag,(String)loginUserMap.get("pcCompanyId"),beginDate,endDate,phaseTypeId,a);
        }
        //查询相关人员
        List<String> companyIds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            String comId=(String) data.get("companyId");
            if (StringUtils.isNotBlank(comId)){
                if (!companyIds.contains(comId)) {
                    companyIds.add(comId);
                }
            }
        }

        List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);
        //整理数据，将相关人员整合到相应的记录中去
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            List<Map<String, Object>> relUserList = new ArrayList<>();
            data.put("relativeUserList", relUserList);
            for (int j = 0; j < relativeList.size(); j++) {
                if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
                    relUserList.add(relativeList.get(j));
                }
            }
        }

        Map<String,Object>  resultMap=new HashMap<>();
        resultMap.put("count",count);
        resultMap.put("list",list);
        return resultMap;
    }




    public Integer deleteBusinessOpportunities(List<String> idList){
        if (idList.size()>0) {
            pagerFileMapper.editFolder(idList);
            return businessOpportunityMapper.deleteBusinessOpportunities(idList);
        }
        return 0;
    }


    public List<Map<String,Object>> selectBusinessOpportunityByIds(List<String> idList){
        return businessOpportunityMapper.selectBusinessOpportunityByIds(idList);
    }

    public List<Map<String,String>> getMyTableShow(String loginUserId, String tabType) {
        List<Map<String,String>> list = businessOpportunityMapper.getZDYTableShow(loginUserId,tabType);
        if(null != list && list.size() >0){
            return list;
        }else{
            return businessOpportunityMapper.getMyTableShow(tabType,"1");
        }


    }

    public List<Map<String,Object>> getBusinessOpportunityName(String pcCompanyId, List<String> depList,String opportunityName) {
        return businessOpportunityMapper.getBusinessOpportunityName(pcCompanyId,depList,opportunityName);
    }

    public  List<Map<String,String>> getAllTabColumn(String loginUserId, String tabType) {
        Map<String,List<Map<String,String>>> map = new HashMap<>();
        List<Map<String,String>> my = businessOpportunityMapper.getZDYTableShow(loginUserId,tabType);
        if(null != my && my.size() >0 ){
            List<Map<String,String>> other = businessOpportunityMapper.getOtherTableShow(my);
            my.addAll(other);
            return my;
        }else{
            return businessOpportunityMapper.getMyTableShow(tabType,null);

        }
    }

    public void saveTabColumn(String loginUserId, List<String> tableColumn, String tabType) {
        businessOpportunityMapper.deleteMyTabColumn(loginUserId,tabType);
        businessOpportunityMapper.saveTabColumn(loginUserId,tableColumn,tabType);
    }
}
