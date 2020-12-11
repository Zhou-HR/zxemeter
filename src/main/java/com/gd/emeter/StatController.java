package com.gd.emeter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.mapper.MeterMapper;
import com.gd.mapper.NBMapper;
import com.gd.mapper.StatMapper;
import com.gd.model.po.CarrierValueFee;
import com.gd.model.po.CompanyYearMonthValue;
import com.gd.model.po.Meter;
import com.gd.model.po.Meter12Month;
import com.gd.model.po.MeterLostData;
import com.gd.model.po.MeterMonthReport;
import com.gd.model.po.MeterNum;
import com.gd.model.po.MeterValueTime;
import com.gd.model.po.MeterWarning;
import com.gd.redis.InitLoadToRedis;

/**
 * @author ZhouHR
 */
@Controller
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private MeterMapper meterMapper;

    @Autowired
    private NBMapper nbMapper;

    @Autowired
    private StatMapper satMapper;

    @RequestMapping("/toListMeterValueChart")
    public String toListMeterValueChart(HttpServletRequest request) {
//		request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listMeterValueChart";
    }

    @RequestMapping("/toListMeterValueDayChart")
    public String toListMeterValueDayChart(HttpServletRequest request) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = df1.format(c.getTime());

        request.setAttribute("date1", date1);
        return "jsp/emeter/listMeterValueDayChart";
    }

    @RequestMapping("/toListMeterNum")
    public String toListMeterNum(String companyId, HttpServletRequest request) {
//		request.setAttribute("companyId", companyId);
        return "jsp/emeter/listMeterNum";
    }

    @RequestMapping("/toListMeterValue")
    public String toListMeterValue(String companyId, HttpServletRequest request) {
        return "jsp/emeter/listMeterValue";
    }

    @RequestMapping("/toListMeterValueDay")
    public String toListMeterValueDay(@RequestParam(required = false) String meterNo, String date1, HttpServletRequest request) {
        if (StringUtils.isEmpty(date1)) {
            Date date = new Date();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = df1.format(date);
        }

        request.setAttribute("date1", date1);
        return "jsp/emeter/listMeterValueDay";
    }

    @RequestMapping("/toListCarrierValue")
    public String toListCarrierValue(HttpServletRequest request) {
        return "jsp/emeter/listCarrierValueChart";
    }

    @RequestMapping("/toListCarrierValueDay")
    public String toListCarrierValueDay(HttpServletRequest request) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = df1.format(c.getTime());

        request.setAttribute("date1", date1);
        return "jsp/emeter/listCarrierValueDayChart";
    }

    @RequestMapping("/toListDataReload")
    public String toLostDataReload(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listLostDataReload";
    }

    @RequestMapping("/toListYear")
    public String toListYear(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listMonthReport";
    }

    @RequestMapping("/toListDay")
    public String toListDay(@RequestParam(required = false) String meterNo, String date1, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        if (StringUtils.isEmpty(date1)) {
            Date date = new Date();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = df1.format(date);
        }

        request.setAttribute("date1", date1);
        return "jsp/emeter/listDayReport";
    }

    @RequestMapping("/toListWarning")
    public String toListWarning(@RequestParam(required = false) String meterNo, HttpServletRequest request) {
        request.setAttribute("meterNo", meterNo);
        return "jsp/emeter/listStatWarning";
    }

    private static void plusValue(Meter12Month meter12Month, String value) {
        BigDecimal bd1 = new BigDecimal(meter12Month.getYearValue());
        if (StringUtils.isEmpty(value)) {
            value = "0";
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd1 = bd1.add(bd2);
        meter12Month.setYearValue(bd1.toPlainString());
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/pagingDay")
    @ResponseBody
    public Map pagingDay(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);

        String date1 = params.get("date1").toString();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(date1);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date1 = sdf1.format(c.getTime());
        c.add(Calendar.DAY_OF_WEEK, 6);
        String date2 = sdf1.format(c.getTime());
        params.put("date1", date1);
        params.put("date2", date2);

        List<MeterMonthReport> list = meterMapper.getDayReport(params);

        int total = meterMapper.countDayReport(params);
        List<Meter12Month> listMeter12Month = new ArrayList<Meter12Month>();
        Map<String, Meter12Month> mapMeter12Month = new HashMap<String, Meter12Month>();

        Class clazz = Meter12Month.class;
        Field[] result = clazz.getDeclaredFields();

        for (MeterMonthReport meterMonthReport : list) {
            String meterNo = meterMonthReport.getMeterNo();
            Meter12Month meter12Month = mapMeter12Month.get(meterNo);
            if (meter12Month == null) {
                meter12Month = new Meter12Month();
                meter12Month.setMeterNo(meterMonthReport.getMeterNo());
                meter12Month.setProjectNo(meterMonthReport.getProjectNo());
                meter12Month.setCompanyId(meterMonthReport.getCompanyId());
                meter12Month.setYearValue(meterMonthReport.getEvalue());

                mapMeter12Month.put(meterNo, meter12Month);
                listMeter12Month.add(meter12Month);
//    			total++;
            } else {
//    			plusValue(meter12Month,meterMonthReport);
            }
            int yearmonth = meterMonthReport.getYearmonth();
            Integer nmonth = yearmonth - Integer.valueOf(date1) + 1;

            for (Field field : result) {
                String name = "evalue" + nmonth;
                if (name.equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        field.set(meter12Month, meterMonthReport.getEvalue());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        map.put("rows", listMeter12Month);
        map.put("total", total);
        return map;
    }

    @RequestMapping("/pagingYear")
    @ResponseBody
    public Map pagingYear(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        if (params.get("year") == null) {
            Calendar cyear = Calendar.getInstance();
            params.put("year", cyear.get(Calendar.YEAR));
        }
        List<MeterMonthReport> list = meterMapper.getMonthReport(params);

        int total = meterMapper.countMonthReport(params);
        List<Meter12Month> listMeter12Month = new ArrayList<Meter12Month>();
        Map<String, Meter12Month> mapMeter12Month = new HashMap<String, Meter12Month>();

        Class clazz = Meter12Month.class;
        Field[] result = clazz.getDeclaredFields();

        for (MeterMonthReport meterMonthReport : list) {
            String meterNo = meterMonthReport.getMeterNo();
            Meter12Month meter12Month = mapMeter12Month.get(meterNo + meterMonthReport.getProjectNo());
            if (meter12Month == null) {
                meter12Month = new Meter12Month();
                meter12Month.setMeterNo(meterMonthReport.getMeterNo());
                meter12Month.setProjectNo(meterMonthReport.getProjectNo());
                meter12Month.setCompanyId(meterMonthReport.getCompanyId());
                meter12Month.setYearValue(meterMonthReport.getEvalue());

                mapMeter12Month.put(meterNo + meterMonthReport.getProjectNo(), meter12Month);
                listMeter12Month.add(meter12Month);
//    			total++;
            } else {
                plusValue(meter12Month, meterMonthReport.getEvalue());
            }
//    		String month=meterMonthReport.getYearmonth();
            Integer nmonth = meterMonthReport.getYearmonth() % 100;

            for (Field field : result) {
                String name = "evalue" + nmonth;
                if (name.equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        field.set(meter12Month, meterMonthReport.getEvalue());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        map.put("rows", listMeter12Month);
        map.put("total", total);
        return map;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/pagingWarning")
    @ResponseBody
    public Map pagingWarning(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        params.put("year", year);

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        List<MeterWarning> list = meterMapper.getMonthWarning(params);
        int total = meterMapper.getMonthWarningCount(params);
        List<Meter12Month> listMeter12Month = new ArrayList<Meter12Month>();
        Map<String, Meter12Month> mapMeter12Month = new HashMap<String, Meter12Month>();
        for (MeterWarning meterWarning : list) {
            String meterNo = meterWarning.getMeterNo();
            Meter12Month meter12Month = mapMeter12Month.get(meterNo);
            if (meter12Month == null) {
                meter12Month = new Meter12Month();
                meter12Month.setMeterNo(meterWarning.getMeterNo());
                meter12Month.setProjectNo(meterWarning.getProjectNo());
                meter12Month.setCompanyId(meterWarning.getCompanyId());
                meter12Month.setCompany(meterWarning.getCompany());
                meter12Month.setUnit(meterWarning.getUnit());

                mapMeter12Month.put(meterNo, meter12Month);
                listMeter12Month.add(meter12Month);
//    			total++;
            }
            Integer yearmonth = meterWarning.getYearmonth();
            String stryearmonth = yearmonth.toString();
            stryearmonth = stryearmonth.substring(4);
            Integer nmonth = Integer.valueOf(stryearmonth);
            switch (nmonth) {
                case 1:
                    meter12Month.setEvalue1(getValuePlusOne(meter12Month.getEvalue1()));
                    break;
                case 2:
                    meter12Month.setEvalue2(getValuePlusOne(meter12Month.getEvalue2()));
                    break;
                case 3:
                    meter12Month.setEvalue3(getValuePlusOne(meter12Month.getEvalue3()));
                    break;
                case 4:
                    meter12Month.setEvalue4(getValuePlusOne(meter12Month.getEvalue4()));
                    break;
                case 5:
                    meter12Month.setEvalue5(getValuePlusOne(meter12Month.getEvalue5()));
                    break;
                case 6:
                    meter12Month.setEvalue6(getValuePlusOne(meter12Month.getEvalue6()));
                    break;
                case 7:
                    meter12Month.setEvalue7(getValuePlusOne(meter12Month.getEvalue7()));
                    break;
                case 8:
                    meter12Month.setEvalue8(getValuePlusOne(meter12Month.getEvalue8()));
                    break;
                case 9:
                    meter12Month.setEvalue9(getValuePlusOne(meter12Month.getEvalue9()));
                    break;
                case 10:
                    meter12Month.setEvalue10(getValuePlusOne(meter12Month.getEvalue10()));
                    break;
                case 11:
                    meter12Month.setEvalue11(getValuePlusOne(meter12Month.getEvalue11()));
                    break;
                case 12:
                    meter12Month.setEvalue12(getValuePlusOne(meter12Month.getEvalue12()));
                    break;
                default:

            }

        }
        map.put("rows", listMeter12Month);
        map.put("total", total);
        return map;
    }

    private String getValuePlusOne(String value) {
        if (value == null) {
            return "1";
        } else {
            int n = Integer.valueOf(value);
            n++;
            return String.valueOf(n);
        }
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/pagingMeterLostdata")
    @ResponseBody
    public Map pagingMeterLostdata(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        List<MeterLostData> list = meterMapper.selectMeterLostDataList(params);
        for (MeterLostData meterLostData : list) {
            //设置 公司 事业部
            String companyId = meterLostData.getCompanyId();
            meterLostData.setUnit(InitLoadToRedis.getCompanyName(companyId));
            if (companyId != null && companyId.length() == 4) {
                companyId = companyId.substring(0, 2);
                meterLostData.setCompany(InitLoadToRedis.getCompanyName(companyId));
            }
        }
        map.put("rows", list);
        map.put("total", meterMapper.countMeterLostData(params));
        return map;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/pagingMeterNum")
    @ResponseBody
    public Map pagingMeterNum(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        //查询companyIds
        String companyIds = nbMapper.getCompanyIdsByUserId(userId);
        String[] arr = companyIds.split(",");
        if (StringUtils.isEmpty(companyIds)) {
            companyIds = "('')";
        } else {
            companyIds = "(" + companyIds + ")";
        }
        params.put("companyIds", companyIds);

        String companyId = "";
        if (params.get("companyId") != null) {
            companyId = params.get("companyId").toString();
        }
        List<Meter> list;
        if (params.get("projectNo") != null) {
            list = satMapper.selectListMeterNum2(params);
        } else {
            if (StringUtils.isEmpty(companyId)) {
                list = satMapper.selectListMeterNum(params);
            } else {
                list = satMapper.selectListMeterNum1(params);
            }

        }

        Map<String, MeterNum> mapMeterNum = new HashMap<String, MeterNum>();
        List<MeterNum> listMeterNum = new ArrayList<MeterNum>();
        //开始遍历
        for (Meter meter : list) {
            String company = meter.getCompany();
            MeterNum meterNum = mapMeterNum.get(company);
            if (meterNum == null) {
                meterNum = new MeterNum();
                meterNum.setCompany(company);
                listMeterNum.add(meterNum);
                mapMeterNum.put(company, meterNum);
            }

            Integer status = meter.getStatus();
            if (status == 1) {
                meterNum.setOnline(meterNum.getOnline() + 1);
            } else {
                meterNum.setOffline(meterNum.getOffline() + 1);
            }
            meterNum.setAll(meterNum.getAll() + 1);
        }

        map.put("rows", listMeterNum);
        if (params.get("projectNo") != null) {
            map.put("total", satMapper.countMeterNum2(params));
        } else {
            if (StringUtils.isEmpty(companyId)) {
                map.put("total", satMapper.countMeterNum(params));
            } else {
                map.put("total", satMapper.countMeterNum1(params));
            }
        }

        return map;
    }

    @RequestMapping("/pagingMeterValue")
    @ResponseBody
    public Map pagingMeterValue(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);
        if (params.get("year") == null) {
            Calendar cyear = Calendar.getInstance();
            params.put("year", cyear.get(Calendar.YEAR));
        }
        String companyId = "";
        if (params.get("companyId") != null) {
            companyId = params.get("companyId").toString();
        }

        List<MeterValueTime> list;
        String projectNo = "";
        if (params.get("projectNo") != null) {
            projectNo = params.get("projectNo").toString();
        }
        if (StringUtils.isNoneEmpty(projectNo)) {
            list = satMapper.selectListMeterValue2(params);
        } else {
            if (StringUtils.isEmpty(companyId)) {
                list = satMapper.selectListMeterValue(params);
            } else {
                if (companyId.length() == 2) {
                    list = satMapper.selectListMeterValue1(params);
                } else {
                    list = satMapper.selectListMeterValue2(params);
                }
            }
        }

        int total = 0;
        if (StringUtils.isNoneEmpty(projectNo)) {
            total = satMapper.countMeterValue2(params);
        } else {
            if (StringUtils.isEmpty(companyId)) {
                total = satMapper.countMeterValue(params);
            } else {
                if (companyId.length() == 2) {
                    total = satMapper.countMeterValue1(params);
                } else {
                    total = satMapper.countMeterValue2(params);
                }
            }
        }

        List<Meter12Month> listMeter12Month = new ArrayList<Meter12Month>();
        Map<String, Meter12Month> mapMeter12Month = new HashMap<String, Meter12Month>();

        Class<Meter12Month> clazz = Meter12Month.class;
        Field[] result = clazz.getDeclaredFields();

        for (MeterValueTime meterValueTime : list) {
            String company = meterValueTime.getCompany();
            Meter12Month meter12Month = mapMeter12Month.get(company);
            if (meter12Month == null) {
                meter12Month = new Meter12Month();
                meter12Month.setCompany(company);
                meter12Month.setYearValue(meterValueTime.getValue());
                mapMeter12Month.put(company, meter12Month);
                listMeter12Month.add(meter12Month);
            } else {
                plusValue(meter12Month, meterValueTime.getValue());
            }

            String stryearmonth = meterValueTime.getTime();
            stryearmonth = stryearmonth.substring(4);
            Integer nmonth = Integer.valueOf(stryearmonth);

            for (Field field : result) {
                String name = "evalue" + nmonth;
                if (name.equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        field.set(meter12Month, meterValueTime.getValue());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        for (Meter12Month meter12Month : listMeter12Month) {
            int nValidMonth = 0;
            String yearValue = meter12Month.getYearValue();
            if (!"0".equals(yearValue)) {
                for (Field field : result) {
                    for (int i = 1; i <= 12; i++) {
                        String name = "evalue" + i;
                        if (name.equals(field.getName())) {
                            try {
                                field.setAccessible(true);
                                String value = field.get(meter12Month).toString();
                                if (!"0".equals(value)) {
                                    nValidMonth++;
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (nValidMonth > 0) {
                    BigDecimal bd1 = new BigDecimal(yearValue);
                    BigDecimal bd2 = new BigDecimal(nValidMonth);
                    bd1 = bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
                    meter12Month.setAvgValue(bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
            }

        }

        map.put("rows", listMeter12Month);
        map.put("total", total);
        return map;
    }

    @RequestMapping("/pagingMeterValueDay")
    @ResponseBody
    public Map pagingMeterValueDay(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();    //分页查询
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer rows = Integer.valueOf(params.get("rows").toString());
        params.put("p1", (page - 1) * rows);
        params.put("p2", (page) * rows);

        String date1 = params.get("date1").toString();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(date1);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date1 = sdf1.format(c.getTime());
        c.add(Calendar.DAY_OF_WEEK, 6);
        String date2 = sdf1.format(c.getTime());
        params.put("date1", date1);
        params.put("date2", date2);

//		List<MeterValueTime> list = satMapper.selectListMeterValueDay(params);

        String companyId = "";
        if (params.get("companyId") != null) {
            companyId = params.get("companyId").toString();
        }

        String projectNo = "";
        if (params.get("projectNo") != null) {
            projectNo = params.get("projectNo").toString();
        }

        List<MeterValueTime> list;

        if (StringUtils.isNoneEmpty(projectNo)) {
            list = satMapper.selectListMeterValueDay2(params);
        } else {
            if (StringUtils.isEmpty(companyId)) {
                list = satMapper.selectListMeterValueDay(params);
            } else {
                if (companyId.length() == 2) {
                    list = satMapper.selectListMeterValueDay1(params);
                } else {
                    list = satMapper.selectListMeterValueDay2(params);
                }
            }
        }

        int total = 0;
        if (StringUtils.isNoneEmpty(projectNo)) {
            total = satMapper.countMeterValueDay2(params);
        } else {
            if (StringUtils.isEmpty(companyId)) {
                total = satMapper.countMeterValueDay(params);
            } else {
                if (companyId.length() == 2) {
                    total = satMapper.countMeterValueDay1(params);
                } else {
                    total = satMapper.countMeterValueDay2(params);
                }
            }
        }


//    	int  total=satMapper.countMeterValueDay(params);
        List<Meter12Month> listMeter12Month = new ArrayList<Meter12Month>();
        Map<String, Meter12Month> mapMeter12Month = new HashMap<String, Meter12Month>();

        Class clazz = Meter12Month.class;
        Field[] result = clazz.getDeclaredFields();

        for (MeterValueTime meterValueTime : list) {
            String company = meterValueTime.getCompany();
            Meter12Month meter12Month = mapMeter12Month.get(company);
            if (meter12Month == null) {
                meter12Month = new Meter12Month();
                meter12Month.setCompany(company);
                meter12Month.setYearValue(meterValueTime.getValue());
                mapMeter12Month.put(company, meter12Month);
                listMeter12Month.add(meter12Month);
            } else {
                plusValue(meter12Month, meterValueTime.getValue());
            }

            String stryearmonth = meterValueTime.getTime();
            int yearmonth = Integer.valueOf(stryearmonth);
            Integer nmonth = yearmonth - Integer.valueOf(date1) + 1;

            for (Field field : result) {
                String name = "evalue" + nmonth;
                if (name.equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        field.set(meter12Month, meterValueTime.getValue());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        map.put("rows", listMeter12Month);
        map.put("total", total);
        return map;
    }

    @RequestMapping("/getCarrierValueDayChart")
    @ResponseBody
    public Map getCarrierValueDayChart(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        List<String> listX = new ArrayList<String>();
        List<String> listY1 = new ArrayList<String>();
        List<String> listY2 = new ArrayList<String>();

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        List<CarrierValueFee> list = satMapper.getCarrierValueDayChart(params);
        for (CarrierValueFee carrierValueFee : list) {
            listX.add(carrierValueFee.getName());
            listY1.add(carrierValueFee.getValue());
            listY2.add(carrierValueFee.getFee());
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", listX);
        map.put("y1", listY1);
        map.put("y2", listY2);
        return map;
    }

    @RequestMapping("/getCarrierValueChart")
    @ResponseBody
    public Map getCarrierValueChart(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        List<String> listX = new ArrayList<String>();
        List<String> listY1 = new ArrayList<String>();
        List<String> listY2 = new ArrayList<String>();

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        List<CarrierValueFee> list = satMapper.getCarrierValueChart(params);
        for (CarrierValueFee carrierValueFee : list) {
            listX.add(carrierValueFee.getName());
            listY1.add(carrierValueFee.getValue());
            listY2.add(carrierValueFee.getFee());
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", listX);
        map.put("y1", listY1);
        map.put("y2", listY2);
        return map;
    }

    @RequestMapping("/getCompanyValueChart")
    @ResponseBody
    public Map getCompanyValueChart(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        List<String> listX = new ArrayList<String>();
        List<String> listY1 = new ArrayList<String>();
        List<String> listY2 = new ArrayList<String>();

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        //查询companyIds
        String companyIds = nbMapper.getCompanyIdsByUserId(userId);
        String[] arr = companyIds.split(",");
        if (StringUtils.isEmpty(companyIds)) {
            companyIds = "('')";
        } else {
            companyIds = "(" + companyIds + ")";
        }
        params.put("companyIds", companyIds);

        List<CarrierValueFee> list = satMapper.getCompanyValueChart(params);
        for (CarrierValueFee carrierValueFee : list) {
            listX.add(carrierValueFee.getName());
            listY1.add(carrierValueFee.getValue());
            listY2.add(carrierValueFee.getFee());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", listX);
        map.put("y1", listY1);
        map.put("y2", listY2);
        return map;
    }

    @RequestMapping("/getCompanyValueDayChart")
    @ResponseBody
    public Map getCompanyValueDayChart(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        List<String> listX = new ArrayList<String>();
        List<String> listY1 = new ArrayList<String>();
        List<String> listY2 = new ArrayList<String>();

        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        //查询companyIds
        String companyIds = nbMapper.getCompanyIdsByUserId(userId);
        String[] arr = companyIds.split(",");
        if (StringUtils.isEmpty(companyIds)) {
            companyIds = "('')";
        } else {
            companyIds = "(" + companyIds + ")";
        }
        params.put("companyIds", companyIds);

        List<CarrierValueFee> list = satMapper.getCompanyValueDayChart(params);
        for (CarrierValueFee carrierValueFee : list) {
            listX.add(carrierValueFee.getName());
            listY1.add(carrierValueFee.getValue());
            listY2.add(carrierValueFee.getFee());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", listX);
        map.put("y1", listY1);
        map.put("y2", listY2);
        return map;
    }

    @RequestMapping("/companyYearMonthValue")
    @ResponseBody
    public Map companyYearMonthValue(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getSession().getAttribute("userId").toString();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        List<CompanyYearMonthValue> list = meterMapper.selectCompanyYearMonthValueByYearAndUserId(Integer.valueOf(userId), year);
        map.put("year", year);
        map.put("rows", list);
        return map;
    }


}
