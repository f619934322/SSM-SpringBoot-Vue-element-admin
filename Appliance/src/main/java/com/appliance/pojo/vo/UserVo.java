package com.appliance.pojo.vo;

import lombok.Data;

@Data
public class UserVo {
	
	/**
	 * 用户名（员工号）
	 */
	private String staffNo;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 个人权限（用户类型）
	 */
	private String perms;
}

