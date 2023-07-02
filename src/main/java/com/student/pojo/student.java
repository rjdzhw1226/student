package com.student.pojo;

import com.student.util.excel.ExcelImport;
import lombok.Data;

@Data
public class student {
    @ExcelImport("学号")
    private String id;
    @ExcelImport("姓名")
    private String name;
    @ExcelImport("年级")
    private String grade;
    @ExcelImport("班级")
    private String grade_class;
    @ExcelImport("手机号")
    private String phone;
    @ExcelImport("年龄")
    private String age;
    @ExcelImport("性别")
    private String gender;
    @ExcelImport("状态")
    private String station;
    @ExcelImport("头像")
    private String url;

}