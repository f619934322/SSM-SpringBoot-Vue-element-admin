package com.appliance.config;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.appliance.annotation.Idempotent;
import com.appliance.exception.IdempotentException;
import com.appliance.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCommands;

/**
 * 幂等切面
 * 
 */
@Aspect
@Component
@Slf4j
@ConditionalOnClass(RedisTemplate.class)
public class IdempotentAspect {

	/**
	 * redis缓存key的模板
	 */
	private static final String KEY_TEMPLATE = "idempotent_%s";

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 根据实际路径进行调整切面注解
	 */
	@Pointcut("@annotation(com.appliance.annotation.Idempotent)")
	public void executeIdempotent() {
		log.info("executeIdempotent-@Pointcut");
	}

	@Around("executeIdempotent()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取方法
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		// 获取幂等注解
		Idempotent idempotent = method.getAnnotation(Idempotent.class);
		// 根据 key前缀 + @Idempotent.value() + 方法签名 + 参数 构建缓存键值
		// 确保幂等处理的操作对象是：同样的 @Idempotent.value() + 方法签名 + 参数
		String key = String.format(KEY_TEMPLATE,
				idempotent.value() + "_" + KeyUtil.generate(method, joinPoint.getArgs()));
		// 通过setnx确保只有一个接口能够正常访问
		// 调用KeyUtil工具类生成key
		String redisRes = redisTemplate
				.execute((RedisCallback<String>) conn -> ((JedisCommands) conn.getNativeConnection()).set(key, key,
						"NX", "PX", idempotent.expireMillis()));

		if (Objects.equals("OK", redisRes)) {
			return joinPoint.proceed();
		} else {
			log.debug("Idempotent hits, key=" + key);
			throw new IdempotentException("Idempotent hits, key=" + key);
		}
	}
}