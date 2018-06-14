package com.kuangchi.sdd.elevatorConsole.authorityByDevice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao.AuthorityByDeviceDao;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.service.AuthorityByDeviceService;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
@Service("authorityByDeviceService")
public class AuthorityByDeviceServiceImpl implements AuthorityByDeviceService {

		@Autowired
		private AuthorityByDeviceDao authorityByDeviceDao;
		
		@Resource(name = "peopleAuthorityManagerDaoImpl")
		private PeopleAuthorityManagerDao peopleAuthorityManagerDao;

		@Override
		public Grid getAllTkDevices(Map map) {
			Grid grid=new Grid();
			grid.setRows(authorityByDeviceDao.getAllTKDevices(map));
			grid.setTotal(authorityByDeviceDao.countAllTKDevices(map));
			return grid;
		}

		@Override
		public Grid getStaffCardsBydeptNums(Map map) {
			Grid grid=new Grid();
			grid.setRows(authorityByDeviceDao.getStaffCardsBydeptNums(map));
			grid.setTotal(authorityByDeviceDao.countStaffCardsBydeptNums(map));
			return grid;
		}

		@Override
		public List<Map> getFloorGroups() {
			return authorityByDeviceDao.getFloorGroups();
		}

		@Override
		public boolean addDeviceAuth(Map map) {
			/*-----------新增权限任务到权限表前先删除原有的权限任务--------------*/
			boolean result1=true;
			if(null != map.get("cardNum")){
				Map map2=new HashMap();
				map2.put("card_num", map.get("cardNum").toString());
				map2.put("device_num",map.get("deviceNum").toString());
				result1=peopleAuthorityManagerDao.delRepeatTkAuthTask(map2);
			}
			
			/*----------------------------------------------------*/
			boolean result=authorityByDeviceDao.addDeviceAuth(map);
			
			if(result && result1){
				return true;
			}else{
				return false;
			}
		}

		@Override
		public Grid getAuths(Map map) {
			Grid grid=new Grid();
			grid.setRows(authorityByDeviceDao.getAuths(map));
			grid.setTotal(authorityByDeviceDao.countAuths(map));
			return grid;
		}

		@Override
		public boolean deleteAuths(Map map) {
			return authorityByDeviceDao.deleteAuths(map);
		}

		@Override
		public boolean preventRepAuth(Map map) {
			return authorityByDeviceDao.preventRepAuth(map);
		}

		@Override
		public Grid getTkDeviceInfo(Map map) {
			Grid grid=new Grid();
			grid.setRows(authorityByDeviceDao.getTkDeviceInfo(map));
			grid.setTotal(authorityByDeviceDao.getTkDeviceInfoCount(map));
			return grid;
		}

		@Override
		public List<Map> getTkAuthByDeviceNum(Map map) {
			return authorityByDeviceDao.getTkAuthByDeviceNum(map);
		}

		@Override
		public List<String> getFloorNum2(String groupNum) {
			return authorityByDeviceDao.getFloorNum2(groupNum);
		}

		@Override
		public Map getTkDeviceIPByDeviceNum(String device_num) {
			return authorityByDeviceDao.getTkDeviceIPByDeviceNum(device_num);
		}

		@Override
		public Grid getAuthsNoRepeat(Map map) {
			Grid grid=new Grid();
			grid.setRows(authorityByDeviceDao.getAuthsNoRepeat(map));
			grid.setTotal(authorityByDeviceDao.countAuthsNoRepeat(map));
			return grid;
		}

		

}
