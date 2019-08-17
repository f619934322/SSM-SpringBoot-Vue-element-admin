package com.appliance.exception;

/**
 * 用于专门处理幂等相关异常。
 */
public class IdempotentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdempotentException(String message) {
		super(message);
	}

	/*
	 * 覆盖方法不应该简单地在超类中调用相同的方法(squid:S1185)， getMessage()可以直接调用超类中的，不必在此重写此方法然后只return
	 * super.getMessage()
	 */
}
