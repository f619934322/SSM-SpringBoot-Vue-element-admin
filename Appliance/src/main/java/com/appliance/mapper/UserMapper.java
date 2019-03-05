package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;

public interface UserMapper {

	/**
	 * 用户登录
	 * 
	 * @param userDto
	 * @return
	 */
	public UserVo userLogin(UserDto userDto);

	/**
	 * 用户列表
	 * 
	 * @param userDto
	 * @return
	 */
	public List<UserVo> userList(UserDto userDto);

	/**
	 * 新增用户
	 * 
	 * @param userDto
	 */
	public void insertUser(UserDto userDto);

	/**
	 * 更新用户信息
	 * 
	 * @param userDto
	 */
	public void updateUser(UserDto userDto);

	/**
	 * 判断数据库中是否有存在要注册的用户
	 * 
	 * @param userDto
	 * @return
	 */
	public int selectUserByStaffNo(UserDto userDto);
}
