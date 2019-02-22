package com.appliance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			log.error("获取inventoryList数据失败,信息{}", e);
			BaseResponse<PageInfo<InventoryVo>> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("获取inventoryList数据失败");
			return response;
		}
	}

	/**
	 * 物品批量删除
	 */
	@Transactional
	@Override
	public BaseResponse<String> batchDeleteInventory(Long[] ids) {
		try {
			for (Long id : ids) {
				inventoryMapper.deleteInventory(id); // 将Long数组遍历然后对每一个id进行删除操作
			}
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("批量删除成功");
			return response;
		} catch (Exception e) {
			log.error("batchDeleteInventory批量删除失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("批量删除失败");
			return response;
		}
	}

	/**
	 * 物品单选删除
	 */
	@Transactional
	@Override
	public BaseResponse<String> deleteInventory(Long id) {
		try {
			inventoryMapper.deleteInventory(id); // 对传入的id进行删除操作
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("单选删除成功");
			return response;
		} catch (Exception e) {
			log.error("batchDeleteInventory批量删除失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("单选删除失败");
			return response;
		}
	}

}
