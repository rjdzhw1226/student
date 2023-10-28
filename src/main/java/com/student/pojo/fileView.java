package com.student.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class fileView {
    private int id;
    private String name;
    private String type;
    private List<fileView> childList;
}
