package com.appliance.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;
import com.appliance.service.DemoService;
import com.appliance.utils.ExportPOIUtils;
import com.appliance.utils.MD5;

@RestController
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private DemoService DemoService;

	/**
	 * 测试MD5转换和返回字符串
	 * @return
	 */
	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = { "application/json" })
	public String helloWorld() {
		String md5 = MD5.md5("Hello World!");// 测试MD5
		return md5;
	}

	/**
	 * 测试返回json
	 * @return
	 */
	@RequestMapping(value = "/demoToString", method = RequestMethod.GET, produces = { "application/json" })
	public String demoToString() {
		DemoPojo demopojo = new DemoPojo();
		demopojo.setUser("admin");
		demopojo.setPassword(MD5.md5("123456"));
		String demoPojotoString = JSONObject.toJSONString(demopojo);// 转为json格式
		return demoPojotoString;
	}

	/**
	 * 测试连接数据库并获取值
	 * @param httpSession
	 * @param DemoDto
	 * @return
	 */
	@RequestMapping(value = "/demoLogin", method = RequestMethod.POST, produces = { "application/json" })
	public String demoLogin(HttpSession httpSession, @RequestBody DemoDto DemoDto) {
		int i = DemoService.demoLogin(DemoDto);
		if (i == 1) {
			String login = "登陆成功";
			return login;
		}
		String login = "登陆失败";
		return login;
	}

	/**
	 * 测试POI导出功能
	 * @param httpSession
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/demoExecl")
	public String demoExecl(HttpSession httpSession,HttpServletResponse response) throws IOException {
		DemoPojo demopojo = new DemoPojo();
		demopojo.setUser("admin");
		demopojo.setPassword(MD5.md5("123456"));
		List<DemoPojo> listDemopojo = new ArrayList<>();
		listDemopojo.add(demopojo);
		String fileName = "账户密码"; 
		// 列名
		String columnNames[] = { "用户","密码" };
		// map中的key
		String keys[] = { "user","password" };
		ExportPOIUtils.start_download(response, fileName, listDemopojo, columnNames, keys);
		return "导出出现问题，请检查导出工具";
	}
}
