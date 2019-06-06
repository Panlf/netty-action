package com.plf.action.rpc.test;

import com.plf.action.rpc.client.NettyRpcClient;
import com.plf.action.rpc.service.HelloService;

public class Test {
	public static void main(String[] args) {
		HelloService helloService = NettyRpcClient.proxy(HelloService.class);
		System.out.println(helloService.sayHello("Lilith"));
	}
}
