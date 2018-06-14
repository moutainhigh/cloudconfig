package com.kuangchi.sdd.zigbee.handler;

import com.kuangchi.sdd.zigbee.model.Data;

import io.netty.channel.ChannelHandlerContext;

public interface DataWritePolicy {
	public static int timeout=10;
    public void writeData (ChannelHandlerContext ctx,Data data) throws Exception;
}
