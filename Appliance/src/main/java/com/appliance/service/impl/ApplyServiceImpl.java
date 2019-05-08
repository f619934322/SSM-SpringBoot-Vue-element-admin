package com.appliance.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.appliance.mapper.ApplyMapper;
import com.appliance.mapper.InventoryMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.ApplyDto;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.ApplyVo;
import com.appliance.pojo.vo.InventoryVo;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.ApplyService;
import com.appliance.utils.DictionaryEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplyServiceImpl implements ApplyService {

	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";// sonar要求同一字符串不得出现两次或以上，必须使用静态最终成员定义

	@Autowired
	private InventoryMapper inventoryMapper;

	@Autowired
	private ApplyMapper applyMapper;

	/**
	 * 时间范围数组判断公用方法
	 * 
	 * @param applyDto
	 */
	public void getTimeBeginToEnd(ApplyDto applyDto) {
		if (applyDto.getCreateTimeBeginToEnd() != null && applyDto.getCreateTimeBeginToEnd().length != 0) {// 要判断日期数组是否不为空
			// 从数组中分离出起始和结束日期
			String[] createTimeBeginAndEnd = applyDto.getCreateTimeBeginToEnd();
			String createTimeBegin = createTimeBeginAndEnd[0];
			String createTimeEnd = createTimeBeginAndEnd[1];
			// 将日期赋值给对象
			applyDto.setCreateTimeBegin(createTimeBegin);
			applyDto.setCreateTimeEnd(createTimeEnd);
		}
	}

	/**
	 * 获取申领表数据
	 */
	@Override
	public BaseResponse<PageInfo<ApplyVo>> applyList(ApplyDto applyDto) {
		try {
			getTimeBeginToEnd(applyDto);
			// 执行分页查询
			PageHelper.startPage(applyDto.getPageNum(), applyDto.getPageSize());
			List<ApplyVo> applyList = applyMapper.applyList(applyDto);
			PageInfo<ApplyVo> pageInfo = new PageInfo<>(applyList);
			BaseResponse<PageInfo<ApplyVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取applyList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取applyList数据失败,信息：", e);
			BaseResponse<PageInfo<ApplyVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取applyList数据失败");
			return response;
		}
	}

	/**
	 * 新增申领物品
	 */
	@Transactional
	@Override
	public BaseResponse<String> insertApply(ApplyDto applyDto) {
		try {
			// 插入一条物品申领数据
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			applyDto.setCreateTime(nowTime);
			applyDto.setCreator(userVo.getName());
			applyMapper.insertApply(applyDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("执行insertApply成功");
			return response;
		} catch (Exception e) {
			log.error("执行insertApply失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("执行insertApply失败");
			return response;
		}
	}

	/**
	 * 审核申领
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Override
	public BaseResponse<String> reviewApply(ApplyDto applyDto) {
		try {
			// 进行物品申领的审核操作
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			applyDto.setUpdateTime(nowTime);
			applyDto.setReviewTime(nowTime);
			applyDto.setReviewer(userVo.getName());
			applyDto.setUpdator(userVo.getName());
			applyMapper.reviewApply(applyDto);
			if (applyDto.getStatus() == 2) {
				InventoryVo inventoryVo = inventoryMapper.selectInventoryById(applyDto.getInventoryId());// 查出该条库存的数量
				int inventoryItemCount = inventoryVo.getItemCount();
				ApplyVo applyVo = applyMapper.selectApplyById(applyDto.getId());// 查出需求的数量
				int applyItemCount = applyVo.getItemCount();
				int newItemCount = inventoryItemCount - applyItemCount;// 算出经过领取后的数量
				InventoryDto inventoryDto = new InventoryDto();
				inventoryDto.setItemCount(newItemCount);
				inventoryDto.setId(inventoryVo.getId());
				inventoryDto.setItemName(inventoryVo.getItemName());
				inventoryDto.setUpdator(userVo.getName());
				inventoryDto.setUpdateTime(nowTime);
				inventoryMapper.updateInventory(inventoryDto);// 更新库存数量
			}
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("执行reviewApply成功");
			return response;
		} catch (Exception e) {
			log.error("执行reviewApply失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("执行reviewApply失败");
			return response;
		}
	}

	/**
	 * 我的申领
	 */
	@Override
	public BaseResponse<PageInfo<ApplyVo>> myApply(ApplyDto applyDto) {
		try {
			getTimeBeginToEnd(applyDto);
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			applyDto.setCreator(userVo.getName());
			// 执行分页查询
			PageHelper.startPage(applyDto.getPageNum(), applyDto.getPageSize());
			List<ApplyVo> applyList = applyMapper.applyList(applyDto);
			PageInfo<ApplyVo> pageInfo = new PageInfo<>(applyList);
			BaseResponse<PageInfo<ApplyVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取myApply数据成功");
			return response;
		} catch (Exception e) {
			BaseResponse<PageInfo<ApplyVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取myApply数据失败");
			return response;
		}
	}

}
