package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.SysUserOperateDao;
import com.xkd.model.Operate;

@Service
public class SysUserOperateService {
	@Autowired
	SysUserOperateDao sysUserOperateDao;

	public int insertList(List<Map<String, Object>> list) {
		return sysUserOperateDao.insertList(list);
	}

	public int deleteByUserId(String userId) {
		return sysUserOperateDao.deleteByUserId(userId);
	}

	public int deleteByUserIds(List<String> userIdList){
		return sysUserOperateDao.deleteByUserIds(userIdList);
	}

	public int deleteByOperateId(String operateId) {
		return sysUserOperateDao.deleteByOperateId(operateId);
	}

	public List<Operate> selectOperateByUserId(String userId) {
		return sysUserOperateDao.selectOperateByUserId(userId);
	}
	
	
	public List<String> selectOperateIdsByUserId(String userId){
		return sysUserOperateDao.selectOperateIdsByUserId(userId);
	}
}
