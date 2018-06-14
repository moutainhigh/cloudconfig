package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class DeptNoCheckSet {
	private Integer id;
    private String dept_num;
     private Integer check_point=0;
     private String from_time;
     private String to_time;
    private String create_time;
    private Integer herited_to_sub_dept;
    private Integer generate_record;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public Integer getCheck_point() {
		return check_point;
	}
	public void setCheck_point(Integer check_point) {
		this.check_point = check_point;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Integer getHerited_to_sub_dept() {
		return herited_to_sub_dept;
	}
	public void setHerited_to_sub_dept(Integer herited_to_sub_dept) {
		this.herited_to_sub_dept = herited_to_sub_dept;
	}
	public Integer getGenerate_record() {
		return generate_record;
	}
	public void setGenerate_record(Integer generate_record) {
		this.generate_record = generate_record;
	}
	 
    
    
    
    
}
