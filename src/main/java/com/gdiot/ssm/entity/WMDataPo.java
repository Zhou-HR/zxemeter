package com.gdiot.ssm.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ZhouHR
 */
@Data
public class WMDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String devId;
    private String origValue;
    private long time;
    private String source;

    //	id,dev_id,orig_value,time,source,
//	wm_num,wm_time,data_flag1,data_flag2,data_seq,
//	wm_flow,wm_reverse_flow,wm_flow_rate,wm_statu1,wm_statu2
    //解析出的数据
    private String wmNum;//表号
    private String wmTime;//数据的时间
    private String dataFlag1;//
    private String dataFlag2;//
    private String dataSeq;//
    private String wmFlow;//
    private String wmReverseFlow;//
    private String wmFlowRate;
    private String wmStatu1 = "";//
    private String wmStatu2 = "";//
    private String sendIp;
    private int sendPort;

}
