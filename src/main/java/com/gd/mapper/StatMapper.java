package com.gd.mapper;

import java.util.List;
import java.util.Map;

import com.gd.model.po.CarrierValueFee;
import com.gd.model.po.Meter;
import com.gd.model.po.MeterValueTime;

/**
 * @author ZhouHR
 */
public interface StatMapper {

    int countMeterNum(Map<String, Object> map);

    List<Meter> selectListMeterNum(Map<String, Object> map);

    int countMeterNum1(Map<String, Object> map);

    List<Meter> selectListMeterNum1(Map<String, Object> map);

    int countMeterNum2(Map<String, Object> map);

    List<Meter> selectListMeterNum2(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValueTime(Map<String, String> map);

    List<MeterValueTime> selectListMeterValueTimeDay(Map<String, String> map);

    int countMeterValue(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValue(Map<String, Object> map);

    int countMeterValue1(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValue1(Map<String, Object> map);

    int countMeterValue2(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValue2(Map<String, Object> map);

    int countMeterValueDay(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValueDay(Map<String, Object> map);

    int countMeterValueDay1(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValueDay1(Map<String, Object> map);

    int countMeterValueDay2(Map<String, Object> map);

    List<MeterValueTime> selectListMeterValueDay2(Map<String, Object> map);

    int countCarrierValue(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValue(Map<String, Object> map);

    int countCarrierValue1(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValue1(Map<String, Object> map);

    int countCarrierValue2(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValue2(Map<String, Object> map);

    int countCarrierValueDay(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValueDay(Map<String, Object> map);

    int countCarrierValueDay1(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValueDay1(Map<String, Object> map);

    int countCarrierValueDay2(Map<String, Object> map);

    List<CarrierValueFee> selectListCarrierValueDay2(Map<String, Object> map);

    List<CarrierValueFee> getCarrierValueDayChart(Map<String, Object> map);

    List<CarrierValueFee> getCarrierValueChart(Map<String, Object> map);

    List<CarrierValueFee> getCompanyValueDayChart(Map<String, Object> map);

    List<CarrierValueFee> getCompanyValueChart(Map<String, Object> map);
}
