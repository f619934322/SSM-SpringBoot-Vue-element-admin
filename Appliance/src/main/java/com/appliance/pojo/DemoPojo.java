package com.appliance.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class DemoPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user;

	private String password;

	private String token;
}
