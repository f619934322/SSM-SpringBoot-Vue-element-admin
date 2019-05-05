package com.appliance.pojo.dto;

import java.io.Serializable;

import com.appliance.model.PaginationBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryDto extends PaginationBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 物品id
	 */
	private Long id;

	/**
	 * 需求表id
	 */
	private Long demandId;

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
	 * 备注
	 */
	private String commit;

	/**
	 * 创建人（审核完成者）
	 */
	private String creator;

	/**
	 * 更新人
	 */
	private String updator;

	/**
	 * 创建时间（审核完成时间）
	 */
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

}
