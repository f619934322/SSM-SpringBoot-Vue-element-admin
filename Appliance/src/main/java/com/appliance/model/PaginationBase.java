package com.appliance.model;

import lombok.Data;

@Data
public class PaginationBase {

	/**
	 * 条数
	 */
    private Integer pageSize;
    
    /**
     * 页数
     */
    private Integer pageNum;

}
