package com.appliance.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.vo.DemandVo;
import com.appliance.service.DemandService;
import com.appliance.utils.ExportPOIUtils;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/appliance/demand")
public class DemandController {

	@Autowired
	private DemandService demandService;

	/**
	 * 采购需求新增
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/insertNewDemand", produces = { "application/json" })
	public String insertNewDemand(@RequestBody DemandDto demandDto) {// 新增采购需求，在需求表里新增一条数据，并且需求标识为1（新增）
		log.info("执行insertNewDemand");
		BaseResponse<String> response = demandService.insertNewDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertNewDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 采购申请补充
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/insertSupplementDemand", produces = { "application/json" })
	public String insertSupplementDemand(@RequestBody DemandDto demandDto) {// 补充采购需求，在需求表里新增一条数据，并且需求标识为0（补充）
		log.info("执行insertSupplementDemand");
		BaseResponse<String> response = demandService.insertSupplementDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("insertSupplementDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 获取审核清单
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/demandList", produces = { "application/json" })
	public String demandList(@RequestBody DemandDto demandDto) {
		log.info("执行demandList");
		BaseResponse<PageInfo<DemandVo>> response = demandService.demandList(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("demandList返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 采购审核操作
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/reviewDemand", produces = { "application/json" })
	public String reviewDemand(@RequestBody DemandDto demandDto) {
		log.info("执行reviewDemand");
		BaseResponse<String> response = demandService.reviewDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("reviewDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 导出需求表
	 * 
	 * @param demandDto
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/excelDemand", produces = { "application/json" })
	public String excelDemand(HttpServletResponse httpServletResponse, @RequestParam("status") int status)
			throws IOException {
		log.info("执行excelDemand");
		DemandDto demandDto = new DemandDto();
		demandDto.setStatus(status);
		demandDto.setAddedFlag(-1); // 因为mybaits判断用-1来认定未传参数，所以赋值-1
		BaseResponse<List<DemandVo>> response = demandService.excelDemand(demandDto);
		List<DemandVo> demandVoList = response.getResponseData();
		List<DemandVo> demandVoListForexcel = new ArrayList<>();
		for (DemandVo demandVo : demandVoList) {
			if (demandVo.getAddedFlag() == 0) {
				demandVo.setAddedFlagStr("需要补充");// 0代表补充
			} else {
				demandVo.setAddedFlagStr("需要新增");// 1代表新增
			}
			demandVoListForexcel.add(demandVo);// 由于excel导出需要看到采购标识是中文，而不是数字代号，所以这里遍历集合并给元素赋值
		}
		String fileName = "采购需求清单";
		// 列名
		String[] columnNames = { "ID", "物品名称", "需求标识", "采购需求数量", "物品类型", "采购金额", "审核状态", "采购需求原因", "采购需求提起人",
				"采购需求发起时间", "审核人", "审核时间", "审核备注" };
		// map中的key
		String[] keys = { "id", "itemName", "addedFlagStr", "itemCount", "itemType", "purchasePrice", "status",
				"commit", "creator", "createTime", "reviewer", "reviewTime", "reviewCommit" };
		ExportPOIUtils.startDownload(httpServletResponse, fileName, demandVoListForexcel, columnNames, keys);
		String resultToString = JSON.toJSONString(response);
		log.info("excelDemand返回的JSON: {}", resultToString);
		return resultToString;
	}

	/**
	 * 我的采购
	 * 
	 * @param demandDto
	 * @return
	 */
	@PostMapping(value = "/myDemand", produces = { "application/json" })
	public String myApply(@RequestBody DemandDto demandDto) {
		log.info("执行myDemand");
		BaseResponse<PageInfo<DemandVo>> response = demandService.myDemand(demandDto);
		String resultToString = JSON.toJSONString(response);
		log.info("myDemand返回的JSON: {}", resultToString);
		return resultToString;
	}
}
