package com.xkd.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {
	  public static List<String> getLoalhostIPs()   {
	        List<String> ipList=new ArrayList<String>();
	        try {
	            Enumeration<?> enumeration=NetworkInterface.getNetworkInterfaces();
	            InetAddress ip=null;
	            while(enumeration.hasMoreElements()){
	                NetworkInterface netInterface = (NetworkInterface) enumeration.nextElement();
	                Enumeration<?> addresses = netInterface.getInetAddresses();
	                while (addresses.hasMoreElements()) {
	                    ip = (InetAddress) addresses.nextElement();
	                    if (ip != null && ip instanceof Inet4Address){
	                        String ip1=ip.getHostAddress();
	                        if (!"127.0.0.1".equals(ip1)) {
	                            ipList.add(ip1);
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
				e.printStackTrace();
	        }
	        return  ipList;
	    }
	  
	  public static void main(String[] args) {
		System.out.println(getLoalhostIPs());
	}
}
