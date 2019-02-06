package com.appliance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/fallback")
public class FallbackController {

	@GetMapping(value = "/fallback")
	public String fallback() {
		log.warn("未登录或当前用户权限不足，跳转至此");
		BaseResponse<String> response = new BaseResponse<>();
		response.setStatusCode(201);
		response.setStatusMsg("未登录或当前用户权限不足");
		return JSON.toJSONString(response);
	}

}
