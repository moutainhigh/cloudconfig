package com.kuangchi.sdd.comm.equipment.common.server;
/**
 * 在无有效数据的情况下通知服务器返回结果信息
 * @author yu.yao
 *
 */
public class Flag {
    private String flag;//0 是 1 否
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
