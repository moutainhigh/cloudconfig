package com.kuangchi.sdd.elevator.authority.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevator.model.Result;

public interface ElevatorAuthorityService {
	/**
	 * 下发权限
	 * by gengji.yang
	 */
	public Result setAuth(Map map);
	
	/**
	 * 删除权限
	 * by gengji.yang
	 */
	public Result delAuth(Map map);


}
