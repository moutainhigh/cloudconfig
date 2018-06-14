package com.kuangchi.sdd.comm.equipment.common;


public class ClearData {
//case 0x01: 清除门禁记录。       case 0x02: 清除巡更记录。     case 0x04:清除刷卡记录。        case 0x08:清除门禁权限。        case 0x10: 清除巡更权限。
		private int dataType;

		public int getDataType() {
			return dataType;
		}

		public void setDataType(int dataType) {
			this.dataType = dataType;
		}
		public int getCrcFromSum() {
			return dataType;
		}
		
		
}
