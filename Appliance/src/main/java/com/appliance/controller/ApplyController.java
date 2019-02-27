package com.appliance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.ApplyDto;
import com.appliance.pojo.vo.ApplyVo;
import com.appliance.service.ApplyService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/apply")
public class ApplyController {

	@Autowired
	private ApplyService applyService;

	/**
	 * 获取申领表数据
	 * 
	 * @param applyDto
	 * @return
	 */
	@PostMapping(value = "/applyList", produces = { "application/json" })
	public String applyList(@RequestBody ApplyDto applyDto) {
		log.info("执行applyList");
		BaseResponse<PageInfo<ApplyVo>> response = applyService.applyList(applyDto);
		String resultToString = JSON.toJSONString(response);
		log.info("applyList返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 新增申领物品
	 * 
	 * @param applyDto
	 * @return
	 */
	@PostMapping(value = "/insertApply", produces = { "application/json" })
	public String insertApply(@RequestBody ApplyDto applyDto) {
		log.info("执行insertApply");
		BaseResponse<String> response = applyService.insertApply(applyDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertApply返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 审核申领
	 * 
	 * @param applyDto
	 * @return
	 */
	@PostMapping(value = "/reviewApply", produces = { "application/json" })
	public String reviewApply(@RequestBody ApplyDto applyDto) {
		log.info("执行reviewApply");
		BaseResponse<String> response = applyService.reviewApply(applyDto);
		String resultToString = JSON.toJSONString(response);
		log.info("reviewApply返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 我的申领
	 * 
	 * @param applyDto
	 * @return
	 */
	@GetMapping(value = "/myApply", produces = { "application/json" })//TODO 前端未写
	public String myApply(@RequestBody ApplyDto applyDto) {
		log.info("执行myApply");
		BaseResponse<List<ApplyVo>> response = applyService.myApply(applyDto);
		String resultToString = JSON.toJSONString(response);
		log.info("myApply返回的JSON: {}", resultToString);
		return resultToString;
	}

}
