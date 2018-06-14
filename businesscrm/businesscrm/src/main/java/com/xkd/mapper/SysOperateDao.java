package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Operate;

public interface SysOperateDao {
	public int insert(Map<String, Object> map);
	public int update(Map<String, Object> map);
	public int delete(@Param("id") String id);
	public List<Operate> selectAllOperate();
 	public List<Operate> searchOperate(@Param("operateName") String operateName, @Param("menuId") String menuId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);
	public Integer searchOperateCount(@Param("operateName") String operateName, @Param("menuId") String menuId);

	public List<Map<String,Object>> selectOperateByOperateCode(@Param("operateCode") String operateCode);

	public List<Operate> selectOperatesByDepartmentId(@Param("pcCompanyId") String pcCompanyId);
	
	
}
