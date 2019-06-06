package com.plf.action.rpc.service.impl;

import com.plf.action.rpc.service.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHello(String name) {
		return "Hi,"+name;
	}

}
