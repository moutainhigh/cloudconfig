package com.kuangchi.sdd.businessConsole.process.model;

public class JsonResults {
	private boolean isSuccess;
	
	private Object msg;
	
	private Integer myAllCounts;//我的流程总数
	
	private Integer finishCounts;//已完成的申请总数
	
	private Integer suspendCounts;//已挂起的流程总数
	
	private Integer processCounts;//执行中的流程总数
	
	private Integer transmitProcessCounts;//获取已转交的流程总数
	
	private Integer processStartTaskCounts;//未提交流程总数
	
	private Integer userTaskCounts;//我的待办任务总数
	
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public Integer getUserTaskCounts() {
		return userTaskCounts;
	}
	public void setUserTaskCounts(Integer userTaskCounts) {
		this.userTaskCounts = userTaskCounts;
	}
	public Integer getMyAllCounts() {
		return myAllCounts;
	}
	public void setMyAllCounts(Integer myAllCounts) {
		this.myAllCounts = myAllCounts;
	}
	public Integer getFinishCounts() {
		return finishCounts;
	}
	public void setFinishCounts(Integer finishCounts) {
		this.finishCounts = finishCounts;
	}
	public Integer getSuspendCounts() {
		return suspendCounts;
	}
	public void setSuspendCounts(Integer suspendCounts) {
		this.suspendCounts = suspendCounts;
	}
	public Integer getProcessCounts() {
		return processCounts;
	}
	public void setProcessCounts(Integer processCounts) {
		this.processCounts = processCounts;
	}
	public Integer getTransmitProcessCounts() {
		return transmitProcessCounts;
	}
	public void setTransmitProcessCounts(Integer transmitProcessCounts) {
		this.transmitProcessCounts = transmitProcessCounts;
	}
	public Integer getProcessStartTaskCounts() {
		return processStartTaskCounts;
	}
	public void setProcessStartTaskCounts(Integer processStartTaskCounts) {
		this.processStartTaskCounts = processStartTaskCounts;
	}
	
	

}
