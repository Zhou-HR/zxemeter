package com.gd.user.web;

import com.gd.basic.crud.CrudService;
import com.gd.basic.crud.FilterRule;
import com.gd.basic.crud.Message;
import com.gd.basic.crud.QueryInfo;
import com.gd.basic.crud.FilterRule.RuleOp;
import com.gd.basic.security.SecurityMetadataSourceWrapper;
import com.gd.common.Update;
import com.gd.mapper.PermissionMapper;
import com.gd.model.po.Permission;
import com.gd.model.po.PermissionMenu;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.groups.Default;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    private CrudService crudService = CrudService.of(Permission.class);

    private CrudService crudMenuService = CrudService.of(PermissionMenu.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(@Valid Permission permission, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
//        int i = crudService.insertAndRtnKey(permission);
        int i = 0;
        permissionMapper.insert(permission);
        SecurityMetadataSourceWrapper.refresh();
        return new Message(true, String.valueOf(i));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Message edit(@Validated({Update.class, Default.class}) Permission permission, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
        crudService.update(permission);
        SecurityMetadataSourceWrapper.refresh();
        return new Message(true, String.valueOf(permission.getId()));
    }

    @RequestMapping("/all")
    @ResponseBody
    public List<Permission> all() {
        QueryInfo queryInfo = new QueryInfo().orderBy("parentId,ordered", "asc, asc");
        List<FilterRule> lstFilterRule = new ArrayList<FilterRule>();
        FilterRule rule = new FilterRule("id", RuleOp.greater, 22);
        lstFilterRule.add(rule);
        queryInfo.setRules(lstFilterRule);
        return crudService.paging(queryInfo);
    }

    @RequestMapping("/menuAll")
    @ResponseBody
    public List<Permission> menuAll() {
        QueryInfo queryInfo = new QueryInfo().orderBy("parentId,ordered", "asc, asc");
//    	List<FilterRule> lstFilterRule=new ArrayList<FilterRule>();
//    	FilterRule rule=new FilterRule("id",RuleOp.greater,22);
//    	lstFilterRule.add(rule);
//    	queryInfo.setRules(lstFilterRule);
        return crudMenuService.paging(queryInfo);
    }

    @RequestMapping("/getStation")
    @ResponseBody
    public List<Project> getStation(String id) {
//    	Map<String,List<Project>> map=InitLoadToRedis.getMapCompanyProject();
        List<Project> list = new ArrayList<Project>();
//    	if(list.size()>0)
//    		System.out.println("emeter "+id+" first project meter status is "+list.get(0).getMeterStatus1());
        //TODO 调试时用，正式中要去掉
//    	if(list==null) return list;
//    	if(list.size()>2){
//    		List<Project> listNew=list.subList(0, 2);
//    		return listNew;
//    	}
        return list;
    }

    @RequestMapping("/remove")
    @ResponseBody
    @Transactional
    public Message remove(String ids) {
        ids = StringUtils.removeStart(ids, ",");
        permissionMapper.delete(ids);
        permissionMapper.deleteRolePermission(ids);
        SecurityMetadataSourceWrapper.refresh();
        return Message.SUCCESS;
    }
}
