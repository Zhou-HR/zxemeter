package com.gd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gd.model.po.MeterMonthReport;

/**
 * @author ZhouHR
 */
public interface ExportMapper {

    List<MeterMonthReport> selectMonthMeterValue(@Param("year") String year, @Param("month") String month, @Param("userId") String userId);

    List<MeterMonthReport> selectMonthMeterValueAll(@Param("year") int year, @Param("month") int month, @Param("companyIds") String companyIds);

    List<String> selectCarrierType(@Param("year") String year, @Param("month") String month, @Param("userId") String userId);

    List<MeterMonthReport> selectMonthCarrierValue(@Param("year") String year, @Param("month") String month, @Param("carrierName") String carrierName, @Param("userId") String userId);
}
