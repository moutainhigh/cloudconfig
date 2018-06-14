package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:10
 * @功能描述: 时段信息Bean
 */
public class TimeGroupData {
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
    
}
