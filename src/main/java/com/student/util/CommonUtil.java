package com.student.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
public class CommonUtil {
    public static boolean isNotNullJson(String json){
        if(json!=null&&!"".equals(json.trim())&&!"null".equals(json)){
            return true;
        }else{
            return false;
        }
    }

    public static String ArrayToString(List<String> array){
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            if(i == array.size() - 1){
                result += array.get(i);
            }else {
                result = result + array.get(i) + ",";
            }
        }
        result = "("+result+")";
        return result;
    }

    public static String getString(String s) {
        if (s == null) {
            return "";
        }
        if (s.isEmpty()) {
            return s;
        }
        return s.trim();
    }

    public static String getCellValue(Cell cell) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (cell == null) {
            return "";
        }

        String ret;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                ret = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                ret = null;
                break;
            case Cell.CELL_TYPE_FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = getCellValue(evaluator.evaluateInCell(cell));
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date theDate = cell.getDateCellValue();
                    ret = dfDate.format(theDate);
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                ret = cell.getRichStringCellValue().getString();
                break;
            default:
                ret = null;
        }

        return ret.trim();
    }

        /**
         * 上传文件到服务器
         *
         * @param title
         * @param downloadList
         * @param i
         * @param absolutePath
         * @throws IOException
         */
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
         * 分割csv文件传浏览器(适用xlxs文件)
         *
         * @param response
         * @param absolutePath
         * @throws IOException
         */
        public static void outCsvStreamCSV(HttpServletResponse response, String absolutePath) throws IOException {
            java.io.OutputStream out = response.getOutputStream();
            byte[] b = new byte[10240];
            java.io.File fileLoad = new java.io.File(absolutePath);
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

    }
