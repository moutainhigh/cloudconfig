package com.wjh.useroperateservice.mapper;

import com.wjh.useroperateservicemodel.model.UserOperatePo;
import com.wjh.useroperateservicemodel.model.UserOperateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOperateMapper {
    public  int insertList(@Param("list") List<UserOperatePo> userOperatePoList);

    public int deleteByUserId(@Param("userId")Long userId);

    public int deleteByOperateId(@Param("operateId")Long operateId);


    public List<UserOperateVo> listByUserId(@Param("userId")Long userId);

 }
