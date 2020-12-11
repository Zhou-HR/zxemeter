package com.gdiot.controller;

import com.gdiot.model.WMDataPo;
import com.gdiot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestAction {

    @Autowired
    @Qualifier("XBEMDataService")
    private IXBEMDataService mXBEMDataService;

    @Autowired
    private IAKREMDataService mAKREMDataService;

    @Autowired
    private INBYDEMEventService mINBYDEMEventService;
    @Autowired
    private INBYDEMReadService mINBYDEMReadService;
    @Autowired
    private INBYDEMStatusService mINBYDEMStatusService;

    @Autowired
    private IWMDataService mIWMDataService;

    @Autowired
    private ISmokeDataService mISmokeDataService;

    @GetMapping(value = "/test")
    public void test() {
        log.info("test===========@@@@@@@@@@@@@");
        log.info("测试页面");
        System.out.printf("测试页面");
    }

    @PostMapping(value = "/getXBEMDataList")
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

    @PostMapping(value = "/getAKREMDataList")
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

    @PostMapping(value = "/getWmData")
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

    @PostMapping(value = "/getSmokeDataList")
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

    @PostMapping(value = "/getXBEventDataList")
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

    @PostMapping(value = "/getNBStatusList")
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
