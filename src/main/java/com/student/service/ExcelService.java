package com.student.service;

import com.student.mapper.StudentMapper;
import com.student.pojo.student;
import com.student.util.CommonUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private StudentMapper studentMapper;

    @Transactional
    public void ExcelUpDone(Sheet sheet, int sta, int size) {
        try{
            List<student> students = new ArrayList<>();
            int rowStart = sheet.getFirstRowNum();
            if(sta == rowStart){
                sta = sta + 1;
            }
            for (int y = sta; y < size; y++) {
                Row row = sheet.getRow(y);
                String id = CommonUtil.getCellValue(row.getCell(0));
                String name = CommonUtil.getCellValue(row.getCell(1));
                String grade = CommonUtil.getCellValue(row.getCell(2));
                String grade_class = CommonUtil.getCellValue(row.getCell(3));
                String phone = CommonUtil.getCellValue(row.getCell(4));
                String age = CommonUtil.getCellValue(row.getCell(5));
                String gender = CommonUtil.getCellValue(row.getCell(6));
                String station = CommonUtil.getCellValue(row.getCell(7));
                String url = CommonUtil.getCellValue(row.getCell(8));

                student st = new student();
                st.setId(id);
                st.setName(name);
                st.setGrade(grade);
                st.setGrade_class(grade_class);
                st.setPhone(phone);
                st.setAge(age);
                st.setGender(gender);
                st.setStation(station);
                st.setUrl(url);

                students.add(st);
            }
            studentMapper.insertArticleTag(students);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
