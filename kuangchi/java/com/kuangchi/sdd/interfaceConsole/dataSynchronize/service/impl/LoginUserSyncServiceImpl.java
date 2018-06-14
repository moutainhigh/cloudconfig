package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.LoginUserSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.UserRoleDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.LoginUserSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.LoginUserSyncService;

@Transactional
@Service("loginUserSyncServiceImpl")
public class LoginUserSyncServiceImpl extends BaseServiceSupport implements LoginUserSyncService {

	@Resource(name = "loginUserSyncDaoImpl")
	private LoginUserSyncDao loginUserSyncDao;
	
	@Resource(name = "userRoleDaoImpl")
	private UserRoleDao userRoleDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	

	@Override
	public void addLoginUserSync(LoginUserSyncModel loginUserSync) {
		int count=this.loginUserSyncDao.getCountByYh_Mc(loginUserSync.getYh_mc()).size();
		if(count==0){  
			this.loginUserSyncDao.addLoginUserSync(loginUserSync);
		}else{
			userRoleDao.delUserRole(loginUserSync.getYh_mc());
			this.loginUserSyncDao.add_modifyLoginUserSync(loginUserSync);
		}
		
	}
	@Override
	public void modifyLoginUserSync(LoginUserSyncModel loginUserSync) {
		int count=this.loginUserSyncDao.getCountByYh_Mc(loginUserSync.getYh_mc()).size();
		if(count==0){
			this.loginUserSyncDao.modifyLoginUserSync(loginUserSync);
		}else{
			this.loginUserSyncDao.modifyLoginUser_NoRole(loginUserSync);
		}
		
	}
	
	//删除用户，则用户和角色都删掉
	@Override    
	public void delLoginUserSync(String userName) {
		loginUserSyncDao.delLoginUserSync(userName);
		userRoleDao.delUserRole(userName);
	}
	@Override
	public void modifyLoginUser_Role(String yh_mc, String supperRole) {
		loginUserSyncDao.modifyLoginUser_Role(yh_mc, supperRole);
	}


}
