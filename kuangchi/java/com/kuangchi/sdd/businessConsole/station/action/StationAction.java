package com.kuangchi.sdd.businessConsole.station.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.station.service.IStationService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.ServletUtil;

@Controller("stationAction")
public class StationAction extends BaseActionSupport {

	private static final long serialVersionUID = 7215329322288461578L;

	private static final Logger LOG = Logger.getLogger(StationAction.class);
	private Station model;

	public StationAction() {
		model = new Station();
	}

	@Override
	public Station getModel() {
		return model;
	}

	@Resource(name = "stationServiceImpl")
	private IStationService stationService;

	/**
	 * 岗位管理
	 */
	public void manangerStation() {
		HttpServletRequest request = getHttpServletRequest();
		Station page = new Station();
		String parentGroupNum = request.getParameter("parentGroupNum");

		BeanUtils.copyProperties(model, page);
		page.setBmDm(parentGroupNum);
		restModel();

		Grid<Station> grid = stationService.getStations(page);
		printHttpServletResponse(GsonUtil.toJson(grid));

	}

	/**
	 * 按部门获得岗位
	 */
	public void getStationByDepartment() {
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		String bmDm = request.getParameter("bmDm");
		Station page = new Station();
		page.setBmDm("'" + bmDm + "'");
		page.setPage(0);
		page.setRows(200);
		Grid<Station> grid = stationService.getStations(page);
		List<Station> stationList = grid.getRows();
		for (Station station : stationList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", station.getGwDm());
			map.put("TEXT", station.getGwMc());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 新增岗位
	 */
	public void addStation() {
		Station page = new Station();

		BeanUtils.copyProperties(model, page);
		page.setDefaultGw("1"); // 只能添加非默认岗位
		restModel();

		// 设置录入人员
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();

		// page.setGwDm(page.getGwDm().trim());//去掉前后空格
		UUID uuid = UUID.randomUUID();// 自动生成刚伪代码（2016.7.25xuewen.deng）
		page.setGwDm(uuid.toString());

		page.setGwMc(page.getGwMc().trim());// 去掉前后空格
		page.setGwMcJ(page.getGwMcJ() == null ? "" : page.getGwMcJ().trim());// 去掉前后空格
		page.setLrryDm(loginUser.getYhDm().trim());
		page.setLrSj(getSysTimestamp());

		JsonResult result = new JsonResult();

		try {
			stationService.addStation(page, login_user);
			result.setMsg("新增岗位成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("add station exception", e);

		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 修改岗位
	 */
	public void modifyStation() {
		Station page = new Station();
		BeanUtils.copyProperties(model, page);
		/*
		 //新增的岗位不能作为默认岗位，直接配置文件赋值'1'
		 if (page.getDefaultGw() == null)
			page.setDefaultGw("0");*/ // 页面带过来，否为1，是为null
		restModel();

		page.setGwDm(page.getGwDm().trim());// 去掉前后空格
		page.setGwMc(page.getGwMc().trim());// 去掉前后空格
		page.setGwMcJ(page.getGwMcJ() == null ? "" : page.getGwMcJ().trim());// 去掉前后空格
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		try {
			Station station = stationService.stationDetails(page.getGwDm());
			Integer employeeCount = stationService
					.getEmployeeCountByStation("'" + page.getGwDm() + "'");
			if (employeeCount > 0 && !station.getBmDm().equals(page.getBmDm())) {
				result.setSuccess(false);
				result.setMsg("请先确保该岗位上无员工后才能更改该岗位的部门");
			} else {
				stationService.modifyStation(page, login_user);
				result.setSuccess(true);
				result.setMsg("修改成功");
			}

		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("add station exception", e);

		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 验证岗位
	 */
	public void validStation() {
		String gwDm = getHttpServletRequest().getParameter("gwDm");

		printHttpServletResponse(GsonUtil.toJson(stationService
				.validStation(gwDm)));
	}

	/**
	 * 岗位详情
	 */
	public void stationDetails() {
		String gwDm = getHttpServletRequest().getParameter("gwDm");
		printHttpServletResponse(GsonUtil.toJson(stationService
				.stationDetails(gwDm)));

	}

	/**
	 * 查看岗位
	 */
	public void stationDetail() {
		String gwDm = getHttpServletRequest().getParameter("gwDm");
		printHttpServletResponse(GsonUtil.toJson(stationService
				.stationDetail(gwDm)));

	}

	/**
	 * 删除岗位
	 */
	public void deleteStation() {

		String gwdms = getHttpServletRequest().getParameter("gwdms");
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		try {
			Integer count = stationService.getEmployeeCountByStation(gwdms);
			if (count > 0) {
				result.setMsg("删除的岗位仍然有员工，请确保该岗位无员工后再删除");
				result.setSuccess(false);
			} else {
				stationService.deleteStationS(gwdms, login_user);
				result.setMsg("删除成功");
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("dele stations exception", e);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	private void restModel() {
		this.model = new Station();
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-6 上午9:43:12
	 * @功能描述: 查询默认岗位DM
	 */
	/*
	 * public void getStationDM(){ JsonResult result = new JsonResult(); String
	 * gwDM = stationService.getStationDM(); if(gwDM==null){
	 * result.setSuccess(false); }else{ result.setMsg(gwDM);
	 * result.setSuccess(true); }
	 * printHttpServletResponse(GsonUtil.toJson(result)); }
	 */

	/**
	 * 根据部门代码查找部门默认岗位代码
	 * 
	 * @param deptNum
	 */
	public void getStationDMByNum() {
		String bmDm = getHttpServletRequest().getParameter("bmDm");
		JsonResult result = new JsonResult();
		String gwDM = stationService.getStationDM(bmDm);
		if (gwDM == null) {
			result.setSuccess(false);
		} else {
			result.setMsg(gwDM);
			result.setSuccess(true);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 获取部门下的所有岗位
	 * @author minting.he
	 */
	public void getStationByDept(){
		HttpServletRequest request = getHttpServletRequest();
		String bm_dm = request.getParameter("bm_dm");
		List<Station> list = stationService.getStationByDept(bm_dm);
		printHttpServletResponse(GsonUtil.toJson(list));
	}

}
