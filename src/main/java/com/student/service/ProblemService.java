package com.student.service;

import com.student.mapper.ProblemMapper;
import com.student.mapper.StudentMapper;
import com.student.pojo.item;
import com.student.pojo.student;
import com.student.pojo.vo.problemVo;
import com.student.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ProblemMapper problemMapper;

    public void saveItem(List<item> itemList, String fatherId){
        String itemId = CommonUtil.generateRandomString(16);

    }


    @Transactional
    public void saveProblem(problemVo proVo,String id) {
        String studentName = proVo.getName();
        student student = studentMapper.queryByName(studentName);
        String stId = student.getId();
        String name = proVo.getProblem().getName();
        Date time = proVo.getProblem().getDate();
        String middle_id = CommonUtil.generateRandomString(16);
        try{
            problemMapper.saveProblem(id,time,name,middle_id);
            problemMapper.saveConnect(id,stId);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
