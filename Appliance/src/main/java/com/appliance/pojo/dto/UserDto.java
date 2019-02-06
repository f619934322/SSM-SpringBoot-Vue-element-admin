package com.appliance.pojo.dto;

import lombok.Data;

@Data
public class UserDto {

	/**
	 * 用户名（员工号）
	 */
	private String staffNo;
	
	/**
	 * 密码
	 */
	private String password;
}
