package com.appliance.utils;

/**
 * 枚举字典
 * 
 * @author fyhz
 *
 */
public enum DictionaryEnum {

	/* 通用code */
	REQUEST_SUCCESS(200, "请求成功"), REQUEST_FAILED(500, "请求失败"), UNAUTHORIZED(401, "未授权");

	private Integer code;
	private String msg;

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	private DictionaryEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String getValue(Integer code) {
		DictionaryEnum[] enums = DictionaryEnum.values();
		for (DictionaryEnum e : enums) {
			if (e.getCode() == code) {
				return e.getMsg();
			}
		}
		return "";
	}

}
