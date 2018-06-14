package com.kuangchi.sdd.businessConsole.station.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.station.dao.IStationDao;
import com.kuangchi.sdd.businessConsole.station.model.Station;

@Repository("stationDaoImpl")
public class StationDaoImpl extends BaseDaoImpl<Station> implements IStationDao {

	@Override
	public String getNameSpace() {
		
		return "common.Station";
	}

	@Override
	public String getTableName() {
		
		return null;
	}

	@Override
	public List<Station> getStations(Station pageStation) {
		int skip = (pageStation.getPage() - 1)* pageStation.getRows();	
		List<Station> list= getSqlMapClientTemplate().queryForList("getStations", pageStation, skip, pageStation.getRows());
        return list;
	}

	@Override
	public int countStations(Station pageStation) {
		return this.queryCount("countStations", pageStation);
	}

	@Override
	public void insertStation(Station station) {
		this.getSqlMapClientTemplate().insert("insertStation", station);
		
	}

	@Override
	public void deleteUsersSta(String yhDms) {
		getSqlMapClientTemplate().delete("deleteUsersSta", yhDms);
		
	}

    @Override
    public int validStation(String gwDm) {
        return queryCount("validStation", gwDm);
    }

    @Override
    public Station stationDetails(String gwDm) {
        return (Station) getSqlMapClientTemplate().queryForObject("stationDetails",gwDm);
    }

    @Override
    public boolean modifyStation(Station station) {
       return update("modifyStation",station);
    }

    @Override
    public void deleGwYh(String gwdms) {
        getSqlMapClientTemplate().delete("deleGwsYh",gwdms);
    }

    @Override
    public boolean deleGw(String gwdms) {
        return delete("deleGw",gwdms);
    }

    @Override
    public List<Station> getBmStations(String bmDm) {
        return getSqlMapClientTemplate().queryForList("getBmStations",bmDm);
    }

    @Override
    public String[] getUserGws(String yhDm) {
        List<String> gws = getSqlMapClientTemplate().queryForList("getUserGws",yhDm);
        return gws.toArray(new String[]{});
    }

    @Override
    public void addUserStations(String yhDm, String[] gwds, String lrryDm) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("yhDm",yhDm);
        params.put("gwds", Arrays.asList(gwds));
        params.put("lrryDm",lrryDm);

        getSqlMapClientTemplate().insert("addUserStations",params);

    }
    public int getEmployeeCountByStation(String gwDm){
        return (Integer)getSqlMapClientTemplate().queryForObject("getEmployeeCountByStation",gwDm);
    }

	@Override
	public Station stationDetail(String gwDm) {
		return (Station) getSqlMapClientTemplate().queryForObject("stationDetail", gwDm);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-6 上午9:43:12
	 * @功能描述: 查询默认岗位UUID
	 */
	public String getStationDM() {
		return (String) find("getStationDM",null);
	}
	/**
	 * @创建人　: 郭楚丹
	 * @创建时间: 2016-6-24 下午
	 * @功能描述: 根据部门代码查找默认岗位
	 */
	@Override
	public String getStationDM(String deptNum) {
		return (String) getSqlMapClientTemplate().queryForObject("getStationDMFromDept",deptNum);
	}

	@Override
	public void deleteAgricultureData() {
		Map< String, String> map=new HashMap<String, String>();
		 getSqlMapClientTemplate().delete("deleteAgricultureData",map);
		
	}

	@Override
	public List<Station> getGwByDeptNumAndName(String gwMc, String bmDm) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("gwMc", gwMc);
		 map.put("bmDm", bmDm);
		return  getSqlMapClientTemplate().queryForList("getGwByDeptNumAndName",map);
	}

	@Override
	public List<Station> getStationByDept(String bm_dm){
		return  getSqlMapClientTemplate().queryForList("getStationByDept", bm_dm);
	}
	
}
