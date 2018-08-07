package com.wjh.companydemo.service;

import com.wjh.companydemo.mapper.CompanyMapper;
import com.wjh.companydemomodel.model.CompanyPo;
import com.wjh.companydemomodel.model.CompanyVo;
import com.wjh.idconfiguration.model.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CompanyService {
//    @Autowired
//    IdGenerator idGenerator;
//    @Autowired
//    CompanyMapper companyMapper;
//
//    @Autowired
//    ElasticSearchCompanyIndexService elasticSearchCompanyIndexService;
//
//    public List<CompanyVo> select() {
//        return companyMapper.select();
//    }
//
//    public int insert(CompanyPo companyPo, Long loginUserId) {
//        companyPo.setId(idGenerator.generateId());
//        Date date = new Date();
//        companyPo.setUpdatedBy(loginUserId);
//        companyPo.setCreatedBy(loginUserId);
//        companyPo.setCreateDate(date);
//        companyPo.setUpdateDate(date);
//        int count= companyMapper.insert(companyPo);
//        //构建索引
//        elasticSearchCompanyIndexService.singleInsert(companyPo);
//        return  count;
//    }
//
//
//    public int update(CompanyPo companyPo,Long loginUserId){
//        Date date = new Date();
//        companyPo.setUpdatedBy(loginUserId);
//        companyPo.setUpdateDate(date);
//        int count= companyMapper.update(companyPo);
//        elasticSearchCompanyIndexService.singleInsert(companyPo);
//        return count;
//    }
}
