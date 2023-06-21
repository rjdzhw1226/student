package com.student.service;

import com.student.mapper.StudentMapper;
import com.student.pojo.page;
import com.student.pojo.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentMapper mapper;
    public page<student> queryAll(int page, int size){
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page-1)*size);
        params.put("size", size);
        List<student> students = mapper.queryAllLimit(params);
        int count = mapper.queryCount();
        page<student> studentpage = new page<>();
        studentpage.setData(students);
        studentpage.setTotal(count);
        return studentpage;
    }

    public student queryById(String id) {
        student student = mapper.queryById(id);
        return student;
    }

    public int updateStudent(String station, String id) {
        int i = mapper.update(station, id);
        return i;
    }

    public int add(student student) {
        int i = mapper.add(student);
        return i;
    }


    public int edit(student student,String idEdit) {
        String studentId = student.getId();
        String name = student.getName();
        String station = student.getStation();
        String grade = student.getGrade();
        String grade_class = student.getGrade_class();
        String gender = student.getGender();
        String age = student.getAge();
        String phone = student.getPhone();
        int i = mapper.edit(studentId,name, grade,grade_class,phone,age,gender,station,idEdit);
        return i;
    }
}
