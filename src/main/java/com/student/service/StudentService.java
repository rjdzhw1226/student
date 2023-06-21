package com.student.service;

import com.student.mapper.StudentMapper;
import com.student.pojo.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper mapper;
    public List<student> queryAll(){
        List<student> students = mapper.queryAll();
        return students;
    }

}
