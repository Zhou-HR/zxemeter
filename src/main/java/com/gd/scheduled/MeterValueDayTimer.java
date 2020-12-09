package com.gd.scheduled;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.mapper.MeterMapper;
import com.gd.model.po.Meter;
import com.gd.model.po.MeterMonthReport;

/**
 * @author ZhouHR
 */
@Component
@Slf4j
public class MeterValueDayTimer {

    @Autowired
    private MeterMapper meterMapper;

    @Scheduled(cron = "0 0 3 * * ?")//03:00
//	@Scheduled(cron = "0/5 * * * * ?")//03:00
    public void syncMonthValueSchedule() {
        log.info("start sync day value schedule!!!");

        Calendar cnow = Calendar.getInstance();
        cnow.add(Calendar.DAY_OF_MONTH, -1);

        long duration = 0;
        String name = "sync day value";
        String msg = "success";
        int count = 0;
        long start = System.currentTimeMillis();
        try {
//			int addDay=1;
//			Calendar cx1=Calendar.getInstance();
//			cx1.add(Calendar.DAY_OF_MONTH, -2);


            Calendar c = Calendar.getInstance();
            //测试，正式要注释
//			c.add(Calendar.DAY_OF_MONTH, -1);
//			c.set(Calendar.MONTH, 2);
//			c.set(Calendar.DAY_OF_MONTH, 1);
//			
//			while(c.getTimeInMillis()<cx1.getTimeInMillis())
            {

//			c.add(Calendar.DAY_OF_MONTH, 1);

                c.add(Calendar.DAY_OF_MONTH, -1);//处理昨天的
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                String createTime = df1.format(c.getTime());

//			c.add(Calendar.MONTH, -1);
//			c.add(Calendar.MONTH, -1);
//			c.set(Calendar.DAY_OF_MONTH, 1);//4月1日
//			System.out.println(c.getTime());

//			while(true){
                long nowMonthDay = c.getTimeInMillis() / 1000;
                int yearmonth = c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH);

                Integer year = c.get(Calendar.YEAR);
                Integer month = c.get(Calendar.MONTH) + 1;
                Integer day = c.get(Calendar.DAY_OF_MONTH);

                c.add(Calendar.DAY_OF_MONTH, -1);

                int lastyearmonth = c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH);

                c.add(Calendar.DAY_OF_MONTH, 2);//5月1日
//			long lastMonthDay=c.getTimeInMillis()/1000;

                List<Meter> listMeterNo = meterMapper.selectAllMeter();
                for (Meter meter : listMeterNo) {
                    String meterNo = meter.getMeterNo();
                    System.out.println(meterNo);

                    //先看有没有日电量
                    MeterMonthReport meterMonthReport = meterMapper.getLastDayReport(meterNo);

                    //获取昨天的最大电量
                    String maxValue = meterMapper.getYesterdayMaxMeterDayValue(meterNo, nowMonthDay);

                    if (meterMonthReport == null) {
                        if (StringUtils.isEmpty(maxValue)) {//2者皆空，则插入一条
                            //continue;
                            maxValue = meterMapper.getMaxMeterDayValue(meterNo, nowMonthDay);
                            if (StringUtils.isEmpty(maxValue)) continue;
                        }
                        meterMonthReport = new MeterMonthReport();
                        meterMonthReport.setMeterNo(meterNo);
                        BigDecimal bd1 = new BigDecimal(meter.getNewValue());
                        BigDecimal bd2 = new BigDecimal(maxValue);
                        BigDecimal bd3 = bd2.subtract(bd1);
                        //判断是否是负数
                        BigDecimal bd4 = new BigDecimal("0");
                        if (bd3.compareTo(bd4) < 0)
                            bd3 = bd4;
                        meterMonthReport.setEvalue(bd3.toPlainString());
                        meterMonthReport.setNewMeterValue(meter.getNewValue());
                        meterMonthReport.setEvalue2(maxValue);
                        meterMonthReport.setEvalue1(meter.getNewValue());

                    } else {//有最近的日报
                        if (StringUtils.isEmpty(maxValue)) continue;//
                        BigDecimal bd1 = new BigDecimal(meterMonthReport.getEvalue());
                        BigDecimal bd2 = new BigDecimal(maxValue);
                        BigDecimal bd3 = bd2.subtract(bd1);
                        //判断是否是负数
                        BigDecimal bd4 = new BigDecimal("0");
                        if (bd3.compareTo(bd4) < 0)
                            bd3 = bd4;
                        meterMonthReport.setEvalue1(meterMonthReport.getEvalue());
                        meterMonthReport.setEvalue(bd3.toPlainString());
                        meterMonthReport.setEvalue2(maxValue);

                    }
                    meterMonthReport.setYearmonth(yearmonth);
                    meterMonthReport.setYear(year.toString());
                    meterMonthReport.setMonth(month.toString());
                    meterMonthReport.setDay(day.toString());
                    meterMonthReport.setCompanyId(meter.getCompanyId());

                    //计算电费
                    String price = meter.getPrice();
                    BigDecimal bd1 = new BigDecimal(price);
                    BigDecimal bd2 = new BigDecimal(meterMonthReport.getEvalue());
                    BigDecimal bd3 = bd1.multiply(bd2).setScale(4, BigDecimal.ROUND_HALF_UP);
                    meterMonthReport.setFee(bd3.toPlainString());

                    meterMapper.insertDayReport(meterMonthReport);
                }
                //都完成以后，统一处理运营商比例和费用
//			meterMapper.updateTodayReport1(yearmonth);
//			meterMapper.updateTodayReport2(yearmonth);
//			meterMapper.updateTodayReport3(yearmonth);
//			meterMapper.updateTodayReport4(yearmonth);

            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
        }

        log.info("end sync day value schedule!!!");
    }
	
	/*
	private Meter3Value getMeter3Value(String meterNo,String meterType,long nowMonthDay){
		if("nb".equals(meterType)){
			List<Meter3Value> meter3ValueMaxList=meterMapper.getMaxMeterDayValue(meterNo,meterType, nowMonthDay);
			if(meter3ValueMaxList.size()==0) return null;
			Meter3Value meter3ValueMax=meter3ValueMaxList.get(0);
			
			return meter3ValueMax;
		}
		if("广东雅达2g".equals(meterType)){
			List<Meter3Value> meter3ValueMaxList=meterMapper.getMaxMeterDayValueGuangDong2g(meterNo,meterType, nowMonthDay);
			if(meter3ValueMaxList.size()==0) return null;
			Meter3Value meter3ValueMax=meter3ValueMaxList.get(0);
			
			return meter3ValueMax;
		}
		if("2g".equals(meterType)){
			List<Meter3Value> meter3ValueMaxList=meterMapper.getMaxMeterDayValue2g(meterNo,meterType, nowMonthDay);
			if(meter3ValueMaxList.size()==0) return null;
			Meter3Value meter3ValueMax=meter3ValueMaxList.get(0);
			
			return meter3ValueMax;
		}
		
		return null;
	}
	
	private void insertDayReport(MeterMonthReport meterMonthReport){
		Project project=InitLoadToRedis.getProject(meterMonthReport.getProjectNo());
		if(project==null){//找不到基站直接就不插入了
			log.error(meterMonthReport.getProjectNo()+"cannot find a project!!!");
			return;
		}
		
		//插入之前判断是否负值，若为负值，设置为0
		String value=meterMonthReport.getEvalue();
		try{
			double d=Double.valueOf(value);
			if(d<0) {
				log.error("meterMonthReport "+meterMonthReport.getMeterNo()+meterMonthReport.getYearmonth().toString()+meterMonthReport.getEvalue()+" is invalid!!!");
				meterMonthReport.setEvalue("0");
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			log.error("meterMonthReport "+meterMonthReport.getMeterNo()+meterMonthReport.getYearmonth().toString()+meterMonthReport.getEvalue()+" is invalid!!!");
			return;
		}
		if(StringUtils.isNotEmpty(meterMonthReport.getEvalue()))//evalue不为空才插入噢
			meterMapper.insertDayReport(meterMonthReport);
		
	}
	*/
}
