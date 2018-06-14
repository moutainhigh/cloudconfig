package com.kuangchi.sdd.zigbee.util;

public class CRC8 {
    public static	int crc8(byte[] ptr ,int len)
	{
		int i;
		int k=0;
		int crc = 0;
		while(len--!=0)
		{
			for(i=0x80;i!=0;i/=2)
			{
				if((crc&0x80)!=0)
				{
					crc*=2;
					crc^=0x07;
				}
				else
					crc*=2;
				if((ptr[k]&i)!=0)
					crc^=0x07;
			}
			k++;
		}
		return crc&0xFF;
	}
	
	public static void main(String[] args) {
		byte[]  b={(byte)0xEE, (byte)0x02, (byte)0x18, (byte)0x8C, (byte)0x13, (byte)0x52, (byte)0x02, (byte)0x00, (byte)0x4B, (byte)0x12, (byte)0x00, (byte)0x00, (byte)0x3F, (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, (byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0xF0, (byte)0xF1, (byte)0xF2, (byte)0xF3};
		 System.out.println(crc8(b, b.length));
	}
 
	

 
}
