package com.appliance.pojo.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DemoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user;

	private String password;
}
