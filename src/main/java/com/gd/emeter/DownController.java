package com.gd.emeter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.basic.crud.Message;
import com.gd.mapper.MeterMapper;
import com.gd.model.po.Meter;
import com.gd.model.po.MeterSendDownLog;
import com.gd.redis.InitLoadToRedis;
import com.gd.redis.JedisUtil;
import com.gd.util.HttpClientUtil;
import com.gd.util.PropertiesUtil;
import com.gdiot.ssm.entity.Project;
import com.gdiot.ssm.entity.User;
import com.google.gson.Gson;

/**
 *
 */
@Controller
@RequestMapping("/down")
public class DownController{
	
	private static String SENDCMD = (String) PropertiesUtil.getInstance().getProperty("interface.properties", "SENDCMD");
	
	@Autowired
	private MeterMapper meterMapper;
	
	
	@RequestMapping("/toListDownSend")
	public String toListDownSend(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterDownSend";
	}
	
	@RequestMapping("/toListMeterRealCheck")
	public String toListMeterRealCheck(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterRealCheck";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingDownSend")
	@ResponseBody
	public Map pagingDownSend(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<Meter> list = meterMapper.selectDownSendList(params);
        map.put("rows", list);
        map.put("total", meterMapper.countDownSendByCondition(params));
        return map;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingMeterRealCheck")
	@ResponseBody
	public Map pagingMeterRealCheck(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<Meter> list = meterMapper.selectMeterRealCheckList(params);
    	for(Meter meter:list){
    		String userCode=meter.getUserCode();
    		User user=InitLoadToRedis.getUser(userCode);
    		if(user!=null){
    			meter.setUserName(user.getRealname());
    		}
    		String projectNo=meter.getProjectNo();
    		Project project=InitLoadToRedis.getProject(projectNo);
    		if(project!=null)
    			meter.setProjectName(project.getProjectName());
    		//设置 公司 事业部
    		String companyId=meter.getCompanyId();
    		meter.setUnit(InitLoadToRedis.getCompanyName(companyId));
    		if(companyId!=null&&companyId.length()==4){
    			companyId=companyId.substring(0,2);
    			meter.setCompany(InitLoadToRedis.getCompanyName(companyId));
    		}
    	}
        map.put("rows", list);
        map.put("total", meterMapper.countMeterRealCheckByCondition(params));
        return map;
	}
	
	@RequestMapping("/editDown/{meterNo}")
    public String editopenclose(@PathVariable String meterNo, @RequestParam String forward, Model model,HttpServletRequest request) {
		Meter meter=meterMapper.getMeterByMeterNo(meterNo);
		model.addAttribute("entity", meter);
        return forward;
    }
	
	private MeterSendDownLog getMeterSendDownLog(HttpServletRequest request,String name,Map<String,String> param){
		MeterSendDownLog meterSendDownLog=new MeterSendDownLog();
		meterSendDownLog.setImei(param.get("imei").toString());
		meterSendDownLog.setName(name);
		Gson gson = new Gson();
		String json=gson.toJson(param);
		meterSendDownLog.setParam(json);
		String userCode=request.getSession().getAttribute("userCode").toString();
		meterSendDownLog.setUser1(userCode);
		
		return meterSendDownLog;
	}
	
	private final static String REDIS_KEY="down";
	private final static Integer TIME_OUT=60;
	private final static String MESSAGETIMEOUT="电表下发指令每分钟只能发一条！";
	
	private boolean timeOut(String meterNo){
		Object object=JedisUtil.get(meterNo+REDIS_KEY);
		if(object!=null) return true;
		return false;
	}
	
	private void setTime(String meterNo){
		JedisUtil.set(meterNo+REDIS_KEY,1,TIME_OUT);
	}
	
	//拉合闸
	@RequestMapping(value = "/openclose", method = RequestMethod.POST)
    @ResponseBody
    public Message openclose(String meterNo,String meterType,String imei,String optype,HttpServletRequest request) {
		Message message=new Message(true);
//		if(timeOut(meterNo)){
//			message.setMessage(MESSAGETIMEOUT);
//			return message;
//		}else
//			setTime(meterNo);
		Map<String,String> param = new HashMap<String,String>();
		if("lora".equals(meterType))
			meterType="lora_em";
		if("2g".equals(meterType))
			meterType="mqtt_2g";
		param.put("module_type",meterType);
		param.put("imei", imei);
		param.put("operate_type", "O");
		param.put("type", "82");
		if("广东雅达2g".equals(meterType)){
			param.put("type", "A822");
		}
		param.put("value", optype);
		
		MeterSendDownLog meterSendDownLog=getMeterSendDownLog(request,"openclose",param);
		meterSendDownLog.setMeterNo(meterNo);
		
		try{
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(StringUtils.isNotEmpty(result)&&(result.indexOf("succ")!=-1||result.indexOf("error\\\":0")!=-1))
				result="下发成功";
			if(result!=null)
				meterSendDownLog.setResult(result);
			if(!"下发成功".equals(result))
				result="下发失败";
//			else
//				result="超时";
//			if(StringUtils.isNotEmpty(result)){
//				if(result.indexOf("data")>0)
//					result="成功";
//			}
			message.setMessage(result);
			return message;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
			
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
        return message;
    }
	
	//设置上报时间
	@RequestMapping(value = "/setReportTime", method = RequestMethod.POST)
    @ResponseBody
    public Message setReportTime(String meterNo,String meterType,String imei,String time,HttpServletRequest request) {
		Message message=new Message(true);
		if(timeOut(meterNo)){
			message.setMessage(MESSAGETIMEOUT);
			return message;
		}else
			setTime(meterNo);
		
		//判断是不是广东2g
		if("广东雅达2g".equals(meterType)){
			Map<String,String> param = new HashMap<String,String>();
			param.put("module_type","gdyd_2g");
			param.put("imei", imei);
			param.put("operate_type", "W");
			param.put("type", "0A001005");
			param.put("value", time);
			
			message=new Message(true);
			try{
				
				String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
				if(result==null)
					result="超时";
				message.setMessage(result);
				if(result==null)
					return message;
//				Map result_map = JSONObject.parseObject(result);
			}catch(Exception e){
				e.printStackTrace();
				message.setMessage(e.getMessage());
				return message;
			}
		}
		
		//开工厂模式
		Map<String,String> param = new HashMap<String,String>();
		param.put("module_type","nb");
		param.put("imei", imei);
		param.put("operate_type", "O");
		param.put("type", "F0");
		param.put("value", "on");
		
		MeterSendDownLog meterSendDownLog=getMeterSendDownLog(request,"factory-mode",param);
		meterSendDownLog.setMeterNo(meterNo);
		
		message=new Message(true);
		try{
			
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(result!=null)
				meterSendDownLog.setResult(result);
			else
				result="超时";
			message.setMessage(result);
			if(result==null)
				return message;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
			return message;
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
		
		//设置上报时间
		param = new HashMap<String,String>();
		param.put("module_type","nb");
		param.put("imei", imei);
		param.put("operate_type", "W");
		param.put("type", "72");
		param.put("value", time);
		
		meterSendDownLog=getMeterSendDownLog(request,"set report time",param);
		meterSendDownLog.setMeterNo(meterNo);
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e1) {
		}
		
		message=new Message(true);
		try{
			
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(result!=null)
				meterSendDownLog.setResult(result);
			else
				result="超时";
			message.setMessage(result);
			return message;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
        return message;
    }
	
	//获取电表数据
	@RequestMapping(value = "/getMeterData", method = RequestMethod.POST)
    @ResponseBody
    public Message getMeterData(String meterNo,String meterType,String imei,String type,HttpServletRequest request) {
		Message message=new Message(true);
		if(timeOut(meterNo)){
			message.setMessage(MESSAGETIMEOUT);
			return message;
		}else
			setTime(meterNo);
		Map<String,String> param = new HashMap<String,String>();
		if("2g".equals(meterType))
			meterType="mqtt_2g";
		if("广东雅达2g".equals(meterType)){
			meterType="gdyd_2g";
			if("10".equals(type))
				type="8000";
			if("72".equals(type))
				type="0A001005";
		}
			
		param.put("module_type",meterType);
		param.put("imei", imei);
		param.put("operate_type", "R");
		param.put("type", type);
		param.put("value", "");
		
		MeterSendDownLog meterSendDownLog=getMeterSendDownLog(request,"getMeterData",param);
		meterSendDownLog.setMeterNo(meterNo);
		
		try{
			
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(result!=null)
				meterSendDownLog.setResult(result);
			else
				result="超时";
			message.setMessage(result);
			return message;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
        return message;
    }
	
	//电量实时查询
	@RequestMapping(value = "/getMeterRealCheck", method = RequestMethod.POST)
    @ResponseBody
    public Message getMeterRealCheck(String meterNo,String meterType,String imei,String type,HttpServletRequest request) {
		Message message=new Message(true);
		if(timeOut(meterNo)){
			message.setMessage(MESSAGETIMEOUT);
			return message;
		}else
			setTime(meterNo);
		Map<String,String> param = new HashMap<String,String>();
		param.put("module_type",meterType);
		param.put("imei", imei);
		param.put("operate_type", "R");
		param.put("type", "10");
		param.put("value", "");
		
		MeterSendDownLog meterSendDownLog=getMeterSendDownLog(request,"getMeterData",param);
		meterSendDownLog.setMeterNo(meterNo);
		
		try{
			
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(result!=null)
				meterSendDownLog.setResult(result);
			else
				result="超时";
			message.setMessage(result);
			return message;
//				Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
        return message;
    }
	
	//丢帧重传
	@RequestMapping(value = "/getLostReportData", method = RequestMethod.POST)
    @ResponseBody
    public Message getLostReportData(String meterNo,String meterType,String imei,String type,String seq,HttpServletRequest request) {
		Message message=new Message(true);
		if(timeOut(meterNo)){
			message.setMessage(MESSAGETIMEOUT);
			return message;
		}else
			setTime(meterNo);
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("module_type",meterType);
		param.put("imei", imei);
		param.put("operate_type", "D");
		param.put("type", type);
		param.put("value", seq);
		
		MeterSendDownLog meterSendDownLog=getMeterSendDownLog(request,"getLostReportData",param);
		meterSendDownLog.setMeterNo(meterNo);
		
//		Message message=new Message(true);
		try{
			
			String result = HttpClientUtil.postJson(SENDCMD, param, "utf8");
			if(result!=null)
				meterSendDownLog.setResult(result);
			else
				result="超时";
			message.setMessage(result);
			return message;
//			Map result_map = JSONObject.parseObject(result);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage(e.getMessage());
			meterSendDownLog.setResult(e.getMessage());
		}finally{
			meterMapper.insertDownLog(meterSendDownLog);
		}
        return message;
    }
	
	@RequestMapping("/getReport")
	@ResponseBody
	public String getReport(String imei,HttpServletRequest request) {
		String value=meterMapper.getReport(imei);
		if(value!=null) return value;
        return "null";
	}
}
