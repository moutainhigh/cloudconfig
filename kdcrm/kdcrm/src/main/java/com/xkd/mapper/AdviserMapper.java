package com.xkd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Adviser;

public interface AdviserMapper {

	Adviser selectAdviserById(@Param("id") String answerPersonId);

	List<Adviser> selectAdviserByType(@Param("type") String type);

	Adviser selectAdviserByName(@Param("name") String name);

	List<Adviser> selectAdviserByNameMH(@Param("adviserName") String adviserName);

	List<Adviser> selectAdvisers(@Param("ttypeOrder") String ttypeOrder, @Param("levelOrder") String levelOrder, @Param("adviserNameOrder") String adviserNameOrder, @Param("pageSizeInt") int pageSizeInt, @Param("currentPageInt") int currentPageInt);

	Integer saveAdviser(Adviser adviser);

	Integer deleteAdviserByIds(@Param("ids") String ids);

	Integer updateAdviserById(Adviser adviser);

	Integer selectAdviserCount();

	List<Adviser> selectAdvisersByType(@Param("ttype") String ttype, @Param("pageSizeInt") int pageSizeInt, @Param("currentPageInt") int currentPageInt);
	
	
}
