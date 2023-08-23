package com.student.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static BufferedImage InputImage(MultipartFile file) {
        BufferedImage srcImage = null;
        try {
            FileInputStream in = (FileInputStream) file.getInputStream();
            srcImage = javax.imageio.ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
        }
        return srcImage;
    }

        /**
         * 分割csv文件传浏览器(适用xlxs文件)
         *
         * @param response
         * @param absolutePath
         * @throws IOException
         */
        public static void outCsvStreamCSV(HttpServletResponse response, String absolutePath) throws IOException {
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[10240];
            File fileLoad = new File(absolutePath);
            response.reset();
            response.setContentType("application/csv");
            response.setHeader("content-disposition", "attachment; filename=" + URLEncoder.encode("export.csv", "UTF-8"));
            FileInputStream in = new FileInputStream(fileLoad);
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
     * 复制单个Bean属性
     * @param source
     * @param clazz
     * @return
     */
    public static <V,T> T copyBean(V source,Class<T> clazz) {
        T vo = null;
        try {
            //获取目标对象
            vo = clazz.newInstance();
            //复制属性
            BeanUtils.copyProperties(source, vo);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 拷贝List型的Bean集合
     * @param list
     * @param clazz
     * @param <V>
     * @param <T>
     * @return
     */
    public static <V,T> List<T> copyBeanList(List<V> list,Class<T> clazz) {
        return list.stream()
                .map(o ->
                        copyBean(o,clazz)
                ).collect(Collectors.toList());
    }

    /**
     * 用于删除图片
     */
    public static void deleteImg(String basePath,String name) {
        System.out.println(basePath + name);
        //delFile(basePath + name);
        File file = new File(basePath + name);
        file.delete();
    }

    /**
     * 删除文件
     * @param filePathAndName 指定得路径
     */
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 生成随机数字（ASCII码范围：48-57）或者随机大写字母（ASCII码范围：65-90）
            boolean isDigit = random.nextBoolean();
            if (isDigit) {
                sb.append((char)(48 + random.nextInt(10)));
            } else {
                sb.append((char)(65 + random.nextInt(26)));
            }
        }

        return sb.toString();
    }

    }
