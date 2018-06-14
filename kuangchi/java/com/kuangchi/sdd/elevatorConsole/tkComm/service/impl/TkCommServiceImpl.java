package com.kuangchi.sdd.elevatorConsole.tkComm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
import com.kuangchi.sdd.elevatorConsole.tkComm.dao.TkCommDao;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-12-06 
 * @功能描述: 梯控通讯服务器-service实现层
 */

@Transactional
@Service("tkCommServiceImpl")
public class TkCommServiceImpl implements TkCommService{

	@Resource(name = "tkCommDaoImpl")
	private TkCommDao tkCommDao;
	
	 /**
	  *  查询所有梯控通讯服务器Ip 
	  *  by huixian.pan
	  */
	 public List<Map>  getTkCommIp(){
		 return tkCommDao.getTkCommIp();
	 }
	 
	 /**
	  *  判断是否已有通讯服务器
	  *  by huixian.pan
	  *  */
	 public boolean  getTkCommIpCountByIp(Map map){
		if(tkCommDao.getTkCommIpCountByIp(map)>0){
			return true;
		}else{
			return false;
		} 
	 }
	 
	 public boolean  getTkCommIpCountByIpA(Map map){
		 if(tkCommDao.getTkCommIpCountByIpA(map)>0){
			 return true;
		 }else{
			 return false;
		 } 
	 }
	 
	 /**
	  * 通过设备编号获取梯控通讯服务器Url 
	  * by huixian.pan
	  */
	 public String  getTkCommUrl(String deviceNum){
		 String commUrl="";
		 if(!"".equals(deviceNum) && null != deviceNum){
			 Map map1=new HashMap();
			 map1.put("deviceNum", deviceNum);
			 Map map=tkCommDao.getTkCommMessByNum(map1);
			 if(map != null && map.size()>0){
				commUrl="http://"+map.get("ip")+":"+map.get("port")+"/comm/";
			 }
		 }
		
		 return  commUrl;
	 }
	 
	 /**
	  * 通过设备Mac地址获取设备编号  
	  * by huixian.pan
	  * */
	 public String  getTkDevNumByMac(String mac){
		 String deviceNum="";
		 if(!"".equals(tkCommDao.getTkDevNumByMac(mac)) && null != tkCommDao.getTkDevNumByMac(mac)){
			 deviceNum=tkCommDao.getTkDevNumByMac(mac);
		 }
		 return deviceNum;
	 }
	 
	 /**
	  * 判断是否有设备使用此通讯服务器   
	  * by huixian.pan
	  * */
	 public boolean  ifTkCommIpUsed(String id){
		 if(tkCommDao.ifTkCommIpUsed(id)>0){
			 return true;
		 }else{
			 return false;
		 }
	 }
}
