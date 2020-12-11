package com.gd.user.service;

import com.gd.mapper.PermissionMapper;
import com.gd.model.po.Permission;
import com.gd.model.vo.MenuNode;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public MenuNode getTopMenuByUserId(int userId) {
        List<Permission> permissions = permissionMapper.getMenuPermissionsByUserId(userId);
        MenuNode menuNode = new MenuNode();
        menuNode.setId(0);
        menuNode.setPId(-1);
        menuNode.setName("根节点");
        menuNode.setPath("");
        menuNode.setChild(getChild(0, permissions));

        return menuNode;
    }

    public MenuNode getMenuByUserId(int userId) {
        List<Permission> permissions = permissionMapper.getPermissionsByUserId(userId);
        MenuNode menuNode = new MenuNode();
        menuNode.setId(0);
        menuNode.setPId(-1);
        menuNode.setName("根节点");
        menuNode.setPath("");
        menuNode.setChild(getChild(0, permissions));

        return menuNode;
    }

    private List<MenuNode> getChild(int parentId, List<Permission> permissions) {
        List<MenuNode> child = null;
        for (Permission permission : permissions) {
            if (parentId != permission.getParentId()) {
                continue;
            }

            if (child == null) {
                child = new ArrayList<>();
            }
            MenuNode menuNode = new MenuNode();
            menuNode.setId(permission.getId());
            menuNode.setPId(permission.getParentId());
            menuNode.setName(permission.getName());
            menuNode.setPath(permission.getPath());
            menuNode.setIcon(permission.getIcon());
            menuNode.setCompanyId(permission.getCompanyId());
            menuNode.setChild(getChild(menuNode.getId(), permissions));
            child.add(menuNode);

//            String companyId=permission.getCompanyId();
//            if(companyId!=null&&companyId.length()==4){
//            	addMenuNodeStation(menuNode,companyId);
//            }

        }
        return child;
    }

    private void addMenuNodeStation(MenuNode menuNodeParent, String companyId) {
//    	Map<String,List<Project>> map=InitLoadToRedis.getMapCompanyProject();
        List<Project> list = InitLoadToRedis.getProjectList(companyId);
        List<MenuNode> child = new ArrayList<MenuNode>();
        menuNodeParent.setChild(child);
        int id = 40000;
        if (list != null) {
            for (Project project : list) {
//        		id++;
                MenuNode menuNode = new MenuNode();
                menuNode.setId(menuNodeParent.getId() + id);
                menuNode.setPId(menuNodeParent.getId());
                String projectName = project.getProjectName();
                if (projectName.length() > 6) {
                    projectName = projectName.substring(0, 6);
                }
                menuNode.setName(projectName);
                menuNode.setPath("");
                menuNode.setIcon(menuNodeParent.getIcon());
                child.add(menuNode);
            }
        }

    }
}
