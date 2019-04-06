package com.appliance.pojo.dto;

import java.io.Serializable;

import com.appliance.model.PaginationBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends PaginationBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id（自增长）
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
	 * 创建人
	 */
	private String creator;

	/**
	 * 更新人
	 */
	private String updator;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 创建时间
	 */
	private String createTime;
}
