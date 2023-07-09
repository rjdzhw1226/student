package com.student.controller;

import com.student.pojo.item;
import com.student.pojo.problem;
import com.student.pojo.vo.problemVo;
import com.student.service.ProblemService;
import com.student.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {

    static final int COUNT = 10;

    @Autowired
    private ProblemService problemService;

    private static final ExecutorService MANY_EXECUTOR = Executors.newFixedThreadPool(COUNT);

    private static final ExecutorService SINGLE_EXECUTOR = Executors.newSingleThreadExecutor();

    @RequestMapping("/save")
    public Map<String,Object> save(@RequestBody problemVo pro){
        String id = CommonUtil.generateRandomString(16);
        List<item> itemList = pro.getProblem().getItemList();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info(Thread.currentThread().getName()+"存题目");
                problemService.saveItem(itemList,id);
            }
        });
        t1.start();
        problemService.saveProblem(pro,id);


        return null;
    }

}
