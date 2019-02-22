package com.appliance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appliance.mapper.InventoryMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;
import com.appliance.service.InventoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryServiceimpl implements InventoryService {

	@Autowired
	private InventoryMapper inventoryMapper;

	/**
	 * 物品库存列表
	 */
	@Override
	public BaseResponse<PageInfo<InventoryVo>> inventoryList(InventoryDto inventoryDto) {
		try {
			PageHelper.startPage(inventoryDto.getPageNum(), inventoryDto.getPageSize());
			List<InventoryVo> inventoryList = inventoryMapper.inventoryList(inventoryDto);
			PageInfo<InventoryVo> pageInfo = new PageInfo<>(inventoryList);
			BaseResponse<PageInfo<InventoryVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(200);
			response.setStatusMsg("获取inventoryList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取inventoryList数据,信息{}", e);
			BaseResponse<PageInfo<InventoryVo>> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("获取inventoryList数据失败");
			return response;
		}
	}

}
