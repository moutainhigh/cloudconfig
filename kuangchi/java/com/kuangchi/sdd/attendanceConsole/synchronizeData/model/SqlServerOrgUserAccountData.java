package com.kuangchi.sdd.attendanceConsole.synchronizeData.model;

public class SqlServerOrgUserAccountData {
	String name	;
	String workno;
	String payoffflag;
	String postname;
	String dept	;

	String subdept;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkno() {
		return workno;
	}

	public void setWorkno(String workno) {
		this.workno = workno;
	}

	public String getPayoffflag() {
		return payoffflag;
	}

	public void setPayoffflag(String payoffflag) {
		this.payoffflag = payoffflag;
	}

	public String getPostname() {
		return postname;
	}

	public void setPostname(String postname) {
		this.postname = postname;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getSubdept() {
		return subdept;
	}

	public void setSubdept(String subdept) {
		this.subdept = subdept;
	}
	
}
