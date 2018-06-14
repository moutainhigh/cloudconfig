package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.kuangchi.sdd.consum.bean.CardNumPack;
import com.kuangchi.sdd.consum.bean.ConsumeRecordPack;
import com.kuangchi.sdd.consum.bean.ParameterSetResponse;
import com.kuangchi.sdd.consum.bean.ParameterUpResponse;
import com.kuangchi.sdd.consum.bean.PersonCardPackUp;
import com.kuangchi.sdd.consum.bean.PingUp;
import com.kuangchi.sdd.consum.bean.SinglePersonCardUp;
import com.kuangchi.sdd.consum.businessBean.PingDownBusiness;
import com.kuangchi.sdd.consum.container.ConnectorFactory;
import com.kuangchi.sdd.consum.container.ParameterPool;
/**
 * 消费协议解析类,业务逻辑处理类
 * 
 * */
public class PingServerHandler extends ChannelHandlerAdapter {
	 private static final Logger LOG = Logger.getLogger(PingServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		PingDownBusiness pingDownBusiness = new PingDownBusiness();
        String machine=null;
		if (msg instanceof PingUp) {// 如果是心跳业务
			PingUp pingUp = (PingUp) msg;
			LOG.debug(pingUp.getMachine());
			pingDownBusiness.responsePing(pingUp, ctx);
			machine=String.valueOf(pingUp.getMachine());
			LOG.debug("pingup");

		} else if (msg instanceof CardNumPack) { // 回复POS01H汉显系列余额包（软件发往终端，52个字节）
			CardNumPack cardNumPack = (CardNumPack) msg;
			pingDownBusiness.responseDeposit(cardNumPack, ctx);
			machine=String.valueOf(cardNumPack.getMachine());
			LOG.debug("cardNumPack");

		} else if (msg instanceof ConsumeRecordPack) { // 回复应答包（软件发往终端，8个字节）
			ConsumeRecordPack consumeRecordPack = (ConsumeRecordPack) msg;
			pingDownBusiness.responsePack(consumeRecordPack, ctx);
			machine=String.valueOf(consumeRecordPack.getMachine());
			LOG.debug("ConsumeRecordPack");
		} else if (msg instanceof ParameterUpResponse) {
			ParameterUpResponse parameterUpResponse = (ParameterUpResponse) msg;
			// 将参数放到参数池中
			ParameterPool.addParameter(
					String.valueOf(parameterUpResponse.getMachine()),
					parameterUpResponse);
			machine=String.valueOf(parameterUpResponse.getMachine());
			LOG.debug("ParameterUpResponse");

		} else if (msg instanceof ParameterSetResponse) {
			// 参数设置的返回结果，暂时什么也不做
			LOG.debug("ParameterSetResponse");
			 

		} else if (msg instanceof PersonCardPackUp) {//
			PersonCardPackUp personCardPackUp = (PersonCardPackUp) msg;

			// 按请求块号下发名单包
			pingDownBusiness.sendPersonCardPacksDown(personCardPackUp, ctx);
			machine=String.valueOf(personCardPackUp.getMachine());

			LOG.debug("PersonCardPackUp");

		} else if (msg instanceof SinglePersonCardUp) {// 如果单个名单下发成功了
			LOG.debug("...................................>>>>>>>>>>下发成功");
			SinglePersonCardUp singlePersonCardUp = (SinglePersonCardUp) msg;
			machine=String.valueOf(singlePersonCardUp.getMachine());
			pingDownBusiness.recordSinglePersonDown(singlePersonCardUp,ctx);
		}

		
		if (null!=machine) {
			// 向连接池中添加连接信息
			ConnectorFactory.registerConnection(
					String.valueOf(machine), ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		cause.printStackTrace();
		ctx.close();

	}

}
