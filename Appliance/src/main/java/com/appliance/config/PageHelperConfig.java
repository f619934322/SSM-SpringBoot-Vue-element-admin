package com.appliance.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
 * 分页配置类
 * @author Dell
 *
 */
@Configuration
public class PageHelperConfig {

	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");//offsetAsPageNum：默认值为 false，该参数对使用 RowBounds 作为分页参数时有效。 当该参数设置为 true 时，会将 RowBounds 中的 offset 参数当成 pageNum 使用，可以用页码和页面大小两个
		properties.setProperty("rowBoundsWithCount", "true");//默认值为false,使用 RowBounds 分页不会进行 count 查询
		properties.setProperty("reasonable", "true");//默认值为false。当该参数设置为 true 时，pageNum<=0 时会查询第一页， pageNum>pages（超过总数时），会查询最后一页
		properties.setProperty("dialect", "mysql"); // 配置helperDialect属性来指定分页插件使用哪种方言。配置时，可以使用下面的缩写值：oracle,mysql,mariadb
		pageHelper.setProperties(properties);
		return pageHelper;

	}
}
