package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-27;
 */

/**
 * 对于日报表进行操作
 * insert by DcYdReport增加记录
 * delete by id 通过id删除记录
 * update by DcYdReport 更新记录
 * select by id 通过id检索记录
 * select by uname 通过用户名检索记录
 * select by uname and type
 * select by uname and date
 */
public interface DcYdReportMapper {

    int insert(Map<String, Object> dcYdReport);

    int deleteById(String id);

    int update(Map<String, Object> dcYdReport);

    List<Map<String, Object>> filterReport(@Param("userIdList") List<String> userIdList,
                                           @Param("status") Integer status,
                                           @Param("type") Integer type,
                                           @Param("startDate") String startDate,
                                           @Param("endDate") String endDate,
                                           @Param("dateFilter") Integer dateFilter,
                                           @Param("pcCompanyId") String pcCompanyId,
                                           @Param("start") Integer start,
                                           @Param("pageSize") Integer pageSize);

    Integer filterReportCount(@Param("userIdList") List<String> userIdList,
                              @Param("status") Integer status,
                              @Param("type") Integer type,
                              @Param("pcCompanyId") String pcCompanyId,
                              @Param("startDate") String startDate,
                              @Param("endDate") String endDate,
                              @Param("dateFilter") Integer dateFilter);

    Map<String, Object> selectById(@Param("id") String id);

    List<Map<String, Object>> selectByUserId(@Param("userId") String userId);

    List<Map<String, Object>> selectByUserIdAndType(@Param("userId") String userId, @Param("type") Integer type);

    List<Map<String, Object>> selectByUserIdAndDate(@Param("userId") String userId, @Param("date") Date date);

    List<String> findUserByRoleAndCompany(@Param("roleId") String roleId, @Param("pcCompanyId") String pcCompanyId);

    Integer countReportByPcCompanyIdAndDate(@Param("pcCompanyId") String pcCompanyId);

    Integer insertForSchedule(@Param("id") String id, @Param("userId") String userId, @Param("pcCompanyId") String pcCompanyId, @Param("type") Integer type);

    List<Map<String, String>> selectAllLastDayReport(@Param("type") Integer type);

    Integer updateReportStatus(@Param("reportIdList") List<String> reportIdList);
}
