package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.consum.util.Util;

public class PrintByte {
	public static final Logger LOG = Logger.getLogger(PrintByte.class);
     public  static void printByte(ByteBuf buf){
       	byte[] bytes=new byte[buf.readableBytes()];
        buf.getBytes(0,bytes);
        LOG.info("收入到心跳包:");
         for(int i=0;i<bytes.length;i++){
        	  LOG.info(Util.getStringHex(bytes[i]));
        	 
         }

     }
}
