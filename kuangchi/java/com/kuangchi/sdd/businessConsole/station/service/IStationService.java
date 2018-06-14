package com.kuangchi.sdd.businessConsole.station.service;

import java.util.List;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.station.model.Station;

public interface IStationService {

	/**
	 * 查询岗位
	 * @param station
	 * @return
	 */
	Grid<Station> getStations(Station station);

	/**
	 * 新增岗位
	 * @param station
	 */
	void addStation(Station station,String login_user);

    /**
     * 验证岗位代码是否存在
     * @param gwDm
     * @return
     */
    JsonResult validStation(String gwDm);

    /**
     * 岗位详情
     * @param gwDm
     * @return
     */
    Station stationDetails(String gwDm);
    
    /**
     * 
     * @创建人　: 邓积辉
     * @创建时间: 2016-5-17 上午9:26:45
     * @功能描述:查看岗位 
     * @参数描述:
     */
    Station stationDetail(String gwDm);
    /**
     * 修改岗位
     * @param station
     */
    void modifyStation(Station station,String login_user);

    /**
     * 删除岗位
     * @param gwdms
     */
    void deleteStationS(String gwdms,String login_user);
    
    /**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-6 上午9:43:12
	 * @功能描述: 查询默认岗位UUID
	 */
	String getStationDM();

    public int getEmployeeCountByStation(String gwDm);
    /**
	 * @创建人　: 郭楚丹
	 * @创建时间: 2016-6-24 下午
	 * @功能描述: 根据部门代码查找默认岗位
	 */
    public String getStationDM(String deptNum);
    
    public void deleteAgricultureData();
    
    public List<Station> getGwByDeptNumAndName(String gwMc, String bmDm) ;
    
    /**
     * 获取部门下的所有岗位
     * @author minting.he
     * @param bmDm
     * @return
     */
    public List<Station> getStationByDept(String bm_dm);
}
