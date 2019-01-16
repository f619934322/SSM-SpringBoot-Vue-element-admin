package com.appliance.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.appliance.mapper.UserMapper;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.utils.MD5;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 执行授权逻辑
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("执行授权逻辑");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		UserVo userVo = (UserVo) subject.getPrincipal();
		String[] perms = userVo.getPerms().split(",");//使用split方法分割字符串获取单个资源权限名称
		for(int i = 0 ; i <perms.length ; i++) {
			String permsOne = perms[i];
			info.addStringPermission(permsOne);//资源权限赋权
		}

		return info;
	}

	/**
	 * 执行认证逻辑
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("执行认证逻辑");
		UsernamePasswordToken thisToken = (UsernamePasswordToken) token;
		UserDto userDto = new UserDto();
		userDto.setUserName(thisToken.getUsername());
		UserVo userVo = userMapper.userLogin(userDto);
		if (userVo != null) {
			return new SimpleAuthenticationInfo(userVo, MD5.md5(userVo.getPassword()), "UserRealm");// Shiro判断密码是否一致,并存储Principal,密码使用MD5进行密码加密
		} else {
			return null;// Shiro底层定义NULL为用户名不存在
		}

	}

}
