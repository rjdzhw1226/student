package com.student.pojo;

import lombok.Data;

import java.util.List;

@Data
public class menu {
    private String id;
    private String name;
    private List<menu_children> children;
}
