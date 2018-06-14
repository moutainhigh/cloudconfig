package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Operate;

public interface SysRoleOperateDao {


    int insertList(@Param("list") List<Map<String, Object>> list);

    int deleteByRoleId(@Param("roleId") String roleId);
    
    int deleteByOperateId(@Param("operateId") String operateId);
    
    List<String> selectOpereateIdsByRoleId(@Param("roleId") String roleId);
    
    List<Operate> selectOperateByRoleId(@Param("roleId") String roleId);
    
    
    void copyOperatesToNewRole(@Param("newRoleId") String newRoleId, @Param("fromRoleId") String fromRoleId);
}
