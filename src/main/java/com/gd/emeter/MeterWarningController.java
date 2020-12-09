package com.gd.emeter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.mapper.MeterWarningMapper;
import com.gd.model.po.MeterWarning;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;

/**
 *
 */
@Controller
@RequestMapping("/meterWarning")
public class MeterWarningController{
	
	@Autowired
	private MeterWarningMapper meterWarningMapper;
	
	@RequestMapping("/toListMeterWarning")
	public String toListMeterWarning(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterWarning";
	}
	
	
	/**
	 * 分页查询
	 * @param params
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/paging")
	@ResponseBody
	public Map paging(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<MeterWarning> list = meterWarningMapper.selectList(params);
    	for(MeterWarning meterWarning:list){
    		String description=meterWarning.getDescription();
    		switch(description){
    		case "power_cut":
    			meterWarning.setDescription("断电");break;
    		case "B1":
    			meterWarning.setDescription("断电");break;
    		case "B3":
    			meterWarning.setDescription("拉合闸");break;
    		case "B5":
    			meterWarning.setDescription("过流");break;
    		case "B7":
    			meterWarning.setDescription("过压");break;
    		case "B9":
    			meterWarning.setDescription("欠压");break;
    		}
    		
    	}
        map.put("rows", list);
        map.put("total", meterWarningMapper.countByCondition(params));
        return map;
	}
	
	@RequestMapping("/get24HrWarning")
	@ResponseBody
	public Map get24HrWarning() {
		Map<String,Object> map=new HashMap<String,Object>();
		List<MeterWarning> list= meterWarningMapper.select10Warning();
		Integer num=19;
		map.put("list", list);
		map.put("num", num);
		
        return map;
	}
	

}
