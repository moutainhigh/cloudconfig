package com.xkd.mapper;

import com.xkd.utils.MailUtils;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/25.
 */


public interface BusinessOpportunityMapper {

public  int insertBusinessOppotunity(Map<String,Object> map);

public  int updateBusinessOppotunity(Map<String,Object> map);

public Map<String,Object> selectBusinessOppotunityById(@Param("id")String  id);



public List<Map<String,Object>> selectBusinessOpportunity(@Param("companyId")String companyId,@Param("phaseId")String phaseId,@Param("opportunityName")String opportunityName,
                                                          @Param("departmentId") String departmentId,@Param("userId") String userId,@Param("flag") Integer flag,@Param("start")Integer start,
                                                          @Param("pageSize") Integer pageSize,@Param("pcCompanyId")String pcCompanyId,@Param("beginDate")String beginDate,@Param("endDate")String endDate,
                                                          @Param("phaseTypeId")String phaseTypeId,@Param("departmentIds")List<String> departmentIds,@Param("orderFlag")String orderFlag);


 public Integer selectBusinessOpportunityCount(@Param("companyId")String companyId,@Param("phaseId")String phaseId,@Param("opportunityName")String opportunityName,
                                               @Param("departmentId") String departmentId,@Param("userId") String userId,@Param("flag") Integer flag,@Param("pcCompanyId")String pcCompanyId
                                                ,@Param("beginDate")String beginDate,@Param("endDate")String endDate,@Param("phaseTypeId")String phaseTypeId,@Param("departmentIds")List<String> departmentIds);

 public Integer deleteBusinessOpportunities(@Param("idList")List<String> idList);

 public List<Map<String,Object>> selectBusinessOpportunityByIds(@Param("idList") List<String> idList);



    List<Map<String,String>> getZDYTableShow(@Param("userId") String userId, @Param("tabType") String tabType);

    List<Map<String,Object>> getBusinessOpportunityName(@Param("pcCompanyId")String pcCompanyId,@Param("depList") List<String> depList,@Param("opportunityName") String opportunityName);

    List<Map<String,String>> getMyTableShow(@Param("tabType")  String tabType,@Param("status") String status);

    void saveTabColumn(@Param("userId") String userId, @Param("tabColumn") List<String> tabColumn,@Param("tabType") String tabType);

    List<Map<String,String>> getOtherTableShow(@Param("other") List<Map<String, String>> other);

    void deleteMyTabColumn(@Param("userId") String userId, @Param("tabType") String tabType);
}
