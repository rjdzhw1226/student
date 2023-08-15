package com.student.service;

import com.student.mapper.StudentMapper;
import com.student.mapper.TeacherMapper;
import com.student.pojo.dto.studentDto;
import com.student.pojo.dto.teacherDto;
import com.student.pojo.dto.teacherDtos;
import com.student.pojo.page;
import com.student.pojo.pageBean;
import com.student.pojo.student;
import com.student.pojo.teacher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    public TeacherService ts;

    /**
     * 查所有老师数据
     * @param page
     * @return
     */
    public page<teacher> findAll(pageBean page) {
        Map<String, Object> map = getMap(page);
        List<teacher> teachers = teacherMapper.findAll(map);
        int count = teacherMapper.count();
        page<teacher> teacherPage = new page<>();
        teacherPage.setData(teachers);
        teacherPage.setTotal(count);
        return teacherPage;
    }

    /**
     * 测试流写法
     * @param page
     * @return
     */
    public page<teacher> findAllTest(pageBean page){
        Map<String, Object> map = getMap(page);
        List<teacherDtos> teacherList = teacherMapper.findAllTest1(map);
        List<teacher> collect = teacherList.stream().map(t -> {
            teacher teach = ts.teacherCommon(t);
            return teach;
        }).collect(Collectors.toList());
        int count = teacherMapper.count();
        //List<teacherDto> teacherDtos = teacherMapper.findAllTest(map);
        //List<teacher> collect = teacherDtos.stream().map(TeacherService::teacherCommon).collect(Collectors.toList());
        page<teacher> result = new page<>();
        result.setTotal(count);
        result.setData(collect);
        return result;
    }

    public List<student> querySub(String id){
        teacherDto byId = teacherMapper.findById(id);

        List<student> students = studentMapper.queryByGradeClass(byId.getGrade(), byId.getGrade_class());
        return students;
    }

    public teacher teacherCommon(teacherDtos temp){
        String grade = temp.getGrade_id();
        String grade_class = temp.getClass_id();
        teacher te = new teacher();
        BeanUtils.copyProperties(temp,te);
        String s = teacherMapper.classFind(grade_class);
        String s1 = teacherMapper.gradeFind(grade);
        te.setGrade(s);
        te.setGrade_class(s1);
        return te;
    }
    private static Map<String, Object> getMap(pageBean page) {
        Map<String,Object> map = new HashMap<>();
        Integer page1 = page.getPage();
        Integer size1 = page.getSize();
        map.put("page", (page1 - 1) * size1);
        map.put("size", size1);
        return map;
    }
}
