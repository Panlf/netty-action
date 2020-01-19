package com.plf.action.server.handler;

import com.plf.action.disruptor.MessageProducer;
import com.plf.action.disruptor.RingBufferWorkerPoolFactory;
import com.plf.action.entity.TranslatorData;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		
		
		TranslatorData request = (TranslatorData)msg;
		
		//自己的应用服务应该有一个ID生成规则
		String producerId = "sessionId:001";
		MessageProducer messageProducer = RingBufferWorkerPoolFactory
								.getInstance()
								.getMessageProducer(producerId);
		
		messageProducer.onData(request, ctx);
	}
}
