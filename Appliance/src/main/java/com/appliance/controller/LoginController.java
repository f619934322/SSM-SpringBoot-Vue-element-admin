package com.appliance.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/user")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 用户登录
	 * 
	 * @param httpSession
	 * @param response
	 * @param userDto
	 * @return
	 */
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = { "application/json" })
	public String login(HttpSession httpSession, HttpServletResponse response, @RequestBody UserDto userDto) {
		log.info("执行userLogin");
		BaseResponse<UserVo> result = loginService.login(userDto);
		String resultToString = JSON.toJSONString(result);
		log.info("userLogin返回的JSON: {}", resultToString);
		return resultToString;
	}
	
	/**
	 * 用户登出
	 * @param httpSession
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userLogOut", method = RequestMethod.POST, produces = { "application/json" })
	public String logOut(HttpSession httpSession, HttpServletResponse response) {
		log.info("执行userLogOut");
		BaseResponse<UserVo> result = loginService.logOut();
		String resultToString = JSON.toJSONString(result);
		log.info("userLogOut返回的JSON: {}", resultToString);
		return resultToString;
	}

}
