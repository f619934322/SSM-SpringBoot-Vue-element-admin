package com.appliance.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		Map<String, String> filterMap = new LinkedHashMap<>();
		/**
		 * Shiro 内置过滤器，过滤链定义，从上向下顺序执行 常用的过滤器： anon:无需认证（登录）可以访问 authc:必须认证才可以访问
		 * user:只要登录过，并且记住了密码，如果设置了rememberMe的功能可以直接访问 perms:该资源必须得到资源权限才可以访问
		 * role:该资源必须得到角色的权限才可以访问
		 */
		filterMap.put("/appliance/user/userLogin", "anon");// 放行的路径必须在拦截的上面编写，否则失效
		filterMap.put("/**", "perms[perm:normal]");// 使得所有路径被拦截，需要资源权限;需要多权限写法：perms[a],perms[b],中括号中间字符串为自定义
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
