package com.plf.action.client.handler;

import com.plf.action.disruptor.MessageProducer;
import com.plf.action.disruptor.RingBufferWorkerPoolFactory;
import com.plf.action.entity.TranslatorData;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		TranslatorData response = (TranslatorData)msg;
		String producerId = "code:sessionId:001";
		MessageProducer messageProducer = RingBufferWorkerPoolFactory
						.getInstance()
						.getMessageProducer(producerId);
		
		messageProducer.onData(response, ctx);
	}
}