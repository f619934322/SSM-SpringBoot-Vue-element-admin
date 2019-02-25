package com.appliance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.service.DemandService;

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
}
