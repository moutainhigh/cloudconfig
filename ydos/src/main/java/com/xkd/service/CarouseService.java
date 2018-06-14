package com.xkd.service;

import com.xkd.mapper.CarouseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/9.
 */
@Service
public class CarouseService {
    @Autowired
    CarouseMapper carouseMapper;
   public int insert(Map<String,Object> map){
       return carouseMapper.insert(map);
    }
    public int deleteByTType( Integer ttype,String pcCompanyId){
        return carouseMapper.deleteByTType(ttype,pcCompanyId);
    }
    public List<Map<String,Object>> selectByTType(Integer ttype,List<String> pcCompanyIdList){
        return carouseMapper.selectByTType(ttype,pcCompanyIdList);
    }


   public int deleteById(@Param("id")String  id){
        return carouseMapper.deleteById(id);
    }


}
