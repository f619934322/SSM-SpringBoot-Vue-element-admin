package com.appliance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;
import com.appliance.service.InventoryService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	/**
	 * 物品库存列表
	 * 
	 * @param inventoryDto
	 * @return
	 */
	@PostMapping(value = "/inventoryList", produces = { "application/json" })
	public String inventoryList(@RequestBody InventoryDto inventoryDto) {
		log.info("执行inventoryList");
		BaseResponse<PageInfo<InventoryVo>> response = inventoryService.inventoryList(inventoryDto);
		String resultToString = JSON.toJSONString(response);
		log.info("inventoryList返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 物品批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/batchDeleteInventory", produces = { "application/json;charset=utf-8" })
	public String batchDeleteInventory(@RequestBody Long[] ids) {
		log.info("执行batchDeleteInventory");
		BaseResponse<String> response = inventoryService.batchDeleteInventory(ids);
		String resultToString = JSON.toJSONString(response);
		log.info("batchDeleteInventory返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 物品单选删除
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/deleteInventory", produces = { "application/json;charset=utf-8" })
	public String deleteInventory(@RequestBody Long id) {
		log.info("执行deleteInventory");
		BaseResponse<String> response = inventoryService.deleteInventory(id);
		String resultToString = JSON.toJSONString(response);
		log.info("deleteInventory返回的JSON: {}", resultToString);
		return resultToString;
	}

}
