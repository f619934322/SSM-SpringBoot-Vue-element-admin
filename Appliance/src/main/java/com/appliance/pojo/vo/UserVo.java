package com.appliance.pojo.vo;

import lombok.Data;

@Data
public class UserVo {
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 个人权限
	 */
	private String perms;
}

