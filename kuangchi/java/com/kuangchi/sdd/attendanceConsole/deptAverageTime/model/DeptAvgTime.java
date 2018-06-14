package com.kuangchi.sdd.attendanceConsole.deptAverageTime.model;

import java.math.BigDecimal;

public class DeptAvgTime {
		private String dept_no;
		private String from_Time;//起始时间
		private String to_Time;//结束时间
		private String dept_num;
		private String dept_name;
		private Double sumTime;//总的工作时间
		private Double avgTime;//日均时间
		
		private String staff_no;
		private String staff_num;
		private String staff_name;
		private String BM_MC;
		private String BM_NO;
		private String bm_dm;
		
		private Double work_time;//实际工作时间
		private String every_date;
		
		
		private Double staffSumTime;
		
		private String layerDeptNum;// 分层部门编号，用于开启分层功能时筛选显示部门  	by yuman.gao
		
		
		public String getLayerDeptNum() {
			return layerDeptNum;
		}
		public void setLayerDeptNum(String layerDeptNum) {
			this.layerDeptNum = layerDeptNum;
		}
		public Double getStaffSumTime() {
			return staffSumTime;
		}
		public void setStaffSumTime(Double staffSumTime) {
			BigDecimal b=new BigDecimal(staffSumTime);  
			double f1=b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			this.staffSumTime = f1;
		}
		public Double getSumTime() {
			return sumTime;
		}
		public void setSumTime(Double sumTime) {
			BigDecimal b=new BigDecimal(sumTime);  
			double f1=b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			this.sumTime = f1;
		}
		public String getDept_name() {
			return dept_name;
		}
		public void setDept_name(String dept_name) {
			this.dept_name = dept_name;
		}
		public String getBm_dm() {
			return bm_dm;
		}
		public void setBm_dm(String bm_dm) {
			this.bm_dm = bm_dm;
		}
		public String getBM_NO() {
			return BM_NO;
		}
		public void setBM_NO(String bM_NO) {
			BM_NO = bM_NO;
		}
		public String getEvery_date() {
			return every_date;
		}
		public void setEvery_date(String every_date) {
			this.every_date = every_date;
		}
		public String getStaff_no() {
			return staff_no;
		}
		public void setStaff_no(String staff_no) {
			this.staff_no = staff_no;
		}
		public String getStaff_num() {
			return staff_num;
		}
		public void setStaff_num(String staff_num) {
			this.staff_num = staff_num;
		}
		public String getStaff_name() {
			return staff_name;
		}
		public void setStaff_name(String staff_name) {
			this.staff_name = staff_name;
		}
		public String getBM_MC() {
			return BM_MC;
		}
		public void setBM_MC(String bM_MC) {
			BM_MC = bM_MC;
		}
		public String getDept_num() {
			return dept_num;
		}
		public void setDept_num(String dept_num) {
			this.dept_num = dept_num;
		}
		
		
		public Double getAvgTime() {
			return avgTime;
		}
		public void setAvgTime(Double avgTime) {
			BigDecimal b=new BigDecimal(avgTime);  
			double f1=b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			this.avgTime = f1;
		}
		public String getDept_no() {
			return dept_no;
		}
		public void setDept_no(String dept_no) {
			this.dept_no = dept_no;
		}
		public String getFrom_Time() {
			return from_Time;
		}
		public void setFrom_Time(String from_Time) {
			this.from_Time = from_Time;
		}
		public String getTo_Time() {
			return to_Time;
		}
		public void setTo_Time(String to_Time) {
			this.to_Time = to_Time;
		}
		public Double getWork_time() {
			return work_time;
		}
		public void setWork_time(Double work_time) {
			BigDecimal b=new BigDecimal(work_time);  
			double f1=b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			this.work_time = work_time;
		}

		
		
}
