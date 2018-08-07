package com.wjh.companydemo.mapper;

import com.wjh.companydemomodel.model.CompanyPo;
import com.wjh.companydemomodel.model.CompanyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyMapper {

    public List<CompanyVo> select();

    public int insert(CompanyPo companyPo);

    public int update(CompanyPo companyPo);


}
