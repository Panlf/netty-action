package com.plf.action.disruptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.ProducerType;
import com.plf.action.entity.TranslatorDataWapper;

public class RingBufferWorkerPoolFactory {
	
	private static class SingletonHolder{
		static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
	}
	
	private RingBufferWorkerPoolFactory(){
		
	}
	
	public static RingBufferWorkerPoolFactory getInstance(){
		return SingletonHolder.instance;
	}
	
	private static Map<String,MessageProducer> producers = new ConcurrentHashMap<>();
	
	private static Map<String,MessageConsumer> consumers = new ConcurrentHashMap<>();

	private RingBuffer<TranslatorDataWapper> ringBuffer;
	
	private SequenceBarrier sequenceBarrier;
	
	private WorkerPool<TranslatorDataWapper> workerPool;
	
	public void initAndStart(ProducerType producerType,int bufferSize,WaitStrategy waitStrategy,MessageConsumer[] messageConsumers){
		//构建ringBuffer对象
		this.ringBuffer = RingBuffer.create(producerType, 
				new EventFactory<TranslatorDataWapper>() {
					public TranslatorDataWapper newInstance(){
						return new TranslatorDataWapper();
					}
				}, 
				bufferSize, 
				waitStrategy);
		//设置序号栅栏
		this.sequenceBarrier = this.ringBuffer.newBarrier();
		//设置工作池
		this.workerPool = new WorkerPool<>(this.ringBuffer,
				this.sequenceBarrier,
				new EventExceptionHandler(),
				messageConsumers);
		
		//把所构建的消费者置入池中
		for (MessageConsumer messageConsumer : messageConsumers) {
			consumers.put(messageConsumer.getConsumerId(), messageConsumer);
		}
		
		//添加Sequences
		this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
		
		//启动工作池
		this.workerPool.start(Executors.newCachedThreadPool());
	}
	
	public MessageProducer getMessageProducer(String producerId){
		MessageProducer messageProducer = producers.get(producerId);
		if(null == messageProducer){
			messageProducer = new MessageProducer(producerId,this.ringBuffer);
			producers.put(producerId, messageProducer);
		}
		return messageProducer;
	}
	
	static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper>{

		@Override
		public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {
			
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			
		}
		
	}
}


