package com.plf.action.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.plf.action.disruptor.MessageConsumer;
import com.plf.action.disruptor.RingBufferWorkerPoolFactory;
import com.plf.action.server.handler.MessageConsumerImpl4Server;
import com.plf.action.server.handler.NettyServer;

@SpringBootApplication
public class NettyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);
		
		MessageConsumer[] consumers = new MessageConsumer[4];
		for(int i=0;i<consumers.length;i++){
			MessageConsumer messageConsumer = new MessageConsumerImpl4Server("code:serverId:"+i);
			consumers[i]=messageConsumer;
		}
		
		RingBufferWorkerPoolFactory.getInstance().initAndStart(
				ProducerType.MULTI, 
				1024*1024,
				new BlockingWaitStrategy(),
				consumers);
		
		new NettyServer();
	}

}
