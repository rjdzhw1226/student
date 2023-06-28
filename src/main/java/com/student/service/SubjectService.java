package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private SubjectMapper subjectMapper;


}
