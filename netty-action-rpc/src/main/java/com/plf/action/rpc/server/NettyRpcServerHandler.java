package com.plf.action.rpc.server;

import java.lang.reflect.Method;

import com.plf.action.rpc.common.Params;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyRpcServerHandler extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof Params){
			Params params = (Params)msg;
			 
			String className = params.getClassName();
			
			
			String lastName = className.substring(className.lastIndexOf(".")+1);
			

			String newClassName = className.replace(lastName, "impl."+lastName+"Impl");
			
			String methodName = params.getMethodName();
			Class<?>[] paramerTypes = params.getParamerTypes();
			Object[] paramsValues = params.getParamerValues();
			
			Class<?> clazz = Class.forName(newClassName);
			
			Object bean = clazz.getDeclaredConstructor().newInstance();
			Method method = clazz.getDeclaredMethod(methodName, paramerTypes);
			
			Object result = method.invoke(bean, paramsValues);
			ctx.writeAndFlush(result);
			ctx.close();
		}
	}

	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
