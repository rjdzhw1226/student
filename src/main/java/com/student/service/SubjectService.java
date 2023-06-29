package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import com.student.mapper.TeacherMapper;
import com.student.pojo.page;
import com.student.pojo.student;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
public class SubjectService {
    @Value("${file.readPath}")
    private String readFilePath;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SubjectMapper subjectMapper;


    /**
     * 同步
     */
    public void findByMultiThreads(){
        int timeSize = 10;
        CountDownLatch countDownLatch=new CountDownLatch(timeSize);
        for(int i = 0; i < timeSize; i++){
            threadPoolTaskExecutor.submit(new Runnable() { //线程池
                @Override
                public void run() {
                    try {
                        //数据内容查询和数据操作

                    } catch (Exception e) {
                        System.out.println("exception" + e.getMessage());
                    } finally {
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                }
            });
            try {
                countDownLatch.await(); //主线程等待所有的子任务结束，如果有一个子任务没有完成则会一直等待
                System.out.println("-------执行完毕-------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public page<subject> queryAllSubject(Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        //查询
        List<subject> list = new ArrayList<>();
        List<subjectVo> subjectVos = subjectMapper.queryAll(params);
        for (subjectVo subjectVo : subjectVos) {
            subject sb = new subject();
            String id = subjectVo.getId();
            String name = subjectVo.getName();
            String gradeMax = subjectVo.getGrade_max();
            String gradeMin = subjectVo.getGrade_min();
            String teacherId = subjectVo.getTeacher_id();
            String teacherName = teacherMapper.queryById(teacherId);
            List<String> strings = menuMapper.queryLabelList(gradeMin, gradeMax);
            sb.setGradeBetween(strings.get(0)+"~"+strings.get(strings.size()-1));
            sb.setTeacherName(teacherName);
            sb.setName(name);
            sb.setId(id);
            list.add(sb);
        }
        int count = subjectMapper.queryCount();
        page<subject> subjectpage = new page<>();
        subjectpage.setData(list);
        subjectpage.setTotal(count);
        return subjectpage;
    }


    public List<subject> queryAllSubjectGiveUp(Integer page, Integer size) throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        //查询 用于互不相干并行查询
        Callable getList = new Callable<List<subjectVo>>(){
            @Override
            public List<subjectVo> call() throws Exception  {
                List<subjectVo> subjectVos = subjectMapper.queryAll(params);
                return subjectVos;
            }
        };

        Callable getListDetail = new Callable<List<String>>(){
            @Override
            public List<String> call() throws Exception  {
                List<String> strings = subjectMapper.queryTeacherName();
                return strings;
            }
        };
        FutureTask<List<subjectVo>> getListBack = new FutureTask<List<subjectVo>>(getList);
        FutureTask<List<String>> getListDetailBack = new FutureTask<List<String>>(getList);
        threadPoolTaskExecutor.submit(getListBack);
        threadPoolTaskExecutor.submit(getListDetail);
        List<subjectVo> subjectVos = getListBack.get();
        List<String> strings = getListDetailBack.get();
        threadPoolTaskExecutor.shutdown();
        return null;
    }
}
