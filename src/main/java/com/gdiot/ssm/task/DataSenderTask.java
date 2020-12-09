package com.gdiot.ssm.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSON;
import com.gdiot.ssm.entity.GprsDataPo;
import com.gdiot.ssm.entity.LoraDataPo;
import com.gdiot.ssm.entity.SmokeDataPo;
import com.gdiot.ssm.entity.WMDataPo;
import com.gdiot.ssm.entity.WMReadDataPo;
import com.gdiot.ssm.entity.XBEMDataPo;
import com.gdiot.ssm.entity.YDEMNBReadPo;
import com.gdiot.ssm.entity.YDEMeterDataPo;
import com.gdiot.ssm.entity.YDEMeterEventPo;
import com.gdiot.ssm.entity.YDEMeterStatusPo;
import com.gdiot.ssm.lora.LoraClientFactory;
import com.gdiot.ssm.lora.LoraSendCmdsUtils;
import com.gdiot.ssm.mqtt.MqttConfig;
import com.gdiot.ssm.mqtt.MqttSendCmdsUtil;
import com.gdiot.ssm.service.AsyncService;
import com.gdiot.ssm.service.IGprsDataService;
import com.gdiot.ssm.service.ILoraDataService;
import com.gdiot.ssm.service.INBYDEMEventService;
import com.gdiot.ssm.service.INBYDEMReadService;
import com.gdiot.ssm.service.INBYDEMStatusService;
import com.gdiot.ssm.service.INBYDEMeterDataService;
import com.gdiot.ssm.service.ISmokeDataService;
import com.gdiot.ssm.service.IWMDataService;
import com.gdiot.ssm.service.IXBEMDataService;
import com.gdiot.ssm.util.DateUtil;
import com.gdiot.ssm.util.SpringContextUtils;
import com.gdiot.ssm.util.YDUtil;
import com.gdiot.ssm.util.Utilty;

/**
 * @author ZhouHR
 */
public class DataSenderTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSenderTask.class);
    private String data;
    private String data_type;
    private Map<String, String> mapData;
    private AsyncService asyncService;

    //	@Qualifier("nbYDMeterDataService")
    private INBYDEMeterDataService nbYDMeterDataService;
    private INBYDEMStatusService nbYDEMStatusService;
    private ILoraDataService mILoraDataService;
    private IGprsDataService mIGprsDataService;
    private INBYDEMEventService mINBYDEMEventService;
    private INBYDEMReadService mINBYDEMReadService;
    private IXBEMDataService mIXBEMDataService;
    private IWMDataService mIWMDataService;
    private ISmokeDataService mISmokeDataService;

    public DataSenderTask(String data, String type) {
        super();
        this.data = data;
        this.data_type = type;
//		LOGGER.info("task: DataSenderTask data receive: data:"+data);
    }

    public DataSenderTask(Map<String, String> data, String type) {
        super();
        this.mapData = data;
        this.data_type = type;
//		LOGGER.info("task: DataSenderTask data receive: data:"+data);
    }

    @Override
    public void run() {
        LOGGER.info("task: DataSenderTask run-data :" + data);
        LOGGER.info("task: DataSenderTask run-data_type :" + data_type);
        switch (data_type) {
            case "nb":
                try {
                    NBAnalysis();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "mqtt_2g":
                Mqtt2gAnalysis();
                break;
            case "lora_em":
                LoraAnalysis();
                break;
            case "lora_wm":
                LoraWMAnalysis();
                break;
            case "lora_smoke":
                LoraSmokeAnalysis();
                break;
            case "lora_wm_send_cmd":
//			String request_id = dev_id + "_" +System.currentTimeMillis();
//			LoraSendCmds mLoraSendCmds = new LoraSendCmds();
//    		String resultData = mLoraSendCmds.sendMsgToWM(value, dev_id,request_id);
                break;
            case "udp_wm":
                String ip = mapData.get("IP");
                int port = Integer.parseInt(mapData.get("port"));
                String dataStr = mapData.get("data");
                UdpWMAnalysis(dataStr, ip, port);
                break;
            default:
                break;
        }
    }

    /**
     * NB电表数据接收解析
     *
     * @throws JSONException
     */
    private void NBAnalysis() throws JSONException {
        YDUtil.BodyObj obj = YDUtil.resolveBody(data, false);
        org.json.JSONObject data = (org.json.JSONObject) obj.getMsg();
//		LOGGER.info("task: data receive: dev_id:"+data.getLong("dev_id"));
        int type = data.getInt("type");
        if (type == 1) {//value 数据点消息
            // "msg":{"at":1553224758882,"imei":"866971030389733","type":1,"ds_id":"3200_0_5750",
            //"value":"800464000000000000FF2C70063400120103191005010000000020030100003102272241036300005002000080020000810200008201000F16",
            //"dev_id":520156945}

            LOGGER.info("task: data receive: imei:" + data.getString("imei"));
            String ori_value = data.getString("value");
            String dev_id = data.getLong("dev_id") + "";
            String ds_id = (data.getString("ds_id").equals("")) ? "" : data.getString("ds_id");
            String imei = data.getString("imei");
            long time = data.getLong("at");

            LOGGER.info("task: data receive: ori_value:" + ori_value);
            String regex = "^[A-Fa-f0-9]+$";//是16进制数
            if (ori_value.matches(regex)) {
//        		YDEMeterValuePo mYDEMeterValuePo = AnalysisData15(ori_value,mYDEMeterDataPo);//AnalysisData(ori_value);
                int len = ori_value.length();
                String orig_code = ori_value.substring(0, len).trim().replaceAll(" ", "");
//        		LOGGER.info("AnalysisData15: orig_code:"+orig_code);
                byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                len = binaryData.length;
                String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                LOGGER.info("analysis: startbyte:" + startbyte);
                LOGGER.info("analysis: endbyte:" + endbyte);
                if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
//        			System.out.println("AnalysisData15: 80----------------");
                    //表号
                    String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                    LOGGER.info("task: 80 analysis: e_num:" + e_num);
                    String regex_eNum = "^\\d{12}$";//e_num 12
                    if (!e_num.matches(regex_eNum)) {//验证表号是否合法
                        LOGGER.error("e_num error");
                        return;
                    }

                    String e_fac = orig_code.substring(16, 18);
                    LOGGER.info("task: 80 analysis: e_fac=" + e_fac);

                    String dataType = orig_code.substring(18, 20);
                    LOGGER.info("task: 80 analysis: dataType=" + dataType);

                    byte dataLen = binaryData[10];
                    LOGGER.info("task: 80 analysis: data Length=" + dataLen);
                    //数据域
                    String valueD = orig_code.substring(22, 22 + dataLen * 2);
                    //700633081201031910051602000000200375090031024223410315953450021D968002000081020000820100
//        			LOGGER.info("AnalysisData15: valueD:"+ valueD);
                    switch (dataType) {
                        case "A1"://定时上报数据
                        case "FF"://定时上报数据
                            Map<String, String> emMap = new HashMap<String, String>();
                            emMap.put("imei", imei);
                            emMap.put("dev_id", imei);
                            emMap.put("ds_id", ds_id);
                            emMap.put("time", String.valueOf(time));
                            emMap.put("type", String.valueOf(type));
                            emMap.put("ori_value", ori_value);
                            emMap.put("source", data_type);
                            emMap.put("e_num", e_num);
                            emMap.put("e_fac", e_fac);
                            emMap.put("dataType", dataType);
                            emMap.put("flag_reload", "0");
                            SaveXBEMDataToDB(emMap, valueD);//20190618
                            LOGGER.info("task: 80 insert into SQL end!");

                            break;
                        case "B1"://停电事件  返回 C90148000000031900B1005316
                        case "B3"://拉合闸事件  返回 C90147000000031900B3005216
                        case "B5"://过流事件	CA0148000000031900B5004E16
                        case "B7"://过压事件
                        case "B9"://欠压事件
                        case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
                        case "BD"://编程记录最近十次	CC0148000000031900BD004416
                            Map<String, String> eventMap = new HashMap<String, String>();
                            eventMap.put("dev_id", imei);
                            eventMap.put("imei", imei);
                            eventMap.put("e_fac", e_fac);
                            eventMap.put("e_num", e_num);
                            eventMap.put("time", String.valueOf(time));
                            eventMap.put("ori_value", ori_value);
                            eventMap.put("source", data_type);
                            eventMap.put("flag_reload", "0");//上报
                            eventMap.put("data_type", dataType);
                            eventMap.put("deal_flag", "0");
                            SaveEventDataToDB(eventMap, valueD);//zjq 20190719
                            LOGGER.info("task: insert nb event into SQL end!");

                            break;
                        default:
                            break;
                    }
                } else if (!"80".equals(endbyte) /*&& ifUpDateSeq(startbyte)*/ && "16".equals(endbyte)) {//电表响应后台的是CX  "C0".equals(startbyte)
                    //C00100000000000000F801205816
//        			byte[] binaryData = Utilty.hexStringToBytes(data);

                    String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                    LOGGER.info("task: C0 analysis: e_num:" + e_num);

                    String e_fac = orig_code.substring(16, 18);
                    LOGGER.info("task: C0 analysis: e_fac=" + e_fac);

                    String dataType = orig_code.substring(18, 20);
                    LOGGER.info("task: C0 analysis: dataType=" + dataType);

                    byte dataLen = binaryData[10];
                    LOGGER.info("task: C0 analysis: data Length=" + dataLen);

                    //数据域
                    String valueD = orig_code.substring(22, 22 + dataLen * 2);

                    switch (dataType) {
                        //定时上报数据 冻结数据
                        case "A1"://读取丢帧项
                        case "FF"://读取多项数据
                        case "A3"://第几次日冻结数据
                        case "A5"://第几次月冻结数据
                            //C00D39000000031900A332A20201007006071111150419100400000000F8012120030000003102632241030700005002000080021800810200808201AA5516
                            //C00D47000000031900A134A003340200700627421811041910050000000000F8012420030000003102762241030700005002000080020000810200808201551516
                            //加flag

                            Map<String, String> emMap = new HashMap<String, String>();
                            emMap.put("imei", imei);
                            emMap.put("dev_id", imei);
                            emMap.put("ds_id", ds_id);
                            emMap.put("time", String.valueOf(time));
                            emMap.put("type", String.valueOf(type));
                            emMap.put("ori_value", ori_value);
                            emMap.put("source", data_type);
                            emMap.put("e_num", e_num);
                            emMap.put("e_fac", e_fac);
                            emMap.put("dataType", dataType);
                            emMap.put("flag_reload", "1");
                            SaveXBEMDataToDB(emMap, valueD);//20190618
                            LOGGER.info("task: CX insert into SQL end!");

                            break;
                        //事件数据
                        case "B1"://停电事件
                        case "B3"://拉合闸事件
                        case "B5"://过流事件
                        case "B7"://过压事件
                        case "B9"://欠压事件
                        case "BB"://重启记录最近十次
                        case "BD"://编程记录最近十次

                            Map<String, String> eventMap = new HashMap<String, String>();
                            eventMap.put("dev_id", imei);
                            eventMap.put("imei", imei);
                            eventMap.put("e_fac", e_fac);
                            eventMap.put("e_num", e_num);
                            eventMap.put("time", String.valueOf(time));
                            eventMap.put("ori_value", ori_value);
                            eventMap.put("source", data_type);
                            eventMap.put("flag_reload", "1");//下行
                            eventMap.put("data_type", dataType);
                            eventMap.put("deal_flag", "0");
                            SaveEventDataToDB(eventMap, valueD);//zjq 20190719
                            LOGGER.info("task: insert nb event into SQL end!");

                            break;
                        //事件次数
                        case "B0"://停电事件总次数 //C00147000000031900B00203005916
                        case "B2"://拉合闸总次数 //C00147000000031900B20209005116
                        case "B4"://过流总数 //C00147000000031900B40200005816
                        case "B6"://过压事件总数 //C00147000000031900B60200005616
                        case "B8"://欠压事件次数 //C00147000000031900B80200005416
                        case "BA"://模块重启次数 //C00147000000031900BA0200005216
                        case "BC"://编程记录次数
                        case "A0"://整点冻结次数 //C00147000000031900A0032802004116
                        case "A2"://日冻结次数 //C00147000000031900A20203006716
                        case "A4"://月冻结次数 //C00147000000031900A401016816
                            //抄表
                        case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
                        case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
                        case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
                        case "F8"://信号强度 //C00147000000031900F80124F116
                        case "F9"://驻网状态
                        case "FA"://模块监控状态字
                        case "10"://有功总电能 //C0014700000003190010050000000000F916
                        case "11"://A相有功电能 //C0014700000003190010050000000000F916
                        case "12"://B相有功电能
                        case "13"://C相有功电能
                        case "20"://总有功功率
                        case "21"://A相有功功率
                        case "22"://B相有功功率
                        case "23"://C相有功功率
                        case "31"://A相电压 //C00147000000031900310258226116
                        case "32"://B相电压
                        case "33"://C相电压
                        case "41"://A相电流 //C001470000000319004103070000C316
                        case "42"://B相电流
                        case "43"://C相电流
                        case "50"://总功率因数
                        case "60"://频率
                        case "70"://时间
                        case "71"://注册标识
                        case "72"://上报时间
                        case "73"://认证时长
                        case "74"://心跳间隔
                        case "80"://电表运行状态字1
                        case "81"://电表运行状态字2
                        case "82"://继电器控制字
                        case "83"://电表常数
                        case "90"://厂家标识
                        case "91"://硬件版本号
                        case "92"://软件版本号
                        case "93"://协议版本号
                        case "94"://表号
                        case "95"://通讯号
                        case "96"://用户号
                            LOGGER.info("task: c0 start analysis-----------dataType=" + dataType);
                            if (mINBYDEMReadService == null) {
                                mINBYDEMReadService = SpringContextUtils.getBean(INBYDEMReadService.class);
                            }
                            YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
                            Map<String, String> result_read = EMDataAnalysisUtil.getReadValue(dataType, valueD);
                            mYDEMNBReadPo.setDev_id(imei);
                            mYDEMNBReadPo.setImei(imei);
                            mYDEMNBReadPo.setOrig_value(ori_value);
                            mYDEMNBReadPo.setSource(data_type);
                            mYDEMNBReadPo.setTime(time);
                            mYDEMNBReadPo.setE_num(e_num);
                            mYDEMNBReadPo.setE_fac(e_fac);
                            mYDEMNBReadPo.setData_seq(startbyte);
                            mYDEMNBReadPo.setRead_type(result_read.get("read_type") != null ? result_read.get("read_type") : "");
                            mYDEMNBReadPo.setRead_value(result_read.get("read_value") != null ? result_read.get("read_value") : "");
                            mINBYDEMReadService.addOne(mYDEMNBReadPo);
                            LOGGER.info("task: c0 insert into SQL end!");
                            break;
                    }
                } else if ("90".equals(startbyte) && "16".equals(endbyte)) {

                    String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                    LOGGER.info("task: C0 analysis: e_num:" + e_num);

                    String e_fac = orig_code.substring(16, 18);
                    LOGGER.info("task: C0 analysis: e_fac=" + e_fac);

                    String dataType = orig_code.substring(18, 20);
                    LOGGER.info("task: C0 analysis: dataType=" + dataType);

                    byte dataLen = binaryData[10];
                    LOGGER.info("task: C0 analysis: data Length=" + dataLen);

                    //数据域
                    String valueD = orig_code.substring(22, 22 + dataLen * 2);

                    switch (dataType) {
                        //定时上报数据 冻结数据
                        case "A1"://读取丢帧项
                            Map<String, String> emMap9 = new HashMap<String, String>();
                            emMap9.put("imei", imei);
                            emMap9.put("dev_id", imei);
                            emMap9.put("ds_id", ds_id);
                            emMap9.put("time", String.valueOf(time));
                            emMap9.put("type", String.valueOf(type));
                            emMap9.put("ori_value", ori_value);
                            emMap9.put("source", data_type);
                            emMap9.put("e_num", e_num);
                            emMap9.put("e_fac", e_fac);
                            emMap9.put("flag_reload", "0");
                            SaveNBDataToDB(emMap9, valueD);//20190618
                            LOGGER.info("task: 90 insert into SQL end!");
                            //zhangjieqiong
                            break;
                        default:
                            break;
                    }
                }
            }
        } else if (type == 2) {//login 设备上下线消息
            //"msg":{"at":1553224749838,"login_type":10,"imei":"866971030389733","type":2,"dev_id":520156945,"status":1}
            if (nbYDEMStatusService == null) {
                nbYDEMStatusService = SpringContextUtils.getBean(INBYDEMStatusService.class);
            }
            String dev_id = data.getLong("dev_id") + "";
            String imei = data.getString("imei");
            int status = data.getInt("status");
            int login_type = data.getInt("login_type");
            long time = data.getLong("at");
            LOGGER.info("task: : nbYDEMStatusService : dev_id=" + dev_id);
            LOGGER.info("task: : nbYDEMStatusService : imei=" + imei);
            LOGGER.info("task: : nbYDEMStatusService : 设备上下线 status=" + status);
            LOGGER.info("task: : nbYDEMStatusService : login_type=" + login_type);
            LOGGER.info("task: : nbYDEMStatusService : time=" + time);

            YDEMeterStatusPo mYDEMeterStatusPo = new YDEMeterStatusPo();
            mYDEMeterStatusPo.setDev_id(dev_id);
            mYDEMeterStatusPo.setImei(imei);
            mYDEMeterStatusPo.setType(type);
            mYDEMeterStatusPo.setLogin_type(login_type);
            mYDEMeterStatusPo.setStatus(status);
            mYDEMeterStatusPo.setTime(time);
            LOGGER.info("task: : nbYDEMStatusService: in insertStatus");
            List<YDEMeterStatusPo> list = nbYDEMStatusService.selectDevid(dev_id, imei);
            if (list.size() >= 1) {//存在，更新
                nbYDEMStatusService.updateEMStatus(mYDEMeterStatusPo);
            } else {//数据库中不存在，插入
                nbYDEMStatusService.insertStatus(mYDEMeterStatusPo);
            }
            LOGGER.info("task: status insert into SQL end!");
        }
    }

    /**
     * lora电表数据接收解析
     */
    public void LoraAnalysis() {
//		LOGGER.info("============= Lora Date start analysis=============");
        try {
            com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
            if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
                String dev_eui = jo.getString("devEUI");
                String ori_value = jo.getString("data");
                String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
                long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");

                LOGGER.info("lora data LoraAnalysis:dev_eui=" + dev_eui);
                LOGGER.info("lora data LoraAnalysis:data=" + ori_value);
                LOGGER.info("lora data LoraAnalysis:time_s=" + time_s);
                int len = ori_value.length();
                String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");

                String regex = "^[A-Fa-f0-9]+$";//是16进制数
                if (orig_data.matches(regex)) {
                    String dataFlag = orig_data.substring(0, 8);
                    LOGGER.info("task: lora data receive: dataFlag:" + dataFlag);
                    String orig_code = orig_data;
                    if ("FEFEFEFE".equals(dataFlag)) {//lora 数据
                        orig_code = orig_data.substring(8, orig_data.length());
//			    		System.out.println("task: data receive: orig_code:"+orig_code);
                        byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                        len = binaryData.length;
                        String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                        String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                        LOGGER.info("analysis: startbyte:" + startbyte);
                        LOGGER.info("analysis: endbyte:" + endbyte);
                        if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
                            //System.out.println("AnalysisData15: 80----------------");
                            //表号
                            String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                            LOGGER.info("task: lora analysis: e_num:" + e_num);

                            String e_fac = orig_code.substring(16, 18);
                            LOGGER.info("task: lora analysis: e_fac=" + e_fac);

                            String dataType = orig_code.substring(18, 20);
                            LOGGER.info("task: lora analysis: dataType=" + dataType);

                            byte dataLen = binaryData[10];
                            LOGGER.info("task: lora analysis: data Length=" + dataLen);

                            if ("70".equals(dataType) && (dataLen == 0)) {
                                String msgStr = LoraSendCmdsUtils.getTimeContent(dev_eui, e_num, e_fac);
                                LOGGER.info(String.format("sendData data:%s", msgStr));
                                LoraClientFactory.getInstance(null).getClient().sendMsg(msgStr);
                            } else {
                                //数据域
                                String valueD = orig_code.substring(22, 22 + dataLen * 2);
                                switch (dataType) {
                                    case "A1"://定时上报数据
                                    case "FF"://定时上报数据
                                    case "A3"://第几次日冻结数据
                                    case "A5"://第几次月冻结数据
                                        Map<String, String> emMap9 = new HashMap<String, String>();
                                        emMap9.put("dev_id", dev_eui);
                                        emMap9.put("time", String.valueOf(time));
                                        emMap9.put("ori_value", orig_data);
                                        emMap9.put("source", data_type);
                                        emMap9.put("e_num", e_num);
                                        emMap9.put("e_fac", e_fac);
                                        emMap9.put("flag_reload", "0");
                                        emMap9.put("dataType", dataType);
                                        SaveXBEMDataToDB(emMap9, valueD);//zjq 20190705
                                        LOGGER.info("task: lora fefefefe 80 insert into SQL end!");

                                        break;
                                    case "B1"://停电事件  返回 C90148000000031900B1005316
                                    case "B3"://拉合闸事件  返回 C90147000000031900B3005216
                                    case "B5"://过流事件	CA0148000000031900B5004E16
                                    case "B7"://过压事件
                                    case "B9"://欠压事件
                                    case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
                                    case "BD"://编程记录最近十次	CC0148000000031900BD004416

                                        Map<String, String> eventMap = new HashMap<String, String>();
                                        eventMap.put("dev_id", dev_eui);
                                        eventMap.put("imei", dev_eui);
                                        eventMap.put("e_fac", e_fac);
                                        eventMap.put("e_num", e_num);
                                        eventMap.put("time", String.valueOf(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss")));
                                        eventMap.put("ori_value", orig_data);
                                        eventMap.put("source", data_type);
                                        eventMap.put("flag_reload", "0");//上报
                                        eventMap.put("data_type", dataType);
                                        eventMap.put("deal_flag", "0");
                                        SaveEventDataToDB(eventMap, valueD);//zjq 20190705
                                        LOGGER.info("task: insert lora event into SQL end!");

                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else if (!"80".equals(endbyte) && "16".equals(endbyte)) {//下行返回
                            String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                            LOGGER.info("task: C0 analysis: e_num:" + e_num);

                            String e_fac = orig_code.substring(16, 18);
                            LOGGER.info("task: C0 analysis: e_fac=" + e_fac);

                            String dataType = orig_code.substring(18, 20);
                            LOGGER.info("task: C0 analysis: dataType=" + dataType);

                            byte dataLen = binaryData[10];
                            LOGGER.info("task: C0 analysis: data Length=" + dataLen);

                            //数据域
                            String valueD = orig_code.substring(22, 22 + dataLen * 2);

                            switch (dataType) {
                                //定时上报数据 冻结数据
                                case "A1"://读取丢帧项
                                case "FF"://读取多项数据
                                case "A3"://第几次日冻结数据
                                case "A5"://第几次月冻结数据
                                    //C00D39000000031900A332A20201007006071111150419100400000000F8012120030000003102632241030700005002000080021800810200808201AA5516
                                    //C00D47000000031900A134A003340200700627421811041910050000000000F8012420030000003102762241030700005002000080020000810200808201551516
                                    //加flag

                                    Map<String, String> emMap9 = new HashMap<String, String>();
                                    emMap9.put("dev_id", dev_eui);
                                    emMap9.put("time", String.valueOf(time));
                                    emMap9.put("ori_value", orig_data);
                                    emMap9.put("source", data_type);
                                    emMap9.put("e_num", e_num);
                                    emMap9.put("e_fac", e_fac);
                                    emMap9.put("flag_reload", "1");
                                    emMap9.put("dataType", dataType);
                                    SaveXBEMDataToDB(emMap9, valueD);//zjq 20190705
                                    LOGGER.info("task: lora fefefefe 80 insert into SQL end!");

                                    break;
                                //事件数据
                                case "B1"://停电事件
                                case "B3"://拉合闸事件
                                case "B5"://过流事件
                                case "B7"://过压事件
                                case "B9"://欠压事件
                                case "BB"://重启记录最近十次
                                case "BD"://编程记录最近十次

                                    Map<String, String> eventMap = new HashMap<String, String>();
                                    eventMap.put("dev_id", dev_eui);
                                    eventMap.put("imei", dev_eui);
                                    eventMap.put("e_fac", e_fac);
                                    eventMap.put("e_num", e_num);
                                    eventMap.put("time", String.valueOf(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss")));
                                    eventMap.put("ori_value", orig_data);
                                    eventMap.put("source", data_type);
                                    eventMap.put("flag_reload", "1");//上报
                                    eventMap.put("data_type", dataType);
                                    eventMap.put("deal_flag", "0");
                                    SaveEventDataToDB(eventMap, valueD);//zjq 20190705
                                    LOGGER.info("task: insert lora event into SQL end!");

                                    break;
                                //事件次数
                                case "B0"://停电事件总次数 //C00147000000031900B00203005916
                                case "B2"://拉合闸总次数 //C00147000000031900B20209005116
                                case "B4"://过流总数 //C00147000000031900B40200005816
                                case "B6"://过压事件总数 //C00147000000031900B60200005616
                                case "B8"://欠压事件次数 //C00147000000031900B80200005416
                                case "BA"://模块重启次数 //C00147000000031900BA0200005216
                                case "BC"://编程记录次数
                                case "A0"://整点冻结次数 //C00147000000031900A0032802004116
                                case "A2"://日冻结次数 //C00147000000031900A20203006716
                                case "A4"://月冻结次数 //C00147000000031900A401016816
                                    //抄表
                                case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
                                case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
                                case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
                                case "F8"://信号强度 //C00147000000031900F80124F116
                                case "F9"://驻网状态
                                case "FA"://模块监控状态字
                                case "10"://有功总电能 //C0014700000003190010050000000000F916
                                case "11"://A相有功电能 //C0014700000003190010050000000000F916
                                case "12"://B相有功电能
                                case "13"://C相有功电能
                                case "20"://总有功功率
                                case "21"://A相有功功率
                                case "22"://B相有功功率
                                case "23"://C相有功功率
                                case "31"://A相电压 //C00147000000031900310258226116
                                case "32"://B相电压
                                case "33"://C相电压
                                case "41"://A相电流 //C001470000000319004103070000C316
                                case "42"://B相电流
                                case "43"://C相电流
                                case "50"://总功率因数
                                case "60"://频率
                                case "70"://时间
                                case "71"://注册标识
                                case "72"://上报时间
                                case "73"://认证时长
                                case "74"://心跳间隔
                                case "80"://电表运行状态字1
                                case "81"://电表运行状态字2
                                case "82"://继电器控制字
                                case "83"://电表常数
                                case "90"://厂家标识
                                case "91"://硬件版本号
                                case "92"://软件版本号
                                case "93"://协议版本号
                                case "94"://表号
                                case "95"://通讯号
                                case "96"://用户号
                                    LOGGER.info("task: c0 start analysis-----------dataType=" + dataType);
                                    if (mINBYDEMReadService == null) {
                                        mINBYDEMReadService = SpringContextUtils.getBean(INBYDEMReadService.class);
                                    }
                                    YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
                                    Map<String, String> result_read = EMDataAnalysisUtil.getReadValue(dataType, valueD);
                                    mYDEMNBReadPo.setDev_id(dev_eui);
                                    mYDEMNBReadPo.setImei(dev_eui);
                                    mYDEMNBReadPo.setOrig_value(orig_code);
                                    mYDEMNBReadPo.setSource(data_type);
                                    mYDEMNBReadPo.setTime(DateUtil.date2TimeStampLong_l(time_s, "yyyy-MM-dd HH:mm:ss"));
                                    mYDEMNBReadPo.setE_num(e_num);
                                    mYDEMNBReadPo.setE_fac(e_fac);
                                    mYDEMNBReadPo.setData_seq(startbyte);
                                    mYDEMNBReadPo.setRead_type(result_read.get("read_type") != null ? result_read.get("read_type") : "");
                                    mYDEMNBReadPo.setRead_value(result_read.get("read_value") != null ? result_read.get("read_value") : "");
                                    mINBYDEMReadService.addOne(mYDEMNBReadPo);
                                    LOGGER.info("task: c0 insert into SQL end!");
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * lora水表数据接收解析
     */
    public void LoraWMAnalysis() {
        //收 读计量数据2：
        //FE FE FE 68 10 01 13 12 19 00 00 00
        //A1 1D 90 1F 01 2C 00 00 00 00 2C 00 00 00 00 35 00 00 00 00 41 20 14 16 12 19 20 40 21 00 02 EB 16
        try {
            com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
            if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
                String dev_eui = jo.getString("devEUI");
                String ori_value = jo.getString("data");
                String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
                long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");

                LOGGER.info("lora data LoraAnalysis:dev_eui=" + dev_eui);
                LOGGER.info("lora data LoraAnalysis:data=" + ori_value);
                LOGGER.info("lora data LoraAnalysis:time_s=" + time_s);
                int len = ori_value.length();
                String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x

                String regex = "^[A-Fa-f0-9]+$";//是16进制数
                if (orig_data.matches(regex)) {
                    String dataFlag = orig_data.substring(0, 6);
                    LOGGER.info("task: lora data receive: dataFlag:" + dataFlag);
                    String orig_code = orig_data;
                    if ("FEFEFE".equals(dataFlag)) {//lora 数据
                        orig_code = orig_data.substring(6, orig_data.length());
//			    		System.out.println("task: data receive: orig_code:"+orig_code);
                        byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                        len = binaryData.length;
                        String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                        String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                        LOGGER.info("analysis: startbyte:" + startbyte);
                        LOGGER.info("analysis: endbyte:" + endbyte);
                        if ("68".equals(startbyte) && "16".equals(endbyte)) {//水表上报发出的是68
                            //System.out.println("AnalysisData15: 80----------------");
                            //表号
                            String w_num = Utilty.convertByteToString(binaryData, 3, 9);
                            System.out.println("task: lora analysis: e_num:" + w_num);
                            //FE FE FE 68 10 01 13 12 19 00 00 00
                            //A1 1D 90 1F 01 2C 00 00 00 00 2C 00 00 00 00 35 00 00 00 00 41 20 14 16 12 19 20 40 21 00 02 EB 16
                            String control_code = orig_code.substring(18, 20);
                            System.out.println("task: lora analysis: control_code=" + control_code);
                            //\xFEFEFE681001131219000000A11D901F012C000000002C00000000350000000056201817121920402200030716
                            int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
                            System.out.println("task: lora analysis: data Length=" + dataLen);

                            //数据域
                            String valueD = orig_code.substring(22, 22 + dataLen * 2);
                            System.out.println("task: lora fefefe 68 valueD=" + valueD);
                            switch (control_code) {
                                case "A1"://读计量数据2
                                    if (mIWMDataService == null) {
                                        mIWMDataService = SpringContextUtils.getBean(IWMDataService.class);
                                    }
                                    WMDataPo wmDataPo = new WMDataPo();
                                    Map<String, String> result_read = EMDataAnalysisUtil.getWMDataValue(valueD);
                                    wmDataPo.setWmNum(w_num);
                                    wmDataPo.setTime(time);
                                    wmDataPo.setOrigValue(orig_data);
                                    wmDataPo.setDevId(dev_eui);
                                    wmDataPo.setSource(data_type);
                                    wmDataPo.setDataFlag1(result_read.get("data_flag1"));
                                    wmDataPo.setDataFlag2(result_read.get("data_flag2"));
                                    wmDataPo.setDataSeq(result_read.get("data_seq"));
                                    wmDataPo.setWmFlow(result_read.get("wm_flow"));
                                    wmDataPo.setWmFlowRate(result_read.get("wm_flow_rate"));
                                    wmDataPo.setWmReverseFlow(result_read.get("wm_reverse_flow"));
                                    wmDataPo.setWmTime(result_read.get("wm_time"));
                                    wmDataPo.setWmStatu1(result_read.get("wm_statu1"));
                                    wmDataPo.setWmStatu2(result_read.get("wm_statu2"));
                                    mIWMDataService.addOne(wmDataPo);
                                    System.out.println("task: lora fefefe 68 insert into SQL end!");

                                    break;
                                case "81"://读计量数据1
                                    break;
                                case "83"://读表头地址
                                    break;
                                case "84"://强制开关阀,自由开关阀
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * 2G电表数据接收解析 mqtt方式接收
     */
    private void Mqtt2gAnalysis() {
        LOGGER.info("============= GPRS Date start analysis=============");
        com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
        if (jo.containsKey("imei") && jo.containsKey("data") && jo.containsKey("type")) {
            String imei = jo.getString("imei");
            String ori_value = jo.getString("data");
            String time_s = jo.getString("time");
            long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");
            String type = jo.getString("type");//emeterGprsData

            LOGGER.info("GPRS data LoraAnalysis:imei=" + imei);
            LOGGER.info("GPRS data LoraAnalysis:data=" + ori_value);
            LOGGER.info("GPRS data LoraAnalysis:time_s=" + time_s);

            LOGGER.info("task: data receive: ori_value:" + ori_value);
            String regex = "^[A-Fa-f0-9]+$";//是16进制数
            if (ori_value.matches(regex)) {
                int len = ori_value.length();
                String orig_code = ori_value.substring(0, len).trim().replaceAll(" ", "");
//	        	LOGGER.info("AnalysisData15: orig_code:"+orig_code);
                byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                len = binaryData.length;
                String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                LOGGER.info("analysis: startbyte:" + startbyte);
                LOGGER.info("analysis: endbyte:" + endbyte);
                if ("80".equals(startbyte) && "16".equals(endbyte)) {//电表启动发出的是8X，电表响应后台的是CX
//	        		System.out.println("AnalysisData15: 80----------------");
                    String e_num = Utilty.convertByteToString(binaryData, 3, 8);//表号
                    LOGGER.info("task: 80 analysis: e_num:" + e_num);
                    String e_fac = orig_code.substring(16, 18);
                    LOGGER.info("task: 80 analysis: e_fac=" + e_fac);
                    String dataType = orig_code.substring(18, 20);
                    LOGGER.info("task: 80 analysis: dataType=" + dataType);
                    byte dataLen = binaryData[10];
                    LOGGER.info("task: 80 analysis: data Length=" + dataLen);
                    //数据域
                    String valueD = orig_code.substring(22, 22 + dataLen * 2);
//	        		LOGGER.info("AnalysisData15: valueD:"+ valueD);
                    switch (dataType) {
                        case "70"://验证订阅
                            JSONObject msg = new JSONObject();
                            msg.put("imei", imei);
                            msg.put("time", DateUtil.timeStamp2Date(String.valueOf(System.currentTimeMillis() / 1000), null));
                            msg.put("data", "sub topic ok");
                            msg.put("type", "emeterGprsData");

                            String topic = MqttConfig.TOPIC_SERVER + "/" + imei;
                            MqttSendCmdsUtil mMqttSendCmdsUtil = new MqttSendCmdsUtil();
                            JSONObject result = mMqttSendCmdsUtil.MqttSendCmds(topic, msg);
                            LOGGER.info("70 mqtt 验证订阅 result=" + result);
//        				LOGGER.info(String.format("sendData,topic:%s,message:%s", topic,msg));
//        				if(MQTTClientFactory.getInstance(null).getClient().pubMessage(topic, msg)) {
//        					LOGGER.info("pub msg success");
//        				}else {
//        					LOGGER.info("pub msg fail");
//        				}
                            break;
                        case "A1"://定时上报数据
                        case "FF"://定时上报数据
                            Map<String, String> emMap = new HashMap<String, String>();
                            emMap.put("dev_id", imei);
                            emMap.put("time", String.valueOf(time));
                            emMap.put("ori_value", ori_value);
                            emMap.put("source", data_type);
                            emMap.put("e_num", e_num);
                            emMap.put("e_fac", e_fac);
                            emMap.put("dataType", dataType);
                            emMap.put("flag_reload", "0");
                            SaveXBEMDataToDB(emMap, valueD);//20190618
                            LOGGER.info("task: 80 gprs insert into SQL end!");

                            break;
                        case "B1"://停电事件  返回 C90148000000031900B1005316
                        case "B3"://拉合闸事件  返回 C90147000000031900B3005216
                        case "B5"://过流事件	CA0148000000031900B5004E16
                        case "B7"://过压事件
                        case "B9"://欠压事件
                        case "BB"://重启记录最近十次  E80D48000000031900BB22BA0281177006313315240419FA030503FFBA0281177006213515240419FA0304FFFFFF16
                        case "BD"://编程记录最近十次	CC0148000000031900BD004416
                            Map<String, String> eventMap = new HashMap<String, String>();
                            eventMap.put("dev_id", imei);
                            eventMap.put("imei", imei);
                            eventMap.put("e_fac", e_fac);
                            eventMap.put("e_num", e_num);
                            eventMap.put("time", String.valueOf(time));
                            eventMap.put("ori_value", ori_value);
                            eventMap.put("source", data_type);
                            eventMap.put("flag_reload", "0");//上报
                            eventMap.put("data_type", dataType);
                            eventMap.put("deal_flag", "0");
                            SaveEventDataToDB(eventMap, valueD);//zjq 20190719
                            LOGGER.info("task: insert gprs event into SQL end!");

                            break;
                        default:
                            break;
                    }
                } else if (!"80".equals(endbyte) && "16".equals(endbyte)) {//电表响应后台的是CX  "C0".equals(startbyte)
                    String e_num = Utilty.convertByteToString(binaryData, 3, 8);
                    LOGGER.info("task: C0 analysis: e_num:" + e_num);
                    String e_fac = orig_code.substring(16, 18);
                    LOGGER.info("task: C0 analysis: e_fac=" + e_fac);
                    String dataType = orig_code.substring(18, 20);
                    LOGGER.info("task: C0 analysis: dataType=" + dataType);
                    byte dataLen = binaryData[10];
                    LOGGER.info("task: C0 analysis: data Length=" + dataLen);
                    //数据域
                    String valueD = orig_code.substring(22, 22 + dataLen * 2);

                    switch (dataType) {
                        //定时上报数据 冻结数据
                        case "A1"://读取丢帧项
                        case "FF"://读取多项数据
                        case "A3"://第几次日冻结数据
                        case "A5"://第几次月冻结数据
                            Map<String, String> emMap = new HashMap<String, String>();
                            emMap.put("dev_id", imei);
                            emMap.put("time", String.valueOf(time));
                            emMap.put("ori_value", ori_value);
                            emMap.put("source", data_type);
                            emMap.put("e_num", e_num);
                            emMap.put("e_fac", e_fac);
                            emMap.put("dataType", dataType);
                            emMap.put("flag_reload", "1");
                            SaveXBEMDataToDB(emMap, valueD);//20190618
                            LOGGER.info("task: CX gprs insert into SQL end!");

                            break;
                        //事件数据
                        case "B1"://停电事件
                        case "B3"://拉合闸事件
                        case "B5"://过流事件
                        case "B7"://过压事件
                        case "B9"://欠压事件
                        case "BB"://重启记录最近十次
                        case "BD"://编程记录最近十次
                            Map<String, String> eventMap = new HashMap<String, String>();
                            eventMap.put("dev_id", imei);
                            eventMap.put("imei", imei);
                            eventMap.put("e_fac", e_fac);
                            eventMap.put("e_num", e_num);
                            eventMap.put("time", String.valueOf(time));
                            eventMap.put("ori_value", ori_value);
                            eventMap.put("source", data_type);
                            eventMap.put("flag_reload", "1");//下行
                            eventMap.put("data_type", dataType);
                            eventMap.put("deal_flag", "0");
                            SaveEventDataToDB(eventMap, valueD);//zjq 20190719
                            LOGGER.info("task: insert gprs event into SQL end!");

                            break;
                        //抄表
                        case "10"://有功总电能 //C0014700000003190010050000000000F916
//        			case "11"://A相有功电能 //C0014700000003190010050000000000F916
//        			case "12"://B相有功电能 
//        			case "13"://C相有功电能
                        case "14"://有功余脉冲数
                        case "15"://无功总电能
                        case "19"://无功余脉冲数
                        case "1F"://电能读块
                        case "20"://总有功功率
                        case "21"://A相有功功率
                        case "22"://B相有功功率
                        case "23"://C相有功功率
                        case "24"://总无功功率
                        case "25"://A相无功功率
                        case "26"://B相无功功率
                        case "27"://C相无功功率
                        case "2F"://功率读块
                        case "31"://A相电压 //C00147000000031900310258226116
                        case "32"://B相电压
                        case "33"://C相电压
                        case "3F"://电压读块
                        case "41"://A相电流 //C001470000000319004103070000C316
                        case "42"://B相电流
                        case "43"://C相电流
                        case "4F"://电流读块
                        case "50"://总功率因数
                        case "51"://A相功率因数
                        case "52"://B相功率因数
                        case "53"://C相功率因数
                        case "5F"://功率因数读块
                        case "60"://频率
                        case "70"://时间
                        case "71"://注册标识
                        case "72"://上报时间
                        case "73"://认证时长
                        case "74"://心跳间隔
                        case "80"://电表运行状态字1
                        case "81"://电表运行状态字2
                        case "82"://继电器控制字
                        case "83"://电表常数
                        case "90"://厂家标识
                        case "91"://硬件版本号
                        case "92"://软件版本号
                        case "93"://协议版本号
                        case "94"://表号
                        case "95"://通讯号
                        case "96"://用户号
                            //事件次数
                        case "A0"://整点冻结次数 //C00147000000031900A0032802004116
                        case "A2"://日冻结次数 //C00147000000031900A20203006716
                        case "A4"://月冻结次数 //C00147000000031900A401016816
                        case "B0"://停电事件总次数 //C00147000000031900B00203005916
                        case "B2"://拉合闸总次数 //C00147000000031900B20209005116
                        case "B4"://过流总数 //C00147000000031900B40200005816
                        case "B6"://过压事件总数 //C00147000000031900B60200005616
                        case "B8"://欠压事件次数 //C00147000000031900B80200005416
                        case "BA"://模块重启次数 //C00147000000031900BA0200005216
                        case "BC"://编程记录次数
                            //抄表
                        case "F1"://读RAM的XXXXXXXX地址后的N字节
                        case "F2"://读ROM的XXXXXXXX地址后的N字节
                        case "F3"://读EEPROM的XXXXXXXX地址后的N字节
                        case "F4"://读计量芯片指定寄存器PP页，RR寄存器地址数据
                        case "F5"://IMSI ASCII //C00147000000031900F50F3436303034353539333130323938310116
                        case "F6"://IMEI  ASCII //C00147000000031900F60F383636393731303330333839333337F016
                        case "F7"://ICCID ASCII //C00147000000031900F7143839383630343335313031383930303532393830ED16
                        case "F8"://信号强度 //C00147000000031900F80124F116
                        case "F9"://驻网状态
                        case "FA"://模块监控状态字
                            LOGGER.info("task: c0 start analysis-----------dataType=" + dataType);
                            if (mINBYDEMReadService == null) {
                                mINBYDEMReadService = SpringContextUtils.getBean(INBYDEMReadService.class);
                            }
                            YDEMNBReadPo mYDEMNBReadPo = new YDEMNBReadPo();
                            Map<String, String> result_read = EMDataAnalysisUtil.getReadValue(dataType, valueD);
                            mYDEMNBReadPo.setDev_id(imei);
                            mYDEMNBReadPo.setImei(imei);
                            mYDEMNBReadPo.setOrig_value(ori_value);
                            mYDEMNBReadPo.setSource(data_type);
                            mYDEMNBReadPo.setTime(time);
                            mYDEMNBReadPo.setE_num(e_num);
                            mYDEMNBReadPo.setE_fac(e_fac);
                            mYDEMNBReadPo.setData_seq(startbyte);
                            mYDEMNBReadPo.setRead_type(result_read.get("read_type") != null ? result_read.get("read_type") : "");
                            mYDEMNBReadPo.setRead_value(result_read.get("read_value") != null ? result_read.get("read_value") : "");
                            mINBYDEMReadService.addOne(mYDEMNBReadPo);
                            LOGGER.info("task: c0 gprs insert into SQL end!");
                            break;
                    }
                }
            }
        }
    }

    /**
     * lora 烟感数据接收解析
     */
    private void LoraSmokeAnalysis() {
        LOGGER.info("--------------lora smoke sensor start --------------");
        try {
            com.alibaba.fastjson.JSONObject jo = JSON.parseObject(data);
            if (jo.containsKey("devEUI") && jo.containsKey("data")) {
//				LOGGER.info("lora data =====================" );
                String dev_eui = jo.getString("devEUI");
                String ori_value = jo.getString("data");
                String time_s = DateUtil.shortenTimeStr(jo.getString("time_s"));//2019-04-09 08:24:09.120570725
                long time = DateUtil.date2TimeStampLong(time_s, "yyyy-MM-dd HH:mm:ss");

                LOGGER.info("lora smoke sensor LoraAnalysis:dev_eui=" + dev_eui);
                LOGGER.info("lora smoke sensor LoraAnalysis:data=" + ori_value);
                LOGGER.info("lora smoke sensor LoraAnalysis:time_s=" + time_s);
                int len = ori_value.length();
                String orig_data = ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x

                String regex = "^[A-Fa-f0-9]+$";//是16进制数
                if (orig_data.matches(regex)) {
                    //\x303230453030
                    String dataFlag = orig_data.substring(0, 6);
                    String dataStatus = orig_data.substring(6, 8);
                    String dataEnd = orig_data.substring(8, 12);
                    String dataStatus_type = "";
                    LOGGER.info("task: lora smoke sensor receive: dataFlag:" + dataFlag);
                    String orig_code = orig_data;
                    if ("303230".equals(dataFlag) && "3030".equals(dataEnd)) {//水表上报发出的是68
                        //System.out.println("AnalysisData15: 80----------------");
                        LOGGER.info("task: lora smoke sensor dataStatus=" + dataStatus);
                        switch (dataStatus) {
                            case "30"://正常
                                dataStatus_type = "正常";
                                break;
                            case "31"://火警
                                dataStatus_type = "火警";
                                break;
                            case "33"://低电
                                dataStatus_type = "低电";
                                break;
                            case "34"://火警消除
                                dataStatus_type = "火警消除";
                                break;
                            case "36"://低电恢复
                                dataStatus_type = "低电恢复";
                                break;
                            case "44"://按键自测
                                dataStatus_type = "按键自测";
                                break;
                            case "45"://探测器上电
                                dataStatus_type = "探测器上电";
                                break;
                            default:
                                break;
                        }
                        LOGGER.info("task: lora smoke sensor dataStatus_type=" + dataStatus_type);
                        if (mISmokeDataService == null) {
                            mISmokeDataService = SpringContextUtils.getBean(ISmokeDataService.class);
                        }
                        SmokeDataPo mSmokeDataPo = new SmokeDataPo();
                        mSmokeDataPo.setDev_id(dev_eui);
                        mSmokeDataPo.setOrig_value(ori_value);
                        mSmokeDataPo.setSource("lora_smoke");
                        mSmokeDataPo.setTime(time);
                        mSmokeDataPo.setData_status(dataStatus_type);
                        mISmokeDataService.insert(mSmokeDataPo);
                        LOGGER.info("task: lora smoke insert into SQL end!");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * udp水表数据接收解析
     */
    private void UdpWMAnalysis(String ori_value, String ip, int port) {
        try {
            int len = ori_value.length();
            String orig_data = ori_value;//ori_value.substring(2, len).trim().replaceAll(" ", "");//去掉\x
            long time = System.currentTimeMillis();
            String regex = "^[A-Fa-f0-9]+$";//是16进制数
            if (orig_data.matches(regex)) {
                String dataFlag = orig_data.substring(0, 12);
                LOGGER.info("task: lora data receive: dataFlag:" + dataFlag);
                String orig_code = orig_data;
                if ("00006CFEFEFE".equals(dataFlag)) {//lora 数据
                    orig_code = orig_data.substring(12, orig_data.length());
//		    		System.out.println("task: data receive: orig_code:"+orig_code);
                    byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                    len = binaryData.length;
                    String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                    String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                    LOGGER.info("analysis: startbyte:" + startbyte);
                    LOGGER.info("analysis: endbyte:" + endbyte);
                    //00006CFEFEFE //6810 54512020000000 81 //5C  //数据长度域
                    //921F01****//2A030B000100//FFFF2E690016

                    if ("68".equals(startbyte) && "16".equals(endbyte)) {//水表上报发出的是68
                        //表号
                        String w_num = Utilty.convertByteToString(binaryData, 3, 9);
                        System.out.println("task: lora analysis: e_num:" + w_num);
                        //控制字
                        String control_code = orig_code.substring(18, 20);
                        System.out.println("task: lora analysis: control_code=" + control_code);
                        //数据长度
                        int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
                        System.out.println("task: lora analysis: data Length=" + dataLen);

                        //数据域
                        String valueD = orig_code.substring(22, 22 + dataLen * 2);
                        System.out.println("task: lora fefefe 68 valueD=" + valueD);
                        switch (control_code) {
                            case "81"://读计量数据2
                                Map<String, String> result_read = EMDataAnalysisUtil.getUdpWMDataValue(valueD);
                                if (mIWMDataService == null) {
                                    mIWMDataService = SpringContextUtils.getBean(IWMDataService.class);
                                }
                                WMDataPo wmDataPo = new WMDataPo();
                                wmDataPo.setWmNum(w_num);
                                wmDataPo.setTime(time);
                                wmDataPo.setOrigValue(orig_data);
                                wmDataPo.setDevId(result_read.get("imei"));
                                wmDataPo.setSource(data_type);
                                wmDataPo.setDataFlag1(result_read.get("data_flag1"));
                                wmDataPo.setDataFlag2(result_read.get("data_flag2"));
                                wmDataPo.setDataSeq(result_read.get("data_seq"));
                                wmDataPo.setWmFlow(result_read.get("wm_flow"));
                                wmDataPo.setWmFlowRate(result_read.get("wm_flow_rate"));
                                wmDataPo.setWmReverseFlow("");
                                wmDataPo.setWmTime(result_read.get("wm_time"));
                                wmDataPo.setWmStatu1(result_read.get("wm_statu1"));
                                wmDataPo.setWmStatu2("");
                                wmDataPo.setSendIp(ip.substring(1, ip.length()));
                                wmDataPo.setSendPort(port);
                                mIWMDataService.addOne(wmDataPo);
                                System.out.println("task: udp wm insert into SQL end!");

                                break;
                            case "83"://读表头地址
                                break;
                            case "84"://强制开关阀,自由开关阀
                                break;
                            default:
                                break;
                        }
                    }
                } else if ("010000000017".equals(dataFlag) || "010000000016".equals(dataFlag)) {//读阀值状态
                    //010000000017FEFEFE685554512020000000A407A0170140429916361600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
                    //010000000016FEFEFE685554512020000000A406A0170140421CA2160000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
                    //010000000017FEFEFE685554512020000000A407A0170140225516D216
                    orig_code = orig_data.substring(18, orig_data.length());
//		    		System.out.println("task: data receive: orig_code:"+orig_code);
                    byte[] binaryData = Utilty.hexStringToBytes(orig_code);
                    len = binaryData.length;
                    String startbyte = Integer.toHexString(binaryData[0] & 0xFF).toUpperCase();
                    String endbyte = Integer.toHexString(binaryData[len - 1] & 0xFF);
                    LOGGER.info("analysis: startbyte:" + startbyte);
                    LOGGER.info("analysis: endbyte:" + endbyte);
                    //00006CFEFEFE //6810 54512020000000 81 //5C  //数据长度域
                    //921F01****//2A030B000100//FFFF2E690016

                    if ("68".equals(startbyte) /*&& "16".equals(endbyte)*/) {//水表上报发出的是68
                        //表号
                        String w_num = Utilty.convertByteToString(binaryData, 3, 9);
                        System.out.println("task: lora analysis: e_num:" + w_num);
                        //控制字
                        String control_code = orig_code.substring(18, 20);
                        System.out.println("task: lora analysis: control_code=" + control_code);
                        //数据长度
                        int dataLen = Integer.parseInt(orig_code.substring(20, 22), 16);
                        System.out.println("task: lora analysis: data Length=" + dataLen);

                        //数据域
                        String valueD = orig_code.substring(22, 22 + dataLen * 2);//A0170140225516
                        System.out.println("task: lora fefefe 68 valueD=" + valueD);
                        switch (control_code) {
                            case "A4"://读计量数据2
                                Map<String, String> result_read = EMDataAnalysisUtil.getUdpWMReadDataValue(valueD);
                                if (mIWMDataService == null) {
                                    mIWMDataService = SpringContextUtils.getBean(IWMDataService.class);
                                }
                                WMReadDataPo mWMReadDataPo = new WMReadDataPo();
                                mWMReadDataPo.setOrigValue(orig_data);
                                mWMReadDataPo.setSource(data_type);
                                mWMReadDataPo.setTime(String.valueOf(time));
                                mWMReadDataPo.setType(result_read.get("type"));
                                mWMReadDataPo.setWmNum(w_num);
                                mWMReadDataPo.setValue(result_read.get("switch_status"));
                                mIWMDataService.insertReadData(mWMReadDataPo);
                                System.out.println("task: udp wm read insert into SQL end!");
                                break;
                            default:
                                break;
                        }
                    }
                } else if (orig_data.length() > 0) {
                    WMReadDataPo mWMReadDataPo = new WMReadDataPo();
                    mWMReadDataPo.setOrigValue(orig_data);
                    mWMReadDataPo.setSource(data_type);
                    mWMReadDataPo.setTime(String.valueOf(time));
                    mWMReadDataPo.setType("");
                    mWMReadDataPo.setWmNum("");
                    mWMReadDataPo.setValue("");
                    mIWMDataService.insertReadData(mWMReadDataPo);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 判定当前上报数据是否是下行指令后电表上报的
     */
    private static boolean ifUpDateSeq(String ser_seq) {
        boolean isUpDate = false;
        if (ser_seq.isEmpty()) {
            return false;
        }
        //服务序号
        String regex = "^[A-Fa-f0-9]+$";//是16进制数
        if (ser_seq.matches(regex)) {
            //十进制：192--255
            //十六进制：C0--FF
            int seq_dec = Integer.parseInt(ser_seq, 16);
            if (seq_dec >= 192 && seq_dec <= 255) {
                System.out.println("上报序列号-----------ser_seq=" + ser_seq);
                return true;
            }
        }
        return isUpDate;
    }

    /**
     * 保存芯北电表（nb,lora,2g）上报数据
     *
     * @param map
     * @param valueD
     */
    private void SaveXBEMDataToDB(Map<String, String> map, String valueD) {
        if (mIXBEMDataService == null) {
            mIXBEMDataService = SpringContextUtils.getBean(IXBEMDataService.class);
        }
        if (map != null && map.size() > 0 && valueD != null) {
            Map<String, String> d_result = EMDataAnalysisUtil.getDataValue(valueD);
            if (d_result.size() > 0) {
                XBEMDataPo dataPo = new XBEMDataPo();
                dataPo.setDev_id(map.get("dev_id"));
                dataPo.setOrig_value(map.get("ori_value"));
                dataPo.setTime(Long.parseLong(map.get("time")));
                dataPo.setSource(map.get("source"));
                dataPo.setE_fac(map.get("e_fac"));
                dataPo.setE_num(map.get("e_num"));
                dataPo.setFlag_reload(Integer.parseInt(map.get("flag_reload")));
                dataPo.setData_type(map.get("dataType"));

                dataPo.setSeq_type(d_result.get("seq_type") != null ? d_result.get("seq_type") : "FF");
                dataPo.setE_seq(Long.parseLong(d_result.get("e_seq") != null ? d_result.get("e_seq") : "0"));
                dataPo.setE_time(d_result.get("e_time") != null ? d_result.get("e_time") : "");
                dataPo.setE_signal(d_result.get("e_signal") != null ? d_result.get("e_signal") : "");

                dataPo.setE_kwh1(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");//有功总电能
                dataPo.setE_kwh2(d_result.get("e_kwh2") != null ? d_result.get("e_kwh2") : "");
                dataPo.setE_kw1_all(d_result.get("e_kw1_all") != null ? d_result.get("e_kw1_all") : "");//总有功功率
                dataPo.setE_kw2_all(d_result.get("e_kw2_all") != null ? d_result.get("e_kw2_all") : "");

                dataPo.setE_voltage_a(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                dataPo.setE_voltage_b(d_result.get("e_voltage_b") != null ? d_result.get("e_voltage_b") : "");
                dataPo.setE_voltage_c(d_result.get("e_voltage_c") != null ? d_result.get("e_voltage_c") : "");
                dataPo.setE_current_a(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流
                dataPo.setE_current_b(d_result.get("e_current_b") != null ? d_result.get("e_current_b") : "");
                dataPo.setE_current_c(d_result.get("e_current_c") != null ? d_result.get("e_current_c") : "");
                dataPo.setE_factor_all(d_result.get("e_factor_all") != null ? d_result.get("e_factor_all") : "");//总功率因数
                dataPo.setE_statu1(d_result.get("e_statu1") != null ? d_result.get("e_statu1") : "");//状态字1
                dataPo.setE_statu2(d_result.get("e_statu2") != null ? d_result.get("e_statu2") : "");//状态字2
                dataPo.setE_switch(d_result.get("e_switch") != null ? d_result.get("e_switch") : "");//继电器

                mIXBEMDataService.addOne(dataPo);
//		        LOGGER.info("task: lora fefefefe 80 insert into SQL end!");
            }
        }
    }

    /**
     * 保存lora上报数据，通过国动物联接收
     *
     * @param map
     * @param valueD
     */
    private void SaveLoraDataToDB(Map<String, String> map, String valueD) {
        if (mILoraDataService == null) {
            mILoraDataService = SpringContextUtils.getBean(ILoraDataService.class);
        }
        if (map != null && map.size() > 0 && valueD != null) {
            Map<String, String> d_result = EMDataAnalysisUtil.getDataValue(valueD);
            if (d_result.size() > 0) {
                LoraDataPo wmdp = new LoraDataPo();
                wmdp.setDev_id(map.get("dev_id"));
                wmdp.setOrig_value(map.get("ori_value"));
                wmdp.setTime(Long.parseLong(map.get("time")));
                wmdp.setSource(map.get("source"));
                wmdp.setE_fac(map.get("e_fac"));
                wmdp.setE_num(map.get("e_num"));
                wmdp.setFlag_reload(Integer.parseInt(map.get("flag_reload")));

                wmdp.setSeq_type(d_result.get("seq_type") != null ? d_result.get("seq_type") : "FF");
                wmdp.setE_seq(Long.parseLong(d_result.get("e_seq") != null ? d_result.get("e_seq") : "0"));
                wmdp.setE_time(d_result.get("e_time") != null ? d_result.get("e_time") : "");
                wmdp.setE_signal(d_result.get("e_signal") != null ? d_result.get("e_signal") : "");

                wmdp.setE_readings(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");//有功总电能
                wmdp.setE_voltage(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                wmdp.setE_current(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流
                wmdp.setE_switch(d_result.get("e_switch") != null ? d_result.get("e_switch") : "");

                wmdp.setE_kwh1(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");//有功总电能
                wmdp.setE_kwh2(d_result.get("e_kwh2") != null ? d_result.get("e_kwh2") : "");
                wmdp.setE_kw1_all(d_result.get("e_kw1_all") != null ? d_result.get("e_kw1_all") : "");//总有功功率
                wmdp.setE_kw2_all(d_result.get("e_kw2_all") != null ? d_result.get("e_kw2_all") : "");

                wmdp.setE_voltage_a(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                wmdp.setE_voltage_b(d_result.get("e_voltage_b") != null ? d_result.get("e_voltage_b") : "");
                wmdp.setE_voltage_c(d_result.get("e_voltage_c") != null ? d_result.get("e_voltage_c") : "");
                wmdp.setE_current_a(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流
                wmdp.setE_current_b(d_result.get("e_current_b") != null ? d_result.get("e_current_b") : "");
                wmdp.setE_current_c(d_result.get("e_current_c") != null ? d_result.get("e_current_c") : "");
                wmdp.setE_factor_all(d_result.get("e_factor_all") != null ? d_result.get("e_factor_all") : "");//总功率因数
                wmdp.setE_statu1(d_result.get("e_statu1") != null ? d_result.get("e_statu1") : "");//状态字1
                wmdp.setE_statu2(d_result.get("e_statu2") != null ? d_result.get("e_statu2") : "");//状态字2
                wmdp.setE_switch(d_result.get("e_switch") != null ? d_result.get("e_switch") : "");//继电器

                mILoraDataService.addOne(wmdp);
//		        LOGGER.info("task: lora fefefefe 80 insert into SQL end!");
            }
        }
    }

    /**
     * 保存NB电表上报数据，通过移动ONENET平台接收
     *
     * @param map
     * @param valueD
     */
    private void SaveNBDataToDB(Map<String, String> map, String valueD) {
        if (nbYDMeterDataService == null) {
            nbYDMeterDataService = SpringContextUtils.getBean(INBYDEMeterDataService.class);
        }
        if (map != null && map.size() > 0 && valueD != null) {
            Map<String, String> d_result = EMDataAnalysisUtil.getDataValue(valueD);
            if (d_result.size() > 0) {
                YDEMeterDataPo mYDEMeterDataPo = new YDEMeterDataPo();
                mYDEMeterDataPo.setDev_id(map.get("dev_id"));
                mYDEMeterDataPo.setDs_id(map.get("ds_id"));
                mYDEMeterDataPo.setImei(map.get("imei"));
                mYDEMeterDataPo.setTime(Long.parseLong(map.get("time")));
                mYDEMeterDataPo.setType(Integer.parseInt(map.get("type")));
                mYDEMeterDataPo.setOrig_value(map.get("ori_value"));
                mYDEMeterDataPo.setSource(map.get("source"));
                mYDEMeterDataPo.setE_num(map.get("e_num"));
                mYDEMeterDataPo.setE_fac(map.get("e_fac"));
                mYDEMeterDataPo.setFlag_reload(Integer.parseInt(map.get("flag_reload")));//正常上报

                mYDEMeterDataPo.setSeq_type(d_result.get("seq_type") != null ? d_result.get("seq_type") : "FF");
                mYDEMeterDataPo.setE_seq(Long.parseLong(d_result.get("e_seq") != null ? d_result.get("e_seq") : "0"));
                mYDEMeterDataPo.setE_time(d_result.get("e_time") != null ? d_result.get("e_time") : "");
                mYDEMeterDataPo.setE_signal(d_result.get("e_signal") != null ? d_result.get("e_signal") : "");
                //单相、三相第一帧
                mYDEMeterDataPo.setE_readings(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");////有功总电能
                mYDEMeterDataPo.setE_voltage(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                mYDEMeterDataPo.setE_current(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流

                mYDEMeterDataPo.setE_kwh1(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");//有功总电能
                mYDEMeterDataPo.setE_kwh2(d_result.get("e_kwh2") != null ? d_result.get("e_kwh2") : "");
                mYDEMeterDataPo.setE_kw1_all(d_result.get("e_kw1_all") != null ? d_result.get("e_kw1_all") : "");//总有功功率
//			        mYDEMeterDataPo.setE_kw1_a(d_result.get("e_kw1_a") !=null ? d_result.get("e_kw1_a"):"");
//			        mYDEMeterDataPo.setE_kw1_b(d_result.get("e_kw1_b") !=null ? d_result.get("e_kw1_b"):"");
//			        mYDEMeterDataPo.setE_kw1_c(d_result.get("e_kw1_c") !=null ? d_result.get("e_kw1_c"):"");
                mYDEMeterDataPo.setE_kw2_all(d_result.get("e_kw2_all") != null ? d_result.get("e_kw2_all") : "");
//			        mYDEMeterDataPo.setE_kw2_a(d_result.get("e_kw2_a") !=null ? d_result.get("e_kw2_a"):"");
//			        mYDEMeterDataPo.setE_kw2_b(d_result.get("e_kw2_b") !=null ? d_result.get("e_kw2_b"):"");
//			        mYDEMeterDataPo.setE_kw2_c(d_result.get("e_kw2_c") !=null ? d_result.get("e_kw2_c"):"");

                mYDEMeterDataPo.setE_voltage_a(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                mYDEMeterDataPo.setE_voltage_b(d_result.get("e_voltage_b") != null ? d_result.get("e_voltage_b") : "");
                mYDEMeterDataPo.setE_voltage_c(d_result.get("e_voltage_c") != null ? d_result.get("e_voltage_c") : "");
                mYDEMeterDataPo.setE_current_a(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流
                mYDEMeterDataPo.setE_current_b(d_result.get("e_current_b") != null ? d_result.get("e_current_b") : "");
                mYDEMeterDataPo.setE_current_c(d_result.get("e_current_c") != null ? d_result.get("e_current_c") : "");
                mYDEMeterDataPo.setE_factor_all(d_result.get("e_factor_all") != null ? d_result.get("e_factor_all") : "");//总功率因数
//			        mYDEMeterDataPo.setE_factor_a(d_result.get("e_factor_a") !=null ? d_result.get("e_factor_a"):"");
//			        mYDEMeterDataPo.setE_factor_b(d_result.get("e_factor_b") !=null ? d_result.get("e_factor_b"):"");
//			        mYDEMeterDataPo.setE_factor_c(d_result.get("e_factor_c") !=null ? d_result.get("e_factor_c"):"");
                mYDEMeterDataPo.setE_statu1(d_result.get("e_statu1") != null ? d_result.get("e_statu1") : "");//状态字1
                mYDEMeterDataPo.setE_statu2(d_result.get("e_statu2") != null ? d_result.get("e_statu2") : "");//状态字2
//			        mYDEMeterDataPo.setOrig_value2("");//保存第二帧
                mYDEMeterDataPo.setE_switch(d_result.get("e_switch") != null ? d_result.get("e_switch") : "");//继电器

                nbYDMeterDataService.addOne(mYDEMeterDataPo);
//		        LOGGER.info("task: 80 insert into SQL end!");
            }
        }
    }

    /**
     * 保存上报事件，不区分lora和NB
     *
     * @param map
     * @param valueD
     */
    private void SaveEventDataToDB(Map<String, String> map, String valueD) {
        if (mINBYDEMEventService == null) {
            mINBYDEMEventService = SpringContextUtils.getBean(INBYDEMEventService.class);
        }
        if (map != null && map.size() > 0 && valueD != null) {
            Map<String, String> result = EMDataAnalysisUtil.getEventDataValue(valueD);
            if (result.size() > 0) {
                YDEMeterEventPo mYDEMeterEventPo = new YDEMeterEventPo();
                mYDEMeterEventPo.setDev_id(map.get("dev_id"));
                mYDEMeterEventPo.setImei(map.get("imei"));
                mYDEMeterEventPo.setE_fac(map.get("e_fac"));
                mYDEMeterEventPo.setE_num(map.get("e_num"));
                mYDEMeterEventPo.setTime(Long.parseLong(map.get("time")));//时间长类型
                mYDEMeterEventPo.setOrig_value(map.get("ori_value"));
                mYDEMeterEventPo.setFlag_reload(Integer.parseInt(map.get("flag_reload")));//重传上报
                mYDEMeterEventPo.setSource(map.get("source"));
                mYDEMeterEventPo.setEvent_type(map.get("data_type"));
                mYDEMeterEventPo.setDeal_flag(Integer.parseInt(map.get("deal_flag")));

                mYDEMeterEventPo.setE_seq(Long.parseLong(result.containsKey("e_seq") ? result.get("e_seq") : "0"));
                mYDEMeterEventPo.setE_start_time(result.containsKey("start_time") ? result.get("start_time") : "");
                mYDEMeterEventPo.setE_end_time(result.containsKey("end_time") ? result.get("end_time") : "");

                mYDEMeterEventPo.setE_start_kwh(result.containsKey("start_kwh1") ? result.get("start_kwh1") : "");
                mYDEMeterEventPo.setE_start_kwh2(result.containsKey("start_kwh2") ? result.get("start_kwh2") : "");
                mYDEMeterEventPo.setE_end_kwh(result.containsKey("end_kwh1") ? result.get("end_kwh1") : "");
                mYDEMeterEventPo.setE_end_kwh2(result.containsKey("end_kwh2") ? result.get("end_kwh2") : "");

                mYDEMeterEventPo.setE_start_voltage_a(result.containsKey("start_voltage_a") ? result.get("start_voltage_a") : "");
                mYDEMeterEventPo.setE_start_voltage_b(result.containsKey("start_voltage_b") ? result.get("start_voltage_b") : "");
                mYDEMeterEventPo.setE_start_voltage_c(result.containsKey("start_voltage_c") ? result.get("start_voltage_c") : "");
                mYDEMeterEventPo.setE_end_voltage_a(result.containsKey("end_voltage_a") ? result.get("end_voltage_a") : "");
                mYDEMeterEventPo.setE_end_voltage_b(result.containsKey("end_voltage_b") ? result.get("end_voltage_b") : "");
                mYDEMeterEventPo.setE_end_voltage_c(result.containsKey("end_voltage_c") ? result.get("end_voltage_c") : "");

                mYDEMeterEventPo.setE_start_current_a(result.containsKey("start_current_a") ? result.get("start_current_a") : "");
                mYDEMeterEventPo.setE_start_current_b(result.containsKey("start_current_b") ? result.get("start_current_b") : "");
                mYDEMeterEventPo.setE_start_current_c(result.containsKey("start_current_c") ? result.get("start_current_c") : "");
                mYDEMeterEventPo.setE_end_current_a(result.containsKey("end_current_a") ? result.get("end_current_a") : "");
                mYDEMeterEventPo.setE_end_current_b(result.containsKey("end_current_b") ? result.get("end_current_b") : "");
                mYDEMeterEventPo.setE_end_current_c(result.containsKey("end_current_c") ? result.get("end_current_c") : "");

                mYDEMeterEventPo.setE_start_status1(result.containsKey("start_status1") ? result.get("start_status1") : "");
                mYDEMeterEventPo.setE_end_status1(result.containsKey("end_status1") ? result.get("end_status1") : "");
                mYDEMeterEventPo.setE_start_status2(result.containsKey("start_status2") ? result.get("start_status2") : "");
                mYDEMeterEventPo.setE_end_status2(result.containsKey("end_status2") ? result.get("end_status2") : "");
                mYDEMeterEventPo.setE_start_switch(result.containsKey("start_switch") ? result.get("start_switch") : "");
                mYDEMeterEventPo.setE_end_switch(result.containsKey("end_switch") ? result.get("end_switch") : "");
                mYDEMeterEventPo.setE_end_module(result.containsKey("start_module") ? result.get("start_module") : "");
                mYDEMeterEventPo.setE_start_module(result.containsKey("end_module") ? result.get("end_module") : "");
                mINBYDEMEventService.addOne(mYDEMeterEventPo);
//			LOGGER.info("task: insert event into SQL end!");

            }
        }

    }

    /**
     * 保存2G电表上报数据，通过mqtt接收
     *
     * @param map
     * @param valueD
     */
    private void SaveGPRSDataToDB(Map<String, String> map, String valueD) {
        if (mIGprsDataService == null) {
            mIGprsDataService = SpringContextUtils.getBean(IGprsDataService.class);
        }
        if (map != null && map.size() > 0 && valueD != null) {
            Map<String, String> d_result = EMDataAnalysisUtil.getDataValue(valueD);
            if (d_result.size() > 0) {
                GprsDataPo mGprsDataPo = new GprsDataPo();
                mGprsDataPo.setDev_id(map.get("imei"));
                mGprsDataPo.setTime(Long.parseLong(map.get("time")));
                mGprsDataPo.setOrig_value(map.get("ori_value"));
                mGprsDataPo.setSource(map.get("source"));
                mGprsDataPo.setE_num(map.get("e_num"));
                mGprsDataPo.setE_fac(map.get("e_fac"));
                mGprsDataPo.setFlag_reload(Integer.parseInt(map.get("flag_reload")));//正常上报0,下行读取1

                mGprsDataPo.setSeq_type(d_result.get("seq_type") != null ? d_result.get("seq_type") : "FF");
                mGprsDataPo.setE_seq(Long.parseLong(d_result.get("e_seq") != null ? d_result.get("e_seq") : "0"));
                mGprsDataPo.setE_time(d_result.get("e_time") != null ? d_result.get("e_time") : "");
                mGprsDataPo.setE_signal(d_result.get("e_signal") != null ? d_result.get("e_signal") : "");

                mGprsDataPo.setE_kwh1(d_result.get("e_kwh1") != null ? d_result.get("e_kwh1") : "");//有功总电能
                mGprsDataPo.setE_kwh2(d_result.get("e_kwh2") != null ? d_result.get("e_kwh2") : "");
                mGprsDataPo.setE_kw1_all(d_result.get("e_kw1_all") != null ? d_result.get("e_kw1_all") : "");//总有功功率
                mGprsDataPo.setE_kw2_all(d_result.get("e_kw2_all") != null ? d_result.get("e_kw2_all") : "");
                mGprsDataPo.setE_voltage_a(d_result.get("e_voltage_a") != null ? d_result.get("e_voltage_a") : "");//A相电压
                mGprsDataPo.setE_voltage_b(d_result.get("e_voltage_b") != null ? d_result.get("e_voltage_b") : "");
                mGprsDataPo.setE_voltage_c(d_result.get("e_voltage_c") != null ? d_result.get("e_voltage_c") : "");
                mGprsDataPo.setE_current_a(d_result.get("e_current_a") != null ? d_result.get("e_current_a") : "");//A相电流
                mGprsDataPo.setE_current_b(d_result.get("e_current_b") != null ? d_result.get("e_current_b") : "");
                mGprsDataPo.setE_current_c(d_result.get("e_current_c") != null ? d_result.get("e_current_c") : "");
                mGprsDataPo.setE_factor_all(d_result.get("e_factor_all") != null ? d_result.get("e_factor_all") : "");//总功率因数
                mGprsDataPo.setE_statu1(d_result.get("e_statu1") != null ? d_result.get("e_statu1") : "");//状态字1
                mGprsDataPo.setE_statu2(d_result.get("e_statu2") != null ? d_result.get("e_statu2") : "");//状态字2
                mGprsDataPo.setE_switch(d_result.get("e_switch") != null ? d_result.get("e_switch") : "");//继电器

                mIGprsDataService.addOne(mGprsDataPo);
            }
        }
    }

    public AsyncService getAsyncService() {
        return asyncService;
    }

    public void setAsyncService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }
}
