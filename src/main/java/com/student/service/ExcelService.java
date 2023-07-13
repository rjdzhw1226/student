package com.student.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import com.student.pojo.student;
import com.student.pojo.studentTest;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectLabelVo;
import com.student.pojo.vo.subjectVo;
import com.student.util.CallQueryThread;
import com.student.util.CommonUtil;
import com.student.util.ObjectUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExcelService {

    final static int SIZE = 10;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    public static void main(String[] args) {
        Class<student> studentClass = student.class;
        Field[] declaredFields = studentClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            declaredFields[i].setAccessible(true);//设置访问权限
            try {
                declaredFields[i].set(declaredFields[i].getName(),""+i+"");//赋值
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void ExcelUpDoneAuto(Sheet sheet, int sta, int size, Class aClass){
        try{
            //这里泛型list
//            List<T> list = new ArrayList<>();
            //开始行读取
            int rowStart = sheet.getFirstRowNum();
//            List<String> name = new LinkedList<>();
//            Row rowI = sheet.getRow(rowStart+1);
//            //获得当前行的开始列
//            int firstCellNumI = rowI.getFirstCellNum();
//            //获得当前行的列数
//            int lastCellNumI = rowI.getLastCellNum();
//            for (int j = firstCellNumI; j < lastCellNumI; j++) {
//                name.add(CommonUtil.getCellValue(rowI.getCell(j)));
//            }
            if(sta == rowStart){
                sta = sta + 2;
            }
            for (int i = sta; i < size; i++) {
                Row row = sheet.getRow(i);
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getLastCellNum();
                List<String> listSave = new LinkedList<>();
                for (int j = firstCellNum; j < lastCellNum; j++) {
                    String cellValue = CommonUtil.getCellValue(row.getCell(j));
                    listSave.add(cellValue);
                }
                String str = ObjectUtils.twoString(listSave);
                FileOutputStream fos = new FileOutputStream("D:/log.txt",true);
                byte bytes[]=new byte[512];
                bytes=str.getBytes();
                fos.write(bytes);
                fos.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void ExcelUpDone(Sheet sheet, int sta, int size) {
        try{
            List<student> students = new ArrayList<>();
            int rowStart = sheet.getFirstRowNum();
            if(sta == rowStart){
                sta = sta + 1;
            }
            for (int y = sta; y < size; y++) {
                Row row = sheet.getRow(y);
                String id = CommonUtil.getCellValue(row.getCell(0));
                String name = CommonUtil.getCellValue(row.getCell(1));
                String grade = CommonUtil.getCellValue(row.getCell(2));
                String grade_class = CommonUtil.getCellValue(row.getCell(3));
                String phone = CommonUtil.getCellValue(row.getCell(4));
                String age = CommonUtil.getCellValue(row.getCell(5));
                String gender = CommonUtil.getCellValue(row.getCell(6));
                String station = CommonUtil.getCellValue(row.getCell(7));
                String url = CommonUtil.getCellValue(row.getCell(8));

                student st = new student();
                st.setId(id);
                st.setName(name);
                st.setGrade(grade);
                st.setGrade_class(grade_class);
                st.setPhone(phone);
                st.setAge(age);
                st.setGender(gender);
                st.setStation(station);
                st.setUrl(url);

                students.add(st);
            }
            studentMapper.insertArticleTag(students);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List queryData(int start, int end){
        Map<String, Object> params = new HashMap<>();
        params.put("page", start);
        params.put("size", end);
        List<subject> subjectLabelVos = subjectMapper.queryAllLabel(params);
        return subjectLabelVos;
    }

    /**
     * 获取多线程结果并进行结果合并
     * @return
     */
    public List<subject> getMultiCombineResult(int count) {
        //开始时间
        long start = System.currentTimeMillis();
        //返回结果
        List<subject> result = new ArrayList<>();
        //传入总量
        Map<String,String> splitMap = getSplitMap(count,SIZE);
        int bindex = 1;
        //Callable用于产生结果
        List<Callable<List>> tasks = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            //不同的线程处理不同分段的数据量，平均分摊查询数据的压力
            String[] nums = splitMap.get(String.valueOf(i * (count / SIZE))).split(":");
            int startNum = Integer.valueOf(String.valueOf(i * (count / SIZE)));
            int endNum = Integer.valueOf(nums[0]);
            Callable<List> qfe = new CallQueryThread(startNum, endNum);
            tasks.add(qfe);
            bindex += bindex;
        }
        try{
            //定义固定长度的线程池  防止线程过多，5
            ExecutorService executorService = Executors.newFixedThreadPool(SIZE);
            //Future用于获取结果
            List<Future<List>> futures=executorService.invokeAll(tasks);
            //处理线程返回结果
            if(futures!=null&&futures.size() > 0){
                for (Future<List> future:futures){
                    result.addAll(future.get());
                }
            }
            //关闭线程池 一定不能忘记 非自定义配置类一定要关闭
            executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("线程查询数据用时:"+(end-start)+"ms");
        return result;
    }

    private Map<String, String> getSplitMap(int count, int i) {
        Map<String,String> map = new HashMap<>();
        int mode = count % i;
        int index = count / i;
        for (int j = 0; j < i; j++) {
            if(j == i - 1){
                map.put(""+j * index+"",""+(j * index + index + mode)+"");
            }else{
                map.put(""+j * index+"",""+(j * index + index)+"");
            }
        }
        return map;
    }

    public static <T> List<T> toListClass(String strName ,Map<String , Object> params ,Class<T> clazz){
        String returnValue = "";
        List<T> list01 = new ArrayList<>();

        if (!"".equals(returnValue)) {
            cn.hutool.json.JSONObject resJson = JSONUtil.parseObj(returnValue);
            if("0".equals(resJson.getJSONObject("__sys__").getStr("status"))){ //查询成功
                cn.hutool.json.JSONObject dataJson = resJson.getJSONObject("data"); //数据
                if(dataJson.get("data")!=null && dataJson.get("data").toString().startsWith("[")){
                    JSONArray arrays = dataJson.getJSONArray("data");
                    if(dataJson.containsKey("success")){
                        String successJson = dataJson.getStr("success");
                        if("true".equals(successJson)){
                            list01 = JSON.parseArray(arrays.toString(), clazz);
                        }else{
                            list01 = JSON.parseArray(arrays.toString(), clazz);
                            //list01 = JSON.parseArray(new JSONArray().toString(), clazz);
                        }
                    }else{
                        //没有success这个字段
                        list01 = JSON.parseArray(arrays.toString(), clazz);
                    }
                }
            }
        }
        return list01;
    }

    public static Map<String,Object> toMapOnly(String strName ,Map<String , Object> params){
        String returnValue = "";
        Map<String,Object> result = new HashMap<>();
        if (!"".equals(returnValue)) {
            cn.hutool.json.JSONObject resJson = JSONUtil.parseObj(returnValue);
            if("0".equals(resJson.getJSONObject("__sys__").getStr("status"))){
                cn.hutool.json.JSONObject dataJson = resJson.getJSONObject("data");
                if(dataJson.containsKey("success")){
                    String successJson = dataJson.getStr("success");
                    if("true".equals(successJson)){
                        result = (Map<String,Object>)JSON.parseObject(dataJson.getJSONObject("data").toString(), Map.class);
                    }
                }else {
                    //没有success这个字段
                    result = (Map<String,Object>)JSON.parseObject(dataJson.getJSONObject("data").toString(), Map.class);
                }
            }
        }
        return result;
    }
}
