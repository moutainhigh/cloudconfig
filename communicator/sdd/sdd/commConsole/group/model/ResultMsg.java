package com.kuangchi.sdd.commConsole.group.model;

import java.util.List;

public class ResultMsg {
    private String result_code;
    private String result_msg;
    private List<TimeGroupBlock> groupBlock;
    private String result_value;
    public String getResult_code() {
        return result_code;
    }
    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }
    public String getResult_msg() {
        return result_msg;
    }
    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }
	public List<TimeGroupBlock> getGroupBlock() {
		return groupBlock;
	}
	public void setGroupBlock(List<TimeGroupBlock> groupBlock) {
		this.groupBlock = groupBlock;
	}
	public String getResult_value() {
		return result_value;
	}
	public void setResult_value(String result_value) {
		this.result_value = result_value;
	}
    
}
