package com.gd.emeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.basic.crud.Message;
import com.gd.mapper.StationMapper;
import com.gd.model.po.Company;
import com.gd.model.po.DataHotMap;
import com.gd.model.po.Station;
import com.gd.model.po.TextValue;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;
import com.gdiot.ssm.entity.ProjectMap;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationMapper stationMapper;

    @RequestMapping("/toListCarrier")
    public String toListMeterInstall(HttpServletRequest request) {
        List<TextValue> list = stationMapper.getCarrier();
        request.setAttribute("list", list);
        return "jsp/emeter/listCarrier";
    }

    @RequestMapping("/toListMap")
    public String toListMap(String projectNo, HttpServletRequest request) {
        Project project = InitLoadToRedis.getProject(projectNo);
        if (project != null) {
            request.setAttribute("projectNo", projectNo);
            request.setAttribute("jd", project.getLongitude());
            request.setAttribute("wd", project.getLatitude());
            request.setAttribute("project", project);
        }

        return "jsp/emeter/listMap";
    }

    /**
     * 分页查询
     *
     * @param params
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/paging")
    @ResponseBody
    public Map paging(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);
        List<Station> list = stationMapper.selectList(params);
        map.put("rows", list);
        map.put("total", stationMapper.countByCondition(params));
        return map;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/getProject")
    @ResponseBody
    public Project getProject(String projectNo, HttpServletRequest request) {
//		Map<String,Object> map=new HashMap<String,Object>();	

        Project projec = InitLoadToRedis.getProject(projectNo);

//        map.put("project", projec);
        return projec;
    }

    @RequestMapping("/getCompanyNum")
    @ResponseBody
    public List getCompanyNum() {

        List<Company> list = stationMapper.getCompanyNum();
        List<DataHotMap> list1 = new ArrayList<DataHotMap>();
        for (Company company : list) {
            DataHotMap data = new DataHotMap();
            data.setValue(company.getNum());
            String name = company.getCompanyName();
            name = name.substring(0, name.indexOf("分公司"));
            data.setName(name);
            list1.add(data);
        }

        return list1;
    }

    @RequestMapping("/getJituanProjectMap")
    @ResponseBody
    public List<ProjectMap> getJituanProjectMap() {
        List<ProjectMap> list = InitLoadToRedis.getJituanProjectMap();

        return list;
    }

    @RequestMapping("/getShengProjectMap")
    @ResponseBody
    public List<ProjectMap> getShengProjectMap(String companyId) {

        return InitLoadToRedis.getShengProjectMap(companyId);
    }

    @RequestMapping("/getProjectList")
    @ResponseBody
    public List<Project> getProjectList(String companyId) {

        return InitLoadToRedis.getProjectList(companyId);
    }

    @RequestMapping("/setCarrier")
    @ResponseBody
    public Message setCarrier(String name1, String part, String price, String companyId, String projectNo, HttpServletRequest request) {

        Message message = Message.SUCCESS;
        String userId = request.getSession().getAttribute("userId").toString();
        stationMapper.updateCarrier1(name1, part, price, companyId, projectNo, userId);
        return message;
    }

    @RequestMapping("/restoreDefault")
    @ResponseBody
    public Message restoreDefault(String companyId, String projectNo, HttpServletRequest request) {

        Message message = Message.SUCCESS;
        String userId = request.getSession().getAttribute("userId").toString();
        stationMapper.restoreDefault(companyId, projectNo, userId);
        return message;
    }

    private static boolean check(String value) {
        try {
            int n = Integer.valueOf(value);
            if (n < 0 || n > 10000) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @RequestMapping("/getCarrier")
    @ResponseBody
    public List<TextValue> getCarrier() {
        List<TextValue> list = stationMapper.getCarrier();
        return list;
    }

}
