package com.student.pojo;

import lombok.Data;

import java.util.List;

@Data
public class item {
    private String id;
    private String title;
    private String fatherItem_id;
    private String text_id;
    private String type;
    private String father_id;
    private String sign;
    private List<String> textList;
}
