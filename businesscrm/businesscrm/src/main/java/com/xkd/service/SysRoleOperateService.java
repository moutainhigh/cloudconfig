package com.xkd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.SysRoleOperateDao;
import com.xkd.model.Operate;

@Service
public class SysRoleOperateService {
	@Autowired
	SysRoleOperateDao sysRoleOperateDao;

	public int insertList(@Param("list") List<Map<String, Object>> list) {
		if (list.size()>0) {
			return sysRoleOperateDao.insertList(list);	
		}
		return 0;
	}

	public int deleteByRoleId(@Param("roleId") String roleId){
		return sysRoleOperateDao.deleteByRoleId(roleId);
	}

	public int deleteByOperateId(@Param("operateId") String operateId){
		return sysRoleOperateDao.deleteByOperateId(operateId);
	}
	
	
	public List<Operate> selectOperateByRoleId( String roleId){
		if(StringUtils.isNotBlank(roleId)){
			return sysRoleOperateDao.selectOperateByRoleId(roleId);
		}
		return new ArrayList<>();
	}
	
	List<String> selectOpereateIdsByRoleId( String roleId){
		return sysRoleOperateDao.selectOpereateIdsByRoleId(roleId);
	}
	
	public void  copyOperatesToNewRole(String newRoleId,String fromRoleId){
		 sysRoleOperateDao.copyOperatesToNewRole(newRoleId, fromRoleId);
	}

}
