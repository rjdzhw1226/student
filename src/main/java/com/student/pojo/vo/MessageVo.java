package com.student.pojo.vo;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MessageVo {
    private String chatId;
    private Date sendTime;
    private String messageId;
    private Byte type;
    private String toGroupId;
    private String toUserId;
    private String fromUserId;
    private String message;
    /**
     * 已读 1 未读 0
     */
    private String readType;
    private String fileType;
}
