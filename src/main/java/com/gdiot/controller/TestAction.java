package com.gdiot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdiot.model.WMDataPo;
import com.gdiot.service.IAKREMDataService;
import com.gdiot.service.INBYDEMEventService;
import com.gdiot.service.INBYDEMReadService;
import com.gdiot.service.INBYDEMStatusService;
import com.gdiot.service.ISmokeDataService;
import com.gdiot.service.IWMDataService;
import com.gdiot.service.IXBEMDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhouHR
 */
@Slf4j
@Controller
@RequestMapping("/test")
public class TestAction {

    @Autowired()
    @Qualifier("XBEMDataService")
    private IXBEMDataService mXBEMDataService;

    @Autowired()
    private IAKREMDataService mAKREMDataService;

    @Autowired()
    private INBYDEMEventService mINBYDEMEventService;
    @Autowired()
    private INBYDEMReadService mINBYDEMReadService;
    @Autowired()
    private INBYDEMStatusService mINBYDEMStatusService;

    @Autowired()
    private IWMDataService mIWMDataService;

    @Autowired()
    private ISmokeDataService mISmokeDataService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public void test() {
        log.info("test===========@@@@@@@@@@@@@");
        log.info("测试页面");
        System.out.printf("测试页面");
    }

    @RequestMapping(value = "/getXBEMDataList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get_em_data_list(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        String dev_id = null;
        String eNum = null;
        Long beginTime = null;
        Long endTime = null;
        String source = null;
        int pageNo = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("dev_id")) {
                dev_id = params.get("dev_id");
            }
            if (params.containsKey("e_num")) {
                eNum = params.get("e_num");
            }
            if (params.containsKey("source")) {
                source = params.get("source");
            }
            try {
                if (params.containsKey("begin_time")) {
                    beginTime = Long.valueOf(params.get("beginTime"));
                }
                if (params.containsKey("end_time")) {
                    endTime = Long.valueOf(params.get("endTime"));
                }
                if (params.containsKey("pageNo")) {
                    pageNo = Integer.valueOf(params.get("pageNo"));
                }
                if (params.containsKey("pageSize")) {
                    pageSize = Integer.valueOf(params.get("pageSize"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("error", 500);
                result.put("msg", "date format error!");
                return result;
            }
        }
        result = mXBEMDataService.selectList(dev_id, eNum, beginTime, endTime, source, pageNo, pageSize);
        result.put("error", 0);
        result.put("msg", "success");
        return result;
    }

    @RequestMapping(value = "/getAKREMDataList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get_akr_em_data_list(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        String dev_id = null;
        String eNum = null;
        Long beginTime = null;
        Long endTime = null;
        String source = null;
        int pageNo = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("dev_id")) {
                dev_id = params.get("dev_id");
            }
            if (params.containsKey("e_num")) {
                eNum = params.get("e_num");
            }
            if (params.containsKey("source")) {
                source = params.get("source");
            }
            try {
                if (params.containsKey("begin_time")) {
                    beginTime = Long.valueOf(params.get("beginTime"));
                }
                if (params.containsKey("end_time")) {
                    endTime = Long.valueOf(params.get("endTime"));
                }
                if (params.containsKey("pageNo")) {
                    pageNo = Integer.valueOf(params.get("pageNo"));
                }
                if (params.containsKey("pageSize")) {
                    pageSize = Integer.valueOf(params.get("pageSize"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("error", 500);
                result.put("msg", "date format error!");
                return result;
            }
        }
        result = mAKREMDataService.selectList(dev_id, eNum, beginTime, endTime, source, pageNo, pageSize);
        result.put("error", 0);
        result.put("msg", "success");
        return result;
    }

    @RequestMapping(value = "/getWmData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getWmData(@RequestBody Map<String, String> params) {
        String wm_num = null;
        int pageNo = 1;
        int pageSize = 20;
        if (params != null) {
            if (params.containsKey("wm_num")) {
                wm_num = params.get("wm_num");
            }
            if (params.containsKey("pageNo")) {
                pageNo = Integer.valueOf(params.get("pageNo"));
            }
            if (params.containsKey("pageSize")) {
                pageSize = Integer.valueOf(params.get("pageSize"));
            }
        }
        List<WMDataPo> list = mIWMDataService.selectbyDevId(wm_num, pageNo, pageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", pageSize);
        map.put("list", list);
        return map;
    }

    @RequestMapping(value = "/getSmokeDataList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get_smoke_data_list(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        String dev_id = null;
        int pageNo = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("dev_id")) {
                dev_id = params.get("dev_id");
            }
            try {
                if (params.containsKey("pageNo")) {
                    pageNo = Integer.valueOf(params.get("pageNo"));
                }
                if (params.containsKey("pageSize")) {
                    pageSize = Integer.valueOf(params.get("pageSize"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("error", 500);
                result.put("msg", "date format error!");
                return result;
            }
        }
        result = mISmokeDataService.selectbyDevId(dev_id, pageNo, pageSize);
        result.put("error", 0);
        result.put("msg", "success");
        return result;
    }

    @RequestMapping(value = "/getXBEventDataList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get_xb_event_data_list(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        String dev_id = null;
        String eNum = null;
        Long beginTime = null;
        Long endTime = null;
        String source = null;
        int pageNo = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("dev_id")) {
                dev_id = params.get("dev_id");
            }
            if (params.containsKey("e_num")) {
                eNum = params.get("e_num");
            }
            if (params.containsKey("source")) {
                source = params.get("source");
            }
            try {
                if (params.containsKey("begin_time")) {
                    beginTime = Long.valueOf(params.get("beginTime"));
                }
                if (params.containsKey("end_time")) {
                    endTime = Long.valueOf(params.get("endTime"));
                }
                if (params.containsKey("pageNo")) {
                    pageNo = Integer.valueOf(params.get("pageNo"));
                }
                if (params.containsKey("pageSize")) {
                    pageSize = Integer.valueOf(params.get("pageSize"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("error", 500);
                result.put("msg", "date format error!");
                return result;
            }
        }
        result = mINBYDEMEventService.listNBEMeterData(dev_id, eNum, dev_id, beginTime, endTime, source, pageNo, pageSize);
        result.put("error", 0);
        result.put("msg", "success");
        return result;
    }

    @RequestMapping(value = "/getNBStatusList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get_nb_status_list(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        String dev_id = null;
        String status = null;
        int pageNo = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("dev_id")) {
                dev_id = params.get("dev_id");
            }
            if (params.containsKey("status")) {
                status = params.get("status");
            }
            try {
                if (params.containsKey("pageNo")) {
                    pageNo = Integer.valueOf(params.get("pageNo"));
                }
                if (params.containsKey("pageSize")) {
                    pageSize = Integer.valueOf(params.get("pageSize"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.put("error", 500);
                result.put("msg", "date format error!");
                return result;
            }
        }
        result = mINBYDEMStatusService.selectStatus(dev_id, dev_id, status, pageNo, pageSize);
        result.put("error", 0);
        result.put("msg", "success");
        return result;
    }

}
