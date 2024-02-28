package com.student.pojo;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class publishPo implements Serializable {
    private static final long serialVersionUID = 782703369813600963L;
    private Integer type;
    private String id;
    private String fromId;
    private String message;
    private ByteBuf buf;
}
