package com.gdiot.ssm.action;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.gdiot.ssm.entity.WMCmdDataPo;
import com.gdiot.ssm.entity.WMCmdSendLogPo;
import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.entity.WMReadDataPo;
import com.gdiot.ssm.service.AsyncService;
import com.gdiot.ssm.service.IWMDataService;
import com.gdiot.ssm.udp.UDPClient2;
import com.gdiot.ssm.udp.UdpClient;
import com.gdiot.ssm.udp.UdpClientFactory;
import com.gdiot.ssm.udp.UdpSendTask;
import com.gdiot.ssm.util.UdpConfig;
import com.gdiot.ssm.util.WMUdpSendCmdsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/udp")
public class UDPController {
	
	@Autowired
	private AsyncService asyncService;
	
	@Autowired()
    private IWMDataService mIWMDataService;
	
	//延迟发送，放在待发送列表中，等待下一次发送
	@RequestMapping(value = "/udpAddDownCmd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> udpAddDownCmd(@RequestBody Map<String, String> params){
		String module_type=null;
		String wm_num = null;
		String type = null;
		String value = null;
		String operate_type = null;
		if(params!=null) {
			if(params.containsKey("module_type")) {//wm_switch
				module_type = params.get("module_type");
			}
			if(params.containsKey("dev_id")) {
				wm_num = params.get("dev_id");
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
		String content = WMUdpSendCmdsUtils.getCmdContent(wm_num, type, operate_type, value);
		String typeStr = "";
		switch(type) {
		case "wm_switch"://强制开关阀
			if("O".equals(operate_type)) {//执行，操作
				if("on".equals(value)) {
					typeStr = "强制开阀";//强制开阀
				}else if("off".equals(value)) {
					typeStr = "强制关阀";//强制关阀
				}else if("back".equals(value)) {
					typeStr = "强制撤消";//强制撤消
				}else if("free_on".equals(value)) {
					typeStr = "自由开阀";//自由开阀
				}else if("free_off".equals(value)) {
					typeStr = "自由关阀";//自由关阀
				}
			}else if("R".equals(operate_type)) {
				typeStr = "读阀控状态";//读阀控状态
			}
			break;
			default:
				break;
		}
		WMCmdDataPo mWMCmdDataPo = new WMCmdDataPo();
		mWMCmdDataPo.setWmNum(wm_num);
		mWMCmdDataPo.setCmdData(content);
		mWMCmdDataPo.setDownStatus("1");
		mWMCmdDataPo.setType(typeStr);
		mIWMDataService.insertCmdData(mWMCmdDataPo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("errno", 0);
		map.put("result", "已经放在待发送列表中，等待下一次上报后立即下发！");
		return map;
	}
	
	@RequestMapping(value = "/getWmData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getWmData(@RequestBody Map<String, String> params){
		String wm_num = null;
		int pageNo = 1;
		int pageSize = 20;
		if(params!=null) {
			if(params.containsKey("wm_num")) {
				wm_num = params.get("wm_num");
			}
			if (params.containsKey("pageNo")) {
				pageNo = Integer.valueOf(params.get("pageNo"));
			}
			if (params.containsKey("pageSize")) {
				pageSize = Integer.valueOf(params.get("pageSize"));
			}
		}
		List<WMDataPo> list = mIWMDataService.selectbyDevId(wm_num,pageNo,pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", pageSize);
		map.put("list", list);
		return map;
	}
	@RequestMapping(value = "/getWmReadData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getWmReadData(@RequestBody Map<String, String> params){
		String wm_num = null;
		int pageNo = 1;
		int pageSize = 20;
		if(params!=null) {
			if(params.containsKey("wm_num")) {
				wm_num = params.get("wm_num");
			}
			if (params.containsKey("pageNo")) {
				pageNo = Integer.valueOf(params.get("pageNo"));
			}
			if (params.containsKey("pageSize")) {
				pageSize = Integer.valueOf(params.get("pageSize"));
			}
		}
		List<WMReadDataPo> list = mIWMDataService.selectReadData(null,null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", pageSize);
		map.put("list", list);
		return map;
	}
	
	//即使下发，目前udp不支持
	@RequestMapping(value = "/udpDownCmd", method = RequestMethod.POST)
	@ResponseBody
	public String udpDownCmd(@RequestBody Map<String, String> params){
		String module_type=null;
		String wm_num = null;
		String type = null;
		String value = null;
		String operate_type = null;
		if(params!=null) {
			if(params.containsKey("module_type")) {//wm_switch
				module_type = params.get("module_type");
			}
			if(params.containsKey("dev_id")) {
				wm_num = params.get("dev_id");
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
		String content = WMUdpSendCmdsUtils.getCmdContent(wm_num, type, operate_type, value); 
		
		String typeStr = "";
		switch(type) {
		case "wm_switch"://强制开关阀
			if("O".equals(operate_type)) {//执行，操作
				if("on".equals(value)) {
					typeStr = "强制开阀";//强制开阀
				}else if("off".equals(value)) {
					typeStr = "强制关阀";//强制关阀
				}else if("back".equals(value)) {
					typeStr = "强制撤消";//强制撤消
				}else if("free_on".equals(value)) {
					typeStr = "自由开阀";//自由开阀
				}else if("free_off".equals(value)) {
					typeStr = "自由关阀";//自由关阀
				}
			}else if("R".equals(operate_type)) {
				typeStr = "读阀控状态";//读阀控状态
			}
			break;
			default:
				break;
		}
		
		List<WMDataPo> list = mIWMDataService.selectbyNum(wm_num);
		if(list != null && list.size()>0) {
			String ip = list.get(0).getSendIp();
			int port = list.get(0).getSendPort();
			log.info("get IP="+ip);
			log.info("get Port="+port);
			if(ip != null && port != 0) {
				try {
					UdpSendTask mUdpSendTask = UdpClientFactory.getInstance(null).getClient();
					if(mUdpSendTask != null) {
						mUdpSendTask.send(ip, port, content);
						log.info("---------------------------send cmd msg succ----------------------");
						//save to db
						WMCmdSendLogPo mWMCmdSendLogPo = new WMCmdSendLogPo();
						mWMCmdSendLogPo.setWmNum(wm_num);
						mWMCmdSendLogPo.setCmdData(content);
						mWMCmdSendLogPo.setDownIp(ip);
						mWMCmdSendLogPo.setDownPort(String.valueOf(port));
						mWMCmdSendLogPo.setType(typeStr);
						mIWMDataService.insertCmdSendLog(mWMCmdSendLogPo);
						return "content:"+content+"  send succ";
					}else {
						log.info("---------------------------send cmd msg fail----------------------");
						return "send cmd msg fail";
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				return "content:"+content+"  send fial";
			}
		}
		return "content:"+content;
	}
	
	 
	@RequestMapping(value = "/sendUDPMsg", method = RequestMethod.POST)
	@ResponseBody
	public String sendUDPMsg(@RequestBody Map<String, String> params){
		String msg = null;
		if(params!=null) {
			if(params.containsKey("msg")) {
				msg = params.get("msg");
			}
		}
		UdpClient udpClient = new UdpClient(msg);
		return msg;
	}
	
	@RequestMapping(value = "/sendUDPMsg2", method = RequestMethod.POST)
	@ResponseBody
	public String sendUDPMsg2(@RequestBody Map<String, String> params){
		String msg = null;
		if(params!=null) {
			if(params.containsKey("msg")) {
				msg = params.get("msg");
			}
		}
		UDPClient2 client = new UDPClient2();
//      Scanner scanner = new Scanner(System.in);
      //建立死循环，不断发送数据
//      String msg = scanner.nextLine();
      //打印响应的数据
        System.out.println(client.sendAndReceive(UdpConfig.SERVICE_IP,UdpConfig.PORT,msg));
        
		return msg;
	}
	@RequestMapping(value = "/sendUDPDownCmd", method = RequestMethod.POST)
	@ResponseBody
	public String sendUDPDownCmd(@RequestBody Map<String, String> params){
		String msg = null;
		String ip = null;
		int port = 0;
		if(params!=null) {
			if(params.containsKey("msg")) {
				msg = params.get("msg");
			}
			if(params.containsKey("ip")) {
				ip = params.get("ip");
			}
			if(params.containsKey("port")) {
				port = Integer.parseInt(params.get("port"));
			}
		}
		
		try {
			UdpSendTask mUdpSendTask = UdpClientFactory.getInstance(null).getClient();
			if(mUdpSendTask != null) {
				mUdpSendTask.send(ip, port, msg);
				log.info("---------------------------send cmd msg succ----------------------");
				return "send cmd msg succ";
			}else {
				log.info("---------------------------send cmd msg fail----------------------");
				return "send cmd msg fail";
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return msg;
	}
}
