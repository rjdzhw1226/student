package com.student.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class problem {
    private String id;
    private String name;
    private Date date;
    private List<item> itemList;
    private List<String> option;

}
