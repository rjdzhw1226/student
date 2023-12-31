package com.student.util;

import com.student.pojo.student;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author rjd
 * 想设计一个能够解析一个bean的全部属性并按照顺序进行遍历
 */
public class ObjectUtils {
    public static void main(String[] args) {
        arrange(new Integer[]{1,2,3},new Stack<>());
    }

    /**
     * 全排列 栈实现
     * @param array
     * @param stack
     */
    public static void arrange(Integer[] array, Stack<Integer> stack){
        if(array.length == 0){
            System.out.println(stack);
        }else{
            for (int i = 0; i < array.length; i++) {
                Integer[] temp = new Integer[array.length - 1];
                System.arraycopy(array,0,temp,0,i);
                System.arraycopy(array,i+1,temp,i,array.length-i-1);
                stack.push(array[i]);
                arrange(temp,stack);
                stack.pop();
            }
        }
    }
    /**
     * 按照属性的顺序赋值。可接受null，但是不能跳过某个属性进行赋值。就是说就算
     * 有一个值为空，那你也要传递进行null
     *
     * @param target
     * @param value
     * @param <E>
     * @return
     */
    public static <E> E forEachSetValue(E target, Object value) {
        if (target == null) {
            return target;
        }
        List<Field> fields = new ArrayList<>();
        try {
            // 1.解析出所有的属性
            Field[] declaredFields = target.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                fields.add(declaredField);
            }
            // 2.把每个属性放入一个集合中
            if (fields.size() <= 0) {
                return target;
            }
            while (fields.get(0).get(target) != null) {
                fields.remove(0);
            }
            Field field = fields.get(0);
            field.set(target, value);
            fields.remove(0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return target;
    }

    /**
     * 本方法为了遍历索引进行赋值
     *
     * @param e
     * @param index
     * @return
     */
    public static Object forEachGetValue(Object e, int index) {
        try {
            Field[] declaredFields = e.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
            }
            return declaredFields[index].get(e);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        return e;
    }

    public static int size(Object o) {
        if (o == null) {
            return 0;
        }
        Field[] declaredFields = o.getClass().getDeclaredFields();
        return declaredFields.length;
    }

    /**
     * 本方法是为了把已经有值得对象中属性名相同的名属性赋值到没有值得对象用。
     *
     * @param target
     * @param value
     */
    public static <E> E copyValueFromObject(E target, Object value) {
        if (target == null || value == null) {
            return null;
        }
        Field[] vs = target.getClass().getDeclaredFields();
        Field[] ts = value.getClass().getDeclaredFields();

        try {
            for (int i = 0; i < vs.length; i++) {
                for (int j = 0; j < ts.length; j++) {
                    if (vs[i].getName().equals(ts[j])) {
                        ts[j].set(target, vs[i].get(value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 这个方法能把list中的值按照顺序设置到目标对象中
     *
     * @param target
     * @param value
     * @param <E>
     * @return
     */
    public static <E> E forEachSetValueFromList(E target, List value) {

        if (target == null || value == null || value.size() == 0) {
            return target;
        }
        Field[] ts = target.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < ts.length; i++) {
                ts[i].set(target, value.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 从数组中进行设置值
     *
     * @param target
     * @param value
     * @param <E>
     * @return
     */
    public static <E> E forEachSetValueFromArray(E target, Object[] value) {

        if (target == null || value == null || value.length == 0) {
            return target;
        }
        Field[] ts = target.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < ts.length; i++) {
                ts[i].set(target, value[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }


    public static Object[] getArrayValue(Object o) {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        Object[] result = new Object[declaredFields.length];
        try {
            for (int i = 0; i < declaredFields.length; i++) {
                result[i] = declaredFields[i].get(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List getListValue(Object o) {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        List result = new ArrayList(declaredFields.length);
        try {
            for (int i = 0; i < declaredFields.length; i++) {
                result.add(declaredFields[i].get(o));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String twoJson(List<String> key, List<String> value){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        //{"1":"aaa","2":"bbb","3":"ccc"}
        //{"1":"aaa","2":"bbb","3":"ccc","4":"ddd"}
        for (int j = 0; j < key.size(); j++) {
            if (j == key.size() - 1) {
                stringBuilder.append("\"").append(key.get(j)).append("\"").append(":").append("\"").append(value.get(j)).append("\"");
            } else {
                stringBuilder.append("\"").append(key.get(j)).append("\"").append(":").append("\"").append(value.get(j)).append("\"").append(",");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
    public static String twoString(List<String> value){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : value) {
            stringBuilder.append(s).append(",");
        }
        return stringBuilder.toString().substring(stringBuilder.lastIndexOf(","));
    }
}

