package com.xkd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.AdviserMapper;
import com.xkd.model.Adviser;

@Service
public class AdviserService {

	@Autowired
	private AdviserMapper adviserMapper;
	
	public Adviser selectAdviserById(String answerPersonId){
		
		Adviser adviser = adviserMapper.selectAdviserById(answerPersonId);
		
		return adviser;
	}

	public List<Adviser> selectAdviserByType(String type) {
		
		List<Adviser> advisers = adviserMapper.selectAdviserByType(type);
		
		return advisers;
	}

	public Adviser selectAdviserByName(String teacher) {
		
		Adviser adviser = adviserMapper.selectAdviserByName(teacher);
		
		return adviser;
	}

	public List<Adviser> selectAdviserByNameMH(String adviserName) {
		
		List<Adviser> advisers = adviserMapper.selectAdviserByNameMH(adviserName);
		
		return advisers;
	}

	public List<Adviser> selectAdvisers(String ttypeOrder, String levelOrder, String adviserNameOrder, int pageSizeInt, int currentPageInt) {
		
		List<Adviser> advisers = adviserMapper.selectAdvisers(ttypeOrder,levelOrder,adviserNameOrder,pageSizeInt,currentPageInt);
		
		return advisers;
	}

	public Integer saveAdviser(Adviser adviser) {
		
		Integer num = adviserMapper.saveAdviser(adviser);
		
		return num;
	}

	public Integer deleteAdviserByIds(String ids) {
		
		Integer num = adviserMapper.deleteAdviserByIds(ids);
		
		return num;
	}

	public Integer updateAdviserById(Adviser adviser) {
		
		Integer num = adviserMapper.updateAdviserById(adviser);
		
		return num;
	}

	public Integer selectAdviserCount() {
		
		Integer num = adviserMapper.selectAdviserCount();
		
		return num;
	}

	public List<Adviser> selectAdvisersByType(String ttype, int pageSizeInt, int currentPageInt) {
		
		List<Adviser> advisers = adviserMapper.selectAdvisersByType(ttype,pageSizeInt,currentPageInt);
		
		return advisers;
	}
}
