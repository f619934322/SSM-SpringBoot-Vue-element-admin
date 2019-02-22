package com.appliance.pojo.dto;

import java.io.Serializable;

import com.appliance.model.PaginationBase;

import lombok.Data;

@Data
public class InventoryDto extends PaginationBase implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 物品名称
	 */
	private String itemName;

}
