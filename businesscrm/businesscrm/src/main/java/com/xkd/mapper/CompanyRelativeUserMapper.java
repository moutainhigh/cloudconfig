package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/7.
 */
public interface CompanyRelativeUserMapper {
    public int insertList(@Param("list")List<Map<String,Object>> list);

    public int deleteByCompanyId(@Param("companyId") String companyId);

    public int deleteByCompanyIdsString(@Param("companyIds") String companyIds);
    public List<Map<String,Object>>  selectRelativeUserListByCompanyIds(@Param("companyIdList")List<String> companyIdList);

    public int deleteRelativeUserById(@Param("id")String id);

}
