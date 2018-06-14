package com.kuangchi.sdd.commConsole.upLoadVersion.service;

import java.io.UnsupportedEncodingException;

public interface IUpLoadVersionService {
	/**
	 * 下发程序（握手）
	 * 
	 * @author xuewen.deng
	 * @throws Exception
	 */
	public String downSoftware(String mac, String deviceType) throws Exception;

	/**
	 * 正式下发程序包
	 * 
	 * @author xuewen.deng
	 * @return
	 * @throws Exception
	 */
	public int packageDownSoftware(String mac, String deviceType,
			String packageNum, String isoString) throws Exception;

	/**
	 * 下发程序包尾部
	 * 
	 * @author xuewen.deng
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public int packageStern(String mac, String deviceType,
			String residueLength, String packageNum, String isoString)
			throws UnsupportedEncodingException, Exception;

	/**
	 * 下发程序结束
	 * 
	 * @author xuewen.deng
	 * @throws Exception
	 */
	public int overSoftware(String mac, String deviceType) throws Exception;
}
