package com.student.service;

import com.student.mapper.TeacherMapper;
import com.student.pojo.dto.teacherDto;
import com.student.pojo.page;
import com.student.pojo.pageBean;
import com.student.pojo.student;
import com.student.pojo.teacher;
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
        List<teacherDto> teacherDtos = teacherMapper.findAllTest(map);
        List<teacher> collect = teacherDtos.stream().map(TeacherService::teacherCommon).collect(Collectors.toList());
        return null;
    }

    private static teacher teacherCommon(teacherDto temp){
        teacher te = new teacher();
        te.setGrade(temp.getGrade());
        te.setGrade_class(temp.getGrade_class());
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
