package com.xkd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Label;

public interface LabelMapper {

	List<Label> selectLabelsByCompanyId(@Param("companyId") String companyId);

	int updateHasResourceLabelById(Label label);

	int updateNeedResourceLabelById(Label label);
}
