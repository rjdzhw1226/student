package com.student.pojo.vo;

import lombok.Data;

@Data
public class subjectChooseVo {
    private String id;
    private String name;
    private String grade_max;
    private String grade_min;
    private String teacher_id;
    private String teacherName;
    private String count;
}
