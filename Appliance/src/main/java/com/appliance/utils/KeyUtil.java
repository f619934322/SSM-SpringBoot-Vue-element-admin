package com.appliance.utils;

import java.lang.reflect.Method;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Key生成工具
 */
public class KeyUtil {

	private KeyUtil() {
		// 私有构造函数
	}

	/**
	 * 根据{方法名 + 参数列表}和md5转换生成key
	 */
	public static String generate(Method method, Object... args) {
		StringBuilder sb = new StringBuilder(method.toString());
		for (Object arg : args) {
			sb.append(toString(arg));
		}
		return DigestUtils.md5Hex(sb.toString());
	}

	private static String toString(Object object) {
		if (object == null) {
			return "null";
		}
		if (object instanceof Number) {
			return object.toString();
		}
		// 调用json工具类转换成String
		return JsonUtil.toJson(object);
	}
}
