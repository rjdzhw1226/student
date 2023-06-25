package com.student.pojo;

import lombok.Data;

import java.util.List;
@Data
public class tree {
    private String id;
    private String label;
    private List<tree> children;
}
