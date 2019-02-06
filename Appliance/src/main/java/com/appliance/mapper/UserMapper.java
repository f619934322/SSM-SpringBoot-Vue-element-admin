package com.appliance.mapper;

import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;

public interface UserMapper {

	/**
	 * 用户登录
	 * @param userDto
	 * @return
	 */
	public UserVo userLogin(UserDto userDto);
}
