package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserDynamicMapper {

	List<Map<String, Object>> selectUserDynamicByGroupId(@Param("groupId") String groupId, @Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("orderFlag") String orderFlag);
	
	public int selectUserDynamicCountByGroupId(@Param("groupId") String groupId);
	
	Integer saveUserDynamic(Map<String, Object> map);
	
	Integer deleteUserDynamicByIds(@Param("ids") List<String> ids);

	void changeUserDynamic(Map<String, String> userDynamic);
}
