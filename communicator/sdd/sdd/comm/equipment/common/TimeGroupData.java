package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

/**
 * 时段组
 * 记录用户的时段组信息（起始时间-结束时间）
 * @author yu.yao
 *
 */
public class TimeGroupData extends Data {
    private TimeData start;//起始时间
    private TimeData end;//起始时间

	public TimeData getStart() {
		return start;
	}
	public void setStart(TimeData start) {
		this.start = start;
	}
	public TimeData getEnd() {
		return end;
	}
	public void setEnd(TimeData end) {
		this.end = end;
	}
	public int getCrcFromSum(){
		int result =(start==null?0:start.getCrcFromSum())+
				    (end==null?0:end.getCrcFromSum());
		
		return result;
	}
}
