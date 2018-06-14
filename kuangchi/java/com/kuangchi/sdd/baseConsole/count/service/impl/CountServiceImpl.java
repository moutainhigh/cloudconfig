package com.kuangchi.sdd.baseConsole.count.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.count.dao.ICountDao;
import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.model.CardInfo;
import com.kuangchi.sdd.baseConsole.count.model.CountCardAuthority;
import com.kuangchi.sdd.baseConsole.count.model.CountWarining;
import com.kuangchi.sdd.baseConsole.count.model.DepartmentInfo;
import com.kuangchi.sdd.baseConsole.count.model.KcardState;
import com.kuangchi.sdd.baseConsole.count.model.ParamState;
import com.kuangchi.sdd.baseConsole.count.service.ICountService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-1下午5:45:40
 * @功能描述:	通过状态名称查询信息Service实现类
 * @参数描述:
 */
@Transactional
@Service("countServiceImpl")
public class CountServiceImpl extends BaseServiceSupport implements ICountService{
	@Resource(name = "countDaoImpl")
	private ICountDao icountDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	
	@Override
	public Grid<CardHistoryModel> getCardHistoryListByParam(Map map) {
		Integer cardHistoryCount=icountDao.getCardHistoryCount(map);
		List<CardHistoryModel> cardHistoryList=icountDao.getCardHistoryByParam(map);
		Grid grid=new Grid();
		grid.setTotal(cardHistoryCount);
		grid.setRows(cardHistoryList);
		return grid;
	}

	@Override
	public List<KcardState> getAllCardStateNames() {
		return icountDao.getAllCardStateNames();
	}

	@Override
	public Grid<CountCardAuthority> getCardAuthorityByName(ParamState staff_name, String Page, String size) {
		Integer coun=icountDao.getCardAuthorityCount(staff_name);
		List<CountCardAuthority> listCountCardAuthority=icountDao.getCardAuthorityByName(staff_name, Page, size);
		Grid grida=new Grid();
		grida.setTotal(coun);
		grida.setRows(listCountCardAuthority);
		return grida;
	}

	@Override
	public List<CountCardAuthority> getCardAuthorityByNameList(ParamState staff_name) {
		return icountDao.getCardAuthorityByNameList(staff_name);
	}
	@Override
	public List<Map> getCardHistoryOperate() {
		return icountDao.getCardHistoryOperate();
	}

	@Override
	public List<CardHistoryModel> getCardHistoryList(Map map) {
		return icountDao.getCardHistoryList(map);
	}

	@Override
	public Grid<Map> getPeopleAuthorityInfoByStaffNumForCount(Map map) {
		Grid<Map> grid=new Grid<Map>();
		grid.setRows(icountDao.getPeopleAuthorityInfoByStaffNumForCount(map));
		grid.setTotal(icountDao.countPeopleAuthorityInfoByStaffNumForCount(map));
		return grid;
	}

	@Override
	public List<Map> getPeopleAuthorityInfoByStaffNumForExcel(Map map) {
		return icountDao.getPeopleAuthorityInfoByStaffNumForExcel(map);
	}

	@Override
	public boolean insertCardHistoryInfo(CardHistoryModel cardHistoryModel,String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			icountDao.insertCardHistoryInfo(cardHistoryModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.put("V_OP_NAME", "卡片日志管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "新增卡片日志信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public Grid<CountWarining> getCountWariningInfo(Map map) {
		Grid<CountWarining> grid = new Grid<CountWarining>();
		List<CountWarining> list =icountDao.getCountWariningInfo(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	icountDao.getCountWariningInfoCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<CountWarining> exportCountWariningInfo(Map map) {
		return icountDao.exportCountWariningInfo(map);
	}
	@Override
	public Grid<DepartmentInfo> getCountDepartmentInfo(Map map) {
		Grid<DepartmentInfo> grid = new Grid<DepartmentInfo>();
		List<DepartmentInfo> list =icountDao.getCountDepartmentInfo(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	icountDao.getCountDepartmentInfoCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	@Override
	public List<DepartmentInfo> exportCountDepartmentInfo(Map map) {
		return icountDao.exportCountDepartmentInfo(map);
	}

	@Override
	public Grid<CardInfo> getCountCardInfo(Map map) {
		Grid<CardInfo> grid = new Grid<CardInfo>();
		List<CardInfo> list =icountDao.getCountCardInfo(map);
		grid.setRows(list);
		if(null!=list){
		Integer count =	icountDao.getCountCardInfoCount(map);
		grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	@Override
	public List<CardInfo> exportCountCardInfo(Map map) {
		return icountDao.exportCountCardInfo(map);
	}

	
}
