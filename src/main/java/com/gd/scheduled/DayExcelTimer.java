package com.gd.scheduled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gd.mapper.ExportMapper;
import com.gd.model.po.MeterMonthReport;
import com.gd.redis.InitLoadToRedis;
import com.gd.util.DateUtil;
import com.gdiot.ssm.entity.Project;



//@Component
@Slf4j
public class DayExcelTimer {
/*	
	@Autowired
	private ExportMapper exportMapper;
	
//	@Scheduled(cron = "0/5 * * * * ?")
//	@Scheduled(cron = "0 0 4 * * ?")
    public void dayEvalueToExcel() {//日电量excel生成
		
		//获取存放路径 excel
		String path=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		log.info("Thread.currentThread() path is :"+path);
		int pos=path.indexOf("WEB-INF");
		if(pos>0)
			path=path.substring(0,pos);
		String templatePath=path+ "template/monthReport.xlsx";;
		
		path=path+"excel/";
		log.info("Thread.currentThread() path is :"+path);
		Calendar c = Calendar.getInstance();
		//第二天凌晨执行的，所以日期-1
		c.add(Calendar.DAY_OF_YEAR, -1);
//		c.add(Calendar.MONTH, -3);
//		String fileName=path+DateUtil.getYearMonth()+"月电量数据汇总.xlsx";
		String fileName=path+DateUtil.getYearMonth(c.getTime())+"Value.xlsx";
		File file=null;
		InputStream in = null;
		OutputStream out = null;
		try{
			in = new FileInputStream(templatePath);
			Workbook workbook = new XSSFWorkbook(in);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(0);
			
			Integer days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

			//这里把模板超出的列表头清空
			for (Integer i = days + 1; i <= 32; i++) {
				Cell cell = row.getCell(1 + i);
				cell.setCellValue("");
			}

			// 开始填数据了
			int rowNum = 0;
			long t2,t1=System.currentTimeMillis();
			List<MeterMonthReport> list = exportMapper.selectMonthMeterValueAll(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
			t2=System.currentTimeMillis();
			log.info("=========================get meter day value size="+list.size()+"========spend time="+(t2-t1)+"==============");
			
			String lastMeterNoProjectNo = "";
			int listPos=0;
			while(listPos<list.size()){
				MeterMonthReport meterMonthReport=list.get(listPos);
				String meterNoProjectNo = meterMonthReport.getMeterNo()+ meterMonthReport.getProjectNo();
				if(meterNoProjectNo.equals(lastMeterNoProjectNo)){
					listPos++;
					continue;
				}
				if (!meterNoProjectNo.equals(lastMeterNoProjectNo)) {
					lastMeterNoProjectNo=meterNoProjectNo;
					//开始新的一行罗
					Cell cell = null;
					int cellNum = 0;
					rowNum++;
					row = sheet.createRow(rowNum);

					cell = row.createCell(cellNum);
					cellNum++;
					cell.setCellValue(meterMonthReport.getCompany());//0公司

					cell = row.createCell(cellNum);
					cellNum++;
					cell.setCellValue(meterMonthReport.getMeterNo());//2表号

					//处理每天的数据
					int tmplistPos=0;
					for (Integer i = 1; i <= days; i++) {
						boolean bFind=false;
						MeterMonthReport meterMonthReportFind=null;
						for(int j=listPos;j<list.size();j++){
							tmplistPos=j;
							MeterMonthReport meterMonthReportDay=list.get(j);
							String meterNoProjectNoDay = meterMonthReportDay.getMeterNo()+ meterMonthReportDay.getProjectNo();
							if(!meterNoProjectNoDay.equals(meterNoProjectNo)){
								
								break;
							}
							if(i.toString().equals(meterMonthReportDay.getDay())){
								bFind=true;
								meterMonthReportFind=meterMonthReportDay;
								break;
							}
						}
						cell = row.createCell(cellNum);
						cellNum++;
						if(bFind){
							cell.setCellValue(Double.valueOf(meterMonthReportFind.getEvalue()));
						}else
							cell.setCellValue(0.00);
						
					}
					
					listPos=tmplistPos;
					//如果是最后一条了，把listPos+1，退出循环
//					if(listPos==list.size()-1)
//						listPos++;
				}
			}
			
			sum(sheet,rowNum,days+2);

			file=new File(fileName);
			out = new FileOutputStream(file);
			workbook.write(out);
			workbook.close();
			out.flush();
			out.close();
			in.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
//			if(file!=null)
//				file.cl
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
	
	private void sum(Sheet sheet,int rowNum,int columnNum){
		if(rowNum==0) return;
		Row row = sheet.getRow(0);
		Cell cell=row.getCell(columnNum);
		cell.setCellValue("汇总");
		List<BigDecimal> list=new ArrayList<BigDecimal>();
		BigDecimal all1=new BigDecimal(0);
		for(int i=1;i<=rowNum;i++){
			row=sheet.getRow(i);
//			BigDecimal bd=new BigDecimal(0);
			BigDecimal all=new BigDecimal(0);
			for(int j=5;j<columnNum;j++){
				cell=row.getCell(j);
				int cellType=cell.getCellType();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String str=cell.getStringCellValue();
				cell.setCellType(cellType);
				cell.setCellValue(Double.valueOf(str));
				all=all.add(new BigDecimal(str));
				all1=all1.add(new BigDecimal(str));;
//				bd=bd.add(new BigDecimal(str));
				
				int pos=j-5;
				if(list.size()>pos){
					BigDecimal d=list.get(pos);
					d=d.add(new BigDecimal(str));
					list.set(pos, d);
				}else{
					list.add(new BigDecimal(str));
				}
			}
			cell=row.createCell(columnNum);
			
//			double number1 = 1; double number2 = 20.2; double number3 = 300.03; double result = number1 + number2 + number3; System.out.println(result);
			
			cell.setCellValue(Double.valueOf(all.toPlainString()));
		}
		
		rowNum++;
		row=sheet.createRow(rowNum);
		for(int j=5;j<columnNum;j++){
			cell=row.createCell(j);
			cell.setCellValue(Double.valueOf((list.get(j-5).toPlainString())));
		}
		cell=row.createCell(columnNum);
		cell.setCellValue(Double.valueOf(all1.toPlainString()));
	}
	

//	@Scheduled(cron = "0/5 * * * * ?")
//	@Scheduled(cron = "0 0 4 * * ?")
    public void dayFeeToExcel() {//日电费excel生成
		
		//获取存放路径 excel
		String path=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		log.info("Thread.currentThread() path is :"+path);
		int pos=path.indexOf("WEB-INF");
		if(pos>0)
			path=path.substring(0,pos);
		String templatePath=path+ "template/monthReport.xlsx";;
		
		path=path+"excel/";
		log.info("Thread.currentThread() path is :"+path);
		Calendar c = Calendar.getInstance();
		//第二天凌晨执行的，所以日期-1
		c.add(Calendar.DAY_OF_YEAR, -1);
//		c.add(Calendar.MONTH, -3);
//		String fileName=path+DateUtil.getYearMonth()+"月电量数据汇总.xlsx";
		String fileName=path+DateUtil.getYearMonth(c.getTime())+"Fee.xlsx";
		File file=null;
		InputStream in = null;
		OutputStream out = null;
		try{
			in = new FileInputStream(templatePath);
			Workbook workbook = new XSSFWorkbook(in);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(0);
			
			Integer days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

			//这里把模板超出的列表头清空
			for (Integer i = days + 1; i <= 32; i++) {
				Cell cell = row.getCell(1 + i);
				cell.setCellValue("");
			}

			// 开始填数据了
			int rowNum = 0;
			long t2,t1=System.currentTimeMillis();
			List<MeterMonthReport> list = exportMapper.selectMonthMeterValueAll(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
			t2=System.currentTimeMillis();
			log.info("=========================get meter day value size="+list.size()+"========spend time="+(t2-t1)+"==============");
			
			String lastMeterNoProjectNo = "";
			int listPos=0;
			while(listPos<list.size()){
				MeterMonthReport meterMonthReport=list.get(listPos);
				String meterNoProjectNo = meterMonthReport.getMeterNo()+ meterMonthReport.getProjectNo();
				if(meterNoProjectNo.equals(lastMeterNoProjectNo)){
					listPos++;
					continue;
				}
				if (!meterNoProjectNo.equals(lastMeterNoProjectNo)) {
					lastMeterNoProjectNo=meterNoProjectNo;
					//开始新的一行罗
					Cell cell = null;
					int cellNum = 0;
					rowNum++;
					row = sheet.createRow(rowNum);

					cell = row.createCell(cellNum);
					cellNum++;
					cell.setCellValue(meterMonthReport.getCompany());//0公司

					cell = row.createCell(cellNum);
					cellNum++;
					cell.setCellValue(meterMonthReport.getMeterNo());//2表号

					//处理每天的数据
					int tmplistPos=0;
					for (Integer i = 1; i <= days; i++) {
						boolean bFind=false;
						MeterMonthReport meterMonthReportFind=null;
						for(int j=listPos;j<list.size();j++){
							tmplistPos=j;
							MeterMonthReport meterMonthReportDay=list.get(j);
							String meterNoProjectNoDay = meterMonthReportDay.getMeterNo()+ meterMonthReportDay.getProjectNo();
							if(!meterNoProjectNoDay.equals(meterNoProjectNo)){
								
								break;
							}
							if(i.toString().equals(meterMonthReportDay.getDay())){
								bFind=true;
								meterMonthReportFind=meterMonthReportDay;
								break;
							}
						}
						cell = row.createCell(cellNum);
						cellNum++;
						if(bFind){
							cell.setCellValue(Double.valueOf(meterMonthReportFind.getFee()));
						}else
							cell.setCellValue(0.00);
						
					}
					
					listPos=tmplistPos;
					//如果是最后一条了，把listPos+1，退出循环
//					if(listPos==list.size()-1)
//						listPos++;
				}
			}
			
			sum(sheet,rowNum,days+2);

			file=new File(fileName);
			out = new FileOutputStream(file);
			workbook.write(out);
			workbook.close();
			out.flush();
			out.close();
			in.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
//			if(file!=null)
//				file.cl
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    */
}
