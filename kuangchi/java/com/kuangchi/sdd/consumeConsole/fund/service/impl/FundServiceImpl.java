package com.kuangchi.sdd.consumeConsole.fund.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.fund.dao.IFundDao;
import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;
import com.kuangchi.sdd.consumeConsole.fund.service.IFundService;

@Transactional
@Service("fundServiceImpl")
public class FundServiceImpl implements IFundService{
	
	@Resource(name = "fundDaoImpl")
	private IFundDao fundDao;

	@Override
	public List<FundModel> getFundByParamPage(Map<String, Object> map) {
		return fundDao.getFundByParamPage(map);
	}

	@Override
	public Integer getFundByParamCount(Map<String, Object> map) {
		return fundDao.getFundByParamCount(map);
	}
	
	

}
