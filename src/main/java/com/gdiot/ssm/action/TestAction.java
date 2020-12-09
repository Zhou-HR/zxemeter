package com.gdiot.ssm.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gdiot.ssm.lora.LoraClientFactory;
import com.gdiot.ssm.lora.LoraSendCmdsUtils;
import com.gdiot.ssm.service.INBYDEMeterDataService;
import com.gdiot.ssm.service.IXBEMDataService;
import com.gdiot.ssm.util.ResultObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestAction {
	
	@Autowired()
    @Qualifier("XBEMDataService")
	private IXBEMDataService mXBEMDataService;

	@RequestMapping(value ="/test", method = RequestMethod.GET)
	@ResponseBody
	public void test() {
		log.info("test===========@@@@@@@@@@@@@");
		log.info("测试页面");
		System.out.printf("测试页面");
	}
	
	@RequestMapping(value = "/getXBEMDataList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_em_data_list(@RequestBody Map<String, String> params){
		Map<String, Object> result= new HashMap<String, Object>();
    	String dev_id = null;
		String eNum = null;
		Long beginTime = null;
		Long endTime = null;
		String source = "";
		int pageNo = 1;
		int pageSize = 10;
		if(params!=null) {
			if(params.containsKey("dev_id")) {
				dev_id = params.get("dev_id");
			}
			if(params.containsKey("e_num")) {
				eNum = params.get("e_num");
			}
			if (params.containsKey("source")) {
				source = params.get("source");
			}
			try {
				if(params.containsKey("begin_time")) {
					beginTime = Long.valueOf(params.get("beginTime"));
				}
				if(params.containsKey("end_time")) {
					endTime = Long.valueOf(params.get("endTime"));
				}
				if (params.containsKey("pageNo")) {
					pageNo = Integer.valueOf(params.get("pageNo"));
				}
				if (params.containsKey("pageSize")) {
					pageSize = Integer.valueOf(params.get("pageSize"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("error", 500);
				result.put("msg","date format error!");
				return result;
			}
		}
		result = mXBEMDataService.selectList(dev_id,eNum,beginTime,endTime,source,pageNo,pageSize);
		result.put("error", 100);
		result.put("msg","success");
		return result;
	}

}
