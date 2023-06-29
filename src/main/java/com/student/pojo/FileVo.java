package com.student.pojo;

import lombok.Data;

import java.util.List;

/**
 * 文件类
 */
@Data
public class FileVo {
    private String name;
    private int type;
    private List<String[]> listString;
    private List<List<String>> downloadList;
    private String[] title;
}