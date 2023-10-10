package com.student.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
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
    public static String run(String command) throws IOException {
        Scanner input = null;
        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result += input.nextLine() + "\n";
            }
            result = command + "\n" + result; //加上命令本身，打印出来
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    public static BufferedImage InputImage(MultipartFile file) {
        BufferedImage srcImage = null;
        try {
            FileInputStream in = (FileInputStream) file.getInputStream();
            srcImage = javax.imageio.ImageIO.read(in);
            srcImage = gray(srcImage);
            srcImage = binaryImage(srcImage);
        } catch (Exception e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
        }
        return srcImage;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
    public static BufferedImage gray(BufferedImage bufferedImage) {
        BufferedImage grayImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {

                final int color = bufferedImage.getRGB(i, j);
                final int r = (color >> 16) & 0xff;     //右移四位
                final int g = (color >> 8) & 0xff;      //右移3为
                final int b = color & 0xff;
                //运用灰度处理 的方法  加权平均值
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                //每一个像素点的灰度转化
                int newPixel = colorToRGB(255, gray, gray, gray);
                grayImage.setRGB(i, j, newPixel);


            }
        }
        return grayImage;
    }


    public static BufferedImage binaryImage(BufferedImage image) throws Exception {
        int w = image.getWidth();
        int h = image.getHeight();
        float[] rgb = new float[3];
        double[][] zuobiao = new double[w][h];
        int black = new java.awt.Color(0, 0, 0).getRGB();
        int white = new Color(255, 255, 255).getRGB();
        BufferedImage bi= new BufferedImage(w, h,
                BufferedImage.TYPE_BYTE_BINARY);;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0]+rgb[1]+rgb[2])/3;
                zuobiao[x][y] = avg;

            }
        }
        //这里是阈值，白底黑字还是黑底白字，大多数情况下建议白底黑字，后面都以白底黑字为例
        double SW = 192;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (zuobiao[x][y] < SW) {
                    bi.setRGB(x, y, black);
                }else{
                    bi.setRGB(x, y, white);
                }
            }
        }
        return bi;
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
