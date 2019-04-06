package com.appliance.pojo.dto;

import java.io.Serializable;

import com.appliance.model.PaginationBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyDto extends PaginationBase implements Serializable {

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

	/**
	 * 前端传入的起始与结束日期数组
	 */
	private String[] createTimeBeginToEnd;

	/**
	 * 前端传入的起始日期（需要从数组中分离）
	 */
	private String createTimeBegin;

	/**
	 * 前端传入的结束日期（需要从数组中分离）
	 */
	private String createTimeEnd;

}
