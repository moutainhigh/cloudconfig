package com.kuangchi.sdd.attendanceConsole.synchronizeData.action;

import groovy.transform.Synchronized;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.CheckDataService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.OrgDataService;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.SynService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("synDataAction")
public class SynDataAction extends BaseActionSupport {

	static Semaphore semaphore = new Semaphore(1);
	@Resource(name = "checkDataService")
	CheckDataService checkDataService;
	@Resource(name = "orgDataService")
	OrgDataService orgDataService;
	@Resource(name = "synService")
	SynService synService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getSynCheckData() {

	}

	public void getSqlServerOrgUserAccountData() {
		List<SqlServerOrgUserAccountData> list = orgDataService
				.getSqlServerOrgUserAccountData("");
	}

	public void synSqlServerOrgDepartmentData() {
		synService.synSqlServerOrgDepartmentData();
	};

	public void synSqlServerOrgUserAccountData() {
		synService.synSqlServerOrgUserAccountData();
	};

	public void synSqlServerCheckData() {
		String startDate = "2015-01-01";
		String endDate = "2017-01-01";
		synService.synSqlServerCheckData(startDate, endDate);
	};

	public void synSqlServerOutData() {
		String startDate = "2015-01-01";
		String endDate = "2017-01-01";
		synService.synSqlServerOutData(startDate, endDate);
	};

	public void synSqlServerBrushCardLog() {
		String startDate = "2015-01-01";
		String endDate = "2017-01-01";
		synService.synSqlServerBrushCardLog(startDate, endDate);
	};

	public void onkeySyn() {

		boolean getlock=false;
		try {
			getlock = semaphore.tryAcquire(3,TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			 
			e1.printStackTrace();
		}

		HttpServletRequest request = getHttpServletRequest();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String flag = request.getParameter("flag");
		JsonResult result = new JsonResult();

		try {

			if (getlock) {

				if ("1".equals(flag)) { // 同步部门员工
					synService.synSqlServerOrgDepartmentData();
					synService.synSqlServerOrgUserAccountData();

				} else if ("2".equals(flag)) { // 同步请假外出
					synService.synSqlServerCheckData(startDate, endDate);
					synService.synSqlServerOutData(startDate, endDate);
				} else if ("3".equals(flag)) {// 同步刷卡记录
					synService.synSqlServerBrushCardLog(startDate, endDate);

				} else {
					synService.synSqlServerOrgDepartmentData();
					synService.synSqlServerOrgUserAccountData();
					synService.synSqlServerCheckData(startDate, endDate);
					synService.synSqlServerOutData(startDate, endDate);
					synService.synSqlServerBrushCardLog(startDate, endDate);
				}

				result.setSuccess(true);
				result.setMsg("同步成功");
			}else{
				result.setSuccess(false);
				result.setMsg("服务器正忙，请稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("同步失败");
		} finally {
			semaphore.release();
		}

		printHttpServletResponse(GsonUtil.toJson(result));

	}

}
