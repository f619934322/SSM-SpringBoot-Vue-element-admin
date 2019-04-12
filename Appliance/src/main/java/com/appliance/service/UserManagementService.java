package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.github.pagehelper.PageInfo;

public interface UserManagementService {

	/**
	 * 用户列表
	 * 
	 * @param userDto
	 * @return
	 */
	public BaseResponse<PageInfo<UserVo>> userList(UserDto userDto);

	/**
	 * 新增用户
	 * 
	 * @param userDto
	 * @return
	 */
	public BaseResponse<String> insertUser(UserDto userDto);

	/**
	 * 用户编辑
	 * 
	 * @param userDto
	 * @return
	 */
	public BaseResponse<String> updateUser(UserDto userDto);

	/**
	 * 用户单选删除
	 * 
	 * @param id
	 * @return
	 */
	public BaseResponse<String> deleteUser(Long id);

	/**
	 * 用户修改密码
	 * 
	 * @param userDto
	 * @return
	 */
	public BaseResponse<String> passwordUpdate(UserDto userDto);
}
