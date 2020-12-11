package com.gd.emeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gd.mapper.ExportMapper;
import com.gd.mapper.MeterMapper;
import com.gd.mapper.MeterWarningMapper;
import com.gd.mapper.NBMapper;
import com.gd.model.po.Meter;
import com.gd.model.po.MeterMonthReport;
import com.gd.model.po.MeterWarning;
import com.gd.model.po.NBValue;
import com.gd.model.po.OfflineReport;
import com.gd.redis.InitLoadToRedis;
import com.gd.util.DateUtil;
import com.gdiot.ssm.entity.Project;

/**
 * @author ZhouHR
 */
@Slf4j
@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportMapper exportMapper;

    @Autowired
    private MeterMapper meterMapper;

    @Autowired
    private NBMapper nbMapper;

    @Autowired
    private MeterWarningMapper meterWarningMapper;

    @RequestMapping("/downloadMeterImportTemplate")
    public void downloadMeterImportTemplate(HttpServletRequest request, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "/template/meterImportTemplate.xlsx";

            in = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(in);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String("电表导入模板".getBytes("gbk"), "iso8859-1")
                    + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/meterInstallReport")
    public void meterInstallReport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        InputStream in = null;
        OutputStream out = null;
        String fileNameCheckTime = "";
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        List<Meter> list = meterMapper.meterInstallReport(params);

        try {
            fileNameCheckTime = sdf.format(new Date());
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "/template/meterInstallStatus.xlsx";
            log.info("========================201901017yxl path=====" + path);

            in = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 1;
            for (Meter meter : list) {

                Row row = sheet.createRow(rowNum);
                rowNum++;

                int cellNum = 0;
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(meter.getCompany());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getUnit());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getMeterNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getMeterType());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getProjectNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getProjectName());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getAddress());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getStrStatus());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meter.getStrInstllTime());
                cellNum++;

            }
            String filename = "电表安装情况" + fileNameCheckTime;
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/offlineReport")
    public void offlineReport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);

        List<OfflineReport> list = meterWarningMapper.offlineReport();
        for (OfflineReport offlineReport : list) {
            String companyId = offlineReport.getCompanyId();
            companyId = companyId.substring(0, 2);
            offlineReport.setCompany(InitLoadToRedis.getCompanyName(companyId));
        }

        InputStream in = null;
        OutputStream out = null;
        String fileNameCheckTime = "";
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        try {
            fileNameCheckTime = sdf.format(c.getTime());
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "/template/offlineReport.xlsx";
            log.info("========================20190709yxl path=====" + path);

            in = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 1;
            for (OfflineReport offlineReport : list) {
//				fileNameCheckTime=offlineReport.getCheckTime();

                Row row = sheet.createRow(rowNum);
                rowNum++;

                int cellNum = 0;
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getCompany());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getUnit());
                cellNum++;

                ///////////////////
                cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getProjectName());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getProjectNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getMeterNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(offlineReport.getLastSendTime());
                cellNum++;

                //获取24小时断电时间
                String lastSendTime = offlineReport.getLastSendTime();
                String meterNo = offlineReport.getMeterNo();
                if (StringUtils.isEmpty(lastSendTime)) {
                    cellNum += 4;
                    cell = row.createCell(cellNum);
                    cell.setCellValue(offlineReport.getInstallTime());
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellValue(fileNameCheckTime);
                    cellNum++;
                } else {
                    OfflineReport powOff = meterWarningMapper.powerOff(meterNo, lastSendTime);
                    String powOffTime = "";
                    if (powOff != null) {
                        powOffTime = powOff.getEstarttime() + "~" + powOff.getEendtime();
                    }
                    cell = row.createCell(cellNum);
                    cell.setCellValue(powOffTime);
                    cellNum++;

                    //20191016 yxl 改为从历史库中拿数据
                    List<NBValue> lstNBValue = new ArrayList<NBValue>();
                    long lmeterNo = Long.valueOf(meterNo);
                    Long mod = lmeterNo % 1000;
                    String strMod = mod.toString();
                    while (strMod.length() < 3) {
                        strMod = "0" + strMod;
                    }
                    log.info(strMod);
//					lstNBValue=meterWarningMapper.offLineDeal(meterNo, lastSendTime,offlineReport.getCompanyId());
                    lstNBValue = meterWarningMapper.offLineDeal(meterNo, lastSendTime, strMod);

                    String minEsignal = "";
                    String maxESeq = "";
                    Map<String, Integer> repeatSeqMap = new HashMap<String, Integer>();
                    Integer repeatSeq = 0;
                    if ("190623101526".equals(meterNo)) {
                        log.info(meterNo);
                    }
                    for (NBValue nbValue : lstNBValue) {

                        if (repeatSeqMap.get(nbValue.getEseq()) != null) {
                            repeatSeq++;
                        }
                        repeatSeqMap.put(nbValue.getEseq(), 1);
                        if (StringUtils.isEmpty(minEsignal)) {
                            minEsignal = nbValue.getEsignal();
                        } else {
                            if (Integer.valueOf(nbValue.getEsignal()) < Integer.valueOf(minEsignal)) {
                                minEsignal = nbValue.getEsignal();
                            }
                        }
                        if (StringUtils.isEmpty(maxESeq)) {
                            maxESeq = nbValue.getEseq();
                        } else {
                            if (Integer.valueOf(nbValue.getEseq()) > Integer.valueOf(maxESeq)) {
                                maxESeq = nbValue.getEseq();
                            }
                        }
                    }

                    cell = row.createCell(cellNum);
                    if (StringUtils.isEmpty(minEsignal)) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(Integer.valueOf(minEsignal));
                    }
                    cellNum++;

                    cell = row.createCell(cellNum);
                    cell.setCellValue(repeatSeq);
                    cellNum++;

                    cell = row.createCell(cellNum);
                    if (StringUtils.isEmpty(maxESeq)) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(Integer.valueOf(maxESeq));
                    }
                    cellNum++;

                    cell = row.createCell(cellNum);
                    cell.setCellValue(offlineReport.getInstallTime());
                    cellNum++;

                    cell = row.createCell(cellNum);
                    cell.setCellValue(fileNameCheckTime);
                    cellNum++;

                }
            }
            fileNameCheckTime = fileNameCheckTime.replace(":", "_");
            fileNameCheckTime = fileNameCheckTime.replace(" ", "_");
            String filename = "离线电表分析" + fileNameCheckTime;
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/meterLowValue")
    public void meterLowValue(String companyId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);
        if (StringUtils.isNotEmpty(companyId)) {
            params.put("companyId", companyId);
        }
        List<NBValue> list = meterMapper.exportListMeterLowValue(params);
        for (NBValue nbValue : list) {
            //设置 公司 事业部
            companyId = nbValue.getCompanyId();
            if (StringUtils.isNotEmpty(companyId)) {
                nbValue.setUnit(InitLoadToRedis.getCompanyName(companyId));
                companyId = companyId.substring(0, 2);
                nbValue.setCompany(InitLoadToRedis.getCompanyName(companyId));
            }
            String projectNo = nbValue.getProjectNo();
            if (StringUtils.isNotEmpty(projectNo)) {
                Project project = InitLoadToRedis.getProject(projectNo);
                if (project != null) {
                    nbValue.setProjectName(project.getProjectName());
                }
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            String filename = "低电量电表";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "/template/meterLowValue.xlsx";
            log.info("========================20190709yxl path=====" + path);

            in = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 1;
            for (NBValue nbValue : list) {
                Row row = sheet.createRow(rowNum);
                rowNum++;

                int cellNum = 0;
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getCompany());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getUnit());
                cellNum++;

                ///////////////////
                cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getProjectName());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getProjectNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getMeterNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(nbValue.getEvalue());
                cellNum++;

            }
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/meterWarning")
    public void meterWarning(String companyId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = request.getSession().getAttribute("userId").toString();
        params.put("userId", userId);
        if (StringUtils.isNotEmpty(companyId))
            params.put("companyId", companyId);
        List<MeterWarning> list = meterWarningMapper.selectExportList(params);
        for (MeterWarning meterWarning : list) {
            //设置 公司 事业部
            companyId = meterWarning.getCompanyId();
            if (StringUtils.isNotEmpty(companyId)) {
                meterWarning.setUnit(InitLoadToRedis.getCompanyName(companyId));
                companyId = companyId.substring(0, 2);
                meterWarning.setCompany(InitLoadToRedis.getCompanyName(companyId));
            }
            String projectNo = meterWarning.getProjectNo();
            if (StringUtils.isNotEmpty(projectNo)) {
                Project project = InitLoadToRedis.getProject(projectNo);
                if (project != null)
                    meterWarning.setProjectName(project.getProjectName());
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            String filename = "智能电表报警信息";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "/template/warningReport.xlsx";
            log.info("========================20190709yxl path=====" + path);

            in = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 1;
            for (MeterWarning meterWarning : list) {
                Row row = sheet.createRow(rowNum);
                rowNum++;

                int cellNum = 0;
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getCompany());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getUnit());
                cellNum++;

                ///////////////////
                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getProjectName());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getProjectNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getMeterNo());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getDescription());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getEstarttime());
                cellNum++;

                cell = row.createCell(cellNum);
                cell.setCellValue(meterWarning.getEendtime());
                cellNum++;

            }
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/monthMeterValue")
    public void monthMeterValue(String year, String month,
                                HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        try {
            Calendar c = Calendar.getInstance();
            if (StringUtils.isEmpty(year))
                year = String.valueOf(c.get(Calendar.YEAR));
            if (StringUtils.isEmpty(month))
                month = String.valueOf(c.get(Calendar.MONTH) + 1);

            c.set(Calendar.YEAR, Integer.valueOf(year));
            c.set(Calendar.MONTH, Integer.valueOf(month) - 1);

            String yearmonth = year + month;
            if (yearmonth.length() == 5)
                yearmonth = year + "0" + month;
            String filename = yearmonth + "月电量数据汇总";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");

            out = response.getOutputStream();
            String userId = request.getSession().getAttribute("userId").toString();
            dayEvalueToExcel(out, "value", userId);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("========================20190709yxl error========" + e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @RequestMapping("/monthMeterFee")
    public void monthMeterFee(String year, String month,
                              HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        try {
            Calendar c = Calendar.getInstance();
            if (StringUtils.isEmpty(year))
                year = String.valueOf(c.get(Calendar.YEAR));
            if (StringUtils.isEmpty(month))
                month = String.valueOf(c.get(Calendar.MONTH) + 1);

            c.set(Calendar.YEAR, Integer.valueOf(year));
            c.set(Calendar.MONTH, Integer.valueOf(month) - 1);

            String yearmonth = year + month;
            if (yearmonth.length() == 5)
                yearmonth = year + "0" + month;
            String filename = yearmonth + "月电费数据汇总";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso8859-1")
                    + ".xlsx");

            out = response.getOutputStream();
            String userId = request.getSession().getAttribute("userId").toString();
            dayEvalueToExcel(out, "fee", userId);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("========================20190709yxl error========" + e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void dayEvalueToExcel(OutputStream out, String type, String userId) {//日电量excel生成

        //获取存放路径 excel
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        log.info("Thread.currentThread() path is :" + path);
        int pos = path.indexOf("WEB-INF");
        if (pos > 0)
            path = path.substring(0, pos);
        String templatePath = path + "template/monthReport.xlsx";
        ;

        path = path + "excel/";
        log.info("Thread.currentThread() path is :" + path);
        Calendar c = Calendar.getInstance();
        //第二天凌晨执行的，所以日期-1
        c.add(Calendar.DAY_OF_YEAR, -1);
//		c.add(Calendar.MONTH, -3);
//		String fileName=path+DateUtil.getYearMonth()+"月电量数据汇总.xlsx";
        String fileName = path + DateUtil.getYearMonth(c.getTime()) + "Value.xlsx";
        File file = null;
        InputStream in = null;
//		OutputStream out = null;
        try {
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
            long t2, t1 = System.currentTimeMillis();

            //查询companyIds
            String companyIds = nbMapper.getCompanyIdsByUserId(userId);
            String[] arr = companyIds.split(",");
            if (StringUtils.isEmpty(companyIds)) {
                companyIds = "('')";
            } else {
                companyIds = "(" + companyIds + ")";
            }

            List<MeterMonthReport> list = exportMapper.selectMonthMeterValueAll(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, companyIds);
            t2 = System.currentTimeMillis();
            log.info("=========================get meter day value size=" + list.size() + "========spend time=" + (t2 - t1) + "==============");

            String lastMeterNoProjectNo = "";
            int listPos = 0;
            while (listPos < list.size()) {
                MeterMonthReport meterMonthReport = list.get(listPos);
                String meterNoProjectNo = meterMonthReport.getMeterNo() + meterMonthReport.getProjectNo();
                if (meterNoProjectNo.equals(lastMeterNoProjectNo)) {
                    listPos++;
                    continue;
                }
                if (!meterNoProjectNo.equals(lastMeterNoProjectNo)) {
                    lastMeterNoProjectNo = meterNoProjectNo;
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
                    int tmplistPos = 0;
                    for (Integer i = 1; i <= days; i++) {
                        boolean bFind = false;
                        MeterMonthReport meterMonthReportFind = null;
                        for (int j = listPos; j < list.size(); j++) {
                            tmplistPos = j;
                            MeterMonthReport meterMonthReportDay = list.get(j);
                            String meterNoProjectNoDay = meterMonthReportDay.getMeterNo() + meterMonthReportDay.getProjectNo();
                            if (!meterNoProjectNoDay.equals(meterNoProjectNo)) {

                                break;
                            }
                            if (i.toString().equals(meterMonthReportDay.getDay())) {
                                bFind = true;
                                meterMonthReportFind = meterMonthReportDay;
                                break;
                            }
                        }
                        cell = row.createCell(cellNum);
                        cellNum++;
                        if (bFind) {
                            cell.setCellValue(Double.valueOf(meterMonthReportFind.getEvalue()));
                            if ("fee".equals(type)) {
                                cell.setCellValue(Double.valueOf(meterMonthReportFind.getFee()));
                            }
                        } else {
                            cell.setCellValue(0.00);
                        }

                    }

                    listPos = tmplistPos;
                    //如果是最后一条了，把listPos+1，退出循环
//					if(listPos==list.size()-1)
//						listPos++;
                }
            }

            sum(sheet, rowNum, days + 2);

//			file=new File(fileName);
//			out = new FileOutputStream(file);
            workbook.write(out);
            workbook.close();
//			out.flush();
//			out.close();
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sum(Sheet sheet, int rowNum, int columnNum) {
        if (rowNum == 0) {
            return;
        }
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(columnNum);
        cell.setCellValue("汇总");
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        BigDecimal all1 = new BigDecimal(0);
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
//			BigDecimal bd=new BigDecimal(0);
            BigDecimal all = new BigDecimal(0);
            for (int j = 5; j < columnNum; j++) {
                cell = row.getCell(j);
                int cellType = cell.getCellType();
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String str = cell.getStringCellValue();
                cell.setCellType(cellType);
                cell.setCellValue(Double.valueOf(str));
                all = all.add(new BigDecimal(str));
                all1 = all1.add(new BigDecimal(str));
                ;
//				bd=bd.add(new BigDecimal(str));

                int pos = j - 5;
                if (list.size() > pos) {
                    BigDecimal d = list.get(pos);
                    d = d.add(new BigDecimal(str));
                    list.set(pos, d);
                } else {
                    list.add(new BigDecimal(str));
                }
            }
            cell = row.createCell(columnNum);

//			double number1 = 1; double number2 = 20.2; double number3 = 300.03; double result = number1 + number2 + number3; System.out.println(result);

            cell.setCellValue(Double.valueOf(all.toPlainString()));
        }

        rowNum++;
        row = sheet.createRow(rowNum);
        for (int j = 5; j < columnNum; j++) {
            cell = row.createCell(j);
            cell.setCellValue(Double.valueOf((list.get(j - 5).toPlainString())));
        }
        cell = row.createCell(columnNum);
        cell.setCellValue(Double.valueOf(all1.toPlainString()));
    }


}
