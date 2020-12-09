package com.gd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.CompanyYearMonthValue;
import com.gd.model.po.Meter;
import com.gd.model.po.Meter3Value;
import com.gd.model.po.MeterLostData;
import com.gd.model.po.MeterMonthReport;
import com.gd.model.po.MeterSendDownLog;
import com.gd.model.po.MeterWarning;
import com.gd.model.po.NBValue;
import com.gd.model.po.TbMeterInfo;

/**
 * @author ZhouHR
 */
public interface MeterMapper {

    int countByCondition(Map<String, Object> map);

    List<Meter> selectList(Map<String, Object> map);

    int countDownSendByCondition(Map<String, Object> map);

    List<Meter> selectDownSendList(Map<String, Object> map);

    int countMeterRealCheckByCondition(Map<String, Object> map);

    List<Meter> selectMeterRealCheckList(Map<String, Object> map);

    List<MeterMonthReport> getMonthReport(Map<String, Object> map);

    int countMonthReport(Map<String, Object> map);

    List<MeterMonthReport> getDayReport(Map<String, Object> map);

    int countDayReport(Map<String, Object> map);

    List<MeterWarning> getMonthWarning(Map<String, Object> map);

    int getMonthWarningCount(Map<String, Object> map);

    int countMeterLostData(Map<String, Object> map);

    List<MeterLostData> selectMeterLostDataList(Map<String, Object> map);

    Meter getMeterByMeterNo(@Param("meterNo") String meterNo);

    String getMeterLastInstallCompany(@Param("meterNo") String meterNo);

    void insertDownLog(MeterSendDownLog meterSendDownLog);

    int countTbMeterInfo(Map<String, Object> map);

    List<TbMeterInfo> selectTbMeterInfo(Map<String, Object> map);

    String getReport(@Param("imei") String imei);

    int countMeterLowValueByCondition(Map<String, Object> map);

    List<NBValue> selectListMeterLowValue(Map<String, Object> map);

    List<NBValue> exportListMeterLowValue(Map<String, Object> map);

    List<Meter> meterInstallReport(Map<String, Object> map);

    List<Meter> selectAllMeter();

    void insertMeter(Meter meter);

    List<String> selectCompnayIdByUserId(@Param("userId") int userId);

    List<String> selectCompnayIdByName(@Param("name") String name);

    void deleteById(@Param("id") int id);

    List<CompanyYearMonthValue> selectCompanyYearMonthValueByYearAndUserId(@Param("userId") int userId, @Param("year") int year);

    void updateMeterStatus(@Param("meterNo") String meterNo, @Param("status") int status);

    void updateMeterPrice(Map<String, Object> map);

    void insertDayReport(MeterMonthReport meterMonthReport);

    String getYesterdayMaxMeterDayValue(@Param("meterNo") String meterNo, @Param("time1") Long time1);

    String getMaxMeterDayValue(@Param("meterNo") String meterNo, @Param("time1") Long time1);

    MeterMonthReport getLastDayReport(@Param("meterNo") String meterNo);


}
