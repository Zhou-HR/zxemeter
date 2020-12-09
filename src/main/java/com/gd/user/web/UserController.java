package com.gd.user.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.basic.crud.CrudService;
import com.gd.basic.crud.FilterRule;
import com.gd.basic.crud.FilterRule.RuleOp;
import com.gd.basic.crud.Message;
import com.gd.basic.crud.QueryInfo;
import com.gd.common.Update;
import com.gd.mapper.MeterWarningMapper;
import com.gd.model.po.MeterProperty;
import com.gd.model.po.User;
import com.gd.user.service.UserService;

/**
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MeterWarningMapper meterWarningMapper;
    
    private CrudService crudService = CrudService.of(User.class);

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable int id, @RequestParam String forward, Model model) {
        model.addAttribute("entity", userService.findById(id));
        model.addAttribute("roleIds", userService.getRoleIdsByUser(id));
        model.addAttribute("roleMenuIds", userService.getMenuRoleIdsByUser(id));
        return forward;
    }

    @RequestMapping("/remove/{id}")
    @ResponseBody
    public Message remove(@PathVariable int id) {
        userService.remove(id);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(String roles,String wechats, @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
        userService.save(user, roles,wechats);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Message edit(String roles,String menuRoles,@Validated({Update.class, Default.class}) User user, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
        userService.edit(user, roles,menuRoles);
        return Message.SUCCESS;
    }
    
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public Message changePwd(String oldPassword,String newPassword, HttpServletRequest request) {
        String encodedPassword=userService.getEncodePassword(oldPassword);
        HttpSession session = request.getSession();
        User user=(User) session.getAttribute("user");
        if(!encodedPassword.equals(user.getPassword())){
        	return new Message(false, "密码错误!");
        }
        user.setPassword(userService.getEncodePassword(newPassword));
        userService.changePwd(user);
        return Message.SUCCESS;
    }
    
    @RequestMapping(value = "/setEmail", method = RequestMethod.POST)
    @ResponseBody
    public Message setEmail(String email,String everyHour,String hour, HttpServletRequest request) {
    	HttpSession session = request.getSession();
        User user=(User) session.getAttribute("user");
        user.setEmail(email);
        user.setEveryHour(everyHour);
        user.setHour(hour);
        crudService.update(user);
        
        return Message.SUCCESS;
    }
    
    @RequestMapping(value = "/paging1", method = RequestMethod.POST)
    @ResponseBody
    public Map paging1(@RequestBody QueryInfo queryInfo) {
    	List<FilterRule> lstFilterRule=queryInfo.getRules();
    	FilterRule rule=new FilterRule("name",RuleOp.notequal,"admins");
    	if(lstFilterRule==null)
    		lstFilterRule=new ArrayList<FilterRule>();
    	lstFilterRule.add(rule);
    	rule=new FilterRule("status",RuleOp.equal,"0");
    	lstFilterRule.add(rule);
    	queryInfo.setRules(lstFilterRule);
    	queryInfo.setSort("id");
        HashMap<Object, Object> map = new HashMap<>(2);
        map.put("total", crudService.count(queryInfo.getRules()));
        map.put("rows", crudService.paging(queryInfo));

        return map;
    }
    
    @RequestMapping("/toListEmail")
	public String toListEmail(HttpServletRequest request) {
    	HttpSession session = request.getSession();
        User user=(User) session.getAttribute("user");
        request.setAttribute("user", user);
		return "jsp/security/email";
	}
    
    @RequestMapping("/toListMeterProperty")
	public String toListMeterProperty(HttpServletRequest request) {
    	List<MeterProperty> listMeterProperty= meterWarningMapper.getMeterProperty();
    	for(MeterProperty meterProperty:listMeterProperty){
			if("电压".equals(meterProperty.getName())){
				String maxVoltage=meterProperty.getValue();
				request.setAttribute("maxVoltage", maxVoltage);
			}
				
			if("电量".equals(meterProperty.getName())){
				String maxEvalue=meterProperty.getValue();
				request.setAttribute("maxEvalue", maxEvalue);
			}
		}
    	
		return "jsp/security/meter-listProperty";
	}
    
    @RequestMapping(value = "/setMeterProperty", method = RequestMethod.POST)
    @ResponseBody
    public Message setMeterProperty(String maxVoltage,String maxEvalue, HttpServletRequest request) {
    	meterWarningMapper.setMeterProperty(1,maxVoltage);
    	meterWarningMapper.setMeterProperty(2,maxEvalue);
        return Message.SUCCESS;
    }
}
