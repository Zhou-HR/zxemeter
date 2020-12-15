package com.gdiot.controller;

import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMDataPo;
import com.gdiot.model.WMReadDataPo;
import com.gdiot.service.AsyncService;
import com.gdiot.service.IWMDataService;
import com.gdiot.util.WMUdpSendCmdsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
@RestController
@RequestMapping("/udp")
public class UDPController {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private IWMDataService mIWMDataService;

    //延迟发送，放在待发送列表中，等待下一次发送
    @PostMapping(value = "/udpAddDownCmd")
    public Map<String, Object> udpAddDownCmd(@RequestBody Map<String, String> params) {
        String module_type = null;
        String wm_num = null;
        String type = null;
        String value = null;
        String operate_type = null;
        if (params != null) {
            if (params.containsKey("module_type")) {//wm_switch
                module_type = params.get("module_type");
            }
            if (params.containsKey("dev_id")) {
                wm_num = params.get("dev_id");
            }
            if (params.containsKey("type")) {//wm_switch
                type = params.get("type");
            }
            if (params.containsKey("value")) {
                value = params.get("value");
            }
            if (params.containsKey("operate_type")) {
                operate_type = params.get("operate_type");
            }
        }
        String content = WMUdpSendCmdsUtils.getCmdContent(wm_num, type, operate_type, value);
        String typeStr = "";
        if ("wm_switch".equals(type)) {//强制开关阀
            if ("O".equals(operate_type)) {//执行，操作
                if ("on".equals(value)) {
                    typeStr = "强制开阀";//强制开阀
                } else if ("off".equals(value)) {
                    typeStr = "强制关阀";//强制关阀
                } else if ("back".equals(value)) {
                    typeStr = "强制撤消";//强制撤消
                } else if ("free_on".equals(value)) {
                    typeStr = "自由开阀";//自由开阀
                } else if ("free_off".equals(value)) {
                    typeStr = "自由关阀";//自由关阀
                }
            } else if ("R".equals(operate_type)) {
                typeStr = "读阀控状态";//读阀控状态
            }
        }
        WMCmdDataPo mWMCmdDataPo = new WMCmdDataPo();
        mWMCmdDataPo.setWmNum(wm_num);
        mWMCmdDataPo.setCmdData(content);
        mWMCmdDataPo.setDownStatus("1");
        mWMCmdDataPo.setType(typeStr);
        mIWMDataService.insertCmdData(mWMCmdDataPo);

        Map<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("result", "已经放在待发送列表中，等待下一次上报后立即下发！");
        return map;
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
                pageNo = Integer.parseInt(params.get("pageNo"));
            }
            if (params.containsKey("pageSize")) {
                pageSize = Integer.parseInt(params.get("pageSize"));
            }
        }
        List<WMDataPo> list = mIWMDataService.selectbyDevId(wm_num, pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("count", pageSize);
        map.put("list", list);
        return map;
    }

    @PostMapping(value = "/getWmReadData")
    public Map<String, Object> getWmReadData(@RequestBody Map<String, String> params) {
        String wm_num = null;
        int pageNo = 1;
        int pageSize = 20;
        if (params != null) {
            if (params.containsKey("wm_num")) {
                wm_num = params.get("wm_num");
            }
            if (params.containsKey("pageNo")) {
                pageNo = Integer.parseInt(params.get("pageNo"));
            }
            if (params.containsKey("pageSize")) {
                pageSize = Integer.parseInt(params.get("pageSize"));
            }
        }
        List<WMReadDataPo> list = mIWMDataService.selectReadData(null, null);
        Map<String, Object> map = new HashMap<>();
        map.put("count", pageSize);
        map.put("list", list);
        return map;
    }

}
