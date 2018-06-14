package com.kuangchi.sdd.elevatorConsole.tkComm.dao;

import java.util.List;
import java.util.Map;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 梯控通讯服务器-dao层
 */

public interface TkCommDao {
       
	 /* 查询所有梯控通讯服务器Ip by huixian.pan*/
	 public List<Map>  getTkCommIp();
	 
	 /* 通过梯控通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan*/
	 public Integer  getTkCommIpCountByIp(Map map);
	 
	 public Integer  getTkCommIpCountByIpA(Map map);
	 
	 /* 通过设备编号查询通讯服务器IP地址，port端口号 by huixian.pan*/
	 public Map  getTkCommMessByNum(Map map);
	 
	 /*通过设备Mac地址获取设备编号   by huixian.pan*/
	 public String  getTkDevNumByMac(String mac);
	 
	 /*判断是否有设备使用此通讯服务器   by huixian.pan*/
	 public Integer  ifTkCommIpUsed(String id);
	
}
