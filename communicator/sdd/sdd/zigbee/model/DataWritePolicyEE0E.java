package com.kuangchi.sdd.zigbee.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import com.kuangchi.sdd.zigbee.handler.DataWritePolicy;
import com.kuangchi.sdd.zigbee.model.Data;
import com.kuangchi.sdd.zigbee.util.CRC8;
import com.kuangchi.sdd.zigbee.util.Tool;

public class DataWritePolicyEE0E implements DataWritePolicy {

	@Override
	public void writeData(ChannelHandlerContext ctx, Data data)
			throws Exception {
  	    Data0xEE0x0E data0xEE0x0E=(Data0xEE0x0E) data;

		ByteBuf byteBuf = Unpooled.buffer(3+data0xEE0x0E.getLength()+1);
		byteBuf.writeByte(data0xEE0x0E.getHeader());
		byteBuf.writeByte(data0xEE0x0E.getCmd());
		byteBuf.writeByte(data0xEE0x0E.getLength());
		
		byteBuf.writeBytes(Tool.hexStringtoBytes(data0xEE0x0E.getLockId()));   		
		byteBuf.writeByte(data0xEE0x0E.getStatus());
		
		byte[] bytes=new byte[3+data0xEE0x0E.getLength()];
		byteBuf.getBytes(0, bytes);
		int calCrc=CRC8.crc8(bytes, bytes.length);
		data0xEE0x0E.setCrc(calCrc);
		byteBuf.writeByte(data0xEE0x0E.getCrc());
		ctx.writeAndFlush(byteBuf); 
		Thread.sleep(1000);
        ctx.close();
      
		
	}

}
