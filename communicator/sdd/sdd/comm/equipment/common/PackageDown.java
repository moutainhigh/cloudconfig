package com.kuangchi.sdd.comm.equipment.common;

public class PackageDown {
	int packageNum;
    byte[] bytes;
    
    
    
    
    
    public int getPackageNum() {
		return packageNum;
	}





	public void setPackageNum(int packageNum) {
		this.packageNum = packageNum;
	}





	public byte[] getBytes() {
		return bytes;
	}





	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}





	public long getCrcFromSum() {
    	long val=0;
		 if (bytes!=null) {
			for (int i = 0; i < bytes.length; i++) {
				val+=bytes[i]&0xff;
			}
		}
		 return val+packageNum;
	}
}
