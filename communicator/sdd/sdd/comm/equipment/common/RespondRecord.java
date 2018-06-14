package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

public class RespondRecord extends Data{
		int recordType;
		long recordId;

		
		public int getRecordType() {
			return recordType;
		}

		public void setRecordType(int recordType) {
			this.recordType = recordType;
		}

		
		
		
		

		

		public long getRecordId() {
			return recordId;
		}

		public void setRecordId(long recordId) {
			this.recordId = recordId;
		}

		public long getCrcFromSum() {
			long gsum=0;
			long g1, g2, g3, g4;
			g1 = ((recordId>>>24) & 0x000000FF);
			g2 = (recordId & 0x00FF0000) >> 16;
			g3 = (recordId & 0x0000FF00) >> 8;
			g4 = (recordId & 0x000000FF);
			gsum += (g1 + g2 + g3 + g4);;
			
			return gsum+recordType;
		}	
		
}
