package com.appliance.scheduled;

//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@Component
public class ScheduledDemo {
	// 这是示例的定时任务，但是本项目暂时不需要定时任务

/*	@Scheduled(cron = "0 0 0/1 * * ? ") // 每小时执行一次
	public void statusCheck() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nowTime = sdf.format(date);
		log.info("这是每小时执行一次的定时任务！！！当前时间：" + nowTime);
	}*/

}
