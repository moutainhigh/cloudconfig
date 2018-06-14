package com.kuangchi.sdd.businessConsole.station.dao;

import java.util.List;

import com.kuangchi.sdd.businessConsole.station.model.Station;

public interface IStationDao {

	List<Station> getStations(Station pageStation);

	int countStations(Station pageStation);

	void insertStation(Station station);

	void deleteUsersSta(String yhDms);

    int validStation(String gwDm);

    Station stationDetails(String gwDm);

    boolean modifyStation(Station station);

    void deleGwYh(String gwdms);

    boolean deleGw(String gwdms);

    /**
     * 部门下所有岗位
     * @param bmDm
     * @return
     */
    List<Station> getBmStations(String bmDm);

    String[] getUserGws(String yhDm);

    void addUserStations(String yhDm, String[] gwds, String lrryDm);

    public int getEmployeeCountByStation(String gwDm);

	Station stationDetail(String gwDm);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-6 上午9:43:12
	 * @功能描述: 查询默认岗位UUID
	 */
	String getStationDM();
	/**
	 * @创建人　: 郭楚丹
	 * @创建时间: 2016-6-24 下午
	 * @功能描述: 根据部门代码查找默认岗位
	 */
    public String getStationDM(String deptNum);
    
    public void deleteAgricultureData();
    
    public List<Station>   getGwByDeptNumAndName(String gwDm,String bmDm);
    
    /**
     * 获取部门下的所有岗位
     * @author minting.he
     * @param pageStation
     * @return
     */
    public List<Station> getStationByDept(String bm_dm);
}
