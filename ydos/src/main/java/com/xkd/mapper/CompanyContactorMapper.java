package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/11.
 */
public interface CompanyContactorMapper {
    List<Map<String, Object>> selectCompanyContactorByCompanyId(@Param("companyId") String companyId, @Param("pcCompanyId") String pcCompanyId, @Param("uname") String uname, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    int selectCompanyContactorCountByCompanyId(@Param("companyId") String companyId, @Param("pcCompanyId") String pcCompanyId, @Param("uname") String uname);

    int deleteById(@Param("id") String id);

    int deleteByIds(@Param("idList") List<String> idList);

    int deleteByCompanyId(@Param("companyId") String companyId);

    int insertCompanyContactorList(@Param("list") List<Map<String, Object>> list);

    Map<String, Object> selectCompanyContactorById(@Param("id") String id);


    List<Map<String,Object>>  selectCompanyContactorByMutileCondition(@Param("pcCompanyId") String pcCompanyId,@Param("companyId")String companyId,@Param("userId")String userId);


    List<String> selectCompanyIdListByContactor(@Param("userId") String userId,@Param("includingSharer")Integer includingSharer);

    List<String> selectPcCompanyIdListByContactor(@Param("userId")String userId);

    Map<String, Object> selectCompanyContactorByCompanyIdAndUserIdAndPcCompanyId(@Param("companyId") String companyId, @Param("userId") String userId, @Param("pcCompanyId") String pcCompanyId);

    List<String> getCompanyIdByUserId (@Param("userId") String userId);

    int update(Map<String,Object> map);

    int deleteContactor(@Param("id")String id);

    List<String> selectAllUserIdByPcCompanyIdAndCompanyId(@Param("pcCompanyId")String pcCompanyId,@Param("companyId")String companyId);
}
