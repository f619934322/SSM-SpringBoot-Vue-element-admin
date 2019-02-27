package com.appliance.pojo.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名（员工号）
	 */
	private String staffNo;
	
	/**
	 * 密码
	 */
	private String password;
}
