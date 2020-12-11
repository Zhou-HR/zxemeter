package com.gd.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导出excel 工具类
 *
 * @author ShiHui.Zhang
 * Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class ExportDataTools {
    private static int len = 2048;

    /**
     * 设置工作表title
     *
     * @param sheet
     * @param headers
     * @Author ShiHui.Zhang
     * @createDate 2014年11月25日 下午3:24:28
     */
    public static void setSheetTitle(Sheet sheet, String[] headers, Row row, CellStyle style) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            if (style != null) {
                cell.setCellStyle(style);
            }
            sheet.setColumnWidth(i, headers[i].getBytes().length * 2 * 256 < len ? len : headers[i].getBytes().length * 2 * 256);
        }
    }

    /**
     * 设置工作表数据
     *
     * @param sheet
     * @param dataset
     * @param row
     * @param headers
     * @param style   样式
     * @Author ShiHui.Zhang
     * @createDate 2014年11月25日 下午3:31:06
     */
    public void setSheetData(HSSFSheet sheet, List<Map> dataset, HSSFRow row, String[] headers, HSSFCellStyle style) {
        // 遍历集合数据，产生数据行
        int index = 0;
        for (Map map : dataset) {
            index++;
            row = sheet.createRow(index);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(map.get(headers[i]) == null ? "" : map.get(headers[i]) + "");
                if (style != null) {
                    cell.setCellStyle(style);
                }
                sheet.setColumnWidth(i, headers[i].getBytes().length * 2 * 256 < len ? len : headers[i].getBytes().length * 2 * 256);
            }
        }
    }

    /**
     * 导出excel 多个sheet
     *
     * @param titles  sheet Name数组 String[]
     * @param headers 表格头数组  集合 List<String[]>
     * @param dataset 表格数据   集合 List<List<Map>>
     * @param out
     * @Author ShiHui.Zhang
     * @createDate 2014年11月26日 上午10:52:12
     */
    public void exportExcel(String[] titles, List<String[]> headers, List<List<Map>> dataset, OutputStream out, HSSFCellStyle headerStyle, HSSFCellStyle dataStyle) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        for (int i = 0; i < titles.length; i++) {
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(titles[i]);
            // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            setSheetTitle(sheet, headers.get(i), row, headerStyle);
            setSheetData(sheet, dataset.get(i), row, headers.get(i), dataStyle);
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出excel 单个sheet
     *
     * @param title       sheet标题
     * @param headers     表格头
     * @param dataset     表格数据
     * @param headerStyle 表格头样式
     * @param dataStyle   表数据样式
     * @param out
     * @Author ShiHui.Zhang
     * @createDate 2014年11月26日 上午10:52:40
     */
    public void exportExcel(String title, String[] headers, List<Map> dataset, OutputStream out, HSSFCellStyle headerStyle, HSSFCellStyle dataStyle) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        setSheetTitle(sheet, headers, row, headerStyle);
        setSheetData(sheet, dataset, row, headers, dataStyle);
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String fileName) {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/vnd.ms-excel");
//	    	response.setContentType("application/x-excel");  
        response.setCharacterEncoding("UTF-8");
        try {
            fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
//			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");  
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(fileName + "导出失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }

    }

    /**
     * 给指定样式添加指定的背景颜色
     *
     * @param style
     * @param index
     * @return
     */
    public static HSSFCellStyle setBackgroundColor(HSSFCellStyle style, short index) {
        style.setFillForegroundColor(index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 给指定样式添加指定的背景颜色
     *
     * @param style
     * @param index
     * @return
     */
    public static CellStyle setBackgroundColor(CellStyle style, short index) {
        style.setFillForegroundColor(index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置自定义背景颜色
     *
     * @param workbook
     * @param index    自定义颜色索引
     * @param b        颜色bgk色值中的b值
     * @param g        颜色bgk色值中的g值
     * @param k        颜色bgk色值中的k值
     * @return
     */
    public static XSSFCellStyle setBackgroundColor(XSSFWorkbook workbook, short index, byte b, byte g, byte k, Boolean fontIsWhite, Boolean fontIsBold) {
        XSSFCellStyle style = workbook.createCellStyle();
//        HSSFPalette palette = workbook.getCustomPalette(); 
//		palette.setColorAtIndex(index, b, g, k);
//		style = setBackgroundColor(style, index);

        XSSFColor color = new XSSFColor(new byte[]{b, g, k});
        style.setFillForegroundColor(color);//setFillForegroundColor(color);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        XSSFFont font = null;
        if (fontIsWhite) {
            font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex()); //font.setColor(new XSSFColor(new byte[]{0,0,0}));
        }
        if (fontIsBold) {
            if (font == null) {
                font = workbook.createFont();
            }
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        if (font != null) {
            style.setFont(font);
        }
        return style;
    }

    public static XSSFCellStyle setBackgroundColor(XSSFWorkbook workbook, XSSFCellStyle style, byte b, byte g, byte k, Boolean fontIsWhite, Boolean fontIsBold) {
        XSSFColor color = new XSSFColor(new byte[]{b, g, k});
        style.setFillForegroundColor(color);//setFillForegroundColor(color);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        XSSFFont font = null;
        if (fontIsWhite) {
            font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex()); //font.setColor(new XSSFColor(new byte[]{0,0,0}));
        }
        if (fontIsBold) {
            if (font == null) {
                font = workbook.createFont();
            }
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        if (font != null) {
            style.setFont(font);
        }
        return style;
    }

    public static CellStyle setBackgroundColor(Workbook workbook, short index) {
        CellStyle style = workbook.createCellStyle();
        style = setBackgroundColor(style, index);
        return style;
    }

    /**
     * 设置样式水平和垂直方向居中
     *
     * @param style
     * @return
     */
    public static XSSFCellStyle setAlignment(XSSFCellStyle style) {
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        return style;
    }

    /**
     * 返回加粗的字体
     *
     * @param workbook
     * @return
     */
    public static Font getBoldFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);         //字体增粗
        return font;
    }

    public static Map ConvertObjToMap(Object obj) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(
                            fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    reMap.put(fields[i].getName(), o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

}
