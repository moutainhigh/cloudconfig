package com.xkd.service;

import com.xkd.mapper.CompanyContactorMapper;
import com.xkd.mapper.InspectionMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/11.
 */
@Service
public class CompanyContactorService {
    @Autowired
    CompanyContactorMapper companyContactorMapper;

    public List<Map<String, Object>> selectCompanyContactorByCompanyId(String companyId, String pcCompanyId, String uname, Integer currentPage, Integer pageSize) {
        int start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return companyContactorMapper.selectCompanyContactorByCompanyId(companyId, pcCompanyId, uname, start, pageSize);
    }


    public int selectCompanyContactorCountByCompanyId(String companyId, String pcCompanyId, String uname) {
        return companyContactorMapper.selectCompanyContactorCountByCompanyId(companyId, pcCompanyId, uname);
    }


    public int deleteById(String id) {
        return companyContactorMapper.deleteById(id);
    }

    public int deleteByIds(List<String> idList) {
        if (idList.size() > 0) {
            return companyContactorMapper.deleteByIds(idList);
        }
        return 0;
    }

    public int deleteByCompanyId(String companyId) {
        return companyContactorMapper.deleteByCompanyId(companyId);
    }


    public int insertCompanyContactorList(List<Map<String, Object>> list) {
        return companyContactorMapper.insertCompanyContactorList(list);
    }


    public Map<String, Object> selectCompanyContactorById(String id) {
        return companyContactorMapper.selectCompanyContactorById(id);
    }


    public List<String> selectCompanyIdListByContactor(String userId,Integer includingSharer) {
        return companyContactorMapper.selectCompanyIdListByContactor(userId,includingSharer);
    }
    public List<String> selectPcCompanyIdListByContactor(@Param("userId")String userId){
        return companyContactorMapper.selectPcCompanyIdListByContactor(userId);
    }


    public Map<String, Object> selectCompanyContactorByCompanyIdAndUserIdAndPcCompanyId(String companyId, String userId, String pcCompanyId) {
        return companyContactorMapper.selectCompanyContactorByCompanyIdAndUserIdAndPcCompanyId(companyId, userId, pcCompanyId);
    }

    public List<String> getCompanyIdByuserId(String userId){
        return companyContactorMapper.getCompanyIdByUserId(userId);
    }


    List<Map<String,Object>>  selectCompanyContactorByMutileCondition(  String pcCompanyId, String companyId, String userId){
        return companyContactorMapper.selectCompanyContactorByMutileCondition(pcCompanyId,companyId,userId);
    }

   public int update(Map<String,Object> map){
        return companyContactorMapper.update(map);
    }

   public List<String> selectAllUserIdByPcCompanyIdAndCompanyId( String pcCompanyId, String companyId){
       return companyContactorMapper.selectAllUserIdByPcCompanyIdAndCompanyId(pcCompanyId,companyId);
   }




}
