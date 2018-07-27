package com.wjh.roleservice.sevice;

import com.wjh.common.model.RedisKeyConstant;
import com.wjh.idconfiguration.model.IdGenerator;
import com.wjh.roleservice.mapper.RoleMapper;
import com.wjh.roleservicemodel.model.RolePo;
import com.wjh.roleservicemodel.model.RoleVo;
import com.wjh.utils.redis.RedisCacheUtil;
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
    IdGenerator idGenerator;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    public RolePo insert(RolePo rolePo, Long loginUserId) {
        Long id = idGenerator.generateId();
        Date date = new Date();
        rolePo.setId(id);
        rolePo.setCreateDate(date);
        rolePo.setUpdateDate(date);
        rolePo.setCreatedBy(loginUserId);
        rolePo.setUpdatedBy(loginUserId);
        roleMapper.insert(rolePo);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return rolePo;
    }

    public RolePo update(RolePo rolePo, Long loginUserId) {
        Date date = new Date();
        rolePo.setUpdateDate(date);
        rolePo.setUpdatedBy(loginUserId);
        roleMapper.update(rolePo);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return  rolePo;
    }

    public long delete(Long id, Long loginUserId) {
        roleMapper.delete(id);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
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
