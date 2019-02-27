package com.appliance.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApplyVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 申领表id
	 */
	private Long id;

	/**
	 * 库存表id
	 */
	private Long inventoryId;

	/**
	 * 物品名称
	 */
	private String itemName;
	
	/**
	 * 物品类型（关联库存表查出）
	 */
	private String itemType;

	/**
	 * 申领数量
	 */
	private int itemCount;

	/**
	 * 审核状态（0未通过，1驳回，2已通过，3已领取）
	 */
	private int status;

	/**
	 * 申领备注
	 */
	private String commit;

	/**
	 * 审核备注
	 */
	private String reviewCommit;

	/**
	 * 审核人
	 */
	private String reviewer;

	/**
	 * 审核时间
	 */
	private String reviewTime;

	/**
	 * 申领人
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
