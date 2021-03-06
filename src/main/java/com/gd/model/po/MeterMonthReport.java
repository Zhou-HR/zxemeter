package com.gd.model.po;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class MeterMonthReport {

    private Integer id;

    private String meterNo;

    private String projectNo = "";

    private String year;

    private String month;

    private String companyId = "99";

    private String evalue1 = "";

    private String evalue2 = "";

    private String evalue = "";

    private String eseq1 = "";

    private String eseq2 = "";

    private Integer yearmonth;

    private String day;

    private String carrierName;

    private String carrierValue;

    private String carrierFee;

    private String tbMeterIdNew = "";

    private String tbMeterIdOld = "";

    private String newMeterValue = "";

    private String oldMeterValue = "";

    private String company = "";

    private String shiyebu = "";

    private String fee = "0";

}
