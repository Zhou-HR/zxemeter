package com.gd.emeter;

import java.util.ArrayList;
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

import com.gd.mapper.MeterSimMapper;
import com.gd.mapper.MeterWarningMapper;
import com.gd.model.po.MeterSim;
import com.gd.model.po.MeterWarning;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/meterSim")
public class MeterSimController {

    @Autowired
    private MeterSimMapper meterSimMapper;

    @RequestMapping("/toListMeterSim")
    public String toListMeterSim(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listMeterSim";
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
        if (params.get("meterNo") == null) {
            map.put("rows", new ArrayList<MeterSim>());
            map.put("total", 0);
            return map;
        }

        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);
        List<MeterSim> list = meterSimMapper.selectList(params);
        map.put("rows", list);
        map.put("total", meterSimMapper.countByCondition(params));
        return map;
    }


}
