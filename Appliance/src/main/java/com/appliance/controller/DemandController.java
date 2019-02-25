package com.appliance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.vo.DemandVo;
import com.appliance.service.DemandService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/demand")
public class DemandController {

	@Autowired
	private DemandService demandService;

	/**
	 * 采购需求新增
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/insertNewDemand", produces = { "application/json" })
	public String insertNewDemand(@RequestBody DemandDto demandDto) {// 新增采购需求，在需求表里新增一条数据，并且需求标识为1（新增）
		log.info("执行insertNewDemand");
		BaseResponse<String> response = demandService.insertNewDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertNewDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 采购申请补充
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/insertSupplementDemand", produces = { "application/json" })
	public String insertSupplementDemand(@RequestBody DemandDto demandDto) {// 新增采购需求，在需求表里新增一条数据，并且需求标识为1（新增）
		log.info("执行insertSupplementDemand");
		BaseResponse<String> response = demandService.insertSupplementDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertSupplementDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 获取审核清单
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/demandList", produces = { "application/json" })
	public String demandList(@RequestBody DemandDto demandDto) {
		log.info("执行demandList");
		BaseResponse<PageInfo<DemandVo>> response = demandService.demandList(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("demandList返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 采购审核操作
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/reviewDemand", produces = { "application/json" })// TODO 尚未完成业务逻辑
	public String reviewDemand(@RequestBody DemandDto demandDto) {
		log.info("执行reviewDemand");
		BaseResponse<String> response = demandService.reviewDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("reviewDemand返回的JSON: {}", resultToString);
		return resultToString;
	}
}
