package com.appliance.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Json格式化工具
 */
@Slf4j
public class JsonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setSerializationInclusion(Include.NON_NULL);
	}

	private JsonUtil() {
		// 私有构造函数
	}

	/**
	 * Java Object Maps To Json
	 */
	public static String toJson(Object obj) {
		String result = "";
		if (obj == null || obj instanceof String) {
			return (String) obj;
		}
		try {
			result = MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("Java Object Maps To Json Error !", e);
			// 本来抛出一个 new RuntimeException，我给删除了，但是这样要给这个字符串对象赋初值。不应该抛出泛型异常(squid:S00112),捕获打印日志即可
		}
		return result;
	}
}