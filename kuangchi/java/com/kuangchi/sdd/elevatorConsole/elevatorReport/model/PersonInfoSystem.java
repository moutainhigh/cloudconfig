package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

public class PersonInfoSystem {
		private String staff_name ; //员工姓名
		private String staff_sex ;  //员工性别
		//private String birthdate ;  //出生日期 (表中没有)
		private String staff_no ; //工号
		private String staff_mobile ;//手机号
		private String papers_num ;//身份证号码
		private String staff_address ;//地址
		private String staff_remark ; //备注
		private String BM_MC;//部门名称（机构）
		private String BM_DM;
		
		public String getBM_DM() {
			return BM_DM;
		}
		public void setBM_DM(String bM_DM) {
			BM_DM = bM_DM;
		}
		public String getStaff_name() {
			return staff_name;
		}
		public void setStaff_name(String staff_name) {
			this.staff_name = staff_name;
		}
		public String getStaff_sex() {
			return staff_sex;
		}
		public void setStaff_sex(String staff_sex) {
			this.staff_sex = staff_sex;
		}
		public String getStaff_no() {
			return staff_no;
		}
		public void setStaff_no(String staff_no) {
			this.staff_no = staff_no;
		}
		public String getStaff_mobile() {
			return staff_mobile;
		}
		public void setStaff_mobile(String staff_mobile) {
			this.staff_mobile = staff_mobile;
		}
		public String getPapers_num() {
			return papers_num;
		}
		public void setPapers_num(String papers_num) {
			this.papers_num = papers_num;
		}
		public String getStaff_address() {
			return staff_address;
		}
		public void setStaff_address(String staff_address) {
			this.staff_address = staff_address;
		}
		public String getStaff_remark() {
			return staff_remark;
		}
		public void setStaff_remark(String staff_remark) {
			this.staff_remark = staff_remark;
		}
		public String getBM_MC() {
			return BM_MC;
		}
		public void setBM_MC(String bM_MC) {
			BM_MC = bM_MC;
		}
		
	
	
	
}
