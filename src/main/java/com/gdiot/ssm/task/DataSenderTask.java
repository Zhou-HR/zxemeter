package com.gdiot.ssm.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdiot.model.*;
import com.gdiot.service.*;
import com.gdiot.ssm.cmds.AKRSendCmdsUtils;
import com.gdiot.ssm.cmds.QDSendCmdsUtils;
import com.gdiot.ssm.cmds.SendCmds;
import com.gdiot.ssm.cmds.SendCmdsUtils;
import com.gdiot.ssm.redis.RedisUtil;
import com.gdiot.ssm.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
public class DataSenderTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSenderTask.class);
    private String data;
    private final String type;
    private Map<String, String> msgMap;

    private AsyncService asyncService;
    public RedisUtil redisUtil;
    private IXBEMDataService mIXBEMDataService;
    private INBYDEMCmdsService mINBYDEMCmdsService;
    private INBYDEMReadService mINBYDEMReadService;
    private INBYDEMEventService mINBYDEMEventService;
    private IAKREMDataService mIAKREMDataService;
    private IQDEMReadService mIQDEMReadService;

    public DataSenderTask(String data, String type) {
        super();
        this.data = data;
        this.type = type;
    }

    public DataSenderTask(Map<String, String> map, String type) {
        super();
        this.msgMap = map;
        this.type = type;
    }

    @Override
    public void run() {
        LOGGER.info("task: DataSenderTask run-data :" + data);
        LOGGER.info("task: DataSenderTask run-type :" + type);
        LOGGER.info("task: DataSenderTask run-msgMap :" + msgMap);

        String regex_imei = "^\\d{15}$";
        String regex_dev = "[a-f0-9A-F]{16}";

        switch (type) {
            case "gdyd_2g":
                LOGGER.info("================start down cmd gdyd_2g =====================");
                String module_type = msgMap.get("module_type");

                //dev_id
                String dev_id = msgMap.get("imei");
                String data_type = msgMap.get("type");
//			String value = msgMap.get("value");
                String operate_type = msgMap.get("operate_type");
                String request_id = msgMap.get("request_id");

                SendCmds mSendCmds = new SendCmds();
                JSONObject result = mSendCmds.sendDownCmd(msgMap, request_id);
                LOGGER.info("下行返回结果result=" + result.toJSONString());

                //{"result":"{\"errno\":0,
                //\"data\":{\"cmd_uuid\":\"2beca395-f22d-5663-b5ec-b1a7c8228a43\"},
                //\"error\":\"succ\"}",
                //"statusCode":200}

                if (result.containsKey("statusCode")) {
                    int statusCode = result.getIntValue("statusCode");
                    String resultStr = result.getString("result");
                    LOGGER.info("------gdyd_2g---------resultStr=" + resultStr);
                    if (statusCode == 200 && resultStr.length() > 11) {
                        JSONObject resultJson = JSON.parseObject(resultStr);
                        int errno = resultJson.getIntValue("errno");
                        String error = resultJson.getString("error");
                        LOGGER.info("------gdyd_2g---------errno=" + errno);
                        LOGGER.info("------gdyd_2g---------error=" + error);
                        if (errno == 0) {
                            //获取下行后上报的数据
                            String resultData = "";
                            //getTcpResultData(module_type,dev_id,data_type,System.currentTimeMillis());
                            LOGGER.info("------gdyd_2g---------resultData=" + resultData);
                            if (resultData != null && "R".equals(operate_type)) {
                                redisUtil.set(request_id, resultData, 0);
                                redisUtil.expire(request_id, 1800, 0);
                            } else {
                                redisUtil.set(request_id, result.toJSONString(), 0);
                                redisUtil.expire(request_id, 1800, 0);
                            }
                        } else {
                            redisUtil.set(request_id, result.toJSONString(), 0);
                            redisUtil.expire(request_id, 1800, 0);
                        }
                    } else {
                        redisUtil.set(request_id, result.toJSONString(), 0);
                        redisUtil.expire(request_id, 1800, 0);
                    }
                } else {
                    redisUtil.set(request_id, result.toJSONString(), 0);
                    redisUtil.expire(request_id, 1800, 0);
                }
                break;

            case "nb":
            case "zx_xb_nb_em":
            case "lora_em":

                //2g下行
            case "mqtt_2g":
            case "zx_jbq_nb_em":
                LOGGER.info("================start down cmd=====================");
                String module_type0 = msgMap.get("module_type");
                String imei0 = msgMap.get("imei");
                String e_num = msgMap.get("eNum");

                //芯北电表固定值
                String fac_id = "01";
                String data_type0 = msgMap.get("type");
                String value = msgMap.get("value");
                String operate_type0 = msgMap.get("operate_type");
                String request_id0 = msgMap.get("request_id");
                if (!imei0.matches(regex_imei) && !imei0.matches(regex_dev)) {
                    LOGGER.error("imei error");
                    break;
                }
                SendCmdsUtils mSendCmdsUtils = new SendCmdsUtils();
//			Map<String ,String> emInfo = mSendCmdsUtils.getEMInfoByImei(module_type0,imei0);
//			if(emInfo != null && emInfo.size() >0) {
                if (e_num != null) {
//				String e_num = emInfo.get("e_num") != null ? emInfo.get("e_num") : "";
//				String fac_id = emInfo.get("e_fac") != null ? emInfo.get("e_fac") : "01";
//				String time = emInfo.get("time") != null ? emInfo.get("time") : "" ;
                    msgMap.put("e_num", e_num);
                    msgMap.put("fac_id", fac_id);
                    msgMap.put("time", String.valueOf(System.currentTimeMillis()));
//				String request_id = imei + "_" + new_seq_hex;
                    Map<String, Object> map = mSendCmdsUtils.getCmdsInfo(msgMap);
                    if (map != null && map.size() > 0) {
                        String content = map.get("content").toString();
                        String new_seq_hex = map.get("new_seq_hex").toString();
                        String new_data_seq_hex = map.get("new_data_seq_hex").toString();
                        LOGGER.info("content===" + content);
                        if (content != null && !"".equals(content)) {
                            msgMap.put("content", content);
                            msgMap.put("new_seq_hex", new_seq_hex);
                            msgMap.put("new_data_seq_hex", new_data_seq_hex);

                            SendCmds mSendCmds1 = new SendCmds();
                            JSONObject result0 = mSendCmds1.sendDownCmd(msgMap, request_id0);
                            LOGGER.info("下行返回结果result=" + result0.toJSONString());

                            LOGGER.info("task: read insert into SQL start------------");
                            if (mINBYDEMReadService == null) {
                                mINBYDEMReadService = SpringContextUtils.getBean(INBYDEMReadService.class);
                            }

                            YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
                            mYDEMNBReadPo.setDev_id(imei0);
                            mYDEMNBReadPo.setImei(imei0);
                            mYDEMNBReadPo.setOrig_value(content);
                            mYDEMNBReadPo.setSource(module_type0);
                            mYDEMNBReadPo.setTime(System.currentTimeMillis());
                            mYDEMNBReadPo.setE_num(e_num);
                            mYDEMNBReadPo.setE_fac(fac_id);
                            mYDEMNBReadPo.setData_seq(new_seq_hex);
                            mYDEMNBReadPo.setRead_type(data_type0);
                            mYDEMNBReadPo.setRead_value(result0.toJSONString());
                            mINBYDEMReadService.addOne(mYDEMNBReadPo);
                            LOGGER.info("task: read insert into SQL end!");

                            //mqtt_2g0  下行返回结果result={"errno":0,"error":"succ"} 失败 errno :1
                            //lora0   下行返回结果result={"result":"error":0}  失败：error
                            //nb0  下行返回结果result={"errno":0,"error":"succ"}
                            int err_no = -1;
                            if ("nb".equals(module_type0)
                                    || "mqtt_2g".equals(module_type0)) {
                                err_no = (int) result0.get("errno");
                            } else if ("lora_em".equals(module_type0)) {
                                err_no = (int) result0.get("error");
                            }
                            LOGGER.info("更新seq--------new_data_seq_hex=" + new_data_seq_hex);

                            //发送成功
                            if (err_no == 0) {
//							LOGGER.info("更新seq--------------");
                                //发送成功，更新seq
                                EMCmdsSEQPo mNBYDEMCmdsPo = new EMCmdsSEQPo();
                                mNBYDEMCmdsPo.setE_num(e_num);
                                mNBYDEMCmdsPo.setImei(imei0);
                                mNBYDEMCmdsPo.setCmd_seq(new_seq_hex);
                                mNBYDEMCmdsPo.setData_seq(new_data_seq_hex.toUpperCase());
                                mNBYDEMCmdsPo.setCreate_time(new Date(System.currentTimeMillis()));
                                updateDBCmdsSeq(mNBYDEMCmdsPo);

                                if ("R".equals(operate_type0) || "D".equals(operate_type0)) {
                                    LOGGER.info("-------获取返回值------start------------");
                                    //获取下行后上报的数据
                                    String resultData = getNBResultData(msgMap);
                                    LOGGER.info("-------获取到的返回值------resultData=" + resultData);
                                    if (resultData != null) {
                                        redisUtil.set(request_id0, resultData, 0);
                                        redisUtil.expire(request_id0, 1800, 0);
                                    } else {
                                        redisUtil.set(request_id0, result0.toString(), 0);
                                        redisUtil.expire(request_id0, 1800, 0);
                                    }
                                } else {
                                    redisUtil.set(request_id0, result0.toString(), 0);
                                    redisUtil.expire(request_id0, 1800, 0);
                                }
                            } else {
                                redisUtil.set(request_id0, result0.toString(), 0);
                                redisUtil.expire(request_id0, 1800, 0);
                            }
                        } else {
                            redisUtil.expire(request_id0, 180, 0);
                        }
                    }
                } else {
                    LOGGER.info("表号错误");
                    redisUtil.set(request_id0, "表号错误", 0);
                    redisUtil.expire(request_id0, 1800, 0);
                }
                break;

            case "2g_poweroff":
//			Tcp2gAnalysis();
                break;

            //安科瑞NB电表 下行
            case "akr_nb_em":
                LOGGER.info("================start down cmd=====================");
                String module_type_akr = msgMap.get("module_type");
                String e_num_akr = msgMap.get("eNum");
                String imei_akr = msgMap.get("imei");
                String data_type_akr = msgMap.get("type");
                String value_akr = msgMap.get("value");
                String operate_type_akr = msgMap.get("operate_type");
                String request_id_akr = msgMap.get("request_id");
//			String regex_imei_akr ="^\\d{15}$";
//			String regex_dev_akr ="[a-f0-9A-F]{16}";
                if (!imei_akr.matches(regex_imei) && !imei_akr.matches(regex_dev)) {
                    LOGGER.error("imei error");
                    break;
                }
                if (mIAKREMDataService == null) {
                    mIAKREMDataService = SpringContextUtils.getBean(IAKREMDataService.class);
                }
                AKRSendCmdsUtils mAKRSendCmdsUtils = new AKRSendCmdsUtils();
                Map<String, Object> map = mAKRSendCmdsUtils.getCmdsInfo(msgMap);
                if (map != null && map.size() > 0) {
                    String content = map.get("content").toString();
                    LOGGER.info("content===" + content);
                    if (content != null && !"".equals(content)) {
                        msgMap.put("content", content);

                        String time_akr = String.valueOf(System.currentTimeMillis());
                        JSONObject result_akr = mAKRSendCmdsUtils.SendMsgNB(imei_akr, content, time_akr);

                        AKREMReadPo mAKREMReadPo = new AKREMReadPo();
                        mAKREMReadPo.setDevId(imei_akr);
                        mAKREMReadPo.setImei(imei_akr);
                        mAKREMReadPo.setOrigValue(content);
                        mAKREMReadPo.setSource(type);
                        mAKREMReadPo.setTime(System.currentTimeMillis());
                        mAKREMReadPo.setENum(e_num_akr);
//    		        mAKREMReadPo.setE_fac("");
                        mAKREMReadPo.setDataSeq("");
                        mAKREMReadPo.setReadType(data_type_akr);
                        mAKREMReadPo.setReadValue(result_akr.toJSONString());
                        mIAKREMDataService.insertReadData(mAKREMReadPo);
                        LOGGER.info("task: akr read insert into SQL end!");

                        //result={"errno":0,"error":"succ"}
                        LOGGER.info("nb----------result=" + result_akr.toString());
                        LOGGER.info("task: read insert into SQL start------------");
                        if (result_akr != null && (int) result_akr.get("errno") == 0) {
//						redisUtil.set(request_id_akr, result_akr.toString(),0);
//						redisUtil.expire(request_id_akr, 1800, 0);
                            Boolean res = "R".equals(operate_type_akr) && ("003E".equals(data_type_akr) || "01C2".equals(data_type_akr) || "1001".equals(data_type_akr));
                            if (res) {
                                //获取下行后上报的数据
                                String resultData = getNBResultData(msgMap);
                                LOGGER.info("-------获取到的返回值------resultData=" + resultData);
                                if (resultData != null) {
                                    redisUtil.set(request_id_akr, resultData, 0);
                                    redisUtil.expire(request_id_akr, 1800, 0);
                                } else {
                                    redisUtil.set(request_id_akr, result_akr.toString(), 0);
                                    redisUtil.expire(request_id_akr, 1800, 0);
                                }
                            } else {
                                redisUtil.set(request_id_akr, result_akr.toString(), 0);
                                redisUtil.expire(request_id_akr, 1800, 0);
                            }
                        } else {
                            redisUtil.set(request_id_akr, result_akr.toString(), 0);
                            redisUtil.expire(request_id_akr, 1800, 0);
                        }


                    } else {
                        redisUtil.expire(request_id_akr, 180, 0);
                    }
                }
                break;

            //千丁电表下行
            case "qd_nb_em":
                LOGGER.info("================start down cmd=====================");
                String module_type_qd = msgMap.get("module_type");
                String e_num_qd = msgMap.get("eNum");
                String imei_qd = msgMap.get("imei");
                String data_type_qd = msgMap.get("type");
                String value_qd = msgMap.get("value");
                String operate_type_qd = msgMap.get("operate_type");
                String request_id_qd = msgMap.get("request_id");

                if (!imei_qd.matches(regex_imei) && !imei_qd.matches(regex_dev)) {
                    LOGGER.error("imei error");
                    break;
                }
                if (mIQDEMReadService == null) {
                    mIQDEMReadService = SpringContextUtils.getBean(IQDEMReadService.class);
                }
                QDSendCmdsUtils mQDSendCmdsUtils = new QDSendCmdsUtils();
                Map<String, Object> cmdsInfo = mQDSendCmdsUtils.getCmdsInfo(msgMap);

                if (cmdsInfo != null && cmdsInfo.size() > 0) {
                    String content = cmdsInfo.get("content").toString();
                    LOGGER.info("content===" + content);
                    if (content != null && !"".equals(content)) {
                        msgMap.put("content", content);

                        String time_qd = String.valueOf(System.currentTimeMillis());
                        JSONObject result_qd = mQDSendCmdsUtils.SendMsgNB(imei_qd, content, time_qd);

                        QDEMReadPo mQDEMReadPo = new QDEMReadPo();
                        mQDEMReadPo.setDevId(imei_qd);
                        mQDEMReadPo.setImei(imei_qd);
                        mQDEMReadPo.setOrigValue(content);
                        mQDEMReadPo.setSource(type);
                        mQDEMReadPo.setTime(System.currentTimeMillis());
                        mQDEMReadPo.setENum(e_num_qd);
                        mQDEMReadPo.setDataSeq("");
                        mQDEMReadPo.setReadType(data_type_qd);
                        mQDEMReadPo.setReadValue(result_qd.toJSONString());

                        mIQDEMReadService.insertReadData(mQDEMReadPo);
                        LOGGER.info("task: qd read insert into SQL end!");

                        LOGGER.info("nb----------result=" + result_qd.toString());
                        LOGGER.info("task: read insert into SQL start------------");

                        if (result_qd != null && (int) result_qd.get("errno") == 0) {
                            Boolean res = "R".equals(operate_type_qd) && ("003E".equals(data_type_qd) || "01C2".equals(data_type_qd) || "1001".equals(data_type_qd));
                            if (res) {
                                //获取下行后上报的数据
                                String resultData = getNBResultData(msgMap);
                                LOGGER.info("-------获取到的返回值------resultData=" + resultData);
                                if (resultData != null) {
                                    redisUtil.set(request_id_qd, resultData, 0);
                                    redisUtil.expire(request_id_qd, 1800, 0);
                                } else {
                                    redisUtil.set(request_id_qd, result_qd.toString(), 0);
                                    redisUtil.expire(request_id_qd, 1800, 0);
                                }
                            } else {
                                redisUtil.set(request_id_qd, result_qd.toString(), 0);
                                redisUtil.expire(request_id_qd, 1800, 0);
                            }
                        } else {
                            redisUtil.set(request_id_qd, result_qd.toString(), 0);
                            redisUtil.expire(request_id_qd, 1800, 0);
                        }

                    } else {
                        redisUtil.expire(request_id_qd, 180, 0);
                    }
                }
                break;
            default:
                break;
        }
    }

    public AsyncService getAsyncService() {
        return asyncService;
    }

    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private int updateDBCmdsSeq(EMCmdsSEQPo mNBYDEMCmdsPo) {
//		logger.info("从更新数据库序列号-------------------");
        if (mINBYDEMCmdsService == null) {
            mINBYDEMCmdsService = SpringContextUtils.getBean(INBYDEMCmdsService.class);
        }
        int result = mINBYDEMCmdsService.updatecmdseq(mNBYDEMCmdsPo);
        LOGGER.info("update cmds em_cmds_seq-----result=" + result);
        return result;
    }

    private String getNBResultData(Map<String, String> msgMap) {
        String resultData = null;
        int count = 0;

        //循环查询
        while (resultData == null && count <= 30) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            resultData = getNBReadData(msgMap);
            if (resultData != null) {
                LOGGER.info("------getNBReadData---------count=" + count);
                LOGGER.info("------getNBReadData---------resultData=" + resultData);
                return resultData;
            } else {
                count++;
            }
        }
        return resultData;
    }

    private String getNBReadData(Map<String, String> msgMap) {
        String readValue = null;
        String module_type = msgMap.containsKey("module_type") ? msgMap.get("module_type") : null;
        String imei = msgMap.containsKey("imei") ? msgMap.get("imei") : null;
        String data_type = msgMap.containsKey("data_type") ? msgMap.get("data_type") : null;
        String new_data_seq_hex = msgMap.containsKey("new_data_seq_hex") ? msgMap.get("new_data_seq_hex") : null;
        String value = msgMap.containsKey("value") ? msgMap.get("value") : null;
        if ("akr_nb_em".equals(module_type)) {
            if (mIAKREMDataService == null) {
                mIAKREMDataService = SpringContextUtils.getBean(IAKREMDataService.class);
            }
            List<AKREMReadPo> list = mIAKREMDataService.selectRead(imei, null, data_type);
            LOGGER.info("------getAKRReadData---------list.size()=" + list.size());
            if (list != null && list.size() > 0) {
                AKREMReadPo mAKREMReadPo = list.get(0);
                readValue = mAKREMReadPo.getReadValue();
                LOGGER.info("------getAKRReadData---------readValue=" + readValue);
                long time = mAKREMReadPo.getTime();
                long currentTime = System.currentTimeMillis();

                //小于10分钟的内容返回，如果与当前时间间隔太久，说明是老数据，不返回
                if ((currentTime - time) < 10 * 1000) {
                    return readValue;
                } else {
                    readValue = null;
                }
                return readValue;
            } else {
                return readValue;
            }
        } else {
            if (mIXBEMDataService == null) {
                mIXBEMDataService = SpringContextUtils.getBean(IXBEMDataService.class);
            }
            if ("A1".equals(type) || "A3".equals(type) || "A5".equals(type)) {
                //冻结数据
                int flag_reload = 1;
                int seq_value = Integer.parseInt(value);
                //data_type : nb,lora_em, mqtt_2g
                List<XBEMDataPo> list = mIXBEMDataService.selectbySeq(imei, data_type, flag_reload, seq_value);
                if (list != null && !list.isEmpty() && list.size() > 0) {
                    XBEMDataPo mXBEMDataPo = list.get(0);
                    readValue = mXBEMDataPo.getOrig_value();
                    LOGGER.info("------getNBReadData---------readValue=" + readValue);
                }
            } else if ("B1".equals(type) || "B3".equals(type) || "B5".equals(type) || "B7".equals(type) || "B9".equals(type) || "BB".equals(type) || "BD".equals(type)) {
                //事件记录
                if (mINBYDEMEventService == null) {
                    mINBYDEMEventService = SpringContextUtils.getBean(INBYDEMEventService.class);
                }
                int flag_reload = 1;
                int seq_value = Integer.parseInt(value);
                List<YDEMeterEventPo> list = mINBYDEMEventService.selectEventbySeq(imei, type, flag_reload, seq_value);
                if (list != null && !list.isEmpty() && list.size() > 0) {
                    YDEMeterEventPo mYDEMeterEventPo = list.get(0);
                    readValue = mYDEMeterEventPo.getOrig_value();
                    LOGGER.info("------getNBReadData---------readValue=" + readValue);
                }
            } else {//采集数据
                if (mINBYDEMReadService == null) {
                    mINBYDEMReadService = SpringContextUtils.getBean(INBYDEMReadService.class);
                }
                List<YDEMNBReadPo> list = mINBYDEMReadService.listNBEMRead(imei, type, new_data_seq_hex);
                if (list.size() > 0) {
                    YDEMNBReadPo mYDEMNBReadPo = list.get(0);
                    readValue = mYDEMNBReadPo.getRead_value();
                    long time = mYDEMNBReadPo.getTime();
                    LOGGER.info("------getNBReadData---------readValue=" + readValue);
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime - time) < 1 * 60 * 60 * 1000) {
                        //小于1小时的内容返回，如果与当前时间间隔太久，说明是老数据，不返回
                        return readValue;
                    } else {
                        readValue = null;
                    }
                }
            }
            return readValue;

        }

    }
}
