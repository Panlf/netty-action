package com.plf.action.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plf.action.disruptor.MessageConsumer;
import com.plf.action.entity.TranslatorData;
import com.plf.action.entity.TranslatorDataWapper;

import io.netty.channel.ChannelHandlerContext;

public class MessageConsumerImpl4Server extends MessageConsumer{
	
	private final Logger log = LoggerFactory.getLogger(MessageConsumerImpl4Server.class);
	
	public MessageConsumerImpl4Server(String consumerId) {
		super(consumerId);
	}

	public void onEvent(TranslatorDataWapper event) throws Exception {
		TranslatorData request = event.getData();
		ChannelHandlerContext ctx = event.getCtx();
		
		//业务逻辑处理
		log.info("Server端:id={},name={},message={}",request.getId(),request.getName(),request.getMessage());
		
		//回应相应信息
		TranslatorData response = new TranslatorData();
		response.setId("response:"+request.getId());
		response.setName("response:"+request.getName());
		response.setMessage("response:"+request.getMessage());
		ctx.writeAndFlush(response);
	}

}
