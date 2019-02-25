package com.appliance.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class InventoryVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 教学管理用品库存表ID
	 */
	private Long id;
	
	/**
	 * 物品名称
	 */
	private String itemName;
	
	/**
	 * 物品数量
	 */
	private int itemCount;
	
	/**
	 * 物品类型
	 */
	private String itemType;
	
	/**
	 * 备注信息
	 */
	private String commit;
	
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 更新人
	 */
	private String updator;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 更新时间
	 */
	private String updateTime;
}
