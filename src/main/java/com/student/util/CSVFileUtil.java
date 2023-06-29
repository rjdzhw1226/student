package com.student.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * csv导出工具类
 */
@Slf4j
public class CSVFileUtil {
    /**
     * 读取
     *
     * @param file      csv文件(路径+文件)
     * @param delimiter 分割符
     * @return
     */
    public static List<String[]> importCsv(File file, String delimiter, String charsetName) {
        List<String[]> dataList = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), charsetName);
            br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line.split(delimiter));
            }
        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

    /**
     * 写入
     * csv文件(路径+文件名)，csv文件不存在会自动创建
     *
     * @param exportData 数据
     * @return
     */
    public static File exportCsv(List<List<String>> exportData, String outPutPath, String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    log.info("创建成功");
                } else {
                    log.error("创建失败");
                }
            }
            //定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, true), StandardCharsets.UTF_8), 1024);
            for (List<String> exportDatum : exportData) {
                writeRow(exportDatum, csvFileOutputStream);
                csvFileOutputStream.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvFileOutputStream != null) {
                    csvFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 写一行数据
     *
     * @param row       数据列表
     * @param csvWriter
     * @throws IOException
     */
    public static void writeRow(List<String> row, BufferedWriter csvWriter) throws IOException {
        int i = 0;
        for (String data : row) {
            csvWriter.write(data);
            if (i != row.size() - 1) {
                csvWriter.write(",");
            }
            i++;
        }
    }

    /**
     * 剔除特殊字符
     *
     * @param str 数据
     */
    public static String DelQuota(String str) {
        String result = str;
        String[] strQuota = {"~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "`", ";", "'", ",", ".", "/", ":", "/,", "<", ">", "?"};
        for (String s : strQuota) {
            if (result.contains(s)) {
                result = result.replace(s, "");
            }
        }
        return result;
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        exportCsv();
        //importCsv();
    }

    /**
     * CSV读取测试
     *
     * @throws Exception
     */
    public static void importCsv() {
        List<String[]> dataList = CSVFileUtil.importCsv(new File("F:/test_two.csv"), ",", "GB2312");
        if (!dataList.isEmpty()) {
            for (String[] cells : dataList) {
                if (cells != null && cells.length > 0) {
                    for (String cell : cells) {
                        System.out.print(cell + "  ");
                    }
                    System.out.println();
                }
            }
        }
    }

    /**
     * CSV写入测试
     *
     * @throws Exception
     */
    public static void exportCsv() {
        List<List<String>> listList = new ArrayList<>();

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        list1.add("编号");
        list1.add("姓名");
        list1.add("身高");
        list1.add("电话");

        list2.add("1");
        list2.add("小明");
        list2.add("180cm");
        list2.add("1111111");

        list3.add("2");
        list3.add("小红");
        list3.add("176cm");
        list3.add("1111111");

        listList.add(list1);
        listList.add(list2);
        listList.add(list3);

        CSVFileUtil.exportCsv(listList, "D://", "testFile");
    }
}
