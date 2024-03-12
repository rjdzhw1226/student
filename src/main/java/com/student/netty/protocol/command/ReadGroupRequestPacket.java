package com.student.netty.protocol.command;

import java.util.Date;

public class ReadGroupRequestPacket extends Packet{
    private String groupId;
    private String messageId;
    private String toUserId;
    private String fromUserId;
    private Date readTime;
    private String readType;

    public ReadGroupRequestPacket() {
    }

    public ReadGroupRequestPacket(String groupId, String messageId, String toUserId, String fromUserId, Date readTime, String readType) {
        this.groupId = groupId;
        this.messageId = messageId;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.readTime = readTime;
        this.readType = readType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    @Override
    public String toString() {
        return "ReadGroupRequestPacket{" +
                "groupId='" + groupId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", readTime=" + readTime +
                ", readType='" + readType + '\'' +
                '}';
    }

    @Override
    public Byte getCommand() {
        return Command.READ_GROUP_REQUEST;
    }
}
