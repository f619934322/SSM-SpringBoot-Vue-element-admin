package com.appliance.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appliance.mapper.UserMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.LoginService;
import com.appliance.utils.MD5;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

	/**
	 * 用户登录service
	 */
	@Override
	public BaseResponse<UserVo> login(UserDto userDto) {
		/**
		 * Shiro认证流程
		 */
		// 1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(userDto.getUserName(), MD5.md5(userDto.getPassword()));//使用MD5进行密码加密
		// 3.执行登录方法
		try {
			subject.login(token);//进入com.appliance.shiro.UserRealm中执行Shiro认证
			BaseResponse<UserVo> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("登录成功");
			return response;
		} catch (UnknownAccountException e) {
			log.warn("用户名不存在");
			BaseResponse<UserVo> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("用户名不存在");
			return response;
		} catch (IncorrectCredentialsException e) {
			log.warn("密码错误");
			BaseResponse<UserVo> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("密码错误");
			return response;
		}

	}

}
