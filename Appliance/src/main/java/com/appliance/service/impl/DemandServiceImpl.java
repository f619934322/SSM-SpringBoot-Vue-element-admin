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

import com.appliance.mapper.DemandMapper;
import com.appliance.mapper.InventoryMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.DemandVo;
import com.appliance.pojo.vo.InventoryVo;
import com.appliance.pojo.vo.UserVo;
import com.appliance.service.DemandService;
import com.appliance.utils.DictionaryEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemandServiceImpl implements DemandService {

	private static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";// sonar要求同一字符串不得出现两次或以上，必须使用静态最终成员定义

	@Autowired
	private DemandMapper demandMapper;

	@Autowired
	private InventoryMapper inventoryMapper;

	/**
	 * 时间范围数组判断公用方法
	 * 
	 * @param demandDto
	 */
	public void timeBeginToEnd(DemandDto demandDto) {
		if (demandDto.getCreateTimeBeginToEnd() != null && demandDto.getCreateTimeBeginToEnd().length != 0) {// 要判断日期数组是否不为空
			// 从数组中分离出起始和结束日期
			String[] createTimeBeginAndEnd = demandDto.getCreateTimeBeginToEnd();
			String createTimeBegin = createTimeBeginAndEnd[0];
			String createTimeEnd = createTimeBeginAndEnd[1];
			// 将日期赋值给对象
			demandDto.setCreateTimeBegin(createTimeBegin);
			demandDto.setCreateTimeEnd(createTimeEnd);
		}
	}

	/**
	 * 采购需求新增
	 */
	@Transactional
	@Override
	public BaseResponse<String> insertNewDemand(DemandDto demandDto) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			demandDto.setCreator(userVo.getName());
			demandDto.setCreateTime(nowTime);
			demandMapper.insertNewDemand(demandDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("insertNewDemand插入成功");
			return response;
		} catch (Exception e) {
			log.error("insertNewDemand失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("insertNewDemand插入失败");
			return response;
		}
	}

	/**
	 * 采购申请补充
	 */
	@Transactional
	@Override
	public BaseResponse<String> insertSupplementDemand(DemandDto demandDto) {
		try {
			/* 这里将缓存中的工号取出来，并取当前时间，最后赋值给对象并传给Mapper方法 */
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			demandDto.setCreator(userVo.getName());
			demandDto.setCreateTime(nowTime);
			demandMapper.insertSupplementDemand(demandDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("insertSupplementDemand插入成功");
			return response;
		} catch (Exception e) {
			log.error("insertSupplementDemand失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("insertSupplementDemand插入失败");
			return response;
		}
	}

	/**
	 * 获取审核清单
	 */
	@Override
	public BaseResponse<PageInfo<DemandVo>> demandList(DemandDto demandDto) {
		try {
			timeBeginToEnd(demandDto);
			// 执行分页查询
			PageHelper.startPage(demandDto.getPageNum(), demandDto.getPageSize());
			List<DemandVo> inventoryList = demandMapper.demandList(demandDto);
			PageInfo<DemandVo> pageInfo = new PageInfo<>(inventoryList);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取demandList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取demandList数据失败,信息：", e);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取demandList数据失败");
			return response;
		}
	}

	/**
	 * 采购审核操作
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Override
	public BaseResponse<String> reviewDemand(DemandDto demandDto) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			/*
			 * 当审核状态为4时且需求标识为1时，执行如下步骤：
			 * 
			 * 1.库存表新增一条数据，入参来自于demandDto，给inventoryDto的成员赋值；
			 * 2.用入参对象的需求表id查出唯一一条库存表id，用inventoryVo保留；
			 * 3.根据2中得到的库存id和demandDto中的id更新需求表对应的数据。
			 */
			if (demandDto.getStatus() == 4) {
				if (demandDto.getAddedFlag() == 1) {// 需求标识为1时为新增，执行上述步骤
					InventoryDto inventoryDto = new InventoryDto();
					inventoryDto.setItemName(demandDto.getItemName());
					inventoryDto.setCommit(demandDto.getReviewCommit());
					inventoryDto.setItemCount(demandDto.getItemCount());
					inventoryDto.setItemType(demandDto.getItemType());
					inventoryDto.setDemandId(demandDto.getId());
					inventoryDto.setCreator(userVo.getName());
					inventoryDto.setCreateTime(nowTime);
					log.info("执行reviewDemand更新审核状态，新增");
					demandDto.setReviewer(userVo.getName());
					demandDto.setReviewTime(nowTime);
					demandMapper.reviewDemand(demandDto);
					log.info("执行insertNewInventory插入一条新的库存表数据");
					inventoryMapper.insertNewInventory(inventoryDto);
					log.info("执行selectInventoryByDemandId获取唯一一条库存数据");
					InventoryVo inventoryVo = inventoryMapper.selectInventoryByDemandId(demandDto.getId());
					demandDto.setInventoryId(inventoryVo.getId());
					demandDto.setUpdator(userVo.getName());
					demandDto.setUpdateTime(nowTime);
					log.info("执行updateDemandInventoryId更新需求表中的库存表ID外键");
					demandMapper.updateDemandInventoryId(demandDto);
				} else {// 否则直接更新库存表数量
					log.info("执行selectInventoryById查询唯一的库存表数据");
					InventoryVo inventoryVo = inventoryMapper.selectInventoryById(demandDto.getInventoryId());
					int itemCount = demandDto.getItemCount() + inventoryVo.getItemCount();
					InventoryDto inventoryDto = new InventoryDto();
					inventoryDto.setId(demandDto.getInventoryId());
					inventoryDto.setItemCount(itemCount);
					demandDto.setReviewer(userVo.getName());
					demandDto.setReviewTime(nowTime);
					inventoryDto.setUpdator(userVo.getName());
					inventoryDto.setUpdateTime(nowTime);
					log.info("执行reviewDemand更新审核状态等，补充");
					demandMapper.reviewDemand(demandDto);
					log.info("执行updateInventoryForitemCount更新补充的库存数量");
					inventoryMapper.updateInventoryForitemCount(inventoryDto);
				}
			} else {// 如果审核标识不为4，则正常更新，不对库存表做操作
				demandDto.setReviewer(userVo.getName());
				demandDto.setUpdator(userVo.getName());
				demandDto.setReviewTime(nowTime);
				demandDto.setUpdateTime(nowTime);
				log.info("执行reviewDemand更新审核状态等，采购未完成");
				demandMapper.reviewDemand(demandDto);
			}

			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("reviewDemand操作成功");
			return response;
		} catch (Exception e) {
			log.error("reviewDemand失败,信息：", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("reviewDemand操作失败");
			return response;
		}
	}

	/**
	 * 导出需求表
	 */
	@Override
	public BaseResponse<List<DemandVo>> excelDemand(DemandDto demandDto) {
		try {
			List<DemandVo> inventoryList = demandMapper.demandList(demandDto);
			BaseResponse<List<DemandVo>> response = new BaseResponse<>();
			response.setResponseData(inventoryList);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("excelDemand导出成功");
			return response;
		} catch (Exception e) {
			log.error("执行excelDemand异常，信息：", e);
			BaseResponse<List<DemandVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("excelDemand导出失败");
			return response;
		}
	}

	/**
	 * 我的采购
	 */
	@Override
	public BaseResponse<PageInfo<DemandVo>> myDemand(DemandDto demandDto) {
		try {
			timeBeginToEnd(demandDto);
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			demandDto.setCreator(userVo.getName());
			// 执行分页查询
			PageHelper.startPage(demandDto.getPageNum(), demandDto.getPageSize());
			List<DemandVo> demandList = demandMapper.demandList(demandDto);
			PageInfo<DemandVo> pageInfo = new PageInfo<>(demandList);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(DictionaryEnum.REQUEST_SUCCESS.getCode());
			response.setStatusMsg("获取myDemand数据成功");
			return response;
		} catch (Exception e) {
			log.error("执行myDemand异常，信息：", e);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setStatusCode(DictionaryEnum.REQUEST_FAILED.getCode());
			response.setStatusMsg("获取myDemand数据失败");
			return response;
		}
	}

}
