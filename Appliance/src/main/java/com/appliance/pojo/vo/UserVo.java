package com.appliance.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserVo implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户自增ID
	 */
	private Long id;
	
	/**
	 * 用户名（员工号）
	 */
	private String staffNo;
	
	/**
	 * 用户姓名
	 */
	private String name;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 个人权限（用户类型）
	 */
	private int userType;
	
	/**
	 * 前端需求的角色类别
	 */
	private String[] roles;
}

