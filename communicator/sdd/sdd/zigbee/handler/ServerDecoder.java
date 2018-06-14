package com.kuangchi.sdd.zigbee.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.zigbee.model.Data0xAA0x01;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x02;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x03;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x05;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x06;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x07;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x08;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x09;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0A;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0B;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0C;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0D;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0E;
import com.kuangchi.sdd.zigbee.model.Data0xAA0x0F;
import com.kuangchi.sdd.zigbee.util.CRC8;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
/**
 * 读取网关发到平台的协议 ReadService类为协议解析类
 * 
 * */
public class ServerDecoder   extends ByteToMessageDecoder{
	public static final Logger LOG = Logger.getLogger(ServerDecoder.class);
	
	 private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',  
         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
	 public static String byte2HexString(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        buffer.append(HexCode[(b >>> 4) & 0x0f]);  
	        buffer.append(HexCode[b & 0x0f]);  
	        return buffer.toString();  
	    }  
	 
	 
	 public static void printHex(byte b){
		 LOG.info(byte2HexString(b));
	
		
	 }
	
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
	 if (in.readableBytes()<=3) {//判断至少要读取超过命令头，命令号和长度的字节数
		return;
	}
	
	 LOG.info("读取字节：");
	 for (int i = 0; i < in.readableBytes(); i++) {
		printHex(in.getByte(i));
	}
	 
	 
	 
	 
	 
	  while (in.readableBytes()>3) {
		     int cmd=in.getByte(1)&0xff; 
			 int length=in.getByte(2)&0xff;
			 
		  if (in.readableBytes()<3+length+1) { //如果不是一条完整的记录，则继续等待缓冲区积累
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
		    	  if(cmd==0xff){  // "AAFF010000" 上电后上报的字节
		    		   byte[] by=new byte[5];
		    		   in.readBytes(by);
		    	  }
		          else if (cmd==0x01) { 
					Data0xAA0x01 data0xAA0x01=ReadService.generateData0xAA0x01(in);
					out.add(data0xAA0x01);
				  }else if(cmd==0x02){
					  Data0xAA0x02 data0xAA0x02=ReadService.generateData0xAA0x02(in);  
				      out.add(data0xAA0x02);
				  }else if(cmd==0x03){
					  Data0xAA0x03 data0xAA0x03=ReadService.generateData0xAA0x03(in); 
					  out.add(data0xAA0x03);
				  }else if(cmd==0x05){
					  Data0xAA0x05 data0xAA0x05=ReadService.generateData0xAA0x05(in);
					  out.add(data0xAA0x05);
				  }else if(cmd==0x06){
					  Data0xAA0x06 data0xAA0x06=ReadService.generateData0xAA0x06(in);
					  out.add(data0xAA0x06);
				  }else if(cmd==0x07){
					  Data0xAA0x07 data0xAA0x07=ReadService.generateData0xAA0x07(in);
					  out.add(data0xAA0x07);
				  }else if(cmd==0x08){
					  Data0xAA0x08 data0xAA0x08=ReadService.generateData0xAA0x08(in);
					  out.add(data0xAA0x08);
				  }else if(cmd==0x09){
					  Data0xAA0x09 data0xAA0x09=ReadService.generateData0xAA0x09(in);
					  out.add(data0xAA0x09);
				  }else if(cmd==0x0a){
					  Data0xAA0x0A data0xAA0x0A=ReadService.generateData0xAA0x0A(in);
					  out.add(data0xAA0x0A);
				  }else if(cmd==0x0b){
					  Data0xAA0x0B data0xAA0x0B=ReadService.generateData0xAA0x0B(in);
					  out.add(data0xAA0x0B);
				  }else if(cmd==0x0c){
					  Data0xAA0x0C data0xAA0x0C=ReadService.generateData0xAA0x0C(in);
					  out.add(data0xAA0x0C);
				  }else if(cmd==0x0d){
					  Data0xAA0x0D data0xAA0x0D=ReadService.generateData0xAA0x0D(in);
					  out.add(data0xAA0x0D);
				  }else if(cmd==0x0e){
					  Data0xAA0x0E data0xAA0x0E=ReadService.generateData0xAA0x0E(in);
					  out.add(data0xAA0x0E);
				  }else if(cmd==0x0f){
					  Data0xAA0x0F data0xAA0x0F=ReadService.generateData0xAA0x0F(in);
					  out.add(data0xAA0x0F);
				  }
		    }
			  if (in.readableBytes()>0&&in.readableBytes()<=3) {//判断至少要读取超过命令头，命令号和长度的字节数,如：如果接收到的是一个包和一个半包，则等待半包的别外一半也接收到了再一起处理
						return;
			  }
		      in.discardReadBytes();
	}
	 
	 
	 
	 
	 
	 
	 
	}
    
}
