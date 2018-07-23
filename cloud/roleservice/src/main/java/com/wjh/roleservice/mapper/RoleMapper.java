package com.wjh.roleservice.mapper;

import com.wjh.roleservicemodel.model.RolePo;
import com.wjh.roleservicemodel.model.RoleVo;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    public int insert(RolePo rolePo);

    public int update(RolePo rolePo);

    public int delete(@Param("id") Long id);

    public List<RoleVo> search(@Param("roleName") String roleName,@Param("start") Integer start,@Param("pageSize") Integer pageSize);
}
