package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SysPagesMapper {

	List<Map<String, Object>> selectColumnsByPageID(@Param("pageId") String pageId);

}
