package com.wjh.useroperateservice.service;

import com.wjh.menuoperateservicemodel.model.OperateVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserOperateService {
    public List<OperateVo> listByUserId(Long userId){
        return new ArrayList<OperateVo>();
    }
}
