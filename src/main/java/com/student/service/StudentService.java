package com.student.service;

import com.student.mapper.ClassMapper;
import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.pojo.dto.classDto;
import com.student.pojo.page;
import com.student.pojo.student;
import com.student.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentMapper mapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Transactional
    public page<student> queryAll(int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
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

    @Transactional
    public int add(student student) {
        String grade = student.getGrade();
        String gradeClass = student.getGrade_class();
        String labelId = menuMapper.queryTreeTwoByLabel(grade,gradeClass);
        if (CommonUtil.isNotNullJson(labelId)) {
            classDto dto = classMapper.query(labelId);
            int i1 = dto.getCount() + 1;
            classMapper.editCount(i1,labelId);
        }else{
            return 0;
        }
        int i = mapper.add(student);
        return i;
    }

    @Transactional
    public int edit(student student, String idEdit) {
        String labelIdC = common(idEdit);
        String studentId = student.getId();
        String name = student.getName();
        String station = student.getStation();
        String grade = student.getGrade();
        String grade_class = student.getGrade_class();
        String gender = student.getGender();
        String age = student.getAge();
        String phone = student.getPhone();
        String url = student.getUrl();
        String labelId = menuMapper.queryTreeTwoByLabel(grade,grade_class);
        if (CommonUtil.isNotNullJson(labelId) && CommonUtil.isNotNullJson(labelIdC)){
            classDto dto = classMapper.query(labelId);
            classDto dtoBefore = classMapper.query(labelIdC);
            int i1 = dto.getCount() + 1;
            int i = dtoBefore.getCount() - 1;
            classMapper.editCount(i1,labelId);
            classMapper.editCount(i,labelIdC);
        }else{
            return 0;
        }
        int i = mapper.edit(studentId, name, grade, grade_class, phone, age, gender, station, url, idEdit);
        return i;
    }

    @Transactional
    public page<student> queryLike(String queryStr, Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        page = (page - 1) * size;
        size = size;
        List<student> students = mapper.queryLike(queryStr, page, size);
        int count = mapper.queryCountLike(queryStr);
        page<student> studentpage = new page<>();
        studentpage.setData(students);
        studentpage.setTotal(count);
        return studentpage;
    }

    public int deleteIds(List<String> array) {
        int count = 0;
        for (String id : array) {
            String labelId = common(id);
            if (CommonUtil.isNotNullJson(labelId)){
                classDto dto = classMapper.query(labelId);
                int i1 = dto.getCount() - 1;
                classMapper.editCount(i1,labelId);
                int i = mapper.deleteId(id);
                count = count + i;
            }else{
                return 0;
            }
        }
        return count;
    }
    private String common(String id){
        student student = mapper.queryById(id);
        String grade = student.getGrade();
        String grade_class = student.getGrade_class();
        String labelId = menuMapper.queryTreeTwoByLabel(grade,grade_class);
        return labelId;
    }

    public int deleteId(String id, String name) {
        String grade = name.split(",")[0];
        String gradeClass = name.split(",")[1];
        String labelId = menuMapper.queryTreeTwoByLabel(grade,gradeClass);
        if (CommonUtil.isNotNullJson(labelId)){
            classDto dto = classMapper.query(labelId);
            int i1 = dto.getCount() - 1;
            classMapper.editCount(i1,labelId);
        }else{
            return 0;
        }
        int i = mapper.deleteId(id);
        return i;
    }
}
