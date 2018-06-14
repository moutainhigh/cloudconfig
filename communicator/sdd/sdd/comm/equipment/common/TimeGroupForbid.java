package com.kuangchi.sdd.comm.equipment.common;

public class TimeGroupForbid {
	  int enable;

	  TimeForbid[] timeForbids;
	  
	  
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}

	  
	
	public TimeForbid[] getTimeForbids() {
		return timeForbids;
	}
	public void setTimeForbids(TimeForbid[] timeForbids) {
		this.timeForbids = timeForbids;
	}
	public int getCrcFromSum() {
		int result=enable;
        if (null!=timeForbids) {
        	for (int i = 0; i < timeForbids.length; i++) {
        		TimeForbid timeForbid=timeForbids[i];
        		result=result+timeForbid.getCrcFromSum();
			}

		}
        return result;
	}
	  
	  

}
