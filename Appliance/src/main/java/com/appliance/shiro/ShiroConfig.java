package com.appliance.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appliance.service.impl.LoginServiceImpl;

/**
 * Shiro配置类
 * 
 * @author Dell
 *
 */
@Configuration
public class ShiroConfig {

	/**
	 * 创建ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(
			@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 添加Shiro内置过滤器
		Map<String, String> filterMap = new LinkedHashMap<String, String>();
		/*
		 * 常见过滤器： anon:无需认证可直接访问； authc:必须认证才可访问； user：使用rememberMe功能才可访问；
		 * perms：该资源需得到资源权限才可访问； role：该资源需要得到角色权限才可访问
		 */
		filterMap.put("/appliance/user/userLogin", "anon");// 放行的路径必须在拦截的上面编写，否则失效
		filterMap.put("/**", "perms[normal]");// 使得所有路径被拦截，需要资源权限
		shiroFilterFactoryBean.setLoginUrl("/");// 权限不足访问失败跳转
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 创建DefaultWebSecurityManager
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 关联Realm
		securityManager.setRealm(userRealm);
		return securityManager;
	}

	/**
	 * 创建Realm
	 */
	@Bean(name = "userRealm")
	public UserRealm getRealm() {
		return new UserRealm();
	}
}
