package com.kuangchi.sdd.businessConsole.randomCode.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.businessConsole.randomCode.dao.RandomCodeDao;
import com.kuangchi.sdd.businessConsole.randomCode.model.RandomCode;
import com.kuangchi.sdd.businessConsole.randomCode.service.RandomCodeService;
@Service("randomCodeServiceImpl")
public class RandomCodeServiceImpl implements RandomCodeService {
	@Resource(name="randomCodeDaoImpl")
	private RandomCodeDao randomCodeDao;
	@Override
	public boolean insertRandCodeInfo(Map map) {
		return randomCodeDao.insertRandCodeInfo(map);
	}
	@Override
	public List<RandomCode> getRandCodeByUserMail(String staffMail) {
		return randomCodeDao.getRandCodeByUserMail(staffMail);
	}
	@Override
	public Integer getCountByRandCode(Map map) {
		return randomCodeDao.getCountByRandCode(map);
	}

}
