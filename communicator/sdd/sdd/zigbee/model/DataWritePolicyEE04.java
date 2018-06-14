package com.kuangchi.sdd.zigbee.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import com.kuangchi.sdd.zigbee.handler.DataWritePolicy;
import com.kuangchi.sdd.zigbee.model.Data;
import com.kuangchi.sdd.zigbee.util.CRC8;
import com.kuangchi.sdd.zigbee.util.Tool;
/**
 * 上传开锁信息
 * */
public class DataWritePolicyEE04 implements DataWritePolicy {

	@Override
	public void writeData(ChannelHandlerContext ctx, Data data)
			throws Exception {
  	    Data0xEE0x04 data0xEE0x04=(Data0xEE0x04) data;

		ByteBuf byteBuf = Unpooled.buffer(3+data0xEE0x04.getLength()+1);
		byteBuf.writeByte(data0xEE0x04.getHeader());
		byteBuf.writeByte(data0xEE0x04.getCmd());
		byteBuf.writeByte(data0xEE0x04.getLength());
		
		byteBuf.writeBytes(Tool.hexStringtoBytes(data0xEE0x04.getLockId()));   		
		byteBuf.writeByte(data0xEE0x04.getStatus());
		
		byte[] bytes=new byte[3+data0xEE0x04.getLength()];
		byteBuf.getBytes(0, bytes);
		int calCrc=CRC8.crc8(bytes, bytes.length);
		data0xEE0x04.setCrc(calCrc);
		byteBuf.writeByte(data0xEE0x04.getCrc());
		ctx.writeAndFlush(byteBuf); 
		Thread.sleep(1000);
        ctx.close();
      
		
	}

}
