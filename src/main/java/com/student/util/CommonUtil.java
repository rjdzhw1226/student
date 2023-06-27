package com.student.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
}
