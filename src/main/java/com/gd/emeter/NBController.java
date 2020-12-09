package com.gd.emeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.mapper.G2Mapper;
import com.gd.mapper.Guangdong2gMapper;
import com.gd.mapper.LoraMapper;
import com.gd.mapper.MeterMapper;
import com.gd.mapper.NBMapper;
import com.gd.mapper.NBSendPackageMapper;
import com.gd.model.po.Meter;
import com.gd.model.po.NBSendPackage;
import com.gd.model.po.NBValue;
import com.gd.redis.InitLoadToRedis;
import com.gdiot.ssm.entity.Project;

/**
 * @author ZhouHR
 */
@Slf4j
@Controller
@RequestMapping("/nb")
public class NBController {

    @Autowired
    private NBMapper nbMapper;

    @Autowired
    private LoraMapper loraMapper;

    @Autowired
    private G2Mapper g2Mapper;

    @Autowired
    private Guangdong2gMapper guangdong2gMapper;

    @Autowired
    private MeterMapper meterMapper;

    @Autowired
    private NBSendPackageMapper nbSendPackageMapper;

    @RequestMapping("/toListSendPackage")
    public String toListSendPackage(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listNBSendPackage";
    }

    @RequestMapping("/toListNB")
    public String toListNB(@RequestParam(required = false) String meterNo, String installDate1, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        request.setAttribute("installDate1", installDate1);
        return "jsp/emeter/listNB";
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
        long t0 = System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        long t2 = 0L;
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        //根据电表查询
        String meterNo = "";
        String meterType = "";
        Meter meter = null;
        if (params.get("meterNo") != null) {
            meterNo = params.get("meterNo").toString();
            meter = meterMapper.getMeterByMeterNo(meterNo);
            if (meter == null) {
                map.put("rows", new ArrayList<NBValue>());
                map.put("total", 0);
                return map;
            }
            meterType = meter.getMeterType();
        }

        String source = null;
        if (params.get("source") != null)
            source = params.get("source").toString();
        else
            source = meterType;

        List<NBValue> list = null;
        int total = 0;

//			if(ocompanyId!=null&&(ocompanyId.toString().length()==4||"99".equals(ocompanyId.toString()))){
//				list = nbMapper.selectZZNBList(params);
//				total=nbMapper.countZZNBByCondition(params);
//			}else
        {//从 em_nb_data查询
            t1 = System.currentTimeMillis();
            //查询companyIds
            String companyIds = nbMapper.getCompanyIdsByUserId(userId);
            String[] arr = companyIds.split(",");
            if (StringUtils.isEmpty(companyIds))
                companyIds = "('')";
            else {
//					companyIds="("+companyIds+")";
                StringBuilder sb = new StringBuilder("(");
                for (String str : arr) {
                    sb.append("'");
                    sb.append(str);
                    sb.append("',");
                }
                companyIds = sb.toString();
                companyIds = companyIds.substring(0, companyIds.length() - 1);
                companyIds += ")";
            }

            log.info(companyIds);
            if (arr.length < 400)
                params.put("companyIds", companyIds);
            list = nbMapper.selectNBList(params);
            t2 = System.currentTimeMillis();
            log.info("===============================selectNBList共花时间：" + (t2 - t1) + "====================================");
            t1 = System.currentTimeMillis();
            total = nbMapper.countNBByCondition(params);
            t2 = System.currentTimeMillis();
            log.info("===============================countNBByCondition共花时间：" + (t2 - t1) + "====================================");
        }

        map.put("rows", list);
        map.put("total", total);
        long t3 = System.currentTimeMillis();
        log.info("===============================共花时间：" + (t3 - t0) + "====================================");
        return map;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/pagingSendPackage")
    @ResponseBody
    public Map pagingSendPackage(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);
        List<NBSendPackage> list = nbSendPackageMapper.selectList(params);
        for (NBSendPackage nbValue : list) {
            //设置 公司 事业部
            String projectNo = nbValue.getProjectNo();
            Project project = InitLoadToRedis.getProject(projectNo);
            if (project == null) continue;
            String companyId = project.getCompanyId();
            nbValue.setUnit(InitLoadToRedis.getCompanyName(companyId));
            if (companyId != null && companyId.length() == 4) {
                companyId = companyId.substring(0, 2);
                nbValue.setCompany(InitLoadToRedis.getCompanyName(companyId));
            }
        }
        map.put("rows", list);
        map.put("total", nbSendPackageMapper.countByCondition(params));
        return map;
    }

}
