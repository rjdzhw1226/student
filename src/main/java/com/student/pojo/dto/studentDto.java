package com.student.pojo.dto;

import com.student.util.excel.ExcelImport;
import lombok.Data;

@Data
public class studentDto {

    private String id;
    private String name;
    private String grade;
    private String grade_class;
    private String phone;
    private String age;
    private String gender;
}
