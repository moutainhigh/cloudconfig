package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.RoleSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.RoleSyncService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("roleSyncAction")
public class RoleSyncAction extends BaseActionSupport {
 
	@Resource(name = "roleSyncServiceImpl")
	 private RoleSyncService roleSyncService;
	@Override
	public Object getModel() {
		return null;
	}
	/**
	 * 获取所有角色
	 */
		public void getAlRoleSync(){
			List<RoleSyncModel> RoleSync=(List<RoleSyncModel>) roleSyncService.getAllRoleSync();
			printHttpServletResponse(GsonUtil.toJson(RoleSync));
		}
		
       
	}
	
	

