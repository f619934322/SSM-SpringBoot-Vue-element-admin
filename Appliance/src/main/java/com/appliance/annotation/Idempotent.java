package com.appliance.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
	
	/**
	 * 幂等名称，作为redis缓存Key的一部分。
	 */
	String value();

	/**
	 * 幂等过期时间，即：在此时间段内，对API进行幂等处理。
	 */
	long expireMillis();
}