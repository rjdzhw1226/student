package com.student.util;

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
}
