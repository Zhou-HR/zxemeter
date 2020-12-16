package com.gdiot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse.FormComponentValueVo;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse.OperationRecordsVo;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse.ProcessInstanceTopVo;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse.TaskTopVo;
import com.dingtalk.api.response.OapiProcessinstanceListidsResponse;
import com.gdiot.ssm.util.DingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@RestController
@RequestMapping("/dd")
public class DingDingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DingDingController.class);

    /**
     * 发送工作通知消息
     *
     * @param params
     * @return
     */
    @RequestMapping("/sendNotifyByCode")
    public String sendMessage(@RequestBody Map<String, String> params) {
        String userID = null;
        String msg = null;
        if (params != null) {
            if (params.containsKey("userID")) {
                userID = params.get("userID");
            }
            if (params.containsKey("msg")) {
                msg = params.get("msg");
            }
        }
        String ddUserId = userID;
        LOGGER.info("ddUserId=" + ddUserId);
        OapiMessageCorpconversationAsyncsendV2Response sendnotify = DingUtils.sendMessage(ddUserId, msg);
        LOGGER.info("sendnotify=" + sendnotify.getBody());
        boolean isSuccess = sendnotify.isSuccess();
        if (isSuccess) {
//			DingMsgDataPo mDingMsgDataPo = new DingMsgDataPo();
//			mDingMsgDataPo.setUserid(userID);
//			mDingMsgDataPo.setMsg(msg);
//			notifyService.insertDingMsg(mDingMsgDataPo);
        }
        return sendnotify.getBody();

    }

    /**
     * 发送工作通知消息
     *
     * @param params
     * @return
     */
    @RequestMapping("/sendNotify")
    public String sendMessageToUser(@RequestBody Map<String, String> params) {
        String userID = null;
        String msg = null;
        if (params != null) {
            if (params.containsKey("userID")) {
                userID = params.get("userID");
            }
            if (params.containsKey("msg")) {
                msg = params.get("msg");
            }
        }
        OapiMessageCorpconversationAsyncsendV2Response sendnotify = DingUtils.sendMessage(userID, msg);
        LOGGER.info("getBody=" + sendnotify.getBody());
        LOGGER.info("getErrorCode=" + sendnotify.getErrorCode());
        LOGGER.info("isSuccess=" + sendnotify.isSuccess());
        boolean isSuccess = sendnotify.isSuccess();
        if (isSuccess) {
//			DingMsgDataPo mDingMsgDataPo = new DingMsgDataPo();
//			mDingMsgDataPo.setUserid(userID);
//			mDingMsgDataPo.setMsg(msg);
//			notifyService.insertDingMsg(mDingMsgDataPo);
        }
        return sendnotify.getBody();

    }

    /**
     * 查询钉钉用户个数
     *
     * @param params
     * @return
     */
    @RequestMapping("/getUserCount")
    public String getUserCount(@RequestBody Map<String, String> params) {
        Long userCount = DingUtils.getUserCount();
        LOGGER.info("usercount=" + userCount);
        return "dingding user count:" + userCount;
    }

    /**
     * 测试用接口：获取审批实例列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/getProcessListId")
    public Map<String, Object> getProcessListId(@RequestBody Map<String, String> params) {
        String userId = null;
        long startTime = 0;
        long endTime = 0;
        if (params != null) {

            if (params.containsKey("userId")) {
                userId = params.get("userId");
            }
            if (params.containsKey("startTime")) {
                startTime = Long.parseLong(params.get("startTime"));
            }
            if (params.containsKey("endTime")) {
                endTime = Long.parseLong(params.get("endTime"));
            }
        }

        OapiProcessinstanceListidsResponse listId = DingUtils.getPorcessListId(startTime, endTime, userId);
        LOGGER.info("  listId=" + listId.toString());
        LOGGER.info("  getBody=" + listId.getBody());
        // getBody={"errcode":0,"result":{"list":["4f43b3ed-6060-4edc-967a-7e75ed639c32"]},"request_id":"16dmb748jjjbl"}
        LOGGER.info("  getCode=" + listId.getCode());
        LOGGER.info("  getErrmsg=" + listId.getErrmsg());
        LOGGER.info("  getErrorCode=" + listId.getErrorCode());
        LOGGER.info("  getMessage=" + listId.getMessage());
        LOGGER.info("  getMsg=" + listId.getMsg());
        LOGGER.info("  getSubMessage=" + listId.getSubMessage());
        LOGGER.info("  getSubMsg=" + listId.getSubMsg());
        LOGGER.info("  getErrcode=" + listId.getErrcode());
        LOGGER.info("  getResult=" + listId.getResult().toString());

        JSONObject resultJson = JSONObject.parseObject(listId.getBody());
        Map<String, Object> result = new HashMap<>();
        result.put("result", listId.getBody());
        return resultJson;
    }

    /**
     * 测试用接口：获取审批实例
     *
     * @param params
     * @return
     */
    @RequestMapping("/getPorcessInstance")
    public JSONArray getPorcessInstance(@RequestBody Map<String, String> params) {
        String processId = null;
        if (params != null) {

            if (params.containsKey("processId")) {
                processId = params.get("processId");
            }
        }

        OapiProcessinstanceGetResponse response = DingUtils.getPorcessInstance(processId);
        LOGGER.info("  getBody=" + response.getBody());
        LOGGER.info("  getCode=" + response.getCode());
        LOGGER.info("  getErrmsg=" + response.getErrmsg());
        LOGGER.info("  getErrorCode=" + response.getErrorCode());
        LOGGER.info("  getMessage=" + response.getMessage());
        LOGGER.info("  getMsg=" + response.getMsg());
        LOGGER.info("  getSubMessage=" + response.getSubMessage());
        LOGGER.info("  getSubMsg=" + response.getSubMsg());
        LOGGER.info("  getErrcode=" + response.getErrcode());
        LOGGER.info("  getProcessInstance=" + response.getProcessInstance());
        LOGGER.info("  toString=" + response.toString());

        JSONArray jsonArray = new JSONArray();

        //返回含页面所有内容
        JSONObject resultJson = JSONObject.parseObject(response.getBody());
        jsonArray.add(resultJson);

        Map<String, Object> result = new HashMap<>();
//		result.put("result", response.getBody());

        ProcessInstanceTopVo process = response.getProcessInstance();
        LOGGER.info("  getBizAction=" + process.getBizAction());
        LOGGER.info("  getBusinessId=" + process.getBusinessId());
        LOGGER.info("  getOriginatorDeptId=" + process.getOriginatorDeptId());
        LOGGER.info("  getOriginatorDeptName=" + process.getOriginatorDeptName());
        LOGGER.info("  getResult=" + process.getResult());
        LOGGER.info("  getStatus=" + process.getStatus());
        LOGGER.info("  getApproverUserids=" + process.getApproverUserids());
        LOGGER.info("  getAttachedProcessInstanceIds=" + process.getAttachedProcessInstanceIds());
        LOGGER.info("  getCcUserids=" + process.getCcUserids());
        LOGGER.info("  getCreateTime=" + process.getCreateTime());
        LOGGER.info("  getFinishTime=" + process.getFinishTime());
        LOGGER.info("  getFormComponentValues=" + process.getFormComponentValues());
        LOGGER.info("  getOperationRecords=" + process.getOperationRecords());
        LOGGER.info("  getTasks=" + process.getTasks());


        List<FormComponentValueVo> componentList = process.getFormComponentValues();
        List<OperationRecordsVo> recordslist = process.getOperationRecords();
        List<TaskTopVo> taskList = process.getTasks();
        if (componentList != null && componentList.size() > 0) {
            for (FormComponentValueVo formVo : componentList) {
                LOGGER.info("  getId=" + formVo.getId());
                LOGGER.info("  getExtValue=" + formVo.getExtValue());
                LOGGER.info("  getName=" + formVo.getName());

                //返回出差申请相关的具体内容
                LOGGER.info("  getValue=" + formVo.getValue());
                String formId = formVo.getId();
                if (formId.contains("DDBizSuite")) {
                    return JSONArray.parseArray(formVo.getValue());
                }
            }
        }
        if (recordslist != null && recordslist.size() > 0) {
            for (OperationRecordsVo recordsVo : recordslist) {
                LOGGER.info("  getOperationResult=" + recordsVo.getOperationResult());
                LOGGER.info("  getOperationType=" + recordsVo.getOperationType());

                //返回出差申请审批结果
                LOGGER.info("  getRemark=" + recordsVo.getRemark());
                LOGGER.info("  getUserid=" + recordsVo.getUserid());
                LOGGER.info("  getDate=" + recordsVo.getDate());
            }
        }
        if (taskList != null && taskList.size() > 0) {
            for (TaskTopVo taskVo : taskList) {
                LOGGER.info("  getTaskid=" + taskVo.getTaskid());
                LOGGER.info("  getTaskResult=" + taskVo.getTaskResult());
                LOGGER.info("  getTaskStatus=" + taskVo.getTaskStatus());
                LOGGER.info("  getUserid=" + taskVo.getUserid());

                //返回发出出差申请的开始时间
                LOGGER.info("  getCreateTime=" + taskVo.getCreateTime());

                //返回发出出差申请的结束时间
                LOGGER.info("  getFinishTime=" + taskVo.getFinishTime());
            }
        }

        return jsonArray;
    }
}
