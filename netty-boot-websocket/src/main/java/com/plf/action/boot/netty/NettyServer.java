package com.plf.action.boot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author panlf
 * @date 2021/12/29
 */
@Component
public class NettyServer {

    /**
     * Netty启动的端口
     */
    @Value("${netty.port}")
    private Integer port;

    /**
     * Netty整合WebSocket的访问地址
     */
    @Value("${netty.ws}")
    private String websocketPath;

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //因为是基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加 ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            说明：
                            1. http 数据在传输过程中是分段，HttpObjectAggregator，就是可以将多个报文段聚合
                            2. 这就是为什么，当浏览器发送大量数据时，就会发出多次http请求。
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                             * 说明：
                             *  1.对应websocket ，它的数据是以  帧（frame） 形式传播
                             *  2. 可以看到WebSocketFrame ，下面有六个子类
                             *  3. 浏览器请求时，  ws://localhost:7000/hello 表示请求的uri
                             *  4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws 协议，保持长连接
                             *  5. 是通过一个状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));

                            //自定义的handler，处理业务逻辑
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });

            System.out.println("netty服务器启动");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
