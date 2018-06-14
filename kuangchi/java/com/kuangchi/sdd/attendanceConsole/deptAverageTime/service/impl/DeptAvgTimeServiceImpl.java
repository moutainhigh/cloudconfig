package com.kuangchi.sdd.attendanceConsole.deptAverageTime.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kuangchi.sdd.attendanceConsole.deptAverageTime.dao.IDeptAvgTimeDao;
import com.kuangchi.sdd.attendanceConsole.deptAverageTime.model.DeptAvgTime;
import com.kuangchi.sdd.attendanceConsole.deptAverageTime.service.IDeptAvgTimeService;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;



@Component("deptAvgTimeServiceImpl")
public class DeptAvgTimeServiceImpl implements IDeptAvgTimeService{
	 private static final Logger LOG = Logger.getLogger(DeptAvgTimeServiceImpl.class);
	
		@Resource(name = "deptAvgTimeDaoImpl")
	    private IDeptAvgTimeDao deptAvgTimeDao;

		@Resource(name = "departmentDaoImpl")
	    private IDepartmentDao departmentDao;
		
		
		//统计部门日均信息
		@Override
		public Grid<DeptAvgTime> getSearchStatistics(DeptAvgTime deptAvgTime, int page, int rows) {
					
			String[] allDeptNum = deptAvgTime.getDept_num().split(",");
			String[] newDeptNum;
			if(allDeptNum.length > page*rows+1){
				newDeptNum = Arrays.copyOfRange(allDeptNum, (page-1)*rows, page*rows+1); //每次只查询十条数据，加快查询速度
			} else {
				newDeptNum = Arrays.copyOfRange(allDeptNum, (page-1)*rows, allDeptNum.length); //每次只查询十条数据，加快查询速度
			}
			List<DeptAvgTime> deptAvgList = getDeptAvg(newDeptNum, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
			grid.setRows(deptAvgList);
			grid.setTotal(allDeptNum.length);
			
			return grid;
		}
		
		//导出部门日均信息
		@Override
		public List<DeptAvgTime> exportSearchStatistics(DeptAvgTime deptAvgTime){
			
			String[] allDeptNum = deptAvgTime.getDept_num().split(",");
			List<DeptAvgTime> deptAvgList = getDeptAvg(allDeptNum, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			return deptAvgList;
		}
		
		
		
		//统计员工日均信息
		@Override
		public Grid<DeptAvgTime> getStaffTimeStatistics(DeptAvgTime deptAvgTime, int page, int rows) {
			
			//注意传过来的所有部门编号代码是没有加''
			String strs = new String();
			if(deptAvgTime.getDept_num()!=null){
				String[] str=deptAvgTime.getDept_num().split(",");
				for (int i = 0; i < str.length; i++) {
					strs+="'"+str[i]+"',";
				}
				strs=strs.substring(0,strs.length()-1);
				
			}
			
			Map<String, Object> staffAvgParam = new HashMap<String, Object>();
			staffAvgParam.put("dept_nums", strs);
			staffAvgParam.put("staff_no", deptAvgTime.getStaff_no());
			staffAvgParam.put("staff_name", deptAvgTime.getStaff_name());
			staffAvgParam.put("page", (page-1)*rows);
			staffAvgParam.put("rows", rows);
			List<DeptAvgTime> staffAvg = getStaffAvg(staffAvgParam, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			
			staffAvgParam.put("from_time", deptAvgTime.getFrom_Time());
			staffAvgParam.put("to_time", deptAvgTime.getTo_Time());
			int staffAvgCount = deptAvgTimeDao.getStaffAvgCount(staffAvgParam);
			
			Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
			grid.setRows(staffAvg);
			grid.setTotal(staffAvgCount);
			
			return grid;
			
		}

		//导出员工日均信息
		@Override
		public List<DeptAvgTime> exportStaffTimeStatistics(DeptAvgTime deptAvgTime) {
			
			//注意传过来的所有部门编号代码是没有加''
			String strs = new String();
			if(deptAvgTime.getDept_num()!=null){
				String[] str=deptAvgTime.getDept_num().split(",");
				for (int i = 0; i < str.length; i++) {
					strs+="'"+str[i]+"',";
				}
				strs=strs.substring(0,strs.length()-1);
				
			}
			
			Map<String, Object> staffAvgParam = new HashMap<String, Object>();
			staffAvgParam.put("dept_nums", strs);
			staffAvgParam.put("staff_no", deptAvgTime.getStaff_no());
			staffAvgParam.put("staff_name", deptAvgTime.getStaff_name());
			List<DeptAvgTime> staffAvg = getStaffAvg(staffAvgParam, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			return staffAvg;
			
		}

		
		
		//所有一级部门日均时间统计
		@Override
		public Grid<DeptAvgTime> getAllAvgTimeStatistics(DeptAvgTime deptAvgTime,int page, int rows) {
			// 若部门编号为空，则查询所有一级部门
			if(deptAvgTime.getDept_num() == null) {
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			String[] allDeptNum = deptAvgTime.getDept_num().split(",");
			String[] newDeptNum;
			if(allDeptNum.length > page*rows+1){
				newDeptNum = Arrays.copyOfRange(allDeptNum, (page-1)*rows, page*rows+1); //每次只查询十条数据，加快查询速度
			} else {
				newDeptNum = Arrays.copyOfRange(allDeptNum, (page-1)*rows, allDeptNum.length); //每次只查询十条数据，加快查询速度
			}
			List<DeptAvgTime> deptAvgList = getDeptAvg(newDeptNum, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
			grid.setRows(deptAvgList);
			grid.setTotal(allDeptNum.length);
			
			return grid;
		}
		
		//所有一级部门日均时间导出
		@Override
		public List<DeptAvgTime> exportAllAvgTimeStatistics(DeptAvgTime deptAvgTime) {
			// 若部门编号为空，则查询所有一级部门
			if(deptAvgTime.getDept_num() == null) {
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			String[] allDeptNum = deptAvgTime.getDept_num().split(",");
			List<DeptAvgTime> deptAvgList = getDeptAvg(allDeptNum, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			return deptAvgList;
		}
		
		
		
		//所有二级部门日均时间统计
		@Override
		public Grid<DeptAvgTime> getAllTwoDeptAvgTime(DeptAvgTime deptAvgTime,int page, int rows) {
			
			if(deptAvgTime.getDept_num()==null){
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			String[] strs = deptAvgTime.getDept_num().split(",");
			
			// 查询所选部门下的二级部门
			String childDeptStr = "";
			for (int i = 0; i < strs.length; i++) {
				List<DeptAvgTime> bmdms = deptAvgTimeDao.getBmdmBySjbmDm(strs[i], deptAvgTime.getLayerDeptNum() );
				for (DeptAvgTime deptAvg : bmdms) {
					childDeptStr += deptAvg.getBm_dm() +",";
				}
			}
			
			String[] childDeptNum = childDeptStr.split(",");
			String[] newDeptNum;
			if(childDeptNum.length > page*rows+1){
				newDeptNum = Arrays.copyOfRange(childDeptNum, (page-1)*rows, page*rows+1); //每次只查询十条数据，加快查询速度
			} else {
				newDeptNum = Arrays.copyOfRange(childDeptNum, (page-1)*rows, childDeptNum.length); //每次只查询十条数据，加快查询速度
			}
			List<DeptAvgTime> deptAvgList = getDeptAvg(newDeptNum, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
			grid.setRows(deptAvgList);
			grid.setTotal(childDeptNum.length);
			
			return grid;
		}
		
		//所有二级部门日均时间导出
		@Override
		public List<DeptAvgTime> exportAllTwoDeptAvgTime(DeptAvgTime deptAvgTime) {
			
			if(deptAvgTime.getDept_num()==null){
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			String[] strs = deptAvgTime.getDept_num().split(",");
			
			// 查询所选部门下的二级部门
			String childDeptNum = "";
			for (int i = 0; i < strs.length; i++) {
				List<DeptAvgTime> bmdms = deptAvgTimeDao.getBmdmBySjbmDm(strs[i], deptAvgTime.getLayerDeptNum() );
				for (DeptAvgTime deptAvg : bmdms) {
					childDeptNum += deptAvg.getBm_dm() +",";
				}
			}
			
			List<DeptAvgTime> deptAvgList = getDeptAvg(childDeptNum.split(","), deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			return deptAvgList;
		}
		
		//所有员工日均统计、和导出
		@Override
		public Grid<DeptAvgTime> getAllStaffAvgTime(DeptAvgTime deptAvgTime,int page, int rows) {
			
			Map<String, Object> staffAvgParam = new HashMap<String, Object>();
			staffAvgParam.put("dept_nums", deptAvgTime.getDept_num());
			staffAvgParam.put("staff_no", deptAvgTime.getStaff_no());
			staffAvgParam.put("staff_name", deptAvgTime.getStaff_name());
			staffAvgParam.put("page", (page-1)*rows);
			staffAvgParam.put("rows", rows);
			List<DeptAvgTime> staffAvg = getStaffAvg(staffAvgParam, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			staffAvgParam.put("from_time", deptAvgTime.getFrom_Time());
			staffAvgParam.put("to_time", deptAvgTime.getTo_Time());
			int staffAvgCount = deptAvgTimeDao.getStaffAvgCount(staffAvgParam);
			
			Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
			grid.setRows(staffAvg);
			grid.setTotal(staffAvgCount);
			
			return grid;
		}
		
		//所有员工日均统计、和导出
		@Override
		public List<DeptAvgTime> exportAllStaffAvgTime(DeptAvgTime deptAvgTime) {
			
			Map<String, Object> staffAvgParam = new HashMap<String, Object>();
			staffAvgParam.put("dept_nums", deptAvgTime.getDept_num());
			staffAvgParam.put("staff_no", deptAvgTime.getStaff_no());
			staffAvgParam.put("staff_name", deptAvgTime.getStaff_name());
			List<DeptAvgTime> staffAvg = getStaffAvg(staffAvgParam, deptAvgTime.getFrom_Time(), deptAvgTime.getTo_Time());
			
			return staffAvg;
		}

		/**
		 * 根据部门查询部门日均
		 * @author yuman.gao
		 */
		public List<DeptAvgTime> getDeptAvg(String[] deptNums, String from_time, String to_time){
			List<DeptAvgTime> deptAvg=new ArrayList<DeptAvgTime>();
			
			for (int i = 0; i < deptNums.length; i++) {
				
				// 不统计根部门
				if(deptNums[i].equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)){
					continue;
				}

				// 查询该部门的子部门
				String childDept = "";
				List<Department> allDepartment = departmentDao.getAllDepart();
				childDept += getAllChildDeptByParent(deptNums[i],allDepartment);
				childDept += "'"+deptNums[i]+"'";

				// 查询部门总工时
				Map<String, Object> deptAvgParam = new HashMap<String, Object>();
				deptAvgParam.put("from_time", from_time);
				deptAvgParam.put("to_time", to_time);
				deptAvgParam.put("dept_nums", childDept);
				Map deptAvgList = deptAvgTimeDao.getDeptAvgInfo(deptAvgParam);
				BigDecimal sumTime = (BigDecimal)deptAvgList.get("sumTime");
				
				// 查询部门总人数
				int staffCount = deptAvgTimeDao.getStaffCountByDept(childDept);
				String deptName = deptAvgTimeDao.getDeptName(deptNums[i]);
				int workDay = (deptAvgTimeDao.getDateWorkTime(childDept,from_time,to_time)).size();
				
				DeptAvgTime avg=new DeptAvgTime();
				avg.setAvgTime(sumTime == null ? 0.0 : sumTime.doubleValue()/(staffCount * workDay));
				avg.setDept_name(deptName);
				avg.setFrom_Time(from_time);
				avg.setTo_Time(to_time);
				deptAvg.add(avg);
			}
			
			
			return deptAvg;
		}
		
		/**
		 * 查询员工日均
		 * @param param: 包含staff_no, staff_name, dept_num
		 * @author yuman.gao
		 */
		public List<DeptAvgTime> getStaffAvg(Map<String, Object> param, String from_time, String to_time){
			
			List<DeptAvgTime> deptAvg=new ArrayList<DeptAvgTime>();
			
			//查询员工日均
			param.put("from_time", from_time);
			param.put("to_time", to_time);
			List<Map> staffAvgList = deptAvgTimeDao.getStaffAvgInfo(param);
			
			for (Map staffAvgMap : staffAvgList) {
				int workDayCount = deptAvgTimeDao.getStaffDateWorkTime((String)staffAvgMap.get("staff_num"),from_time,to_time).size();
				DeptAvgTime staffAvg = new DeptAvgTime();
				staffAvg.setDept_name((String)staffAvgMap.get("dept_name"));
				staffAvg.setStaff_no((String)staffAvgMap.get("staff_no"));
				staffAvg.setStaff_name((String)staffAvgMap.get("staff_name"));
				staffAvg.setFrom_Time(from_time);
				staffAvg.setTo_Time(to_time);
				staffAvg.setStaffSumTime(staffAvgMap.get("sumTime") == null ? 0.0 :((BigDecimal)staffAvgMap.get("sumTime")).doubleValue());//总工时
				staffAvg.setAvgTime(staffAvgMap.get("sumTime") == null || workDayCount == 0? 0.0 : staffAvg.getStaffSumTime()/workDayCount);//员工日均时间 
				deptAvg.add(staffAvg);  
			}
			 
			return deptAvg;
		}
		
		@Override
		public List<DeptAvgTime> getBmdmBySjbmDm(String sjbm_dm) {
			return deptAvgTimeDao.getBmdmBySjbmDm(sjbm_dm, null);
		}

		
		/**
		 * 查询该父部门下所有子部门
		 * @author yuman.gao
		 */
		public String getAllChildDeptByParent(String deptNum,List<Department> allDepartments) {
			StringBuffer deptNums = new StringBuffer();
			
			for (Department department:allDepartments) {
				if (department.getSjbmDm().equals(deptNum)) {
					deptNums.append("'" + department.getBmDm() +"',");
					deptNums.append(getAllChildDeptByParent(department.getBmDm(),allDepartments));
				}
			}
			
			return deptNums.toString();
		}
		
		
		
		
		
		//统计部门日均信息、和导出
		/*@Override
		public List<DeptAvgTime> getSearchStatistics(DeptAvgTime deptAvgTime) {
			List<DeptAvgTime> deptAvg = new ArrayList<DeptAvgTime>();
			
			String[] allDeptNum = deptAvgTime.getDept_num().split(",");
			for (int i = 0; i < allDeptNum.length; i++) {
				
				// 不统计根部门
				if(allDeptNum[i].equals(GlobalConstant.DEPARTMENT_ROOT_BM_DM)){
					continue;
				}

				// 查询该部门的子部门
				String childDept = "";
				List<Department> allDepartment = departmentDao.getAllDepart();
				childDept += getAllChildDeptByParent(allDeptNum[i],allDepartment);
				childDept += "'"+allDeptNum[i]+"'";

				Double sumtime=0.0;
				
				//查询员工信息(包含员工人数)
				 List<DeptAvgTime> staff = deptAvgTimeDao.getStaffAndDeptInfo(childDept,deptAvgTime.getStaff_no(),
						 deptAvgTime.getStaff_name(), deptAvgTime.getLayerDeptNum());
				 if(staff != null && staff.size()!=0){
					 for (DeptAvgTime deptAvgTime2 : staff) {
						 //查询员工除节假日、公休日外总的工作时间
						 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime2.getStaff_num(),
								 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
						 if(deptSumTime != null && deptSumTime.size()!=0){
							 for (DeptAvgTime dept : deptSumTime) {
								 sumtime=sumtime+dept.getSumTime();
							 }
						 }
					 }
					 sumtime = sumtime/staff.size();//总工时除以人数（多少个员工）	 
				 }
				 String dept_name= deptAvgTimeDao.getDeptNames(allDeptNum[i]);//根据部门代码查询部门名称
				 
				 DeptAvgTime avg=new DeptAvgTime();
				 //查询员工除节假日、公休日外(主要用户取出正常上班多少天)
				 List<DeptAvgTime> list= deptAvgTimeDao.getDateWorkTime(childDept,
						 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
				 if(list.size()!=0){
					 avg.setAvgTime(sumtime/list.size());
				 }else{
					 avg.setAvgTime(0.0);
				 }
				 avg.setDept_name(dept_name);
				 avg.setFrom_Time(deptAvgTime.getFrom_Time());
				 avg.setTo_Time(deptAvgTime.getTo_Time());
				 deptAvg.add(avg);
			}
			return deptAvg;
		}*/
		
		
		
		/*//员工日均统计、和导出
		@Override
		public List<DeptAvgTime> getStaffTimeStatistics(DeptAvgTime deptAvgTime) {
			
			List<DeptAvgTime> deptAvg=new ArrayList<DeptAvgTime>();
			
			//注意传过来的所有部门编号代码是没有加‘’
			String strs = new String();
			if(deptAvgTime.getDept_num()!=null){
				String[] str=deptAvgTime.getDept_num().split(",");
				for (int i = 0; i < str.length; i++) {
					strs+="'"+str[i]+"',";
				}
				strs=strs.substring(0,strs.length()-1);
				
			}

			//Double sumtime=0.0;
			
			//查询员工信息(并且有多少个员工)
			 List<DeptAvgTime> staff=deptAvgTimeDao.getStaffAndDeptInfo(strs,deptAvgTime.getStaff_no(),deptAvgTime.getStaff_name(), deptAvgTime.getLayerDeptNum());
			 if(staff.size()!=0){
				 for (DeptAvgTime deptAvgTime2 : staff) {
					 //查询员工节假日、公休日除外总的工作时间
					 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime2.getStaff_num(),
							 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
					 //查询员工实际的工作天数
					 List<DeptAvgTime> deptSumcount= deptAvgTimeDao.getStaffDateWorkTime(deptAvgTime2.getStaff_num(),
							 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
					 
					 if(deptSumTime.size()!=0 && deptSumcount.size()!=0){//两者都不为空的时候执行
						 for (DeptAvgTime dept : deptSumTime) {
								 DeptAvgTime staffAvg=new DeptAvgTime();
								 staffAvg.setDept_name(deptAvgTime2.getBM_MC());
								 staffAvg.setStaff_no(dept.getStaff_no());
								 staffAvg.setStaff_name(dept.getStaff_name());
								 staffAvg.setFrom_Time(deptAvgTime.getFrom_Time());
								 staffAvg.setTo_Time(deptAvgTime.getTo_Time());
								 staffAvg.setStaffSumTime(dept.getSumTime());//总工时
								 staffAvg.setAvgTime(dept.getSumTime()/deptSumcount.size());//员工日均时间 
								 deptAvg.add(staffAvg);  
						 }
					 }
				 }
			 }
			return deptAvg;
		}*/
		
		
		
		/*//所有一级部门日均时间统计、导出(部门)
		@Override
		public List<DeptAvgTime> getAllAvgTimeStatistics(DeptAvgTime deptAvgTime) {
			List<DeptAvgTime> deptAvg=new ArrayList<DeptAvgTime>();
			
			// 若部门编号为空，则查询所有一级部门
			if(deptAvgTime.getDept_num() == null) {
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			// 遍历所有一级部门，查询各部门的日均工作天数
			String[] strs=deptAvgTime.getDept_num().split(",");
			for (int i = 0; i < strs.length; i++) {
				String str=new String();
				
				// 查询该部门的子部门
				List<Department> allDepartment = departmentDao.getAllDepart();
				str += getAllChildDeptByParent(strs[i],allDepartment);
				str += "'"+strs[i]+"'";
					
				Double sumtime=0.0;
				//查询员工信息(并且有多少个员工)
				 List<DeptAvgTime> staff=deptAvgTimeDao.getStaffAndDeptInfo(str,deptAvgTime.getStaff_no(),deptAvgTime.getStaff_name(), deptAvgTime.getLayerDeptNum());
				 if(staff.size()!=0){
					 for (DeptAvgTime deptAvgTime2 : staff) {
						 //查询员工除节假日、公休日外总的工作时间
						 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime2.getStaff_num(),
								 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
						 if(deptSumTime.size()!=0){
							 for (DeptAvgTime dept : deptSumTime) {
								 sumtime=sumtime+dept.getSumTime();
							 }
						 }
					 }
					 sumtime=sumtime/staff.size();//总工时除以人数（多少个员工）	 
				 }
				 String dept_name= deptAvgTimeDao.getDeptNames(strs[i]);//根据部门代码查询部门名称
				 
				 DeptAvgTime avg=new DeptAvgTime();
				 //查询员工除节假日、公休日外(主要用户取出正常上班多少天)
				 List<DeptAvgTime> list= deptAvgTimeDao.getDateWorkTime(str,
						 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
				 if(list.size()!=0){
					 avg.setAvgTime(sumtime/list.size());
				 }else{
					 avg.setAvgTime(0.0);
				 }
				 avg.setDept_name(dept_name);
				 avg.setFrom_Time(deptAvgTime.getFrom_Time());
				 avg.setTo_Time(deptAvgTime.getTo_Time());
				 deptAvg.add(avg);
			}
			return deptAvg;
		}*/
		
		//所有二级部门日均时间统计、导出(部门)
		/*@Override
		public List<DeptAvgTime> getAllTwoDeptAvgTime(DeptAvgTime deptAvgTime) {
			
			if(deptAvgTime.getDept_num()==null){
				List<DeptAvgTime> bmdm = getBmdmBySjbmDm(GlobalConstant.DEPARTMENT_ROOT_BM_DM);
				if(bmdm.size()!=0){
					String str=new String();
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						str+=deptAvgTime2.getBm_dm()+",";
					}
					deptAvgTime.setDept_num(str);
				}
			}
			
			String[] strs = deptAvgTime.getDept_num().split(",");
			
			for (int i = 0; i < strs.length; i++) {
				List<DeptAvgTime> bmdm = deptAvgTimeDao.getBmdmBySjbmDm(strs[i], deptAvgTime.getLayerDeptNum() );
				
				if(bmdm.size()!=0){
					for (DeptAvgTime deptAvgTime2 : bmdm) {
						
						// 查询该部门的子部门
						List<Department> allDepartment = departmentDao.getAllDepart();
						String childDept = "";
						childDept += "'" + deptAvgTime2.getBm_dm() + "',";
						childDept += getAllChildDeptByParent(deptAvgTime2.getBm_dm(),allDepartment);
						childDept +="'"+strs[i]+"'";
						
						//str+="'"+deptAvgTime2.getBm_dm()+"',";
						Double sumtime=0.0;
						//查询员工信息(并且有多少个员工)
						 List<DeptAvgTime> staff=deptAvgTimeDao.getStaffAndDeptInfo(childDept,deptAvgTime.getStaff_no(),
								 deptAvgTime.getStaff_name(), deptAvgTime.getLayerDeptNum());
						 if(staff.size()!=0){
							 for (DeptAvgTime deptAvgTime3 : staff) {
								 //查询员工除节假日、公休日外总的工作时间
								 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime3.getStaff_num(),
										 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
								 if(deptSumTime.size()!=0){
									 for (DeptAvgTime dept : deptSumTime) {
										 sumtime=sumtime+dept.getSumTime();
									 }
								 }
							 }
							 sumtime=sumtime/staff.size();//总工时除以人数（多少个员工）	 
						 }
						 String dept_name= deptAvgTimeDao.getDeptNames(deptAvgTime2.getBm_dm());//根据部门代码查询部门名称
						 
						 DeptAvgTime avg=new DeptAvgTime();
						 //查询员工除节假日、公休日外(主要用户取出正常上班多少天)
						 List<DeptAvgTime> list= deptAvgTimeDao.getDateWorkTime("'"+deptAvgTime2.getBm_dm()+"'",
								 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
						 if(list.size()!=0){
							 avg.setAvgTime(sumtime/list.size());
						 }else{
							 avg.setAvgTime(0.0);
						 }
						 avg.setDept_name(dept_name);
						 avg.setFrom_Time(deptAvgTime.getFrom_Time());
						 avg.setTo_Time(deptAvgTime.getTo_Time());
						 deptAvg.add(avg);
					}
				
				}
			}
			return deptAvg;
			
		}*/
		
		/*//所有员工日均统计、和导出
		@Override
		public List<DeptAvgTime> getAllStaffAvgTime(DeptAvgTime deptAvgTime) {
			List<DeptAvgTime> deptAvg=new ArrayList<DeptAvgTime>();
			
			// 如果部门为空，则查询所有部门的员工
			if(deptAvgTime.getDept_num()!=null){
					// 查询该部门的子部门
					List<Department> allDepartment = departmentDao.getAllDepart();
					String childDept = "";
					childDept += getAllChildDeptByParent(deptAvgTime.getDept_num(),allDepartment);
					childDept +="'"+deptAvgTime.getDept_num()+"'";
					
					
					//Double sumtime=0.0;
					//查询员工信息(并且有多少个员工)
					 List<DeptAvgTime> staff=deptAvgTimeDao.getStaffAndDeptInfo(childDept,deptAvgTime.getStaff_no(),deptAvgTime.getStaff_name(),deptAvgTime.getLayerDeptNum());
					 if(staff.size()!=0){
						 for (DeptAvgTime deptAvgTime2 : staff) {
							 //查询员工节假日、公休日除外总的工作时间
							 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime2.getStaff_num(),
									 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
							 //查询员工实际的工作天数
							 List<DeptAvgTime> deptSumcount= deptAvgTimeDao.getStaffDateWorkTime(deptAvgTime2.getStaff_num(),
									 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
							 
							 if(deptSumTime.size()!=0 && deptSumcount.size()!=0){//两者都不为空的时候执行
								 for (DeptAvgTime dept : deptSumTime) {
										 DeptAvgTime staffAvg=new DeptAvgTime();
										 staffAvg.setDept_name(deptAvgTime2.getBM_MC());
										 staffAvg.setStaff_no(dept.getStaff_no());
										 staffAvg.setStaff_name(dept.getStaff_name());
										 staffAvg.setFrom_Time(deptAvgTime.getFrom_Time());
										 staffAvg.setTo_Time(deptAvgTime.getTo_Time());
										 staffAvg.setStaffSumTime(dept.getSumTime());//总工时
										 staffAvg.setAvgTime(dept.getSumTime()/deptSumcount.size());//员工日均时间 
										 deptAvg.add(staffAvg);  
								 }
							 }
						 }
					 }
			}else{
				//查询员工信息(并且有多少个员工)
				 List<DeptAvgTime> staff=deptAvgTimeDao.getStaffAndDeptInfo(null,deptAvgTime.getStaff_no(),deptAvgTime.getStaff_name(), deptAvgTime.getLayerDeptNum());

				 if(staff.size()!=0){
					 for (DeptAvgTime deptAvgTime2 : staff) {
						 //查询员工节假日、公休日除外总的工作时间
						 List<DeptAvgTime> deptSumTime= deptAvgTimeDao.getDateSumWorkTime(deptAvgTime2.getStaff_num(),
								 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
						 //查询员工实际的工作天数
						 List<DeptAvgTime> deptSumcount= deptAvgTimeDao.getStaffDateWorkTime(deptAvgTime2.getStaff_num(),
								 deptAvgTime.getFrom_Time(),deptAvgTime.getTo_Time());
						 
						 if(deptSumTime.size()!=0 && deptSumcount.size()!=0){//两者都不为空的时候执行
							 for (DeptAvgTime dept : deptSumTime) {
									 DeptAvgTime staffAvg=new DeptAvgTime();
									 staffAvg.setDept_name(deptAvgTime2.getBM_MC());
									 staffAvg.setStaff_no(dept.getStaff_no());
									 staffAvg.setStaff_name(dept.getStaff_name());
									 staffAvg.setFrom_Time(deptAvgTime.getFrom_Time());
									 staffAvg.setTo_Time(deptAvgTime.getTo_Time());
									 staffAvg.setStaffSumTime(dept.getSumTime());//总工时
									 staffAvg.setAvgTime(dept.getSumTime()/deptSumcount.size());//员工日均时间 
									 deptAvg.add(staffAvg);  
							 }
						 }
					 }
				 }
			}
					return deptAvg;
		}*/
		
}
