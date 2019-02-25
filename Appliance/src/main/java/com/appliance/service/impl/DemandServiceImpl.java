package com.appliance.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemandServiceImpl implements DemandService {

	@Autowired
	private DemandMapper demandMapper;

	@Autowired
	private InventoryMapper inventoryMapper;

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
			log.error("insertNewDemand失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			demandDto.setCreator(userVo.getStaffNo());
			demandDto.setCreateTime(nowTime);
			demandMapper.insertSupplementDemand(demandDto);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("insertSupplementDemand插入成功");
			return response;
		} catch (Exception e) {
			log.error("insertSupplementDemand失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
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
			PageHelper.startPage(demandDto.getPageNum(), demandDto.getPageSize());
			List<DemandVo> inventoryList = demandMapper.demandList(demandDto);
			PageInfo<DemandVo> pageInfo = new PageInfo<>(inventoryList);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setResponseData(pageInfo);
			response.setStatusCode(200);
			response.setStatusMsg("获取demandList数据成功");
			return response;
		} catch (Exception e) {
			log.error("获取demandList数据失败,信息{}", e);
			BaseResponse<PageInfo<DemandVo>> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("获取demandList数据失败");
			return response;
		}
	}

	/**
	 * 采购审核操作
	 */
	@Transactional
	@Override
	public BaseResponse<String> reviewDemand(DemandDto demandDto) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			Subject subject = SecurityUtils.getSubject();
			UserVo userVo = (UserVo) subject.getPrincipal();
			/*
			 * 当审核状态为4时，执行如下步骤： 1.库存表新增一天消息，入参来自于demandDto，给inventoryDto的成员赋值；
			 * 2.用入参对象的需求表id查出唯一一条库存表id，用inventoryVo保留 3.根据2中得到的库存id和demandDto中的id更新需求表对应的数据
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
					demandMapper.reviewDemand(demandDto);
					inventoryMapper.insertNewInventory(inventoryDto);
					InventoryVo inventoryVo = inventoryMapper.selectInventoryByDemandId(demandDto.getId());
					demandDto.setInventoryId(inventoryVo.getId());
					demandDto.setUpdator(userVo.getName());
					demandDto.setUpdateTime(nowTime);
					demandMapper.updateDemandInventoryId(demandDto);
				} else {// 否则直接更新库存表数量
					InventoryVo inventoryVo = inventoryMapper.selectInventoryById(demandDto.getInventoryId());
					int itemCount = demandDto.getItemCount() + inventoryVo.getItemCount();
					InventoryDto inventoryDto = new InventoryDto();
					inventoryDto.setId(demandDto.getInventoryId());
					inventoryDto.setItemCount(itemCount);
					inventoryDto.setUpdator(userVo.getName());
					inventoryDto.setUpdateTime(nowTime);
					demandMapper.reviewDemand(demandDto);
					inventoryMapper.updateInventoryForitemCount(inventoryDto);
				}
			} else {// 如果审核标识不为4，则正常更新，不对库存表做操作
				demandDto.setReviewer(userVo.getName());
				demandDto.setUpdator(userVo.getName());
				demandDto.setUpdateTime(nowTime);
				demandMapper.reviewDemand(demandDto);
			}

			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(200);
			response.setStatusMsg("reviewDemand操作成功");
			return response;
		} catch (Exception e) {
			log.error("reviewDemand失败,信息{}", e);
			BaseResponse<String> response = new BaseResponse<>();
			response.setStatusCode(201);
			response.setStatusMsg("reviewDemand操作失败");
			return response;
		}
	}

}
