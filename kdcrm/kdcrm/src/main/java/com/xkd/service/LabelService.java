package com.xkd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.LabelMapper;
import com.xkd.model.Label;

@Service
public class LabelService {

	@Autowired
	private LabelMapper labelMapper;
	
	public List<Label> selectLabelsByCompanyId(String companyId){
		
		List<Label> labels = labelMapper.selectLabelsByCompanyId(companyId);
		
		return labels;
	}

	public Integer updateHasResourceLabelById(Label label) {
		
		Integer num = labelMapper.updateHasResourceLabelById(label);
		
		return num;
	}
	
	public Integer updateNeedResourceLabelById(Label label) {
		
		Integer num = labelMapper.updateNeedResourceLabelById(label);
		
		return num;
	}


}
