package com.plf.action.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.plf.action.client.handler.MessageConsumerImpl4Client;
import com.plf.action.client.handler.NettyClient;
import com.plf.action.disruptor.MessageConsumer;
import com.plf.action.disruptor.RingBufferWorkerPoolFactory;

@SpringBootApplication
public class NettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyClientApplication.class, args);
		
		MessageConsumer[] consumers = new MessageConsumer[4];
		for(int i=0;i<consumers.length;i++){
			MessageConsumer messageConsumer = new MessageConsumerImpl4Client("code:clientId:"+i);
			consumers[i]=messageConsumer;
		}
		
		RingBufferWorkerPoolFactory.getInstance().initAndStart(
				ProducerType.MULTI, 
				1024*1024,
				new BlockingWaitStrategy(),
				consumers);
		
		new NettyClient().sendData();
	}

}
