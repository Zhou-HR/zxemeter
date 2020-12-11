package com.gd.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZhouHR
 */
public class POIUtils {

    public static void creatNewExcel(Sheet sheet, Workbook wb)
            throws IOException {

        // 新的excel 文件名
        String excelName = "新的excel 文件名";

        // 创建新的excel
//		HSSFWorkbook wbCreat = new HSSFWorkbook();
        Sheet sheetCreat = wb.createSheet("new sheet");

        // 复制源表中的合并单元格
        //MergerRegion(sheetCreat, sheet);

        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        for (int i = firstRow; i <= lastRow; i++) {

            // 创建新建excel Sheet的行
            Row rowCreat = sheetCreat.createRow(i);

            // 取得源有excel Sheet的行
            Row row = sheet.getRow(i);

            // 单元格式样
            CellStyle cellStyle = null;

            int firstCell = row.getFirstCellNum();
            int lastCell = row.getLastCellNum();
            for (int j = firstCell; j < lastCell; j++) {

                // 自动适应列宽 貌似不起作用
                sheetCreat.autoSizeColumn(j);

                // new一个式样
                cellStyle = wb.createCellStyle();

                // 设置边框线型
                cellStyle.setBorderTop(row.getCell(j).getCellStyle()
                        .getBorderTop());
                cellStyle.setBorderBottom(row.getCell(j).getCellStyle()
                        .getBorderBottom());
                cellStyle.setBorderLeft(row.getCell(j).getCellStyle()
                        .getBorderLeft());
                cellStyle.setBorderRight(row.getCell(j).getCellStyle()
                        .getBorderRight());

                // 设置内容位置:例水平居中,居右，居工
                cellStyle.setAlignment(row.getCell(j).getCellStyle()
                        .getAlignment());
                // 设置内容位置:例垂直居中,居上，居下
                cellStyle.setVerticalAlignment(row.getCell(j).getCellStyle()
                        .getVerticalAlignment());

                // 自动换行
                cellStyle.setWrapText(row.getCell(j).getCellStyle()
                        .getWrapText());
                rowCreat.createCell(j).setCellStyle(cellStyle);

                // 设置单元格高度
                rowCreat.getCell(j).getRow()
                        .setHeight(row.getCell(j).getRow().getHeight());

                // 单元格类型
                switch (row.getCell(j).getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        String strVal = removeInternalBlank(row.getCell(j)
                                .getStringCellValue());

                        rowCreat.getCell(j).setCellValue(strVal);
                        break;

                    case HSSFCell.CELL_TYPE_NUMERIC:
                        rowCreat.getCell(j).setCellValue(
                                row.getCell(j).getNumericCellValue());
                        break;

                    case HSSFCell.CELL_TYPE_FORMULA:
                        try {
                            rowCreat.getCell(j).setCellValue(
                                    String.valueOf(row.getCell(j)
                                            .getNumericCellValue()));
                        } catch (IllegalStateException e) {
                            try {
                                rowCreat.getCell(j).setCellValue(
                                        String.valueOf(row.getCell(j)
                                                .getRichStringCellValue()));
                            } catch (Exception ex) {
                                rowCreat.getCell(j).setCellValue("公式出错");
                            }
                        }
                        break;
                }
            }
        }
//		String strPath = "D:\\excelTo\\";// 保存新EXCEL路径
//
//		// 检查同名
//		excelName = checkFileName(strPath, excelName);
//
//		FileOutputStream fileOut = new FileOutputStream(strPath + excelName
//				+ ".xls");
//		wbCreat.write(fileOut);
//		fileOut.close();

    }

    /**
     * 检查此文件夹下有无同名，若有返回新文件名“文件名_重名”
     *
     * @param strPath       "D:\\excelTo\\"
     * @param checkFilename 文件名
     * @return 文件名
     */
    private static String checkFileName(String strPath, String checkFilename) {
        File file = new File(strPath);
        for (File f : file.listFiles()) {
            if (f.getName().equals(checkFilename)) {
                checkFilename += checkFilename + "_重名";
                checkFileName(strPath, checkFilename);
                break;
            }
        }
        return checkFilename;
    }

    /**
     * 复制原有sheet的合并单元格到新创建的sheet
     *
     * @param sheetCreat 新创建sheet
     * @param sheet      原有的sheet
     */
    private static void MergerRegion(Sheet sheetCreat, Sheet sheet) {

//		int sheetMergerCount = sheet.getNumMergedRegions();
//		for (int i = 0; i < sheetMergerCount; i++) {
//			Region mergedRegionAt = sheet.getMergedRegionAt(i);
//			sheetCreat.addMergedRegion(mergedRegionAt);
//		}

    }

    /**
     * 判断单元格在不在合并单元格范围内
     *
     * @param sheet
     * @param intCellRow 被判断的单元格的行号
     * @param intCellCol 被判断的单元格的列号
     * @return TRUE 表示在，反之不在
     * @throws IOException
     */
    private static boolean isInMergerCellRegion(HSSFSheet sheet,
                                                int intCellRow, int intCellCol) throws IOException {
        boolean retVal = false;

        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress cra = (CellRangeAddress) sheet.getMergedRegion(i);
            // 合并单元格CELL起始行
            int firstRow = cra.getFirstRow();
            // 合并单元格CELL起始列
            int firstCol = cra.getFirstColumn();

            // 合并单元格CELL结束行
            int lastRow = cra.getFirstColumn();
            // 合并单元格CELL结束列
            int lastCol = cra.getLastColumn();

            if (intCellRow >= firstRow && intCellRow <= lastRow) {
                if (intCellCol >= firstCol && intCellCol <= lastCol) {
                    retVal = true;
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 去除字符串内部空格
     */
    public static String removeInternalBlank(String s) {
        // System.out.println("bb:" + s);
        Pattern p = Pattern.compile("");
        Matcher m = p.matcher(s);
        char str[] = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                sb.append(' ');
            } else {
                break;
            }
        }
        String after = m.replaceAll("");
        return sb.toString() + after;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
