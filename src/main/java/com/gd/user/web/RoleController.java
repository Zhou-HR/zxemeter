package com.gd.user.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gd.basic.crud.Message;
import com.gd.basic.crud.QueryInfo;
import com.gd.basic.crud.FilterRule.RuleOp;
import com.gd.common.Update;
import com.gd.model.po.Permission;
import com.gd.model.po.Role;
import com.gd.user.service.RoleService;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private CrudService crudService = CrudService.of(Role.class);

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable int id, @RequestParam String forward, Model model) {
        model.addAttribute("entity", roleService.findById(id));
        List<Permission> list = roleService.getPermissionByRoleId(id);
        for (int i = 0; i < list.size(); i++) {
            Permission permission = list.get(i);
            if (permission.getId() == 21) {
                list.remove(i);
                break;
            }
        }
        model.addAttribute("permissions", list);
        return forward;
    }

    @RequestMapping("/remove/{id}")
    @ResponseBody
    public Message remove(@PathVariable int id) {
        roleService.remove(id);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(String permissions, @Valid Role role, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
        roleService.save(role, permissions);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Message edit(String permissions, @Validated({Update.class, Default.class}) Role role, Errors errors) {
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }
        roleService.edit(role, permissions);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/paging1", method = RequestMethod.POST)
    @ResponseBody
    public Map paging1(@RequestBody QueryInfo queryInfo) {
        List<FilterRule> lstFilterRule = queryInfo.getRules();
        FilterRule rule = new FilterRule("name", RuleOp.notequal, "ROLE_ADMIN");
        if (lstFilterRule == null) {
            lstFilterRule = new ArrayList<FilterRule>();
        }
        lstFilterRule.add(rule);
        queryInfo.setRules(lstFilterRule);
        queryInfo.setSort("name");
        HashMap<Object, Object> map = new HashMap<>(2);
        map.put("total", crudService.count(queryInfo.getRules()));
        map.put("rows", crudService.paging(queryInfo));

        return map;
    }

    @RequestMapping(value = "/list1")
    @ResponseBody
    public List list() {
        FilterRule rule = new FilterRule("name", RuleOp.notequal, "ROLE_ADMIN");
        return crudService.list(rule);
    }
}
