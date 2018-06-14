package com.kuangchi.sdd.attendanceConsole.attendCount.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendDeptDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendDeptService;
import com.kuangchi.sdd.base.model.easyui.Grid;

@Service("attendDeptService")
public class AttendDeptServiceImpl implements AttendDeptService {

	@Resource(name="attendDeptDao")
	private AttendDeptDao attendDeptDao;

	@Override
	public Grid<AttendDeptModel> getAllAttendDept(Map map) {
		String month = (String)map.get("month");
		if(null!=month){
			month=month.substring(0, 7);
			map.put("month", month);
		}
		
		List<AttendDeptModel> deptCountList = attendDeptDao.getAllAttendDept(map);
		// 查询各部门的总工时
		for (AttendDeptModel attendDeptModel : deptCountList) {
			Map<String, Object> deptParam = new HashMap<String, Object>();
			String dept_num = attendDeptModel.getDeptNum();
			if(dept_num != null){
				deptParam.put("dept_num", dept_num);
				Double allWorkTime = attendDeptDao.getDeptAllWorkTime(deptParam);
				attendDeptModel.setAllWorkTime(allWorkTime);
				
				attendDeptModel.setAvgWorkTime(attendDeptModel.getWorkDays()==0?0:allWorkTime/attendDeptModel.getWorkDays());
			}
		}
		
		
		
		
		Integer total=attendDeptDao.countAllAttendDept(map);
		Grid<AttendDeptModel> grid=new Grid<AttendDeptModel>();
		grid.setRows(deptCountList);
		grid.setTotal(total);
		return grid;
	}

	@Override
	public List<AttendDeptModel> exportAllToExcel(Map map) {
		String month = (String)map.get("month");
		if(null!=month){
			month=month.substring(0, 7);
			map.put("month", month);
		}
		
		List<AttendDeptModel> exportDeptCountList = attendDeptDao.exportAllDeptToExcel(map);
		// 查询各部门的总工时
		for (AttendDeptModel attendDeptModel : exportDeptCountList) {
			Map<String, Object> deptParam = new HashMap<String, Object>();
			String dept_num = attendDeptModel.getDeptNum();
			if(dept_num != null){
				deptParam.put("dept_num", dept_num);
				Double allWorkTime = attendDeptDao.getDeptAllWorkTime(deptParam);
				attendDeptModel.setAllWorkTime(allWorkTime);
				
				attendDeptModel.setAvgWorkTime(attendDeptModel.getWorkDays()==0?0:allWorkTime/attendDeptModel.getWorkDays());
			}
		}
		
		
		return exportDeptCountList;
	}

}
