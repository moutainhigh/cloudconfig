package com.kuangchi.sdd.businessConsole.station.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.station.dao.IStationDao;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.station.service.IStationService;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

import org.springframework.stereotype.Service;

@Service("stationServiceImpl")
public class StationServiceImpl extends BaseServiceSupport implements IStationService {

	@Resource(name="stationDaoImpl")
	private IStationDao stationDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	
	@Override
	public Grid<Station> getStations(Station pageStation) {
		Grid<Station> grid = new Grid<Station>();
		grid.setRows(stationDao.getStations(pageStation));
		grid.setTotal(stationDao.countStations(pageStation));
		return grid;
	}
	@Override
	public void addStation(Station station,String login_user) {
		
		station.setUUID(UUIDUtil.uuidStr());
		Map<String, String> log = new HashMap<String, String>();
		stationDao.insertStation(station);
		log.put("V_OP_NAME", "部门管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "新增部门岗位");
		log.put("V_OP_TYPE", "业务");
		logDao.addLog(log);
		
	}

    @Override
    public JsonResult validStation(String gwDm) {

        JsonResult result = new JsonResult();
        result.setSuccess(true);

        int gwDmNum = stationDao.validStation(gwDm);

        if (gwDmNum > 0){
            result.setSuccess(false);
            result.setMsg("岗位代码已存在");
        }
        return result;
    }

    @Override
    public Station stationDetails(String gwDm) {
        return stationDao.stationDetails(gwDm);
    }

    @Override
    public void modifyStation(Station station,String login_user) {   
    	/*if("0".equals(station.getDefaultGw())){
    		String gw_dm=getStationDM(station.getBmDm());
    		Station newStation = stationDao.stationDetails(gw_dm);
    		newStation.setDefaultGw("1");
    		stationDao.modifyStation(newStation);
    	}*/
    	boolean result = stationDao.modifyStation(station);
    	Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "岗位管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "修改岗位信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
    }

    @Override
    public void deleteStationS(String gwdms,String login_user) {
        //删除岗位关联用户(t_xt_yh_gw)
//        stationDao.deleGwYh(gwdms);
        //删除岗位
        
    	boolean result = stationDao.deleGw(gwdms);
    	Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "岗位管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "删除岗位信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
    }

    @Override
    public int getEmployeeCountByStation(String gwDm) {
        return stationDao.getEmployeeCountByStation(gwDm);
    }
	@Override
	public Station stationDetail(String gwDm) {
		return stationDao.stationDetail(gwDm);
	}
	 /**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-6 上午9:43:12
	 * @功能描述: 查询默认岗位UUID
	 */
	public String getStationDM() {
		return stationDao.getStationDM();
	}
	/**
	 * @创建人　: 郭楚丹
	 * @创建时间: 2016-6-24 下午
	 * @功能描述: 根据部门代码查找默认岗位
	 */
	@Override
	public String getStationDM(String deptNum) {
		return stationDao.getStationDM(deptNum);
	}
	@Override
	public void deleteAgricultureData() {
	     stationDao.deleteAgricultureData();
		
	}
	@Override
	public List<Station> getGwByDeptNumAndName(String gwMc, String bmDm) {
		 
		return stationDao.getGwByDeptNumAndName(gwMc, bmDm);
	}
	
	@Override
	public List<Station> getStationByDept(String bm_dm){
		return stationDao.getStationByDept(bm_dm);
	}
	
	
}
