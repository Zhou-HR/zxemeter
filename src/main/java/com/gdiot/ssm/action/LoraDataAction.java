package com.gdiot.ssm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.lora.LoraSendCmds;
import com.gdiot.ssm.lora.LoraSendCmdsUtils;
import com.gdiot.ssm.service.AsyncService;
import com.gdiot.ssm.service.ILoraDataService;
import com.gdiot.ssm.service.IWMDataService;
import com.gdiot.ssm.task.DataSenderTask;
import com.gdiot.ssm.util.ResultObject;
import com.gdiot.ssm.util.SpringContextUtils;

@Controller
@RequestMapping(value = "/lora")
public class LoraDataAction {
	
	@Autowired()
	@Qualifier("LoraDataService")
	private ILoraDataService LoraDataService;
	
	@Autowired()
    private AsyncService asyncService;
	
	@Autowired()
    private IWMDataService mIWMDataService;
	
	/**
	 * 
	 * { "device_no":"010101",
	 *  "begin_time":"2018-5-22 11:08:20",
	 * "end_time":"2018-5-22 11:08:20" }
	 * 
	 * @param params
	 * @return
	 */

	@RequestMapping("/get_lora_data_list")
	@ResponseBody
	public ResultObject get_lora_data_list(@RequestBody Map<String, String> params/*,HttpServletResponse response*/) {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		String deviceNo = null;
		Long beginTime = null;
		Long endTime = null;
		String source = "00";
		int pageNo = 1;
		int pageSize = 20;
		if (params != null) {
			if (params.containsKey("device_no")) {
				deviceNo = params.get("device_no");
			}
			if (params.containsKey("source")) {
				source = params.get("source");
				if ("00".equals(source)) {
					source = null;
				}
			}
			try {
				if (params.containsKey("begin_time")) {
					beginTime = Long.valueOf(params.get("begin_time"));
				}
				if (params.containsKey("end_time")) {
					endTime = Long.valueOf(params.get("end_time"));
				}
				if (params.containsKey("pageNo")) {
					pageNo = Integer.valueOf(params.get("pageNo"));
				}
				if (params.containsKey("pageSize")) {
					pageSize = Integer.valueOf(params.get("pageSize"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultObject("500", "date format error!");
			}
		}
		return new ResultObject("100", "success",
				LoraDataService.listLoraData(deviceNo, beginTime, endTime,source,pageNo,pageSize));
	}
	
	@RequestMapping("/send_cmd")
	@ResponseBody
	public Map<String, Object> send_cmd(@RequestBody Map<String, String> params) {
		System.out.print("send_cmd---------------------------");
		String module_type=null;
		String dev_id = null;
		String type = null;
		String value = null;
		String operate_type = null;
		if(params!=null) {
			if(params.containsKey("module_type")) {
				module_type = params.get("module_type");
			}
			if(params.containsKey("dev_id")) {
				dev_id = params.get("dev_id");
			}
			if(params.containsKey("type")) {//wm_switch
				type = params.get("type");
			}
			if(params.containsKey("value")) {
				value = params.get("value");
			}
			if(params.containsKey("operate_type")) {
				operate_type = params.get("operate_type");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		String regex_dev ="^[A-Fa-f0-9]+$";//16 dev_eui
		String regex_imei ="^\\d{15}$";//imei 15
		String regex_yd_dev_id ="^\\d{9}$";//yd_dev_id 9
    	if( (dev_id.matches(regex_dev) && dev_id.length()==16)
    			|| dev_id.matches(regex_imei) 
    			|| dev_id.matches(regex_yd_dev_id)){
    		
    		String request_id = dev_id + "_" +System.currentTimeMillis();
			Map<String,String> map = new HashMap<String,String>();
			map.put("module_type", module_type);
			map.put("dev_id", dev_id);
			map.put("type", type);
			map.put("value", value);
			map.put("operate_type", operate_type);
			map.put("request_id", request_id);
//			DataSenderTask task = new DataSenderTask(map,module_type);
//			task.setAsyncService(asyncService);
//			asyncService.executeAsync(task);
			LoraSendCmdsUtils mLoraSendCmdsUtils = new LoraSendCmdsUtils();
			Map<String ,String> wmInfo = mLoraSendCmdsUtils.getEMInfoByDeveui(dev_id);
			String wm_num = wmInfo.get("wm_num");
			System.out.print("查询到的水表号：wm_num="+wm_num);
			if(wm_num != null) {
				String content = mLoraSendCmdsUtils.getCmdContent(wm_num, type, operate_type, value);
				if(content != null) {
					LoraSendCmds mLoraSendCmds = new LoraSendCmds();
		    		String resultData = mLoraSendCmds.sendMsgToWM(content, dev_id,request_id);
		    		result.put("data", resultData);
				}else {
					result.put("data", "Parameters error");
				}
			}else {
				result.put("data", "Parameters error");
			}
    	}else {
    		result.put("data", "Parameters error");
    	}
		return result;
	}
	@RequestMapping("/lora_wm_send_cmd")
	@ResponseBody
	public Map<String, Object> lora_wm_send_cmd(@RequestBody Map<String, String> params) {
		System.out.print("send_cmd---------------------------");
		String dev_id = null;
		String value = null;
		if(params!=null) {
			if(params.containsKey("dev_id")) {
				dev_id = params.get("dev_id");
			}
			if(params.containsKey("value")) {
				value = params.get("value");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		String regex_dev ="^[A-Fa-f0-9]+$";//16 dev_eui
		String regex_imei ="^\\d{15}$";//imei 15
		String regex_yd_dev_id ="^\\d{9}$";//yd_dev_id 9
    	if( (dev_id.matches(regex_dev) && dev_id.length()==16)
    			|| dev_id.matches(regex_imei) 
    			|| dev_id.matches(regex_yd_dev_id)){
    		
    		String request_id = dev_id + "_" +System.currentTimeMillis();
			LoraSendCmds mLoraSendCmds = new LoraSendCmds();
    		String resultData = mLoraSendCmds.sendMsgToWM(value, dev_id,request_id);
    		
    		result.put("data", resultData);
			return result;
    	}
		return result;
	}
	
	@RequestMapping("/get_wm_list")
	@ResponseBody
	public Map<String, Object> get_wm_list(@RequestBody Map<String, String> params) {
		System.out.print("send_cmd---------------------------");
		Map<String, Object> result = new HashMap<String, Object>();
		String dev_id = null;
		int pageNo = 1;
		int pageSize = 20;
		if(params!=null) {
			if(params.containsKey("dev_id")) {
				dev_id = params.get("dev_id");
			}
			try {
				if (params.containsKey("pageNo")) {
					pageNo = Integer.valueOf(params.get("pageNo"));
				}
				if (params.containsKey("pageSize")) {
					pageSize = Integer.valueOf(params.get("pageSize"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("data", "Parameters error");
			}
		}
		String regex_dev ="^[A-Fa-f0-9]+$";//16 dev_eui
		String regex_imei ="^\\d{15}$";//imei 15
		if(dev_id != null && dev_id != "") {
	    	if((dev_id.matches(regex_dev) && dev_id.length()==16)
	    			|| dev_id.matches(regex_imei)){
	    		if(mIWMDataService == null) {
					mIWMDataService =  SpringContextUtils.getBean(IWMDataService.class);
				}
				List<WMDataPo> list = mIWMDataService.selectbyDevId(dev_id,pageNo,pageSize);
				result.put("data", list);
	    	}else {
	    		result.put("data", "Parameters error");
	    	}
    	}else {
    		dev_id = null;
    		List<WMDataPo> list = mIWMDataService.selectbyDevId(dev_id,pageNo,pageSize);
			result.put("data", list);
    	}
		
    	
    	return result;
   }
}
