package com.kuangchi.sdd.util.file;

import java.io.UnsupportedEncodingException;

public class DownloadFile {

	/**
	 * 导出文件名编码
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String sIn){ 
		
		if (sIn == null || sIn.equals(""))
			return sIn;
		try {
			return new String(sIn.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException usex) {
			return sIn;
		}
	}
}
