package com.student.util.excel;

import cn.hutool.core.date.DateTime;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.Date;

public class ReadManyInfo {


    public void switchDataType(FileInputStream filterInputStream) throws Exception {

        //把我们的流放在工作簿用 用于读取excel数据
        Workbook workbook = new HSSFWorkbook(filterInputStream);
        //2 获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //3获取行(表头)
        Row rowTitle = sheet.getRow(0); //其实这就是我们的表头 最上面的部分
        //判断行不为空才读
        if (rowTitle != null) {
            //如果行不为空才去读列的信息
            //getPhysicalNumberOfCells()获取全部的列并且返回行数
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            System.out.println("cellCount = " + cellCount);
            for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                //得到每一行的数据
                Cell cell = rowTitle.getCell(cellNum);
                //判断每一行是否为空 不为空再做处理
                if (cell != null) {
                    //获取全部行的数据类型
                    int cellType = cell.getCellType();
                    //获取行的值
                    String stringCellValue = cell.getStringCellValue();
                    //进行输出 这里就不换行了 直接一行显示用竖线分割
                    System.out.print(stringCellValue + "|");
                }
            }
            //打印完一行换行打印另外一行
            System.out.println();
        }
        //获取表中的内容
        int rowCount = sheet.getPhysicalNumberOfRows();
        //循环获取数据
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {
            Row row = sheet.getRow(rowNum);
            //不为空再做处理
            if (row != null) {
                //读取行中的列    getPhysicalNumberOfCells获取全部的列
                int cellCount = rowTitle.getPhysicalNumberOfCells();
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    System.out.print("[" + (rowNum + 1) + "-" + (cellNum + 1) + "]");
                    //获取数据
                    Cell cell = row.getCell(cellNum);
                    //因为不知道列的数据类型 所以这里我们要匹配数据类型
                    //如果不为空
                    if (cell != null) {
                        //获取类型
                        int cellType = cell.getCellType();
                        String cellValue = "";
                        //判断cell的数据类型
                        switch (cellType) {
                            case HSSFCell.CELL_TYPE_STRING: //字符串
                                System.out.print("STRING");
                                cellValue = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN: //布尔
                                System.out.print("BOOLEAN");
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_BLANK: //空
                                System.out.print("BLANK");
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                System.out.print("NUMERIC");
                                //cellValue = String.valueOf(cell.getNumericCellValue());
                                if (HSSFDateUtil.isCellDateFormatted(cell)) { //日期
                                    System.out.print("日期");
                                    Date date = cell.getDateCellValue();
                                    cellValue = new DateTime(date).toString("yyyy-MM-dd");
                                } else {
                                    // 不是日期格式，则防止当数字过长时以科学计数法显示
                                    System.out.print("转换成字符串");
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    cellValue = cell.toString();
                                }
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                System.out.print("数据类型错误");
                                break;

                        }
                        System.out.println(cellValue);
                    }
                }
            }
        }
        //关闭流
        filterInputStream.close();


    }

}

