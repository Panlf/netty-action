package com.plf.action.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plf.action.codec.MarshallingCodeCFactory;
import com.plf.action.entity.TranslatorData;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {

	private final Logger log = LoggerFactory.getLogger(NettyClient.class);
	
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 8765;
	
	//用于实际处理业务的线程组
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	
	private Channel channel; //扩展完善
	
	private ChannelFuture cf;
	
	public NettyClient(){
		this.connect(HOST,PORT);
	}

	private void connect(String host, int port) {
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workGroup)
			.channel(NioSocketChannel.class)
			//缓冲区动态调配
			.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
			//缓存区池化
			.option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)			
			.handler(new LoggingHandler(LogLevel.INFO))
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast(new ClientHandler());
				}
			});
		
		//绑定端口，同步等等请求连接
		try {
			this.cf = bootstrap.connect(host, port).sync();
			log.info("Client connected...");
			
			//数据发送
			this.channel = cf.channel();
			
		} catch (Exception e) {
			log.error("出现异常，异常信息:{}",e.getMessage());
		}
	}
	
	
	public void sendData(){
		for(int i=0;i<10;i++){
			TranslatorData request = new TranslatorData();
			request.setId(""+i);
			request.setName("请求消息名称"+i);
			request.setMessage("请求消息内容"+i);
			this.channel.writeAndFlush(request);
		}
	}
	
	public void close() throws InterruptedException{
		cf.channel().closeFuture().sync();
		workGroup.shutdownGracefully();
		log.info("Client ShutDown...");
	}
}
