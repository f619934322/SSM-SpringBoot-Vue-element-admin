package com.appliance.shiro;

import org.apache.shiro.SecurityUtils;
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
		if (userVo.getUserType() == 1) {// 如果数据库的用户类型为1，则给予如下资源权限
			info.addStringPermission("perm:normal");// 资源权限赋权
		}
		if (userVo.getUserType() == 2) {// 如果为2，则给予如下资源权限
			info.addStringPermission("perm:normal");// 资源权限赋权
			info.addStringPermission("perm:admin");// 资源权限赋权
		}
		SecurityUtils.getSubject().getSession().setTimeout(1000 * 60 * 60 * 6L);// 设置超时时长，超时后自动logout。单位ms，刷新时间
		return info;
	}

	/**
	 * 执行认证逻辑
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		log.info("执行认证逻辑");
		UsernamePasswordToken thisToken = (UsernamePasswordToken) token;
		UserDto userDto = new UserDto();
		userDto.setStaffNo(thisToken.getUsername());
		UserVo userVo = userMapper.userLogin(userDto);// 根据员工号查询数据库对应数据
		SecurityUtils.getSubject().getSession().setTimeout(1000 * 60 * 60 * 6L);// 设置超时时长，超时后自动logout。单位ms
		if (userVo != null) {
			return new SimpleAuthenticationInfo(userVo, userVo.getPassword(), "UserRealm");// Shiro判断密码是否一致,并存储Principal,可能抛出IncorrectCredentialsException在service
		} else {
			return null;// Shiro底层定义NULL为用户名不存在,可能抛出UnknownAccountException在service
		}

	}

}
