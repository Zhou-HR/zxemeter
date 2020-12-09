package com.gdiot.model;

import java.io.Serializable;

/**
 * @author: shenzhengkai
 * @className: ElectricBanlanceData
 * @description: ElectricBanlanceData
 * @date: 日期: 2020/10/26 时间: 9:13
 **/
public class ktEmDataPo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String devId;// 设备id
    private String origValue;// 数据预报
    private long time;//时间
    private String eNum;//表号
    private Integer eSeq;//序列号
    private String eSource;  // 设备资源类型
    private String eSignal;//信号
    private String eKwAll = "";//有功总功率
    private String eKw1 = "";//正向有功功率
    private String eKw2 = "";//反向无功功率
    private String eVoltage = "";//电压
    private String eCurrent = "";//相电流
    private String eFactorAll = "";//总功率因数
    private String eStatus = "";//总功率因数
    private String ekw;//总电能
    private String eSwitch;// 拉合闸状态


    public String geteSource() {
        return eSource;
    }

    public void seteSource(String eSource) {
        this.eSource = eSource;
    }

    public String geteSwitch() {
        return eSwitch;
    }

    public void seteSwitch(String eSwitch) {
        this.eSwitch = eSwitch;
    }

    public String getEkw() {
        return ekw;
    }

    public void setEkw(String ekw) {
        this.ekw = ekw;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String geteNum() {
        return eNum;
    }

    public void seteNum(String eNum) {
        this.eNum = eNum;
    }

    public Integer geteSeq() {
        return eSeq;
    }

    public void seteSeq(Integer eSeq) {
        this.eSeq = eSeq;
    }

    public String geteKw1() {
        return eKw1;
    }

    public void seteKw1(String eKw1) {
        this.eKw1 = eKw1;
    }

    public String geteKw2() {
        return eKw2;
    }

    public void seteKw2(String eKw2) {
        this.eKw2 = eKw2;
    }

    public String geteStatus() {
        return eStatus;
    }

    public void seteStatus(String eStatus) {
        this.eStatus = eStatus;
    }


    public String geteVoltage() {
        return eVoltage;
    }

    public void seteVoltage(String eVoltage) {
        this.eVoltage = eVoltage;
    }

    public String geteCurrent() {
        return eCurrent;
    }

    public void seteCurrent(String eCurrent) {
        this.eCurrent = eCurrent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getOrigValue() {
        return origValue;
    }

    public void setOrigValue(String origValue) {
        this.origValue = origValue;
    }

    public String geteSignal() {
        return eSignal;
    }

    public void seteSignal(String eSignal) {
        this.eSignal = eSignal;
    }


    public String geteKwAll() {
        return eKwAll;
    }

    public void seteKwAll(String eKwAll) {
        this.eKwAll = eKwAll;
    }

    public String geteFactorAll() {
        return eFactorAll;
    }

    public void seteFactorAll(String eFactorAll) {
        this.eFactorAll = eFactorAll;
    }
}
