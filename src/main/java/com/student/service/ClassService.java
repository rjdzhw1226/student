package com.student.service;

import com.student.mapper.*;
import com.student.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ClassService {

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Transactional
    public classVo query(String id) {
        classVo classVo = new classVo();
        classDto query = classMapper.query(id);
        String id1 = query.getId();
        String father_id = query.getFather_id();
        String teacher_id = query.getTeacher_id();
        Integer countC = query.getCount();
        String labelGrade = menuMapper.queryTreeById(id1);
        String labelGradeClass = menuMapper.queryTreeTwoId(father_id);
        String teacherName = teacherMapper.queryById(teacher_id);
        List<student> students = studentMapper.queryByGradeClass(labelGradeClass, labelGrade);
        classVo.setName(labelGradeClass+labelGrade);
        classVo.setId(id1);
        classVo.setFather_id(teacher_id);
        classVo.setTeacherName(teacherName);
        classVo.setTeacher_id(teacher_id);
        classVo.setStudents(students);
        classVo.setCountC(countC);
        return classVo;
    }

    @Transactional
    public void findStudentByClass(String name) {
        int timeSize = 1;  // 获取需要的线程数
        final String nameStr = name;
        CountDownLatch countDownLatch=new CountDownLatch(timeSize);
        for(int i = 0; i < timeSize; i++){
            threadPoolTaskExecutor.submit(new Runnable() {   //线程池
                @Override
                public void run() {
                    try {
                        //数据内容查询和数据操作
                        extractedAdd(nameStr);
                    }catch (Exception e) {
                        System.out.println("exception"+e.getMessage());
                    }finally{
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                }
            });

        }
        try {
            countDownLatch.await();    //主线程等待所有的子任务结束，如果有一个子任务没有完成则会一直等待
            System.out.println("-------执行完毕-------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractedAdd(String name) {
        List<student> students = studentMapper.queryAll();
        for (student student : students) {
            String id = student.getId();
            List<score> scores = scoreMapper.queryScore(id);
            for (score score : scores) {
                String subId = score.getSubId();
                String querySubNameById = scoreMapper.querySubNameById(subId);
                System.out.println(querySubNameById);
            }
        }
    }
}
