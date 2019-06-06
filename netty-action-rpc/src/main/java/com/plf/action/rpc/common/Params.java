package com.plf.action.rpc.common;

import java.io.Serializable;

public class Params implements Serializable{

	private static final long serialVersionUID = 6875308187396586903L;
	
	/*
	 * 全限定类名
	 */
	private String className;
	
	/**
	 * 调用的方法
	 */
	private String methodName;
	
	/**
	 * 方法参数类型列表
	 */
	private Class<?>[] paramerTypes;
	
	/**
	 * 参数列表
	 */
	private Object[] paramerValues;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParamerTypes() {
		return paramerTypes;
	}

	public void setParamerTypes(Class<?>[] paramerTypes) {
		this.paramerTypes = paramerTypes;
	}

	public Object[] getParamerValues() {
		return paramerValues;
	}

	public void setParamerValues(Object[] paramerValues) {
		this.paramerValues = paramerValues;
	}
}
