package com.kuangchi.sdd.elevatorConsole.floorsgroup.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.dao.FloorsGroupDao;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.model.FloorsGroupModel;

@Repository("FloorsGroupDaoImpl")
public class FloorsGroupDaoImpl extends BaseDaoImpl<FloorsGroupModel> implements FloorsGroupDao {
	
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

	//添加楼层信息
	@Override
	public Boolean insertFloorGroup(FloorsGroupModel floor) {
		Object obj=this.getSqlMapClientTemplate().insert("insertFloorGroup", floor);
		if(obj==null){
			return true;
		}
		return false;
	}
	//添加楼层组映射信息
	@Override
	public Boolean insertFloorGroupMap(FloorsGroupModel floor) {
		Object obj=this.getSqlMapClientTemplate().insert("insertFloorGroupMap", floor);
		if(obj==null){
			return true;
		}
		return false;
	}
	//查询分组后的楼层与楼层组的信息
	@Override
	public List<FloorsGroupModel> selectAllFloorGroup(FloorsGroupModel floor,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("floor_group_num", floor.getFloor_group_num());
		return this.getSqlMapClientTemplate().queryForList("selectAllFloorGroup", mapState);
	}
	
	public Integer selectAllFloorGroupCount(FloorsGroupModel floor){
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("floor_group_num", floor.getFloor_group_num());
		return queryCount("selectAllFloorGroupCount",mapState);
	}
	//查询所有组编号
	@Override
	public List<FloorsGroupModel> selectfloorgroupnum(String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList("selectfloorgroupnum", mapState);
	}
	//查询组编号是否存在
	@Override
	public List<FloorsGroupModel> selectByfloorgroupnum(String floorgroupnum) {
		return this.getSqlMapClientTemplate().queryForList("selectByfloorgroupnum", floorgroupnum);
	}
	//根据编号查询信息
	@Override
	public List<FloorsGroupModel> selectByfloor(String floorgroupnum) {
		return this.getSqlMapClientTemplate().queryForList("selectByfloor", floorgroupnum);
	}
	//更新楼层组信息表
	@Override
	public Boolean updateFloorgroupname(FloorsGroupModel floor) {
		Object obj=this.getSqlMapClientTemplate().update("updateFloorgroupname", floor);
		if(obj==null){
			return false;
		}
		return true;
	}
	//删除楼层映射表
	@Override
	public Integer deleteFloorgroupmap(String floorgroupnum) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().delete("deleteFloorgroupmap", floorgroupnum);
	/*	if(obj!=0){
			return 1;
		}
		return 0;*/
		return obj;
	}
	//删除楼层组信息
	@Override
	public Boolean updateDeleteFlag(String floorgroupnum) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().delete("updateDeleteFlag", floorgroupnum);
		if(obj==0){
			return false;
		}
		return true;
		}
	//判断授权表是否存在楼层组编号
	@Override
	public List<FloorsGroupModel> selectByAuthority(String floorgroupnum) {
		return this.getSqlMapClientTemplate().queryForList("selectByAuthority", floorgroupnum);
	}
	//根据楼层组编号查询楼层组名称
	@Override
	public List<FloorsGroupModel> selectByFloorName() {
		return this.getSqlMapClientTemplate().queryForList("selectByFloorName", "");
	}
	//页面显示数据
	@Override
	public List<FloorsGroupModel> SelectAllFloorGroups(FloorsGroupModel floor,
			String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("floor_group_name", floor.getFloor_group_name());
		return this.getSqlMapClientTemplate().queryForList("SelectAllFloorGroups", mapState);
	}

	@Override
	public Integer SelectAllFloorGroupsCount(FloorsGroupModel floor) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("floor_group_name", floor.getFloor_group_name());
		return queryCount("SelectAllFloorGroupsCount",mapState);
	}
	
	@Override
	public List<FloorsGroupModel> getFloorGroupInfo(){
		return this.getSqlMapClientTemplate().queryForList("getFloorGroupInfo");
	}
	
	@Override
	public List<String> getFloorByGroup(String floor_group_num){
		return this.getSqlMapClientTemplate().queryForList("getFloorByGroup", floor_group_num);
	}

	@Override
	public String getfloorgroupnum() {
		return (String)this.getSqlMapClientTemplate().queryForObject("getfloorgroupnum", "");
	}
	
}
