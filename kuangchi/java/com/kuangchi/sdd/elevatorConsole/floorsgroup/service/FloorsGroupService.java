package com.kuangchi.sdd.elevatorConsole.floorsgroup.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.floorsgroup.model.FloorsGroupModel;

public interface FloorsGroupService {
	public Boolean insertFloorGroup(FloorsGroupModel floor, String login_user);//添加楼层信息
	//public Boolean insertFloorGroupMap(FloorsGroupModel floor);//添加楼层组映射信息
	public Grid selectAllFloorGroup(FloorsGroupModel floor,String Page, String size);
	public List<FloorsGroupModel> selectByfloorgroupnum(String floorgroupnum);//查询组编号是否存在
	public List<FloorsGroupModel> selectByfloor(String floorgroupnum);//根据编号查询信息
	public Boolean updateFloorgroupname(FloorsGroupModel floor, String login_user);//更新楼层组信息表
	//public Integer deleteFloorgroupmap(String floorgroupnum);//删除楼层映射表
	public Boolean updateDeleteFlag(String floorgroupnum, String login_user);//删除楼层组信息
	public List<FloorsGroupModel> selectByAuthority(String floorgroupnum);//判断授权表是否存在楼层组编号
	public List<FloorsGroupModel> selectByFloorName();//根据楼层组编号查询楼层组名称
	public Grid SelectAllFloorGroups(FloorsGroupModel floor,String Page, String size);//页面显示数据
	public String getfloorgroupnum();
	/**
	 * 查询楼层组信息
	 * @author minting.he
	 * @return
	 */
	public List<FloorsGroupModel> getFloorGroupInfo();
	
	/**
	 * 查询组对应的楼层
	 * @author minting.he
	 * @param floor_group_num
	 * @return
	 */
	public List<String> getFloorByGroup(String floor_group_num);
}
