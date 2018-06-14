package com.xkd.mapper;

import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ContactsUserMapper {
    List<Map<String,String>> getContactsUserList(@Param("queryType")String queryType,@Param("queryName")String queryName,@Param("userId") String userId,@Param("showMobile") Boolean showMobile,@Param("depList") List<String> depList,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize);

    List<Map<String,String>> getContactsUserByCompanyIdAndUserId(@Param("companyId") String companyId,@Param("mobile")  String mobile);

    int getContactsUserListTotal(@Param("queryType")String queryType,@Param("queryName")String queryName,@Param("userId") String userId,@Param("showMobile") Boolean showMobile,@Param("depList") List<String> depList);

    List<Map<String,String>> getContactsStatistics(@Param("userId")String userId,@Param("startDate") String startDate,@Param("endDate")  String endDate,@Param("depList") List<String> depList);
}
