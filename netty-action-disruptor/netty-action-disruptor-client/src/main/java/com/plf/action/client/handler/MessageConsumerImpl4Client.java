package com.plf.action.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plf.action.disruptor.MessageConsumer;
import com.plf.action.entity.TranslatorData;
import com.plf.action.entity.TranslatorDataWapper;

//import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumerImpl4Client extends MessageConsumer{
	
	private final Logger log = LoggerFactory.getLogger(MessageConsumerImpl4Client.class);
	
	public MessageConsumerImpl4Client(String consumerId) {
		super(consumerId);
	}

	@Override
	public void onEvent(TranslatorDataWapper event) throws Exception {
		TranslatorData response = event.getData();
		//ChannelHandlerContext ctx = event.getCtx();
		try {
			//业务逻辑处理
			log.info("Server端:id={},name={},message={}",response.getId(),response.getName(),response.getMessage());
		} finally {
			ReferenceCountUtil.release(response);
		}
	}

}
