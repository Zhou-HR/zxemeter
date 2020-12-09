package com.gd.emeter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gd.basic.crud.Message;
import com.gd.mapper.MeterMaintainMapper;
import com.gd.mapper.MeterMapper;
import com.gd.mapper.MeterPersonMapper;
import com.gd.mapper.NBMapper;
import com.gd.mapper.UserMapper;
import com.gd.model.po.Meter;
import com.gd.model.po.MeterMaintain;
import com.gd.model.po.MeterMaintainPic;
import com.gd.model.po.NBValue;
import com.gd.model.po.SimpleDict;
import com.gd.model.po.TbMeterInfo;
import com.gd.redis.InitLoadToRedis;
import com.gd.service.MeterService;
import com.gd.util.DateUtil;
import com.gd.util.DingDingUtil;
import com.gdiot.ssm.entity.Project;
import com.gdiot.ssm.entity.User;

/**
 *
 */
@Controller
@RequestMapping("/meter")
public class MeterController{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MeterMapper meterMapper;
	
	@Autowired
	private MeterService meterService;
	
	@Autowired
	private MeterPersonMapper meterPersonMapper;
	
	@Autowired
	private MeterMaintainMapper meterMaintainMapper;
	
	@Autowired
	private NBMapper nbMapper;
	
	@RequestMapping("/toListMeterInstall")
	public String toListMeterInstall(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterInstall";
	}
	
	@RequestMapping("/toListMeterLowValue")
	public String toListMeterLowValue( HttpServletRequest request) {
		return "jsp/emeter/listMeterLowValue";
	}
	
	@RequestMapping("/toListMeter")
	public String toListMeter(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeter";
	}
	
	@RequestMapping("/toListStation")
	public String toListStation(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listStation";
	}
	
	@RequestMapping("/toListMeterPerson")
	public String toListMeterPerson(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterPerson";
	}
	
	@RequestMapping("/toListMeterMaintainReply")
	public String toListMeterMaintainReply(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterMaintainReply";
	}
	
	@RequestMapping("/toListMeterMaintainCheck")
	public String toListMeterMaintainCheck(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
		request.setAttribute("meterNo", meterNo);
		return "jsp/emeter/listMeterMaintainCheck";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingMeterValueLow")
	@ResponseBody
	public Map pagingMeterValueLow(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		long t1=System.currentTimeMillis();
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
		List<NBValue> list=meterMapper.selectListMeterLowValue(params);
		int total=meterMapper.countMeterLowValueByCondition(params);
		
    	for(NBValue nbValue:list){
    		//设置 公司 事业部
    		String companyId=nbValue.getCompanyId();
    		if(StringUtils.isNotEmpty(companyId)){
    			nbValue.setUnit(InitLoadToRedis.getCompanyName(companyId));
    			companyId=companyId.substring(0,2);
    			nbValue.setCompany(InitLoadToRedis.getCompanyName(companyId));
    		}
    		String projectNo=nbValue.getProjectNo();
    		if(StringUtils.isNotEmpty(projectNo)){
    			Project project=InitLoadToRedis.getProject(projectNo);
    			if(project!=null)
    				nbValue.setProjectName(project.getProjectName());
    		}
    	}
    	
        map.put("rows", list);
        map.put("total", total);
        return map;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingMeterInstall")
	@ResponseBody
	public Map pagingMeterInstall(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<TbMeterInfo> list = meterMapper.selectTbMeterInfo(params);
        map.put("rows", list);
        map.put("total", meterMapper.countTbMeterInfo(params));
        return map;
	}
	
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
		//查询companyIds
		String companyIds=nbMapper.getCompanyIdsByUserId(userId);
		String[] arr=companyIds.split(",");
		if(StringUtils.isEmpty(companyIds))
			companyIds="('')";
		else
			companyIds="("+companyIds+")";
		params.put("companyIds", companyIds);
    	List<Meter> list = meterMapper.selectList(params);
    	for(Meter meter:list){
    		String userCode=meter.getUserCode();
    		String str=meter.getOldValue();
    		if(StringUtils.isNotEmpty(str)&&str.indexOf(".")==0)
    			meter.setOldValue("0"+str);
    		str=meter.getNewValue();
    		if(StringUtils.isNotEmpty(str)&&str.indexOf(".")==0)
    			meter.setNewValue("0"+str);
//    		User user=InitLoadToRedis.getUser(userCode);
//    		if(user!=null){
//    			meter.setUserName(user.getRealname());
//    		}
//    		String projectNo=meter.getProjectNo();
//    		Project project=InitLoadToRedis.getProject(projectNo);
//    		if(project!=null)
//    			meter.setProjectName(project.getProjectName());
    		//设置 公司 事业部
//    		String companyId=meter.getCompanyId();
//    		meter.setUnit(InitLoadToRedis.getCompanyName(companyId));
//    		if(companyId!=null&&companyId.length()==4){
//    			companyId=companyId.substring(0,2);
//    			meter.setCompany(InitLoadToRedis.getCompanyName(companyId));
//    		}
    	}
        map.put("rows", list);
        map.put("total", meterMapper.countByCondition(params));
        return map;
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingMeterPerson")
	@ResponseBody
	public Map pagingMeterPerson(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<Meter> list = meterPersonMapper.selectList(params);
    	
    	for(Meter meter:list){
    		String userCode=meter.getUserCode();
    		if(userCode!=null){
    			User user=InitLoadToRedis.getUser(userCode);
        		if(user!=null){
        			meter.setUserName(user.getRealname());
        		}
    		}
    		
    		//设置 公司 事业部
    		String companyId=meter.getCompanyId();
    		meter.setUnit(InitLoadToRedis.getCompanyName(companyId));
    		if(companyId!=null&&companyId.length()==4){
    			companyId=companyId.substring(0,2);
    			meter.setCompany(InitLoadToRedis.getCompanyName(companyId));
    		}
    	}
        map.put("rows", list);
        map.put("total", meterPersonMapper.countByCondition(params));
        return map;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pagingMeterMaintain")
	@ResponseBody
	public Map pagingMeterMaintain(@RequestBody(required = false) Map<String, Object> params,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();	//分页查询
		Integer page=Integer.valueOf(params.get("page").toString());
		Integer rows=Integer.valueOf(params.get("rows").toString());
		params.put("p1", (page-1)*rows);
		params.put("p2", (page)*rows);
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
    	List<MeterMaintain> list = meterMaintainMapper.selectList(params);
    	for(MeterMaintain meterMaintain:list){
    		meterMaintain.setDealResultShort(meterMaintain.getDealResult());
    		if(meterMaintain.getDealResult()!=null&&meterMaintain.getDealResult().length()>20)
    			meterMaintain.setDealResultShort(meterMaintain.getDealResult().substring(0,20)+"...");
    		User user=InitLoadToRedis.getUser(meterMaintain.getSender());
    		if(user!=null) meterMaintain.setSenderName(user.getRealname());
    		
    		user=InitLoadToRedis.getUser(meterMaintain.getReceiver());
    		if(user!=null) meterMaintain.setReceiverName(user.getRealname());
    		
    		user=InitLoadToRedis.getUser(meterMaintain.getDealer());
    		if(user!=null) meterMaintain.setDealerName(user.getRealname());
    		
    		//设置 公司 事业部
    		String companyId=meterMaintain.getCompanyId();
    		meterMaintain.setUnit(InitLoadToRedis.getCompanyName(companyId));
    		if(companyId!=null&&companyId.length()==4){
    			companyId=companyId.substring(0,2);
    			meterMaintain.setCompany(InitLoadToRedis.getCompanyName(companyId));
    		}
    	}
        map.put("rows", list);
        map.put("total", meterMaintainMapper.countByCondition(params));
        return map;
	}
	
	@RequestMapping("/detail/{id}")
    public String detail(@PathVariable int id, @RequestParam String forward, Model model) {
		Meter meter=meterPersonMapper.findById(id);
		model.addAttribute("entity", meter);
        return forward;
    }
	
	@RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, @RequestParam String forward, Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userCode=session.getAttribute("userCode").toString();
		MeterMaintain meterMaintain=meterPersonMapper.findMeterMaintainByMeterId(id,userCode,DateUtil.getToday2());
//		MeterMaintain meterMaintain=null;
		Meter meter=meterPersonMapper.findById(id);
		if(meterMaintain==null){
			
			meterMaintain=new MeterMaintain();
			meterMaintain.setMeterNo(meter.getMeterNo());
			
		}
		
		userCode=meter.getUserCode();
		User user=InitLoadToRedis.getUser(userCode);
		if(user!=null){
			meterMaintain.setReceiverName(user.getRealname());
		}
		meterMaintain.setSenderName(session.getAttribute("userName").toString());
		model.addAttribute("entity", meterMaintain);
        return forward;
    }
	
	@RequestMapping("/editMaintain/{id}")
    public String editMaintain(@PathVariable int id, @RequestParam String forward, Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userCode=session.getAttribute("userCode").toString();
		MeterMaintain meterMaintain=meterMaintainMapper.findById(id);
		List<MeterMaintainPic> list=meterMaintainMapper.selectMaintainPic(id);
		if(meterMaintain==null){
			Meter meter=meterPersonMapper.findById(id);
			meterMaintain=new MeterMaintain();
			meterMaintain.setMeterNo(meter.getMeterNo());
			userCode=meter.getUserCode();
			User user=InitLoadToRedis.getUser(userCode);
			if(user!=null){
				meterMaintain.setReceiverName(user.getRealname());
			}
		}else{
			userCode=meterMaintain.getReceiver();
			User user=InitLoadToRedis.getUser(userCode);
			if(user!=null){
				meterMaintain.setReceiverName(user.getRealname());
			}
		}
		meterMaintain.setSenderName(session.getAttribute("userName").toString());
		
		model.addAttribute("entity", meterMaintain);
		
		model.addAttribute("list", list);
        return forward;
    }
	
	@RequestMapping("/person")
	@ResponseBody
    public List<SimpleDict> person(int id) {
		List<SimpleDict> list=meterPersonMapper.findPersonById(id);
		
        return list;
    }
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(int id,String userCode) {
//        userService.save(user, roles,wechats);
		meterPersonMapper.updateMeterPerson(id, userCode);
        return Message.SUCCESS;
    }
	
	@RequestMapping(value = "/sendMaintain", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMaintain(MeterMaintain meterMaintain,HttpServletRequest request) {
//		String date=DateUtil.getToday2();
		HttpSession session = request.getSession();
		Meter meter=meterPersonMapper.findByMeterNo(meterMaintain.getMeterNo());
		meterMaintain.setReceiver(meter.getUserCode());
		meterMaintain.setProjectNo(meter.getProjectNo());
		if(meterMaintain.getId()==null){
			
//			meterMaintain.setSendDate(date);
			meterMaintain.setSender(session.getAttribute("userCode").toString());
			meterMaintain.setCompanyId(meter.getCompanyId());
			
			meterPersonMapper.insert(meterMaintain);
			
			//发送钉钉消息
			User user=InitLoadToRedis.getUser(meter.getUserCode());
			DingDingUtil.send(user.getName(),user.getDdUserId(), "您有新的维护工单，请登录智能电表系统查看！");
		}else{
			
			meterPersonMapper.updateReasonById(meterMaintain);
		}
		
        return Message.SUCCESS;
    }
	
	@RequestMapping(value = "/updateMaintainResult", method = RequestMethod.POST)
    @ResponseBody
    public Message updateMaintainResult(MeterMaintain meterMaintain,HttpServletRequest request) {
//		String date=DateUtil.getToday2();
		HttpSession session = request.getSession();
		meterMaintain.setDealer(session.getAttribute("userCode").toString());
		meterPersonMapper.updateResultById(meterMaintain);
		meterMaintainMapper.updateMaintainPicReset(meterMaintain.getId());
		String imgs=meterMaintain.getImgs();
		if(StringUtils.isNotEmpty(imgs)){
			imgs=imgs.substring(1);
			meterMaintainMapper.updateMaintainPic(imgs, meterMaintain.getId());
		}
		
		//发送钉钉消息
		User user=InitLoadToRedis.getUser(meterMaintain.getSender());
		DingDingUtil.send(user.getName(),user.getDdUserId(), "您有派单已完成，请登录智能电表系统查看！");
		
        return Message.SUCCESS;
    }
	
	@RequestMapping("/remove/{id}")
    @ResponseBody
    public Message  remove(@PathVariable("id") Integer id) {
		meterMapper.deleteById(id);
		return Message.SUCCESS;
	}
	
	@RequestMapping("/saveImport")
    @ResponseBody
    public synchronized Message  saveImport(MultipartFile file,HttpServletRequest request) throws IOException{
		Message message=new Message(false);
		HttpSession session = request.getSession();
		String userId=request.getSession().getAttribute("userId").toString();
		//获取用户权限
		List<String> listCompanyId=meterMapper.selectCompnayIdByUserId(Integer.valueOf(userId));
		if(listCompanyId.size()==0){
			message.setMessage("没有客户权限！");
			return message;
		}
		Map<String,Integer> mapCompnayId=new HashMap<String,Integer>();
		//用户权限
		for(String companyId:listCompanyId){
			mapCompnayId.put(companyId, 1);
		}
		
		List<Meter> listMeter=meterMapper.selectAllMeter();
		Map<String,Integer> mapMeterNo=new HashMap<String,Integer>();
		Map<String,Integer> mapMeterIMEI=new HashMap<String,Integer>();
		for(Meter meter:listMeter){
			mapMeterNo.put(meter.getMeterNo(), 1);
			mapMeterIMEI.put(meter.getImei(), 1);
		}
		
		int count=0;
		Workbook workbook=null;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			List<Meter> list=new ArrayList<Meter>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row=sheet.getRow(i);
				if(row==null||row.getCell(1)==null)
					break;
				for(int j=0;j<row.getLastCellNum();j++){
					if(row.getCell(j)!=null)
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
				}
					
				
				Meter meter=new Meter();
				
				String meterNo=row.getCell(1).getStringCellValue();
				if(StringUtils.isEmpty(meterNo)) break;
				
				//判断表号是否已经存在
				if(mapMeterNo.get(meterNo)==null)
					mapMeterNo.put(meterNo, 1);
				else{
					workbook.close();
					message.setMessage(meterNo+"表号已存在！");
					return message;
				}
				
				//判断客户是否正确 
				String name=row.getCell(0).getStringCellValue();
				if(StringUtils.isEmpty(meterNo)){
					workbook.close();
					message.setMessage(meterNo+"客户不能为空！");
					return message;
				}
				List<String> listImportCompanyId=meterMapper.selectCompnayIdByName(name);
				if(listImportCompanyId.size()!=1){
					workbook.close();
					message.setMessage(name+"不存在！");
					return message;
				}else{
					String companyId=listImportCompanyId.get(0);
					//判断用户是否有权限
					if(mapCompnayId.get(companyId)==null){
						workbook.close();
						message.setMessage("没有导入"+name+"的权限！");
						return message;
					}
					meter.setCompanyId(companyId);
				}
				
				String meterType=row.getCell(2).getStringCellValue();
				if(StringUtils.isEmpty(meterType)||(!"nb".equals(meterType)&&!"lora".equals(meterType)&&!"2g".equals(meterType))){
					workbook.close();
					message.setMessage(meterNo+"电表类型不正确！");
					return message;
				}
				
				String imei=row.getCell(3).getStringCellValue();
				if(StringUtils.isEmpty(imei)){
					workbook.close();
					message.setMessage(meterNo+"设备号或IMEI不能为空！");
					return message;
				}
				if(mapMeterIMEI.containsKey(imei)){
					workbook.close();
					message.setMessage(meterNo+"设备号或IMEI已经存在！");
					return message;
				}else{
					mapMeterIMEI.put(imei, 1);
				}
				
				String newValue=row.getCell(4).getStringCellValue();
				if(StringUtils.isEmpty(newValue)){
					workbook.close();
					message.setMessage("新表读数不能为空！");
					return message;
				}
				
				meter.setMeterNo(meterNo);
				meter.setMeterType(meterType);
				meter.setImei(imei);
				meter.setNewValue(newValue);
				if(row.getCell(5)!=null)
					meter.setOldMeterNo(row.getCell(5).getStringCellValue());
				if(row.getCell(6)!=null)
					meter.setOldValue(row.getCell(6).getStringCellValue());
				if(row.getCell(7)!=null)
					meter.setAddress(row.getCell(7).getStringCellValue());
				
				list.add(meter);
			}
			workbook.close();
			
			count=meterService.insertMeter(list);
			
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage("解析失败！");
			return message;
		}finally {
			if(workbook!=null) workbook.close();
		}
		
		message.setMessage("成功导入"+count+"条!");
		return message;
	}
	
	@RequestMapping("/updatePrice")
	@ResponseBody
    public Message updatePrice(String companyId,String price,HttpServletRequest request) {
		System.out.println(companyId);
		Map<String,Object> params=new HashMap<String,Object>();
//		if(StringUtils.isEmpty(companyId))
//			companyId="31";
		String userId=request.getSession().getAttribute("userId").toString();
		params.put("userId", userId);
		
		//查询companyIds
		String companyIds=nbMapper.getCompanyIdsByUserId(userId);
		String[] arr=companyIds.split(",");
		if(StringUtils.isEmpty(companyIds))
			companyIds="('')";
		else
			companyIds="("+companyIds+")";
		params.put("companyIds", companyIds);
		
		params.put("companyId", companyId);
		params.put("price", price);
		
		meterMapper.updateMeterPrice(params);
		
		return Message.SUCCESS;
    }

}
