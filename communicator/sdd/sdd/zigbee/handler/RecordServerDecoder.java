package com.kuangchi.sdd.zigbee.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x01;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x02;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x03;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x04;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x05;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x06;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x07;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x08;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x09;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0A;

import com.kuangchi.sdd.zigbee.util.CRC8;
/**
 * 读取网关发来的记录数据，并进行协议解析
 * */
public class RecordServerDecoder  extends ByteToMessageDecoder{
	public static final Logger LOG = Logger.getLogger(RecordServerDecoder.class);
	 private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',  
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
	 public static String byte2HexString(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        buffer.append(HexCode[(b >>> 4) & 0x0f]);  
	        buffer.append(HexCode[b & 0x0f]);  
	        return buffer.toString();  
	    }  
	 
	 
	 public static void printHex(byte b){
		
			System.out.print(byte2HexString(b));
	
		
	 }
	
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		 if (in.readableBytes()<=3) {//判断至少要读取超过命令头，命令号和长度的字节数
				return;
			}
		 LOG.info("锁服务缓存字节：");
			 for (int i = 0; i < in.readableBytes(); i++) {
				printHex(in.getByte(i));
			}
			 LOG.info("--------------------");
			 
			 
			 
			 
	  while (in.readableBytes()>3) {
		     int cmd=in.getByte(1); 
		     
			 int length=in.getByte(2);
	 
		  if (in.readableBytes()<3+length+1) {  //如果不是一条完整的记录，则继续等待缓冲区积累
				return;
		   } 
		   byte[] bs=new byte[3+length];
		   in.getBytes(0, bs);
		   int calculateCrc=CRC8.crc8(bs, bs.length);
		   int receiveCrc=in.getByte(3+length)&0xff;
		   if (calculateCrc!=receiveCrc) {
		        byte[] bytes=new byte[3+length+1];  
			    in.readBytes(bytes);
		    }else{    
			  Data0xAA0x04 data0xAA0x04=ReadService.generateData0xAA0x04(in);
			  out.add(data0xAA0x04);
		    } 
		   
			 if (in.readableBytes()>0&&in.readableBytes()<=3) {//判断至少要读取超过命令头，命令号和长度的字节数,如：如果接收到的是一个包和一个半包，则等待半包的别外一半也接收到了再一起处理
					return;
			}
		      in.discardReadBytes();

	  }
	}
	
	 
	 
	 
	 
	 
	 
	
}
