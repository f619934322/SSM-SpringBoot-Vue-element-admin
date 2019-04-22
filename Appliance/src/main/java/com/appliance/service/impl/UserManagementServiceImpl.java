package com.appliance.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appliance.mapper.UserMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.dto.UserDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.UserManagementService;
import com.appliance.utils.DictionaryEnum;
import com.appliance.utils.MD5;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {

	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";// sonar要求同一字符串不得出现两次或以上，必须使用静态最终成员定义

	@Autowired
	private UserMapper userMapper;

	/**
	 * 用户列表
	 */
	@Override
	public BaseResponse<PageInfo<UserVo>> userList(UserDto userDto) {
		try {
			// 执行分页查询
			PageHelper.startPage(userDto.getPageNum(), userDto.getPageSize());
			List<UserVo> userList = userMapper.userList(userDto);
			PageInfo<UserVo> pageInfo = new PageInfo<>(userList);
			BaseResponse<PageInfo<UserVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取userList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取userList数据失败,信息{}", e);
			BaseResponse<PageInfo<UserVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取userList数据失败");
			return response;
		}
	}

	/**
	 * 新增用户
	 */
	@Override
	public BaseResponse<String> insertUser(UserDto userDto) {
		try {
			int count = userMapper.selectUserByStaffNo(userDto); // 判断数据库中是否有相同工号的用户
			if (count != 0) {
				log.warn("执行insertUser失败,数据库中已经有该用户名");
				BaseResponse<String> response = new BaseResponse<>();
				response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
				response.setStatusMsg("工号已经重复！");
				return response;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			userDto.setCreator(userVo.getName());
			userDto.setCreateTime(nowTime);
			userDto.setPassword(MD5.md5(userDto.getPassword()));// 密码用MD5转换后再插入
			userMapper.insertUser(userDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("执行insertUser成功");
			return response;
		} catch (Exception e) {
			log.error("执行insertUser失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("执行insertUser失败");
			return response;
		}
	}

	/**
	 * 更新用户
	 */
	@Override
	public BaseResponse<String> updateUser(UserDto userDto) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			userDto.setUpdator(userVo.getName());
			userDto.setUpdateTime(nowTime);
			userDto.setPassword(MD5.md5(userDto.getPassword()));// 密码用MD5转换后再插入
			userMapper.updateUser(userDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("执行updateUser成功");
			return response;
		} catch (Exception e) {
			log.error("执行updateUser失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("执行updateUser失败");
			return response;
		}
	}

	/**
	 * 用户单选删除
	 */
	@Override
	public BaseResponse<String> deleteUser(Long id) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			StringBuilder canNotDelete = new StringBuilder("");// 实例一个StringBuilder对象用于拼接
			if (id.longValue() != userVo.getId().longValue()) { // 只删除非当前操作用户，注意Long的比较
				UserDto userDto = new UserDto();
				SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
				String nowTime = sdf.format(new Date());
				userDto.setUpdator(userVo.getStaffNo());
				userDto.setUpdateTime(nowTime);
				userDto.setId(id);
				userMapper.deleteUser(userDto); // 对传入的id进行删除操作
				BaseResponse<String> response = new BaseResponse<>();
				response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
				response.setStatusMsg("deleteUser单选删除成功");
				return response;
			} else {
				canNotDelete.append(userVo.getStaffNo()).append(",是当前操作用户，不可删除");
				BaseResponse<String> response = new BaseResponse<>();
				response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
				response.setStatusMsg("deleteUser单选删除失败" + canNotDelete);
				return response;
			}
		} catch (Exception e) {
			log.error("deleteUser单选删除失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("deleteUser单选删除失败");
			return response;
		}
	}

	/**
	 * 用户修改密码
	 */
	@Override
	public BaseResponse<String> passwordUpdate(UserDto userDto) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			userDto.setStaffNo(userVo.getStaffNo()); // 获取工号，修改对应工号的密码
			userDto.setUpdator(userVo.getName());
			userDto.setUpdateTime(nowTime);
			userDto.setOldPassword(MD5.md5(userDto.getOldPassword()));// 旧密码加密后与session中密码进行比较
			userDto.setPassword(MD5.md5(userDto.getNewPassword()));// 入参MD5加密
			if (userDto.getOldPassword().equals(userVo.getPassword())) {// 判断入参和当前session中密码是否一致
				userMapper.passwordUpdate(userDto);// 用户修改密码
				BaseResponse<String> response = new BaseResponse<>();
				response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
				response.setStatusMsg("执行passwordUpdate成功");
				return response;
			} else {
				BaseResponse<String> response = new BaseResponse<>();
				response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
				response.setStatusMsg("执行passwordUpdate失败,与修改前密码不一致");
				return response;
			}
		} catch (Exception e) {
			log.error("执行passwordUpdate失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("执行passwordUpdate失败");
			return response;
		}
	}

	/**
	 * 用户批量删除
	 */
	@Override
	public BaseResponse<String> bacthDeleteUser(Long[] ids) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			UserDto userDto = new UserDto();
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			StringBuilder canNotDelete = new StringBuilder("");// 实例一个StringBuilder对象用于拼接
			for (Long id : ids) {
				if (id.longValue() != userVo.getId().longValue()) { // 只删除非当前操作用户，注意Long的比较
					userDto.setUpdator(userVo.getStaffNo());
					userDto.setUpdateTime(nowTime);
					userDto.setId(id);
					userMapper.deleteUser(userDto); // 将Long数组遍历然后对每一个id进行删除操作
				} else {
					canNotDelete.append(userVo.getStaffNo()).append(",是当前操作用户，不可删除");
				}
			}
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("批量删除完成" + canNotDelete);
			return response;
		} catch (Exception e) {
			log.error("batchDeleteInventory批量删除失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("批量删除失败");
			return response;
		}
	}
}
