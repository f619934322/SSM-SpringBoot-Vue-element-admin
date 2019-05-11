package com.appliance.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appliance.mapper.InventoryMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.ApplyVo;
import com.appliance.pojo.vo.DemandVo;
import com.appliance.pojo.vo.InventoryVo;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.InventoryService;
import com.appliance.utils.DictionaryEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";// sonar要求同一字符串不得出现两次或以上，必须使用静态最终成员定义

	@Autowired
	private InventoryMapper inventoryMapper;

	/**
	 * 物品库存列表
	 */
	@Override
	public BaseResponse<PageInfo<InventoryVo>> inventoryList(InventoryDto inventoryDto) {
		try {
			// 执行分页查询
			PageHelper.startPage(inventoryDto.getPageNum(), inventoryDto.getPageSize());
			List<InventoryVo> inventoryList = inventoryMapper.inventoryList(inventoryDto);
			PageInfo<InventoryVo> pageInfo = new PageInfo<>(inventoryList);
			BaseResponse<PageInfo<InventoryVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取inventoryList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取inventoryList数据失败,信息：", e);
			BaseResponse<PageInfo<InventoryVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取inventoryList数据失败");
			return response;
		}
	}

	/**
	 * 物品批量删除
	 */
	@Transactional
	@Override
	public BaseResponse<String> batchDeleteInventory(Long[] ids) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			InventoryDto inventoryDto = new InventoryDto();
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			for (Long id : ids) {
				inventoryDto.setUpdator(userVo.getStaffNo());
				inventoryDto.setUpdateTime(nowTime);
				inventoryDto.setId(id);
				inventoryMapper.deleteInventory(inventoryDto); // 将Long数组遍历然后对每一个id进行删除操作
			}
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("批量删除成功");
			return response;
		} catch (Exception e) {
			log.error("batchDeleteInventory批量删除失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("批量删除失败");
			return response;
		}
	}

	/**
	 * 物品单选删除
	 */
	@Transactional
	@Override
	public BaseResponse<String> deleteInventory(Long id) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			InventoryDto inventoryDto = new InventoryDto();
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			inventoryDto.setUpdator(userVo.getStaffNo());
			inventoryDto.setUpdateTime(nowTime);
			inventoryDto.setId(id);
			inventoryMapper.deleteInventory(inventoryDto); // 对传入的id进行删除操作
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("deleteInventory单选删除成功");
			return response;
		} catch (Exception e) {
			log.error("deleteInventory单选删除失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("deleteInventory单选删除失败");
			return response;
		}
	}

	/**
	 * 对库存表进行编辑
	 */
	@Transactional
	@Override
	public BaseResponse<String> updateInventory(InventoryDto inventoryDto) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			inventoryDto.setUpdator(userVo.getName());
			inventoryDto.setUpdateTime(nowTime);
			inventoryMapper.updateInventory(inventoryDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("对库存表进行编辑成功");
			return response;
		} catch (Exception e) {
			log.error("updateInventory对库存表进行编辑失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("对库存表进行编辑失败");
			return response;
		}
	}

	/**
	 * 库存物品的采购详情
	 */
	@Override
	public BaseResponse<List<DemandVo>> inventoryDetailForDemand(Long id) {
		try {
			List<DemandVo> inventoryDetail = inventoryMapper.inventoryDetailForDemand(id);
			BaseResponse<List<DemandVo>> response = new BaseResponse<>();
			response.setResponseData(inventoryDetail);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取库存表详情成功");
			return response;
		} catch (Exception e) {
			log.error("inventoryDetailForDemand获取库存表详情失败,信息：", e);
			BaseResponse<List<DemandVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取库存表详情失败");
			return response;
		}
	}

	/**
	 * 库存领取详情
	 */
	@Override
	public BaseResponse<List<ApplyVo>> inventoryDetailForApply(Long id) {
		try {
			List<ApplyVo> inventoryDetail = inventoryMapper.inventoryDetailForApply(id);
			BaseResponse<List<ApplyVo>> response = new BaseResponse<>();
			response.setResponseData(inventoryDetail);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取库存表详情成功");
			return response;
		} catch (Exception e) {
			log.error("inventoryDetailForApply获取库存表详情失败,信息：", e);
			BaseResponse<List<ApplyVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取库存表详情失败");
			return response;
		}
	}

}
