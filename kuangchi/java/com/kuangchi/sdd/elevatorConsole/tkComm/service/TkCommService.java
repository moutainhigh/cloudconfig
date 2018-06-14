package com.kuangchi.sdd.elevatorConsole.tkComm.service;

import java.util.List;
import java.util.Map;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 梯控通讯服务器-service层
 */
public interface TkCommService {
	
	 /* 查询所有梯控通讯服务器Ip by huixian.pan*/
	 public List<Map>  getTkCommIp();
	 
	 /* 判断是否已有通讯服务器 by huixian.pan*/
	 public boolean  getTkCommIpCountByIp(Map map);
	 
	 public boolean  getTkCommIpCountByIpA(Map map);
	 
	 /* 通过设备编号查询通讯服务器IP地址，port端口号 by huixian.pan*/
	 public String  getTkCommUrl(String deviceNum);
	 
	 /*通过设备Mac地址获取设备编号   by huixian.pan*/
	 public String  getTkDevNumByMac(String mac);
	 
	 /*判断是否有设备使用此通讯服务器   by huixian.pan*/
	 public boolean  ifTkCommIpUsed(String id);
}
