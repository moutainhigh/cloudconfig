package com.kuangchi.sdd.baseConsole.mjComm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.mjComm.dao.MjCommDao;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-07 
 * @功能描述: 门禁通讯服务器-dao实现层
 */

@Repository("mjCommDaoImpl")
public class MjCommDaoImpl extends BaseDaoImpl<Object> implements MjCommDao{

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	  * 查询所有门禁通讯服务器信息 
	  * by huixian.pan
	  */
    public List<Map>  getMjCommIpMess(Map map){
    	return  getSqlMapClientTemplate().queryForList("getMjCommIpMess", map);
    }
    
    /**
     *  查询所有门禁通讯服务器信息条数  
     *  by huixian.pan
     */
    public Integer  countMjCommIpMess(Map map){
    	return (Integer) getSqlMapClientTemplate().queryForObject("countMjCommIpMess", map);
    }
    
    /**
     * 通过门禁通讯服务器id查询通讯服务器信息  
     * by huixian.pan
     */
	 public Map getMjCommIpById(String id){
		 return (Map) getSqlMapClientTemplate().queryForObject("getMjCommIpById", id);
	 }
	
	 /**
	  * 新增门禁通讯服务器信息 
	  * by huixian.pan
	  */
	 public boolean addMjCommIp(Map map){
		 return this.insert("addMjCommIp", map);
	 }
	 
	 /**
	  * 删除门禁通讯服务器信息
	  * by huixian.pan 
	  * */
    public boolean delMjCommIp(String id){
    	return this.update("delMjCommIp", id);
    }
    
    /**
     * 修改门禁通讯服务器信息
     * by huixian.pan
     * */
    public boolean updateMjCommIp(Map map){
    	return this.update("updateMjCommIp", map);
    }
	

	 /**
	  *  查询所有门禁通讯服务器Ip 
	  *  by huixian.pan
	  *  */
	 public List<Map>  getMjCommIp(){
		 return  getSqlMapClientTemplate().queryForList("getMjCommIp");
	 }
	 
	 /**
	  *  通过设备编号查询门禁通讯服务器IP地址，port端口号 
	  *  by huixian.pan
	  *  */
	 public Map getMjCommMessByNum(Map map){
		 return (Map) getSqlMapClientTemplate().queryForObject("getMjCommMessByNum", map);
	 }
	 
	 /**
	  * 通过设备Mac地址获取设备编号  
	  * by huixian.pan
	  * */
	 public String  getMjDevNumByMac(String mac){
		 return (String) getSqlMapClientTemplate().queryForObject("getMjDevNumByMac",mac);
	 }
	 
	 /**
	  *  通过消费通讯服务器ip和端口号查询已有通讯服务器条数 
	  *  by huixian.pan
	  *  */
	 public Integer  getMjCommIpCountByIp(Map map){
         return (Integer) getSqlMapClientTemplate().queryForObject("getMjCommIpCountByIp", map);		 
	 }
	 
	 public Integer  getMjCommIpCountByIpA(Map map){
		 return (Integer) getSqlMapClientTemplate().queryForObject("getMjCommIpCountByIpA", map);		 
	 }
	 
	 /** 
	  * 判断是否有设备使用此通讯服务器 
	  * by huixian.pan
	  * */
	 public Integer  ifMjCommIpUsed(String id){
		 return  (Integer) getSqlMapClientTemplate().queryForObject("ifMjCommIpUsed", id);
	 }
}
