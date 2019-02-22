package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;

public interface InventoryMapper {

	public List<InventoryVo> inventoryList(InventoryDto inventoryDto);
}
