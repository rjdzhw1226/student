package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

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
}
