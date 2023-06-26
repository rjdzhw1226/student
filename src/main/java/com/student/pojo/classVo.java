package com.student.pojo;

import lombok.Data;

import java.util.List;

@Data
public class classVo {
    private String id;
    private String father_id;
    private String teacher_id;
    private String name;
    private String teacherName;
    private Integer countC;
    private List<student> students;
}
