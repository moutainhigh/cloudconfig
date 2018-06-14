package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class AttendanceDutyUser {
	    private Integer id;
		private Integer duty_id;
		private String  begin_time;
		private String end_time;
		private String dept_num;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getDuty_id() {
			return duty_id;
		}
		public void setDuty_id(Integer duty_id) {
			this.duty_id = duty_id;
		}
		public String getBegin_time() {
			return begin_time;
		}
		public void setBegin_time(String begin_time) {
			this.begin_time = begin_time;
		}
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		public String getDept_num() {
			return dept_num;
		}
		public void setDept_num(String dept_num) {
			this.dept_num = dept_num;
		}
		
}
