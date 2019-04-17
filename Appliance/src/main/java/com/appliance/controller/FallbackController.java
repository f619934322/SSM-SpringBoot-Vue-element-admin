package com.appliance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.utils.DictionaryEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/fallback")
public class FallbackController {

	@GetMapping(value = "/fallback")
	public String fallback() {
		log.warn("未登录或当前用户权限不足，跳转至此");
		BaseResponse<String> response = new BaseResponse<>();
		response.setStatusCode(DictionaryEnum.UNAUTHORIZED.getCode());
		response.setStatusMsg("未登录或当前用户权限不足");
		String resultToString = JSON.toJSONString(response);
		log.info("fallback返回的JSON: {}", resultToString);
		return resultToString;
	}

}
