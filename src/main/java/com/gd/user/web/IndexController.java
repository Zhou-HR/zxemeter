package com.gd.user.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.mapper.UserMapper;
import com.gd.model.po.User;
import com.gd.model.vo.MenuNode;
import com.gd.user.service.PermissionService;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("")
public class IndexController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/index")
    public String index(@RequestParam(required = false) String redirect, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.getUserByName(name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(user.getId()));
        session.setAttribute("userId", user.getId());
        session.setAttribute("userCode", user.getName());
        session.setAttribute("userName", user.getRealname());
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        session.setAttribute("basePath", basePath);
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        session.setAttribute("role", "ROLE_EM");
        session.removeAttribute("roleId");
        request.getSession().setAttribute("opType", "0");


        {
            session.setAttribute("user", user);

            MenuNode menuNode = permissionService.getMenuByUserId(user.getId());
            session.setAttribute("menuNodeAll", menuNode);
            session.setAttribute("topMenu", menuNode.getChild());

            if (menuNode.getChild() != null) {
                session.setAttribute("menuNode", menuNode.getChild().get(0));
            }

            session.setAttribute("basePath", basePath);

            Map<String, Integer> mapTopMenuPermission = new HashMap<String, Integer>();
            MenuNode menuNodeTop = permissionService.getTopMenuByUserId(user.getId());
            if (menuNodeTop.getChild() != null) {
                session.setAttribute("menuNodeTop", menuNodeTop.getChild().get(0));
                List<MenuNode> listTopMenu = menuNodeTop.getChild().get(0).getChild();
                for (MenuNode menuNodeTopMenu : listTopMenu) {
                    if (menuNodeTopMenu.getChild() == null) {
                        mapTopMenuPermission.put(menuNodeTopMenu.getPath(), 1);
                    } else {
                        List<MenuNode> listSubTopMenu = menuNodeTopMenu.getChild();
                        for (MenuNode subTopMenu : listSubTopMenu) {
                            mapTopMenuPermission.put(subTopMenu.getPath(), 1);
                        }
                    }
                }
                session.setAttribute("mapTopMenuPermission", mapTopMenuPermission);
            }
        }

        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                session.setAttribute("user", user);
                session.setAttribute("role", "ROLE_ADMIN");
                MenuNode menuNode = permissionService.getMenuByUserId(user.getId());
                session.setAttribute("menuNodeAll", menuNode);
                session.setAttribute("topMenu", menuNode.getChild());

                session.setAttribute("menuNode", menuNode.getChild().get(0));

                return "jsp/index";
            } else {
                session.removeAttribute("role");
            }
        }

        return "jsp/index";
    }

    @RequestMapping("/toMenu/{menuId}")
    public String toMenu(@PathVariable Integer menuId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        MenuNode menuNode = (MenuNode) session.getAttribute("menuNodeAll");
        for (MenuNode topMenu : menuNode.getChild()) {
            if (menuId == topMenu.getId()) {
                session.setAttribute("menuNode", topMenu);
            }
        }
        session.removeAttribute("roleId");
        return "jsp/index";

    }

    @RequestMapping("/getShiyebu")
    @ResponseBody
    public List<MenuNode> getShiyebu(String id, HttpServletRequest request) {
        List<MenuNode> list = new ArrayList<MenuNode>();
        HttpSession session = request.getSession();
        MenuNode menuNode = (MenuNode) session.getAttribute("menuNode");
        for (MenuNode topMenu : menuNode.getChild()) {
            for (MenuNode subCompanyMenu : topMenu.getChild()) {
                if (subCompanyMenu.getCompanyId().equals(id)) {
                    return subCompanyMenu.getChild();
                }
            }

        }
        session.removeAttribute("roleId");
        return list;
    }

    @RequestMapping("/index/saveOpen")
    @ResponseBody
    public void saveOpen(String roleId, HttpServletRequest request) {
        request.getSession().setAttribute("roleId", roleId);
        request.getSession().removeAttribute("projectNo");
    }

    @RequestMapping("/index/saveOpType")
    @ResponseBody
    public void saveOpType(String opType, HttpServletRequest request) {
        request.getSession().setAttribute("opType", opType);
    }

    @RequestMapping("/topMenu/{menu}")
    public String topMenu(@PathVariable String menu, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("topMenu", menu);
        return "jsp/emeter/list" + menu;
    }

}
