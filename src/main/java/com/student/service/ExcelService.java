package com.student.service;

import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import com.student.pojo.student;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectLabelVo;
import com.student.pojo.vo.subjectVo;
import com.student.util.CallQueryThread;
import com.student.util.CommonUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<subject> getMultiCombineResult() {
        //开始时间
        long start = System.currentTimeMillis();
        //返回结果
        List<subject> result = new ArrayList<>();
        //查询数据库总数量
        int count = subjectMapper.queryCount();
        Map<String,String> splitMap = getSplitMap(count,SIZE);
        int bindex = 1;
        //Callable用于产生结果
        List<Callable<List>> tasks = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            //不同的线程用户处理不同分段的数据量，这样就达到了平均分摊查询数据的压力
            String[] nums = splitMap.get(String.valueOf(i * (count / SIZE))).split(":");
            int startNum = Integer.valueOf(String.valueOf(i * (count / SIZE)));
            int endNum = Integer.valueOf(nums[0]);
            Callable<List> qfe = new CallQueryThread(startNum, endNum);
            tasks.add(qfe);
            bindex += bindex;
        }
        try{
            //定义固定长度的线程池  防止线程过多，5就够用了
            ExecutorService executorService = Executors.newFixedThreadPool(SIZE);
            //Future用于获取结果
            List<Future<List>> futures=executorService.invokeAll(tasks);
            //处理线程返回结果
            if(futures!=null&&futures.size() > 0){
                for (Future<List> future:futures){
                    result.addAll(future.get());
                }
            }
            //关闭线程池，一定不能忘记
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

}
