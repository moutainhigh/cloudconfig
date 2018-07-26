package com.wjh.menuoperateservice.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.RedisKeyConstant;
import com.wjh.menuoperateservice.mapper.OperateMapper;
import com.wjh.menuoperateservicemodel.model.OperatePo;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.utils.redis.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OperateService {


    @Autowired
    IdService idService;

    @Autowired
    OperateMapper operateMapper;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    public OperatePo insert(OperatePo operatePo, Long loginUserId) {
        Long id = idService.generateId();
        Date date = new Date();
        operatePo.setId(id);
        operatePo.setCreatedBy(loginUserId);
        operatePo.setUpdatedBy(loginUserId);
        operatePo.setCreateDate(date);
        operatePo.setUpdateDate(date);
        operateMapper.insert(operatePo);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return operatePo;
    }

    public OperatePo update(OperatePo operatePo, Long loginUserId) {
        operatePo.setUpdatedBy(loginUserId);
        operateMapper.update(operatePo);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return operatePo;
    }

    public List<OperateVo> search(String operateName, int currentPage, int pageSize) {
        if (currentPage < 1) {
            currentPage = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int start = (currentPage - 1) * pageSize;
        return operateMapper.search(operateName, start, pageSize);
    }


    public Long delete(Long id) {
        operateMapper.delete(id);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return id;
    }


    public List<OperateVo> selectByIds(List<Long> idList) {
        if (idList.size() == 0) {
            return new ArrayList<OperateVo>(0);
        }
        return operateMapper.selectByIds(idList);
    }

}
