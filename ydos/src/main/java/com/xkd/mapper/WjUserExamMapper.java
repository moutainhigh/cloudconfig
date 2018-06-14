package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

public interface WjUserExamMapper {

	Integer  deleteWjUserExam(@Param("sql") String sql);
}
