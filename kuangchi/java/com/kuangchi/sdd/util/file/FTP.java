package com.kuangchi.sdd.util.file;

public class FTP {
	private	String userName  ;//登录名
	private	String host;//文件服务器IP	
	private	Integer port;//文件服务器端口号
	private	String password;//文件服务器用户密码
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


}
