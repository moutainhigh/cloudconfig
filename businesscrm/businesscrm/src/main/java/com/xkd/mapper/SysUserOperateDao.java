package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Operate;

public interface SysUserOperateDao {
	public int insertList(@Param("list") List<Map<String, Object>> list);
	public int deleteByUserId(@Param("userId") String userId);
	public int deleteByOperateId(@Param("operateId") String operateId);
	public List<Operate> selectOperateByUserId(@Param("userId") String userId);
	public List<String> selectOperateIdsByUserId(@Param("userId") String userId);
	public int deleteByUserIds(@Param("userIdList")List<String> userIdList);
}
