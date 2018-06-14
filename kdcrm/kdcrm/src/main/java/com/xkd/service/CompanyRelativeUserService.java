package com.xkd.service;

import com.xkd.mapper.CompanyRelativeUserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/7.
 */
@Service
public class CompanyRelativeUserService {

    @Autowired
    CompanyRelativeUserMapper companyRelativeUserMapper;
    public int insertList(List<Map<String,Object>> list){
      return   companyRelativeUserMapper.insertList(list);
    }

    public int deleteByCompanyId( String companyId){
        return companyRelativeUserMapper.deleteByCompanyId(companyId);
    }

    public List<Map<String,Object>>  selectRelativeUserListByCompanyIds(List<String>  companyIdList){
            if (companyIdList.size()>0) {
                return companyRelativeUserMapper.selectRelativeUserListByCompanyIds(companyIdList);
            }else{
                return new ArrayList<>();
            }
    }


    public int deleteByCompanyIdsString( String companyIds){
        return companyRelativeUserMapper.deleteByCompanyIdsString(companyIds);
    }

}
