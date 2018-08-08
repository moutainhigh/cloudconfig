package com.wjh.companydemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonArray;
import com.wjh.companydemo.mapper.CompanyMapper;
import com.wjh.companydemomodel.model.CompanyPo;
import com.wjh.companydemomodel.model.CompanySearchDto;
import com.wjh.companydemomodel.model.CompanyVo;
import com.wjh.idconfiguration.model.IdGenerator;
import org.apache.lucene.search.similarities.LMSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CompanyService {
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    CompanyIndexService companyIndexService;



    public int insert(CompanyPo companyPo, Long loginUserId) {
        companyPo.setId(idGenerator.generateId());
        Date date = new Date();
        companyPo.setUpdatedBy(loginUserId);
        companyPo.setCreatedBy(loginUserId);
        companyPo.setCreateDate(date);
        companyPo.setUpdateDate(date);
        int count = companyMapper.insert(companyPo);
        //构建索引
        String jsonString = JSON.toJSONString(companyPo);
        Map<String, Object> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
        });
        companyIndexService.addCompanyIndex(map);
        return count;
    }


    public int update(CompanyPo companyPo, Long loginUserId) {
        Date date = new Date();
        companyPo.setUpdatedBy(loginUserId);
        companyPo.setUpdateDate(date);
        int count = companyMapper.update(companyPo);


        //构建索引
        String jsonString = JSON.toJSONString(companyPo);
        Map<String, Object> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
        });
        companyIndexService.addCompanyIndex(map);
        return count;
    }


    public void rebuildIndex(){
        List<CompanyVo> companyList = companyMapper.select();
        List<Map<String,Object>> list=new ArrayList();
        for (int i = 0; i < companyList.size() ; i++) {
            String jsonString = JSON.toJSONString(companyList.get(i));
            Map<String, Object> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
            });
            list.add(map);
        }
        companyIndexService.deleteIndex();
        companyIndexService.addCompanyIndexList(list);
    }


   public void search(CompanySearchDto companySearchDto){
        companyIndexService.search(companySearchDto);
   }

}
