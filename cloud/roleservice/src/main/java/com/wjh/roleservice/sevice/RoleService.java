package com.wjh.roleservice.sevice;

import com.wjh.roleservice.mapper.RoleMapper;
import com.wjh.roleservicemodel.model.RolePo;
import com.wjh.roleservicemodel.model.RoleVo;
import org.apache.commons.lang3.event.EventListenerSupport;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleService {


    @Autowired
    IdService idService;

    @Autowired
    RoleMapper roleMapper;

    public RolePo insert(RolePo rolePo, Long loginUserId) {
        Long id = idService.generateId();
        Date date = new Date();
        rolePo.setId(id);
        rolePo.setCreateDate(date);
        rolePo.setUpdateDate(date);
        rolePo.setCreatedBy(loginUserId);
        rolePo.setUpdatedBy(loginUserId);
        roleMapper.insert(rolePo);
        return rolePo;
    }

    public RolePo update(RolePo rolePo, Long loginUserId) {
        Date date = new Date();
        rolePo.setUpdateDate(date);
        rolePo.setUpdatedBy(loginUserId);
        roleMapper.update(rolePo);
        return  rolePo;
    }

    public long delete(Long id, Long loginUserId) {
        roleMapper.delete(id);
        return id;
    }

    public List<RoleVo> search(String roleName,int currentPage,int pageSize){
        if (currentPage<1){
            currentPage=1;
        }
        if (pageSize<1){
            pageSize=10;
        }


        int start=(currentPage-1)*pageSize;
        return roleMapper.search(roleName,start,pageSize);
    }


    public List<RoleVo> selectByIds(List<Long> idList){
        if (idList.size()==0){
            return new ArrayList<RoleVo>(0);
        }
        return roleMapper.selectByIds(idList);
    }


}
