package com.student.pojo.dto;

import lombok.Data;

import java.util.Date;
@Data
public class messageDto {
    private String userId;
    private Date start;
    private Date end;
}
