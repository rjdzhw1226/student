package com.student.util;

public class CommonUtil {
    public static boolean isNotNullJson(String json){
        if(json!=null&&!"".equals(json.trim())&&!"null".equals(json)){
            return true;
        }else{
            return false;
        }
    }
}
