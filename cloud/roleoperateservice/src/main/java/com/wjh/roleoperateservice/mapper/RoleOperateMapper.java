package com.wjh.roleoperateservice.mapper;

import com.wjh.roleoperateservicemodel.model.RoleOperatePo;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleOperateMapper {
    public int insertList(@Param("list") List<RoleOperatePo> roleOperatePoList);

    public int deleteByRoleId(@Param("roleId") Long roleId);

    public int deleteByOperateId(@Param("operateId") Long operateId);

    public List<RoleOperateVo> listByRoleId(@Param("roleId")Long roleId);

}
