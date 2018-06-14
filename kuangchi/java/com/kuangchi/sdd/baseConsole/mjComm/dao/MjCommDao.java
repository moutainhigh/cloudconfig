package com.kuangchi.sdd.baseConsole.mjComm.dao;

import java.util.List;
import java.util.Map;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-07 
 * @功能描述: 门禁通讯服务器-dao层
 */

public interface MjCommDao {
	
	 /**
	  * 查询所有门禁通讯服务器信息 
	  * by huixian.pan
	  */
     public List<Map>  getMjCommIpMess(Map map);
     
     /**
      *  查询所有门禁通讯服务器信息条数  
      *  by huixian.pan
      */
     public Integer  countMjCommIpMess(Map map);
     
     /**
      * 通过门禁通讯服务器id查询通讯服务器信息  
      * by huixian.pan
      */
	 public Map getMjCommIpById(String id);
	
	 /**
	  * 新增门禁通讯服务器信息 
	  * by huixian.pan
	  */
	 public boolean addMjCommIp(Map map);
	 
	 /**
	  * 删除门禁通讯服务器信息
	  * by huixian.pan 
	  * */
     public boolean delMjCommIp(String id);
     
     /**
      * 修改门禁通讯服务器信息
      * by huixian.pan
      * */
     public boolean updateMjCommIp(Map map);
     
	 /**
	  * 查询所有门禁通讯服务器Ip 
	  * by huixian.pan
	  * */
	 public List<Map>  getMjCommIp();
	 
	 /**
	  *  通过设备编号查询门禁通讯服务器IP地址，port端口号 
	  *  by huixian.pan
	  *  */
	 public Map  getMjCommMessByNum(Map map);
	 
	 
	 /**
	  * 通过设备Mac地址获取设备编号  
	  * by huixian.pan
	  * */
	 public String  getMjDevNumByMac(String mac);
	 
	 
	 /** 
	  * 通过门禁通讯服务器ip和端口号查询已有通讯服务器条数 
	  * by huixian.pan
	  * */
	 public Integer  getMjCommIpCountByIp(Map map);
	 
	 public Integer  getMjCommIpCountByIpA(Map map);
	 
	 /** 
	  * 判断是否有设备使用此通讯服务器 
	  * by huixian.pan
	  * */
	 public Integer  ifMjCommIpUsed(String id);
	
	
}
