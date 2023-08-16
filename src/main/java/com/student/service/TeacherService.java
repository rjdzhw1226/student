package com.student.service;

import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.TeacherMapper;
import com.student.pojo.*;
import com.student.pojo.dto.*;
import com.student.pojo.vo.DateSignVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.poi.excel.sax.AttributeName.s;

@Service
public class TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MenuMapper menuMapper;

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
        String s1 = menuMapper.queryTreeById(byId.getGrade());
        String s2 = menuMapper.queryTreeById(byId.getGrade_class());
        List<student> students = studentMapper.queryByGradeClass(s1, s2);
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

    public List<DateSignVo> pinAll(String user) {
        List<DateSign> sign = teacherMapper.findSign(user);
        List<DateSignVo> collect = sign.stream().map(TeacherService::dateResolve).collect(Collectors.toList());
        return collect;
    }

    private static DateSignVo dateResolve(DateSign dateSign) {
        SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd" );
        DateSignVo dateSignVo = new DateSignVo();
        String format = sdf.format(dateSign.getDate());
        dateSignVo.setDate(format);
        dateSignVo.setContent(dateSign.getContent());
        return dateSignVo;
    }

    public Map<String, Object> savePin(DateSignDto dto) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd" );
        try {
            Date date = sdf.parse(dto.getDay());
            signDto sign = new signDto(dto.getUsername(),date,"已签到");
            teacherMapper.insert(sign);
            map.put("flag",true);
        } catch (ParseException e) {
            map.put("flag",false);
            throw new RuntimeException(e);
        }
        return map;
    }
}
