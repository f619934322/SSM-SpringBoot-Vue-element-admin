package com.appliance.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appliance.mapper.DemandMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.DemandService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemandServiceImpl implements DemandService {

	@Autowired
	private DemandMapper demandMapper;

	/**
	 * 采购需求新增
	 */
	@Transactional
	@Override
	public BaseResponse<String> insertNewDemand(DemandDto demandDto) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			demandDto.setCreator(userVo.getStaffNo());
			demandDto.setCreateTime(nowTime);
			demandMapper.insertNewDemand(demandDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("insertNewDemand插入成功");
			return response;
		} catch (Exception e) {
			log.error("获取inventoryList数据失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("insertNewDemand插入失败");
			return response;
		}
	}

}
