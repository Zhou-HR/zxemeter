package com.gd.emeter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.mapper.MenuMapper;
import com.gd.mapper.PermissionMapper;
import com.gd.model.po.Menu;
import com.gd.model.po.Permission;

/**
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController{
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	@RequestMapping("/all")
	@ResponseBody
	public List all() {
		List<Menu> list=new ArrayList<Menu>();
//		Map<String,Object> map=new HashMap<String,Object>();
    	List<Menu> list1 = menuMapper.selectList();
    	for(Menu menu:list1){
    		if(menu.getCompanyId().length()==2){
    			list.add(menu);
    			Permission permission=new Permission();
    			//permissionMapper.insert(permission);
    		}
    			
    	}
    	for(Menu menu:list){
    		List<Menu> subList=new ArrayList<Menu>();
    		for(Menu subMenu:list1){
	    		if(subMenu.getCompanyId().length()==4){
	    			if(subMenu.getCompanyId().substring(0,2).equals(menu.getCompanyId())){
	    				subList.add(subMenu);
	    			}
	    		}
    		}
    		menu.setSubMenu(subList);	
    	}
//    	map.put("menu",list);
        return list;
	}

}
