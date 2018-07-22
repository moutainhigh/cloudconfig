package com.wjh.menuoperate.service;

import com.wjh.menuoperate.mapper.OperateMapper;
import com.wjh.menuoperateservicemodel.model.OperatePo;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateService {


    @Autowired
    IdService idService;

    @Autowired
    OperateMapper operateMapper;

    public OperatePo insert(OperatePo operatePo) {
        Long id=idService.generateId();
        operatePo.setId(id);
        operateMapper.insert(operatePo);
        return operatePo;
    }

    public OperatePo update(OperatePo operatePo) {
         operateMapper.update(operatePo);
         return operatePo;
    }

    public List<OperateVo> search(String operateName,int currentPage,int pageSize) {
        if (currentPage<1){
            currentPage=1;
        }
        if (pageSize<1){
            pageSize=10;
        }
        int start=(currentPage-1)*pageSize;
        return operateMapper.search(operateName,start,pageSize);
    }


    public Long delete(Long id) {
         operateMapper.delete(id);
         return id;
    }

}
