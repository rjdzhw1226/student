package com.student.util.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class writeFile {
    private final static String DATE_FORMAT = "yyyy/MM/dd";
    private final static String COLUM_FORMAT = "COLUM";
    //回车加换行符
    static String rt = "\r\n";

    //TODO 文件输出重做
    public static void Compiler(List<Map<String, String>> list, List<Map<String, String>> listName,String name) {
        StringBuilder str = new StringBuilder();
        long i = System.currentTimeMillis();
        //写文件，目录可以自己定义
        String filename = "F:/工作项目/student/studentWork/student/src/main/java/com/student/pojo/"+name+".java";
        for (int j = 0; j < list.get(0).size(); j++) {
            String cellValue = list.get(0).get("COLUM" + (j + 1));
            String nameValue = listName.get(0).get(cellValue);
            str.append("@ExcelImport(\""+cellValue+"\")");
            str.append("\r\n");
            str.append("@Column(name = \""+ nameValue +"\",type = MySqlTypeConstant.VARCHAR,length = 128)");
            str.append("\r\n");
            str.append("private String "+ nameValue +";");
            str.append("\r\n");

        }
        String src =
                "package com.student.pojo;"+ rt +
                        "import com.gitee.sunchenbin.mybatis.actable.annotation.Column;"+ rt +
                        "import com.gitee.sunchenbin.mybatis.actable.annotation.Table;"+ rt +
                        "import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;"+ rt +
                        "import com.student.util.excel.ExcelImport;"  + rt +
                        "import lombok.AllArgsConstructor;"  + rt +
                        "import lombok.NoArgsConstructor;"  + rt +
                        "import lombok.Builder;"  + rt +
                        "import lombok.Data;"  + rt +
                        "import java.util.Date;"  + rt +
                        "@Data" + rt +
                        "@Builder" + rt +
                        "@AllArgsConstructor" + rt +
                        "@NoArgsConstructor" + rt +
                        "@Table(name = \""+name+"\")" + rt +
                        "public class "+ name +"{" + rt +
                        str.toString() + rt +
                        "}";
        File file = new File (filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(src);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void ExcelMap(Workbook workbook,List<String> name){
        //sheet个数
        int numberOfSheets = workbook.getNumberOfSheets();
        if(numberOfSheets == 1){
            String nameOne = name.get(0);
            Result result = getResult(workbook);
            if (result == null) return;
            Compiler(result.list, result.listName,nameOne);
        }
        else{
            //遍历sheet
            for(int sheetNum = 0;sheetNum < numberOfSheets;sheetNum++){
                Result result = getResult(workbook);
                if (result == null) return;
                Compiler(result.list, result.listName,name.get(sheetNum));
            }
        }
    }

    private static Result getResult(Workbook workbook) {
        //创建集合存储
        List<Map<String,String>> list = new ArrayList<>();
        List<Map<String,String>> listName = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        Map<String,String> mapName = new HashMap<>();
        //获得当前sheet工作表
        Sheet sheet = workbook.getSheetAt(0);
        if(sheet == null){
            return null;
        }
        //获得当前sheet的开始行
        int firstRowNum  = sheet.getFirstRowNum();
        int NextCellNum  = firstRowNum + 1;
        //获得当前行
        Row row = sheet.getRow(firstRowNum);
        Row rowNext = sheet.getRow(NextCellNum);
        if(row == null || rowNext == null){
            return null;
        }
        //获得当前行的开始列
        int firstCellNum = row.getFirstCellNum();
        //获得当前行的列数
        int lastCellNum = row.getLastCellNum();
        //循环当前行
        int i = 1;
        for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
            Cell cell = row.getCell(cellNum);
            Cell cellName = rowNext.getCell(cellNum);
            String cellValue = getCellValue(cell);
            String cellValueName = getCellValue(cellName);
            if (!"null".equals(cellValue)){
                map.put(COLUM_FORMAT+i, cellValue);
                mapName.put(cellValue,cellValueName);
                i++;
            }
        }
        list.add(map);
        listName.add(mapName);
        Result result = new Result(list, listName);
        return result;
    }

    private static class Result {
        public final List<Map<String, String>> list;
        public final List<Map<String, String>> listName;

        public Result(List<Map<String, String>> list, List<Map<String, String>> listName) {
            this.list = list;
            this.listName = listName;
        }
    }

    public static String getCellStyle(Cell cell){
        String cellValue = "";
        switch (cell.getCellTypeEnum()) {
            case STRING:
                cellValue = "string";
                break;
            case NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    cellValue = "dateForm";
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    cellValue = "dateForm";
                }else{
                    cellValue = "bigInt";
                }
                break;
            case BOOLEAN:
                cellValue = "boolean";
                break;
            case BLANK:
                cellValue = "";
                break;
            default:
                cellValue = "defaultString";
                break;
        }
        return cellValue;
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
