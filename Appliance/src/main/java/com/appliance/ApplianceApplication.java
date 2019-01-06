package com.appliance;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.appliance.mapper")
@EnableTransactionManagement
public class ApplianceApplication {
	static Logger logger = LoggerFactory.getLogger(ApplianceApplication.class);

	public static void main(String[] args) {
		logger.info("SpringBoot启动开始");
		SpringApplication.run(ApplianceApplication.class, args);
		logger.info("SpringBoot启动完毕");
	}

}
