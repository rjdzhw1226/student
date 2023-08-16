package com.student.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class signDto {
    private String username;
    private Date date;
    private String content;
}
