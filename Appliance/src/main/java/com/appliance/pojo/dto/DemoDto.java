package com.appliance.pojo.dto;

import java.io.Serializable;

import com.appliance.model.PaginationBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DemoDto extends PaginationBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user;

	private String password;
}
