package com.kuangchi.sdd.businessConsole.user.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.kuangchi.sdd.base.aop.Log;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.menu.dao.IMenuDao;
import com.kuangchi.sdd.businessConsole.role.dao.IRoleDao;
import com.kuangchi.sdd.businessConsole.station.dao.IStationDao;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.user.dao.IUserDao;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.businessConsole.user.service.IUserService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EncodeUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceSupport implements IUserService {
	
	@Resource(name = "userDaoImpl")
	private IUserDao userDao;
	
	@Resource(name = "roleDaoImpl")
	private IRoleDao roleDao;
	
	@Resource(name = "departmentDaoImpl")
	private IDepartmentDao departmentDao;
	
	@Resource(name = "stationDaoImpl")
	private IStationDao stationDao;
	
	@Resource(name = "menuDaoImpl")
	private IMenuDao menuDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Log(operationName="系统登录",operationType="登录")
	public User getLoginUser(User user) {
		return userDao.getLoginUser(user);

	}

	public Grid<User> getUsers(User userPage) {
		Grid<User> grid = new Grid<User>();
		List<User> userList=userDao.getUsers(userPage);
		for(User user:userList){
			user.setDefaultRoleName(userDao.getCurrentRoleNameByStaffNum(user.getYhDm()));
		}
		grid.setRows(userList);
		grid.setTotal(userDao.countUsers(userPage));
		return grid;
	}

	public void addNewUser(User user,String jsDm) {
		user.setUUID(UUIDUtil.uuidStr());
		if(user.getYhDm() == null){
			user.setYhDm(UUIDUtil.uuidStr());
		}
		user.setGlyBj(GlobalConstant.GLY_BJ_N);
		user.setZfBj(GlobalConstant.ZF_BJ_N);
		user.setYhMm(EncodeUtil.encode(GlobalConstant.DEFAULT_PWD));
		user.setYgxz(GlobalConstant.YGXZ);
		user.setLrSj(DateUtil.getSysTimestamp());
		userDao.addNewUser(user);
		roleDao.insertUserDefaultRole(user.getYhDm(), jsDm,user.getLrryDm());
		
		/*TODO 插入部门信息*/
		
	//	departmentDao.addUserDepart(user.getYhDm(),user.getBmDm(),user.getLrryDm());
		
	}


	public void setUser(User user,String jsDm) {
		user.setUUID(UUIDUtil.uuidStr());
		user.setGlyBj(GlobalConstant.GLY_BJ_N);
		user.setZfBj(GlobalConstant.ZF_BJ_N);
		user.setYhMm(EncodeUtil.encode(GlobalConstant.DEFAULT_PWD));
		user.setYgxz(GlobalConstant.YGXZ);
		user.setLrSj(DateUtil.getSysTimestamp());
		userDao.addNewUser(user);
		roleDao.insertUserDefaultRole(user.getYhDm(), jsDm,user.getLrryDm());
		
		/*TODO 插入部门信息*/
		
		//	departmentDao.addUserDepart(user.getYhDm(),user.getBmDm(),user.getLrryDm());
		
	}
	
	@Override
	public String[] getUserAdditionMenu(String userId) {
		
		return userDao.getUserAdditionMenu(userId);
	}

	@Override
	public void changeUserAdditionMenu(String userId, String[] cdDm,String lrryDm,Timestamp start,Timestamp end) {
		userDao.deleteUserAdditionMenu(userId);
		userDao.addUserAdditionMenu(userId,cdDm,lrryDm,start,end);
		
	}

	@Override
	public User getUserByYhDm(String yhDm) {
		return userDao.getUserByYhDm(yhDm);
	}

	@Override
	public void updateUser(User user) {
		
		userDao.updateUser(user);
		
	}

	@Override
	public void modifyUserPwd(String yhDm, String pwd) {
		userDao.modifyUserPwd(yhDm,pwd);
		
	}

	@Override
	public void deleteUser(String yhDms) {
		//删除用户
		userDao.deleteUsers(yhDms);
		//删除用户角色
		roleDao.deleteUserSRoles(yhDms);
		//删除用户部门
		departmentDao.deleteUserSDep(yhDms);
		//删除用户岗位
		stationDao.deleteUsersSta(yhDms);
		//删除用户额外菜单
		menuDao.deleteUsersAdditionMenu(yhDms);
		
	}

	@Override
	public void changeUserDepartment(String yhDm, String[] bmDmS, String lrryhDm) {
		//删除现有部门
		
		departmentDao.deleteUserSDep("'" +yhDm + "'");
		//新增部门
		
		departmentDao.addUserDeparts(yhDm, bmDmS, lrryhDm);
		
	}

	@Override
	public JsonResult validUser(String yhDm, String yhMc) {
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		
		StringBuilder msg = new StringBuilder();
		int yhDmNum = userDao.isContainYhDM(yhDm);
		int yhMcNum = userDao.isContainYhMc(yhMc);
		if(yhDmNum > 0){
			result.setSuccess(false);
			msg.append("用户代码已存在！ ");
		}
		
		if(yhMcNum > 0){
			result.setSuccess(false);
			msg.append("用户名已存在！");
		}
		result.setMsg(msg.toString());
		
		return result;
	}

    @Override
    public Map<String, List<Station>> getUserDepartStations(String yhDm) {
        //用户所有部门
        String[] uerAllDep = departmentDao.getUserDepartment(yhDm);

        Map<String, List<Station>> all = null;

        if(uerAllDep.length > 0 && null != uerAllDep[0]){
            all = new HashMap<String, List<Station>>();
            for (String s:uerAllDep){
                List<Station> tmp = stationDao.getBmStations(s);
                all.put(s,tmp);
            }
        }

        return all;
    }

    @Override
    public String getUserGws(String yhDm) {
        String[] userGws = stationDao.getUserGws(yhDm);
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < userGws.length; i++) {
            builder.append("'");
            builder.append(userGws[i]);
            builder.append("'");

            if (i != userGws.length - 1){
                builder.append(",");
            }
        }

        builder.append("]");
        return builder.toString();
    }

    @Override
    public void updateUserStations(String yhDm, String[] gwds, String lrryDm) {
        //删除现有岗位
        stationDao.deleteUsersSta("'"+yhDm+"'");
        //新增岗位
        stationDao.addUserStations(yhDm,gwds,lrryDm);
    }

	@Override
	public User getLoginUserByYhMc(String yhMc) {
		return userDao.getLoginUserByYhMc(yhMc);
	}

	@Override
	public Integer selectYhDm(String yhDm) {
		return userDao.selectYhDm(yhDm);
	}

	@Override
	public String isSeeLoginOutBtn() {
		return userDao.isSeeLoginOutBtn();
	}

	@Override
	public String isLikeCSLogin() {
		return userDao.isLikeCSLogin();
	}

	@Override
	public Integer validPwd(String userName, String password) {
		return userDao.validPwd(userName, password);
	}

	@Override
    public String getLrryDm(String yhDm){
    	 /*StringBuffer sb = new StringBuffer();
    	 
    	 List<User> childUserList = userDao.getUsersByLrryDm(yhDm);
    	 if(childUserList != null && childUserList.size() !=0) {
    		 sb.append("'" + yhDm + "',");
    	 }
    	 for (User childUser : childUserList) {
    		 String lrryDm = getLrryDm(childUser.getYhDm());
    		 sb.append(lrryDm);
		 }
    	 
    	 return sb.toString();*/
		
		StringBuffer sb = new StringBuffer();
		sb.append("'" + yhDm + "',");
		
	   	List<User> childUserList = userDao.getUsersByLrryDm(yhDm);
	   	if(childUserList != null && childUserList.size() !=0) {
	   		for (User childUser : childUserList) {
		   		 String lrryDm = getLrryDm(childUser.getYhDm());
		   		 sb.append(lrryDm);
			}
	   	}
	   	return sb.toString();
    }

	@Override
	public List<String> getRoleXtAuths(String roleDm) {
		return userDao.getRoleXtAuths(roleDm);
	}
	
	@Override
	public void licenseLog(String trigger, String flag, String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			if("0".equals(trigger)){
				log.put("V_OP_FUNCTION", "激活");
			}else {
				log.put("V_OP_FUNCTION", "修改");
			}
			if("0".equals(flag)){
				log.put("V_OP_TYPE", "业务");
			}else {
				log.put("V_OP_TYPE", "异常");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			log.put("V_OP_NAME", "系统授权码管理");
			log.put("V_OP_MSG", "系统授权码管理");
			log.put("V_OP_ID", login_user);
			logDao.addLog(log);
		}
	}

	@Override
	public void addLogger(String userDm,String flag) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			if("1".equals(flag)){
				log.put("V_OP_NAME", "系统登录");
				log.put("V_OP_FUNCTION", "登录");
				log.put("V_OP_ID", userDm);
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "系统登录");
			}else if("2".equals(flag)){
				log.put("V_OP_NAME", "系统退出");
				log.put("V_OP_FUNCTION", "退出");
				log.put("V_OP_ID", userDm);
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "系统退出");
			}else if("0".equals(flag)){
				log.put("V_OP_NAME", "系统登录");
				log.put("V_OP_FUNCTION", "登录");
				log.put("V_OP_ID", userDm);
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "系统登录");
			}
			logDao.addLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
