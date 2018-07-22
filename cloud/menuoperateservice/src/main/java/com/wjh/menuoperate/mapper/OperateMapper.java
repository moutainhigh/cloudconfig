package com.wjh.menuoperate.mapper;

import com.wjh.menuoperateservicemodel.model.OperatePo;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateMapper {

    public int insert(OperatePo operatePo);

    public int update(OperatePo operatePo);

    public List<OperateVo> search(@Param("operateName") String operateName, @Param("start") int start, @Param("pageSize") int pageSize);

    public int searchCount(@Param("operateName") String operateName);

    public int delete(@Param("id") Long id);

}
