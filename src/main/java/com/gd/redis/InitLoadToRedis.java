package com.gd.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.gdiot.ssm.entity.Project;
import com.gdiot.ssm.entity.ProjectMap;
import com.gdiot.ssm.entity.User;

/**
 * @author ZhouHR
 */
@Component
public class InitLoadToRedis {

    private static Map<String, List<Project>> mapCompanyProject = new HashMap<String, List<Project>>();

    private static Map<String, User> mapUser = new HashMap<String, User>();

    private static Map<String, Project> mapProjectNoProject = new HashMap<String, Project>();

    private static Map<String, String> mapCompany = new HashMap<String, String>();

    private static Map<String, List<ProjectMap>> mapShengCompanyProjectMap = new HashMap<String, List<ProjectMap>>();

    private static List<ProjectMap> listJituanProjectMap = new ArrayList<ProjectMap>();

    public static User getUser(String userCode) {
        return mapUser.get(userCode);
    }

    public static List<Project> getProjectList(String companyId) {
        return mapCompanyProject.get(companyId);
    }

    public static Project getProject(String projectNo) {
        return mapProjectNoProject.get(projectNo);
    }

    public static String getCompanyName(String companyId) {
        return mapCompany.get(companyId);
    }

    public static List<ProjectMap> getJituanProjectMap() {
        return InitLoadToRedis.listJituanProjectMap;
    }

    public static List<ProjectMap> getShengProjectMap(String companyId) {
        return InitLoadToRedis.mapShengCompanyProjectMap.get(companyId);
    }

    @PostConstruct
    public void load() {
//		mapCompanyProject=(Map<String, List<Project>>) JedisUtil.get("mapCompanyProject");
//		System.out.println(mapCompanyProject);
        sync();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void sync() {
        mapCompanyProject = (Map<String, List<Project>>) JedisUtil.get("mapCompanyProject");
        mapUser = (Map<String, User>) JedisUtil.get("user");
        mapProjectNoProject = (Map<String, Project>) JedisUtil.get("mapProjectNoProject");

        mapCompany = (Map<String, String>) JedisUtil.get("mapCompany");

        mapShengCompanyProjectMap = (Map<String, List<ProjectMap>>) JedisUtil.get("mapShengCompanyProjectMap");

        listJituanProjectMap = (List<ProjectMap>) JedisUtil.get("listJituanProjectMap");

    }

}
