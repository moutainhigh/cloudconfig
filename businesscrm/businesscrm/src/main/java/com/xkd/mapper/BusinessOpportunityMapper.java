package com.xkd.mapper;

import com.xkd.utils.MailUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/25.
 */


public interface BusinessOpportunityMapper {

public  int insertBusinessOppotunity(Map<String,Object> map);

public  int updateBusinessOppotunity(Map<String,Object> map);

public Map<String,Object> selectBusinessOpportunityById(@Param("id")String  id);



public List<Map<String,Object>> selectBusinessOpportunity(@Param("searchValue")String searchValue,@Param("businessTypeId")String  businessTypeId,@Param("businessSourceId")String businessSourceId, @Param("companyId")String companyId,@Param("phaseId")String phaseId,@Param("opportunityName")String opportunityName,@Param("departmentIdList") List<String> departmentIdList,@Param("loginUserId") String loginUserId,@Param("publicFlag") Integer publicFlag,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("start")Integer start,@Param("pageSize") Integer pageSize);


 public Integer selectBusinessOpportunityCount(@Param("searchValue")String searchValue,@Param("businessTypeId")String  businessTypeId,@Param("businessSourceId")String businessSourceId,@Param("companyId")String companyId,@Param("phaseId")String phaseId,@Param("opportunityName")String opportunityName,@Param("departmentIdList") List<String> departmentIdList,@Param("loginUserId") String loginUserId,@Param("publicFlag") Integer publicFlag,@Param("startDate") String startDate,@Param("endDate") String endDate);

 public Integer deleteBusinessOpportunities(@Param("idList")List<String> idList);

 public List<Map<String,Object>> selectBusinessOpportunityByIds(@Param("idList") List<String> idList);


}
