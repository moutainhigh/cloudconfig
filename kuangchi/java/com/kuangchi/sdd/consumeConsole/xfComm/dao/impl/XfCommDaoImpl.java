package com.kuangchi.sdd.consumeConsole.xfComm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.xfComm.dao.XfCommDao;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-07
 * @功能描述: 消费通讯服务器-dao实现层
 */

@Repository("xfCommDaoImpl")
public class XfCommDaoImpl extends BaseDaoImpl<Object> implements XfCommDao {

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
	 * 查询所有消费通讯服务器信息 by huixian.pan
	 */
	public List<Map> getXfCommIpMess(Map map) {
		return getSqlMapClientTemplate().queryForList("getXfCommIpMess", map);
	}

	/**
	 * 查询所有消费通讯服务器信息条数 by huixian.pan
	 */
	public Integer countXfCommIpMess(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"countXfCommIpMess", map);
	}

	/**
	 * 通过消费通讯服务器id查询通讯服务器信息 by huixian.pan
	 */
	public Map getXfCommIpById(String id) {
		return (Map) getSqlMapClientTemplate().queryForObject(
				"getXfCommIpById", id);
	}

	/**
	 * 新增消费通讯服务器信息 by huixian.pan
	 */
	public boolean addXfCommIp(Map map) {
		return this.insert("addXfCommIp", map);
	}

	/**
	 * 删除消费通讯服务器信息 by huixian.pan
	 * */
	public boolean delXfCommIp(String id) {
		return this.update("delXfCommIp", id);
	}

	/**
	 * 修改消费通讯服务器信息 by huixian.pan
	 * */
	public boolean updateXfCommIp(Map map) {
		return this.update("updateXfCommIp", map);
	}

	/**
	 * 查询所有消费通讯服务器Ip by huixian.pan
	 * */
	public List<Map> getXfCommIp() {
		return getSqlMapClientTemplate().queryForList("getXfCommIp");
	}

	/**
	 * 通过设备编号查询消费通讯服务器IP地址，port端口号 by huixian.pan
	 * */
	public Map getXfCommMessByNum(Map map) {
		return (Map) getSqlMapClientTemplate().queryForObject(
				"getXfCommMessByNum", map);
	}

	/**
	 * 通过消费通讯服务器ip和端口号查询已有通讯服务器条数 by huixian.pan
	 * */
	public Integer getXfCommIpCountByIp(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getXfCommIpCountByIp", map);
	}

	/**
	 * 通过通讯服务器id判断是否有设备使用此通讯服务器 by huixian.pan
	 * */
	public Integer ifXfCommIpUsed(String id) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"ifXfCommIpUsed", id);
	}

	@Override
	public String getXfCommIpIdByMap(Map map) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getXfCommIpIdByMap", map);
	}

	@Override
	public String getXfCommIpMaxId() {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getXfCommIpMaxId");
	}
}
