package com.student.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class PoiUtil {
    public static void main(String[] args) throws IOException {
        String[] strArray = new String[]{"学号","姓名","年级","班级","手机号","年龄","性别","状态","头像"};
        List<String> listSort = new LinkedList<>(Arrays.asList("1234","renjiadong","一年级","一班","123243432","18","男","1","img.jpg"));
        List<List<String>> list = new ArrayList<>(Arrays.asList(listSort));
        PoiUtil.exportCSVFile(strArray,list,0,"D:\\DOWNLOAD\\student3.csv");
    }

    public static void exportCSVFile(String[] title, List<List<String>> downloadList, int i, String absolutePath) throws IOException {
        //true 拼接导出文件
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath, true));
        log.info("创建文件地址: " + absolutePath);
        //如果是第一次循环 添加表头
        if (i == 0) {
            PoiUtil.writeHead(title, bufferedWriter);
            //另起一行
            bufferedWriter.newLine();
        }
        //循环list中数据 逐个添加
        for (List<String> list : downloadList) {
            CSVFileUtil.writeRow(list, bufferedWriter);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /**
     * 创建临时的csv文件
     *
     * @return
     * @throws IOException
     */
    public static File createTempFile(List<String[]> datas, String[] headers) throws IOException {
        File tempFile = File.createTempFile("vehicle", ".csv");
        CsvWriter csvWriter = new CsvWriter(tempFile.getCanonicalPath(), ',', StandardCharsets.UTF_8);
        // 写表头
        csvWriter.writeRecord(headers);
        for (String[] data : datas) {
            //这里如果数据不是String类型，请进行转换
            for (String datum : data) {
                csvWriter.write(datum, true);
            }
            csvWriter.endRecord();
        }
        csvWriter.close();
        return tempFile;
    }
    /**
     * csv文件表头写入
     *
     * @param title
     * @param bufferedWriter
     * @throws IOException
     */
    public static void writeHead(String[] title, BufferedWriter bufferedWriter) throws IOException {
        // 写表头
        int i = 0;
        for (String data : title) {
            bufferedWriter.write(data);
            if (i != title.length - 1) {
                bufferedWriter.write(",");
            }
            i++;
        }
    }


    /**
     * 普通csv文件传浏览器
     *
     * @param response
     * @param tempFile
     * @throws IOException
     */
    public static void outCsvStream(HttpServletResponse response, File tempFile) throws IOException {
        java.io.OutputStream out = response.getOutputStream();
        byte[] b = new byte[10240];
        java.io.File fileLoad = new java.io.File(tempFile.getCanonicalPath());
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("content-disposition", "attachment; filename=" + URLEncoder.encode("export.csv", "UTF-8"));
        java.io.FileInputStream in = new java.io.FileInputStream(fileLoad);
        int n;
        //为了保证excel打开csv不出现中文乱码
        out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        while ((n = in.read(b)) != -1) {
            //每次写入out1024字节
            out.write(b, 0, n);
        }
        in.close();
        out.close();
    }


    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 低占用内存xlsx文件导出
     *
     * @param title
     * @param rows
     * @return
     */
    public static SXSSFWorkbook exportExcelSXSSF(String[] title, List<String[]> rows) {
        //这样表示SXSSFWorkbook只会保留100条数据在内存中，其它的数据都会写到磁盘里，这样的话占用的内存就会很少
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        // 生成一个表格
        SXSSFSheet sheet = workbook.createSheet();
        //设置表头白字黑底居中
        Font font = workbook.createFont();
        //设置字体颜色
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontName("宋体");
        //设置表头边框
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.HAIR);
        headerStyle.setBorderLeft(BorderStyle.HAIR);
        headerStyle.setBorderRight(BorderStyle.HAIR);
        headerStyle.setBorderTop(BorderStyle.HAIR);
        // 创建一个居中格式
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //五十度灰
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(font);
        Font contentFont = workbook.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 10);
        //设置表内容边框
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.HAIR);
        bodyStyle.setBorderLeft(BorderStyle.HAIR);
        bodyStyle.setBorderRight(BorderStyle.HAIR);
        bodyStyle.setBorderTop(BorderStyle.HAIR);
        // 创建一个居中格式
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bodyStyle.setFont(contentFont);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);
        sheet.setColumnWidth(title.length - 1, (int) ((40 + 0.72) * 256));
        // 循环字段名数组，创建标题行
        SXSSFRow row = sheet.createRow(0);
        for (int j = 0; j < title.length; j++) {
            // 创建列
            SXSSFCell cell = row.createCell(j);
            // 设置单元类型为String
            cell.setCellType(CellType.STRING);
            cell.setCellValue(title[j]);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < rows.size(); i++) {
            // 因为第一行已经用于创建标题行，故从第二行开始创建
            row = sheet.createRow(i + 1);
            // 如果是第一行就让其为标题行
            String[] rowData = rows.get(i);
            //每一行的数据
            recyclingCellSXSSF(rowData, row, bodyStyle);
        }
        return workbook;
    }

    private static void recyclingCellSXSSF(String[] rowData, SXSSFRow row, CellStyle bodyStyle) {
        for (int j = 0; j < rowData.length; j++) {
            // 创建列
            SXSSFCell cell = row.createCell(j);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(rowData[j]);
            cell.setCellStyle(bodyStyle);
        }
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 设置文件响应头
     * @param filename
     * @param
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void setDownLoadHeader(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fname= URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition","attachment; filename="+fname);
    }

}
