package com.xkd.service;

import com.xkd.mapper.BusinessOpportunityMapper;
import com.xkd.mapper.CompanyMapper;
import com.xkd.mapper.PagerFileMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
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
        return businessOpportunityMapper.selectBusinessOpportunityById(id);
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
            String businessTypeId,
            String businessSourceId,
            Integer status,
            String loginUserId) {

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
        map.put("businessTypeId", businessTypeId);
        map.put("businessSourceId", businessSourceId);
        map.put("updatedBy", loginUserId);
        map.put("updateDate", new Date());
        map.put("status", status);
        userDynamicService.addUserDynamic(loginUserId, (String) opportunityMap.get("companyId"), null, "更新", "更新了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        userDynamicService.addUserDynamic(loginUserId, id, null, "更新", "更新了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        if (StringUtils.isNotBlank(opportunityName)) {
            map.put("companyName", opportunityName);
            companyMapper.updatePagerFileName(map);
        }
        return businessOpportunityMapper.updateBusinessOppotunity(map);
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
            String businessTypeId,
            String businessSourceId,
            String loginUserId) {
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
        map.put("businessTypeId", businessTypeId);
        map.put("businessSourceId", businessSourceId);
        map.put("createdBy", loginUserId);
        map.put("createDate", new Date());
        map.put("status", 0);
        userDynamicService.addUserDynamic(loginUserId, companyId, null, "添加", "添加了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        userDynamicService.addUserDynamic(loginUserId, id, null, "添加", "添加了企业商机\"" + opportunityName + "\"", 0, null, null, null);
        return businessOpportunityMapper.insertBusinessOppotunity(map);

    }


    public Map<String, Object> selectBusinessOpportunity(int withDataPermission,String searchValue, String  businessTypeId,String businessSourceId, String companyId, String phaseId, String opportunityName, String departmentId, String loginUserId, Integer publicFlag,String startDate,String endDate, Integer currentPage, Integer pageSize) {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Integer start = (currentPage - 1) * pageSize;

        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String, Object>> list = null;
        List<String> departmentIdList = new ArrayList<>();
        if (withDataPermission == 1) { //如果包含数据视图权限
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
        }
        list = businessOpportunityMapper.selectBusinessOpportunity(searchValue,businessTypeId,businessSourceId,companyId, phaseId, opportunityName, departmentIdList, loginUserId, publicFlag,  startDate,  endDate, start, pageSize);
        //查询相关人员
        List<String> companyIds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            String comId = (String) data.get("companyId");
            if (StringUtils.isNotBlank(comId)) {
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

        Integer count = businessOpportunityMapper.selectBusinessOpportunityCount( searchValue,  businessTypeId, businessSourceId,companyId, phaseId, opportunityName, departmentIdList, loginUserId, publicFlag,startDate,endDate);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", count);
        resultMap.put("list", list);
        return resultMap;


    }


    public Integer deleteBusinessOpportunities(List<String> idList) {
        if (idList.size() > 0) {
            pagerFileMapper.editFolder(idList);
            return businessOpportunityMapper.deleteBusinessOpportunities(idList);
        }
        return 0;
    }


    public List<Map<String, Object>> selectBusinessOpportunityByIds(List<String> idList) {
        return businessOpportunityMapper.selectBusinessOpportunityByIds(idList);
    }
}
