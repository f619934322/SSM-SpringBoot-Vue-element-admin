package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;

public interface LoginService {

	/**
	 * 用户登录service
	 * @param userDto
	 * @return
	 */
	public BaseResponse<UserVo> login(UserDto userDto);
	
	/**
	 * 用户登出
	 * @return
	 */
	public BaseResponse<UserVo> logout();
}
