package com.student.pojo.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MessageVo {
    private String messageId;
    private Byte type;
    private String toGroupId;
    private String toUserId;
    private String fromUserId;
    private String message;
    private String fileType;
}
