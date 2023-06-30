package com.student.util.file;

import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CreatBean {

    public static <T> List<T> creatBean(Class<T> classzz, Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);

        //获取最后一行
        int lastRowNum = sheet.getLastRowNum()+1;
        //获取最后一列
        int lastCellNum = sheet.getRow(0).getLastCellNum();
        //获取实体类字段
        Field[] fields = classzz.getDeclaredFields();

        Row row = null;

        List<String> headList = new ArrayList<>();

        //一行存为一个Obj,放在List中
        List<T> beans = new ArrayList<T>();

        //获取表头放在list中
        for (int j=0;j<lastCellNum;j++){
            row = sheet.getRow(0);
            Cell cell = row.getCell(j);
            //均格式化为字符串
            DataFormatter formatter = new DataFormatter();
            String value = formatter.formatCellValue(cell);
            headList.add(value);
        }

        try{
            for (int i=1;i<lastRowNum;i++){

                //通过class创建实体对象
                T instance = classzz.newInstance();
                for (int j=0;j<lastCellNum;j++){

                    row = sheet.getRow(i);
                    Cell cell = row.getCell(j);
                    DataFormatter formatter = new DataFormatter();
                    String value = formatter.formatCellValue(cell);

                    String headName = headList.get(j);

                    for (Field field : fields){

                        if (headName.equalsIgnoreCase(field.getName())){

                            String methodName = MethodUtils.setMethodName(field.getName());
                            Method method = classzz.getMethod(methodName,field.getType());
                            //注入值
                            method.invoke(instance,value);
                        }

                    }

                }

                beans.add(instance);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return beans;

    }
    static class MethodUtils {
        private static final String SET_PREFIX = "set";
        private static final String GET_PREFIX = "get";
        private static String capitalize(String name) {
            if (name == null || name.length() == 0) {
                return name;
            }
            //set+首字母大写 比如setSid
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        public static String setMethodName(String propertyName) {
            return SET_PREFIX + capitalize(propertyName);
        }
        public static String getMethodName(String propertyName) {
            return GET_PREFIX + capitalize(propertyName);
        }
    }
}
