package com.kuangchi.sdd.elevatorConsole.tkComm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.tkComm.dao.TkCommDao;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 梯控通讯服务器-dao实现层
 */

@Repository("tkCommDaoImpl")
public class TkCommDaoImpl extends BaseDaoImpl<Object> implements TkCommDao{

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
	

	 /* 查询所有梯控通讯服务器Ip by huixian.pan*/
	 public List<Map>  getTkCommIp(){
		 return  getSqlMapClientTemplate().queryForList("getTkCommIp");
	 }
	 
	 /* 通过梯控通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan*/
	 public Integer  getTkCommIpCountByIp(Map map){
         return (Integer) getSqlMapClientTemplate().queryForObject("getTkCommIpCountByIp", map);		 
	 }
	 
	 public Integer  getTkCommIpCountByIpA(Map map){
		 return (Integer) getSqlMapClientTemplate().queryForObject("getTkCommIpCountByIpA", map);		 
	 }
	 
	 /* 通过设备编号查询通讯服务器IP地址，port端口号 by huixian.pan*/
	 public Map getTkCommMessByNum(Map map){
		 return (Map) getSqlMapClientTemplate().queryForObject("getTkCommMessByNum", map);
	 }
	 
	 /*通过设备Mac地址获取设备编号   by huixian.pan*/
	 public String  getTkDevNumByMac(String mac){
		 return (String) getSqlMapClientTemplate().queryForObject("getTkDevNumByMac",mac);
	 }
	 
	 /*判断是否有设备使用此通讯服务器   by huixian.pan*/
	 public Integer  ifTkCommIpUsed(String id){
		 return (Integer) getSqlMapClientTemplate().queryForObject("ifTkCommIpUsed", id);
	 }
}
