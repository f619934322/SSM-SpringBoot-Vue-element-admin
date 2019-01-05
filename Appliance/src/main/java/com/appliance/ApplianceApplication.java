package com.appliance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.appliance.mapper")
@EnableTransactionManagement
public class ApplianceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplianceApplication.class, args);
	}

}

