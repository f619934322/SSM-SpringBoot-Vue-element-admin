package com.appliance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.UserManagementService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/userManagement")
public class UserManagementController {

	@Autowired
	private UserManagementService userManagementService;

	/**
	 * 用户列表
	 * 
	 * @param userDto
	 * @return
	 */
	@PostMapping(value = "/userList", produces = { "application/json" })
	public String userList(@RequestBody UserDto userDto) {
		log.info("执行userList");
		BaseResponse<PageInfo<UserVo>> response = userManagementService.userList(userDto);
		String resultToString = JSON.toJSONString(response);
		log.info("userList返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 用户新增
	 * 
	 * @param userDto
	 * @return
	 */
	@PostMapping(value = "/insertUser", produces = { "application/json" })
	public String insertUser(@RequestBody UserDto userDto) {
		log.info("执行insertUser");
		BaseResponse<String> response = userManagementService.insertUser(userDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertUser返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 用户编辑
	 * 
	 * @param userDto
	 * @return
	 */
	@PostMapping(value = "/updateUser", produces = { "application/json" })
	public String updateUser(@RequestBody UserDto userDto) {
		log.info("执行updateUser");
		BaseResponse<String> response = userManagementService.updateUser(userDto);
		String resultToString = JSON.toJSONString(response);
		log.info("updateUser返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 单选删除用户
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/deleteUser", produces = { "application/json;charset=utf-8" })
	public String deleteUser(@RequestBody Long id) {// 这样写，前端给的不是json格式，而是一个数字
		log.info("执行deleteUser");
		BaseResponse<String> response = userManagementService.deleteUser(id);
		String resultToString = JSON.toJSONString(response);
		log.info("deleteUser返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 用户修改密码
	 * 
	 * @param userDto
	 * @return
	 */
	@PostMapping(value = "/passwordUpdate", produces = { "application/json" })
	public String passwordUpdate(@RequestBody UserDto userDto) {
		log.info("执行passwordUpdate");
		BaseResponse<String> response = userManagementService.passwordUpdate(userDto);
		String resultToString = JSON.toJSONString(response);
		log.info("passwordUpdate返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 用户批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/bacthDeleteUser", produces = { "application/json" })
	public String bacthDeleteUser(@RequestBody Long[] ids) {
		log.info("执行bacthDeleteUser");
		BaseResponse<String> response = userManagementService.bacthDeleteUser(ids);
		String resultToString = JSON.toJSONString(response);
		log.info("bacthDeleteUser返回的JSON: {}", resultToString);
		return resultToString;
	}
}
