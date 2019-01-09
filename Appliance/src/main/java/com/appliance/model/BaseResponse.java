package com.appliance.model;

import lombok.Data;

/**
 * 公共响应类
 * @author fyhz
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> {

    private Integer statusCode;
    private String statusMsg;
    private T responseData;
    
}
