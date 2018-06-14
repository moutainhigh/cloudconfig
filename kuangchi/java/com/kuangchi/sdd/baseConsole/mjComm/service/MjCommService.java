package com.kuangchi.sdd.baseConsole.mjComm.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 门禁通讯服务器-service层
 */
public interface MjCommService {
	
	 /**
	  * 查询所有门禁通讯服务器信息 
	  * by huixian.pan
	  */
    public Grid  getMjCommIpMess(Map map);
    
    /**
     * 通过门禁通讯服务器id查询通讯服务器信息  
     * by huixian.pan
     */
	 public Map getMjCommIpById(String id);
	
	 /**
	  * 新增门禁通讯服务器信息 
	  * by huixian.pan
	  */
	 public boolean addMjCommIp(Map map,String create_user);
	 
	 /**
	  * 删除门禁通讯服务器信息
	  * by huixian.pan 
	  * */
    public boolean delMjCommIp(String id,String create_user);
    
    /**
     * 修改门禁通讯服务器信息
     * by huixian.pan
     * */
    public boolean updateMjCommIp(Map map,String create_user);
    
	 /**
	  * 查询所有门禁通讯服务器Ip 
	  * by huixian.pan
	  * */
	 public List<Map>  getMjCommIp();
	 
	 /**
	  *  通过设备编号查询门禁通讯服务器IP地址，port端口号 
	  *  by huixian.pan
	  *  */
	 public String  getMjCommUrl(String deviceNum);
	
	 
	 /**
	  * 通过设备Mac地址获取设备编号   
	  * by huixian.pan
	  * */
	 public String  getTkDevNumByMac(String mac);
	 
	 /**
	  *  判断是否已有通讯服务器
	  *  by huixian.pan
	  *  */
	 public boolean  getMjCommIpCountByIp(Map map);
	 
	 public boolean  getMjCommIpCountByIpA(Map map);
	
	 /** 
	  * 判断是否有设备使用此通讯服务器 
	  * by huixian.pan
	  * */
	 public boolean  ifMjCommIpUsed(String id);
}
